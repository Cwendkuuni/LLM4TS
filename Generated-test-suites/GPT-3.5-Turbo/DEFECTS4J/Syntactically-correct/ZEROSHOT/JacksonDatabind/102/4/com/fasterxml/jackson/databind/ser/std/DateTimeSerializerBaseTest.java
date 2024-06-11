package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonFormat;
import com.fasterxml.jackson.databind.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Before;
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

    private DateTimeSerializerBase<Object> dateTimeSerializer;

    @Before
    public void setUp() {
        dateTimeSerializer = new DateTimeSerializerBase<Object>(Object.class, null, null) {
            @Override
            public DateTimeSerializerBase<Object> withFormat(Boolean timestamp, DateFormat customFormat) {
                return null;
            }

            @Override
            protected long _timestamp(Object value) {
                return 0;
            }

            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

            }

            @Override
            public JsonNode getSchema(SerializerProvider serializers, Type typeHint) {
                return null;
            }

            @Override
            public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {

            }
        };
    }

    @Test
    public void createContextual_ShouldReturnThis_WhenPropertyIsNull() throws JsonMappingException {
        // Arrange
        SerializerProvider serializerProvider = new SerializerProvider();

        // Act
        JsonSerializer<?> result = dateTimeSerializer.createContextual(serializerProvider, null);

        // Assert
        assertSame(dateTimeSerializer, result);
    }

    @Test
    public void createContextual_ShouldReturnThis_WhenFormatIsNull() throws JsonMappingException {
        // Arrange
        BeanProperty property = new BeanProperty();
        property.setFormat(null);
        SerializerProvider serializerProvider = new SerializerProvider();

        // Act
        JsonSerializer<?> result = dateTimeSerializer.createContextual(serializerProvider, property);

        // Assert
        assertSame(dateTimeSerializer, result);
    }

    @Test
    public void createContextual_ShouldWithFormatTrue_WhenFormatShapeIsNumeric() throws JsonMappingException {
        // Arrange
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);
        BeanProperty property = new BeanProperty();
        property.setFormat(format);
        SerializerProvider serializerProvider = new SerializerProvider();

        // Act
        JsonSerializer<?> result = dateTimeSerializer.createContextual(serializerProvider, property);

        // Assert
        assertEquals(Boolean.TRUE, result.withFormat);
    }

    @Test
    public void createContextual_ShouldWithFormatFalseAndCustomFormat_WhenFormatHasPattern() throws JsonMappingException {
        // Arrange
        String pattern = "yyyy-MM-dd";
        JsonFormat.Value format = new JsonFormat.Value();
        format.setPattern(pattern);
        BeanProperty property = new BeanProperty();
        property.setFormat(format);
        SerializerProvider serializerProvider = new SerializerProvider();

        // Act
        JsonSerializer<?> result = dateTimeSerializer.createContextual(serializerProvider, property);

        // Assert
        assertEquals(pattern, result.customFormat.getPattern());
    }

    @Test
    public void createContextual_ShouldReturnThis_WhenFormatHasLocaleAndTimeZoneAndNotShapeString() throws JsonMappingException {
        // Arrange
        JsonFormat.Value format = new JsonFormat.Value();
        format.setLocale(Locale.ENGLISH);
        format.setTimeZone(TimeZone.getDefault());
        BeanProperty property = new BeanProperty();
        property.setFormat(format);
        SerializerProvider serializerProvider = new SerializerProvider();

        // Act
        JsonSerializer<?> result = dateTimeSerializer.createContextual(serializerProvider, property);

        // Assert
        assertEquals(dateTimeSerializer, result);
    }

    @Test
    public void createContextual_ShouldReturnThis_WhenFormatDoesNotHaveLocaleAndTimeZoneAndIsNotShapeString() throws JsonMappingException {
        // Arrange
        JsonFormat.Value format = new JsonFormat.Value();
        BeanProperty property = new BeanProperty();
        property.setFormat(format);
        SerializerProvider serializerProvider = new SerializerProvider();

        // Act
        JsonSerializer<?> result = dateTimeSerializer.createContextual(serializerProvider, property);

        // Assert
        assertEquals(dateTimeSerializer, result);
    }

    @Test
    public void createContextual_ShouldReturnThis_WhenProviderIsNull() throws JsonMappingException {
        // Arrange
        JsonSerializer<?> result = dateTimeSerializer.createContextual(null, null);

        // Assert
        assertEquals(dateTimeSerializer, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createContextual_ShouldThrowIllegalArgumentException_WhenProviderIsNullAndCustomDateFormatIsNotNull() throws JsonMappingException {
        // Act
        dateTimeSerializer.createContextual(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createContextual_ShouldThrowIllegalArgumentException_WhenProviderFormatIsNotSimpleDateFormat() throws JsonMappingException {
        // Arrange
        SerializerProvider serializerProvider = new SerializerProvider();
        serializerProvider.setDateFormat(new DateFormat());

        // Act
        dateTimeSerializer.createContextual(serializerProvider, null);
    }

    @Test
    public void createContextual_ShouldApplyFormatLocale_WhenConfigurationFormatIsStdDateFormatAndFormatHasLocale() throws JsonMappingException {
        // Arrange
        Locale locale = Locale.ENGLISH;
        JsonFormat.Value format = new JsonFormat.Value();
        format.setLocale(locale);
        BeanProperty property = new BeanProperty();
        property.setFormat(format);
        SerializerProvider serializerProvider = new SerializerProvider();
        serializerProvider.setDateFormat(new StdDateFormat());

        // Act
        JsonSerializer<?> result = dateTimeSerializer.createContextual(serializerProvider, property);
        SimpleDateFormat customFormat = (SimpleDateFormat)result.customFormat;

        // Assert
        assertEquals(locale, customFormat.getLocale());
    }

    @Test
    public void createContextual_ShouldNotApplyFormatLocale_WhenConfigurationFormatIsStdDateFormatAndFormatHasNoLocale() throws JsonMappingException {
        // Arrange
        JsonFormat.Value format = new JsonFormat.Value();
        BeanProperty property = new BeanProperty();
        property.setFormat(format);
        SerializerProvider serializerProvider = new SerializerProvider();
        serializerProvider.setDateFormat(new StdDateFormat());

        // Act
        JsonSerializer<?> result = dateTimeSerializer.createContextual(serializerProvider, property);
        SimpleDateFormat customFormat = (SimpleDateFormat)result.customFormat;

        // Assert
        assertNull(customFormat.getLocale());
    }

    @Test
    public void createContextual_ShouldApplyFormatTimeZone_WhenConfigurationFormatIsStdDateFormatAndFormatHasTimeZone() throws JsonMappingException {
        // Arrange
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        JsonFormat.Value format = new JsonFormat.Value();
        format.setTimeZone(timeZone);
        BeanProperty property = new BeanProperty();
        property.setFormat(format);
        SerializerProvider serializerProvider = new SerializerProvider();
        serializerProvider.setDateFormat(new StdDateFormat());

        // Act
        JsonSerializer<?> result = dateTimeSerializer.createContextual(serializerProvider, property);
        SimpleDateFormat customFormat = (SimpleDateFormat)result.customFormat;

        // Assert
        assertEquals(timeZone, customFormat.getTimeZone());
    }

    @Test
    public void createContextual_ShouldNotApplyFormatTimeZone_WhenConfigurationFormatIsStdDateFormatAndFormatHasNoTimeZone() throws JsonMappingException {
        // Arrange
        JsonFormat.Value format = new JsonFormat.Value();
        BeanProperty property = new BeanProperty();
        property.setFormat(format);
        SerializerProvider serializerProvider = new SerializerProvider();
        serializerProvider.setDateFormat(new StdDateFormat());

        // Act
        JsonSerializer<?> result = dateTimeSerializer.createContextual(serializerProvider, property);
        SimpleDateFormat customFormat = (SimpleDateFormat)result.customFormat;

        // Assert
        assertNull(customFormat.getTimeZone());
    }

    @Test
    public void isEmpty_ShouldReturnFalse() {
        // Arrange
        SerializerProvider serializerProvider = new SerializerProvider();
        Object value = new Object();

        // Act
        boolean result = dateTimeSerializer.isEmpty(serializerProvider, value);

        // Assert
        assertFalse(result);
    }

    @Test
    public void getSchema_ShouldReturnSchemaNodeWithNumberTypeAndUTC_MILLISECFormat_WhenTimestampIsTrue_() {
        // Arrange
        SerializerProvider serializerProvider = new SerializerProvider();
        Type typeHint = Object.class;

        // Act
        JsonNode result = dateTimeSerializer.getSchema(serializerProvider, typeHint);

        // Assert
        assertEquals("number", result.get("type").asText());
        assertEquals("utc-millisec", result.get("format").asText());
        assertTrue(result.get("required").asBoolean());
    }

    @Test
    public void getSchema_ShouldReturnSchemaNodeWithStringTypeAndDateTimeFormat_WhenTimestampIsFalse() {
        // Arrange
        SerializerProvider serializerProvider = new SerializerProvider();
        Type typeHint = Object.class;

        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType javaType = new JavaType();
        boolean asNumber = false;

        // Act
        dateTimeSerializer.acceptJsonFormatVisitor(visitor, javaType, asNumber);
        JsonNode result = dateTimeSerializer.getSchema(serializerProvider, typeHint);

        // Assert
        assertEquals("string", result.get("type").asText());
        assertEquals("date-time", result.get("format").asText());
        assertTrue(result.get("required").asBoolean());
    }
}