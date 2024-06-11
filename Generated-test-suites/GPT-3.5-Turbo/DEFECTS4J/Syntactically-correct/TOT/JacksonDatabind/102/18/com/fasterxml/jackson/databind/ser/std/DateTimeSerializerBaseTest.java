package com.fasterxml.jackson.databind.ser.std;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DateTimeSerializerBaseTest {

    private static class DateTimeSerializerBaseConcrete extends DateTimeSerializerBase<Date> {

        protected DateTimeSerializerBaseConcrete(Boolean useTimestamp, DateFormat customFormat) {
            super(Date.class, useTimestamp, customFormat);
        }

        @Override
        public DateTimeSerializerBase<Date> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new DateTimeSerializerBaseConcrete(timestamp, customFormat);
        }

        @Override
        protected long _timestamp(Date value) {
            return value.getTime();
        }

        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) {
            // No implementation needed for testing
        }

    }

    private DateTimeSerializerBaseConcrete serializer;

    @Before
    public void setup() {
        serializer = new DateTimeSerializerBaseConcrete(null, null);
    }

    @Test
    public void testCreateContextual_SerializeAsNumeric() throws JsonMappingException {
        // Arrange
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);
        SerializerProvider serializers = null;
        BeanProperty property = null;

        // Act
        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        // Assert
        Assert.assertTrue(result instanceof DateTimeSerializerBaseConcrete);
        DateTimeSerializerBaseConcrete concreteResult = (DateTimeSerializerBaseConcrete) result;
        Assert.assertTrue(concreteResult._useTimestamp);
    }

    @Test
    public void testCreateContextual_SerializeAsString_CustomPattern() throws JsonMappingException {
        // Arrange
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy-MM-dd");
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        SerializerProvider serializers = null;
        BeanProperty property = null;

        // Act
        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        // Assert
        Assert.assertTrue(result instanceof DateTimeSerializerBaseConcrete);
        DateTimeSerializerBaseConcrete concreteResult = (DateTimeSerializerBaseConcrete) result;
        Assert.assertFalse(concreteResult._useTimestamp);
        Assert.assertEquals("yyyy-MM-dd", ((SimpleDateFormat) concreteResult._customFormat).toPattern());
        Assert.assertEquals(Locale.US, concreteResult._customFormat.getLocale());
        Assert.assertEquals(TimeZone.getTimeZone("GMT"), concreteResult._customFormat.getTimeZone());
    }

    @Test
    public void testCreateContextual_SerializeAsString_DefaultFormat_CustomLocaleAndTimeZone() throws JsonMappingException {
        // Arrange
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        SerializerProvider serializers = null;
        BeanProperty property = null;

        // Act
        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        // Assert
        Assert.assertTrue(result instanceof DateTimeSerializerBaseConcrete);
        DateTimeSerializerBaseConcrete concreteResult = (DateTimeSerializerBaseConcrete) result;
        Assert.assertFalse(concreteResult._useTimestamp);
        Assert.assertNull(concreteResult._customFormat);
        Assert.assertEquals(StdDateFormat.class, concreteResult._reusedCustomFormat.get().getClass());
        Assert.assertEquals(Locale.US, ((StdDateFormat)concreteResult._reusedCustomFormat.get()).getLocale());
        Assert.assertEquals(TimeZone.getTimeZone("GMT"), ((StdDateFormat)concreteResult._reusedCustomFormat.get()).getTimeZone());
    }

    @Test
    public void testCreateContextual_SerializeAsString_CustomFormatProviderUseDefault() throws JsonMappingException {
        // Arrange
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        SerializerProvider serializers = new SerializerProvider();
        BeanProperty property = null;

        // Act
        JsonSerializer<?> result = serializer.createContextual(serializers, property);

        // Assert
        Assert.assertTrue(result instanceof DateTimeSerializerBaseConcrete);
        DateTimeSerializerBaseConcrete concreteResult = (DateTimeSerializerBaseConcrete) result;
        Assert.assertFalse(concreteResult._useTimestamp);
        Assert.assertNull(concreteResult._customFormat);
        Assert.assertEquals(StdDateFormat.class, concreteResult._reusedCustomFormat.get().getClass());
        Assert.assertEquals(Locale.US, ((StdDateFormat) concreteResult._reusedCustomFormat.get()).getLocale());
        Assert.assertEquals(TimeZone.getTimeZone("GMT"), ((StdDateFormat) concreteResult._reusedCustomFormat.get()).getTimeZone());
    }

    @Test
    public void testIsEmpty_NullSerializerProvider() {
        // Arrange
        SerializerProvider serializers = null;
        Date value = new Date();

        // Act
        boolean result = serializer.isEmpty(serializers, value);

        // Assert
        Assert.assertFalse(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsEmpty_NullSerializerProviderAndNullValue() {
        // Arrange
        SerializerProvider serializers = null;
        Date value = null;

        // Act
        serializer.isEmpty(serializers, value);
    }

    @Test
    public void testIsEmpty_NotEmpty() {
        // Arrange
        SerializerProvider serializers = new SerializerProvider();
        Date value = new Date();

        // Act
        boolean result = serializer.isEmpty(serializers, value);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testIsEmpty_Empty() {
        // Arrange
        SerializerProvider serializers = new SerializerProvider();
        Date value = new Date(0);

        // Act
        boolean result = serializer.isEmpty(serializers, value);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testGetSchema_AsTimestamp() {
        // Arrange
        SerializerProvider serializers = null;
        Type typeHint = null;

        // Act
        JsonNode result = serializer.getSchema(serializers, typeHint);

        // Assert
        Assert.assertEquals("number", result.get("type").asText());
        Assert.assertTrue(result.get("format").asText(), result.get("format").isBoolean());
        Assert.assertTrue(result.get("format").asBoolean());
    }

    @Test
    public void testGetSchema_AsString() {
        // Arrange
        serializer._useTimestamp = Boolean.FALSE;
        SerializerProvider serializers = null;
        Type typeHint = null;

        // Act
        JsonNode result = serializer.getSchema(serializers, typeHint);

        // Assert
        Assert.assertEquals("string", result.get("type").asText());
        Assert.assertTrue(result.get("format").asText(), result.get("format").isBoolean());
        Assert.assertTrue(result.get("format").asBoolean());
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsTimestamp() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = new FakeJsonFormatVisitorWrapper();
        JavaType typeHint = null;
        serializer._useTimestamp = Boolean.TRUE;

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        Assert.assertEquals(JsonParser.NumberType.LONG, visitor.getNumberType());
        Assert.assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getFormat());
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsString() throws JsonMappingException {
        // Arrange
        JsonFormatVisitorWrapper visitor = new FakeJsonFormatVisitorWrapper();
        JavaType typeHint = null;
        serializer._useTimestamp = Boolean.FALSE;

        // Act
        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        // Assert
        Assert.assertNull(visitor.getNumberType());
        Assert.assertEquals(JsonValueFormat.DATE_TIME, visitor.getFormat());
    }

    private static class FakeJsonFormatVisitorWrapper extends JsonFormatVisitorWrapper.Base {

        private JsonParser.NumberType numberType;
        private JsonValueFormat format;

        public JsonParser.NumberType getNumberType() {
            return numberType;
        }

        public JsonValueFormat getFormat() {
            return format;
        }

        @Override
        public SerializerProvider getProvider() {
            return null;
        }

        @Override
        public void setProvider(SerializerProvider provider) {
            // No implementation needed for testing
        }

        @Override
        public Object expectNumberFormat(JavaType type) throws JsonMappingException {
            numberType = getProvider().getConfig().getTypeFactory().constructType(type).asPrimitiveType().asNumberType();
            return null;
        }

        @Override
        public void expectAnyFormat(JavaType type) {
            format = JsonValueFormat.DATE_TIME;
        }

    }

    @Test
    public void testSerialize_AsString() throws Exception {
        // Arrange
        JsonGenerator gen = null;
        SerializerProvider serializers = null;

        serializer._useTimestamp = null;
        serializer._customFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date value = new SimpleDateFormat("yyyy-MM-dd").parse("2000-01-01");

        // Act
        serializer.serialize(value, gen, serializers);

        // No assertion needed for void method
    }

    @Test
    public void testSerialize_AsTimestamp() throws Exception {
        // Arrange
        JsonGenerator gen = null;
        SerializerProvider serializers = null;

        serializer._useTimestamp = Boolean.TRUE;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date value = format.parse("2000-01-01");

        // Act
        serializer.serialize(value, gen, serializers);

        // No assertion needed for void method
    }

}