package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormatVisitor;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    @Mock
    private SerializerProvider serializerProviderMock;
    @Mock
    private JsonGenerator jsonGeneratorMock;
    @Mock
    private BeanProperty beanPropertyMock;
    @Mock
    private JsonFormatVisitorWrapper visitorWrapperMock;
    @Mock
    private JavaType javaTypeMock;

    private DateTimeSerializerBaseStub dateTimeSerializerBaseStub;

    private class DateTimeSerializerBaseStub extends DateTimeSerializerBase {

        public DateTimeSerializerBaseStub(Class type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            // do nothing
        }

        @Override
        public DateTimeSerializerBase withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(T value) {
            return 0;
        }
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        dateTimeSerializerBaseStub = new DateTimeSerializerBaseStub(null, null, null);
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase result = dateTimeSerializerBaseStub.withFormat(null, null);
        assertNull(result);
    }

    @Test
    public void testCreateContextualWithNullProperty() throws JsonMappingException {
        JsonSerializer result = dateTimeSerializerBaseStub.createContextual(serializerProviderMock, null);
        assertEquals(dateTimeSerializerBaseStub, result);
    }

    @Test
    public void testCreateContextualWithNullFormat() throws JsonMappingException {
        setupAnnotations(null);

        JsonSerializer result = dateTimeSerializerBaseStub.createContextual(serializerProviderMock, beanPropertyMock);

        assertEquals(dateTimeSerializerBaseStub, result);
    }

    @Test
    public void testCreateContextualWithNumericFormat() throws JsonMappingException {
        setupAnnotations(JsonFormat.Shape.NUMBER);

        JsonSerializer result = dateTimeSerializerBaseStub.createContextual(serializerProviderMock, beanPropertyMock);

        verifyResultWithTimestamp(result);
    }

    @Test
    public void testCreateContextualWithStringFormatWithCustomPatternAndTimeZone() throws JsonMappingException {
        setupAnnotations(JsonFormat.Shape.STRING, "yyyy-MM-dd", "GMT+02:00");

        JsonSerializer result = dateTimeSerializerBaseStub.createContextual(serializerProviderMock, beanPropertyMock);

        verifyResultWithCustomFormat(result);
    }

    @Test
    public void testCreateContextualWithStringFormatWithDefaultPatternAndLocale() throws JsonMappingException {
        String expectedPattern = StdDateFormat.DATE_FORMAT_STR_ISO8601;
        Locale expectedLocale = Locale.getDefault();
        setupAnnotations(JsonFormat.Shape.STRING, null, null);

        JsonSerializer result = dateTimeSerializerBaseStub.createContextual(serializerProviderMock, beanPropertyMock);

        verifyResultWithDefaultFormat(result, expectedPattern, expectedLocale);
    }

    @Test
    public void testCreateContextualWithStringFormatWithDefaultPatternAndCustomLocale() throws JsonMappingException {
        String expectedPattern = StdDateFormat.DATE_FORMAT_STR_ISO8601;
        Locale expectedLocale = Locale.US;
        setupAnnotations(JsonFormat.Shape.STRING, null, null, expectedLocale);

        JsonSerializer result = dateTimeSerializerBaseStub.createContextual(serializerProviderMock, beanPropertyMock);

        verifyResultWithDefaultFormat(result, expectedPattern, expectedLocale);
    }

    @Test
    public void testIsEmptyWithNullValue() {
        boolean result = dateTimeSerializerBaseStub.isEmpty(serializerProviderMock, null);
        assertEquals(true, result);
    }

    @Test
    public void testIsEmptyWithValue() {
        boolean result = dateTimeSerializerBaseStub.isEmpty(serializerProviderMock, new Object());
        assertEquals(false, result);
    }

    @Test
    public void testGetSchemaWithTimestampSerialization() {
        when(serializerProviderMock.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);

        JsonNode result = dateTimeSerializerBaseStub.getSchema(serializerProviderMock, null);

        verifyTimestampSchema(result);
    }

    @Test
    public void testGetSchemaWithStringSerialization() {
        when(serializerProviderMock.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);

        JsonNode result = dateTimeSerializerBaseStub.getSchema(serializerProviderMock, null);

        verifyStringSchema(result);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithTimestampSerialization() throws JsonMappingException {
        when(visitorWrapperMock.getProvider()).thenReturn(serializerProviderMock);
        when(serializerProviderMock.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);

        dateTimeSerializerBaseStub.acceptJsonFormatVisitor(visitorWrapperMock, null);

        verifyTimestampFormatVisitor();
    }

    @Test
    public void testAcceptJsonFormatVisitorWithDateSerialization() throws JsonMappingException {
        when(visitorWrapperMock.getProvider()).thenReturn(serializerProviderMock);
        when(serializerProviderMock.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);

        dateTimeSerializerBaseStub.acceptJsonFormatVisitor(visitorWrapperMock, null);

        verifyDateFormatVisitor();
    }

    // Helper methods

    private void setupAnnotations(JsonFormat.Shape shape) {
        when(beanPropertyMock.getMember()).thenReturn(null);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(serializerProviderMock.getAnnotationIntrospector().findFormat((Annotated)beanPropertyMock.getMember()))
                .thenReturn(format);
        when(format.getShape()).thenReturn(shape);
    }

    private void setupAnnotations(JsonFormat.Shape shape, String pattern, String timeZone) {
        setupAnnotations(shape);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.hasPattern()).thenReturn(pattern != null);
        when(format.getPattern()).thenReturn(pattern);
        when(format.getTimeZone()).thenReturn(timeZone != null ? TimeZone.getTimeZone(timeZone) : null);
        when(serializerProviderMock.getLocale()).thenReturn(Locale.getDefault());
        when(format.hasLocale()).thenReturn(false);
        when(serializerProviderMock.getAnnotationIntrospector().findFormat((Annotated)beanPropertyMock.getMember()))
                .thenReturn(format);
    }

    private void setupAnnotations(JsonFormat.Shape shape, String pattern, String timeZone, Locale locale) {
        setupAnnotations(shape, pattern, timeZone);
        JsonFormat.Value format = mock(JsonFormat.Value.class);
        when(format.hasLocale()).thenReturn(true);
        when(format.getLocale()).thenReturn(locale);
        when(serializerProviderMock.getAnnotationIntrospector().findFormat((Annotated)beanPropertyMock.getMember()))
                .thenReturn(format);
    }

    private void verifyResultWithTimestamp(JsonSerializer result) {
        assertEquals(dateTimeSerializerBaseStub, result);
        assertEquals(Boolean.TRUE, dateTimeSerializerBaseStub._useTimestamp);
        assertNull(dateTimeSerializerBaseStub._customFormat);
    }

    private void verifyResultWithCustomFormat(JsonSerializer result) {
        assertEquals(dateTimeSerializerBaseStub, result);
        assertEquals(Boolean.FALSE, dateTimeSerializerBaseStub._useTimestamp);

        SimpleDateFormat expectedDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        expectedDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+02:00"));
        assertEquals(expectedDateFormat, dateTimeSerializerBaseStub._customFormat);
    }

    private void verifyResultWithDefaultFormat(JsonSerializer result, String expectedPattern, Locale expectedLocale) {
        assertEquals(dateTimeSerializerBaseStub, result);
        assertEquals(Boolean.FALSE, dateTimeSerializerBaseStub._useTimestamp);

        SimpleDateFormat expectedDateFormat = new SimpleDateFormat(expectedPattern, expectedLocale);
        assertEquals(expectedDateFormat, dateTimeSerializerBaseStub._customFormat);
    }

    private void verifyTimestampSchema(JsonNode result) {
        assertEquals("number", result.get("type").asText());
    }

    private void verifyStringSchema(JsonNode result) {
        assertEquals("string", result.get("type").asText());
        assertEquals(JsonValueFormat.DATE_TIME.toString(), result.get("format").asText());
    }

    private void verifyTimestampFormatVisitor() throws JsonMappingException {
        verify(visitorWrapperMock).expectNumberFormat(javaTypeMock, JsonParser.NumberType.LONG,
                JsonValueFormat.UTC_MILLISEC);
    }

    private void verifyDateFormatVisitor() throws JsonMappingException {
        verify(visitorWrapperMock).expectStringFormat(javaTypeMock, JsonValueFormat.DATE_TIME);
    }
}