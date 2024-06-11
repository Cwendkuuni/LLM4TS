package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Test
    public void testConstructor() {
        DateTimeSerializerBase<DateTime> serializerBase = new DateTimeSerializerBase<DateTime>(DateTime.class, Boolean.TRUE, new SimpleDateFormat());
        assertNotNull(serializerBase);
        assertEquals(DateTime.class, serializerBase.handledType());
        assertTrue(serializerBase._useTimestamp);
        assertNotNull(serializerBase._customFormat);
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<DateTime> serializerBase = new DateTimeSerializerBase<DateTime>(DateTime.class, Boolean.TRUE, new SimpleDateFormat());
        DateTimeSerializerBase<DateTime> newSerializerBase = serializerBase.withFormat(Boolean.FALSE, new SimpleDateFormat());
        assertNotNull(newSerializerBase);
        assertEquals(DateTime.class, newSerializerBase.handledType());
        assertFalse(newSerializerBase._useTimestamp);
        assertNotNull(newSerializerBase._customFormat);
    }

    @Test
    public void testCreateContextual() {
        DateTimeSerializerBase<DateTime> serializerBase = new DateTimeSerializerBase<DateTime>(DateTime.class, Boolean.TRUE, new SimpleDateFormat());
        JsonSerializer<?> jsonSerializer = serializerBase.createContextual(new SerializerProvider(), null);
        assertNotNull(jsonSerializer);
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<DateTime> serializerBase = new DateTimeSerializerBase<DateTime>(DateTime.class, Boolean.TRUE, new SimpleDateFormat());
        assertTrue(serializerBase.isEmpty(null, null));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<DateTime> serializerBase = new DateTimeSerializerBase<DateTime>(DateTime.class, Boolean.TRUE, new SimpleDateFormat());
        JsonNode jsonNode = serializerBase.getSchema(new SerializerProvider(), null);
        assertNotNull(jsonNode);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBase<DateTime> serializerBase = new DateTimeSerializerBase<DateTime>(DateTime.class, Boolean.TRUE, new SimpleDateFormat());
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = new JsonFormatVisitors.Base(this);
        JavaType javaType = null;
        assertThrows(JsonMappingException.class, () -> {
            serializerBase.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);
        });
    }

    @Test
    public void testSerialize() {
        DateTimeSerializerBase<DateTime> serializerBase = new DateTimeSerializerBase<DateTime>(DateTime.class, Boolean.TRUE, new SimpleDateFormat());
        JsonGenerator jsonGenerator = null;
        SerializerProvider serializerProvider = null;
        assertThrows(IOException.class, () -> {
            serializerBase.serialize(null, jsonGenerator, serializerProvider);
        });
    }

    @Test
    public void testPrivateMethod() {
        DateTimeSerializerBase<DateTime> serializerBase = new DateTimeSerializerBase<DateTime>(DateTime.class, Boolean.TRUE, new SimpleDateFormat());
        SerializerProvider serializerProvider = null;
        assertFalse(serializerBase._asTimestamp(serializerProvider));
    }

    @Test
    public void testAcceptJsonFormatVisitorPrivateMethod() {
        DateTimeSerializerBase<DateTime> serializerBase = new DateTimeSerializerBase<DateTime>(DateTime.class, Boolean.TRUE, new SimpleDateFormat());
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = new JsonFormatVisitors.Base(this);
        JavaType javaType = null;
        boolean asNumber = true;
        assertDoesNotThrow(() -> {
            serializerBase._acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType, asNumber);
        });
    }
}