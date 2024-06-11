package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NumberSerializerTest {
    @Test
    public void testSerialize() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        
        // Test with BigDecimal
        BigDecimal bigDecimal = new BigDecimal("10.35");
        StringWriter writer = new StringWriter();
        JsonGenerator generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(bigDecimal, generator, null);
        generator.close();
        assertEquals("10.35", writer.toString());
        
        // Test with BigInteger
        BigInteger bigInteger = new BigInteger("1000");
        writer = new StringWriter();
        generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(bigInteger, generator, null);
        generator.close();
        assertEquals("1000", writer.toString());
        
        // Test with Long
        Long numberLong = new Long(1000000);
        writer = new StringWriter();
        generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(numberLong, generator, null);
        generator.close();
        assertEquals("1000000", writer.toString());
        
        // Test with Double
        Double numberDouble = new Double(100.55);
        writer = new StringWriter();
        generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(numberDouble, generator, null);
        generator.close();
        assertEquals("100.55", writer.toString());
        
        // Test with Float
        Float numberFloat = new Float(200.78);
        writer = new StringWriter();
        generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(numberFloat, generator, null);
        generator.close();
        assertEquals("200.78", writer.toString());
        
        // Test with Integer
        Integer numberInteger = new Integer(500);
        writer = new StringWriter();
        generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(numberInteger, generator, null);
        generator.close();
        assertEquals("500", writer.toString());
        
        // Test with Byte
        Byte numberByte = new Byte("2");
        writer = new StringWriter();
        generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(numberByte, generator, null);
        generator.close();
        assertEquals("2", writer.toString());
        
        // Test with Short
        Short numberShort = new Short("4");
        writer = new StringWriter();
        generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(numberShort, generator, null);
        generator.close();
        assertEquals("4", writer.toString());
        
        // Test with other Number types
        // We'll have to use fallback "untyped" number write method
        class MyNumber extends Number {
            public double doubleValue() {
                return 7.89;
            }
            public float floatValue() {
                return (float) 7.89;
            }
            public int intValue() {
                return 7;
            }
            public long longValue() {
                return 7;
            }
            public byte byteValue() {
                return 7;
            }
            public short shortValue() {
                return 7;
            }
            public String toString() {
                return "7.89";
            }
        }
        MyNumber myNumber = new MyNumber();
        writer = new StringWriter();
        generator = new JsonFactory().createGenerator(writer);
        serializer.serialize(myNumber, generator, null);
        generator.close();
        assertEquals("7.89", writer.toString());
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumerSerializer(Number.class);
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = Number.class;
        
        JsonNode schema = serializer.getSchema(provider, typeHint);
        
        JsonNode expectedSchema = createSchemaNode("number", true);
        
        assertEquals(expectedSchema, schema);
    }
}