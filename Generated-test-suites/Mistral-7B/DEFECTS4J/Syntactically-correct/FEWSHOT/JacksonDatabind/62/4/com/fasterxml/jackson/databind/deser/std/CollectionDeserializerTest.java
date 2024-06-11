package org.apache.commons.cli;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

/**
 * Test class for {@link CollectionDeserializer}.
 */
@JacksonStdImpl
public class CollectionDeserializerTest {

    @SuppressWarnings("unchecked")
    private static <T> Collection<T> createCollectionDeserializer(JavaType collectionType,
            JsonDeserializer<T> valueDeserializer, TypeDeserializer valueTypeDeserializer,
            ValueInstantiator valueInstantiator, Boolean unwrapSingle) {
        CollectionDeserializer deserializer = new CollectionDeserializer(collectionType, valueDeserializer,
                valueTypeDeserializer, valueInstantiator, null, unwrapSingle);
        return deserializer;
    }

    @Test
    public void testCreateContextual() throws Exception {
        // Test creation of contextual instances

        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        TypeDeserializer stringTypeDeserializer = mock(TypeDeserializer.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        CollectionDeserializer deserializer = createCollectionDeserializer(javaType, stringDeserializer,
                stringTypeDeserializer, valueInstantiator, null);

        DeserializationContext context = mock(DeserializationContext.class);
        BeanProperty beanProperty = mock(BeanProperty.class);

        CollectionDeserializer contextualDeserializer = deserializer.createContextual(context, beanProperty);
        assertEquals(deserializer, contextualDeserializer);

        verify(valueInstantiator, times(1)).canCreateUsingDelegate();
        if (valueInstantiator.canCreateUsingDelegate()) {
            JavaType delegateType = valueInstantiator.getDelegateType(context.getConfig());
            JsonDeserializer<Object> delegateDeserializer = findDeserializer(context, delegateType, beanProperty);
            assertEquals(stringDeserializer, delegateDeserializer);
        }

        Boolean unwrapSingle = findFormatFeature(context, beanProperty, javaType.getRawClass(),
                JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
        JsonDeserializer<Object> valueDeserializer = stringDeserializer;
        TypeDeserializer valueTypeDeserializer = stringTypeDeserializer;

        CollectionDeserializer withResolvedDeserializer = deserializer.withResolved(valueDeserializer, valueDeserializer,
                valueTypeDeserializer, unwrapSingle);
        assertEquals(deserializer, withResolvedDeserializer);
    }

    @Test
    public void testDeserialize() throws Exception {
        // Test deserialization from JsonParser

        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        TypeDeserializer stringTypeDeserializer = mock(TypeDeserializer.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        CollectionDeserializer deserializer = createCollectionDeserializer(javaType, stringDeserializer,
                stringTypeDeserializer, valueInstantiator, null);

        ObjectReader objectReader = new ObjectMapper().readerFor(javaType);
        JsonParser jsonParser = objectReader.getParser();

        DeserializationContext context = mock(DeserializationContext.class);
        Collection<Object> collection = new ArrayList<>();

        when(valueInstantiator.createUsingDefault(context)).thenReturn(collection);

        Collection<Object> deserializedCollection = deserializer.deserialize(jsonParser, context);
        assertEquals(collection, deserializedCollection);

        verifyZeroInteractions(context, jsonParser);

        // Test deserialization from JsonParser with delegate deserializer

        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        deserializer = createCollectionDeserializer(javaType, stringDeserializer, stringTypeDeserializer,
                valueInstantiator, delegateDeserializer);

        jsonParser.setCurrentValue(collection);

        when(delegateDeserializer.deserialize(jsonParser, context)).thenReturn("delegate");

        deserializedCollection = deserializer.deserialize(jsonParser, context);
        assertEquals(List.of("delegate"), deserializedCollection);

        verify(delegateDeserializer, times(1)).deserialize(jsonParser, context);
    }

    @Test
    public void testDeserializeWithType() throws Exception {
        // Test deserialization with TypeDeserializer

        JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
        JsonDeserializer<String> stringDeserializer = new StringDeserializer();
        TypeDeserializer stringTypeDeserializer = mock(TypeDeserializer.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        CollectionDeserializer deserializer = createCollectionDeserializer(javaType, stringDeserializer,
                stringTypeDeserializer, valueInstantiator, null);

        ObjectReader objectReader = new ObjectMapper().readerFor(javaType);
        JsonParser jsonParser = objectReader.getParser();

        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(stringTypeDeserializer.deserializeTypedFromArray(jsonParser, mock(DeserializationContext.class)))
                .thenReturn(List.of("typed"));
        when(typeDeserializer.deserializeTypedFromArray(jsonParser, mock(DeserializationContext.class)))
                .thenReturn(List.of("typed"));

        Collection<Object> collection = new ArrayList<>();

        when(valueInstantiator.createUsingDefault(mock(DeserializationContext.class)))
                .thenReturn(collection);

        Collection<Object> deserializedCollection = deserializer.deserializeWithType(jsonParser, mock(DeserializationContext.class),
                typeDeserializer);
        assertEquals(List.of("typed"), deserializedCollection);

        verifyZeroInteractions(context, jsonParser);
        verify(typeDeserializer, times(1)).deserializeTypedFromArray(jsonParser, mock(DeserializationContext.class));
    }
}

class StringDeserializer extends StdDeserializer<String> {
    public StringDeserializer() {
        this(null);
    }

    protected StringDeserializer(Class<String> t) {
        super(t);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsString();
    }
}