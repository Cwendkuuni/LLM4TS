import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.*;

public class StringCollectionDeserializerTest {

    private static final JavaType COLLECTION_TYPE = Mockito.mock(JavaType.class);
    private static final JsonDeserializer<String> VALUE_DESERIALIZER = Mockito.mock(JsonDeserializer.class);
    private static final ValueInstantiator VALUE_INSTANTIATOR = Mockito.mock(ValueInstantiator.class);
    private static final JsonDeserializer<Object> DELEGATE_DESERIALIZER = Mockito.mock(JsonDeserializer.class);
    private static final NullValueProvider NULL_VALUE_PROVIDER = Mockito.mock(NullValueProvider.class);

    @Test
    public void testDeserialize() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(COLLECTION_TYPE,
                VALUE_DESERIALIZER, VALUE_INSTANTIATOR);

        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Collection<String> result = new ArrayList<>();

        Mockito.when(parser.isExpectedStartArrayToken()).thenReturn(true);
        Mockito.when(VALUE_DESERIALIZER.deserialize(parser, ctxt)).thenReturn("value1");
        Mockito.when(parser.nextTextValue()).thenReturn("value2").thenReturn(null);
        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.END_ARRAY);

        deserializer.deserialize(parser, ctxt, result);

        Assert.assertEquals(Arrays.asList("value1", "value2"), result);
        Mockito.verify(parser, Mockito.times(2)).nextTextValue();
        Mockito.verify(VALUE_DESERIALIZER, Mockito.times(2)).deserialize(parser, ctxt);
    }

    @Test
    public void testDeserializeWithDelegateDeserializer() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(COLLECTION_TYPE,
                VALUE_INSTANTIATOR, DELEGATE_DESERIALIZER, VALUE_DESERIALIZER, NULL_VALUE_PROVIDER, null);

        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Collection<String> result = new ArrayList<>();

        Mockito.when(deserializer.getDelegateDeserializer()).thenReturn(DELEGATE_DESERIALIZER);
        Mockito.when(DELEGATE_DESERIALIZER.deserialize(parser, ctxt)).thenReturn(Collections.singletonList("value"));

        deserializer.deserialize(parser, ctxt, result);
        
        Assert.assertEquals(Collections.singletonList("value"), result);
        Mockito.verify(deserializer).getDelegateDeserializer();
        Mockito.verify(DELEGATE_DESERIALIZER).deserialize(parser, ctxt);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(COLLECTION_TYPE,
                VALUE_DESERIALIZER, VALUE_INSTANTIATOR);

        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);

        Mockito.when(typeDeserializer.deserializeTypedFromArray(parser, ctxt)).thenReturn(Collections.singletonList("value"));

        Object result = deserializer.deserializeWithType(parser, ctxt, typeDeserializer);

        Assert.assertEquals(Collections.singletonList("value"), result);
        Mockito.verify(typeDeserializer).deserializeTypedFromArray(parser, ctxt);
    }

    @Test
    public void testHandleNonArrayWithUnwrapSingleTrue() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(COLLECTION_TYPE,
                VALUE_DESERIALIZER, VALUE_INSTANTIATOR);
        deserializer.setUnwrapSingle(true);

        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Collection<String> result = new ArrayList<>();

        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(VALUE_DESERIALIZER.deserialize(parser, ctxt)).thenReturn("value");

        deserializer.handleNonArray(parser, ctxt, result);

        Assert.assertEquals(Collections.singletonList("value"), result);
        Mockito.verify(parser).getCurrentToken();
        Mockito.verify(VALUE_DESERIALIZER).deserialize(parser, ctxt);
    }

    @Test
    public void testHandleNonArrayWithUnwrapSingleNullAndAcceptSingleValueAsArray() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(COLLECTION_TYPE,
                VALUE_DESERIALIZER, VALUE_INSTANTIATOR);

        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Collection<String> result = new ArrayList<>();

        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        Mockito.when(VALUE_DESERIALIZER.deserialize(parser, ctxt)).thenReturn("value");

        deserializer.handleNonArray(parser, ctxt, result);

        Assert.assertEquals(Collections.singletonList("value"), result);
        Mockito.verify(parser).getCurrentToken();
        Mockito.verify(VALUE_DESERIALIZER).deserialize(parser, ctxt);
    }

    @Test
    public void testHandleNonArrayWithUnwrapSingleFalseAndTypeMismatch() throws IOException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(COLLECTION_TYPE,
                VALUE_DESERIALIZER, VALUE_INSTANTIATOR);

        JsonParser parser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Collection<String> result = new ArrayList<>();

        Mockito.when(parser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);

        deserializer.handleNonArray(parser, ctxt, result);

        Assert.assertNull(result);
        Mockito.verify(parser).getCurrentToken();
        Mockito.verify(ctxt).handleUnexpectedToken(StringCollectionDeserializer.class, parser);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(COLLECTION_TYPE,
                VALUE_INSTANTIATOR, null, null, NULL_VALUE_PROVIDER, null);

        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);

        JsonDeserializer<Object> contextualValueDeser = Mockito.mock(JsonDeserializer.class);

        Mockito.when(ctxt.getConfig()).thenReturn(ObjectMapper.builder().build().getDeserializationConfig());
        Mockito.when(ctxt.findContextualValueDeserializer(VALUE_INSTANTIATOR.getType(ctxt.getConfig()), property))
                .thenReturn(contextualValueDeser);

        JsonDeserializer<?> result = deserializer.createContextual(ctxt, property);

        Assert.assertNotNull(result);
        Assert.assertNotEquals(result, deserializer);
        Mockito.verify(ctxt).findContextualValueDeserializer(VALUE_INSTANTIATOR.getType(ctxt.getConfig()), property);
        Mockito.verify(ctxt, Mockito.never()).handleSecondaryContextualization(Mockito.any(), Mockito.any(), Mockito.any());
    }
    
    // Add more test cases for other methods in the class

}