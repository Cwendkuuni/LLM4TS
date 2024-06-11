package com.example.junit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.StdDateFormat;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Date> dateTimeSerializer;
    private SerializerProvider serializers;
    private JsonGenerator gen;
    private JsonParser parser;

    @Before
    public void setup() {
        serializers = mock(SerializerProvider.class);
        gen = mock(JsonGenerator.class);
        parser = mock(JsonParser.class);
        ObjectCodec objCodec = mock(ObjectCodec.class);
        when(serializers.getDefaultCodec()).thenReturn(objCodec);
        when(objCodec.findValueSerializer(Date.class)).thenReturn(dateTimeSerializer);
        dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
    }

    @Test
    public void testDefaultSerialization() throws JsonProcessingException, IOException {
        Date date = new Date();
        dateTimeSerializer.serialize(date, gen, serializers);
        String expectedJson = "{\"@type\":\"Date\",\"@value\":\"";
        expectedJson += new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(date);
        expectedJson += "\"}";
        String actualJson = getJsonFromJsonGenerator(gen);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializationWithFormat() throws JsonProcessingException, IOException {
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateTimeSerializerBase<Date> customSerializer = dateTimeSerializer.withFormat(false, customFormat);
        Date date = new Date();
        customSerializer.serialize(date, gen, serializers);
        String expectedJson = "{\"@type\":\"String\",\"@value\":\"dd-MM-yyyy HH:mm:ss\"}";
        String actualJson = getJsonFromJsonGenerator(gen);
        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializationAsTimestamp() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> timestampSerializer = dateTimeSerializer.withFormat(true, null);
        Date date = new Date();
        timestampSerializer.serialize(date, gen, serializers);
        String expectedJson = "{\"@type\":\"Number\",\"@value\":\"" + date.getTime() + "\"}";
        String actualJson = getJsonFromJsonGenerator(gen);
        assertEquals(expectedJson, actualJson);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithInvalidFormat() throws JsonProcessingException, IOException {
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:sszzz");
        DateTimeSerializerBase<Date> customSerializer = dateTimeSerializer.withFormat(false, customFormat);
        Date date = new Date();
        customSerializer.serialize(date, gen, serializers);
    }

    @Test
    public void testDeserialization() throws JsonProcessingException, ParseException, IOException {
        String json = "{\"@type\":\"Date\",\"@value\":\"2023-03-01T01:23:45.123+02:00\"}";
        JsonNode node = MapperFactory.getMapper().readTree(json);
        Date deserializedDate = dateTimeSerializer.deserialize(node, serializers);
        assertEquals(new Date(), deserializedDate);
    }

    @Test
    public void testDeserializationWithFormat() throws JsonProcessingException, ParseException, IOException {
        String json = "{\"@type\":\"String\",\"@value\":\"dd-MM-yyyy HH:mm:ss\"}";
        JsonNode node = MapperFactory.getMapper().readTree(json);
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        DateTimeSerializerBase<Date> customSerializer = dateTimeSerializer.withFormat(false, customFormat);
        Date deserializedDate = customSerializer.deserialize(node, serializers);
        assertEquals(new Date(), deserializedDate);
    }

    @Test
    public void testDeserializationAsTimestamp() throws JsonProcessingException, IOException {
        String json = "{\"@type\":\"Number\",\"@value\":\"1678259235000\"}";
        JsonNode node = MapperFactory.getMapper().readTree(json);
        DateTimeSerializerBase<Date> timestampSerializer = dateTimeSerializer.withFormat(true, null);
        Date deserializedDate = timestampSerializer.deserialize(node, serializers);
        assertEquals(new Date(), deserializedDate);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidJson() throws JsonProcessingException, IOException {
        String json = "{\"@type\":\"InvalidType\",\"@value\":\"Invalid Value\"}";
        JsonNode node = MapperFactory.getMapper().readTree(json);
        dateTimeSerializer.deserialize(node, serializers);
    }

    private String getJsonFromJsonGenerator(JsonGenerator generator) throws JsonProcessingException, IOException {
        StringWriter stringWriter = new StringWriter();
        generator.writeValue(stringWriter);
        stringWriter.flush();
        return stringWriter.toString();
    }
}