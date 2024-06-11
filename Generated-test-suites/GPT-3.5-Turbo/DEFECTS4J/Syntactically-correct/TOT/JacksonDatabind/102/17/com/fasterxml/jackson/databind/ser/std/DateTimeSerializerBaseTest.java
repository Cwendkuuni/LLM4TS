package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private static class CustomDateTimeSerializer extends DateTimeSerializerBase<Date> {
        private CustomDateTimeSerializer(Class<Date> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new CustomDateTimeSerializer(Date.class, timestamp, customFormat);
        }

        @Override
        protected long _timestamp(Date value) {
            return 0;
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        }
    }

    @Test
    public void testSerialize_TimestampAsNumeric() throws IOException {
        // Test Case 1: Serialize Date value as numeric timestamp
        CustomDateTimeSerializer serializer = new CustomDateTimeSerializer(Date.class, true, null);
        DateFormat customFormat = serializer._customFormat;
        Assert.assertNull(customFormat);
        SerializerProvider provider = new DefaultSerializerProvider().createInstance(
                new ObjectMapper().getSerializationConfig(), new DateSerializer());
        GenMockJsonGenerator gen = new GenMockJsonGenerator();
        Date date = new Date();
        serializer.serialize(date, gen, provider);
        Assert.assertEquals(date.getTime(), gen.getTimestampValue());
    }

    @Test
    public void testSerialize_DateFormatPattern() throws IOException {
        // Test Case 2: Serialize Date value using a custom date format pattern
        String pattern = "yyyy-MM-dd";
        DateFormat customFormat = new SimpleDateFormat(pattern);
        CustomDateTimeSerializer serializer = new CustomDateTimeSerializer(Date.class, false, customFormat);
        DateFormat reSerializedFormat = serializer._reusedCustomFormat.get();
        Assert.assertNull(reSerializedFormat);
        SerializerProvider provider = new DefaultSerializerProvider().createInstance(
                new ObjectMapper().getSerializationConfig(), new DateSerializer());
        GenMockJsonGenerator gen = new GenMockJsonGenerator();
        Date date = new Date();
        serializer.serialize(date, gen, provider);
        Assert.assertEquals(new SimpleDateFormat(pattern).format(date), gen.getTextValue());
        reSerializedFormat = serializer._reusedCustomFormat.get();
        Assert.assertNotNull(reSerializedFormat);
        Assert.assertEquals(reSerializedFormat, customFormat);
    }

    @Test
    public void testSerialize_Default() throws IOException {
        // Test Case 3: Serialize Date value using default serialization
        CustomDateTimeSerializer serializer = new CustomDateTimeSerializer(Date.class, null, null);
        Assert.assertNull(serializer._useTimestamp);
        Assert.assertNull(serializer._customFormat);
        SerializerProvider provider = new DefaultSerializerProvider().createInstance(
                new ObjectMapper().getSerializationConfig(), new DateSerializer());
        GenMockJsonGenerator gen = new GenMockJsonGenerator();
        Date date = new Date();
        serializer.serialize(date, gen, provider);
        Assert.assertEquals(date.toString(), gen.getTextValue());
    }

    @Test
    public void testIsEmpty() {
        // Test Case 4: Check if the value is considered empty
        CustomDateTimeSerializer serializer = new CustomDateTimeSerializer(Date.class, true, null);
        SerializerProvider provider = new DefaultSerializerProvider().createInstance(
                new ObjectMapper().getSerializationConfig(), new DateSerializer());
        boolean empty = serializer.isEmpty(provider, new Date());
        Assert.assertFalse(empty);
    }

    @Test
    public void testGetSchema() {
        // Test Case 5: Get the schema for the value
        CustomDateTimeSerializer serializer = new CustomDateTimeSerializer(Date.class, true, null);
        SerializerProvider provider = new DefaultSerializerProvider().createInstance(
                new ObjectMapper().getSerializationConfig(), new DateSerializer());
        JsonNode schema = serializer.getSchema(provider, Date.class);
        Assert.assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        // Test Case 6: Accept a JsonFormatVisitor and visit the formats
        CustomDateTimeSerializer serializer = new CustomDateTimeSerializer(Date.class, true, null);
        SerializerProvider provider = new DefaultSerializerProvider().createInstance(
                new ObjectMapper().getSerializationConfig(), new DateSerializer());
        JsonFormatVisitorWrapper visitor = new GenMockJsonFormatVisitorWrapper(provider, null);
        serializer.acceptJsonFormatVisitor(visitor, Date.class);
        Assert.assertEquals(JsonParser.NumberType.LONG, visitor.getNumberType());
        Assert.assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getValueFormat());
    }

    @Test
    public void testSerializeAsString() throws IOException {
        // Test Case 7: Serialize Date value as string using custom format
        String pattern = "yyyy-MM-dd";
        DateFormat customFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
        CustomDateTimeSerializer serializer = new CustomDateTimeSerializer(Date.class, false, customFormat);
        SerializerProvider provider = new DefaultSerializerProvider().createInstance(
                new ObjectMapper().getSerializationConfig(), new DateSerializer());
        GenMockJsonGenerator gen = new GenMockJsonGenerator();
        Date date = new Date();
        serializer._serializeAsString(date, gen, provider);
        Assert.assertEquals(new SimpleDateFormat(pattern, Locale.ENGLISH).format(date), gen.getTextValue());
        DateFormat reSerializedFormat = serializer._reusedCustomFormat.get();
        Assert.assertNotNull(reSerializedFormat);
        Assert.assertEquals(reSerializedFormat, customFormat);
    }
}