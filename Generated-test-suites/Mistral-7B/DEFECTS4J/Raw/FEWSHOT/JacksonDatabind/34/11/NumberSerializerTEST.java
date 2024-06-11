package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.JsonParser.NumberType.BIG_DECIMAL;
import static com.fasterxml.jackson.core.JsonParser.NumberType.BIG_INTEGER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    private static final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;

    @Test
    public void testSerializeInteger() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;

        JsonParser parser = createParser("{\"num\":1}");
        JsonToken token = parser.nextToken();
        ObjectNode node = parser.readValueAsTree();
        assertNotNull(node);

        JsonGenerator generator = createGenerator();
        serializer.serialize(1, generator, new SerializerProvider());
        JsonNode result = generator.getOutputTree();
        assertEquals(node, result);
    }

    @Test
    public void testSerializeLong() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;

        JsonParser parser = createParser("{\"num\":1L}");
        JsonToken token = parser.nextToken();
        ObjectNode node = parser.readValueAsTree();
        assertNotNull(node);

        JsonGenerator generator = createGenerator();
        serializer.serialize(1L, generator, new SerializerProvider());
        JsonNode result = generator.getOutputTree();
        assertEquals(node, result);
    }

    @Test
    public void testSerializeFloat() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;

        JsonParser parser = createParser("{\"num\":1.0f}");
        JsonToken token = parser.nextToken();
        ObjectNode node = parser.readValueAsTree();
        assertNotNull(node);

        JsonGenerator generator = createGenerator();
        serializer.serialize(1.0f, generator, new SerializerProvider());
        JsonNode result = generator.getOutputTree();
        assertEquals(node, result);
    }

    @Test
    public void testSerializeDouble() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;

        JsonParser parser = createParser("{\"num\":1.0}");
        JsonToken token = parser.nextToken();
        ObjectNode node = parser.readValueAsTree();
        assertNotNull(node);

        JsonGenerator generator = createGenerator();
        serializer.serialize(1.0, generator, new SerializerProvider());
        JsonNode result = generator.getOutputTree();
        assertEquals(node, result);
    }

    @Test
    public void testSerializeBigInteger() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;

        JsonParser parser = createParser("{\"num\":123456789012345678901234567890}");
        JsonToken token = parser.nextToken();
        ObjectNode node = parser.readValueAsTree();
        assertNotNull(node);

        JsonGenerator generator = createGenerator();
        serializer.serialize(new BigInteger("123456789012345678901234567890"), generator, new SerializerProvider());
        JsonNode result = generator.getOutputTree();
        assertEquals(node, result);
    }

    @Test
    public void testSerializeBigDecimal() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;

        JsonParser parser = createParser("{\"num\":1234567890.1234567890}");
        JsonToken token = parser.nextToken();
        ObjectNode node = parser.readValueAsTree();
        assertNotNull(node);

        JsonGenerator generator = createGenerator();
        serializer.serialize(new BigDecimal("1234567890.1234567890"), generator, new SerializerProvider());
        JsonNode result = generator.getOutputTree();
        assertEquals(node, result);
    }

    @Test
    public void testSerializeOtherNumber() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;

        JsonParser parser = createParser("{\"num\":new Byte((byte)1)}");
        JsonToken token = parser.nextToken();
        ObjectNode node = parser.readValueAsTree();
        assertNotNull(node);

        JsonGenerator generator = createGenerator();
        serializer.serialize(new Byte((byte)1), generator, new SerializerProvider());
        JsonNode result = generator.getOutputTree();
        assertEquals(node, result);
    }

    @Test
    public void testGetSchemaInteger() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;

        ObjectNode expectedSchema = nodeFactory.objectNode();
        expectedSchema.put("type", "integer");
        expectedSchema.put("description", "Number");

        JsonNode actualSchema = serializer.getSchema(new SerializerProvider(), Number.class);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetSchemaBigInteger() throws Exception {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);

        ObjectNode expectedSchema = nodeFactory.objectNode();
        expectedSchema.put("type", "integer");
        expectedSchema.put("description", "Number");

        JsonNode actualSchema = serializer.getSchema(new SerializerProvider(), BigInteger.class);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testGetSchemaBigDecimal() throws Exception {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);

        ObjectNode expectedSchema = nodeFactory.objectNode();
        expectedSchema.put("type", "number");
        expectedSchema.put("description", "Number");

        JsonNode actualSchema = serializer.getSchema(new SerializerProvider(), BigDecimal.class);
        assertEquals(expectedSchema, actualSchema);
    }

    @Test
    public void testAcceptJsonFormatVisitorInteger() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;

        JsonFormatVisitorWrapper visitor = new JsonSerializer.StdJsonSerializers.BaseJsonFormatVisitor<>(null, null, null);
        visitor.expectNumberFormat(Number.class);

        serializer.acceptJsonFormatVisitor(visitor, Number.class);
    }

    @Test
    public void testAcceptJsonFormatVisitorBigInteger() throws Exception {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);

        JsonFormatVisitorWrapper visitor = new JsonSerializer.StdJsonSerializers.BaseJsonFormatVisitor<>(null, null, null);
        visitor.expectNumberFormat(BigInteger.class);

        serializer.acceptJsonFormatVisitor(visitor, BigInteger.class);
    }

    @Test
    public void testAcceptJsonFormatVisitorBigDecimal() throws Exception {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);

        JsonFormatVisitorWrapper visitor = new JsonSerializer.StdJsonSerializers.BaseJsonFormatVisitor<>(null, null, null);
        visitor.expectNumberFormat(BigDecimal.class);

        serializer.acceptJsonFormatVisitor(visitor, BigDecimal.class);
    }

    private JsonParser createParser(String content) throws IOException {
        return new ObjectMapper().parserForObject(new StringReader(content));
    }

    private JsonGenerator createGenerator() throws IOException {
        return new ObjectMapper().getFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.getDefaultSerializerProvider());
    }
}