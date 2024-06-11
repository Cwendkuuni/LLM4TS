package com.example.junit;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

public class DateTimeSerializerBaseTest {

    private static final ObjectMapper mapper = new JsonMapper();

    @Test
    public void testDateTimeSerializerBase() throws Exception {
        Date date = new Date();
        DateTimeSerializerBase<Date> serializer = new StdDateSerializer();

        // Test serialization
        String json = mapper.writeValueAsString(date);
        Date deserializedDate = mapper.readValue(json, Date.class);
        assertEquals(date, deserializedDate);

        // Test with custom format
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Date> customSerializer = new StdDateSerializer()
                .withFormat(Boolean.FALSE, customFormat);
        String jsonWithCustomFormat = mapper.writeValueAsString(date);
        assertEquals(jsonWithCustomFormat, mapper.writeValueAsString(deserializedDate, customSerializer));

        // Test with timestamp flag
        DateTimeSerializerBase<Date> timestampSerializer = new StdDateSerializer()
                .withFormat(Boolean.TRUE, null);
        long timestamp = serializer._timestamp(date);
        assertEquals(timestamp, timestampSerializer._timestamp(date));

        // Test isEmpty method
        assertFalse(serializer.isEmpty(mapper.getSerializerProvider(), null));
        assertFalse(serializer.isEmpty(mapper.getSerializerProvider(), date));
    }

    @Test
    public void testDateTimeSerializerBaseContextualSerialization() throws Exception {
        Date date = new Date();

        ObjectMapper mapper = new JsonMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        JsonSerializer<Date> serializer = mapper.findValueSerializer(Date.class);
        JsonSerializer<Date> serializedSerializer = serializer
                .withContextualHandler(new MyContextualSerializer());

        String json = mapper.writeValueAsString(date);
        Date deserializedDate = mapper.readValue(json, Date.class);
        assertEquals(date, deserializedDate);

        JsonParser parser = mapper.getFactory().createParser(json);
        Date deserializedDateFromParser = serializedSerializer.deserialize(parser, Date.class);
        assertEquals(date, deserializedDateFromParser);
    }

    static class MyContextualSerializer extends StdContextualSerializers.StdDateSerializer {
        @Override
        public JsonSerializer<Date> createContextual(SerializerProvider serializers, BeanProperty property)
                throws JsonMappingException {
            Boolean useTimestamp = serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            DateFormat customFormat = serializers.findFormat(property, Date.class);
            return new StdDateSerializer()
                    .withFormat(useTimestamp, customFormat);
        }
    }
}