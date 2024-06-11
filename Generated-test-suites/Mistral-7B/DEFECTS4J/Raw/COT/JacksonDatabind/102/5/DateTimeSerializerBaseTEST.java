package com.example.junit.tests;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {
    private DateTimeSerializerBase<Date> dateTimeSerializer;
    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        this.dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testDefaultSerialization() throws JsonProcessingException {
        Date date = new Date();
        this.dateTimeSerializer = this.dateTimeSerializer.withFormat(Boolean.FALSE, null);
        this.dateTimeSerializer.serialize(date, this.objectMapper.getFactory().createGenerator(this.objectMapper), this.objectMapper.getSerializerProvider());
        JsonParser parser = this.objectMapper.getFactory().createParser(this.objectMapper.getSerializerProvider().getTextValueAsString(dateTimeSerializer.serializeToString(date, this.objectMapper.getSerializerProvider())));
        assertEquals(Long.parseLong(parser.getValueAsString()), date.getTime());
    }

    @Test
    public void testSerializationWithCustomFormat() throws JsonProcessingException, ParseException {
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        this.dateTimeSerializer = this.dateTimeSerializer.withFormat(Boolean.FALSE, customFormat);
        Date date = new Date();
        String expectedResult = customFormat.format(date);
        this.dateTimeSerializer.serialize(date, this.objectMapper.getFactory().createGenerator(this.objectMapper), this.objectMapper.getSerializerProvider());
        String actualResult = this.objectMapper.getFactory().createParser(this.objectMapper.getSerializerProvider().getTextValueAsString(dateTimeSerializer.serializeToString(date, this.objectMapper.getSerializerProvider()))).getValueAsString();
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializationWithInvalidCustomFormat() throws JsonProcessingException {
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss zzz");
        this.dateTimeSerializer = this.dateTimeSerializer.withFormat(Boolean.FALSE, customFormat);
        Date date = new Date();
        this.dateTimeSerializer.serialize(date, this.objectMapper.getFactory().createGenerator(this.objectMapper), this.objectMapper.getSerializerProvider());
    }

    @Test
    public void testSerializationWithTimestampFlag() throws JsonProcessingException {
        this.dateTimeSerializer = this.dateTimeSerializer.withFormat(Boolean.TRUE, null);
        Date date = new Date();
        this.dateTimeSerializer.serialize(date, this.objectMapper.getFactory().createGenerator(this.objectMapper), this.objectMapper.getSerializerProvider());
        JsonParser parser = this.objectMapper.getFactory().createParser(this.objectMapper.getSerializerProvider().getTextValueAsString(dateTimeSerializer.serializeToString(date, this.objectMapper.getSerializerProvider())));
        long timestamp = parser.getValueAsLong();
        assertEquals(date.getTime(), timestamp);
    }

    @Test
    public void testDeserialization() throws JsonMappingException, IOException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String jsonString = inputFormat.format(new Date());
        Date date = this.dateTimeSerializer.deserialize(jsonString, this.objectMapper.getParserFactory().getDateParser(), this.objectMapper.getDeserializationContext());
        assertEquals(inputFormat.parse(jsonString), date);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidFormat() throws JsonMappingException, IOException {
        String jsonString = "2023-03-31-12:59:60";
        this.dateTimeSerializer.deserialize(jsonString, this.objectMapper.getParserFactory().getDateParser(), this.objectMapper.getDeserializationContext());
    }
}