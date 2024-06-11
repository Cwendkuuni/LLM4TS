
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {
    
    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // stub
            }
        };
        
        DateTimeSerializerBase<Date> result = serializer.withFormat(true, null);
        assertEquals(true, result._useTimestamp);
        assertNull(result._customFormat);
    }
    
    @Test
    public void testCreateContextualWithoutFormatOverrides() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = null;
        
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // stub
            }
        };
        
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        assertEquals(serializer, result);
    }
    
    @Test
    public void testCreateContextualWithNumericFormatOverride() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = null;
        
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // stub
            }
        };
        
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.NUMBER);
        
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        assertNotNull(result);
        assertEquals(true, ((DateTimeSerializerBase<?>)result)._useTimestamp);
        assertNull(((DateTimeSerializerBase<?>)result)._customFormat);
    }
    
    @Test
    public void testCreateContextualWithPatternFormatOverride() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = null;
        
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // stub
            }
        };
        
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withPattern("yyyy-MM-dd");
        format = format.withLocale(Locale.US);
        format = format.withTimeZone(TimeZone.getTimeZone("GMT"));
        
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        assertNotNull(result);
        assertEquals(false, ((DateTimeSerializerBase<?>)result)._useTimestamp);
        assertNotNull(((DateTimeSerializerBase<?>)result)._customFormat);
    }
    
    @Test
    public void testCreateContextualWithStringFormatOverride() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = null;
        
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // stub
            }
        };
        
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.STRING);
        
        JsonSerializer<?> result = serializer.createContextual(serializers, property);
        assertNotNull(result);
        assertEquals(false, ((DateTimeSerializerBase<?>)result)._useTimestamp);
        assertNull(((DateTimeSerializerBase<?>)result)._customFormat);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithNullProvider() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = null;
        
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // stub
            }
        };
        
        serializer.createContextual(serializers, property);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithNullDateFormatter() throws JsonMappingException {
        SerializerProvider serializers = null;
        BeanProperty property = null;
        
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // stub
            }
        };
        
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withPattern("yyyy-MM-dd");
        format = format.withLocale(Locale.US);
        format = format.withTimeZone(TimeZone.getTimeZone("GMT"));
        
        serializer.createContextual(serializers, property);
    }
    
    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // stub
            }
        };
        
        SerializerProvider serializers = null;
        Date value = new Date();
        
        boolean result = serializer.isEmpty(serializers, value);
        assertFalse(result);
    }
    
    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // stub
            }
        };
        
        SerializerProvider serializers = null;
        Type typeHint = null;
        
        JsonNode result = serializer.getSchema(serializers, typeHint);
        assertNotNull(result);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // stub
            }
        };
        
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
    }
}
