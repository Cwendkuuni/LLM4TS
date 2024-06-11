package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest<T> {

    // Utility method for creating a sample date
    private Date createDate(int year, int month, int day, int hour, int minute, int second) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month-1, day, hour, minute, second);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<T> serializer = new DateTimeSerializerBaseTestImpl(createDateFormat("yyyy-MM-dd HH:mm:ss")) {};
        DateTimeSerializerBase<T> newSerializer = serializer.withFormat(true, createDateFormat("yyyy-MM-dd HH:mm:ss"));

        // Ensure that newSerializer is not the same object as serializer
        assertNotSame(serializer, newSerializer);

        // Ensure that the flag has been updated
        assertTrue(newSerializer._useTimestamp);

        // Ensure that the custom format has been updated
        assertEquals(createDateFormat("yyyy-MM-dd HH:mm:ss"), newSerializer._customFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<T> serializer = new DateTimeSerializerBaseTestImpl(createDateFormat("yyyy-MM-dd HH:mm:ss")) {};
        SerializerProvider serializers = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);

        // Ensure that the serializer returned is the same object
        assertSame(serializer, serializer.createContextual(serializers, property));
    }

    @Test
    public void testIsEmptyWithValue() {
        DateTimeSerializerBase<T> serializer = new DateTimeSerializerBaseTestImpl(createDateFormat("yyyy-MM-dd HH:mm:ss")) {};
        Date date = createDate(2022, 1, 1, 12, 0, 0);

        assertFalse(serializer.isEmpty(null, date));
    }

    @Test
    public void testIsEmptyWithNullValue() {
        DateTimeSerializerBase<T> serializer = new DateTimeSerializerBaseTestImpl(createDateFormat("yyyy-MM-dd HH:mm:ss")) {};

        assertTrue(serializer.isEmpty(null, null));
    }

    @Test
    public void test_timestamp() {
        DateTimeSerializerBase<T> serializer = new DateTimeSerializerBaseTestImpl(createDateFormat("yyyy-MM-dd HH:mm:ss")) {};
        Date date = createDate(2022, 1, 1, 12, 0, 0);

        long timestamp = serializer._timestamp(date);

        assertEquals(date.getTime(), timestamp);
    }

    @Test
    public void test_acceptJsonFormatVisitor_asNumber() throws JsonMappingException {
        DateTimeSerializerBase<T> serializer = new DateTimeSerializerBaseTestImpl(createDateFormat("yyyy-MM-dd HH:mm:ss")) {};
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        boolean asNumber = true;

        serializer.acceptJsonFormatVisitor(visitor, typeHint, asNumber);

        verify(visitor).visitIntFormat(typeHint, JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC);
    }

    @Test
    public void test_acceptJsonFormatVisitor_notAsNumber() throws JsonMappingException {
        DateTimeSerializerBase<T> serializer = new DateTimeSerializerBaseTestImpl(createDateFormat("yyyy-MM-dd HH:mm:ss")) {};
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);
        boolean asNumber = false;

        serializer.acceptJsonFormatVisitor(visitor, typeHint, asNumber);

        verify(visitor).visitStringFormat(typeHint, JsonValueFormat.DATE_TIME);
    }

    @Test
    public void test_serialize() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);

        DateTimeSerializerBase<T> serializer = new DateTimeSerializerBaseTestImpl(createDateFormat("yyyy-MM-dd HH:mm:ss")) {};

        Date date = createDate(2022, 1, 1, 12, 0, 0);
        serializer.serialize(date, gen, serializers);

        verify(gen).writeNumber(date.getTime());
    }

    // Utility method for creating a date format
    private DateFormat createDateFormat(String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        sdf.setLenient(false);
        return sdf;
    }

    // Test implementation of DateTimeSerializerBase for testing abstract methods
    private class DateTimeSerializerBaseTestImpl extends DateTimeSerializerBase<T> {
        public DateTimeSerializerBaseTestImpl(DateFormat customFormat) {
            super(null, null, customFormat);
        }

        @Override
        public DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat) {
            return null;
        }

        @Override
        protected long _timestamp(T value) {
            return 0;
        }

        @Override
        public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {}

        @Override
        public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {}
    }
}