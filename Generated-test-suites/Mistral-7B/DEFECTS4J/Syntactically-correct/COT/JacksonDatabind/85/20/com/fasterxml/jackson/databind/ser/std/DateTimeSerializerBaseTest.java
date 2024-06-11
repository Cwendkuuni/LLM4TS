package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

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
        longSerializer = new DateTimeSerializerBase<Long>() {
            @Override
            protected long _timestamp(Long value) {
                return value.longValue();
            }
        };

        stringSerializer = new DateTimeSerializerBase<String>() {
            @Override
            protected long _timestamp(String value) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    return formatter.parse(value).getTime();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Test
    public void testIsEmpty() {
        assertFalse(longSerializer.isEmpty(null));
        assertFalse(longSerializer.isEmpty(0L));
        assertTrue(longSerializer.isEmpty(longSerializer));
        assertTrue(longSerializer.isEmpty(longSerializer, null));
        assertTrue(longSerializer.isEmpty(longSerializer, 0L));

        assertFalse(stringSerializer.isEmpty(null));
        assertFalse(stringSerializer.isEmpty(""));
        assertTrue(stringSerializer.isEmpty(stringSerializer));
        assertTrue(stringSerializer.isEmpty(stringSerializer, null));
        assertTrue(stringSerializer.isEmpty(stringSerializer, ""));
    }

    @Test
    public void testGetSchema() {
        assertEquals("number", longSerializer.getSchema(null, Object.class.getType()).toString());
        assertEquals("string", stringSerializer.getSchema(null, Object.class.getType()).toString());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();
        BeanProperty property = mock(BeanProperty.class);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setPattern("yyyy-MM-dd HH:mm:ss");
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        JsonSerializer<?> serializer = longSerializer.createContextual(serializers, property);
        JsonSerializer<?> expectedSerializer = new DateTimeSerializerBase<Long>(Long.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US, TimeZone.getTimeZone("UTC")));
        assertEquals(expectedSerializer.getClass(), serializer.getClass());

        serializer = stringSerializer.createContextual(serializers, property);
        expectedSerializer = new DateTimeSerializerBase<String>(String.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US, TimeZone.getTimeZone("UTC")));
        assertEquals(expectedSerializer.getClass(), serializer.getClass());

        // Test with null format
        serializer = longSerializer.createContextual(serializers, null);
        assertSame(longSerializer, serializer);

        serializer = stringSerializer.createContextual(serializers, null);
        assertSame(stringSerializer, serializer);

        // Test with no format
        serializer = longSerializer.createContextual(serializers, property);
        assertSame(longSerializer, serializer);

        serializer = stringSerializer.createContextual(serializers, property);
        assertSame(stringSerializer, serializer);

        // Test with non-string format
        format.setShape(JsonFormat.Shape.NUMBER);
        serializer = longSerializer.createContextual(serializers, property);
        assertEquals(longSerializer.getClass(), serializer.getClass());

        serializer = stringSerializer.createContextual(serializers, property);
        assertEquals(stringSerializer.getClass(), serializer.getClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualWithNullProvider() throws JsonMappingException {
        longSerializer.createContextual(null, null);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        longSerializer.serialize(null, gen, null);
        assertArrayEquals(new byte[0], gen.getOutputBuffer().toByteArray());

        Long value = 1234567890123L;
        longSerializer.serialize(value, gen, null);
        long expected = value.longValue();
        byte[] bytes = gen.getOutputBuffer().toByteArray();
        assertEquals(Long.toString(expected).getBytes(), bytes);

        String formattedDate = "2023-03-29 14:24:15";
        stringSerializer.serialize(formattedDate, gen, null);
        bytes = gen.getOutputBuffer().toByteArray();
        assertEquals(formattedDate.getBytes(), bytes);

        // Test serializing null value
        stringSerializer.serialize(null, gen, null);
        bytes = gen.getOutputBuffer().toByteArray();
        assertTrue(bytes.length == 0);
    }

    @Test(expected = IOException.class)
    public void testSerializeWithNullGenerator() throws IOException {
        longSerializer.serialize(null, null, null);
    }

    @Test
    public void testWithFormat() {
        Boolean useTimestamp = true;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeSerializerBase<Long> serializer1 = new DateTimeSerializerBase<Long>(Long.class, useTimestamp, customFormat);
        DateTimeSerializerBase<Long> serializer2 = serializer1.withFormat(false, null);

        assertEquals(Long.class, serializer1.getHandledType());
        assertEquals(Long.class, serializer2.getHandledType());

        assertFalse(serializer1._useTimestamp.booleanValue());
        assertFalse(serializer2._useTimestamp.booleanValue());

        assertNotNull(serializer1._customFormat);
        assertNull(serializer2._customFormat);
    }
}