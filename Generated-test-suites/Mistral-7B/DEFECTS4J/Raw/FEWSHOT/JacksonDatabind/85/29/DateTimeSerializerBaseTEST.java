package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Before;
import org.junit.Test;

public class DateTimeSerializerBaseTest<T> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    private DateTimeSerializerBase<T> serializer;

    @Before
    public void setup() {
        serializer = new DateTimeSerializerBase<T>(T.class, false, null);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(serializer.isEmpty(null));
        assertTrue(serializer.isEmpty(null, null));
        assertTrue(serializer.isEmpty(new Object(), null));
        assertTrue(serializer.isEmpty(new Object(), null, new Object()));

        Calendar calendar = Calendar.getInstance(UTC);
        calendar.setTime(new Date());
        T value = (T) calendar.getTimeInMillis();
        assertFalse(serializer.isEmpty(value));
        assertFalse(serializer.isEmpty(value, null));
        assertFalse(serializer.isEmpty(new Object(), value));
        assertFalse(serializer.isEmpty(new Object(), value, new Object()));
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null);
        serializer._acceptJsonFormatVisitor(visitor, null, false);
        assertTrue(visitor.getFormat() instanceof SimpleDateFormat);
        assertTrue(((SimpleDateFormat) visitor.getFormat()).getDateFormatSymbols().getShortMonths() != null);
        assertTrue(((SimpleDateFormat) visitor.getFormat()).getDateFormatSymbols().getShortWeekdays() != null);

        visitor = new JsonFormatVisitorWrapper(null, null);
        serializer._acceptJsonFormatVisitor(visitor, null, true);
        assertTrue(visitor.getFormat() instanceof StdDateFormat.NumberFormat);
        assertNull(visitor.getFormat().getClass().getDeclaredField("dateFormatSymbols"));
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null, 1024);
        serializeToJson(serializer, gen, null);
        assertEquals("null", gen.getOutputContent().toString());

        Calendar calendar = Calendar.getInstance(UTC);
        calendar.setTime(new Date());
        T value = (T) calendar.getTimeInMillis();
        serializeToJson(serializer, gen, value);
        String expected = DATE_FORMAT.format(calendar.getTime());
        assertEquals(expected, gen.getOutputContent().toString());

        serializer = new DateTimeSerializerBase<T>(T.class, true, null);
        serializeToJson(serializer, gen, value);
        assertEquals(Long.toString(value.longValue()), gen.getOutputContent().toString());
    }

    private void serializeToJson(DateTimeSerializerBase<T> serializer, JsonGenerator gen, T value) throws IOException {
        serializer.serialize(value, gen, new JsonSerializer.DefaultSerializers.InstanceManager(null));
        gen.flush();
    }
}

class T {
    long value;
}