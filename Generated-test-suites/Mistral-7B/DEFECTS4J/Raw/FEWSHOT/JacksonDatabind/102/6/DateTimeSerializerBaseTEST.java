package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseWithDefaultValues() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, null);
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date();
        try {
            JsonNode result = mapper.writeValueAsTree(date, new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, null));
            assertEquals("\"2023-03-23T12:25:34.567Z\"", result.toString());
        } catch (JsonProcessingException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormatAsString() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date();
        try {
            JsonNode result = mapper.writeValueAsTree(date, new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")));
            assertEquals("\"2023-03-23 12:25:34 EDT\"", result.toString());
        } catch (JsonProcessingException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormatThreadSafe() throws JsonProcessingException, InterruptedException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date();
        JsonNode result1 = mapper.writeValueAsTree(date, new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")));
        String result1String = result1.toString();

        JsonGenerationException exception = null;
        for (int i = 0; i < 100; i++) {
            try {
                JsonNode result2 = mapper.writeValueAsTree(date, dateTimeSerializerBase);
                assertEquals(result1String, result2.toString());
            } catch (JsonGenerationException e) {
                exception = e;
                break;
            }
        }
        assertNull(exception);
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormatThreadUnsafe() throws JsonProcessingException, InterruptedException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date();
        JsonNode result1 = mapper.writeValueAsTree(date, new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")));
        String result1String = result1.toString();

        JsonGenerationException exception = null;
        for (int i = 0; i < 100; i++) {
            DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            JsonNode result2 = mapper.writeValueAsTree(date, new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, customFormat));
            if (!result1String.equals(result2.toString())) {
                exception = new JsonGenerationException("Unexpected result: " + result2.toString());
                break;
            }
        }
        assertNotNull(exception);
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormatAndLocale() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH));
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date();
        try {
            JsonNode result = mapper.writeValueAsTree(date, new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH)));
            assertEquals("\"23 mars 2023 12:25:34\"", result.toString());
        } catch (JsonProcessingException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormatAndTimeZone() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", new SimpleTimeZone(0, "UTC")));
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date();
        try {
            JsonNode result = mapper.writeValueAsTree(date, new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", new SimpleTimeZone(0, "UTC"))));
            assertEquals("\"2023-03-23 12:25:34Z\"", result.toString());
        } catch (JsonProcessingException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormatThreadSafeAndLocale() throws JsonProcessingException, InterruptedException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH));
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date();
        JsonNode result1 = mapper.writeValueAsTree(date, new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH)));
        String result1String = result1.toString();

        JsonGenerationException exception = null;
        for (int i = 0; i < 100; i++) {
            try {
                JsonNode result2 = mapper.writeValueAsTree(date, dateTimeSerializerBase);
                assertEquals(result1String, result2.toString());
            } catch (JsonGenerationException e) {
                exception = e;
                break;
            }
        }
        assertNull(exception);
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormatThreadUnsafeAndLocale() throws JsonProcessingException, InterruptedException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH));
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date();
        JsonNode result1 = mapper.writeValueAsTree(date, new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH)));
        String result1String = result1.toString();

        JsonGenerationException exception = null;
        for (int i = 0; i < 100; i++) {
            SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRENCH);
            JsonNode result2 = mapper.writeValueAsTree(date, new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, customFormat));
            if (!result1String.equals(result2.toString())) {
                exception = new JsonGenerationException("Unexpected result: " + result2.toString());
                break;
            }
        }
        assertNotNull(exception);
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormatAndLocaleAndTimeZone() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", new SimpleTimeZone(0, "UTC")));
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date();
        try {
            JsonNode result = mapper.writeValueAsTree(date, new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", new SimpleTimeZone(0, "UTC"))));
            assertEquals("\"2023-03-23 12:25:34Z\"", result.toString());
        } catch (JsonProcessingException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormatThreadSafeAndLocaleAndTimeZone() throws JsonProcessingException, InterruptedException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", new SimpleTimeZone(0, "UTC")));
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date();
        JsonNode result1 = mapper.writeValueAsTree(date, new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", new SimpleTimeZone(0, "UTC"))));
        String result1String = result1.toString();

        JsonGenerationException exception = null;
        for (int i = 0; i < 100; i++) {
            try {
                JsonNode result2 = mapper.writeValueAsTree(date, dateTimeSerializerBase);
                assertEquals(result1String, result2.toString());
            } catch (JsonGenerationException e) {
                exception = e;
                break;
            }
        }
        assertNull(exception);
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormatThreadUnsafeAndLocaleAndTimeZone() throws JsonProcessingException, InterruptedException {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", new SimpleTimeZone(0, "UTC")));
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date();
        JsonNode result1 = mapper.writeValueAsTree(date, new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", new SimpleTimeZone(0, "UTC"))));
        String result1String = result1.toString();

        JsonGenerationException exception = null;
        for (int i = 0; i < 100; i++) {
            SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", new SimpleTimeZone(0, "UTC"));
            JsonNode result2 = mapper.writeValueAsTree(date, new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, customFormat));
            if (!result1String.equals(result2.toString())) {
                exception = new JsonGenerationException("Unexpected result: " + result2.toString());
                break;
            }
        }
        assertNotNull(exception);
    }

    @Test
    public void testDateTimeSerializerBaseWithUseTimestamp() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, Boolean.TRUE, null);
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date();
        try {
            JsonNode result = mapper.writeValueAsTree(date, new DateTimeSerializerBase<Date>(Date.class, Boolean.TRUE, null));
            assertEquals("1679142756000", result.toString());
        } catch (JsonProcessingException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testDateTimeSerializerBaseWithUseTimestampAndCustomFormat() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, Boolean.TRUE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date();
        try {
            JsonNode result = mapper.writeValueAsTree(date, new DateTimeSerializerBase<Date>(Date.class, Boolean.TRUE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z")));
            fail("Expected exception: Cannot use custom format with timestamp serialization");
        } catch (JsonMappingException e) {
            assertEquals("Cannot use custom format with timestamp serialization", e.getMessage());
        }
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormatAndEmptyInstance() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, Boolean.FALSE, null);
        ObjectMapper mapper = new ObjectMapper();

        JsonNode result = mapper.writeValueAsTree(null, dateTimeSerializerBase);
        assertEquals("null", result.toString());
    }

    @Test
    public void testDateTimeSerializerBaseWithUseTimestampAndEmptyInstance() {
        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, Boolean.TRUE, null);
        ObjectMapper mapper = new ObjectMapper();

        JsonNode result = mapper.writeValueAsTree(null, dateTimeSerializerBase);
        assertEquals("null", result.toString());
    }
}