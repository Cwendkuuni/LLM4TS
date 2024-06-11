package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private static ObjectMapper mapper = new JsonMapper();

    @Test
    public void testDateTimeSerializerBaseWithDefaultSettings() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);

        Date date = new Date();

        ObjectNode node = mapper.writeValueAsTree(date);
        assertEquals("\"2022-12-31T23:59:59.000Z\"", node.toString());

        Date deserializedDate = mapper.readValue(node.toString(), Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormat() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a z"));

        Date date = new Date();

        ObjectNode node = mapper.writeValueAsTree(date);
        assertEquals("\"25-01-2022 10:15:20 AM CET\"", node.toString());

        Date deserializedDate = mapper.readValue(node.toString(), Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test
    public void testDateTimeSerializerBaseWithTimestamp() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, true, null);

        Date date = new Date();

        ObjectNode node = mapper.writeValueAsTree(date);
        assertEquals("2524604800000", node.get(0).asText());

        Date deserializedDate = mapper.readValue(node.toString(), Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test(expected = JsonMappingException.class)
    public void testDateTimeSerializerBaseWithInvalidFormat() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a zzz"));

        Date date = new Date();

        ObjectNode node = mapper.writeValueAsTree(date);

        SerializerProvider serializers = mapper.getSerializerProvider();
        JsonFormatVisitorWrapper visitor = serializers.getConfig().findSerializerProviderDefaultFactory(JsonFormat.Value.class, null);
        dateSerializer.acceptJsonFormatVisitor(visitor, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testDateTimeSerializerBaseWithInvalidFormatDuringDeserialization() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a zzz"));

        ObjectNode node = mapper.createObjectNode();
        node.put("date", "\"25-01-2022 10:15:20 AM CET\"");

        dateSerializer.deserialize(null, null, node, null);
    }

    @Test
    public void testDateTimeSerializerBaseWithLocale() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a z", Locale.FRENCH));

        Date date = new Date();

        ObjectNode node = mapper.writeValueAsTree(date);
        assertEquals("\"25-01-2022 10:15:20

        heume lundi 25 janvier 2022\"", node.toString());

        Date deserializedDate = mapper.readValue(node.toString(), Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test
    public void testDateTimeSerializerBaseWithTimeZone() throws JsonProcessingException, IOException {
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("dd-MM-yyyy hh:mm:ss z", Locale.ENGLISH));

        Date date = new Date();

        ObjectNode node = mapper.writeValueAsTree(date);
        assertEquals("\"25-01-2022 10:15:20+01:00\"", node.toString());

        Date deserializedDate = mapper.readValue(node.toString(), Date.class);
        assertEquals(date, deserializedDate);
    }

    @Test
    public void testDateTimeSerializerBaseWithFormatOverrides() throws JsonProcessingException, IOException {
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.FRENCH);
        format.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        format.setPattern("dd-MM-yyyy hh:mm:ss a z");

        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
        DateTimeSerializerBase<Date> serializedDateSerializer
                = dateSerializer.withFormat(false, format);

        Date date = new Date();

        ObjectNode node = mapper.writeValueAsTree(date);
        assertEquals("\"25-01-2022 10:15:20 heume lundi 25 janvier 2022\"", node.toString());

        Date deserializedDate = mapper.readValue(node.toString(), Date.class);
        assertEquals(date, deserializedDate);
    }
}