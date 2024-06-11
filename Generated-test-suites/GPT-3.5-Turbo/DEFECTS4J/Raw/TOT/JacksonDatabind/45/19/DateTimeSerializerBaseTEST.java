package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.SerializerProvider;

public class DateTimeSerializerBaseTest {
    
    private static class TestClass {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
        private Date date;
        
        public TestClass(Date date) {
            this.date = date;
        }
    }
    
    private DateTimeSerializerBase<TestClass> serializer;
    private SerializerProvider serializerProvider;
    private JsonGenerator jsonGenerator;
    private ObjectMapper objectMapper;
    
    @Before
    public void setUp() throws Exception {
        serializer = new DateTimeSerializerBase<TestClass>(TestClass.class, false, null) {
            @Override
            public DateTimeSerializerBase<TestClass> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            
            @Override
            public void serialize(TestClass value, JsonGenerator gen, SerializerProvider serializers) {
                
            }
            
        };
        serializerProvider = new SerializerProvider();
        jsonGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        objectMapper = new ObjectMapper();
    }
    
    @Test
    public void testCreateContextual() {
        // Typical use case
        try {
            TestClass testClass = new TestClass(new Date());
            ContextualSerializer contextualSerializer = serializer.createContextual(serializerProvider, null);
            assertNotNull(contextualSerializer);
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
        
        // Edge case: Null property
        try {
            ContextualSerializer contextualSerializer = serializer.createContextual(serializerProvider, null);
            assertNotNull(contextualSerializer);
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }
    
    @Test
    public void testIsEmpty() {
        // Typical use case
        try {
            TestClass testClass = new TestClass(new Date());
            boolean isEmpty = serializer.isEmpty(serializerProvider, testClass);
            assertFalse(isEmpty);
            
            TestClass emptyTestClass = new TestClass(null);
            boolean isEmpty2 = serializer.isEmpty(serializerProvider, emptyTestClass);
            assertTrue(isEmpty2);
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }
    
    @Test
    public void testGetSchema() {
        // Typical use case
        try {
            serializer.getSchema(serializerProvider, null);
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        // Typical use case
        try {
            serializer.acceptJsonFormatVisitor(null, null);
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }
    
    @Test
    public void testSerialize() {
        // Typical use case
        try {
            TestClass testClass = new TestClass(new Date());
            serializer.serialize(testClass, jsonGenerator, serializerProvider);
        } catch (Exception e) {
            fail("Unexpected exception was thrown");
        }
    }
}