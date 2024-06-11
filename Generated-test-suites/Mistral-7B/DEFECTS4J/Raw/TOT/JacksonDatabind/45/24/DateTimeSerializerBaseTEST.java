package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonFormat;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseWithTimestamp() throws JsonProcessingException {
        DateTimeSerializerBase<Long> dateTimeSerializerBase = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);

        SerializerProvider serializers = new SerializerProvider();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        SerializerProvider serializerProviders = new SerializerProvider();
        serializerProviders.setSerializerProvider(serializers);

        Long timestamp = Instant.now().getEpochSecond();
        Long value = timestamp;

        dateTimeSerializerBase.serialize(value, jsonGenerator, serializerProviders);
        jsonGenerator.flush();

        JsonParser jsonParser = objectMapper.getFactory().createParser(System.in);
        JsonNode jsonNode = objectMapper.readTree(jsonParser);

        JsonNode expectedJsonNode = objectMapper.valueToTree(timestamp);

        assertEquals(expectedJsonNode, jsonNode);
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomFormat() throws JsonProcessingException {
        DateTimeSerializerBase<String> dateTimeSerializerBase = new DateTimeSerializerBase<String>(String.class, false,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US));

        SerializerProvider serializers = new SerializerProvider();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        serializerProviders.setSerializerProvider(serializers);

        String dateString = "2023-03-14 15:30:12";
        String value = dateString;

        dateTimeSerializerBase.serialize(value, jsonGenerator, serializerProviders);
        jsonGenerator.flush();

        JsonParser jsonParser = objectMapper.getFactory().createParser(System.in);
        JsonNode jsonNode = objectMapper.readTree(jsonParser);

        JsonNode expectedJsonNode = objectMapper.valueToTree(new Date(System.currentTimeMillis()));
        expectedJsonNode.set("@type", "string");
        expectedJsonNode.set("value", dateString);

        assertEquals(expectedJsonNode, jsonNode);
    }

    @Test
    public void testDateTimeSerializerBaseWithJsonDeserialize() throws JsonProcessingException {
        class TestDate {
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
            private Date date;

            public Date getDate() {
                return date;
            }

            public void setDate(Date date) {
                this.date = date;
            }
        }

        DateTimeSerializerBase<TestDate> dateTimeSerializerBase = new DateTimeSerializerBase<TestDate>(TestDate.class, false, null);

        SerializerProvider serializers = new SerializerProvider();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        serializerProviders.setSerializerProvider(serializers);

        TestDate testDate = new TestDate();
        testDate.setDate(new Date());

        dateTimeSerializerBase.serialize(testDate, jsonGenerator, serializerProviders);
        jsonGenerator.flush();

        JsonParser jsonParser = objectMapper.getFactory().createParser(System.in);
        JsonNode jsonNode = objectMapper.readTree(jsonParser);

        StdDeserializer<TestDate> deserializer = objectMapper.findValueDeserializer(TestDate.class, jsonNode);
        TestDate deserializedDate = deserializer.deserialize(jsonNode, TestDate.class, serializerProviders);

        assertEquals(testDate.getDate(), deserializedDate.getDate());
    }

    @Test
    public void testDateTimeSerializerBaseWithCustomSerialization() throws JsonProcessingException {
        class TestDateTime {
            @JsonDeserialize(using = StdDeserializer.class)
            private Date date;

            public Date getDate() {
                return date;
            }

            public void setDate(Date date) {
                this.date = date;
            }
        }

        DateTimeSerializerBase<TestDateTime> dateTimeSerializerBase = new DateTimeSerializerBase<TestDateTime>(TestDateTime.class, false, null) {
            @Override
            protected long _timestamp(TestDateTime value) {
                return value.getDate().getTime();
            }
        };

        SerializerProvider serializers = new SerializerProvider();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonGenerator jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        serializerProviders.setSerializerProvider(serializers);

        TestDateTime testDateTime = new TestDateTime();
        testDateTime.setDate(new Date());

        dateTimeSerializerBase.serialize(testDateTime, jsonGenerator, serializerProviders);
        jsonGenerator.flush();

        JsonParser jsonParser = objectMapper.getFactory().createParser(System.in);
        JsonNode jsonNode = objectMapper.readTree(jsonParser);

        StdDeserializer<TestDateTime> deserializer = objectMapper.findValueDeserializer(TestDateTime.class, jsonNode);
        TestDateTime deserializedDateTime = deserializer.deserialize(jsonNode, TestDateTime.class, serializerProviders);

        assertEquals(testDateTime.getDate(), deserializedDateTime.getDate());
    }
}