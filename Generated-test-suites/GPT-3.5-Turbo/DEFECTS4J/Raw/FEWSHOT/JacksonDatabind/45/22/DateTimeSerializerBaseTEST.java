
package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {
    
    @Test
    public void testConstructor() {
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
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {}
        };
        
        assertEquals(String.class, serializer.handledType());
        assertEquals(Boolean.TRUE, serializer._useTimestamp);
        assertNull(serializer._customFormat);
    }
    
    @Test
    public void testContextualSerializer_withFormatNumeric() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, null) {
            
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            
            @Override
            protected long _timestamp(String value) {
                return 0;
            }
            
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {}
        };
        
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        BeanProperty property = null;
        
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        
        assertEquals(Boolean.TRUE, ((DateTimeSerializerBase<?>) result)._useTimestamp);
        assertNull(((DateTimeSerializerBase<?>) result)._customFormat);
    }
    
    @Test
    public void testContextualSerializer_withFormatString() throws JsonMappingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, null) {
            
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            
            @Override
            protected long _timestamp(String value) {
                return 0;
            }
            
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {}
        };
        
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        BeanProperty property = null;
        
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        
        assertEquals(Boolean.FALSE, ((DateTimeSerializerBase<?>) result)._useTimestamp);
        
        SimpleDateFormat expectedFormat = new SimpleDateFormat(StdDateFormat.DATE_FORMAT_STR_ISO8601, serializers.getLocale());
        expectedFormat.setTimeZone(serializers.getTimeZone());
        
        assertEquals(expectedFormat, ((DateTimeSerializerBase<?>) result)._customFormat);
    }
    
    @Test
    public void testIsEmpty_NullValue() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, null) {
            
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            
            @Override
            protected long _timestamp(String value) {
                return 0;
            }
            
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {}
        };
        
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        
        assertTrue(serializer.isEmpty(serializers, null));
    }
    
    @Test
    public void testIsEmpty_NullDateValue() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, null) {
            
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            
            @Override
            protected long _timestamp(String value) {
                return 0;
            }
            
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {}
        };
        
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        
        assertTrue(serializer.isEmpty(serializers, ""));
    }
    
    @Test
    public void testIsEmpty_WithDateValue() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, null, null) {
            
            @Override
            public DateTimeSerializerBase<String> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }
            
            @Override
            protected long _timestamp(String value) {
                return 1;
            }
            
            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {}
        };
        
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        
        assertFalse(serializer.isEmpty(serializers, ""));
    }
    
    @Test
    public void testGetSchema_withTimestamp() {
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
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {}
        };
        
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        JavaType typeHint = null;
        
        JsonNode result = serializer.getSchema(serializers, typeHint);
        
        assertEquals("number", result.get("type").asText());
    }
    
    @Test
    public void testGetSchema_withoutTimestamp() {
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
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {}
        };
        
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        JavaType typeHint = null;
        
        JsonNode result = serializer.getSchema(serializers, typeHint);
        
        assertEquals("string", result.get("type").asText());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_withTimestamp() throws JsonMappingException {
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
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {}
        };
        
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        assertEquals(JsonParser.NumberType.LONG, visitor.getProvider().expectedNumberType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getProvider().getFormatWriteFeatures());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_withoutTimestamp() throws JsonMappingException {
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
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) {}
        };
        
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getProvider().getFormatWriteFeatures());
    }
}
