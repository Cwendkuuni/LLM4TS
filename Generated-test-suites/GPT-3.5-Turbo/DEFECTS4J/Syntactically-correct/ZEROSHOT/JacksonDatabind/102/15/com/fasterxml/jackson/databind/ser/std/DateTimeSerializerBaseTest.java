package com.fasterxml.jackson.databind.ser.std;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {
    
    @org.junit.Test
    public void testCreateInstance() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            
            }
        };
        assertNotNull(serializer);
    }
    
    @org.junit.Test
    public void testGetSchema_numeric() {
        SerializerProvider serializers = null;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            
            }
        };
        
        JsonNode result = serializer.getSchema(serializers, String.class);
        assertNotNull(result);
        assertEquals("number", result.get("type").asText());
    }
    
    @org.junit.Test
    public void testGetSchema_string() {
        SerializerProvider serializers = null;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            
            }
        };
        
        JsonNode result = serializer.getSchema(serializers, String.class);
        assertNotNull(result);
        assertEquals("string", result.get("type").asText());
    }
    
    @org.junit.Test
    public void testAcceptJsonFormatVisitor_true() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        SerializerProvider serializers = null;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, true, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            
            }
        };
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
    
    @org.junit.Test
    public void testAcceptJsonFormatVisitor_false() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        SerializerProvider serializers = null;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null) {
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
            
            }
        };
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
}