package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdSerializers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testSerializationWithDefaultFormat() throws JsonProcessingException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = new StdDateSerializer();
        JsonNode result = mapper.writeValueAsTree(new java.util.Date(), dateSerializer);
        assertEquals("\"2023-03-24T15:30:45.000Z\"", result.toString());
    }

    @Test
    public void testSerializationWithCustomFormat() throws JsonProcessingException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = new StdDateSerializer()
                .withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        JsonNode result = mapper.writeValueAsTree(new java.util.Date(), dateSerializer);
        assertEquals("\"2023-03-24 15:30:45 EDT\"", result.toString());
    }

    @Test
    public void testSerializationWithTimestampFormat() throws JsonProcessingException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = new StdDateSerializer()
                .withFormat(Boolean.TRUE, null);
        JsonNode result = mapper.writeValueAsTree(new java.util.Date(), dateSerializer);
        assertEquals("1679547445000", result.toString());
    }

    @Test
    public void testSerializationWithJsonFormatAnnotation() throws JsonProcessingException {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z")
        static class DateWithCustomFormat {
            private java.util.Date date;

            public DateWithCustomFormat(java.util.Date date) {
                this.date = date;
            }

            public java.util.Date getDate() {
                return date;
            }
        }

        DateTimeSerializerBase<DateWithCustomFormat> dateSerializer = mapper.getTypeFactory()
                .constructType(DateTimeSerializerBase.class, DateWithCustomFormat.class);
        JsonNode result = mapper.writeValueAsTree(new DateWithCustomFormat(new java.util.Date()), dateSerializer);
        assertEquals("\"2023-03-24 15:30:45 EDT\"", result.toString());
    }

    @Test
    public void testSerializationWithEmptyValue() throws JsonProcessingException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = new StdDateSerializer();
        JsonNode result = mapper.writeValueAsTree(null, dateSerializer);
        assertTrue(result.isNull());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializationWithNullSerializerProvider() throws JsonProcessingException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = new StdDateSerializer();
        dateSerializer.createContextual(null, null);
    }

    @Test
    public void testDeserializationWithDefaultFormat() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = new StdDateSerializer();
        Object deserializedValue = mapper.readValue(new TextNode("\"2023-03-24T15:30:45.000Z\""),
                dateSerializer.getClass());
        assertEquals(new java.util.Date(), deserializedValue);
    }

    @Test
    public void testDeserializationWithCustomFormat() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = new StdDateSerializer()
                .withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        Object deserializedValue = mapper.readValue(new TextNode("\"2023-03-24 15:30:45 EDT\""),
                dateSerializer.getClass());
        assertEquals(new java.util.Date(), deserializedValue);
    }

    @Test
    public void testDeserializationWithTimestampFormat() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = new StdDateSerializer()
                .withFormat(Boolean.TRUE, null);
        Object deserializedValue = mapper.readValue(new TextNode("1679547445000"), dateSerializer.getClass());
        assertEquals(Instant.now().atZone(ZoneId.systemDefault()).toInstant().minus(1679547445000L).toDate(), deserializedValue);
    }

    @Test
    public void testDeserializationWithJsonFormatAnnotation() throws JsonProcessingException, IOException {
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z")
        static class DateWithCustomFormat {
            private java.util.Date date;

            public DateWithCustomFormat() {
            }

            public java.util.Date getDate() {
                return date;
            }

            public void setDate(java.util.Date date) {
                this.date = date;
            }
        }

        DateTimeSerializerBase<DateWithCustomFormat> dateSerializer = mapper.getTypeFactory()
                .constructType(DateTimeSerializerBase.class, DateWithCustomFormat.class);
        Object deserializedValue = mapper.readValue(new TextNode("\"2023-03-24 15:30:45 EDT\""), dateSerializer.getClass());
        assertTrue(deserializedValue instanceof DateWithCustomFormat);
        assertEquals(new java.util.Date(), ((DateWithCustomFormat) deserializedValue).getDate());
    }

    @Test
    public void testDeserializationWithEmptyString() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = new StdDateSerializer();
        Object deserializedValue = mapper.readValue(new TextNode(""), dateSerializer.getClass());
        assertTrue(deserializedValue == null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidJsonFormat() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = new StdDateSerializer();
        Object deserializedValue = mapper.readValue(new TextNode("\"invalid\""), dateSerializer.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithNullValue() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = new StdDateSerializer();
        Object deserializedValue = mapper.readValue(new TextNode(null), dateSerializer.getClass());
        assertEquals(null, deserializedValue);
    }
}