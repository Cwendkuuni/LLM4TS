import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    public static class MockDateTimeSerializerBaseImpl extends DateTimeSerializerBase<Date> {

        private Long timestamp;

        public MockDateTimeSerializerBaseImpl(Class<Date> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        public MockDateTimeSerializerBaseImpl withTimestamp(Long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return this;
        }

        @Override
        public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property)
                throws JsonMappingException {
            return null;
        }

        @Deprecated
        @Override
        public boolean isEmpty(Date value) {
            return false;
        }

        @Override
        public boolean isEmpty(SerializerProvider serializers, Date value) {
            return false;
        }

        @Override
        protected long _timestamp(Date value) {
            return timestamp;
        }

        @Override
        public JsonNode getSchema(SerializerProvider serializers, java.lang.reflect.Type typeHint) {
            return null;
        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, com.fasterxml.jackson.databind.JavaType typeHint)
                throws JsonMappingException {
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) {
        }

    }

    @Test
    public void testIsEmpty_NullValue_ReturnsTrue() {

        // Arrange
        MockDateTimeSerializerBaseImpl serializer = new MockDateTimeSerializerBaseImpl(Date.class, false, null);

        // Act
        boolean result = serializer.isEmpty(null);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEmpty_NonNullValue_ReturnsFalse() {

        // Arrange
        MockDateTimeSerializerBaseImpl serializer = new MockDateTimeSerializerBaseImpl(Date.class, false, null);

        // Act
        boolean result = serializer.isEmpty(new Date());

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsEmpty_TimestampIsZero_ReturnsTrue() {

        // Arrange
        MockDateTimeSerializerBaseImpl serializer = new MockDateTimeSerializerBaseImpl(Date.class, false, null);
        serializer.withTimestamp(0L);

        // Act
        boolean result = serializer.isEmpty(new Date());

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsEmpty_TimestampIsNonZero_ReturnsFalse() {

        // Arrange
        MockDateTimeSerializerBaseImpl serializer = new MockDateTimeSerializerBaseImpl(Date.class, false, null);
        serializer.withTimestamp(1234567890L);

        // Act
        boolean result = serializer.isEmpty(new Date());

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetSchema_UseTimestamp_ReturnsNumberSchema() {

        // Arrange
        MockDateTimeSerializerBaseImpl serializer = new MockDateTimeSerializerBaseImpl(Date.class, true, null);
        SerializerProvider serializers = null;
        java.lang.reflect.Type typeHint = null;

        // Act
        JsonNode node = serializer.getSchema(serializers, typeHint);

        // Assert
        assertEquals("number", node.get("type").asText());
    }

    @Test
    public void testGetSchema_UseCustomFormat_ReturnsStringSchema() {

        // Arrange
        MockDateTimeSerializerBaseImpl serializer = new MockDateTimeSerializerBaseImpl(Date.class, false,
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US));
        SerializerProvider serializers = null;
        java.lang.reflect.Type typeHint = null;

        // Act
        JsonNode node = serializer.getSchema(serializers, typeHint);

        // Assert
        assertEquals("string", node.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor_UseTimestamp_VisitorVisitsIntFormat() {

        // Arrange
        MockDateTimeSerializerBaseImpl serializer = new MockDateTimeSerializerBaseImpl(Date.class, true, null);
        JsonFormatVisitorWrapper visitor = null;
        com.fasterxml.jackson.databind.JavaType typeHint = null;
        Class<?> expectedFormatType = JsonGenerator.NumberType.LONG.getRawClass();

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        assertEquals(expectedFormatType, visitor.getProvider().getNumberFormat());
    }

    @Test
    public void testAcceptJsonFormatVisitor_UseCustomFormat_VisitorVisitsStringFormat() {

        // Arrange
        MockDateTimeSerializerBaseImpl serializer = new MockDateTimeSerializerBaseImpl(Date.class, false,
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US));
        JsonFormatVisitorWrapper visitor = null;
        com.fasterxml.jackson.databind.JavaType typeHint = null;

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getProvider().getStringFormat());
    }
}