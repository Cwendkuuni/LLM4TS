package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    private static final Class<Object> HANDLED_TYPE = Object.class;
    private static final Long TIMESTAMP_VALUE = 1615136221000L; // Thu, 22 Jul 2021 12:37:01 GMT

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    static class TestDate {
        @JsonValue
        public String value;

        TestDate() {}

        TestDate(String value) {
            this.value = value;
        }
    }

    @Test
    public void testDateTimeSerializerBase() {
        verifyNoMoreInteractions(mock(JsonParser.class), mock(JsonGenerator.class), mock(SerializerProvider.class));
    }

    @Test
    public void testIsEmpty() {
        assertNull(DateTimeSerializerBase.INSTANCE.isEmpty(null));
        assertNull(DateTimeSerializerBase.INSTANCE.isEmpty(new TestDate(null)));
        assertNull(DateTimeSerializerBase.INSTANCE.isEmpty(new TestDate("")));
        assertTrue(DateTimeSerializerBase.INSTANCE.isEmpty(new TestDate("")));
        assertTrue(DateTimeSerializerBase.INSTANCE.isEmpty(new TestDate(null), mock(SerializerProvider.class)));
        assertTrue(DateTimeSerializerBase.INSTANCE.isEmpty(new TestDate(""), mock(SerializerProvider.class)));
    }

    @Test
    public void testTimestamp() {
        DateTimeSerializerBase<TestDate> serializer = new DateTimeSerializerBase<TestDate>(HANDLED_TYPE, Boolean.FALSE, null);
        assertFalse(serializer._asTimestamp(null));
        assertFalse(serializer._asTimestamp(mock(SerializerProvider.class)));
        assertFalse(serializer._asTimestamp(mock(SerializerProvider.class), new TestDate("")));
        assertFalse(serializer._asTimestamp(mock(SerializerProvider.class), new TestDate("2021-07-22T12:37:01.000Z")));
        assertTrue(serializer._asTimestamp(mock(SerializerProvider.class), new TestDate(TIMESTAMP_VALUE)));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<TestDate> serializer = new DateTimeSerializerBase<TestDate>(HANDLED_TYPE, Boolean.FALSE, null);

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        serializers.setContextualSerializerProvider(new StandardSerializerProvider());
        serializers.setContextualSerializerProvider(new StandardSerializerProvider());

        when(visitor.getProvider()).thenReturn(serializers);

        serializer._acceptJsonFormatVisitor(visitor, HANDLED_TYPE, false);

        verify(visitor).visitStringFormat(eq(HANDLED_TYPE), any(), eq(JsonValueFormat.DATE_TIME));
    }

    @Test
    public void testSerialize() throws IOException, JsonMappingException, JsonProcessingException {
        DateTimeSerializerBase<TestDate> serializer = new DateTimeSerializerBase<TestDate>(HANDLED_TYPE, Boolean.FALSE, null);

        TestDate testDate = new TestDate("2021-07-22T12:37:01.000Z");
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = new SerializerProvider();

        when(gen.writeString(eq("2021-07-22T12:37:01.000Z"))).thenReturn(null);

        serializer.serialize(testDate, gen, serializers);

        verify(gen).writeString("2021-07-22T12:37:01.000Z");
    }

    @Test
    public void testSerializeWithTimestamp() throws IOException, JsonMappingException, JsonProcessingException {
        DateTimeSerializerBase<TestDate> serializer = new DateTimeSerializerBase<TestDate>(HANDLED_TYPE, Boolean.TRUE, null);

        TestDate testDate = new TestDate(TIMESTAMP_VALUE);
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = new SerializerProvider();

        when(gen.writeNumber(eq(TIMESTAMP_VALUE))).thenReturn(null);

        serializer.serialize(testDate, gen, serializers);

        verify(gen).writeNumber(TIMESTAMP_VALUE);
    }

    @Test
    public void testCreateContextualWithCustomFormat() throws JsonMappingException {
        DateTimeSerializerBase<TestDate> serializer = new DateTimeSerializerBase<TestDate>(HANDLED_TYPE, Boolean.FALSE, null);

        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        serializers.setContextualSerializerProvider(new StandardSerializerProvider());

        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setLocale(Locale.US);

        JsonFormat jsonFormat = new JsonFormat(HANDLED_TYPE, format);
        JsonFormatVisitorWrapper visitor = JSON_FORMAT_VISITOR_FACTORY.createFormatVisitor(JsonFormat.Value.class, serializers, jsonFormat);

        JsonSerializer<TestDate> result = serializer.createContextual(serializers, null);

        assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<TestDate> castedResult = (DateTimeSerializerBase<TestDate>) result;
        assertEquals(format.getPattern(), castedResult._customFormat.toPattern());
        assertEquals(format.getLocale(), castedResult._customFormat.getLocale());
        assertEquals(format.getTimeZone(), castedResult._customFormat.getTimeZone());
    }

    @Test
    public void testCreateContextualWithDefaultFormat() throws JsonMappingException {
        DateTimeSerializerBase<TestDate> serializer = new DateTimeSerializerBase<TestDate>(HANDLED_TYPE, Boolean.FALSE, null);

        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        serializers.setContextualSerializerProvider(new StandardSerializerProvider());

        JsonFormat jsonFormat = new JsonFormat(HANDLED_TYPE, null);
        JsonFormatVisitorWrapper visitor = JSON_FORMAT_VISITOR_FACTORY.createFormatVisitor(JsonFormat.Value.class, serializers, jsonFormat);

        JsonSerializer<TestDate> result = serializer.createContextual(serializers, null);

        assertNotNull(result);
        assertFalse(result instanceof DateTimeSerializerBase);
    }

    @Test
    public void testCreateContextualWithCustomFormatAndTimestamp() throws JsonMappingException {
        DateTimeSerializerBase<TestDate> serializer = new DateTimeSerializerBase<TestDate>(HANDLED_TYPE, Boolean.TRUE, null);

        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        serializers.setContextualSerializerProvider(new StandardSerializerProvider());

        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.NUMBER);

        JsonFormat jsonFormat = new JsonFormat(HANDLED_TYPE, format);
        JsonFormatVisitorWrapper visitor = JSON_FORMAT_VISITOR_FACTORY.createFormatVisitor(JsonFormat.Value.class, serializers, jsonFormat);

        JsonSerializer<TestDate> result = serializer.createContextual(serializers, null);

        assertTrue(result instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<TestDate> castedResult = (DateTimeSerializerBase<TestDate>) result;
        assertFalse(castedResult._useTimestamp.booleanValue());
        assertNull(castedResult._customFormat);
    }

    @Test
    public void testCreateContextualWithNullFormat() throws JsonMappingException {
        DateTimeSerializerBase<TestDate> serializer = new DateTimeSerializerBase<TestDate>(HANDLED_TYPE, Boolean.FALSE, null);

        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        serializers.setContextualSerializerProvider(new StandardSerializerProvider());

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);

        JsonSerializer<TestDate> result = serializer.createContextual(serializers, null);

        assertNotNull(result);
        assertFalse(result instanceof DateTimeSerializerBase);
    }

    private static JsonFormatVisitorFactory JSON_FORMAT_VISITOR_FACTORY = new StandardJsonFormatVisitorFactory();
}