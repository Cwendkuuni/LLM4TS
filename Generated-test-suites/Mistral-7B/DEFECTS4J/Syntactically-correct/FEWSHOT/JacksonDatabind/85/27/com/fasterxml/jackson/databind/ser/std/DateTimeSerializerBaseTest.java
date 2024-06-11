package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseUseTimestamp() throws JsonMappingException, JsonProcessingException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
        ObjectMapper mapper = new ObjectMapper();

        Long timestamp = 1633023421000L;
        String json = mapper.writeValueAsString(timestamp, serializer);
        Assert.assertEquals("1633023421000", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Long deserialized = mapper.readValue(parser, Long.class, serializer);
        Assert.assertEquals(timestamp, deserialized);
    }

    @Test
    public void testDateTimeSerializerBaseCustomFormatString() throws JsonMappingException, JsonProcessingException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date();
        String json = mapper.writeValueAsString(date, serializer);
        Assert.assertEquals("2022-05-03 12:34:56", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Date deserialized = mapper.readValue(parser, Date.class, serializer);
        Assert.assertEquals(date, deserialized);
    }

    @Test
    public void testDateTimeSerializerBaseCustomFormatLocale() throws JsonMappingException, JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm"));
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date();
        Locale locale = Locale.forLanguageTag("fr-FR");
        String json = mapper.writeValueAsString(date, serializer, locale);
        Assert.assertEquals("03-05-2022 12:34", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Date deserialized = mapper.readValue(parser, Date.class, serializer, locale);
        Assert.assertEquals(date, deserialized);
    }

    @Test
    public void testDateTimeSerializerBaseCustomFormatTimeZone() throws JsonMappingException, JsonProcessingException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy HH:mm z"));
        ObjectMapper mapper = new ObjectMapper();

        Date date = new Date();
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        String json = mapper.writeValueAsString(date, serializer, null, timeZone);
        Assert.assertEquals("03-05-2022 13:34 CET", json);

        JsonParser parser = mapper.getFactory().createParser(json);
        Date deserialized = mapper.readValue(parser, Date.class, serializer, null, timeZone);
        Assert.assertEquals(date, deserialized);
    }
}