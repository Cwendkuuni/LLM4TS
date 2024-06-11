package com.example.junit;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Test
    public void testConstructor() {
        Class<String> type = String.class;
        Boolean useTimestamp = false;
        DateFormat customFormat = null;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(type, useTimestamp, customFormat);
        assertNotNull(serializer);
    }

    @Test
    public void testWithFormat() {
        Class<String> type = String.class;
        Boolean useTimestamp = false;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(type, useTimestamp, customFormat);
        DateTimeSerializerBase<String> withFormat = serializer.withFormat(false, customFormat);
        assertEquals(customFormat, withFormat._customFormat);
    }

    @Test
    public void testCreateContextual() {
        Class<String> type = String.class;
        Boolean useTimestamp = false;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(type, useTimestamp, customFormat);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        ObjectNode node = mock(ObjectNode.class);

        when(serializers.getConfig()).thenReturn(new ObjectMapper.DefaultConfig());
        when(serializers.getDefaultNullValueSerializer()).thenReturn(null);
        when(serializer.createContextual(serializers, null)).thenReturn(serializer);
        when(serializer.createContextual(serializers, null)).thenReturn(serializer);
        when(serializer.createContextual(serializers, mock(BeanProperty.class))).thenReturn(withFormat(false, customFormat, serializers));

        DateTimeSerializerBase<String> withFormat = serializer.withFormat(false, customFormat);
        JsonSerializer<?> serialized = withFormat.createContextual(serializers, null);
        assertEquals(withFormat, serialized);

        // Test with null provider
        DateTimeSerializerBase<String> withFormatNullProvider = serializer.withFormat(false, customFormat);
        JsonSerializer<?> serializedNullProvider = withFormatNullProvider.createContextual(null, null);
        assertNull(serializedNullProvider);
    }

    @Test
    public void testSerialize() throws IOException {
        Class<String> type = String.class;
        Boolean useTimestamp = false;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(type, useTimestamp, customFormat);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = mock(SerializerProvider.class);
        ObjectNode node = mock(ObjectNode.class);
        Date date = new Date();

        when(serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);
        when(serializer._customFormat).thenReturn(customFormat);
        when(serializer._reusedCustomFormat.get()).thenReturn(customFormat);
        when(serializer._reusedCustomFormat.getAndSet(null)).thenReturn(null);

        when(node.put("value")).thenAnswer(invocation -> invocation.getArgument(0));

        serializer.serialize(date, gen, serializers);
        verify(gen).writeString(customFormat.format(date));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeWithNullProvider() throws IOException {
        Class<String> type = String.class;
        Boolean useTimestamp = false;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(type, useTimestamp, customFormat);

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider serializers = null;
        ObjectNode node = mock(ObjectNode.class);
        Date date = new Date();

        when(serializers.getDefaultNullValueSerializer()).thenReturn(null);
        when(serializer._customFormat).thenReturn(customFormat);

        serializer.serialize(date, gen, serializers);
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testIsEmpty() {
        Class<String> type = String.class;
        Boolean useTimestamp = false;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(type, useTimestamp, null);
        assertFalse(serializer.isEmpty(null, null));
    }

    @Test
    public void testGetSchema() {
        Class<String> type = String.class;
        Boolean useTimestamp = true;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(type, useTimestamp, null);
        JsonNode schema = serializer.getSchema(null, type);
        assertEquals("number", schema.getClass().getSimpleName());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        Class<String> type = String.class;
        Boolean useTimestamp = false;
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(type, useTimestamp, null);

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JsonParser jsonParser = mock(JsonParser.class);

        when(jsonParser.getProvider()).thenReturn(new SerializerProvider());
        when(visitor.getProvider()).thenReturn(jsonParser);
        when(visitor.getType()).thenReturn(type);

        serializer.acceptJsonFormatVisitor(visitor, type);

        verify(visitor).visitStringFormat(eq(type), any(JsonValueFormat.class));
    }

    private JsonSerializer<?> withFormat(boolean asNumber, DateFormat customFormat, SerializerProvider serializers) {
        StdDateFormat stdDateFormat = new StdDateFormat();
        if (customFormat != null) {
            stdDateFormat.withLocale(customFormat.getLocale());
            stdDateFormat.withTimeZone(customFormat.getTimeZone());
        }
        return new DateTimeSerializerBase<String>(type, useTimestamp, customFormat).withFormat(asNumber, stdDateFormat);
    }

}