package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class NumberSerializerTest {
    @Test
    public void testSerialize() {
        // Test cases for BigDecimal
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        BigDecimal bigDecimal = new BigDecimal("1234.5678");
        JsonGenerator jsonGenerator = new JsonGenerator();
        SerializerProvider serializerProvider = new SerializerProvider();
        serializer.serialize(bigDecimal, jsonGenerator, serializerProvider);
        assertEquals("1234.5678", jsonGenerator.toString());

        String str = "1234567890123456789012345678901234567890";
        bigDecimal = new BigDecimal(str);
        jsonGenerator = new JsonGenerator();
        serializer.serialize(bigDecimal, jsonGenerator, serializerProvider);
        assertEquals(str, jsonGenerator.toString());

        // Test cases for BigInteger
        serializer = new NumberSerializer(BigInteger.class);
        BigInteger bigInteger = new BigInteger("12345678901234567890");
        jsonGenerator = new JsonGenerator();
        serializer.serialize(bigInteger, jsonGenerator, serializerProvider);
        assertEquals("12345678901234567890", jsonGenerator.toString());

        str = "1234567890123456789012345678901234567890";
        bigInteger = new BigInteger(str);
        jsonGenerator = new JsonGenerator();
        serializer.serialize(bigInteger, jsonGenerator, serializerProvider);
        assertEquals(str, jsonGenerator.toString());

        // Test cases for Long
        serializer = new NumberSerializer(Long.class);
        Long l = Long.MAX_VALUE;
        jsonGenerator = new JsonGenerator();
        serializer.serialize(l, jsonGenerator, serializerProvider);
        assertEquals(String.valueOf(Long.MAX_VALUE), jsonGenerator.toString());

        l = Long.MIN_VALUE;
        jsonGenerator = new JsonGenerator();
        serializer.serialize(l, jsonGenerator, serializerProvider);
        assertEquals(String.valueOf(Long.MIN_VALUE), jsonGenerator.toString());

        // Test cases for Double
        serializer = new NumberSerializer(Double.class);
        Double d = Double.MAX_VALUE;
        jsonGenerator = new JsonGenerator();
        serializer.serialize(d, jsonGenerator, serializerProvider);
        assertEquals(String.valueOf(Double.MAX_VALUE), jsonGenerator.toString());

        d = Double.MIN_VALUE;
        jsonGenerator = new JsonGenerator();
        serializer.serialize(d, jsonGenerator, serializerProvider);
        assertEquals(String.valueOf(Double.MIN_VALUE), jsonGenerator.toString());

        // Test cases for Float
        serializer = new NumberSerializer(Float.class);
        Float f = Float.MAX_VALUE;
        jsonGenerator = new JsonGenerator();
        serializer.serialize(f, jsonGenerator, serializerProvider);
        assertEquals(String.valueOf(Float.MAX_VALUE), jsonGenerator.toString());

        f = Float.MIN_VALUE;
        jsonGenerator = new JsonGenerator();
        serializer.serialize(f, jsonGenerator, serializerProvider);
        assertEquals(String.valueOf(Float.MIN_VALUE), jsonGenerator.toString());

        // Test cases for Integer
        serializer = new NumberSerializer(Integer.class);
        Integer i = Integer.MAX_VALUE;
        jsonGenerator = new JsonGenerator();
        serializer.serialize(i, jsonGenerator, serializerProvider);
        assertEquals(String.valueOf(Integer.MAX_VALUE), jsonGenerator.toString());

        i = Integer.MIN_VALUE;
        jsonGenerator = new JsonGenerator();
        serializer.serialize(i, jsonGenerator, serializerProvider);
        assertEquals(String.valueOf(Integer.MIN_VALUE), jsonGenerator.toString());

        // Test cases for Byte
        serializer = new NumberSerializer(Byte.class);
        Byte b = Byte.MAX_VALUE;
        jsonGenerator = new JsonGenerator();
        serializer.serialize(b, jsonGenerator, serializerProvider);
        assertEquals(String.valueOf(Byte.MAX_VALUE), jsonGenerator.toString());

        b = Byte.MIN_VALUE;
        jsonGenerator = new JsonGenerator();
        serializer.serialize(b, jsonGenerator, serializerProvider);
        assertEquals(String.valueOf(Byte.MIN_VALUE), jsonGenerator.toString());

        // Test cases for Short
        serializer = new NumberSerializer(Short.class);
        Short s = Short.MAX_VALUE;
        jsonGenerator = new JsonGenerator();
        serializer.serialize(s, jsonGenerator, serializerProvider);
        assertEquals(String.valueOf(Short.MAX_VALUE), jsonGenerator.toString());

        s = Short.MIN_VALUE;
        jsonGenerator = new JsonGenerator();
        serializer.serialize(s, jsonGenerator, serializerProvider);
        assertEquals(String.valueOf(Short.MIN_VALUE), jsonGenerator.toString());

        // Test cases for other Number type
        serializer = new NumberSerializer(Number.class);
        Number n = 12345678L;
        jsonGenerator = new JsonGenerator();
        serializer.serialize(n, jsonGenerator, serializerProvider);
        assertEquals("12345678", jsonGenerator.toString());
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        SerializerProvider serializerProvider = new SerializerProvider();
        JsonNode schema = serializer.getSchema(serializerProvider, BigDecimal.class);
        assertEquals("number", schema.getNodeType().toString());
        assertEquals(true, schema.isNumber());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        SerializerProvider serializerProvider = new SerializerProvider();
        JavaType typeHint = new JavaType();
        serializer.acceptJsonFormatVisitor(serializerProvider, typeHint);
        // Additional assertions can be added here based on the actual behavior of the method
    }
}