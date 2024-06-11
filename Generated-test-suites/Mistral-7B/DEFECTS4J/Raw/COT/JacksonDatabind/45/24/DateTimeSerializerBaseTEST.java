package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer.StandardSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseConstructor() {
        Class<Integer> intType = Integer.class;
        Boolean useTimestamp = true;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeSerializerBase<Integer> dateTimeSerializer = new DateTimeSerializerBase<>(intType, useTimestamp, customFormat);
        Assert.assertEquals(intType, dateTimeSerializer.getHandlerType());
        Assert.assertEquals(useTimestamp, dateTimeSerializer._useTimestamp);
        Assert.assertEquals(customFormat, dateTimeSerializer._customFormat);
    }

    @Test
    public void testCreateContextual_NullProperty() throws JsonMappingException {
        DateTimeSerializerBase<Integer> dateTimeSerializer = mock(DateTimeSerializerBase.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonFormat.Value format = null;
        Annotated property = mock(Annotated.class);

        when(dateTimeSerializer.createContextual(serializers, property)).thenReturn(dateTimeSerializer);

        DateTimeSerializerBase result = dateTimeSerializer.createContextual(serializers, null);
        Assert.assertEquals(dateTimeSerializer, result);
    }

    @Test
    public void testCreateContextual_FormatIsNumeric() throws JsonMappingException {
        DateTimeSerializerBase<Integer> dateTimeSerializer = mock(DateTimeSerializerBase.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonFormat.Value format = new JsonFormat.Value();
        Annotated property = mock(Annotated.class);

        when(dateTimeSerializer.createContextual(serializers, property)).thenReturn(dateTimeSerializer);
        when(property.getMember()).thenReturn(null);
        when(serializers.getAnnotationIntrospector().findFormat(property)).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.NUMBER);

        DateTimeSerializerBase result = dateTimeSerializer.createContextual(serializers, property);
        Assert.assertEquals(Boolean.TRUE, result._useTimestamp);
        Assert.assertNull(result._customFormat);
    }

    @Test
    public void testCreateContextual_FormatIsString() throws JsonMappingException, ParseException {
        DateTimeSerializerBase<String> dateTimeSerializer = mock(DateTimeSerializerBase.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonFormat.Value format = new JsonFormat.Value();
        Annotated property = mock(Annotated.class);

        when(dateTimeSerializer.createContextual(serializers, property)).thenReturn(dateTimeSerializer);
        when(property.getMember()).thenReturn(null);
        when(serializers.getAnnotationIntrospector().findFormat(property)).thenReturn(format);
        when(format.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(format.getPattern()).thenReturn("yyyy-MM-dd HH:mm:ss");
        when(format.getTimeZone()).thenReturn(TimeZone.getTimeZone("UTC"));

        DateTimeSerializerBase<String> result = (DateTimeSerializerBase<String>) dateTimeSerializer.createContextual(serializers, property);
        Assert.assertEquals(Boolean.FALSE, result._useTimestamp);
        Assert.assertEquals(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"), result._customFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextual_NullSerializerProvider() throws JsonMappingException {
        DateTimeSerializerBase<Integer> dateTimeSerializer = mock(DateTimeSerializerBase.class);
        SerializerProvider serializers = null;
        Annotated property = mock(Annotated.class);

        dateTimeSerializer.createContextual(serializers, property);
    }

    @Test
    public void testIsEmpty_NullValue() {
        DateTimeSerializerBase<Object> dateTimeSerializer = mock(DateTimeSerializerBase.class);
        Assert.assertTrue(dateTimeSerializer.isEmpty(null));
    }

    @Test
    public void testIsEmpty_ZeroTimestamp() {
        DateTimeSerializerBase<Long> dateTimeSerializer = mock(DateTimeSerializerBase.class);
        Assert.assertTrue(dateTimeSerializer.isEmpty(0L));
    }

    @Test
    public void testIsEmpty_NotNullValue() {
        DateTimeSerializerBase<Object> dateTimeSerializer = mock(DateTimeSerializerBase.class);
        Object value = new Object();
        Assert.assertFalse(dateTimeSerializer.isEmpty(value));
    }

    @Test
    public void testSerialize_NumericFormat() throws IOException, JsonGenerationException {
        DateTimeSerializerBase<Long> dateTimeSerializer = new DateTimeSerializerBase<Long>() {
            @Override
            protected long _timestamp(Long value) {
                return Instant.ofEpochMilli(value).getEpochSecond();
            }

            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                gen.writeNumber(value.longValue());
            }
        };
        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        Long value = 1556304423000L;

        dateTimeSerializer.serialize(value, gen, serializers);

        when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);
        when(gen.writeNumber(anyLong())).thenAnswer(invocationOnMock -> {
            long number = invocationOnMock.getArgument(0);
            String expected = String.valueOf(number);
            System.out.println("Serialized number: " + expected);
            return expected;
        });

        dateTimeSerializer.serialize(value, gen, serializers);

        verify(gen, times(1)).writeNumber(1556304423);
    }

    @Test
    public void testSerialize_StringFormat() throws IOException, JsonGenerationException {
        DateTimeSerializerBase<String> dateTimeSerializer = new DateTimeSerializerBase<String>() {
            @Override
            protected long _timestamp(String value) {
                ZonedDateTime dateTime = ZonedDateTime.parse(value);
                return dateTime.toInstant().toEpochMilli();
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                gen.writeString(value);
            }
        };
        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        String value = "2019-06-12T10:15:20Z";

        dateTimeSerializer.serialize(value, gen, serializers);

        when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);
        when(gen.writeString(anyString())).thenAnswer(invocationOnMock -> {
            String string = invocationOnMock.getArgument(0);
            System.out.println("Serialized string: " + string);
            return string;
        });

        dateTimeSerializer.serialize(value, gen, serializers);

        verify(gen, times(1)).writeString(value);
    }

    @Test(expected = JsonGenerationException.class)
    public void testSerialize_InvalidFormat() throws IOException, JsonGenerationException {
        DateTimeSerializerBase<String> dateTimeSerializer = new DateTimeSerializerBase<String>() {
            @Override
            protected long _timestamp(String value) {
                return 0;
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                throw new JsonGenerationException("Serialize error");
            }
        };
        SerializerProvider serializers = mock(SerializerProvider.class);
        JsonGenerator gen = mock(JsonGenerator.class);
        String value = "InvalidFormat";

        dateTimeSerializer.serialize(value, gen, serializers);
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Integer> dateTimeSerializer = new DateTimeSerializerBase<Integer>() {};
        SerializerProvider serializers = mock(SerializerProvider.class);

        JsonNode result = dateTimeSerializer.getSchema(serializers, null);
        String expected = "number";
        Assert.assertEquals(expected, result.getClass().getSimpleName());
    }

    @Test
    public void testGetSchema_AsStringFormat() {
        DateTimeSerializerBase<String> dateTimeSerializer = new DateTimeSerializerBase<String>() {};
        SerializerProvider serializers = mock(SerializerProvider.class);

        JsonNode result = dateTimeSerializer.getSchema(serializers, null);
        String expected = "string";
        Assert.assertEquals(expected, result.getClass().getSimpleName());
    }
}