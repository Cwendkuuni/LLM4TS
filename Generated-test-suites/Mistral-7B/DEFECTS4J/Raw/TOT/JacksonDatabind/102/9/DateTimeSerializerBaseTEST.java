package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testDefaultSerialization() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer();
        Date date = new Date();

        String actual = mapper.writeValueAsString(date, serializer);
        String expected = _getDefaultFormat().format(date);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCustomSerialization() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer(null, null, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

        Date date = new Date();

        String actual = mapper.writeValueAsString(date, serializer);
        String expected = "dd-MM-yyyy HH:mm:ss";

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializationWithLocale() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer(null, null, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        format.setLocale(Locale.US);

        Date date = new Date();

        String actual = mapper.writeValueAsString(date, serializer);
        String expected = format.format(date);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializationWithTimeZone() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer(null, null, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = new Date();

        String actual = mapper.writeValueAsString(date, serializer);
        String expected = format.format(date);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializationWithFormat() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

        Date date = new Date();

        String actual = mapper.writeValueAsString(date, serializer);
        String expected = "dd-MM-yyyy HH:mm:ss";

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializationWithFormatAndLocale() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        format.setLocale(Locale.US);

        Date date = new Date();

        String actual = mapper.writeValueAsString(date, serializer);
        String expected = format.format(date);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializationWithFormatAndTimeZone() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = new Date();

        String actual = mapper.writeValueAsString(date, serializer);
        String expected = format.format(date);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserialization() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer();

        String json = "\"2022-12-31T23:59:59.000Z\"";

        Date actual = mapper.readValue(json, Date.class, serializer);
        Date expected = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(json);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializationWithFormat() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

        String json = "\"31-12-2022 23:59:59\"";

        Date actual = mapper.readValue(json, Date.class, serializer);
        Date expected = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(json);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializationWithFormatAndLocale() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        format.setLocale(Locale.US);

        String json = "\"31-12-2022 23:59:59\"";

        Date actual = mapper.readValue(json, Date.class, serializer);
        Date expected = format.parse(json);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializationWithFormatAndTimeZone() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        String json = "\"31-12-2022 23:59:59\"";

        Date actual = mapper.readValue(json, Date.class, serializer);
        Date expected = format.parse(json);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDeserializationWithFormatAndLocaleAndTimeZone() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        String json = "\"31-12-2022 23:59:59\"";

        Date actual = mapper.readValue(json, Date.class, serializer);
        Date expected = format.parse(json);

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidFormat() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer();

        String json = "\"Invalid date\"";

        mapper.readValue(json, Date.class, serializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidFormatAndLocale() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        format.setLocale(Locale.US);

        String json = "\"Invalid date\"";

        mapper.readValue(json, Date.class, serializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidFormatAndTimeZone() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        String json = "\"Invalid date\"";

        mapper.readValue(json, Date.class, serializer);
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializationWithInvalidFormatAndLocaleAndTimeZone() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer(false, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        String json = "\"Invalid date\"";

        mapper.readValue(json, Date.class, serializer);
    }

    @Test
    public void testSerializationWithEmptyValue() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer();

        String actual = mapper.writeValueAsString(null, serializer);
        TextNode expected = mapper.valueToTree("null");

        Assert.assertEquals(expected, mapper.readTree(actual));
    }

    @Test
    public void testSerializationWithEmptyValueAndFormat() throws JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer(false, null);

        String actual = mapper.writeValueAsString(null, serializer);
        TextNode expected = mapper.valueToTree("null");

        Assert.assertEquals(expected, mapper.readTree(actual));
    }

    private SimpleDateFormat _getDefaultFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }
}