package com.example; // replace with your package name

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class DateTimeSerializerBaseTest {

    private SerializerProvider serializers;
    private JsonParser parser;
    private JsonGenerator generator;
    private DateTimeSerializerBase<Date> dateTimeSerializer;

    @Before
    public void setup() {
        serializers = mock(SerializerProvider.class);
        parser = mock(JsonParser.class);
        generator = mock(JsonGenerator.class);

        dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null) {
            @Override
            protected long _timestamp(Date value) {
                return value.getTime();
            }

            @Override
            protected void serialize(Date value, JsonGenerator gen, SerializerProvider serializers)
                    throws IOException {
                _serializeAsString(value, gen, serializers);
            }
        };
    }

    @Test
    public void testConstructor() {
        DateTimeSerializerBase<Date> datetimeSerializer = new DateTimeSerializerBase<>(Date.class, false, null);
        assertNull(datetimeSerializer._customFormat);
        assertFalse(datetimeSerializer._useTimestamp);
        assertNull(datetimeSerializer._reusedCustomFormat);
    }

    @Test
    public void testWithFormat() {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        DateTimeSerializerBase<Date> datetimeSerializer = new DateTimeSerializerBase<>(Date.class, false, customFormat);
        DateTimeSerializerBase<Date> withFormatTimestamp = datetimeSerializer.withFormat(Boolean.TRUE, null);
        assertSame(datetimeSerializer, withFormatTimestamp);

        DateTimeSerializerBase<Date> withFormatCustomDateFormat = datetimeSerializer.withFormat(Boolean.FALSE, customFormat);
        assertFalse(withFormatCustomDateFormat._useTimestamp);
        assertSame(customFormat, withFormatCustomDateFormat._customFormat);
        assertNull(withFormatCustomDateFormat._reusedCustomFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        JsonFormat.Value format = JsonFormat.stdFormat(new JsonFormat.Value());
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        when(serializers.getConfig()).thenReturn(new ObjectMapper.DefaultConfig());
        when(serializers.getLocale()).thenReturn(Locale.US);
        when(serializers.getTimeZone()).thenReturn(TimeZone.getTimeZone("UTC"));

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);

        DateTimeSerializerBase<Date> datetimeSerializer = new DateTimeSerializerBase<>(Date.class, false, null);
        DateTimeSerializerBase<Date> serializer = datetimeSerializer.createContextual(serializers, null);
        assertSame(datetimeSerializer, serializer);

        DateTimeSerializerBase<Date> withFormat = datetimeSerializer.createContextual(serializers, null);
        assertFalse(withFormat._useTimestamp);
        assertNull(withFormat._customFormat);
        assertNull(withFormat._reusedCustomFormat);

        when(serializers.getConfig()).thenReturn(new ObjectMapper.DefaultConfig().withFormatOverrides(format));

        serializer = datetimeSerializer.createContextual(serializers, null);
        assertFalse(serializer._useTimestamp);
        assertSame(format, serializer._customFormat);
        assertNull(serializer._reusedCustomFormat);
    }

    @Test
    public void testSerialize() throws IOException {
        when(parser.getValueAsDate()).thenReturn(new Date());
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        when(generator.getOutputFactory()).thenReturn(new JsonFactory().getOutputFactory());

        dateTimeSerializer.serialize(parser.getValueAs(Date.class), generator, serializers);
        String serializedDate = generator.getOutput().getString();
        assertSame("2022-12-31T23:59:59Z", serializedDate);

        _reusedCustomFormat.set(customFormat);
        dateTimeSerializer.serialize(new Date(), generator, serializers);
        serializedDate = generator.getOutput().getString();
        assertSame("2022-12-31T23:59:59Z", serializedDate);
    }
}