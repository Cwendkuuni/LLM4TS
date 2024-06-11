package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

public class DateTimeSerializerBaseTest {

    public class CustomDateTimeSerializer extends DateTimeSerializerBase<Date> {

        public CustomDateTimeSerializer() {
            super(Date.class, null, null);
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
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
    public void testContextual() throws Exception {
        DateTimeSerializerBase<Date> serializer = new CustomDateTimeSerializer();
        SerializerProvider mockProvider = null;
        BeanProperty mockProperty = null;
        serializer.createContextual(mockProvider, mockProperty);
        // Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsEmpty() throws Exception {
        DateTimeSerializerBase<Date> serializer = new CustomDateTimeSerializer();
        SerializerProvider mockProvider = null;
        Date value = null;
        boolean result = serializer.isEmpty(mockProvider, value);
        // Assert.assertEquals(expectedValue, result);
    }

    @Test
    public void testGetSchema() throws Exception {
        DateTimeSerializerBase<Date> serializer = new CustomDateTimeSerializer();
        SerializerProvider mockProvider = null;
        Type mockType = null;
        JsonNode result = serializer.getSchema(mockProvider, mockType);
        // Assert.assertEquals(expectedValue, result);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        DateTimeSerializerBase<Date> serializer = new CustomDateTimeSerializer();
        JsonFormatVisitorWrapper mockVisitor = null;
        JavaType mockType = null;
        serializer.acceptJsonFormatVisitor(mockVisitor, mockType);
        // Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testSerialize() throws Exception {
        DateTimeSerializerBase<Date> serializer = new CustomDateTimeSerializer();
        JsonGenerator mockGen = null;
        SerializerProvider mockProvider = null;
        Date value = null;
        serializer.serialize(value, mockGen, mockProvider);
        // Assert.assertEquals(expectedValue, actualValue);
    }
}