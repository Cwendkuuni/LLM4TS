package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {
    
    @Test
    public void testSerialize() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        
        // Test BigDecimal
        BigDecimal bigDecimal = new BigDecimal("123.456");
        JsonGenerator jsonGenerator = null; // initialize JsonGenerator
        SerializerProvider provider = null; // initialize SerializerProvider
        try {
            numberSerializer.serialize(bigDecimal, jsonGenerator, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        
        // Test BigInteger
        BigInteger bigInteger = new BigInteger("1234567890");
        try {
            numberSerializer.serialize(bigInteger, jsonGenerator, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        
        // Test Integer
        Integer integer = 123;
        try {
            numberSerializer.serialize(integer, jsonGenerator, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        
        // Test Long
        Long longNumber = 123L;
        try {
            numberSerializer.serialize(longNumber, jsonGenerator, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        
        // Test Double
        Double doubleNumber = 123.456;
        try {
            numberSerializer.serialize(doubleNumber, jsonGenerator, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        
        // Test Float
        Float floatNumber = 123.456F;
        try {
            numberSerializer.serialize(floatNumber, jsonGenerator, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        
        // Test Byte
        Byte byteNumber = 123;
        try {
            numberSerializer.serialize(byteNumber, jsonGenerator, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        
        // Test Short
        Short shortNumber = 123;
        try {
            numberSerializer.serialize(shortNumber, jsonGenerator, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        
        // Test Unknown Number Type
        Number number = 123;
        try {
            numberSerializer.serialize(number, jsonGenerator, provider);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        SerializerProvider provider = null; // initialize SerializerProvider
        Type typeHint = null; // initialize Type
        JsonNode schema = numberSerializer.getSchema(provider, typeHint);
        assertNotNull(schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = null; // initialize JsonFormatVisitorWrapper
        JavaType typeHint = null; // initialize JavaType
        try {
            numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            fail("Exception should not be thrown");
        }
    }
}