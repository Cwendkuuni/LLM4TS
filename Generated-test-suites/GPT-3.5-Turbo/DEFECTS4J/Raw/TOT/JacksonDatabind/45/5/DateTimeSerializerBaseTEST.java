package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Date> serializer;

    @Before
    public void setUp() {
        serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
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
        };
    }

    @Test
    public void testWithFormat_NullTimestamp_CustomFormatSet_ShouldReturnNonNull() {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        DateTimeSerializerBase<Date> result = serializer.withFormat(null, customFormat);
        assertEquals(customFormat, result._customFormat);
    }

    @Test
    public void testWithFormat_NonNullTimestamp_CustomFormatSet_ShouldReturnNonNull() {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        DateTimeSerializerBase<Date> result = serializer.withFormat(true, customFormat);
        assertEquals(customFormat, result._customFormat);
        assertEquals(true, result._useTimestamp);
    }

    @Test
    public void testWithFormat_NullTimestamp_CustomFormatNotSet_ShouldReturnNonNull() {
        DateTimeSerializerBase<Date> result = serializer.withFormat(null, null);
        assertEquals(null, result._customFormat);
    }

    @Test
    public void testWithFormat_NonNullTimestamp_CustomFormatNotSet_ShouldReturnNonNull() {
        DateTimeSerializerBase<Date> result = serializer.withFormat(true, null);
        assertEquals(null, result._customFormat);
        assertEquals(true, result._useTimestamp);
    }
}