package com.example.junit; // replace with your test package

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;
    private DateTimeSerializerBase<Date> dateSerializer;

    @Before
    public void setup() throws IOException {
        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, new DateTimeSerializerBase<Date>(Date.class, false, null));
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);
        dateSerializer = (DateTimeSerializerBase<Date>) objectMapper.getTypeFactory()
                .constructValueType(DateTimeSerializerBase.class, Date.class);
    }

    @Test
    public void testDefaultSerialization() throws JsonProcessingException {
        Date date = new Date();
        String result = dateSerializer.serialize(date, null, new SerializerProvider().builder().build());
        assertEquals("Expected default serialization to be a valid JSON string",
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(date), result);
    }

    @Test
    public void testSerializationWithCustomFormat() throws JsonProcessingException {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeSerializerBase<Date> customSerializer = dateSerializer.withFormat(false, customFormat);
        Date date = new Date();
        String result = customSerializer.serialize(date, null, new SerializerProvider().builder().build());
        assertEquals("Expected custom serialization to match the format",
                customFormat.format(date), result);
    }

    @Test
    public void testSerializationAsTimestamp() throws JsonProcessingException {
        DateTimeSerializerBase<Date> timestampSerializer = dateSerializer.withFormat(true, null);
        Date date = new Date();
        String result = timestampSerializer.serialize(date, null, new SerializerProvider().builder().build());
        assertEquals("Expected timestamp serialization to be a valid JSON number",
                Long.toString(date.getTime()), result);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithNullDate() throws JsonProcessingException {
        dateSerializer.serialize(null, null, new SerializerProvider().builder().build());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithInvalidCustomFormat() throws JsonProcessingException {
        DateFormat invalidFormat = new SimpleDateFormat("invalid_pattern");
        DateTimeSerializerBase<Date> invalidSerializer = dateSerializer.withFormat(false, invalidFormat);
        Date date = new Date();
        String result = invalidSerializer.serialize(date, null, new SerializerProvider().builder().build());
        // The test will fail with a JsonMappingException
    }

    @Test
    public void testDeserialization() throws JsonProcessingException, ParseException {
        String jsonString = "\"2022-03-23 14:30:15\"";
        Date date = objectMapper.readValue(jsonString, Date.class);
        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date deserializedDate = expectedFormat.parse(jsonString);
        assertEquals("Expected deserialization to match the input string",
                deserializedDate, date);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializationWithInvalidJsonString() throws JsonProcessingException {
        String invalidJsonString = "invalid_json_string";
        objectMapper.readValue(invalidJsonString, Date.class);
        // The test will fail with a JsonProcessingException
    }
}