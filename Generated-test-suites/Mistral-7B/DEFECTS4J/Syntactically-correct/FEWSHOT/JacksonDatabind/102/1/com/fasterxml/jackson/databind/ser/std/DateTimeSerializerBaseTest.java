package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest<T> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SuppressWarnings("unchecked")
    abstract static class TestDateTimeSerializerBase<T> extends DateTimeSerializerBase<T> {
        TestDateTimeSerializerBase(Boolean useTimestamp, DateFormat customFormat) {
            super(handledType(), useTimestamp, customFormat);
        }

        @Override
        DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new TestDateTimeSerializerBase<>(timestamp, customFormat);
        }

        @Override
        void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // Implement this in test cases
        }

        @Override
        long _timestamp(T value) {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    public void testDefault() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestDateTimeSerializerBase<Date> serializer = new TestDateTimeSerializerBase<>(Boolean.FALSE, null);
        mapper.setSerializerInstance(Date.class, serializer);
        Date date = new Date();
        String json = mapper.writeValueAsString(date);
        assertEquals("\"2023-03-29T14:57:35.123Z\"", json);
    }

    @Test
    public void testUseTimestamp() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestDateTimeSerializerBase<Date> serializer = new TestDateTimeSerializerBase<>(Boolean.TRUE, null);
        mapper.setSerializerInstance(Date.class, serializer);
        Date date = new Date();
        String json = mapper.writeValueAsString(date);
        assertEquals("2023-03-29T14:57:35.123Z", json);
    }

    @Test
    public void testCustomFormat() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestDateTimeSerializerBase<Date> serializer = new TestDateTimeSerializerBase<>(Boolean.FALSE, DATE_FORMAT);
        mapper.setSerializerInstance(Date.class, serializer);
        Date date = new Date();
        String json = mapper.writeValueAsString(date);
        assertEquals("\"2023-03-29 14:57:35\"", json);
    }

    @Test
    public void testCustomFormatWithLocale() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestDateTimeSerializerBase<Date> serializer = new TestDateTimeSerializerBase<>(Boolean.FALSE,
                new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.GERMAN));
        mapper.setSerializerInstance(Date.class, serializer);
        Date date = new Date();
        String json = mapper.writeValueAsString(date);
        assertEquals("\"30.03.2023 14:57:35\"", json);
    }

    @Test
    public void testCustomFormatWithTimeZone() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestDateTimeSerializerBase<Date> serializer = new TestDateTimeSerializerBase<>(Boolean.FALSE,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US));
        mapper.setTimeZone(TimeZone.getTimeZone("UTC"));
        mapper.setSerializerInstance(Date.class, serializer);
        Date date = new Date();
        String json = mapper.writeValueAsString(date);
        assertEquals("\"2023-03-29 14:57:35Z\"", json);
    }

    @Test
    public void testCustomFormatWithLocaleAndTimeZone() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestDateTimeSerializerBase<Date> serializer = new TestDateTimeSerializerBase<>(Boolean.FALSE,
                new SimpleDateFormat("dd.MM.yyyy HH:mm:ss z", Locale.GERMAN));
        mapper.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
        mapper.setSerializerInstance(Date.class, serializer);
        Date date = new Date();
        String json = mapper.writeValueAsString(date);
        assertEquals("\"30.03.2023 15:57:35+0100\"", json);
    }

    @Test(expected = JsonMappingException.class)
    public void testCustomFormatWithInvalidFormat() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestDateTimeSerializerBase<Date> serializer = new TestDateTimeSerializerBase<>(Boolean.FALSE,
                new SimpleDateFormat("xyz"));
        mapper.setSerializerInstance(Date.class, serializer);
        Date date = new Date();
        mapper.writeValueAsString(date);
    }

    @Test
    public void testSerializeAsString() throws JsonProcessingException, IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        TestDateTimeSerializerBase<Date> serializer = new TestDateTimeSerializerBase<>(Boolean.FALSE, null);
        mapper.setSerializerInstance(Date.class, serializer);
        Date date = new Date();
        String json = mapper.writeValueAsString(date);
        TestDateTimeSerializerBase<Date> deserializer = new TestDateTimeSerializerBase<>(Boolean.FALSE, null);
        Date deserialized = mapper.readValue(json, Date.class);
        assertEquals(date, deserialized);
        assertEquals(date.toString(), json);
    }

    @Test
    public void testSerializeAsStringWithCustomFormat() throws JsonProcessingException, IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        TestDateTimeSerializerBase<Date> serializer = new TestDateTimeSerializerBase<>(Boolean.FALSE, DATE_FORMAT);
        mapper.setSerializerInstance(Date.class, serializer);
        Date date = new Date();
        String json = mapper.writeValueAsString(date);
        TestDateTimeSerializerBase<Date> deserializer = new TestDateTimeSerializerBase<>(Boolean.FALSE, DATE_FORMAT);
        Date deserialized = mapper.readValue(json, Date.class);
        assertEquals(date, deserialized);
        assertEquals(DATE_FORMAT.format(date), json);
    }

    @Test
    public void testEmptySerialization() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TestDateTimeSerializerBase<Date> serializer = new TestDateTimeSerializerBase<>(Boolean.FALSE, null);
        mapper.setSerializerInstance(Date.class, serializer);
        assertNull(mapper.writeValueAsString(null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        TestDateTimeSerializerBase<Date> serializer = new TestDateTimeSerializerBase<>(Boolean.FALSE, null);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(mapper.getSerializationConfig(), JsonParser.EmptyContext.instance(), JsonFormatTypes.DATE);
        serializer.acceptJsonFormatVisitor(visitor, JavaType.valueOf(Date.class));
        assertEquals("Date", visitor.getSerializableType().getName());
        assertEquals("date", visitor.getSerializerProvider().defaultSerializeDateValue(null, visitor.getGenerator()));
    }
}