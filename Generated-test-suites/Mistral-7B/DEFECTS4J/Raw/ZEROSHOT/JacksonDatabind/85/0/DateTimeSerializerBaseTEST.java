package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @org.junit.Test
    public void testDateTimeSerializerBase() throws IOException, ParseException {
        // Test empty value
        assertEquals(null, mapper.readValue(mapper.writeValueAsString(new DateTimeSerializerBaseTest.DummyDate())).getValueAs(DummyDate.class));

        // Test serialization with default settings
        DummyDate date = new DummyDate(new java.util.Date());
        JsonNode json = mapper.writeValueAsTree(date);
        assertEquals(JsonNode.ValueType.STRING, json.getValueAs(JsonNode.class).getType());
        String dateString = json.get(0).asText();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");
        assertSame(dateFormat.parse(dateString), date.getValue());

        // Test serialization as timestamp
        date = new DummyDate(new java.util.Date(1516044520000L)); // Unix timestamp for Mon Jun 11 15:25:20 CEST 2018
        json = mapper.writeValueAsTree(date);
        assertEquals(JsonNode.ValueType.NUMBER, json.getValueAs(JsonNode.class).getType());
        assertEquals(date.getValue().getTime(), json.get(0).asLong());

        // Test serialization with custom format
        date = new DummyDate(new java.util.Date(), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"), Locale.ENGLISH, TimeZone.getTimeZone("UTC"));
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        json = mapper.writeValueAsTree(date);
        assertEquals(JsonNode.ValueType.STRING, json.getValueAs(JsonNode.class).getType());
        assertEquals(dateString, json.get(0).asText());

        // Test deserialization with default settings
        json = mapper.readTree("\"2018-06-11T15:25:20.000+02:00\"");
        date = mapper.readValue(json, DummyDate.class);
        assertSame(dateFormat.parse(dateString), date.getValue());

        // Test deserialization as timestamp
        json = mapper.readTree("1516044520000");
        date = mapper.readValue(json, DummyDate.class);
        assertEquals(date.getValue().getTime(), json.get(0).asLong());

        // Test deserialization with custom format
        mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
        json = mapper.readTree("\"2018-06-11T15:25:20.000+02:00\"");
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        date = mapper.readValue(json, DummyDate.class);
        assertSame(dateFormat.parse(dateString), date.getValue());
    }

    static class DummyDate {
        private final Date value;
        private final DateFormat customFormat;

        public DummyDate(Date value) {
            this.value = value;
        }

        public DummyDate(Date value, DateFormat customFormat) {
            this.value = value;
            this.customFormat = customFormat;
        }

        public Date getValue() {
            return value;
        }

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSZ")
        public static class Serializer extends DateTimeSerializerBase<DummyDate> {
            public Serializer() {
                this(false, null);
            }
        }

        public static class Deserializer extends StdDeserializer<DummyDate> {
            public Deserializer() {
                this(DummyDate.class);
            }

            @Override
            public DummyDate deserialize(JsonParser p, DeserializationContext c) throws IOException {
                JsonNode node = p.getValueAsTree();
                if (node.getType() == JsonNode.Type.NUMBER) {
                    return new DummyDate(new Date(node.asLong()));
                }
                String dateString = node.asText();
                Date date = null;
                try {
                    date = customFormat.parse(dateString);
                } catch (ParseException e) {
                    throw c.wrapException(JsonMappingException.ClassMapException.from(p, DummyDate.class, dateString));
                }
                return new DummyDate(date, customFormat);
            }
        }
    }
}

class DateTimeSerializerBaseTest {
    // Test class here
}