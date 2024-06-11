package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    private DateTimeSerializerBase<T> createDateTimeSerializerBase(Class<T> type, Boolean useTimestamp, DateFormat customFormat) {
        return new DateTimeSerializerBase<T>() {
            @Override
            public DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat) {
                return this;
            }
        }.getClass().getConstructor(type, useTimestamp, customFormat).newInstance(type, useTimestamp, customFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z", Locale.US);
        customFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        DateTimeSerializerBase<Long> longSerializer = createDateTimeSerializerBase(Long.TYPE, Boolean.TRUE, null);
        JsonSerializer<Long> serializedLong = longSerializer.createContextual(null, null);
        assertNotNull(serializedLong);

        DateTimeSerializerBase<String> stringSerializer = createDateTimeSerializerBase(String.class, Boolean.FALSE, customFormat);
        JsonSerializer<String> serializedString = stringSerializer.createContextual(null, null);
        assertNotNull(serializedString);

        JsonFormat.Shape shape = JsonFormat.Value.standardShape(JsonFormat.Feature.WRITE_DATES_AS_TIMESTAMPS);
        DateTimeSerializerBase<Long> longSerializerTimestamp = createDateTimeSerializerBase(Long.TYPE, null, null);
        JsonSerializer<Long> serializedLongWithFormat = longSerializerTimestamp.createContextual(new ObjectMapper().getSerializationConfig(), new BeanProperty("testProperty"));
        assertEquals(shape.getShape(), serializedLongWithFormat.getShape());

        DateTimeSerializerBase<String> stringSerializerCustomFormat = createDateTimeSerializerBase(String.class, null, customFormat);
        JsonSerializer<String> serializedStringWithFormat = stringSerializerCustomFormat.createContextual(new ObjectMapper().getSerializationConfig(), new BeanProperty("testProperty"));
        assertEquals(JsonFormat.Shape.STRING, serializedStringWithFormat.getShape());
        assertEquals("yyyy-MM-dd HH:mm:ss.SSS Z", serializedStringWithFormat.getSerializationConfig().getSerializationConfig().getFormatValue().getFormat().getClass().getName());
    }

    @Test
    public void testSerializeDeserialize() throws JsonGenerationException, JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Long> longSerializer = createDateTimeSerializerBase(Long.TYPE, Boolean.TRUE, null);

        JsonSerializer<Long> serializedLong = longSerializer.createContextual(null, null);
        JsonSerializer<Long> serializedLongWithFormat = longSerializer.createContextual(new ObjectMapper().getSerializationConfig(), new BeanProperty("testProperty"));

        Long dateAsLong = 1646370800000L; // Mon Jan 01 00:00:00 UTC 2018
        Long dateAsLongWrapped = 1646370800000L;

        Long deserializedLong = mapper.readValue("1646370800000", serializedLong.getClass());
        assertEquals(dateAsLong, deserializedLong);

        String dateAsString = "2018-01-01 00:00:00.000Z";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = dateFormat.parse(dateAsString);
        Long deserializedLongFromString = mapper.readValue(mapper.writerWithRootValueSeparator(false).writeValueAsString(date), serializedLongWithFormat.getClass());
        assertEquals(dateAsLong, deserializedLongFromString.longValue());

        JsonGenerator gen = mapper.getPolymorphicSerializer(String.class).getGenerator(mapper, null);
        serializedLong.serialize(dateAsLongWrapped, gen, null);
        assertEquals("1646370800000", gen.getOutputValueAsString());

        serializedLongWithFormat.serialize(dateAsLongWrapped, gen, null);
        assertEquals("1970-01-01T00:00:00.000Z", gen.getOutputValueAsString());
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase<Long> longSerializer = createDateTimeSerializerBase(Long.TYPE, Boolean.TRUE, null);
        assertTrue(longSerializer.isEmpty(null));
        assertTrue(longSerializer.isEmpty(null, null));
        assertTrue(longSerializer.isEmpty(1L));
        assertTrue(longSerializer.isEmpty(1L, null));
        assertTrue(longSerializer.isEmpty(null, 1L));
        assertTrue(longSerializer.isEmpty(null, 0L));

        DateTimeSerializerBase<String> stringSerializer = createDateTimeSerializerBase(String.class, Boolean.FALSE, null);
        assertTrue(stringSerializer.isEmpty(null));
        assertTrue(stringSerializer.isEmpty(null, null));
        assertFalse(stringSerializer.isEmpty(null, ""));
        assertFalse(stringSerializer.isEmpty(null, ""));
    }
}