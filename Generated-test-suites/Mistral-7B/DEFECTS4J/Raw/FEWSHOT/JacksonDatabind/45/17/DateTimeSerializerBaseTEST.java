package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;

public class DateTimeSerializerBaseTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testDateTimeSerializerBase() {
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE, null);
        assertTrue(serializer instanceof ContextualSerializer);
        assertFalse(serializer.useTimestamp());
        assertNull(serializer.customFormat());

        DateTimeSerializerBase<Long> timestampSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.TRUE, null);
        assertTrue(timestampSerializer instanceof ContextualSerializer);
        assertTrue(timestampSerializer.useTimestamp());
        assertNull(timestampSerializer.customFormat());

        DateTimeSerializerBase<Long> customFormatSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, Boolean.FALSE,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US));
        assertTrue(customFormatSerializer instanceof ContextualSerializer);
        assertFalse(customFormatSerializer.useTimestamp());
        assertEquals(customFormatSerializer.customFormat(), expectedFormat());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<String> stringSerializer = mapper.findValueSerializer(String.class);
        JsonSerializer<Long> timestampSerializer = mapper.findValueSerializer(Long.class);

        JsonFormat.Value dateFormatAnnotation = new JsonFormat.Value();
        dateFormatAnnotation.setShape(JsonFormat.Shape.STRING);
        dateFormatAnnotation.setPattern("yyyy-MM-dd HH:mm:ss z");
        dateFormatAnnotation.setLocale(Locale.US);
        dateFormatAnnotation.setTimeZone(TimeZone.getDefault());

        JsonSerializer<Long> customFormatSerializer = stringSerializer.withResolvedAnnotation(dateFormatAnnotation);

        BeanProperty property = new BeanProperty("testProperty", Long.class);
        assertEquals(customFormatSerializer, mapper.getSerializerProvider().getSerializerProvider().getSerializerProvider()
                .getSerializerProvider()
                .getSerializerProvider()
                .getSerializerProvider()
                .getSerializerProvider()
                .getBeanPropertySerializerProvider()
                .findSerializer(property));

        assertEquals(timestampSerializer, mapper.getSerializerProvider().getSerializerProvider().getSerializerProvider()
                .getSerializerProvider()
                .getSerializerProvider()
                .getSerializerProvider()
                .getSerializerProvider()
                .getBeanPropertySerializerProvider()
                .findSerializer(property.withAnnotation(JsonFormat.class)));
    }

    private DateFormat expectedFormat() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        return df;
    }
}