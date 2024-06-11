package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * Test cases for {@link NumberSerializer} class.
 */
@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    private static final JsonParser parser = new JsonFactory().createParser("{}");
    private static final JsonGenerator generator = new JsonFactory().createGenerator(System.out);

    @Test
    public void testSerializeNumber() throws Exception {
        for (Number number : new Number[]{null, 0, 1, 1L, 1.0F, 1.0, new BigDecimal("1"), new BigInteger("1")}) {
            NumberSerializer serializer = NumberSerializer.instance;
            JsonGenerator g = generator.getOutputFactory().createGenerator(new ByteArrayOutputStream());
            serializer.serialize(number, g, new SerializerProvider());
            JsonParser p = parser;
            p.readValueAs(Number.class, g.getOutputStreamValue());
            assertEquals(number, p.getValueAsNumber());
        }
    }

    @Test
    public void testSerializeBigDecimal() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator g = generator.getOutputFactory().createGenerator(new ByteArrayOutputStream());
        g.writeStartObject();
        g.writeFieldName("value");
        g.writeValue(new BigDecimal("123.456"));
        g.writeEndObject();
        JsonParser p = parser;
        Object readValue = p.readValueAs(Object.class, g.getOutputStreamValue());
        JsonNode node = p.readValueAsTree(g.getOutputStreamValue());
        assertEquals(new BigDecimal("123.456"), readValue);
        JsonNode expectedNode = new ObjectMapper().readTree("{\"value\":123.456}");
        assertEquals(expectedNode, node);
    }

    @Test
    public void testSerializeBigInteger() throws Exception {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator g = generator.getOutputFactory().createGenerator(new ByteArrayOutputStream());
        g.writeStartObject();
        g.writeFieldName("value");
        g.writeValue(new BigInteger("123"));
        g.writeEndObject();
        JsonParser p = parser;
        Object readValue = p.readValueAs(Object.class, g.getOutputStreamValue());
        JsonNode node = p.readValueAsTree(g.getOutputStreamValue());
        assertEquals(new BigInteger("123"), readValue);
        JsonNode expectedNode = new ObjectMapper().readTree("{\"value\":123}");
        assertEquals(expectedNode, node);
    }

    @Test
    public void testContextualSerialization() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFormat.Shape shape = JsonFormat.Value.builder().shape(JsonFormat.Shape.STRING).build();
        JsonFormat jsonFormat = JsonFormat.value(".{@type=java.math.BigDecimal}");
        jsonFormat = jsonFormat.withFormatOverrides(new JsonFormat.Value(shape));

        ObjectNode node = objectMapper.createObjectNode();
        node.setMixInAnnotations(new JsonMixInAnnotations(new JsonFormat.Value[]{jsonFormat}));

        JsonSerializer<Number> serializer = new NumberSerializer(Number.class);
        serializer = serializer.withContextualSerializer(new ToStringSerializer(Number.class));

        ObjectMapper contextualObjectMapper = new ObjectMapper();
        contextualObjectMapper.registerSerializer(Number.class, serializer);

        BigDecimal bigDecimal = new BigDecimal("123.456");
        NodeNode nodeNode = contextualObjectMapper.valueToTree(bigDecimal);
        String serializedString = contextualObjectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(nodeNode);

        JsonParser p = parser;
        Object readValue = p.readValueAs(Object.class, new ByteArrayInputStream(serializedString.getBytes()));
        JsonNode jsonNode = p.readValueAsTree(new ByteArrayInputStream(serializedString.getBytes()));
        assertEquals(bigDecimal, readValue);
        assertEquals(nodeNode, jsonNode);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(new JsonDeserializer.Nulls.Empty(), null);
        serializer.acceptJsonFormatVisitor(visitor, new SimpleType(Number.class));
        assertTrue(visitor.hasHandledNumberFormat());
    }

    @Test
    public void testUnhandledNumberFormat()
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.createObjectNode();
        node.put("number", new Object());
        JsonMappingException exception = null;
        try {
            ObjectMapper contextualObjectMapper = new ObjectMapper();
            contextualObjectMapper.registerSerializer(Number.class, NumberSerializer.instance);
            contextualObjectMapper.writeValue(new File("output.json"), node);
        } catch (JsonMappingException e) {
            exception = e;
        }
        assertNotNull(exception);
    }
}