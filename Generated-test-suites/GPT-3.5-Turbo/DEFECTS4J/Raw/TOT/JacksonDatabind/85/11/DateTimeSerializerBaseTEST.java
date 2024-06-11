
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;
    private SimpleDateFormat simpleDateFormat;
    private TimeZone timeZone;

    @Before
    public void setUp() {
        objectMapper = JsonMapper.builder().build();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        timeZone = TimeZone.getTimeZone("GMT");
        simpleDateFormat.setTimeZone(timeZone);
    }

    @Test
    public void testSerialize_WithTimestampEnabled_ShouldSerializeAsTimestamp() throws ParseException {
        // Arrange
        Date date = simpleDateFormat.parse("2022-01-01T00:00:00.000Z");
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, true, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Stub implementation
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }
        };

        // Act
        ObjectNode schema = (ObjectNode) serializer.getSchema(objectMapper, null);

        // Assert
        assertTrue(schema.has("type"));
        assertEquals("number", schema.get("type").asText());
    }

    @Test
    public void testSerialize_WithTimestampDisabled_ShouldSerializeAsString() throws ParseException {
        // Arrange
        Date date = simpleDateFormat.parse("2022-01-01T00:00:00.000Z");
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Stub implementation
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }
        };

        // Act
        ObjectNode schema = (ObjectNode) serializer.getSchema(objectMapper, null);

        // Assert
        assertTrue(schema.has("type"));
        assertEquals("string", schema.get("type").asText());
    }
    
    @Test
    public void testSerialize_WithCustomFormat_ShouldSerializeWithCustomFormat() throws ParseException {
        // Arrange
        Date date = simpleDateFormat.parse("2022-01-01T00:00:00.000Z");
        DateFormat customFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, customFormat) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Stub implementation
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }
        };

        // Act
        ObjectNode schema = (ObjectNode) serializer.getSchema(objectMapper, null);

        // Assert
        assertTrue(schema.has("type"));
        assertEquals("string", schema.get("type").asText());
        assertTrue(schema.has("format"));
        assertEquals("date-time", schema.get("format").asText());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void test_serialize_WithNullSerializerProvider_ShouldThrowIllegalArgumentException() throws ParseException {
        // Arrange
        Date date = simpleDateFormat.parse("2022-01-01T00:00:00.000Z");
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                // Stub implementation
            }

            @Override
            protected long _timestamp(Date value) {
                return 0;
            }
        };

        // Act
        serializer._asTimestamp(null);
    }
}
