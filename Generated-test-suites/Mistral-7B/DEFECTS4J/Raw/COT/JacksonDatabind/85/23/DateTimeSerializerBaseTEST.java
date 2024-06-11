package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {
    

    @Test
    public void testDateTimeSerializerBaseConstructor() {
        Class<Integer> intType = Integer.class;
        DateTimeSerializerBase<Integer> serializer = new DateTimeSerializerBase<>(intType, false, null);
        assertNull(serializer._useTimestamp);
        assertNull(serializer._customFormat);
    }

    @Test
    public void testDateTimeSerializerBaseWithFormat() {
        Class<String> stringType = String.class;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(stringType, false, null);
        DateTimeSerializerBase<String> timestampSerializer = serializer.withFormat(true, null);
        assertNotNull(timestampSerializer._useTimestamp);
        assertNull(timestampSerializer._customFormat);

        DateTimeSerializerBase<String> customFormatSerializer = serializer.withFormat(false, new SimpleDateFormat("yyyy-MM-dd"));
        assertNull(customFormatSerializer._useTimestamp);
        assertNotNull(customFormatSerializer._customFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateTimeSerializerBaseWithNullSerializerProvider() {
        Class<Integer> intType = Integer.class;
        new DateTimeSerializerBase<>(intType, false, null).createContextual(null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDateTimeSerializerBaseInvalidFormat() throws JsonMappingException {
        Class<String> stringType = String.class;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(stringType, false, null);
        ObjectMapper mapper = new ObjectMapper();
        StdSerializerProvider serializers = mapper.getSerializerProvider();
        BeanProperty property = null;
        JsonFormat.Value format = JsonFormat.value().withShape(JsonFormat.Shape.STRING).withLocale(Locale.US);
        serializer.createContextual(serializers, property);
    }

    @Test
    public void testDateTimeSerializerBaseEmpty() throws IOException {
        Class<String> stringType = String.class;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(stringType, false, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        Object None = null;
        serializer.serialize(None, gen, mapper.getSerializerProvider());
        gen.flush();
    }

    @Test
    public void testDateTimeSerializerBaseSerializeTimestamp() throws IOException {
        Class<Long> longType = Long.class;
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(longType, true, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        Long value = 1633027200000L; // 2021-10-18 12:00:00
        serializer.serialize(value, gen, mapper.getSerializerProvider());
        gen.flush();
        assertEquals("1633027200000", mapper.readTree(System.in).asText());
    }

    @Test
    public void testDateTimeSerializerBaseSerializeCustomFormatString() throws IOException {
        Class<String> stringType = String.class;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(stringType, false, new SimpleDateFormat("yyyy-MM-dd"));
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        Date date = new Date(1633027200000L); // 2021-10-18 12:00:00
        serializer.serialize(date, gen, mapper.getSerializerProvider());
        gen.flush();
        assertEquals("2021-10-18", mapper.readTree(System.in).asText());
    }

    @Test(expected = IOException.class)
    public void testDateTimeSerializerBaseSerializeInvalidCustomFormat() throws IOException {
        Class<String> stringType = String.class;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(stringType, false, new SimpleDateFormat("XXX"));
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        Date date = new Date(1633027200000L); // 2021-10-18 12:00:00
        serializer.serialize(date, gen, mapper.getSerializerProvider());
    }

    @Test
    public void testDateTimeSerializerBaseSerializeCustomFormatLocaleAndTimezone() throws IOException, ParseException {
        Class<String> stringType = String.class;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(stringType, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse("2021-10-18 12:00:00 EDT");
        serializer.serialize(date, gen, mapper.getSerializerProvider());
        gen.flush();
        assertEquals("2021-10-18 16:00:00 EDT", mapper.readTree(System.in).asText());
    }

    @Test(expected = IOException.class)
    public void testDateTimeSerializerBaseSerializeInvalidCustomFormatLocaleAndTimezone() throws IOException, ParseException {
        Class<String> stringType = String.class;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(stringType, false, new SimpleDateFormat("XXX z"));
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse("2021-10-18 12:00:00 EDT");
        serializer.serialize(date, gen, mapper.getSerializerProvider());
    }

    
}