package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseDefault() throws JsonMappingException, JsonGenerationException {
        DateTimeSerializerBase<Date> serializer = new DefaultDateTimeSerializer();

        // Test serialization of null value
        assertNull(serializer.serialize(null, null, null));

        // Test serialization of empty value (timestamp 0)
        Date emptyDate = new Date(0L);
        String emptyJson = serializer.serializeToString(emptyDate);
        assertEquals("\"0\"", emptyJson);

        // Test serialization of a normal date
        Date date = new Date();
        String dateJson = serializer.serializeToString(date);
        String expectedDateFormat = StdDateFormat.DATE_FORMAT_STR_ISO8601;
        assertEquals(expectedDateFormat + ":" + date.getTime(), dateJson);

        // Test deserialization of a valid JSON string
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        Date deserializedDate = mapper.readValue(new TextNode("2019-12-31T23:59:59.000Z"), DateTimeSerializerBase.class.cast(serializer).getType());
        assertEquals(new Date(1577286399000L), deserializedDate);

        // Test deserialization of a null JSON string
        assertNull(mapper.readValue(null, DateTimeSerializerBase.class.cast(serializer).getType()));

        // Test deserialization of an invalid JSON string
        try {
            mapper.readValue(new TextNode("abc"), DateTimeSerializerBase.class.cast(serializer).getType());
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    @Test
    public void testDateTimeSerializerBaseCustomFormat() throws JsonMappingException, JsonGenerationException {
        DateTimeSerializerBase<Date> serializer = new CustomDateTimeSerializer(new SimpleDateFormat("dd-MM-yyyy", Locale.US));

        // Test serialization of a normal date
        Date date = new Date();
        String dateJson = serializer.serializeToString(date);
        assertEquals("\"01-12-2019\"", dateJson);

        // Test deserialization of a valid JSON string
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy", Locale.US));
        Date deserializedDate = mapper.readValue(new TextNode("01-12-2019"), DateTimeSerializerBase.class.cast(serializer).getType());
        assertEquals(new Date(1577286399000L), deserializedDate);

        // Test deserialization of an invalid JSON string
        try {
            mapper.readValue(new TextNode("abc"), DateTimeSerializerBase.class.cast(serializer).getType());
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    private static class DefaultDateTimeSerializer extends DateTimeSerializerBase<Date> {
        DefaultDateTimeSerializer() {
            this(true, null);
        }
    }

    private static class CustomDateTimeSerializer extends DateTimeSerializerBase<Date> {
        CustomDateTimeSerializer(SimpleDateFormat customFormat) {
            super(Date.class, null, customFormat);
        }
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private static class TestDate {
    }
}

class TestData {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private TestDate date;

    public TestDate getDate() {
        return date;
    }

    public void setDate(TestDate date) {
        this.date = date;
    }
}