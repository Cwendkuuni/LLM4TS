package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest<T> {

    @Test
    public void testDateTimeSerializerBaseInstanceCreation() {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
        assertEquals(Date.class, serializer.handledType());
        assertNull(serializer._useTimestamp);
        assertNull(serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
    }

    @Test
    public void testDateTimeSerializerBaseWithFormat() {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeSerializerBase<Date> baseSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        DateTimeSerializerBase<Date> serializedSerializer = baseSerializer.withFormat(false, customFormat);

        assertEquals(Date.class, serializedSerializer.handledType());
        assertNull(serializedSerializer._useTimestamp);
        assertEquals(customFormat, serializedSerializer._customFormat);
        assertNull(serializedSerializer._reusedCustomFormat);
    }

    @Test
    public void testDateTimeSerializerBaseWithTimestamp() {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, true, null);
        assertEquals(Long.class, serializer.handledType());
        assertTrue(serializer._useTimestamp.booleanValue());
        assertNull(serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
    }

    @Test
    public void testCreateContextualWithFormat() throws IOException {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);

        JsonSerializer<Date> jsonSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);

        assertEquals(Date.class, jsonSerializer.handledType());
        assertFalse(jsonSerializer.isContextualSerialization());
        assertEquals(customFormat, jsonSerializer._customFormat);
        assertNull(jsonSerializer._reusedCustomFormat);

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        jsonSerializer.serialize(new Date(), gen, mapper.getSerializerProvider());
        gen.flush();
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) gen.getOutput();
        assertEquals("2023-03-28 10:15:21", new String(outputStream.toByteArray()));
    }

    @Test
    public void testCreateContextualWithDefaultFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);

        JsonSerializer<Long> jsonSerializer = serializer.createContextual(mapper.getSerializerProvider(), null);

        assertEquals(Long.class, jsonSerializer.handledType());
        assertFalse(jsonSerializer.isContextualSerialization());
        assertNull(jsonSerializer._customFormat);
        assertNull(jsonSerializer._reusedCustomFormat);

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        jsonSerializer.serialize(1679459200000L, gen, mapper.getSerializerProvider());
        gen.flush();
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) gen.getOutput();
        assertEquals("1679459200000", new String(outputStream.toByteArray()));
    }

    @Test
    public void testCreateContextualWithFormatOverride() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);

        JsonSerializer<Date> jsonSerializer = serializer
                .createContextual(mapper.getSerializerProvider(), new StdDateFormat("dd/MM/yyyy HH:mm:ss"));

        assertEquals(Date.class, jsonSerializer.handledType());
        assertFalse(jsonSerializer.isContextualSerialization());
        assertNull(jsonSerializer._customFormat);
        assertNull(jsonSerializer._reusedCustomFormat);

        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        jsonSerializer.serialize(new Date(1679459200000L), gen, mapper.getSerializerProvider());
        gen.flush();
        ByteArrayOutputStream outputStream = (ByteArrayOutputStream) gen.getOutput();
        assertEquals("28/03/2023 10:15:21", new String(outputStream.toByteArray()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithInvalidFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);

        JsonSerializer<Long> jsonSerializer = serializer
                .createContextual(mapper.getSerializerProvider(), new Object());

        assertEquals(Long.class, jsonSerializer.handledType());
        assertFalse(jsonSerializer.isContextualSerialization());
        assertNull(jsonSerializer._customFormat);
        assertNull(jsonSerializer._reusedCustomFormat);
    }
}