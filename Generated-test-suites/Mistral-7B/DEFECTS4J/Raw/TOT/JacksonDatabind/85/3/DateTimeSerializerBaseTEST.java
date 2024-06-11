package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseWithDefaultSettings() throws JsonProcessingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();

        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);

        Date date = new Date();

        String serializedDate = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(date);
        JsonNode jsonNode = mapper.readTree(serializedDate);

        JsonNode expectedJsonNode = mapper.createObjectNode();
        expectedJsonNode.put("@type", "number");
        expectedJsonNode.put("value", mapper.writerWithRootValue(date).writeValueAsString(date));

        Assert.assertEquals(expectedJsonNode, jsonNode);
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormat() throws JsonProcessingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy"));

        Date date = new Date();

        String serializedDate = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(date);
        JsonNode jsonNode = mapper.readTree(serializedDate);

        JsonNode expectedJsonNode = mapper.createObjectNode();
        expectedJsonNode.put("@type", "string");
        expectedJsonNode.put("value", "11-Jun-2023");

        Assert.assertEquals(expectedJsonNode, jsonNode);
    }

    @Test
    public void testDateTimeSerializerBaseWithTimestampSetting() throws JsonProcessingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();

        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, true, null);

        Date date = new Date();

        String serializedDate = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(date);
        JsonNode jsonNode = mapper.readTree(serializedDate);

        JsonNode expectedJsonNode = mapper.createObjectNode();
        expectedJsonNode.put("@type", "number");
        expectedJsonNode.put("value", date.getTime());

        Assert.assertEquals(expectedJsonNode, jsonNode);
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormatAndTimestampSetting() throws JsonProcessingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, true, new SimpleDateFormat("dd-MM-yyyy"));

        Date date = new Date();

        String serializedDate = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(date);
        JsonNode jsonNode = mapper.readTree(serializedDate);

        JsonNode expectedJsonNode = mapper.createObjectNode();
        expectedJsonNode.put("@type", "number");
        expectedJsonNode.put("value", date.getTime());

        Assert.assertEquals(expectedJsonNode, jsonNode);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDateTimeSerializerBaseWithNullSerializerProvider() throws JsonProcessingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();

        DateTimeSerializerBase<Date> dateTimeSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);

        dateTimeSerializerBase.createContextual(null, null);
    }
}