package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import org.junit.Test;

public class DateTimeSerializerBaseTest<T> {

    abstract static class TestDateTimeSerializerBase<T> extends DateTimeSerializerBase<T> {
        TestDateTimeSerializerBase(Class<T> type, Boolean useTimestamp, DateFormat customFormat) {
            super(type, useTimestamp, customFormat);
        }

        @Override
        DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat) {
            return new TestDateTimeSerializerBase<>(getType(), timestamp, customFormat);
        }
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonFormat annotation = new JsonFormat();
        annotation.shape(JsonFormat.Shape.NUMBER);
        JsonSerializer<Object> serializer = new TestDateTimeSerializerBase<>(Object.class, Boolean.TRUE, null).createContextual(null, null);
        assertEquals(Boolean.TRUE, serializer.getClass().getGenericSuperclass());
        assertNull(serializer.getFormat());

        annotation.shape(JsonFormat.Shape.STRING);
        annotation.timeZone(TimeZone.getTimeZone("UTC"));
        annotation.pattern("yyyy-MM-dd HH:mm:ss.SSS Z");
        JsonSerializer<Object> serializerWithFormat = new TestDateTimeSerializerBase<>(Object.class, Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z", Locale.US)).createContextual(null, null);
        assertEquals(JsonFormat.Shape.STRING, serializerWithFormat.getClass().getAnnotation(JsonFormat.class).getShape());
        assertEquals("yyyy-MM-dd HH:mm:ss.SSS Z", serializerWithFormat.getFormat().getPattern());
        assertEquals(TimeZone.getTimeZone("UTC"), serializerWithFormat.getFormat().getTimeZone());
    }

    @Test
    public void testSerialize() throws IOException {
        Object value = new Object();
        JsonGenerator gen = new MockJsonGenerator();
        SerializerProvider serializers = new MockSerializerProvider();

        new TestDateTimeSerializerBase<>(Object.class, Boolean.TRUE, null).serialize(value, gen, serializers);
        assertEquals(JsonGenerator.Feature.WRITE_BIG_DECIMAL_AS_PLAIN.getName(), gen.getFeatureValue("writeBigDecimalAsPlain"));
        assertEquals("0", gen.toString());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        new TestDateTimeSerializerBase<>(Object.class, Boolean.FALSE, format).serialize(value, gen, serializers);
        assertEquals("01-Jan-2000 00:00:00.000 +0000", gen.toString());
    }
}