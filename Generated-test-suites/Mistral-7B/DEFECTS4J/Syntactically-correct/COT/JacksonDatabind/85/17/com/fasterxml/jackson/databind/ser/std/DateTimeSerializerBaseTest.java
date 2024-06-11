package com.example.junit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonGenerator;
import com.fasterxml.jackson.databind.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonSerializer.StdSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Long> longSerializer;
    private DateTimeSerializerBase<String> stringSerializer;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        longSerializer = new DateTimeSerializerBase<Long>() {
            @Override
            protected long _timestamp(Long value) {
                return value.longValue();
            }
        };
        stringSerializer = new DateTimeSerializerBase<String>() {
            @Override
            protected long _timestamp(String value) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return format.parse(value).getTime();
            }
        };
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testCreateContextual() throws IOException {
        // Test serializer creation with no format or timestamp flag
        JsonSerializer<Long> serializer = longSerializer.createContextual(null, null);
        Assert.assertSame(longSerializer, serializer);

        // Test serializer creation with a format
        JsonFormat<Long> format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US, TimeZone.getTimeZone("America/New_York"));
        JsonSerializer<Long> serializerWithFormat = longSerializer.withFormat(false, customFormat).createContextual(null, null);
        Assert.assertNotSame(longSerializer, serializerWithFormat);
        Assert.assertTrue(serializerWithFormat instanceof DateTimeSerializerBase);
        Assert.assertEquals(customFormat, ((DateTimeSerializerBase) serializerWithFormat)._customFormat);

        // Test serializer creation with a timestamp flag
        JsonSerializer<Long> serializerWithTimestamp = longSerializer.withFormat(true, null).createContextual(null, null);
        Assert.assertNotSame(longSerializer, serializerWithTimestamp);
        Assert.assertTrue(serializerWithTimestamp instanceof DateTimeSerializerBase);
        Assert.assertNull(((DateTimeSerializerBase) serializerWithTimestamp)._customFormat);
    }

    @Test
    public void testSerialize() throws IOException {
        // Test serializing a null value
        Long nullValue = null;
        JsonGenerator gen = objectMapper.getFactory().createGenerator(new StringWriter());
        longSerializer.serialize(nullValue, gen, null);
        gen.flush();
        String serializedNull = gen.getOutputBuffer().toString();
        Assert.assertEquals("null", serializedNull);

        // Test serializing a non-null value
        Calendar calendar = Calendar.getInstance();
        Long value = calendar.getTimeInMillis();
        gen.writeStartObject();
        longSerializer.serialize(value, gen, null);
        gen.writeEndObject();
        String serializedValue = gen.getOutputBuffer().toString();
        Assert.assertEquals(Long.toString(value), serializedValue);

        // Test serializing a non-null value with a custom format
        gen.writeStartObject();
        stringSerializer.serialize(calendar.getTime().toString(), gen, null);
        gen.writeEndObject();
        String serializedCustomValue = gen.getOutputBuffer().toString();
        Assert.assertEquals(calendar.getTime().toString(), serializedCustomValue);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws IOException {
        // Test accepting the JsonFormatVisitorWrapper with a number format
        JsonFormatVisitorWrapper numberVisitor = null;
        longSerializer.acceptJsonFormatVisitor(numberVisitor, null);
        Assert.assertNotNull(numberVisitor);

        // Test accepting the JsonFormatVisitorWrapper with a string format
        JsonFormatVisitorWrapper stringVisitor = null;
        stringSerializer.acceptJsonFormatVisitor(stringVisitor, null);
        Assert.assertNotNull(stringVisitor);
    }
}