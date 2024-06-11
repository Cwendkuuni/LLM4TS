package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDateTimeSerializerBaseWithDefaultSettings() throws JsonProcessingException {
        DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<LocalDateTime>(
                LocalDateTime.class, false, null);

        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);

        String json = objectMapper.writeValueAsString(dateTime);
        LocalDateTime deserializedDateTime = objectMapper.readValue(json, LocalDateTime.class);

        assertEquals(dateTime, deserializedDateTime);
    }

    @Test
    public void testDateTimeSerializerBaseWithTimestampSetting() throws JsonProcessingException {
        DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<LocalDateTime>(
                LocalDateTime.class, true, null);

        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);

        String json = objectMapper.writeValueAsString(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        long deserializedTimestamp = objectMapper.readValue(json, Long.class);

        assertEquals(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), deserializedTimestamp);
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormat() throws JsonGenerationException, JsonMappingException, IOException {
        DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<LocalDateTime>(
                LocalDateTime.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz"));

        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);

        String json = new StringBuffer()
                .append("[")
                .append("{")
                .append("\"@class\":\"")
                .append(DateTimeSerializerBase.class.getName())
                .append("\",")
                .append("\"_customFormat\":{")
                .append("\"pattern\":\"yyyy-MM-dd HH:mm:ss zzz\",")
                .append("\"timeZone\":\"UTC\",")
                .append("\"locale\":\"en-US\"}")
                .append("}]")
                .append(":")
                .append(dateTime)
                .append("")
                .toString();

        Object readValue = objectMapper.readValue(json, Object.class);
        JsonNode node = objectMapper.readTree(json);
        JsonSerializer<Object> deserializer = objectMapper.findValueSerializer(Object.class, node);

        LocalDateTime deserializedDateTime = (LocalDateTime) deserializer.deserialize(node, LocalDateTime.class, null);

        assertEquals(dateTime, deserializedDateTime);
    }

    @Test(expected = JsonProcessingException.class)
    public void testDateTimeSerializerBaseWithNullValue() throws JsonProcessingException {
        DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<LocalDateTime>(
                LocalDateTime.class, false, null);

        serializer.serialize(null, objectMapper.getFactory().createGenerator(null), objectMapper);
    }

    @Test(expected = JsonMappingException.class)
    public void testDateTimeSerializerBaseWithInvalidFormat() throws JsonProcessingException {
        DateTimeSerializerBase<LocalDateTime> serializer = new DateTimeSerializerBase<LocalDateTime>(
                LocalDateTime.class, false, new SimpleDateFormat("InvalidPattern"));
    }
}