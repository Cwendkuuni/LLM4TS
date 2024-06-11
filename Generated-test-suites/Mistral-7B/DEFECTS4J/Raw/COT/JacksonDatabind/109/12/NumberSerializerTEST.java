import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Comprehensive JUnit 4 test cases for NumberSerializer class.
 */
public class NumberSerializerTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Test constructor.
     */
    @Test
    public void testNumberSerializerConstructor() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        assertNotNull(numberSerializer);
    }

    /**
     * Test createContextual method with null SerializerProvider and BeanProperty.
     */
    @Test(expected = JsonMappingException.class)
    public void testCreateContextualNullArgs() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        numberSerializer.createContextual(null, null);
    }

    /**
     * Test createContextual method with non-null SerializerProvider and null BeanProperty.
     */
    @Test(expected = JsonMappingException.class)
    public void testCreateContextualNullProperty() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        numberSerializer.createContextual(new SerializerProvider(), null);
    }

    /**
     * Test createContextual method with null SerializerProvider.
     */
    @Test(expected = JsonMappingException.class)
    public void testCreateContextualNullProvider() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        numberSerializer.createContextual(null, new BeanProperty("test"));
    }

    /**
     * Test createContextual method with handled type as BigDecimal.
     */
    @Test
    public void testCreateContextualBigDecimal() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonSerializer<?> jsonSerializer = numberSerializer.createContextual(new SerializerProvider(), new BeanProperty("test"));
        assertSame(ToStringSerializer.instance, jsonSerializer);
    }

    /**
     * Test serialize method with null JsonGenerator and SerializerProvider.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullJsonGenerator() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        numberSerializer.serialize(null, null, null);
    }

    /**
     * Test serialize method with null SerializerProvider.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullSerializerProvider() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        numberSerializer.serialize(new BigDecimal(1), null, null);
    }

    /**
     * Test serialize method with null JsonGenerator and value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullValue() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        numberSerializer.serialize(null, new JsonGenerator(), new SerializerProvider());
    }

    /**
     * Test serialize method with BigDecimal value.
     */
    @Test
    public void testSerializeBigDecimal() throws JsonGenerationException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        ObjectNode result = MAPPER.createObjectNode();
        JsonGenerator generator = result.startObject().getGenerator();
        numberSerializer.serialize(new BigDecimal("123.456"), generator, new SerializerProvider());
        assertEquals("123.456", result.get("test").asText());
    }

    /**
     * Test serialize method with BigInteger value.
     */
    @Test
    public void testSerializeBigInteger() throws JsonGenerationException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        ObjectNode result = MAPPER.createObjectNode();
        JsonGenerator generator = result.startObject().getGenerator();
        numberSerializer.serialize(new BigInteger("12345678901234567890"), generator, new SerializerProvider());
        assertEquals("12345678901234567890", result.get("test").asText());
    }

    /**
     * Test serialize method with Integer value.
     */
    @Test
    public void testSerializeInteger() throws JsonGenerationException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        ObjectNode result = MAPPER.createObjectNode();
        JsonGenerator generator = result.startObject().getGenerator();
        numberSerializer.serialize(new Integer(123), generator, new SerializerProvider());
        assertEquals("123", result.get("test").asText());
    }

    /**
     * Test serialize method with Long value.
     */
    @Test
    public void testSerializeLong() throws JsonGenerationException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        ObjectNode result = MAPPER.createObjectNode();
        JsonGenerator generator = result.startObject().getGenerator();
        numberSerializer.serialize(new Long(1234567890123456789L), generator, new SerializerProvider());
        assertEquals("1234567890123456789", result.get("test").asText());
    }

    /**
     * Test serialize method with Float value.
     */
    @Test
    public void testSerializeFloat() throws JsonGenerationException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        ObjectNode result = MAPPER.createObjectNode();
        JsonGenerator generator = result.startObject().getGenerator();
        numberSerializer.serialize(new Float(123.456), generator, new SerializerProvider());
        assertEquals("123.456", result.get("test").asText());
    }

    /**
     * Test serialize method with Double value.
     */
    @Test
    public void testSerializeDouble() throws JsonGenerationException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        ObjectNode result = MAPPER.createObjectNode();
        JsonGenerator generator = result.startObject().getGenerator();
        numberSerializer.serialize(new Double(123.456), generator, new SerializerProvider());
        assertEquals("123.456", result.get("test").asText());
    }

    /**
     * Test serialize method with Byte value.
     */
    @Test
    public void testSerializeByte() throws JsonGenerationException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        ObjectNode result = MAPPER.createObjectNode();
        JsonGenerator generator = result.startObject().getGenerator();
        numberSerializer.serialize(new Byte((byte) 123), generator, new SerializerProvider());
        assertEquals("123", result.get("test").asText());
    }

    /**
     * Test serialize method with Short value.
     */
    @Test
    public void testSerializeShort() throws JsonGenerationException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        ObjectNode result = MAPPER.createObjectNode();
        JsonGenerator generator = result.startObject().getGenerator();
        numberSerializer.serialize(new Short((short) 123), generator, new SerializerProvider());
        assertEquals("123", result.get("test").asText());
    }

    /**
     * Test serialize method with null value.
     */
    @Test
    public void testSerializeNullValueString() throws JsonGenerationException {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        ObjectNode result = MAPPER.createObjectNode();
        JsonGenerator generator = result.startObject().getGenerator();
        numberSerializer.serialize(null, generator, new SerializerProvider());
        assertNull(result.get("test"));
    }

    /**
     * Test getSchema method with null SerializerProvider.
     */
    @Test
    public void testGetSchemaNullSerializerProvider() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonNode schema = numberSerializer.getSchema(null, Object.class);
        assertNull(schema);
    }

    /**
     * Test getSchema method with Object type hint.
     */
    @Test
    public void testGetSchemaObjectTypeHint() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonNode schema = numberSerializer.getSchema(new SerializerProvider(), Object.class);
        assertEquals("number", schema.asText());
    }

    /**
     * Test getSchema method with int type hint.
     */
    @Test
    public void testGetSchemaIntTypeHint() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonNode schema = numberSerializer.getSchema(new SerializerProvider(), Integer.TYPE);
        assertEquals("integer", schema.asText());
    }

    /**
     * Test acceptJsonFormatVisitor method with null JsonFormatVisitorWrapper and JavaType.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAcceptJsonFormatVisitorNullVisitor() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        numberSerializer.acceptJsonFormatVisitor(null, Object.class);
    }

    /**
     * Test acceptJsonFormatVisitor method with null JavaType.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testAcceptJsonFormatVisitorNullType() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        numberSerializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(), null);
    }

    /**
     * Test acceptJsonFormatVisitor method with handled type as BigDecimal.
     */
    @Test
    public void testAcceptJsonFormatVisitorBigDecimal() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonNode schema = numberSerializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(), JsonTypeFactory.numberType(JsonParser.NumberType.BIG_DECIMAL));
        assertEquals("number", schema.asText());
        assertTrue(schema.has("numberFormat"));
    }

    /**
     * Test acceptJsonFormatVisitor method with handled type as Integer.
     */
    @Test
    public void testAcceptJsonFormatVisitorInteger() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonNode schema = numberSerializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(), JsonTypeFactory.numberType(JsonParser.NumberType.INT));
        assertEquals("integer", schema.asText());
    }

    /**
     * Test acceptJsonFormatVisitor method with other handled types.
     */
    @Test
    public void testAcceptJsonFormatVisitorOtherTypes() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonNode schema = numberSerializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(), JsonTypeFactory.numberType(JsonParser.NumberType.FLOAT));
        assertEquals("number", schema.asText());
    }
}