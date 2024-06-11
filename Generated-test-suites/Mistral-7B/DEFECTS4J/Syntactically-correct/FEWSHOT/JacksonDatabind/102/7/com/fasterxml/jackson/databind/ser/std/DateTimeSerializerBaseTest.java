package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Test;

public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    private static final Class<DateTimeSerializerBase<Object>> TYPE = (Class<DateTimeSerializerBase<Object>>) new DateTimeSerializerBase<Object>(Object.class, Boolean.FALSE, null).handledType();

    @Test
    public void testCreateContextual() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        JsonFormat.Value jsonFormat = new JsonFormat.Value();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        jsonFormat.setLocale(Locale.US);
        jsonFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        JsonSerializer<Object> serializer = serializers.findValueSerializer(Object.class);

        JsonFormat<Object> format = JsonFormat.valueSequence(new JsonFormat.Value(JsonFormat.Shape.STRING), jsonFormat);
        DateTimeSerializerBase<Object> serialized = (DateTimeSerializerBase<Object>) serializer.createContextual(serializers, null);

        assertTrue(serialized._useTimestamp == Boolean.FALSE);
        assertNull(serialized._customFormat);
        assertNull(serialized._reusedCustomFormat);

        assertEquals(FORMATTER_STRING, serialized.getClass().getName());
        assertEquals(TYPE, serialized.handledType());

        SimpleDateFormat format0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.US);
        format0.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Object> serializedFormat = (DateTimeSerializerBase<Object>) serialized.withFormat(Boolean.FALSE, format0);

        assertTrue(serializedFormat._useTimestamp == Boolean.FALSE);
        assertSame(format0, serializedFormat._customFormat);
        assertSame(serializedFormat._reusedCustomFormat, serializedFormat._customFormat);

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        format1.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Object> serializedFormat1 = (DateTimeSerializerBase<Object>) serializedFormat.withFormat(Boolean.FALSE, format1);

        assertTrue(serializedFormat1._useTimestamp == Boolean.FALSE);
        assertNotSame(format0, serializedFormat1._customFormat);
        assertSame(serializedFormat1._reusedCustomFormat, serializedFormat1._customFormat);

        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        format2.setTimeZone(TimeZone.getTimeZone("UTC+1"));
        DateTimeSerializerBase<Object> serializedFormat2 = (DateTimeSerializerBase<Object>) serializedFormat1.withFormat(Boolean.FALSE, format2);

        assertTrue(serializedFormat2._useTimestamp == Boolean.FALSE);
        assertNotSame(format1, serializedFormat2._customFormat);
        assertNotSame(serializedFormat1._reusedCustomFormat, serializedFormat2._customFormat);
        assertSame(serializedFormat2._reusedCustomFormat, serializedFormat2._customFormat);

        SerializerProvider serializers2 = new SerializerProvider();
        JsonSerializer<Object> serializer2 = serializers2.findValueSerializer(Object.class);
        DateTimeSerializerBase<Object> serializedFormat3 = (DateTimeSerializerBase<Object>) serializer2.createContextual(serializers2, null);

        assertTrue(serializedFormat3._useTimestamp == Boolean.FALSE);
        assertNull(serializedFormat3._customFormat);
        assertNull(serializedFormat3._reusedCustomFormat);

        SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        format3.setTimeZone(TimeZone.getTimeZone("UTC+1"));
        DateTimeSerializerBase<Object> serializedFormat4 = (DateTimeSerializerBase<Object>) serializedFormat3.withFormat(Boolean.FALSE, format3);

        assertTrue(serializedFormat4._useTimestamp == Boolean.FALSE);
        assertSame(format3, serializedFormat4._customFormat);
        assertSame(serializedFormat4._reusedCustomFormat, serializedFormat4._customFormat);

        DateTimeSerializerBase<Object> serializedFormat5 = (DateTimeSerializerBase<Object>) serializedFormat4.withFormat(Boolean.FALSE, format2);

        assertTrue(serializedFormat5._useTimestamp == Boolean.FALSE);
        assertNotSame(format3, serializedFormat5._customFormat);
        assertSame(serializedFormat5._reusedCustomFormat, serializedFormat5._customFormat);

        SimpleDateFormat format4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        format4.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateTimeSerializerBase<Object> serializedFormat6 = (DateTimeSerializerBase<Object>) serializedFormat5.withFormat(Boolean.FALSE, format4);

        assertTrue(serializedFormat6._useTimestamp == Boolean.FALSE);
        assertNotSame(format3, serializedFormat6._customFormat);
        assertNotSame(serializedFormat5._reusedCustomFormat, serializedFormat6._customFormat);
        assertSame(serializedFormat6._reusedCustomFormat, serializedFormat6._customFormat);
    }

    @Test
    public void testSerialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        JsonSerializer<Date> serializer = serializers.findValueSerializer(Date.class);
        DateTimeSerializerBase<Date> serialized = (DateTimeSerializerBase<Date>) serializer.createContextual(serializers, null);
        DateTimeSerializerBase<Date> serializedFormat = (DateTimeSerializerBase<Date>) serialized.withFormat(Boolean.FALSE, format);

        Date date = new Date();

        JsonGenerator gen = serializers.getFactory().createGenerator();
        serializedFormat.serialize(date, gen, serializers);
        gen.flush();

        String string1 = gen.getOutputContent();
        String string2 = format.format(date);

        assertEquals(string2, string1);

        SerializerProvider serializers2 = new SerializerProvider();
        JsonSerializer<Date> serializer2 = serializers2.findValueSerializer(Date.class);
        DateTimeSerializerBase<Date> serializedFormat2 = (DateTimeSerializerBase<Date>) serializer2.createContextual(serializers2, null);
        DateTimeSerializerBase<Date> serializedFormat3 = (DateTimeSerializerBase<Date>) serializedFormat2.withFormat(Boolean.FALSE, format);

        JsonGenerator gen2 = serializers2.getFactory().createGenerator();
        serializedFormat3.serialize(date, gen2, serializers2);
        gen2.flush();

        String string3 = gen2.getOutputContent();
        assertEquals(string2, string3);
    }

    @Test
    public void testSerialize_useTimestamp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        JsonSerializer<Date> serializer = serializers.findValueSerializer(Date.class);
        DateTimeSerializerBase<Date> serialized = (DateTimeSerializerBase<Date>) serializer.createContextual(serializers, null);
        DateTimeSerializerBase<Date> serializedFormat = (DateTimeSerializerBase<Date>) serialized.withFormat(Boolean.TRUE, null);

        Date date = new Date();

        JsonGenerator gen = serializers.getFactory().createGenerator();
        serializedFormat.serialize(date, gen, serializers);
        gen.flush();

        String string1 = gen.getOutputContent();

        JsonParser parser = serializers.getFactory().createParser(new ByteArrayInputStream(string1.getBytes()));
        Object readValue = parser.readValueAs(Date.class);

        Date dateRead = (Date) readValue;

        assertEquals(date.getTime(), dateRead.getTime());

        SerializerProvider serializers2 = new SerializerProvider();
        JsonSerializer<Date> serializer2 = serializers2.findValueSerializer(Date.class);
        DateTimeSerializerBase<Date> serializedFormat2 = (DateTimeSerializerBase<Date>) serializer2.createContextual(serializers2, null);
        DateTimeSerializerBase<Date> serializedFormat3 = (DateTimeSerializerBase<Date>) serializedFormat2.withFormat(Boolean.TRUE, null);

        JsonGenerator gen2 = serializers2.getFactory().createGenerator();
        serializedFormat3.serialize(date, gen2, serializers2);
        gen2.flush();

        String string3 = gen2.getOutputContent();

        JsonParser parser2 = serializers2.getFactory().createParser(new ByteArrayInputStream(string3.getBytes()));
        Object readValue2 = parser2.readValueAs(Date.class);

        Date dateRead2 = (Date) readValue2;

        assertEquals(date.getTime(), dateRead2.getTime());
    }

    private static final String FORMATTER_STRING = "com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase";

}