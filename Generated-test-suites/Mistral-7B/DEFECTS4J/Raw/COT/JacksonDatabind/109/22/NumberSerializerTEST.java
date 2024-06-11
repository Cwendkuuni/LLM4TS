import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Random;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class NumberSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    // Test for constructor
    @Test
    public void testNumberSerializerConstructor() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        assertNotNull(numberSerializer);
    }

    // Test for createContextual method
    @Test
    public void testCreateContextualMethod() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonParser mockParser = mock(JsonParser.class);
        BeanProperty mockProperty = mock(BeanProperty.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        JsonFormat.Value mockFormat = mock(JsonFormat.Value.class);

        when(mockParser.getCodec()).thenReturn(mapper.getCodec());
        when(mockProperty.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(mockFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);

        JsonSerializer<Number> result = numberSerializer.createContextual(mockProvider, mockProperty);
        assertEquals(ToStringSerializer.instance, result);

        verify(mockParser).getCodec();
        verify(mockProperty).getShape();
        verify(mockFormat).getShape();
    }

    // Test for serialize method with BigDecimal
    @Test
    public void testSerializeMethodWithBigDecimal() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        BigDecimal bigDecimal = new BigDecimal("123.45");
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        when(mockGenerator.getOutputFinished()).thenReturn(true);

        numberSerializer.serialize(bigDecimal, mockGenerator, mockProvider);

        verify(mockGenerator).writeNumber(bigDecimal);
    }

    // Test for serialize method with BigInteger
    @Test
    public void testSerializeMethodWithBigInteger() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        BigInteger bigInteger = new BigInteger("1234567890123456789012345678901234567890");
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        when(mockGenerator.getOutputFinished()).thenReturn(true);

        numberSerializer.serialize(bigInteger, mockGenerator, mockProvider);

        verify(mockGenerator).writeNumber(bigInteger);
    }

    // Test for serialize method with Long
    @Test
    public void testSerializeMethodWithLong() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Long longValue = 123L;
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        when(mockGenerator.getOutputFinished()).thenReturn(true);

        numberSerializer.serialize(longValue, mockGenerator, mockProvider);

        verify(mockGenerator).writeNumber(longValue.longValue());
    }

    // Test for serialize method with Double
    @Test
    public void testSerializeMethodWithDouble() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Double doubleValue = 123.45;
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        when(mockGenerator.getOutputFinished()).thenReturn(true);

        numberSerializer.serialize(doubleValue, mockGenerator, mockProvider);

        verify(mockGenerator).writeNumber(doubleValue.doubleValue());
    }

    // Test for serialize method with Float
    @Test
    public void testSerializeMethodWithFloat() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Float floatValue = 123.45f;
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        when(mockGenerator.getOutputFinished()).thenReturn(true);

        numberSerializer.serialize(floatValue, mockGenerator, mockProvider);

        verify(mockGenerator).writeNumber(floatValue.floatValue());
    }

    // Test for serialize method with Integer, Byte, or Short
    @Test
    public void testSerializeMethodWithIntegerByteOrShort() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        Integer integerValue = 123;
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        when(mockGenerator.getOutputFinished()).thenReturn(true);

        numberSerializer.serialize(integerValue, mockGenerator, mockProvider);

        verify(mockGenerator).writeNumber(integerValue.intValue());
    }

    // Test for serialize method with other types
    @Test(expected = JsonProcessingException.class)
    public void testSerializeMethodWithOtherTypes() throws JsonProcessingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        String stringValue = "abc";
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        numberSerializer.serialize(stringValue, mockGenerator, mockProvider);
    }

    // Test for getSchema method
    @Test
    public void testGetSchemaMethod() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonNode expectedSchema = createSchemaNode("number", true);
        JsonNode resultSchema = numberSerializer.getSchema(mapper.getSerializerProvider(), Number.class);

        assertEquals(expectedSchema, resultSchema);
    }

    // Test for acceptJsonFormatVisitor method with int format
    @Test
    public void testAcceptJsonFormatVisitorMethodWithIntFormat() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        ObjectNode mockNode = mock(ObjectNode.class);
        JavaType mockTypeHint = mock(JavaType.class);

        numberSerializer.acceptJsonFormatVisitor(mockNode, mockTypeHint);

        verify(mockNode).acceptNumberFormat(eq(mockTypeHint), any(JsonParser.NumberType.class));
    }

    // Test for acceptJsonFormatVisitor method with BigDecimal format
    @Test
    public void testAcceptJsonFormatVisitorMethodWithBigDecimalFormat() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        ObjectNode mockNode = mock(ObjectNode.class);
        JavaType mockTypeHint = mock(JavaType.class);

        when(mockTypeHint.getRawClass()).thenReturn(BigDecimal.class);

        numberSerializer.acceptJsonFormatVisitor(mockNode, mockTypeHint);

        verify(mockNode).expectNumberFormat(eq(mockTypeHint), eq(JsonParser.NumberType.BIG_DECIMAL));
    }

    // Test for acceptJsonFormatVisitor method with other format
    @Test
    public void testAcceptJsonFormatVisitorMethodWithOtherFormat() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        ObjectNode mockNode = mock(ObjectNode.class);
        JavaType mockTypeHint = mock(JavaType.class);

        when(mockTypeHint.getRawClass()).thenReturn(String.class);

        numberSerializer.acceptJsonFormatVisitor(mockNode, mockTypeHint);

        // No verification expected since it should not be called
    }

    // Test edge cases and exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeMethodWithNullNumber() throws IOException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        numberSerializer.serialize(null, mockGenerator, mockProvider);
    }

    @Test(expected = JsonGenerationException.class)
    public void testSerializeMethodWithGeneratorException() throws IOException, JsonProcessingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        BigDecimal bigDecimal = new BigDecimal("123.45");
        JsonGenerator mockGenerator = mock(JsonGenerator.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);

        when(mockGenerator.writeNumber(bigDecimal)).thenThrow(JsonGenerationException.class);

        try {
            numberSerializer.serialize(bigDecimal, mockGenerator, mockProvider);
        } catch (IOException e) {
            throw new JsonProcessingException(e);
        }
    }

    @Test
    public void testCreateContextualMethodWithNullProperty() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonParser mockParser = mock(JsonParser.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        BeanProperty mockProperty = null;

        assertNull(numberSerializer.createContextual(mockProvider, mockProperty));
    }

    @Test
    public void testCreateContextualMethodWithNullParser() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider mockProvider = mock(SerializerProvider.class);
        BeanProperty mockProperty = mock(BeanProperty.class);
        JsonParser mockParser = null;

        assertNull(numberSerializer.createContextual(mockProvider, mockProperty));
    }

    @Test
    public void testCreateContextualMethodWithNullProvider() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonParser mockParser = mock(JsonParser.class);
        BeanProperty mockProperty = mock(BeanProperty.class);
        SerializerProvider mockProvider = null;

        assertNull(numberSerializer.createContextual(mockProvider, mockProperty));
    }

    @Test
    public void testAcceptJsonFormatVisitorMethodWithNullNode() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JavaType mockTypeHint = mock(JavaType.class);

        numberSerializer.acceptJsonFormatVisitor(null, mockTypeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitorMethodWithNullTypeHint() throws JsonMappingException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        ObjectNode mockNode = mock(ObjectNode.class);

        numberSerializer.acceptJsonFormatVisitor(mockNode, null);
    }

    private JsonNode createSchemaNode(String type, boolean isNotNegative) {
        ObjectNode node = mapper.createObjectNode();
        ObjectNode propertiesNode = node.putObject("properties");
        ObjectNode numberNode = propertiesNode.putObject("number");

        ObjectNode typeNode = numberNode.putObject("type");
        typeNode.put("@type", type);

        ObjectNode additionalPropertiesNode = numberNode.putObject("additionalProperties");
        additionalPropertiesNode.putBoolean("allowNullValue", false);
        if (isNotNegative) {
            ObjectNode minNode = additionalPropertiesNode.putObject("minimum");
            minNode.putNumber("value", Long.MIN_VALUE);

            ObjectNode maxNode = additionalPropertiesNode.putObject("maximum");
            maxNode.putNumber("value", Long.MAX_VALUE);
        }

        return node;
    }
}