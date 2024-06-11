package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import junit.framework.TestCase;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest extends TestCase {
    
    public void testSerialize() {
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, false, null) {
            
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
    
            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // implementation for testing
            }
    
            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }
    
            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                return null;
            }
    
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                // implementation for testing
            }
        };
        
        TestObject testObject = new TestObject();
        JsonGenerator gen = null; 
        SerializerProvider provider = null;
        try {
            serializer.serialize(testObject, gen, provider);
        } catch (IOException e) {
            fail("Serialization failed");
        }
    }
    
    public void testIsEmpty() {
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, false, null) {
            
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
    
            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // implementation for testing
            }
    
            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }
    
            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                return null;
            }
    
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                // implementation for testing
            }
        };
        
        TestObject testObject = new TestObject();
        SerializerProvider provider = null;
        
        assertTrue(serializer.isEmpty(testObject));
        assertTrue(serializer.isEmpty(provider, testObject));
    }
    
    public void testCreateContextual() {
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, false, null) {
            
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
    
            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // implementation for testing
            }
    
            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }
    
            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                return null;
            }
    
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                // implementation for testing
            }
        };
        
        SerializerProvider serializers = null;
        BeanProperty property = null;
        try {
            serializer.createContextual(serializers, property);
        } catch (JsonMappingException e) {
            fail("CreateContextual failed");
        }
    }
    
    public void testGetSchema() {
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, false, null) {
            
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
    
            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // implementation for testing
            }
    
            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }
    
            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                return null;
            }
    
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                // implementation for testing
            }
        };
        
        SerializerProvider serializers = null;
        Type typeHint = null;
        JsonNode node = serializer.getSchema(serializers, typeHint);
        
        assertNotNull(node);
    }
    
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBase<TestObject> serializer = new DateTimeSerializerBase<TestObject>(TestObject.class, false, null) {
            
            @Override
            public DateTimeSerializerBase<TestObject> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
    
            @Override
            public void serialize(TestObject value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                // implementation for testing
            }
    
            @Override
            protected long _timestamp(TestObject value) {
                return 0;
            }
    
            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                return null;
            }
    
            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
                // implementation for testing
            }
        };
        
        JsonFormatVisitorWrapper visitor = null; 
        JavaType typeHint = null;
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (JsonMappingException e) {
            fail("AcceptJsonFormatVisitor failed");
        }
    }
    
    public class TestObject {
        // test class
    }

}