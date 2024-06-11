package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.version.Version. Feature.GENERIC_TYPES;
import static com.fasterxml.jackson.core.version.Version.Version.VERSION_2_11;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

/**
 * Test cases for {@link NumberSerializer}.
 */
@JacksonStdImpl
public class NumberSerializerTest {

    @Test
    public void testSerializeNull() throws JsonProcessingException {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = NumberSerializer.instance;
        serializer.serialize(null, gen, null);
        gen.flush();
        JsonParser parser = createJsonParser(gen.getOutputFile());
        assertEquals(JsonToken.VALUE_NULL, parser.nextToken());
    }

    @Test
    public void testSerializeByte() throws JsonProcessingException {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = NumberSerializer.instance;
        byte value = Byte.MIN_VALUE;
        serializer.serialize(value, gen, null);
        gen.flush();
        JsonParser parser = createJsonParser(gen.getOutputFile());
        assertEquals(-128, parser.getIntValue());
    }

    @Test
    public void testSerializeShort() throws JsonProcessingException {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = NumberSerializer.instance;
        short value = Short.MIN_VALUE;
        serializer.serialize(value, gen, null);
        gen.flush();
        JsonParser parser = createJsonParser(gen.getOutputFile());
        assertEquals(-32768, parser.getIntValue());
    }

    @Test
    public void testSerializeInt() throws JsonProcessingException {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = NumberSerializer.instance;
        int value = Integer.MIN_VALUE;
        serializer.serialize(value, gen, null);
        gen.flush();
        JsonParser parser = createJsonParser(gen.getOutputFile());
        assertEquals(-2147483648, parser.getLongValue());
    }

    @Test
    public void testSerializeLong() throws JsonProcessingException {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = NumberSerializer.instance;
        long value = Long.MIN_VALUE;
        serializer.serialize(value, gen, null);
        gen.flush();
        JsonParser parser = createJsonParser(gen.getOutputFile());
        assertEquals(-9223372036854775808L, parser.getLongValue());
    }

    @Test
    public void testSerializeFloat() throws JsonProcessingException {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = NumberSerializer.instance;
        float value = Float.NaN;
        serializer.serialize(value, gen, null);
        gen.flush();
        JsonParser parser = createJsonParser(gen.getOutputFile());
        assertEquals(JsonToken.VALUE_FLOAT, parser.nextToken());
        assertEqualsFloat(parser.getFloatValue(), Float.NaN);
    }

    @Test
    public void testSerializeDouble() throws JsonProcessingException {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = NumberSerializer.instance;
        double value = Double.NaN;
        serializer.serialize(value, gen, null);
        gen.flush();
        JsonParser parser = createJsonParser(gen.getOutputFile());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, parser.nextToken());
        assertEquals(value, parser.getDoubleValue(), 0.0);
    }

    @Test
    public void testSerializeBigInteger() throws JsonProcessingException {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = NumberSerializer.instance;
        BigInteger value = BigInteger.valueOf(Long.MIN_VALUE);
        serializer.serialize(value, gen, null);
        gen.flush();
        JsonParser parser = createJsonParser(gen.getOutputFile());
        assertEqualsLong(parser.getLongValue(), value.longValueExact());
    }

    @Test
    public void testSerializeBigDecimal() throws JsonProcessingException {
        JsonGenerator gen = createJsonGenerator();
        NumberSerializer serializer = NumberSerializer.instance;
        BigDecimal value = new BigDecimal("-1.1E-308");
        serializer.serialize(value, gen, null);
        gen.flush();
        JsonParser parser = createJsonParser(gen.getOutputFile());
        assertEquals(value.toString(), parser.getText());
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer serializer = NumberSerializer.instance;
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider().getSerializerProvider().getProviderInstance(Number.class), null);
        assertEquals("number", schema.getTextValue());
    }

    @Test
    public void testAcceptJsonFormatVisitorIntegerType() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer serializer = mapper.getSerializerProvider().getSerializerProvider().getProviderInstance(Number.class);
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getSerializerProvider().findShapeForValue("1", null, null, JsonType.NUMBER);
        NumberSerializer numberSerializer = (NumberSerializer) serializer;
        numberSerializer.acceptJsonFormatVisitor(visitor, JsonType.NUMBER);
        assertEquals("number", visitor.getCurrentValueFormat().getSchemaDescription());
    }

    @Test
    public void testAcceptJsonFormatVisitorBigDecimalType() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer serializer = mapper.getSerializerProvider().getSerializerProvider().getProviderInstance(Number.class);
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getSerializerProvider().findShapeForValue(new BigDecimal("-1.1E-308"), null, null, JsonType.NUMBER);
        NumberSerializer numberSerializer = (NumberSerializer) serializer;
        numberSerializer.acceptJsonFormatVisitor(visitor, JsonType.NUMBER);
        assertEquals("number", visitor.getCurrentValueFormat().getSchemaDescription());
    }

    private JsonGenerator createJsonGenerator() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createGenerator(new ByteArrayOutputStream(), JsonMapping.SerializerParams.builder()
                .withFeature(GENERIC_TYPES, VERSION_2_11.getFeatureValue(GENERIC_TYPES))
                .build());
    }

    private JsonParser createJsonParser(String jsonData) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(new ByteArrayInputStream(jsonData.getBytes()));
    }

    private JsonParser createJsonParser(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(new File(fileName));
    }

    private void assertEquals(String expectedJson, Object actual) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String actualJson = mapper.writeValueAsString(actual);
        assertEquals(expectedJson, actualJson);
    }

    private void assertEqualsLong(long expected, long actual) {
        assertEquals(expected, actual);
    }

    private void assertEqualsFloat(float expected, float actual) {
        assertEquals(expected, actual, 0.0f);
    }
}