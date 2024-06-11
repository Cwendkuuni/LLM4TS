import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @Before
    public void setup() {
        numberSerializer = new NumberSerializer(Number.class);
        jsonGenerator = new ObjectMapper().createGenerator(System.out);
        serializerProvider = new ObjectMapper().getSerializerProvider();
    }

    @Test
    public void testSerialize() throws IOException {
        Number value = new BigDecimal("123.456");
        ObjectNode rootNode = JsonNodeFactory.instance.objectNode();
        
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        assertEquals("123.456", rootNode.asText());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        Number value = new BigInteger("12345678901234567890");
        ObjectNode rootNode = JsonNodeFactory.instance.objectNode();

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        assertEquals("12345678901234567890", rootNode.asText());
    }

    @Test
    public void testSerializeInteger() throws IOException {
        Number value = new Integer(123);
        ObjectNode rootNode = JsonNodeFactory.instance.objectNode();

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        assertEquals("123", rootNode.asText());
    }

    @Test
    public void testSerializeLong() throws IOException {
        Number value = new Long(1234567890123456789L);
        ObjectNode rootNode = JsonNodeFactory.instance.objectNode();

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        assertEquals("1234567890123456789", rootNode.asText());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        Number value = new Double(123.456);
        ObjectNode rootNode = JsonNodeFactory.instance.objectNode();

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        assertEquals("123.456", rootNode.asText());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        Number value = new Float(123.456f);
        ObjectNode rootNode = JsonNodeFactory.instance.objectNode();

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        assertEquals("123.456", rootNode.asText());
    }

    @Test
    public void testSerializeByte() throws IOException {
        Number value = new Byte((byte) 123);
        ObjectNode rootNode = JsonNodeFactory.instance.objectNode();

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        assertEquals("123", rootNode.asText());
    }

    @Test
    public void testSerializeShort() throws IOException {
        Number value = new Short((short) 123);
        ObjectNode rootNode = JsonNodeFactory.instance.objectNode();

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        assertEquals("123", rootNode.asText());
    }

    @Test
    public void testSerializeFallback() throws IOException {
        Number value = new CustomNumber(123);
        ObjectNode rootNode = JsonNodeFactory.instance.objectNode();

        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        jsonGenerator.flush();

        assertEquals("123", rootNode.asText());
    }

    @Test
    public void testGetSchema() {
        JsonNode schema = numberSerializer.getSchema(serializerProvider, null);

        assertTrue(schema.isNumber());
        assertTrue(schema.get("type").asText().equals("number") || schema.get("type").asText().equals("integer"));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new ObjectMapper().getSerializerProvider().getVisitor();

        numberSerializer.acceptJsonFormatVisitor(visitor, null);

        // Verify that the methods have been called
        // TODO: add appropriate verification
    }

    private static class CustomNumber extends Number {
        private int value;

        public CustomNumber(int value) {
            this.value = value;
        }

        @Override
        public int intValue() {
            return value;
        }

        @Override
        public long longValue() {
            return value;
        }

        @Override
        public float floatValue() {
            return value;
        }

        @Override
        public double doubleValue() {
            return value;
        }
    }
}