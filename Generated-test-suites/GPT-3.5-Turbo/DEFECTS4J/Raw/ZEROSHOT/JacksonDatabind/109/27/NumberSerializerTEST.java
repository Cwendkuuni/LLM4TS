package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {

    @Test
    public void testSerialize() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        
        // Test with BigDecimal
        BigDecimal number1 = new BigDecimal("123.45");
        String expected1 = "123.45";
        serializer.serialize(number1, generator, provider);
        assertEquals(expected1, generator.getOutput());
        
        // Test with BigInteger
        BigInteger number2 = new BigInteger("1000");
        String expected2 = "1000";
        serializer.serialize(number2, generator, provider);
        assertEquals(expected2, generator.getOutput());
        
        // Test with Long
        Long number3 = new Long(10);
        String expected3 = "10";
        serializer.serialize(number3, generator, provider);
        assertEquals(expected3, generator.getOutput());
        
        // Test with Double
        Double number4 = new Double(1.23);
        String expected4 = "1.23";
        serializer.serialize(number4, generator, provider);
        assertEquals(expected4, generator.getOutput());
        
        // Test with Float
        Float number5 = new Float(3.14);
        String expected5 = "3.14";
        serializer.serialize(number5, generator, provider);
        assertEquals(expected5, generator.getOutput());
        
        // Test with Integer
        Integer number6 = new Integer(100);
        String expected6 = "100";
        serializer.serialize(number6, generator, provider);
        assertEquals(expected6, generator.getOutput());
        
        // Test with Byte
        Byte number7 = new Byte("5");
        String expected7 = "5";
        serializer.serialize(number7, generator, provider);
        assertEquals(expected7, generator.getOutput());
        
        // Test with Short
        Short number8 = new Short("20");
        String expected8 = "20";
        serializer.serialize(number8, generator, provider);
        assertEquals(expected8, generator.getOutput());
        
        // Test with other number type
        Float number9 = new Float(2.5);
        String expected9 = "2.5";
        serializer.serialize(number9, generator, provider);
        assertEquals(expected9, generator.getOutput());
    }

    @Test
    public void testGetSchema() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        SerializerProvider provider = new SerializerProvider();
        
        // Test with integer type
        Type intType = Integer.class;
        JsonNode schema1 = serializer.getSchema(provider, intType);
        assertEquals("integer", schema1.getNodeType());
        
        // Test with other number type
        Type numberType = Double.class;
        JsonNode schema2 = serializer.getSchema(provider, numberType);
        assertEquals("number", schema2.getNodeType());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();
        
        // Test with integer type
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertNotNull(visitor.getIntFormat());
        
        // Test with other number type
        visitor = new JsonFormatVisitorWrapper();
        typeHint = new JavaType();
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertNotNull(visitor.getNumberFormat());
    }
}