package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Long> longSerializer;
    private DateTimeSerializerBase<String> stringSerializer;

    @Before
    public void setup() {
        JsonSerializerProvider serializers = Mockito.mock(JsonSerializerProvider.class);
        longSerializer = new DateTimeSerializerBase<Long>() {
            @Override
            protected long _timestamp(Long value) {
                return value.longValue();
            }

            @Override
            public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeNumber(value.longValue());
            }
        }.withFormat(Boolean.TRUE, null);

        stringSerializer = new DateTimeSerializerBase<String>() {
            @Override
            protected long _timestamp(String value) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    return format.parse(value).getTime();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value);
            }
        }.withFormat(Boolean.FALSE, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void testIsEmpty() {
        assertTrue(longSerializer.isEmpty(null));
        assertTrue(longSerializer.isEmpty(0L));
        assertFalse(longSerializer.isEmpty(1L));
        assertTrue(stringSerializer.isEmpty(null));
        assertTrue(stringSerializer.isEmpty(""));
        assertFalse(stringSerializer.isEmpty("2022-01-01 12:00:00"));
    }

    @Test
    public void testGetSchema() {
        assertEquals("number", longSerializer.getSchema(null, Long.TYPE).getClassId().toString());
        assertEquals("string", stringSerializer.getSchema(null, String.class).getClassId().toString());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonFormat format = Mockito.mock(JsonFormat.class);
        Mockito.when(format.hasShape()).thenReturn(JsonFormat.Shape.STRING);
        Mockito.when(format.getPattern()).thenReturn("yyyy-MM-dd HH:mm:ss");
        Mockito.when(format.getLocale()).thenReturn(Locale.US);
        Mockito.when(format.getTimeZone()).thenReturn(TimeZone.getDefault());

        JsonSerializer<String> serializer = stringSerializer.createContextual(null, null);
        assertEquals(String.class, serializer.getClass());
        JsonFormat jsonFormat = Mockito.argumentCaptor(JsonFormat.class).capture(serializer.getClass());
        assertEquals("yyyy-MM-dd HH:mm:ss", jsonFormat.getValue().getPattern());
        assertEquals(Locale.US, jsonFormat.getValue().getLocale());
        assertEquals(TimeZone.getDefault(), jsonFormat.getValue().getTimeZone());
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        ObjectWriter writer = Mockito.mock(ObjectWriter.class);
        Mockito.when(writer.writeValueAsString(Mockito.any())).thenReturn("test");
        Mockito.when(serializers.getProvider()).thenReturn(writer);
        Mockito.when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);

        long value1 = 1633043200000L; // Thu, 01 Jul 2021 00:00:00 GMT
        long value2 = 1633135600000L; // Fri, 02 Jul 2021 00:00:00 GMT
        String value3 = "2021-07-01 00:00:00";

        longSerializer.serialize(value1, gen, serializers);
        gen.flush();
        String result1 = (String) Mockito.when(writer.valueAsString()).thenReturn(null);
        assertEquals("1633043200000", result1);

        longSerializer.serialize(value2, gen, serializers);
        gen.flush();
        String result2 = (String) Mockito.when(writer.valueAsString()).thenReturn(null);
        assertEquals("1633135600000", result2);

        stringSerializer.serialize(value3, gen, serializers);
        gen.flush();
        String result3 = (String) Mockito.when(writer.valueAsString()).thenReturn(null);
        assertEquals("\"2021-07-01 00:00:00\"", result3);
    }
}