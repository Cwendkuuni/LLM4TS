package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NumberSerializerTest {
  
    @Test
    public void testSerialize_Integer() {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Integer value = new Integer(10);
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(10);
    }

    @Test
    public void testSerialize_Long() {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Long value = new Long(1000);
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(1000L);
    }

    @Test
    public void testSerialize_Double() {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Double value = new Double(3.14);
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(3.14);
    }

    @Test
    public void testSerialize_Float() {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Float value = new Float(1.23);
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(1.23f);
    }

    @Test
    public void testSerialize_Byte() {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Byte value = new Byte(5);
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(5);
    }

    @Test
    public void testSerialize_Short() {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Short value = new Short(100);
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(100);
    }

    @Test
    public void testSerialize_BigDecimal() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        BigDecimal value = new BigDecimal("10.5");
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(new BigDecimal("10.5"));
    }

    @Test
    public void testSerialize_BigInteger() {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        BigInteger value = new BigInteger("10");
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(new BigInteger("10"));
    }

    @Test
    public void testSerialize_OtherNumber() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        Number value = new Number();
        serializer.serialize(value, generator, provider);

        verify(generator).writeNumber(value.toString());
    }

    @Test
    public void testGetSchema_Integer() {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        JsonNode schema = serializer.getSchema(provider, Integer.class);
        assertEquals("integer", schema.get("type").asText());
    }

    @Test
    public void testGetSchema_Long() {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        JsonNode schema = serializer.getSchema(provider, Long.class);
        assertEquals("integer", schema.get("type").asText());
    }

    @Test
    public void testGetSchema_Double() {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        JsonNode schema = serializer.getSchema(provider, Double.class);
        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testGetSchema_Float() {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        JsonNode schema = serializer.getSchema(provider, Float.class);
        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testGetSchema_Byte() {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        JsonNode schema = serializer.getSchema(provider, Byte.class);
        assertEquals("integer", schema.get("type").asText());
    }

    @Test
    public void testGetSchema_Short() {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        JsonNode schema = serializer.getSchema(provider, Short.class);
        assertEquals("integer", schema.get("type").asText());
    }

    @Test
    public void testGetSchema_BigDecimal() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        JsonNode schema = serializer.getSchema(provider, BigDecimal.class);
        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testGetSchema_BigInteger() {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        JsonNode schema = serializer.getSchema(provider, BigInteger.class);
        assertEquals("integer", schema.get("type").asText());
    }

    @Test
    public void testGetSchema_OtherNumber() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        JsonNode schema = serializer.getSchema(provider, Number.class);
        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_Integer() {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectIntegerFormat(typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitor_Long() {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectIntegerFormat(typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitor_Double() {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectNumberFormat(typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitor_Float() {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectNumberFormat(typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitor_BigDecimal() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectNumberFormat(typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitor_BigInteger() {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectNumberFormat(typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitor_OtherNumber() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(visitor).expectNumberFormat(typeHint);
    }
}