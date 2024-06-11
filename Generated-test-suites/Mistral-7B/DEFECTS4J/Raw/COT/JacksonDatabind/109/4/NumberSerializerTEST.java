import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NumberSerializerTest {

    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        jsonParser = mock(JsonParser.class);
        jsonGenerator = mock(JsonGenerator.class);
        serializerProvider = new SimpleSerializerProvider().setEmptyBeanContext();
    }

    @Test
    public void testInstanceMethod() {
        NumberSerializer serializer = NumberSerializer.instance;
        Assert.assertNotNull(serializer);
        Assert.assertEquals(Number.class, serializer.handledType());
    }

    @Test
    public void testIsIntMethod() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        Assert.assertTrue(serializer.isInt());

        serializer = new NumberSerializer(BigInteger.class);
        Assert.assertTrue(serializer.isInt());

        serializer = new NumberSerializer(Double.class);
        Assert.assertFalse(serializer.isInt());

        serializer = new NumberSerializer(Float.class);
        Assert.assertFalse(serializer.isInt());

        serializer = new NumberSerializer(BigDecimal.class);
        Assert.assertFalse(serializer.isInt());
    }

    @Test
    public void testCreateContextualMethod() throws JsonMappingException, JsonGenerationException {
        ObjectMapper objectMapper = new ObjectMapper();
        Object node = objectMapper.readValue("{\"number\": 123}", Object.class);
        AnnotationIntrospector introspector = mock(AnnotationIntrospector.class);
        when(introspector.findFormatAnnotation(JsonFormat.class, NumberSerializer.class)).thenReturn(null);

        JsonSerializer<Number> serializer = new NumberSerializer(Number.class);
        JsonSerializer<Number> contextualSerializer = serializer.createContextual(serializerProvider, null);
        Assert.assertEquals(serializer, contextualSerializer);

        when(introspector.findFormatAnnotation(JsonFormat.class, NumberSerializer.class)).thenReturn(new JsonFormat.Value());
        contextualSerializer = serializer.createContextual(serializerProvider, null);
        Assert.assertNotEquals(serializer, contextualSerializer);
    }

    @Test
    public void testSerializeBigDecimalMethod() throws JsonMappingException, JsonGenerationException {
        BigDecimal bigDecimal = new BigDecimal("123.45");
        when(jsonGenerator.writeNumber(bigDecimal)).thenReturn(null);

        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        serializer.serialize(bigDecimal, jsonGenerator, serializerProvider);

        Assert.assertNull(jsonGenerator.findPropertyId());
        Assert.assertEquals(JsonNodeType.NUMBER, jsonGenerator.getCurrentValueAsToken().getNodeType());
    }

    @Test
    public void testSerializeBigIntegerMethod() throws JsonMappingException, JsonGenerationException {
        BigInteger bigInteger = new BigInteger("123");
        when(jsonGenerator.writeNumber(bigInteger)).thenReturn(null);

        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        serializer.serialize(bigInteger, jsonGenerator, serializerProvider);

        Assert.assertNull(jsonGenerator.findPropertyId());
        Assert.assertEquals(JsonNodeType.NUMBER, jsonGenerator.getCurrentValueAsToken().getNodeType());
    }

    @Test
    public void testSerializeLongMethod() throws JsonMappingException, JsonGenerationException {
        long longValue = 123;
        when(jsonGenerator.writeNumber(longValue)).thenReturn(null);

        NumberSerializer serializer = new NumberSerializer(Long.class);
        serializer.serialize(longValue, jsonGenerator, serializerProvider);

        Assert.assertNull(jsonGenerator.findPropertyId());
        Assert.assertEquals(JsonNodeType.NUMBER, jsonGenerator.getCurrentValueAsToken().getNodeType());
    }

    @Test
    public void testSerializeDoubleMethod() throws JsonMappingException, JsonGenerationException {
        double doubleValue = 123.45;
        when(jsonGenerator.writeNumber(doubleValue)).thenReturn(null);

        NumberSerializer serializer = new NumberSerializer(Double.class);
        serializer.serialize(doubleValue, jsonGenerator, serializerProvider);

        Assert.assertNull(jsonGenerator.findPropertyId());
        Assert.assertEquals(JsonNodeType.NUMBER, jsonGenerator.getCurrentValueAsToken().getNodeType());
    }

    @Test
    public void testSerializeFloatMethod() throws JsonMappingException, JsonGenerationException {
        float floatValue = (float) 123.45;
        when(jsonGenerator.writeNumber(floatValue)).thenReturn(null);

        NumberSerializer serializer = new NumberSerializer(Float.class);
        serializer.serialize(floatValue, jsonGenerator, serializerProvider);

        Assert.assertNull(jsonGenerator.findPropertyId());
        Assert.assertEquals(JsonNodeType.NUMBER, jsonGenerator.getCurrentValueAsToken().getNodeType());
    }

    @Test
    public void testSerializeIntegerMethod() throws JsonMappingException, JsonGenerationException {
        int integerValue = 123;
        when(jsonGenerator.writeNumber(integerValue)).thenReturn(null);

        NumberSerializer serializer = new NumberSerializer(Integer.class);
        serializer.serialize(integerValue, jsonGenerator, serializerProvider);

        Assert.assertNull(jsonGenerator.findPropertyId());
        Assert.assertEquals(JsonNodeType.NUMBER, jsonGenerator.getCurrentValueAsToken().getNodeType());
    }

    @Test
    public void testSerializeByteMethod() throws JsonMappingException, JsonGenerationException {
        byte byteValue = 123;
        when(jsonGenerator.writeNumber(byteValue)).thenReturn(null);

        NumberSerializer serializer = new NumberSerializer(Byte.class);
        serializer.serialize(byteValue, jsonGenerator, serializerProvider);

        Assert.assertNull(jsonGenerator.findPropertyId());
        Assert.assertEquals(JsonNodeType.NUMBER, jsonGenerator.getCurrentValueAsToken().getNodeType());
    }

    @Test
    public void testSerializeShortMethod() throws JsonMappingException, JsonGenerationException {
        short shortValue = 123;
        when(jsonGenerator.writeNumber(shortValue)).thenReturn(null);

        NumberSerializer serializer = new NumberSerializer(Short.class);
        serializer.serialize(shortValue, jsonGenerator, serializerProvider);

        Assert.assertNull(jsonGenerator.findPropertyId());
        Assert.assertEquals(JsonNodeType.NUMBER, jsonGenerator.getCurrentValueAsToken().getNodeType());
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerializeUnsupportedNumberTypeMethod() throws JsonMappingException, JsonGenerationException {
        Object unsupportedNumber = new Object();
        NumberSerializer serializer = new NumberSerializer(Number.class);
        serializer.serialize(unsupportedNumber, jsonGenerator, serializerProvider);
        Assert.fail();
    }
}