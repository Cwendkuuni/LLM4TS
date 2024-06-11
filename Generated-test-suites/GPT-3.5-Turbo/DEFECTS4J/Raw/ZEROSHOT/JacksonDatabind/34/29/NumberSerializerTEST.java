package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    private NumberSerializer numberSerializer;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;

    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
        
        // Create a mock JsonGenerator and SerializerProvider
        jsonGenerator = new JsonGenerator();
        serializerProvider = new SerializerProvider();
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        BigDecimal value = new BigDecimal("10.5");
        
        // Call the serialize method
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Verify the result
        assertEquals("10.5", jsonGenerator.getOutput());
    }
    
    @Test
    public void testSerializeBigInteger() throws IOException {
        BigInteger value = new BigInteger("100");
        
        // Call the serialize method
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Verify the result
        assertEquals("100", jsonGenerator.getOutput());
    }
    
    @Test
    public void testSerializeInteger() throws IOException {
        Integer value = new Integer(10);
        
        // Call the serialize method
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Verify the result
        assertEquals("10", jsonGenerator.getOutput());
    }
    
    @Test
    public void testSerializeLong() throws IOException {
        Long value = new Long(1000);
        
        // Call the serialize method
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Verify the result
        assertEquals("1000", jsonGenerator.getOutput());
    }
    
    @Test
    public void testSerializeDouble() throws IOException {
        Double value = new Double(3.14);
        
        // Call the serialize method
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Verify the result
        assertEquals("3.14", jsonGenerator.getOutput());
    }
    
    @Test
    public void testSerializeFloat() throws IOException {
        Float value = new Float(1.5);
        
        // Call the serialize method
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Verify the result
        assertEquals("1.5", jsonGenerator.getOutput());
    }
    
    @Test
    public void testSerializeByte() throws IOException {
        Byte value = new Byte((byte) 1);
        
        // Call the serialize method
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Verify the result
        assertEquals("1", jsonGenerator.getOutput());
    }
    
    @Test
    public void testSerializeShort() throws IOException {
        Short value = new Short((short) 10);
        
        // Call the serialize method
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Verify the result
        assertEquals("10", jsonGenerator.getOutput());
    }
    
    @Test
    public void testSerializeOtherNumber() throws IOException {
        // Create a custom Number object
        MyNumber value = new MyNumber(50);
        
        // Call the serialize method
        numberSerializer.serialize(value, jsonGenerator, serializerProvider);
        
        // Verify the result
        assertEquals("50", jsonGenerator.getOutput());
    }
    
    // Inner class representing a custom Number object
    private static class MyNumber extends Number {
        private int value;
        
        public MyNumber(int value) {
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