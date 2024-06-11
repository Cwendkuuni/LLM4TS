package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializationFeature.WriteDateAsTimestamp;

public class DateTimeSerializerBaseTest {

    @Test
    public void testBasicSerialization() throws JsonProcessingException, JsonGenerationException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();

        // Default serialization (with no custom format)
        Date date = new Date();
        JsonNode jsonNode = mapper.writeValueAsTree(date, new ObjectMapper.DefaultSerializerProvider());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date), jsonNode.toString());

        // Serialization with custom format
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        mapper.setDateFormat(customFormat);
        JsonNode jsonNodeWithCustomFormat = mapper.writeValueAsTree(date, new ObjectMapper.DefaultSerializerProvider());
        assertEquals(customFormat.format(date), jsonNodeWithCustomFormat.toString());

        // Test with null value
        assertNull(mapper.writeValueAsTree(null, new ObjectMapper.DefaultSerializerProvider()));
    }

    @Test
    public void testSerializationWithUseTimestamp() throws JsonProcessingException, JsonGenerationException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();

        // Test with useTimestamp = true
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        Date date = new Date();
        JsonNode jsonNode = mapper.writeValueAsTree(date, new ObjectMapper.DefaultSerializerProvider());
        assertEquals(date.getTime(), jsonNode.longValue());

        // Test with useTimestamp = false and custom format
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        mapper.setDateFormat(customFormat);
        JsonNode jsonNodeWithCustomFormat = mapper.writeValueAsTree(date, new ObjectMapper.DefaultSerializerProvider());
        assertEquals(customFormat.format(date), jsonNodeWithCustomFormat.toString());

        // Test with useTimestamp = false and null custom format
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setDateFormat(null);
        JsonNode jsonNodeWithNullCustomFormat = mapper.writeValueAsTree(date, new ObjectMapper.DefaultSerializerProvider());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date), jsonNodeWithNullCustomFormat.toString());
    }

    @Test
    public void testSerializationWithThreadLocalCustomFormat() throws JsonProcessingException, JsonGenerationException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Date> dateSerializer = new StdDateSerializer();

        // Test with custom format and thread-local reuse
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        mapper.setDateFormat(customFormat);

        // First serialization
        Date date = new Date();
        JsonNode jsonNode1 = mapper.writeValueAsTree(date, new ObjectMapper.DefaultSerializerProvider());
        assertEquals(customFormat.format(date), jsonNode1.toString());

        // Second serialization
        Date date2 = new Date();
        JsonNode jsonNode2 = mapper.writeValueAsTree(date2, new ObjectMapper.DefaultSerializerProvider());
        assertEquals(customFormat.format(date2), jsonNode2.toString());

        // Third serialization
        Date date3 = new Date();
        JsonNode jsonNode3 = mapper.writeValueAsTree(date3, new ObjectMapper.DefaultSerializerProvider());
        assertEquals(customFormat.format(date3), jsonNode3.toString());

        // Test with null custom format
        mapper.setDateFormat(null);
        Date date4 = new Date();
        JsonNode jsonNode4 = mapper.writeValueAsTree(date4, new ObjectMapper.DefaultSerializerProvider());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(date4), jsonNode4.toString());
    }
}