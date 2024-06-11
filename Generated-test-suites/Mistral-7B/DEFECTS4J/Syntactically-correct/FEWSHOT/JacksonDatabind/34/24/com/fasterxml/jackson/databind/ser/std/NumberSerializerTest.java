package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

@JacksonStdImpl
public class NumberSerializerTest {

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);

        Serializer<Number> serializer = NumberSerializer.instance;
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        // Test serialization for various Number types
        numberSerializer.serialize(null, generator, provider);

        numberSerializer.serialize(0, generator, provider);
        numberSerializer.serialize(1, generator, provider);
        numberSerializer.serialize(1L, generator, provider);
        numberSerializer.serialize(1.0d, generator, provider);
        numberSerializer.serialize(1.0f, generator, provider);
        numberSerializer.serialize((byte) 1, generator, provider); // Smallest signed byte, can be represented as int
        numberSerializer.serialize((short) 1, generator, provider); // Smallest signed short, can be represented as int

        numberSerializer.serialize(new Integer(1), generator, provider);
        numberSerializer.serialize(new Long(1), generator, provider);
        numberSerializer.serialize(new Double(1.0), generator, provider);
        numberSerializer.serialize(new Float(1.0), generator, provider);

        BigDecimal bigDecimal = new BigDecimal("1.23456");
        numberSerializer.serialize(bigDecimal, generator, provider);

        BigInteger bigInteger = new BigInteger("123456789012345678901234567890");
        numberSerializer.serialize(bigInteger, generator, provider);

        generator.close();
    }

    @Test
    public void testGetSchema() {
        ObjectMapper mapper = new ObjectMapper();
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);

        JsonNode schema = numberSerializer.getSchema(mapper.getSerializerProvider(), Number.class);
        assertNotNull(schema);
        assertEquals("number", schema.asText());

        numberSerializer = new NumberSerializer(BigInteger.class);
        schema = numberSerializer.getSchema(mapper.getSerializerProvider(), BigInteger.class);
        assertNotNull(schema);
        assertEquals("integer", schema.asText());

        numberSerializer = new NumberSerializer(BigDecimal.class);
        schema = numberSerializer.getSchema(mapper.getSerializerProvider(), BigDecimal.class);
        assertNotNull(schema);
        assertEquals("number", schema.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getJsonFormatVisitor(new TypeFactory(), Number.class);

        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, Number.class);

        numberSerializer = new NumberSerializer(BigInteger.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, BigInteger.class);

        numberSerializer = new NumberSerializer(BigDecimal.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, BigDecimal.class);
    }
}