package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedGeneric;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDateFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Calendar> datetimeSerializer;
    private JsonGenerator mockJsonGenerator;
    private SerializerProvider mockSerializerProvider;
    private JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper;
    private BeanProperty mockBeanProperty;
    private AnnotatedMock annotatedMock;

    @Before
    public void setup() {
        mockJsonGenerator = mock(JsonGenerator.class);
        mockSerializerProvider = mock(SerializerProvider.class);
        mockJsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        mockBeanProperty = mock(BeanProperty.class);
        annotatedMock = new AnnotatedMock();
        MockitoAnnotations.initMocks(this);
        datetimeSerializer = new DateTimeSerializerBase<Calendar>() {
            @Override
            protected long _timestamp(Calendar value) {
                return value.getTimeInMillis();
            }
        };
        when(mockSerializerProvider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);
        when(mockJsonFormatVisitorWrapper.getProvider()).thenReturn(mockSerializerProvider);
    }

    @Test
    public void testCreateContextualWithFormatNumeric() throws IOException {
        when(mockBeanProperty.getMember()).thenReturn(new Object(){});
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);
        when(mockSerializerProvider.getAnnotationIntrospector().findFormat((Annotated)mockBeanProperty)).thenReturn(format);

        JsonSerializer<?> result = datetimeSerializer.createContextual(mockSerializerProvider, mockBeanProperty);
        assertEquals(datetimeSerializer, result);
    }

    @Test
    public void testCreateContextualWithFormatString() throws IOException {
        when(mockBeanProperty.getMember()).thenReturn(new Object(){});
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        TimeZone tz = mock(TimeZone.class);
        String pattern = "yyyy-MM-dd HH:mm:ss";
        Locale loc = Locale.US;
        SimpleDateFormat df = new SimpleDateFormat(pattern, loc);
        df.setTimeZone(tz);
        when(mockSerializerProvider.getAnnotationIntrospector().findFormat((Annotated)mockBeanProperty)).thenReturn(format);
        when(format.getPattern()).thenReturn(pattern);
        when(format.getTimeZone()).thenReturn(tz);
        when(format.getLocale()).thenReturn(loc);

        JsonSerializer<?> result = datetimeSerializer.createContextual(mockSerializerProvider, mockBeanProperty);
        DateTimeSerializerBase<Calendar> dateTimeSerializer = (DateTimeSerializerBase<Calendar>)result;
        assertEquals(Boolean.FALSE, dateTimeSerializer._useTimestamp);
        assertEquals(df, dateTimeSerializer._customFormat);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(datetimeSerializer.isEmpty(null));
        Calendar value = Calendar.getInstance();
        assertFalse(datetimeSerializer.isEmpty(value));
        value.setTimeInMillis(0);
        assertTrue(datetimeSerializer.isEmpty(value));
    }

    @Test
    public void testGetSchema() {
        JsonNode result = datetimeSerializer.getSchema(mockSerializerProvider, Object.class);
        assertEquals("number", result.getClass().getSimpleName());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonProcessingException {
        when(mockJsonFormatVisitorWrapper.getProvider()).thenReturn(mockSerializerProvider);
        when(mockJsonFormatVisitorWrapper.getSerializerProvider()).thenReturn(mockSerializerProvider);

        datetimeSerializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, Object.class);
        ArgumentCaptor<JsonFormatVisitorWrapper> jsonFormatVisitorWrapperCaptor
                = ArgumentCaptor.forClass(JsonFormatVisitorWrapper.class);
        verify(mockJsonGenerator).setSerializationConfig(jsonFormatVisitorWrapperCaptor.capture());
        JsonFormatVisitorWrapper jsonFormatVisitorWrapper = jsonFormatVisitorWrapperCaptor.getValue();
        assertEquals(JsonParser.NumberType.LONG, jsonFormatVisitorWrapper.getNumberType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, jsonFormatVisitorWrapper.getValueFormat());
    }

    @Test
    public void testSerialize() throws IOException {
        Calendar value = Calendar.getInstance();
        value.setTimeInMillis(1234567890L);

        when(mockJsonGenerator.writeNumber(anyLong())).thenAnswer(invocation -> {
            long argument = invocation.getArgument(0);
            long expected = value.getTimeInMillis();
            assertEquals(expected, argument);
            return null;
        });

        datetimeSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSerializeWithNullProvider() throws IOException {
        datetimeSerializer.serialize(null, mockJsonGenerator, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSerializeWithNullValue() throws IOException {
        datetimeSerializer.serialize(null, mockJsonGenerator, mockSerializerProvider);
    }

    @Test
    public void testWithFormat() {
        boolean useTimestamp = true;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeSerializerBase<Calendar> result = datetimeSerializer.withFormat(useTimestamp, customFormat);
        assertEquals(useTimestamp, result._useTimestamp.booleanValue());
        assertEquals(customFormat, result._customFormat);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testWithFormatWithNullUseTimestamp() {
        DateTimeSerializerBase<Calendar> result = datetimeSerializer.withFormat(null, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    @Test (expected = IllegalArgumentException.class)
    public void testWithFormatWithNullCustomFormat() {
        DateTimeSerializerBase<Calendar> result = datetimeSerializer.withFormat(true, null);
    }

    @Test
    public void testSerializeWithFormat() throws IOException {
        Calendar value = Calendar.getInstance();
        value.setTimeInMillis(1234567890L);
        boolean useTimestamp = true;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeSerializerBase<Calendar> result = datetimeSerializer.withFormat(useTimestamp, customFormat);

        when(mockJsonGenerator.writeString(anyString())).thenAnswer(invocation -> {
            String argument = invocation.getArgument(0);
            String expected = customFormat.format(value.getTime());
            assertEquals(expected, argument);
            return null;
        });

        result.serialize(value, mockJsonGenerator, mockSerializerProvider);
    }
}

class AnnotatedMock extends Annotated {
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "UTC")
    public Calendar value;
}