package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
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

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest<T> {

    public abstract static class TestData<U> {
        protected final Class<U> type;
        protected final T value;

        protected TestData(Class<U> type, T value) {
            this.type = type;
            this.value = value;
        }

        protected abstract DateTimeSerializerBase<U> getSerializer();

        protected abstract void testSerialization(DateTimeSerializerBase<U> serializer) throws IOException;

        protected abstract void testDeserialization(DateTimeSerializerBase<U> serializer) throws IOException;
    }

    @SuppressWarnings("unchecked")
    public static class TestDateTimeSerializerBase extends TestData<Long> {
        public TestDateTimeSerializerBase(Long value) {
            super(Long.class, value);
        }

        @Override
        DateTimeSerializerBase<Long> getSerializer() {
            return new DateTimeSerializerBase<Long>(Long.class, false, null);
        }

        @Override
        void testSerialization(DateTimeSerializerBase<Long> serializer) throws IOException {
            JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null, 0, null);
            serializer.serialize(value, gen, new SerializerProvider());
            String json = gen.getOutputContent();
            assertEquals(JsonTest.getJsonStringForLong(value), json);
        }

        @Override
        void testDeserialization(DateTimeSerializerBase<Long> serializer) throws IOException {
            JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(JsonTest.getJsonBytesForLong(value)));
            Long result = serializer.deserialize(parser, Long.class, new SerializerProvider());
            assertEquals(value, result);
        }
    }

    @SuppressWarnings("unchecked")
    public static class TestDateTimeSerializerWithFormat extends TestData<String> {
        public TestDateTimeSerializerWithFormat(String value) {
            super(String.class, value);
        }

        @Override
        DateTimeSerializerBase<String> getSerializer() {
            DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
            customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return new DateTimeSerializerBase<String>(String.class, false, customFormat);
        }

        @Override
        void testSerialization(DateTimeSerializerBase<String> serializer) throws IOException {
            JsonGenerator gen = new JsonFactory().createGenerator(new ByteArrayOutputStream(), null, 0, null);
            serializer.serialize(value, gen, new SerializerProvider());
            String json = gen.getOutputContent();
            assertEquals(value, json);
        }

        @Override
        void testDeserialization(DateTimeSerializerBase<String> serializer) throws IOException {
            JsonParser parser = new JsonFactory().createParser(new ByteArrayInputStream(JsonTest.getJsonBytesForString(value)));
            String result = serializer.deserialize(parser, String.class, new SerializerProvider());
            assertEquals(value, result);
        }
    }

    @Test
    public void testEmpty() {
        assertTrue(new DateTimeSerializerBase<>(Long.class, false, null).isEmpty(null));
        assertTrue(new DateTimeSerializerBase<>(Long.class, false, null).isEmpty(new Long(0)));
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(Long.class, false, null);
        JsonNode schema = serializer.getSchema(new SerializerProvider(), null);
        assertEquals("number", schema.getType());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy-MM-dd HH:mm:ss z");
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false, null);
        assertEquals(serializer.createContextual(new SerializerProvider(), null), serializer);

        JsonSerializer<String> contextualSerializer = serializer.createContextual(new SerializerProvider(), null);
        JsonFormat.Value contextualFormat = contextualSerializer.getFormat();
        assertEquals(format.getShape(), contextualFormat.getShape());
        assertEquals(format.getPattern(), contextualFormat.getPattern());
        assertEquals(format.getLocale(), contextualFormat.getLocale());
        assertEquals(format.getTimeZone(), contextualFormat.getTimeZone());
    }

    @Test
    public void testSerializationWithFormat() throws IOException {
        TestDateTimeSerializerWithFormat testData = new TestDateTimeSerializerWithFormat("2023-03-14 12:34:56 UTC");
        DateTimeSerializerBase<String> serializer = testData.getSerializer();
        testData.testSerialization(serializer);
    }

    @Test
    public void testDeserializationWithFormat() throws IOException {
        TestDateTimeSerializerWithFormat testData = new TestDateTimeSerializerWithFormat("2023-03-14 12:34:56 UTC");
        DateTimeSerializerBase<String> serializer = testData.getSerializer();
        testData.testDeserialization(serializer);
    }

}