package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase() {
        DateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, defaultFormat);

        // Test empty value
        assertNull(dateTimeSerializerBase.createContextual(null, null));
        Date emptyDate = null;
        assertNull(dateTimeSerializerBase.serialize(emptyDate, null, null));

        // Test serialization with default format
        Date date = new Date();
        String serializedDate = dateTimeSerializerBase.serialize(date, null, null);
        Date deserializedDate = dateTimeSerializerBase.deserialize(JsonParser.parseString(serializedDate), null);
        assertEquals(date, deserializedDate);

        // Test serialization with custom format
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        DateTimeSerializerBase<Date> customDateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
        String serializedCustomDate = customDateTimeSerializerBase.serialize(date, null, null);
        Date deserializedCustomDate = customDateTimeSerializerBase.deserialize(JsonParser.parseString(serializedCustomDate), null);
        assertEquals(date, deserializedCustomDate);

        // Test serialization as timestamp
        DateTimeSerializerBase<Date> timestampDateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, true, null);
        long timestamp = date.getTime();
        String serializedTimestamp = timestampDateTimeSerializerBase.serialize(date, null, null);
        assertEquals(Long.toString(timestamp), serializedTimestamp);
        Date deserializedTimestampDate = timestampDateTimeSerializerBase.deserialize(JsonParser.parseNumber(Long.toString(timestamp)), null);
        assertEquals(date, deserializedTimestampDate);
    }

    @Test
    public void testWithFormat() {
        DateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, defaultFormat);

        // Test serialization with timestamp = true and customFormat = null
        DateTimeSerializerBase<Date> timestampDateTimeSerializerBase = dateTimeSerializerBase.withFormat(Boolean.TRUE, null);
        String serializedTimestamp = timestampDateTimeSerializerBase.serialize(new Date(), null, null);
        assertEquals(Long.toString(System.currentTimeMillis()), serializedTimestamp);

        // Test serialization with timestamp = false and customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z")
        DateTimeFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z");
        DateTimeSerializerBase<Date> customDateTimeSerializerBase = dateTimeSerializerBase.withFormat(Boolean.FALSE, customFormat);
        String serializedCustomDate = customDateTimeSerializerBase.serialize(new Date(), null, null);
        assertEquals(customFormat.format(new Date()), serializedCustomDate);
    }
}