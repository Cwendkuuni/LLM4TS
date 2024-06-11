package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class DateTimeSerializerBaseTest {
    
    private DateTimeSerializerBase<Date> serializer;
    
    @Before
    public void setup() {
        serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            private static final long serialVersionUID = 1L;

            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                
            }
        };
    }

    @Test
    public void testConstructor() {
        assertNull(serializer._useTimestamp);
        assertNull(serializer._customFormat);
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Date> newSerializer = serializer.withFormat(true, null);
        assertNotNull(newSerializer);
        assertTrue(newSerializer._useTimestamp);
        assertNull(newSerializer._customFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider serializers = null;
        
        // Test with shape as numeric
        JsonFormat.Value format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.NUMBER);
        format = format.withTimeZone(TimeZone.getDefault());
        
        DateTimeSerializerBase<Date> result = serializer.createContextual(serializers, null);
        assertNotNull(result);
        assertTrue(result._useTimestamp);
        assertNull(result._customFormat);
        
        // Test with shape as string
        format = new JsonFormat.Value();
        format = format.withShape(JsonFormat.Shape.STRING);
        format = format.withPattern("yyyy-MM-dd");
        format = format.withLocale(Locale.US);
        format = format.withTimeZone(TimeZone.getDefault());
        
        result = serializer.createContextual(serializers, null);
        assertNotNull(result);
        assertFalse(result._useTimestamp);
        assertNotNull(result._customFormat);
        assertTrue(result._customFormat instanceof SimpleDateFormat);
        assertEquals("yyyy-MM-dd", ((SimpleDateFormat)result._customFormat).toPattern());
        assertEquals(Locale.US, ((SimpleDateFormat)result._customFormat).getLocale());
    }

    @Test
    public void testIsEmpty() {
        Date value = null;
        
        assertTrue(serializer.isEmpty(value));
        
        value = new Date();
        assertFalse(serializer.isEmpty(value));
    }

    @Test
    public void testGetSchema() {
        JsonSerializer<?> visit = new JsonObjectFormatVisitor.Base();
        JavaType typeHint = null;
        
        JsonNode result = serializer.getSchema(visit, typeHint);
        assertNotNull(result);
        assertEquals("string", result.asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        boolean asNumber = false;
        
        serializer.acceptJsonFormatVisitor(visitor, typeHint, asNumber);
    }

}