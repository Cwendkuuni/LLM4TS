package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase() {
        ObjectMapper mapper = new ObjectMapper();

        // Test with default serializer
        DateTimeSerializerBase<Date> dateSerializer = mapper.findValueSerializer(Date.class);
        assertEquals(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new Date()),
                dateSerializer.serialize(new Date(0), null, null));

        // Test with custom format serializer
        DateTimeSerializerBase<Date> customFormatSerializer = mapper.findValueSerializer(Date.class)
                .withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US));
        Date date = new Date();
        String expectedResult = "2023-03-22 12:34:56";
        assertEquals(expectedResult, customFormatSerializer.serialize(date, null, null).toString());

        // Test with timestamp serializer
        DateTimeSerializerBase<Date> timestampSerializer = mapper.findValueSerializer(Date.class)
                .withFormat(Boolean.TRUE, null);
        assertEquals(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new Date(0).getTime()),
                timestampSerializer.serialize(null, null, null).toString());

        // Test with null value
        assertEquals("null", dateSerializer.serialize(null, null, null).toString());

        // Test serialization with JsonFormat annotation
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        Date jsonFormatDate = new Date();
        JsonSerializer<Date> jsonFormatSerializer = mapper.findValueSerializer(Date.class);
        String jsonFormatExpectedResult = "\"2023-03-22 12:34:56\"";
        assertEquals(jsonFormatExpectedResult, jsonFormatSerializer.serialize(jsonFormatDate, null, null).toString());

        // Test deserialization with custom format deserializer
        JsonParser jsonParser = mapper.getFactory().createParser("\"2023-03-22 12:34:56\"");
        JsonDeserializer<Date> customFormatDeserializer = mapper.findDeserializer(Date.class)
                .withContextualHandler(new StandardDeserializer<>(Date.class) {
                    @Override
                    public Date deserialize(JsonParser p, DeserializationContext ctxt)
                            throws IOException {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        return df.parse(p.getValueAsString());
                    }
                });
        assertEquals(new Date(), customFormatDeserializer.deserialize(jsonParser, null));

        // Test deserialization with timestamp deserializer
        JsonParser timestampJsonParser = mapper.getFactory().createParser("0");
        JsonDeserializer<Date> timestampDeserializer = mapper.findDeserializer(Date.class)
                .withContextualHandler(new StdDeserializer<Date>(Date.class) {
                    @Override
                    public Date deserialize(JsonParser p, DeserializationContext ctxt)
                            throws IOException {
                        return new Date(p.getValueAsLong());
                    }
                });
        assertEquals(new Date(0), timestampDeserializer.deserialize(timestampJsonParser, null));

        // Test deserialization with JsonFormat annotation
        JsonParser jsonFormatJsonParser = mapper.getFactory().createParser("\"2023-03-22 12:34:56\"");
        JsonDeserializer<Date> jsonFormatDeserializer = mapper.findDeserializer(Date.class);
        assertEquals(new Date(), jsonFormatDeserializer.deserialize(jsonFormatJsonParser, null));

        // Test with locale and timezone
        DateTimeSerializerBase<Date> customFormatWithLocaleAndTimeZoneSerializer = mapper.findValueSerializer(Date.class)
                .withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE, TimeZone.getTimeZone("Asia/Shanghai")));
        String expectedResultWithLocaleAndTimeZone = "².²¹³-³³-²³ ²³:²³:²³";
        assertEquals(expectedResultWithLocaleAndTimeZone, customFormatWithLocaleAndTimeZoneSerializer.serialize(date, null, null).toString());

        // Test with null custom format
        DateTimeSerializerBase<Date> nullCustomFormatSerializer = new DateTimeSerializerBase<Date>(Date.class, null, null);
        assertEquals(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(new Date()),
                nullCustomFormatSerializer.serialize(new Date(0), null, null));
    }
}