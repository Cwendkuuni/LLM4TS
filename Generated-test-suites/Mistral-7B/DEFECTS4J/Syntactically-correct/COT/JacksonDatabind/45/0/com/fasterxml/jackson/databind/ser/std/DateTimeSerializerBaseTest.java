package com.example.junit.test.serializer;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase<Date> dateTimeSerializer;

    @Before
    public void setUp() {
        dateTimeSerializer = new DateTimeSerializerBase<Date>(Date.class, false, null);
    }

    @Test
    public void testConstructor() {
        assertNotNull(dateTimeSerializer);
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Date> serialized = dateTimeSerializer.withFormat(true, null);
        assertSame(dateTimeSerializer, serialized);

        DateTimeSerializerBase<Date> serializedWithFormat = dateTimeSerializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy", Locale.US));
        assertNotSame(dateTimeSerializer, serializedWithFormat);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(dateTimeSerializer.isEmpty(null));
        assertTrue(dateTimeSerializer.isEmpty(new Date(0L)));
        assertFalse(dateTimeSerializer.isEmpty(new Date()));
    }

    @Test
    public void testGetSchema() throws JsonProcessingException {
        String schema = dateTimeSerializer.getSchema(new SerializerProvider(), Date.class.getGenericType());
        assertEquals("string", schema);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        // Verify that the serializer accepts the default format when not provided
        dateTimeSerializer.acceptJsonFormatVisitor(null, Date.class.getGenericType());

        // Verify that the serializer accepts a custom format
        SerializerProvider provider = new SerializerProvider();
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        dateTimeSerializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(provider), Date.class.getGenericType(), false);
        assertEquals(customFormat, dateTimeSerializer.getFormat(provider));
    }

    @Test
    public void testSerialize() throws JsonGenerationException, JsonProcessingException, ParseException {
        SerializerProvider serializers = new SerializerProvider();
        SerializerProvider serializersWithFormat = new SerializerProvider();
        serializersWithFormat.setDateFormatInstances(Collections.singletonMap("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")));

        JsonGenerator jsonGenerator = serializers.getJsonFactory().createGenerator(System.out);
        JsonGenerator jsonGeneratorWithFormat = serializersWithFormat.getJsonFactory().createGenerator(System.out);

        Date date = new Date();

        dateTimeSerializer.serialize(date, jsonGenerator, serializers);
        assertEquals("2023-03-22T15:10:30.000Z", jsonGenerator.getOutputText());

        dateTimeSerializer.withFormat(false, new SimpleDateFormat("dd-MM-yyyy", Locale.US)).serialize(date, jsonGeneratorWithFormat, serializersWithFormat);
        assertEquals("22-03-2023", jsonGeneratorWithFormat.getOutputText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeNullProvider() {
        dateTimeSerializer.serialize(new Date(), null, new SerializerProvider());
    }

    @Test(expected = IOException.class)
    public void testSerializeNullValue() {
        dateTimeSerializer.serialize(null, new JsonGenerator(), new SerializerProvider());
    }

    @Test(expected = JsonProcessingException.class)
    public void testSerializeInvalidFormat() throws JsonMappingException, JsonProcessingException {
        SerializerProvider serializers = new SerializerProvider();
        Date date = new Date();
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy-MM-dd", Locale.US);
        dateTimeSerializer.withFormat(false, customFormat).setFormat(customFormat).serialize(date, new JsonGenerator(), serializers);
    }
}