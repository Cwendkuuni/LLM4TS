package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

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
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    private static final DateTimeSerializerBase<Date> DATE_SERIALIZER = new DateTimeSerializerBase<Date>(Date.class, null, null);
    @SuppressWarnings("unchecked")
    private static final DateTimeSerializerBase<Long> TIMESTAMP_SERIALIZER = new DateTimeSerializerBase<Long>(Long.class, Boolean.TRUE, null);

    @Test
    public void testDefaultSerialization() throws IOException {
        Date date = new Date();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        DATE_SERIALIZER.serialize(date, gen, null);
        gen.flush();
        assertEquals("\"2023-03-24T15:17:09.624-05:00\"", gen.getOutputStream().toString().trim());

        Long timestamp = 1679528329123L;
        gen = new JsonFactory().createGenerator(new StringWriter());
        TIMESTAMP_SERIALIZER.serialize(timestamp, gen, null);
        gen.flush();
        assertEquals("1679528329123", gen.getOutputStream().toString().trim());
    }

    @Test
    public void testSerializationWithCustomFormat() throws IOException {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = new Date();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        DateTimeSerializerBase<Date> customFormatSerializer = new DateTimeSerializerBase<>(Date.class, Boolean.FALSE, customFormat);
        customFormatSerializer.serialize(date, gen, null);
        gen.flush();
        assertEquals(customFormat.format(date), gen.getOutputStream().toString().trim());

        Long timestamp = 1679528329123L;
        gen = new JsonFactory().createGenerator(new StringWriter());
        DateTimeSerializerBase<Long> customFormatTimestampSerializer = new DateTimeSerializerBase<>(Long.class, Boolean.FALSE, customFormat);
        customFormatTimestampSerializer.serialize(timestamp, gen, null);
        gen.flush();
        assertEquals(customFormat.format(new Date(timestamp)), gen.getOutputStream().toString().trim());
    }

    @Test
    public void testSerializationWithFormatOverride() throws IOException {
        JsonParser parser = new JsonFactory().createParser(new StringReader("{\"format\":{\"shape\":\"string\",\"pattern\":\"yyyy-MM-dd\",\"timezone\":\"UTC\"}}"));
        ObjectCodec codec = parser.getCodec();

        Date date = new Date();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        DateTimeSerializerBase<Date> overrideSerializer = DATE_SERIALIZER.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd", Locale.US));
        ObjectWriter writer = codec.writerWithRootValueSerializer(overrideSerializer);
        writer.writeValue(gen, date);
        gen.flush();
        assertEquals("\"2023-03-24\"", gen.getOutputStream().toString().trim());

        Long timestamp = 1679528329123L;
        gen = new JsonFactory().createGenerator(new StringWriter());
        DateTimeSerializerBase<Long> overrideTimestampSerializer = TIMESTAMP_SERIALIZER.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US));
        ObjectWriter writer2 = codec.writerWithRootValueSerializer(overrideTimestampSerializer);
        writer2.writeValue(gen, timestamp);
        gen.flush();
        assertEquals("\"2023-03-24T15:17:09.624Z\"", gen.getOutputStream().toString().trim());
    }

    @Test
    public void testSerializationWithLocalizedFormat() throws IOException {
        DateFormat customFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.GERMANY);

        Date date = new Date();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        DateTimeSerializerBase<Date> localizedFormatSerializer = new DateTimeSerializerBase<>(Date.class, Boolean.FALSE, customFormat);
        ObjectWriter writer = new ObjectMapper().writerWithRootValueSerializer(localizedFormatSerializer);
        writer.writeValue(gen, date);
        gen.flush();
        assertEquals(customFormat.format(date), gen.getOutputStream().toString().trim());
    }

    @Test
    public void testSerializationWithTimeZone() throws IOException {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));

        Date date = new Date();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        DateTimeSerializerBase<Date> timeZoneSerializer = new DateTimeSerializerBase<>(Date.class, Boolean.FALSE, customFormat);
        ObjectWriter writer = new ObjectMapper().writerWithRootValueSerializer(timeZoneSerializer);
        writer.writeValue(gen, date);
        gen.flush();
        assertEquals(customFormat.format(date), gen.getOutputStream().toString().trim());
    }
}