package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

@JsonFormat(shape = JsonFormat.Shape.NUMBER)
public class TestNumberSerializer {

    @Test
    public void testNumberSerializer() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        NumberSerializer numberSerializer = NumberSerializer.instance;
        SerializerProvider provider = mapper.getSerializerProvider();

        assertNotNull(numberSerializer);

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        provider.setGenerator(JsonGenerator.class, gen);

        Number number = 123;
        numberSerializer.serialize(number, gen, provider);
        assertTrue(gen.getOutputBuffer().toString().equals("123"));

        BigDecimal bigDecimal = new BigDecimal("123.45");
        numberSerializer.serialize(bigDecimal, gen, provider);
        assertTrue(gen.getOutputBuffer().toString().equals("123.45"));

        BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
        numberSerializer.serialize(bigInteger, gen, provider);
        assertTrue(gen.getOutputBuffer().toString().equals("123456789012345678901234567890"));

        number = Long.MAX_VALUE;
        numberSerializer.serialize(number, gen, provider);
        assertTrue(gen.getOutputBuffer().toString().equals(Long.MAX_VALUE + ""));

        number = Double.MAX_VALUE;
        numberSerializer.serialize(number, gen, provider);
        assertTrue(gen.getOutputBuffer().toString().equals(Double.MAX_VALUE + ""));

        number = new BigDecimal("1.0E23");
        numberSerializer.serialize(number, gen, provider);
        assertTrue(gen.getOutputBuffer().toString().equals("1.0E+23"));

        JsonNode schema = numberSerializer.getSchema(provider, Number.class);
        ObjectNode schemaNode = (ObjectNode) schema;
        assertTrue(schemaNode.get("type").equals("number"));

        JsonFormatVisitorWrapper visitorWrapper = new JsonFormatVisitorWrapper(JsonParser.Feature.AUTO_CLOSE_SOURCE, null);
        numberSerializer.acceptJsonFormatVisitor(visitorWrapper, Number.class);
    }
}