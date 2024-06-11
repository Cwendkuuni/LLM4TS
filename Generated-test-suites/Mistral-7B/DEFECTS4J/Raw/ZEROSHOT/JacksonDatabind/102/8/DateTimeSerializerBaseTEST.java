package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("unchecked")
public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase() {
        Class<Date> dateType = Date.class;
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(dateType, false, null);
        ObjectMapper mapper = new ObjectMapper();

        // Test serialization with default settings
        Date date = new Date();
        try {
            JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
            serializer.serialize(date, gen, mapper.getSerializationConfig());
            gen.flush();
            String serializedDate = gen.getOutputContent().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss.SSSZ");
            Date parsedDate = sdf.parse(serializedDate);
            assertEquals(date, parsedDate);
        } catch (JsonProcessingException | IOException e) {
            fail("Serialization failed: " + e.getMessage());
        }

        // Test serialization with custom format
        DateTimeSerializerBase<Date> serializerWithFormat = serializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
        try {
            JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
            serializerWithFormat.serialize(date, gen, mapper.getSerializationConfig());
            gen.flush();
            String serializedDateWithFormat = gen.getOutputContent().toString();
            Date parsedDateWithFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(serializedDateWithFormat);
            assertEquals(date, parsedDateWithFormat);
        } catch (JsonProcessingException | IOException e) {
            fail("Serialization failed: " + e.getMessage());
        }

        // Test serialization with timestamp
        DateTimeSerializerBase<Date> serializerWithTimestamp = serializer.withFormat(true, null);
        try {
            JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
            serializerWithTimestamp.serialize(date, gen, mapper.getSerializationConfig());
            gen.flush();
            String serializedDateWithTimestamp = gen.getOutputContent().toString();
            assertEquals("" + date.getTime(), serializedDateWithTimestamp);
        } catch (JsonProcessingException | IOException e) {
            fail("Serialization failed: " + e.getMessage());
        }

        // Test deserialization with default settings
        try {
            JsonParser p = mapper.getFactory().createParser(new StringReader("2022-01-01T00:00:00.000Z"));
            Object readValue = mapper.readValue(p, Date.class);
            assertEquals(new Date(), readValue);
        } catch (JsonProcessingException e) {
            fail("Deserialization failed: " + e.getMessage());
        }

        // Test deserialization with custom format
        try {
            JsonParser p = mapper.getFactory().createParser(new StringReader("01-01-2022 00:00:00"));
            Object readValueWithFormat = mapper.readValue(p, Date.class);
            assertEquals(new Date(), readValueWithFormat);
        } catch (JsonProcessingException e) {
            fail("Deserialization failed: " + e.getMessage());
        }

        // Test deserialization with timestamp
        try {
            JsonParser p = mapper.getFactory().createParser(new StringReader("1640925200000"));
            Object readValueWithTimestamp = mapper.readValue(p, Date.class);
            assertEquals(new Date(1640925200000L), readValueWithTimestamp);
        } catch (JsonProcessingException e) {
            fail("Deserialization failed: " + e.getMessage());
        }
    }

    @Test
    public void testDateTimeSerializerBaseWithJsonFormat() {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
        Class<Date> dateTypeWithJsonFormat = Date.class;
        DateTimeSerializerBase<Date> serializerWithJsonFormat = new DateTimeSerializerBase<Date>(dateTypeWithJsonFormat, false, null);

        ObjectMapper mapperWithJsonFormat = new ObjectMapper();
        Date dateWithJsonFormat = new Date();

        // Test serialization with JsonFormat
        try {
            JsonGenerator gen = mapperWithJsonFormat.getFactory().createGenerator(new StringWriter());
            serializerWithJsonFormat.serialize(dateWithJsonFormat, gen, mapperWithJsonFormat.getSerializationConfig());
            gen.flush();
            String serializedDateWithJsonFormat = gen.getOutputContent().toString();
            assertEquals("01-01-2022 00:00:00", serializedDateWithJsonFormat);
        } catch (JsonProcessingException | IOException e) {
            fail("Serialization failed: " + e.getMessage());
        }

        // Test deserialization with JsonFormat
        try {
            JsonParser p = mapperWithJsonFormat.getFactory().createParser(new StringReader("01-01-2022 00:00:00"));
            Object readValueWithJsonFormat = mapperWithJsonFormat.readValue(p, Date.class);
            assertEquals(new Date(), readValueWithJsonFormat);
        } catch (JsonProcessingException e) {
            fail("Deserialization failed: " + e.getMessage());
        }
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomDateFormat() throws IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();

        // Test serialization with custom DateFormat
        DateFormat customDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateTimeSerializerBase<Date> serializerWithCustomFormat = serializer.withFormat(false, customDateFormat);

        Date date = new Date();

        // Test serialization with custom DateFormat
        JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
        serializerWithCustomFormat.serialize(date, gen, mapper.getSerializationConfig());
        gen.flush();
        String serializedDateWithCustomFormat = gen.getOutputContent().toString();
        assertEquals("01-01-2022 00:00:00", serializedDateWithCustomFormat);

        // Test deserialization with custom DateFormat
        JsonParser p = mapper.getFactory().createParser(new StringReader("01-01-2022 00:00:00"));
        Object readValueWithCustomFormat = mapper.readValue(p, Date.class);
        assertEquals(new Date(), readValueWithCustomFormat);
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomDateFormatAndLocale() throws IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();

        // Test serialization with custom DateFormat and Locale
        Locale locale = Locale.FRENCH;
        DateFormat customDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", locale);
        DateTimeSerializerBase<Date> serializerWithCustomFormatAndLocale = serializer.withFormat(false, customDateFormat);

        Date date = new Date();

        // Test serialization with custom DateFormat and Locale
        JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
        serializerWithCustomFormatAndLocale.serialize(date, gen, mapper.getSerializationConfig());
        gen.flush();
        String serializedDateWithCustomFormatAndLocale = gen.getOutputContent().toString();
        assertEquals("01-01-2022 00:00:00", serializedDateWithCustomFormatAndLocale);

        // Test deserialization with custom DateFormat and Locale
        JsonParser p = mapper.getFactory().createParser(new StringReader("01-01-2022 00:00:00"));
        Object readValueWithCustomFormatAndLocale = mapper.readValue(p, Date.class);
        assertEquals(new Date(), readValueWithCustomFormatAndLocale);
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomDateFormatAndTimeZone() throws IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();

        // Test serialization with custom DateFormat and TimeZone
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat customDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        customDateFormat.setTimeZone(timeZone);
        DateTimeSerializerBase<Date> serializerWithCustomFormatAndTimeZone = serializer.withFormat(false, customDateFormat);

        Date date = new Date();

        // Test serialization with custom DateFormat and TimeZone
        JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
        serializerWithCustomFormatAndTimeZone.serialize(date, gen, mapper.getSerializationConfig());
        gen.flush();
        String serializedDateWithCustomFormatAndTimeZone = gen.getOutputContent().toString();
        assertEquals("01-01-2022 00:00:00Z", serializedDateWithCustomFormatAndTimeZone);

        // Test deserialization with custom DateFormat and TimeZone
        JsonParser p = mapper.getFactory().createParser(new StringReader("01-01-2022 00:00:00Z"));
        Object readValueWithCustomFormatAndTimeZone = mapper.readValue(p, Date.class);
        assertEquals(new Date(), readValueWithCustomFormatAndTimeZone);
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomDateFormatAndLocaleAndTimeZone() throws IOException, ParseException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();

        // Test serialization with custom DateFormat, Locale and TimeZone
        Locale locale = Locale.FRENCH;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat customDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", locale);
        customDateFormat.setTimeZone(timeZone);
        DateTimeSerializerBase<Date> serializerWithCustomFormatAndLocaleAndTimeZone = serializer.withFormat(false, customDateFormat);

        Date date = new Date();

        // Test serialization with custom DateFormat, Locale and TimeZone
        JsonGenerator gen = mapper.getFactory().createGenerator(new StringWriter());
        serializerWithCustomFormatAndLocaleAndTimeZone.serialize(date, gen, mapper.getSerializationConfig());
        gen.flush();
        String serializedDateWithCustomFormatAndLocaleAndTimeZone = gen.getOutputContent().toString();
        assertEquals("01-01-2022 00:00:00Z", serializedDateWithCustomFormatAndLocaleAndTimeZone);

        // Test deserialization with custom DateFormat, Locale and TimeZone
        DateFormat customDateFormatDeserialization = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", locale);
        customDateFormatDeserialization.setTimeZone(timeZone);
        JsonParser p = mapper.getFactory().createParser(new StringReader("01-01-2022 00:00:00Z"));
        Object readValueWithCustomFormatAndLocaleAndTimeZone = mapper.readValue(p, Date.class, customDateFormatDeserialization);
        assertEquals(new Date(), readValueWithCustomFormatAndLocaleAndTimeZone);
    }
}