package com.example.junit;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Date> dateTimeSerializer;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDefaultSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        Date date = new Date();
        JsonNode result = objectMapper.writeValueAsTree(date, new SerializerProvider());
        Assert.assertEquals("\"2023-03-28T14:15:30.000Z\"", result.toString());
    }

    @Test
    public void testCustomFormatSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Date> customSerializer = dateTimeSerializer.withFormat(false, customFormat);

        Date date = new Date();
        JsonNode result = objectMapper.writeValueAsTree(date, new SerializerProvider());
        JsonFormatVisitorWrapper visitor = objectMapper.getSerializationConfig().findRootValueSerializer(Date.class);
        customSerializer.acceptJsonFormatVisitor(visitor, null);

        ObjectNode rootNode = (ObjectNode) result;
        ObjectNode dateNode = rootNode.path("@");
        Assert.assertEquals("\"dd-MM-yyyy HH:mm:ss\"", dateNode.get("format").asText());
        Assert.assertEquals("dd-MM-yyyy HH:mm:ss", dateNode.get("valueAsString").asText());
    }

    @Test
    public void testTimestampSerialization() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> timestampSerializer = dateTimeSerializer.withFormat(true, null);

        Date date = new Date();
        JsonNode result = objectMapper.writeValueAsTree(date, new SerializerProvider());
        Assert.assertEquals("2023-03-28T14:15:30.000Z", result.get(0).asText());
    }

    @Test
    public void testSerializationWithCustomLocale() throws JsonGenerationException, JsonMappingException, IOException {
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.FRENCH);
        DateTimeSerializerBase<Date> customSerializer = dateTimeSerializer.withFormat(false, customFormat);

        Date date = new Date();
        JsonNode result = objectMapper.writeValueAsTree(date, new SerializerProvider());
        JsonFormatVisitorWrapper visitor = objectMapper.getSerializationConfig().findRootValueSerializer(Date.class);
        customSerializer.acceptJsonFormatVisitor(visitor, null);

        ObjectNode rootNode = (ObjectNode) result;
        ObjectNode dateNode = rootNode.path("@");
        Assert.assertEquals("\"dd-MM-yyyy HH:mm:ss\"", dateNode.get("format").asText());
        Assert.assertEquals("28-03-2023 14:15:30", dateNode.get("valueAsString").asText());
    }

    @Test
    public void testSerializationWithCustomTimeZone() throws JsonGenerationException, JsonMappingException, IOException {
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        customFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        DateTimeSerializerBase<Date> customSerializer = dateTimeSerializer.withFormat(false, customFormat);

        Date date = new Date();
        JsonNode result = objectMapper.writeValueAsTree(date, new SerializerProvider());
        JsonFormatVisitorWrapper visitor = objectMapper.getSerializationConfig().findRootValueSerializer(Date.class);
        customSerializer.acceptJsonFormatVisitor(visitor, null);

        ObjectNode rootNode = (ObjectNode) result;
        ObjectNode dateNode = rootNode.path("@");
        Assert.assertEquals("\"dd-MM-yyyy HH:mm:ss\"", dateNode.get("format").asText());
        Assert.assertEquals("28-03-2023 22:35:30", dateNode.get("valueAsString").asText());
    }

    @Test
    public void testSerializationWithCustomLocaleAndTimeZone() throws JsonGenerationException, JsonMappingException, IOException {
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        customFormat.setLocale(Locale.FRENCH);
        customFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        DateTimeSerializerBase<Date> customSerializer = dateTimeSerializer.withFormat(false, customFormat);

        Date date = new Date();
        JsonNode result = objectMapper.writeValueAsTree(date, new SerializerProvider());
        JsonFormatVisitorWrapper visitor = objectMapper.getSerializationConfig().findRootValueSerializer(Date.class);
        customSerializer.acceptJsonFormatVisitor(visitor, null);

        ObjectNode rootNode = (ObjectNode) result;
        ObjectNode dateNode = rootNode.path("@");
        Assert.assertEquals("\"dd-MM-yyyy HH:mm:ss\"", dateNode.get("format").asText());
        Assert.assertEquals("28-03-2023 22:35:30", dateNode.get("valueAsString").asText());
    }

    @Test
    public void testSerializationWithEmptyValue() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<Date> emptySerializer = dateTimeSerializer.withFormat(false, null);
        Date date = null;
        JsonNode result = objectMapper.writeValueAsTree(date, new SerializerProvider());
        Assert.assertEquals("null", result.toString());
    }

    @Test
    public void testDeserialization() throws JsonMappingException, IOException {
        String json = "\"2023-03-28T14:15:30.000Z\"";
        Date date = objectMapper.readValue(json, Date.class);
        Assert.assertEquals("Tue Mar 28 14:15:30 GMT", date.toString());
    }

    @Test
    public void testDeserializationWithCustomFormat() throws JsonMappingException, IOException {
        String json = "\"28-03-2023 14:15:30\"";
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Date> customSerializer = dateTimeSerializer.withFormat(false, customFormat);
        ObjectMapper customObjectMapper = new ObjectMapper();
        customObjectMapper.setDefaultSerializerProvider(new SerializerProvider() {
            @Override
            public <T> JsonSerializer<T> findSerializer(Class<?> type, JsonSerializer<T> serializer) {
                if (type == Date.class) {
                    return customSerializer;
                }
                return super.findSerializer(type, serializer);
            }
        });
        Date date = customObjectMapper.readValue(json, Date.class);
        Assert.assertEquals("Tue Mar 28 14:15:30 GMT", date.toString());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidFormat() throws JsonMappingException, IOException {
        String json = "\"Invalid format\"";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.readValue(json, Date.class);
    }
}