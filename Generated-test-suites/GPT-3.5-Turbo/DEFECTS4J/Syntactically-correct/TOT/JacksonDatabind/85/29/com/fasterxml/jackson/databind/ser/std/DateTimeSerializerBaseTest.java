package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private static DateTimeSerializerBase<Date> serializer;
    private static SerializerProvider provider;

    @BeforeClass
    public static void  setUpBeforeClass() {
        serializer = new DateTimeSerializerBaseImpl(Date.class, true, null);
        provider = new SerializerProvider();
    }

    @AfterClass
    public static void tearDownAfterClass() {
        serializer = null;
        provider = null;
    }

    @Test
    public final void testCreateContextualWithNullProperty() throws JsonMappingException {
        BeanProperty property = null;
        JsonSerializer<?> result = serializer.createContextual(provider, property);
        assertEquals(serializer, result);
    }

    @Test
    public final void testCreateContextualWithNumericShape() throws JsonMappingException {
        BeanProperty property = new BeanProperty();
        property.setFormat(new JsonFormat.Value().withShape(JsonFormat.Shape.NUMBER));

        JsonSerializer<?> result = serializer.createContextual(provider, property);
        assertNotNull(result);
        assertTrue(result instanceof DateTimeSerializerBase);
        assertTrue(((DateTimeSerializerBase<?>) result)._useTimestamp);
        assertNull(((DateTimeSerializerBase<?>) result)._customFormat);
    }

    @Test
    public final void testCreateContextualWithNonNumericShape() throws JsonMappingException {
        BeanProperty property = new BeanProperty();
        property.setFormat(new JsonFormat.Value().withShape(JsonFormat.Shape.STRING));

        JsonSerializer<?> result = serializer.createContextual(provider, property);
        assertNotNull(result);
        assertTrue(result instanceof DateTimeSerializerBase);
        assertFalse(((DateTimeSerializerBase<?>) result)._useTimestamp);
        assertNotNull(((DateTimeSerializerBase<?>) result)._customFormat);
    }

    @Test
    public final void testCreateContextualWithCustomFormat() throws JsonMappingException {
        BeanProperty property = new BeanProperty();
        String pattern = "yyyy-MM-dd";
        Locale locale = Locale.ENGLISH;
        TimeZone timezone = TimeZone.getTimeZone("GMT");
        property.setFormat(new JsonFormat.Value().withPattern(pattern).withLocale(locale).withTimeZone(timezone));

        JsonSerializer<?> result = serializer.createContextual(provider, property);
        assertNotNull(result);
        assertTrue(result instanceof DateTimeSerializerBase);
        assertFalse(((DateTimeSerializerBase<?>) result)._useTimestamp);
        assertNotNull(((DateTimeSerializerBase<?>) result)._customFormat);
        DateFormat expectedFormat = new SimpleDateFormat(pattern, locale);
        expectedFormat.setTimeZone(timezone);
        assertEquals(expectedFormat, ((DateTimeSerializerBase<?>) result)._customFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public final void testCreateContextualWithException() throws JsonMappingException {
        BeanProperty property = new BeanProperty();
        property.setFormat(new JsonFormat.Value().withShape(JsonFormat.Shape.STRING).withPattern("yyyy-MM-dd").withTimeZone(null));

        serializer.createContextual(null, property);
    }

    @Test
    public final void testIsEmptyWithValue() {
        Date value = new Date();
        boolean result = serializer.isEmpty(null, value);
        assertFalse(result);
    }

    @Test
    public final void testIsEmptyWithNullValue() {
        Date value = null;
        boolean result = serializer.isEmpty(null, value);
        assertTrue(result);
    }

    @Test
    public final void testIsEmptyWithValueAndZeroTimestamp() {
        Date value = new Date(0L);
        boolean result = serializer.isEmpty(null, value);
        assertTrue(result);
    }

    @Test
    public final void testGetSchemaWhenTimestampFormat() {
        JsonNode result = serializer.getSchema(provider, null);
        assertEquals("number", result.get("type").asText());
    }

    @Test
    public final void testGetSchemaWhenStringFormat() {
        serializer._useTimestamp = false;
        JsonNode result = serializer.getSchema(provider, null);
        assertEquals("string", result.get("type").asText());
    }

    @Test
    public final void testAcceptJsonFormatVisitorWhenTimestampFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertEquals(JsonParser.NumberType.LONG, visitor.getType().asNumberType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getValueFormat());
    }

    @Test
    public final void testAcceptJsonFormatVisitorWhenStringFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();
        serializer._useTimestamp = false;
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getValueFormat());
    }

    private static class DateTimeSerializerBaseImpl extends DateTimeSerializerBase<Date> {

        protected DateTimeSerializerBaseImpl(Class<Date> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
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
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        }
    }
}