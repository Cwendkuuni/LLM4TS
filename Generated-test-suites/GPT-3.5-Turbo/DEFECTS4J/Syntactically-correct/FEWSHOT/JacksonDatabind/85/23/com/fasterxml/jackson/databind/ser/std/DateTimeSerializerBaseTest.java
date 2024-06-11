package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.SerializerProvider;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<?> serializer;

    @Before
    public void setUp() {
        serializer = new DateTimeSerializerBase<Object>(Object.class, false, null) {
            
            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
                    throws JsonMappingException {
            }
        };
    }
    
    @Test
    public void testConstructor() {
        assertEquals(Object.class, serializer.handledType());
        assertEquals(false, serializer._useTimestamp);

        assertNull(serializer._customFormat);
    }
    
    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<?> newSerializer = serializer.withFormat(true, null);
        assertEquals(true, newSerializer._useTimestamp);
        assertNull(newSerializer._customFormat);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(serializer.isEmpty(null));

        assertFalse(serializer.isEmpty(new Object()));
    }

    @Test
    public void testIsEmptyWithProvider() {
        SerializerProvider provider = null;
        assertTrue(serializer.isEmpty(provider, null));

        assertFalse(serializer.isEmpty(provider, new Object()));
    }
    
    @Test
    public void testGetSchema() {
        SerializerProvider provider = null;
        assertNull(serializer.getSchema(provider, null));
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        SerializerProvider provider = null;
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        boolean asNumber = false;
        assertThrows(JsonMappingException.class, () -> {
            serializer.acceptJsonFormatVisitor(visitor, typeHint, asNumber);
        });
    }

    @Test
    public void testSerialize() {
        SerializerProvider provider = null;
        JsonGenerator gen = null;
        Object value = null;
        assertDoesNotThrow(() -> {
            serializer.serialize(value, gen, provider);
        });
    }
}