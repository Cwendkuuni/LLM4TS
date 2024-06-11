package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBaseTest();
        DateTimeSerializerBase<DateTime> result = serializer.withFormat(true, new SimpleDateFormat("yyyy-MM-dd"));
        assertTrue(result._useTimestamp);
        assertEquals(new SimpleDateFormat("yyyy-MM-dd"), result._customFormat);
    }

    @Test
    public void testCreateContextual() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBaseTest();
        SerializerProvider provider = new SerializerProvider();
        assertNull(serializer.createContextual(provider, null));
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBaseTest();
        DateTime value = new DateTime();
        assertFalse(serializer.isEmpty(null, value));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBaseTest();
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = new Type();
        JsonNode result = serializer.getSchema(provider, typeHint);
        assertEquals("number", result.getFormat());
        assertTrue(result.getNullable());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBaseTest();
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();
        boolean asNumber = true;
        serializer.acceptJsonFormatVisitor(visitor, typeHint, asNumber);
    }

    @Test
    public void testSerializeAsString() {
        DateTimeSerializerBase<DateTime> serializer = new DateTimeSerializerBaseTest();
        JsonGenerator g = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        Date value = new Date();
        serializer._serializeAsString(value, g, provider);
    }
}