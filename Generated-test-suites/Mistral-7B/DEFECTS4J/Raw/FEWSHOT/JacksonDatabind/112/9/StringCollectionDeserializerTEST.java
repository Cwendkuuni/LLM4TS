package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

public class StringCollectionDeserializerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final JsonDeserializer<String> stringDeserializer = mock(JsonDeserializer.class);
    private final ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
    private final JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);

    @Test
    public void testDefaultConstruction() {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null, null, null, null);
        assertEquals(String.class, deserializer.getType());
        assertNull(deserializer._valueDeserializer);
        assertNull(deserializer._valueInstantiator);
        assertNull(deserializer._delegateDeserializer);
    }

    @Test
    public void testSimpleConstruction() {
        JavaType collectionType = mock(JavaType.class);
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, valueDeserializer, valueInstantiator);
        assertEquals(collectionType, deserializer._containerType);
        assertEquals(valueDeserializer, deserializer._valueDeserializer);
        assertEquals(valueInstantiator, deserializer._valueInstantiator);
        assertNull(deserializer._delegateDeserializer);
    }

    @Test
    public void testContextualDeserialization() throws JsonMappingException, IOException {
        JavaType collectionType = mock(JavaType.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(collectionType, valueInstantiator, delegateDeserializer, valueDeserializer, nullValueProvider, null);

        BeanDescription propertyDescription = mock(BeanDescription.class);
        when(propertyDescription.getBeanClass()).thenReturn(Collection.class);
        when(propertyDescription.getName()).thenReturn(PropertyName.construct(new PropertyName("test")));

        DeserializationContext context = mock(DeserializationContext.class);
        when(context.handleSecondaryContextualization(valueDeserializer, any(PropertyName.class), any(JavaType.class))).thenReturn(valueDeserializer);

        when(valueDeserializer.createContextual(eq(context), any(BeanProperty.class))).thenReturn(valueDeserializer);
        when(valueDeserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("test");

        Collection<String> result = deserializer.createContextual(context, new PropertyName("test"));
        assertEquals(ArrayList.class, result.getClass());
        assertEquals(1, result.size());
        assertEquals("test", result.get(0));
    }

    @Test
    public void testDeserialize() throws JsonMappingException, IOException {
        StringCollectionDeserializer deserializer = getStringCollectionDeserializer();

        Collection<String> collection = new ArrayList<>();
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.nextToken()).thenReturn(JsonToken.START_ARRAY);
        when(parser.nextTextValue()).thenReturn("test1");
        when(parser.nextTextValue()).thenReturn("test2");

        Collection<String> result = deserializer.deserialize(parser, context);
        assertEquals(2, result.size());
        assertEquals("test1", result.get(0));
        assertEquals("test2", result.get(1));

        verify(parser).nextTextValue();
        verify(parser).nextTextValue();
        verify(parser).nextToken();
    }

    @Test
    public void testDeserializeUsingCustom() throws JsonMappingException, IOException {
        StringCollectionDeserializer deserializer = getStringCollectionDeserializer();
        StringDeserializer stringDeserializerSpy = spy(stringDeserializer);

        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);

        when(parser.nextTextValue()).thenReturn(null);
        when(parser.nextTextValue()).thenReturn("test");

        Collection<String> result = deserializer.deserializeUsingCustom(parser, context, new ArrayList<>(), stringDeserializerSpy);
        assertEquals(1, result.size());
        assertEquals("test", result.get(0));

        verify(parser, times(2)).nextTextValue();
        verify(parser).nextToken();
    }

    @Test
    public void testIsCachable() {
        StringCollectionDeserializer deserializer = getStringCollectionDeserializer();
        JsonDeserializer<String> valueDeserializer = mock(JsonDeserializer.class);
        JsonDeserializer<Object> delegateDeserializer = mock(JsonDeserializer.class);

        assertTrue(deserializer.isCachable());
        deserializer._valueDeserializer = valueDeserializer;
        assertFalse(deserializer.isCachable());
        deserializer._delegateDeserializer = delegateDeserializer;
        assertFalse(deserializer.isCachable());
    }

    private StringCollectionDeserializer getStringCollectionDeserializer() {
        JavaType collectionType = mock(JavaType.class);
        ValueInstantiator valueInstantiator = mock(ValueInstantiator.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);

        return new StringCollectionDeserializer(collectionType, valueInstantiator, null, null, nullValueProvider, null);
    }

}