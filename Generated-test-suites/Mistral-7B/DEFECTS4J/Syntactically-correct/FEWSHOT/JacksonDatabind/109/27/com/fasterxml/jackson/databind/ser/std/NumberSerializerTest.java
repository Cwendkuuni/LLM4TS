package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test cases for {@link NumberSerializer}.
 */
@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Test for serialization of {@link Number} instances.
     */
    @Test
    public void testSerialization() throws Exception {
        final ObjectNode rootNode = mapper.createObjectNode();
        final JsonGenerator generator = rootNode.setUpTreeAsStartingPoint(mapper);

        // Serialize various Number instances
        serializeNumber(generator, (Number) null);
        serializeNumber(generator, 1);
        serializeNumber(generator, 1L);
        serializeNumber(generator, 1.0f);
        serializeNumber(generator, 1.0);
        serializeNumber(generator, new BigDecimal("1"));
        serializeNumber(generator, new BigInteger("1"));
        serializeNumber(generator, new BigDecimal("1.23"));
        serializeNumber(generator, new BigInteger("123"));

        // Check serialization results
        final ObjectNode resultNode = mapper.readTree(generator.getOutputStream().close());
        assertEquals("null", resultNode.get(0).asText());
        assertEquals("1", resultNode.get(1).asText());
        assertEquals("1", resultNode.get(2).asText());
        assertEquals("1.0", resultNode.get(3).asText());
        assertEquals("1.0", resultNode.get(4).asText());
        assertEquals("1", resultNode.get(5).asText());
        assertEquals("1", resultNode.get(6).asText());
        assertEquals("1.23", resultNode.get(7).asText());
        assertEquals("123", resultNode.get(8).asText());
    }

    /**
     * Test for serialization contextualization.
     */
    @Test
    public void testContextualSerialization() throws Exception {
        final ObjectNode rootNode = mapper.createObjectNode();
        final JsonGenerator generator = rootNode.setUpTreeAsStartingPoint(mapper);

        // Test serialization of BigDecimal with custom format
        final JsonFormat format = new JsonFormat();
        format.setShape(JsonFormat.Shape.STRING);
        final ObjectWriter writer = mapper.writerWithView(new SimpleBeanPropertyDefinitionMapper.SerializerProvider(), format);

        final BigDecimal value = new BigDecimal("123.45");
        final NumberSerializer serializer = new NumberSerializer(Number.class);
        final JsonSerializer<Number> contextualSerializer = serializer.createContextual(mapper.getSerializerProvider(), new BeanProperty("test"));
        contextualSerializer.serialize(value, generator, mapper.getSerializerProvider());

        // Check serialization result
        final ObjectNode resultNode = mapper.readTree(generator.getOutputStream().close());
        assertEquals("123.45", resultNode.get("test").asText());
    }

    /**
     * Test for deserialization of BigDecimal instances.
     */
    @Test
    public void testDeserializationBigDecimal() throws Exception {
        final JsonParser parser = mapper.getFactory().createParser("\"123.45\"");
        final ObjectReader reader = mapper.readerForUpdating(Object.class);
        final ObjectNode resultNode = (ObjectNode) reader.readValue(parser);

        // Check deserialization result
        assertEquals("123.45", resultNode.get(0).asText());
    }

    /**
     * Test for deserialization of BigInteger instances.
     */
    @Test
    public void testDeserializationBigInteger() throws Exception {
        final JsonParser parser = mapper.getFactory().createParser("123");
        final ObjectReader reader = mapper.readerForUpdating(Object.class);
        final ObjectNode resultNode = (ObjectNode) reader.readValue(parser);

        // Check deserialization result
        assertEquals("123", resultNode.get(0).asText());
    }

    /**
     * Test for deserialization of Number instances with custom format.
     */
    @Test
    public void testDeserializationCustomFormat() throws Exception {
        // Define custom deserializer
        final JsonDeserializer<Number> customDeserializer = new StdDeserializer<Number>(Number.class) {
            @Override
            public Number deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
                final TextNode node = p.getText();
                if (node.isNumeric()) {
                    final Number number = NumberUtils.parseNumber(node.asText(), NumberUtils.DEFAULT_PARSE_LONG_OR_BIGDECIMAL);
                    return number;
                }
                throw ctxt.wrongTokenException(p, null);
            }
        };

        // Configure ObjectMapper with custom deserializer
        final ObjectMapper customMapper = new ObjectMapper();
        customMapper.registerModule(new SimpleModule() {{
            addDeserializer(Number.class, customDeserializer);
        }});

        // Test deserialization of JSON string with custom format
        final JsonParser parser = customMapper.getFactory().createParser("\"1.23e4\"");
        final ObjectReader reader = customMapper.readerForUpdating(Object.class);
        final ObjectNode resultNode = (ObjectNode) reader.readValue(parser);

        // Check deserialization result
        assertEquals("123000.0", resultNode.get(0).asText());
    }

    /**
     * Helper method for serializing a Number instance to a JsonGenerator.
     */
    private void serializeNumber(JsonGenerator generator, Number number) throws IOException {
        final NumberSerializer serializer = NumberSerializer.instance;
        serializer.serialize(number, generator, mapper.getSerializerProvider());
    }
}