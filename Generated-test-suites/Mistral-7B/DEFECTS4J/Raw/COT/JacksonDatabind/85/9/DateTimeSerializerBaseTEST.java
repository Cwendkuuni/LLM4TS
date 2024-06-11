package com.fasterxml.jackson.databind.ser.std; // The package name should be updated accordingly

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDateFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.test.BaseTest;

public class DateTimeSerializerBaseTest extends BaseTest {

    private static final Class<LocalDateTime> LOCAL_DATE_TIME_TYPE = LocalDateTime.class;
    private static final JsonFormat.Shape STRING_SHAPE = JsonFormat.Shape.STRING;
    private static final JsonFormat.Shape NUMBER_SHAPE = JsonFormat.Shape.NUMBER;

    private DateTimeSerializerBase<LocalDateTime> serializer;
    private JsonParser jsonParser;
    private JsonGenerator jsonGenerator;
    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        serializer = new DateTimeSerializerBase<LocalDateTime>(LOCAL_DATE_TIME_TYPE, false, null);
        jsonParser = mock(JsonParser.class);
        jsonGenerator = mock(JsonGenerator.class);
        mapper = new ObjectMapper();
    }

    @Test
    public void testCreateContextual_NullProperty() throws JsonProcessingException {
        JsonSerializer<?> result = serializer.createContextual(null, null);
        assertEquals(serializer, result);
    }

    @Test
    public void testCreateContextual_DefaultFormat() throws JsonProcessingException {
        when(jsonParser.getProvider()).thenReturn(mapper.getSerializationConfig());
        JsonFormat.Value format = new JsonFormat.Value();
        when(jsonParser.getValueFormat()).thenReturn(format);
        JsonFormat.Shape shape = JsonFormat.Shape.DEFAULT;
        when(format.getShape()).thenReturn(shape);

        JsonSerializer<?> result = serializer.createContextual(mock(SerializerProvider.class), null);
        assertFalse(result instanceof DateTimeSerializerBase);
    }

    @Test
    public void testCreateContextual_NumericFormat() throws JsonProcessingException {
        when(jsonParser.getProvider()).thenReturn(mapper.getSerializationConfig());
        JsonFormat.Value format = new JsonFormat.Value();
        when(jsonParser.getValueFormat()).thenReturn(format);
        JsonFormat.Shape shape = JsonFormat.Shape.NUMBER;
        when(format.getShape()).thenReturn(shape);

        JsonSerializer<?> result = serializer.createContextual(mock(SerializerProvider.class), null);
        assertTrue(result instanceof DateTimeSerializerBase);
        assertEquals(serializer.class, result.getClass());
    }

    @Test
    public void testCreateContextual_CustomFormat() throws JsonProcessingException {
        when(jsonParser.getProvider()).thenReturn(mapper.getSerializationConfig());
        JsonFormat.Value format = new JsonFormat.Value();
        when(jsonParser.getValueFormat()).thenReturn(format);
        JsonFormat.Shape shape = STRING_SHAPE;
        when(format.getShape()).thenReturn(shape);
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        when(format.hasCustomFormat()).thenReturn(true);
        when(format.getCustomFormat()).thenReturn(customFormat);

        JsonSerializer<?> result = serializer.createContextual(mock(SerializerProvider.class), null);
        assertTrue(result instanceof DateTimeSerializerBase);
        assertEquals(serializer.class, result.getClass());
    }

    @Test
    public void testCreateContextual_CustomLocale() throws JsonProcessingException {
        when(jsonParser.getProvider()).thenReturn(mapper.getSerializationConfig());
        JsonFormat.Value format = new JsonFormat.Value();
        when(jsonParser.getValueFormat()).thenReturn(format);
        JsonFormat.Shape shape = STRING_SHAPE;
        when(format.getShape()).thenReturn(shape);
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        when(format.hasCustomFormat()).thenReturn(false);
        when(format.getLocale()).thenReturn(Locale.FRENCH);

        JsonSerializer<?> result = serializer.createContextual(mock(SerializerProvider.class), null);
        assertTrue(result instanceof DateTimeSerializerBase);
        assertEquals(serializer.class, result.getClass());
    }

    @Test
    public void testCreateContextual_CustomTimeZone() throws JsonProcessingException {
        when(jsonParser.getProvider()).thenReturn(mapper.getSerializationConfig());
        JsonFormat.Value format = new JsonFormat.Value();
        when(jsonParser.getValueFormat()).thenReturn(format);
        JsonFormat.Shape shape = STRING_SHAPE;
        when(format.getShape()).thenReturn(shape);
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        when(format.hasCustomFormat()).thenReturn(false);
        when(format.getTimeZone()).thenReturn(TimeZone.getTimeZone("GMT+2"));

        JsonSerializer<?> result = serializer.createContextual(mock(SerializerProvider.class), null);
        assertTrue(result instanceof DateTimeSerializerBase);
        assertEquals(serializer.class, result.getClass());
    }

    @Test
    public void testCreateContextual_NullTypeHint() throws JsonProcessingException {
        JsonSerializer<?> result = serializer.createContextual(mock(SerializerProvider.class), null);
        assertEquals(serializer, result);
    }

    @Test
    public void testCreateContextual_NonDateTimeType() throws JsonProcessingException {
        when(jsonParser.getProvider()).thenReturn(mapper.getSerializationConfig());
        JsonFormat.Value format = new JsonFormat.Value();
        when(jsonParser.getValueFormat()).thenReturn(format);
        JsonFormat.Shape shape = JsonFormat.Shape.STRING;
        when(format.getShape()).thenReturn(shape);
        when(format.getType()).thenReturn(ClassLoader.getSystemClass("java.lang.String"));

        JsonSerializer<?> result = serializer.createContextual(mock(SerializerProvider.class), null);
        assertFalse(result instanceof DateTimeSerializerBase);
    }

    @Test
    public void testEmpty_NullValue() throws Exception {
        assertTrue(serializer.isEmpty(null));
    }

    @Test
    public void testEmpty_ZeroTimestampValue() throws Exception {
        assertTrue(serializer.isEmpty(new Date(0L)));
    }

    @Test
    public void testEmpty_NotNullValue() throws Exception {
        assertFalse(serializer.isEmpty(new Date()));
    }

    @Test
    public void testGetSchema_NumberFormat() throws Exception {
        JsonNode result = serializer.getSchema(mapper.getSerializationConfig(), LOCAL_DATE_TIME_TYPE);
        assertEquals("number", result.getClass().getSimpleName());
    }

    @Test
    public void testGetSchema_StringFormat() throws Exception {
        serializer = new DateTimeSerializerBase<>(LOCAL_DATE_TIME_TYPE, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JsonNode result = serializer.getSchema(mapper.getSerializationConfig(), LOCAL_DATE_TIME_TYPE);
        assertEquals("string", result.getClass().getSimpleName());
    }

    @Test
    public void testSerialize_NullValue() throws IOException {
        when(jsonGenerator.writeNull()).thenReturn(null);

        serializer.serialize(null, jsonGenerator, mapper.getSerializationConfig());
        assertNull(jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSerialize_DefaultFormat_Numeric() throws IOException {
        SerializationConfig config = mapper.getSerializationConfig();
        config.setSerializationFeature(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

        when(jsonGenerator.writeNumber(anyLong())).thenReturn(null);

        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        serializer.serialize(dateTime, jsonGenerator, config);
        assertEquals("1643644800000", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSerialize_CustomFormat_String() throws IOException {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        when(jsonGenerator.writeStringValue(anyString())).thenReturn(null);

        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        serializer = new DateTimeSerializerBase<>(LOCAL_DATE_TIME_TYPE, false, customFormat);
        serializer.serialize(dateTime, jsonGenerator, mapper.getSerializationConfig());
        assertEquals("2022-01-01 12:00:00", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSerialize_CustomLocale() throws IOException {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Locale loc = Locale.FRENCH;

        when(jsonGenerator.writeStringValue(anyString())).thenReturn(null);

        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        serializer = new DateTimeSerializerBase<>(LOCAL_DATE_TIME_TYPE, false, customFormat, loc);
        serializer.serialize(dateTime, jsonGenerator, mapper.getSerializationConfig());
        assertEquals("2022-01-01 12:00:00", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSerialize_CustomTimeZone() throws IOException {
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone timeZone = TimeZone.getTimeZone("GMT+2");

        when(jsonGenerator.writeStringValue(anyString())).thenReturn(null);

        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        serializer = new DateTimeSerializerBase<>(LOCAL_DATE_TIME_TYPE, false, customFormat, null, timeZone);
        serializer.serialize(dateTime, jsonGenerator, mapper.getSerializationConfig());
        assertEquals("2022-01-01 14:00:00", jsonGenerator.getOutputBuffer().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerialize_NullSerializerProvider() throws IOException {
        serializer.serialize(new Date(), jsonGenerator, null);
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerialize_NullValueAndNullGenerator() throws IOException {
        serializer.serialize(null, null, mapper.getSerializationConfig());
        fail("Expected JsonProcessingException");
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerialize_NullValueAndNullProvider() throws IOException {
        serializer.serialize(null, jsonGenerator, null);
        fail("Expected JsonProcessingException");
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerialize_NullGeneratorAndNullProvider() throws IOException {
        serializer.serialize(new Date(), null, mapper.getSerializationConfig());
        fail("Expected JsonProcessingException");
    }

    @Test
    public void testSerialize_NullSerializerProvider_NullValue() throws IOException {
        serializer.serialize(null, jsonGenerator, mapper.getSerializationConfig());
        assertNull(jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSerialize_NullSerializerProvider_NotNullValue() throws IOException {
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        when(jsonGenerator.writeNull()).thenReturn(null);

        serializer.serialize(null, jsonGenerator, mapper.getSerializationConfig());
        assertNull(jsonGenerator.getOutputBuffer().toString());

        serializer.serialize(dateTime, jsonGenerator, mapper.getSerializationConfig());
        assertEquals("1643644800000", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSerialize_NullGenerator_NullValue() throws IOException {
        when(jsonGenerator.writeNull()).thenReturn(null);

        serializer.serialize(null, jsonGenerator, mapper.getSerializationConfig());
        assertNull(jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSerialize_NullGenerator_NotNullValue() throws IOException {
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        when(jsonGenerator.writeStringValue(anyString())).thenReturn(null);

        serializer.serialize(dateTime, jsonGenerator, mapper.getSerializationConfig());
        assertEquals("2022-01-01 12:00:00", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSerialize_NullProvider_NullValue() throws IOException {
        when(jsonGenerator.writeNull()).thenReturn(null);

        serializer.serialize(null, jsonGenerator, null);
        assertNull(jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testSerialize_NullProvider_NotNullValue() throws IOException {
        LocalDateTime dateTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        when(jsonGenerator.writeStringValue(anyString())).thenReturn(null);

        serializer.serialize(dateTime, jsonGenerator, null);
        assertEquals("2022-01-01 12:00:00", jsonGenerator.getOutputBuffer().toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor_NumericFormat() throws JsonProcessingException {
        when(jsonParser.getProvider()).thenReturn(mapper.getSerializationConfig());
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(NUMBER_SHAPE);
        when(jsonParser.getValueFormat()).thenReturn(format);

        serializer._acceptJsonFormatVisitor(mock(JsonFormatVisitorWrapper.class), LOCAL_DATE_TIME_TYPE, true);
        verify(mock(JsonFormatVisitorWrapper.class), times(1)).visitIntFormat(any(JsonFormatVisitorWrapper.class), eq(LOCAL_DATE_TIME_TYPE), anyInt(), eq(JsonValueFormat.UTC_MILLISEC));
    }

    @Test
    public void testAcceptJsonFormatVisitor_StringFormat() throws JsonProcessingException {
        when(jsonParser.getProvider()).thenReturn(mapper.getSerializationConfig());
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(STRING_SHAPE);
        when(jsonParser.getValueFormat()).thenReturn(format);

        serializer._acceptJsonFormatVisitor(mock(JsonFormatVisitorWrapper.class), LOCAL_DATE_TIME_TYPE, false);
        verify(mock(JsonFormatVisitorWrapper.class), times(1)).visitStringFormat(any(JsonFormatVisitorWrapper.class), eq(LOCAL_DATE_TIME_TYPE), eq(JsonValueFormat.DATE_TIME));
    }
}