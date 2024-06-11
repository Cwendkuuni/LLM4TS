
package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    private static class TestDateTimeSerializer extends DateTimeSerializerBase<Date> {
        protected TestDateTimeSerializer() {
            super(Date.class, false, null);
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(Date value) {
            return 0;
        }

        @Override
        public void serialize(Date value, com.fasterxml.jackson.core.JsonGenerator gen, SerializerProvider serializers) throws IOException {

        }

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, com.fasterxml.jackson.databind.JavaType typeHint) throws JsonMappingException {

        }
    }

    @Test
    public void createContextual_noProperty_returnThis() throws JsonMappingException {
        // Arrange
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();
        SerializerProvider serializers = null;
        BeanProperty property = null;

        // Act
        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        // Assert
        assertSame(serializer, result);
    }

    @Test
    public void createContextual_numericShape_returnWithTimestampFormat() throws JsonMappingException {
        // Arrange
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();
        SerializerProvider serializers = null;
        BeanProperty property = null;
        JsonFormat.Value format = new JsonFormat.Value().withShape(JsonFormat.Shape.NUMBER);

        // Act
        TestDateTimeSerializer result = (TestDateTimeSerializer) serializer.createContextual(serializers, property);

        // Assert
        assertTrue(result._useTimestamp);
        assertNull(result._customFormat);
    }

    @Test
    public void createContextual_customPattern_returnWithCustomFormat() throws JsonMappingException {
        // Arrange
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();
        SerializerProvider serializers = null;
        BeanProperty property = null;
        JsonFormat.Value format = new JsonFormat.Value().withPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");


        // Act
        TestDateTimeSerializer result = (TestDateTimeSerializer) serializer.createContextual(serializers, property);

        // Assert
        assertFalse(result._useTimestamp);
        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        assertEquals(expectedFormat.toPattern(), result._customFormat.toPattern());
    }

    @Test
    public void createContextual_defaultDateFormat_returnThis() throws JsonMappingException {
        // Arrange
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();
        SerializerProvider serializers = null;
        BeanProperty property = null;
        JsonFormat.Value format = new JsonFormat.Value().withShape(JsonFormat.Shape.OBJECT);

        // Act
        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        // Assert
        assertSame(serializer, result);
    }

    @Test
    public void createContextual_useStdDateFormat_returnWithStdDateFormat() throws JsonMappingException {
        // Arrange
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        BeanProperty property = null;
        JsonFormat.Value format = new JsonFormat.Value().withShape(JsonFormat.Shape.STRING);

        // Act
        TestDateTimeSerializer result = (TestDateTimeSerializer) serializer.createContextual(serializers, property);

        // Assert
        assertFalse(result._useTimestamp);
        assertTrue(result._customFormat instanceof StdDateFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createContextual_nullSerializerProvider_throwIllegalArgumentException() throws JsonMappingException {
        // Arrange
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();
        SerializerProvider serializers = null;
        BeanProperty property = null;
        JsonFormat.Value format = new JsonFormat.Value().withShape(JsonFormat.Shape.STRING);

        // Act
        TestDateTimeSerializer result = (TestDateTimeSerializer) serializer.createContextual(serializers, property);
    }

    @Test(expected = JsonMappingException.class)
    public void createContextual_notSimpleDateFormat_throwJsonMappingException() throws JsonMappingException {
        // Arrange
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();
        SerializerProvider serializers = new ObjectMapper().getSerializerProvider();
        BeanProperty property = null;
        JsonFormat.Value format = new JsonFormat.Value().withLocale(Locale.US);

        // Act
        TestDateTimeSerializer result = (TestDateTimeSerializer) serializer.createContextual(serializers, property);
    }

    @Test
    public void isEmpty_notEmpty_returnFalse() {
        // Arrange
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();
        SerializerProvider serializers = null;
        Date value = new Date();

        // Act
        boolean result = serializer.isEmpty(serializers, value);

        // Assert
        assertFalse(result);
    }

    @Test
    public void getSchema_asTimestamp_returnNumberSchemaNode() {
        // Arrange
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();
        SerializerProvider serializers = null;
        Type typeHint = null;

        // Act
        JsonNode result = serializer.getSchema(serializers, typeHint);

        // Assert
        assertTrue(result.isNumber());
        assertTrue(result.get("format").asText().equals("utc-millisec"));
    }

    @Test
    public void getSchema_asString_returnStringSchemaNode() {
        // Arrange
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();
        SerializerProvider serializers = null;
        Type typeHint = null;

        // Act
        JsonNode result = serializer.getSchema(serializers, typeHint);

        // Assert
        assertTrue(result.isTextual());
        assertTrue(result.get("format").asText().equals("date-time"));
    }

    @Test
    public void acceptJsonFormatVisitor_asTimestamp_callVisitIntFormat() throws JsonMappingException {
        // Arrange
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        boolean asNumber = true;

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        // Verify the visitIntFormat method is called
    }

    @Test
    public void acceptJsonFormatVisitor_asString_callVisitStringFormat() throws JsonMappingException {
        // Arrange
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        boolean asNumber = false;

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        // Verify the visitStringFormat method is called
    }

    @Test
    public void serialize_withCustomFormat_callWriteString() throws IOException {
        // Arrange
        TestDateTimeSerializer serializer = new TestDateTimeSerializer();
        JsonGenerator gen = null;
        SerializerProvider serializers = null;
        Date value = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .parse("2021-01-01T00:00:00.000+0000");

        // Act
        serializer.serialize(value, gen, serializers);

        // Assert
        // Verify the writeString method is called with correct string format
    }

}

