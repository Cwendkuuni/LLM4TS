package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Test class for NumberSerializer
 */
@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();

        BigDecimal bigDecimal = new BigDecimal("1234567890.1234567890");
        numberSerializer.serialize(bigDecimal, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGeneratorForOutput(SerializerProvider p, DynamicSerializerProvider.SerializerProviderDelegate delegate) throws IOException {
                return jsonGenerator;
            }
        });
        jsonGenerator.flush();

        String result = jsonGenerator.getOutputFactory().getValueAsString();
        assertEquals("1234567890.123456789", result);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();

        BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
        numberSerializer.serialize(bigInteger, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGeneratorForOutput(SerializerProvider p, DynamicSerializerProvider.SerializerProviderDelegate delegate) throws IOException {
                return jsonGenerator;
            }
        });
        jsonGenerator.flush();

        String result = jsonGenerator.getOutputFactory().getValueAsString();
        assertEquals("123456789012345678901234567890", result);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();

        int integer = 123;
        numberSerializer.serialize(integer, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGeneratorForOutput(SerializerProvider p, DynamicSerializerProvider.SerializerProviderDelegate delegate) throws IOException {
                return jsonGenerator;
            }
        });
        jsonGenerator.flush();

        String result = jsonGenerator.getOutputFactory().getValueAsString();
        assertEquals("123", result);
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();

        long longValue = 1234567890123456789L;
        numberSerializer.serialize(longValue, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGeneratorForOutput(SerializerProvider p, DynamicSerializerProvider.SerializerProviderDelegate delegate) throws IOException {
                return jsonGenerator;
            }
        });
        jsonGenerator.flush();

        String result = jsonGenerator.getOutputFactory().getValueAsString();
        assertEquals("1234567890123456789", result);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();

        double doubleValue = 123.456789;
        numberSerializer.serialize(doubleValue, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGeneratorForOutput(SerializerProvider p, DynamicSerializerProvider.SerializerProviderDelegate delegate) throws IOException {
                return jsonGenerator;
            }
        });
        jsonGenerator.flush();

        String result = jsonGenerator.getOutputFactory().getValueAsString();
        assertEquals("123.456789", result);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();

        float floatValue = 123.45f;
        numberSerializer.serialize(floatValue, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGeneratorForOutput(SerializerProvider p, DynamicSerializerProvider.SerializerProviderDelegate delegate) throws IOException {
                return jsonGenerator;
            }
        });
        jsonGenerator.flush();

        String result = jsonGenerator.getOutputFactory().getValueAsString();
        assertEquals("123.45", result);
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();

        byte byteValue = 127;
        numberSerializer.serialize(byteValue, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGeneratorForOutput(SerializerProvider p, DynamicSerializerProvider.SerializerProviderDelegate delegate) throws IOException {
                return jsonGenerator;
            }
        });
        jsonGenerator.flush();

        String result = jsonGenerator.getOutputFactory().getValueAsString();
        assertEquals("127", result);
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator();

        short shortValue = 123;
        numberSerializer.serialize(shortValue, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGeneratorForOutput(SerializerProvider p, DynamicSerializerProvider.SerializerProviderDelegate delegate) throws IOException {
                return jsonGenerator;
            }
        });
        jsonGenerator.flush();

        String result = jsonGenerator.getOutputFactory().getValueAsString();
        assertEquals("123", result);
    }

    @Test
    public void testGetSchemaInteger() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonNode schema = numberSerializer.getSchema(new SerializerProvider(), JavaType.getTypeCollection().type);
        assertEquals("integer", schema.asText());
    }

    @Test
    public void testGetSchemaNumber() throws IOException {
        NumberSerializer numberSerializer = NumberSerializer.instance;
        JsonNode schema = numberSerializer.getSchema(new SerializerProvider(), Number.class.getType());
        assertEquals("number", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorBigDecimal() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getTypeFactory()
                .constructJsonFormatVisitor(JsonParser.NumberType.BIG_DECIMAL);
        NumberSerializer numberSerializer = NumberSerializer.instance;
        numberSerializer.acceptJsonFormatVisitor(visitor, Number.class.getType());
        visitor.expectNumberFormat(Number.class.getType());
    }

    @Test
    public void testAcceptJsonFormatVisitorBigInteger() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getTypeFactory()
                .constructJsonFormatVisitor(JsonParser.NumberType.BIG_INTEGER);
        NumberSerializer numberSerializer = NumberSerializer.instance;
        numberSerializer.acceptJsonFormatVisitor(visitor, BigInteger.class.getType());
        visitor.expectNumberFormat(BigInteger.class.getType());
    }
}