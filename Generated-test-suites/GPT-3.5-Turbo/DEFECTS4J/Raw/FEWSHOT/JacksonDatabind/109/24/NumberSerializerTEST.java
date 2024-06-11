package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

//import all necessary packages
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {
    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        JsonGenerator generator = null; //set the generator
        SerializerProvider provider = null; //set the provider
        
        BigDecimal value = new BigDecimal("123.45");
        
        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        
        //assertions
        //add assertions here
    }
    
    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        JsonGenerator generator = null; //set the generator
        SerializerProvider provider = null; //set the provider
        
        BigInteger value = new BigInteger("12345");
        
        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        
        //assertions
        //add assertions here
    }
    
    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Long.class);
        JsonGenerator generator = null; //set the generator
        SerializerProvider provider = null; //set the provider
        
        Long value = 12345L;
        
        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        
        //assertions
        //add assertions here
    }
    
    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Double.class);
        JsonGenerator generator = null; //set the generator
        SerializerProvider provider = null; //set the provider
        
        Double value = 123.45;
        
        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        
        //assertions
        //add assertions here
    }
    
    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Float.class);
        JsonGenerator generator = null; //set the generator
        SerializerProvider provider = null; //set the provider
        
        Float value = 123.45f;
        
        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        
        //assertions
        //add assertions here
    }
    
    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonGenerator generator = null; //set the generator
        SerializerProvider provider = null; //set the provider
        
        Integer value = 123;
        
        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        
        //assertions
        //add assertions here
    }
    
    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Byte.class);
        JsonGenerator generator = null; //set the generator
        SerializerProvider provider = null; //set the provider
        
        Byte value = 1;
        
        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        
        //assertions
        //add assertions here
    }
    
    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Short.class);
        JsonGenerator generator = null; //set the generator
        SerializerProvider provider = null; //set the provider
        
        Short value = 123;
        
        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        
        //assertions
        //add assertions here
    }
    
    @Test
    public void testSerializeUncastedNumber() throws IOException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonGenerator generator = null; //set the generator
        SerializerProvider provider = null; //set the provider
        
        Number value = 123;
        
        try {
            serializer.serialize(value, generator, provider);
        } catch (IOException e) {
            fail("An exception occurred: " + e.getMessage());
        }
        
        //assertions
        //add assertions here
    }

    @Test
    public void testGetSchemaBigInteger() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(BigInteger.class);
        SerializerProvider provider = null; //set the provider
        Type typeHint = null; //set the typeHint
        
        try {
            JsonNode schema = serializer.getSchema(provider, typeHint);
            
            //assertions
            //add assertions here
            
        } catch (JsonMappingException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetSchemaBigDecimal() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        SerializerProvider provider = null; //set the provider
        Type typeHint = null; //set the typeHint
        
        try {
            JsonNode schema = serializer.getSchema(provider, typeHint);
            
            //assertions
            //add assertions here
            
        } catch (JsonMappingException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testAcceptJsonFormatVisitorInteger() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Integer.class);
        JsonFormatVisitorWrapper visitor = null; //set the visitor
        JavaType typeHint = null; //set the typeHint
        
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
            
            //assertions
            //add assertions here
            
        } catch (JsonMappingException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testAcceptJsonFormatVisitorNotInteger() throws JsonMappingException {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        JsonFormatVisitorWrapper visitor = null; //set the visitor
        JavaType typeHint = null; //set the typeHint
        
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
            
            //assertions
            //add assertions here
            
        } catch (JsonMappingException e) {
            fail("An exception occurred: " + e.getMessage());
        }
    }
}