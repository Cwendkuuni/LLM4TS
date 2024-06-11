package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase() throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // Test default instance
        DateTimeSerializerBase<Date> dateTimeSerializer = mapper.findValueSerializer(Date.class);
        assertNull(dateTimeSerializer._customFormat);
        assertTrue(dateTimeSerializer._useTimestamp == false);

        Date date = new Date();

        // Test serialization with default format
        String json = mapper.writeValueAsString(date);
        assertEquals("\"2023-03-15T10:30:45.123Z\"", json);

        // Test serialization with custom format
        JsonFormat<Date> jsonFormat = new JsonFormat.Builder<Date>()
                .shape(JsonFormat.Shape.STRING)
                .pattern("dd-MM-yyyy HH:mm:ss.SSS")
                .locale(Locale.US)
                .timeZone(TimeZone.getTimeZone("UTC"))
                .build();
        mapper.setDateFormat(new SimpleDateFormat(jsonFormat.getPattern(), jsonFormat.getLocale()));
        mapper.setTimeZone(jsonFormat.getTimeZone());

        DateTimeSerializerBase<Date> customDateTimeSerializer = mapper.findValueSerializer(Date.class);
        assertTrue(customDateTimeSerializer._useTimestamp == false);
        assertEquals(jsonFormat.getPattern(), customDateTimeSerializer._customFormat.toPattern());

        // Test serialization with custom format and reused instance
        json = mapper.writeValueAsString(date);
        assertEquals("\"15-03-2023 10:30:45.123\"", json);

        // Test deserialization with default format
        Date deserializedDate = mapper.readValue("\"2023-03-15T10:30:45.123Z\"", Date.class);
        assertEquals(date, deserializedDate);

        // Test deserialization with custom format
        String customJson = "\"15-03-2023 10:30:45.123\"";
        JsonParser jsonParser = mapper.getFactory().createParser(customJson);
        JsonFormatVisitorWrapper visitor = mapper.getFactory().getSerializers().findValueSerializer(Date.class).acceptJsonFormatVisitor(mapper.getSerializationConfig(), mapper.getTypeFactory().constructType(Date.class));
        Date deserializedCustomDate = jsonParser.readValueAs(Date.class, visitor);
        assertEquals(date, deserializedCustomDate);

        // Test serialization and deserialization with JsonFormat annotation
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss.SSS", locale = Locale.US, timezone = "UTC")
        Date jsonFormatAnnotatedDate = new Date();

        // Test serialization with JsonFormat annotation
        json = mapper.writeValueAsString(jsonFormatAnnotatedDate);
        assertEquals("\"15-03-2023 10:30:45.123\"", json);

        // Test deserialization with JsonFormat annotation
        JsonFormat<Date> jsonFormat = new JsonFormat.Builder<Date>()
                .shape(JsonFormat.Shape.STRING)
                .pattern("dd-MM-yyyy HH:mm:ss.SSS")
                .locale(Locale.US)
                .timeZone(TimeZone.getTimeZone("UTC"))
                .build();
        visitor = mapper.getFactory().getSerializers().findValueSerializer(Date.class).acceptJsonFormatVisitor(mapper.getSerializationConfig(), mapper.getTypeFactory().constructType(Date.class));
        deserializedDate = mapper.readValue(json, visitor);
        assertEquals(jsonFormatAnnotatedDate, deserializedDate);
    }
}