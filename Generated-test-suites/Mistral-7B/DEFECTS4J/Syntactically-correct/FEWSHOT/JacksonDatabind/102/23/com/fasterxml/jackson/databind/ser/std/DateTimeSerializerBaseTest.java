package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    private static final DateTimeSerializerBase<Date> DATE_SERIALIZER = new DateTimeSerializerBase<Date>(Date.class, false, null);

    @Test
    public void testDateTimeSerializerBase() {
        assertEquals(Date.class, DATE_SERIALIZER.handledType());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Date> serializer = mapper.getSerializerProvider().findValueSerializer(Date.class);
        assertEquals(DATE_SERIALIZER, serializer);

        JsonSerializer<Date> customSerializer = DATE_SERIALIZER.createContextual(mapper.getSerializerProvider(null), null);
        assertEquals(DATE_SERIALIZER, customSerializer);
    }

    @Test
    public void testIsEmpty() {
        assertFalse(DATE_SERIALIZER.isEmpty(null, null));
    }

    @Test
    public void testGetSchema() {
        JsonNode schema = DATE_SERIALIZER.getSchema(null, null);
        assertEquals("number", schema.getTextValue());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getProviderInstance(JsonFormatVisitorWrapper.class);
        assertEquals(DATE_SERIALIZER.handledType(), visitor.getTypeHint());
        DATE_SERIALIZER._acceptJsonFormatVisitor(visitor, null, true);
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        Date date = new Date();
        DATE_SERIALIZER.serialize(date, gen, mapper.getSerializerProvider());
        assertEquals(date.getTime(), gen.getOutputBuffer().readLong());
    }

    @Test
    public void testSerializeWithCustomFormat() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        DateTimeSerializerBase<Date> serializerWithCustomFormat = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
        Date date = new Date();

        serializerWithCustomFormat.serialize(date, gen, mapper.getSerializerProvider());
        String expected = customFormat.format(date);
        byte[] actualBytes = gen.getOutputBuffer().toByteArray();
        String actual = new String(actualBytes, "UTF-8");
        assertEquals(expected, actual);
    }

    @Test
    public void testSerializeWithUseTimestamp() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);

        DateTimeSerializerBase<Date> serializerWithUseTimestamp = new DateTimeSerializerBase<Date>(Date.class, true, null);
        Date date = new Date();

        serializerWithUseTimestamp.serialize(date, gen, mapper.getSerializerProvider());
        assertEquals(date.getTime(), gen.getOutputBuffer().readLong());
    }

    @Test
    public void testDeserialize() throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();

        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream("2023-03-17 14:32:50 EDT".getBytes()));
        Date date = mapper.readValue(parser, Date.class);
        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        Date expectedDate = expectedFormat.parse("2023-03-17 14:32:50 EDT");
        assertEquals(expectedDate, date);
    }

    @Test
    public void testDeserializeWithCustomFormat() throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();

        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream("2023-03-17 14:32:50 EDT".getBytes()));
        DateTimeSerializerBase<Date> serializerWithCustomFormat = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"));
        StdDeserializer<Date> deserializer = (StdDeserializer<Date>) serializerWithCustomFormat.createContextual(mapper.getSerializerProvider(null), null);

        Date date = mapper.readValue(parser, Date.class);
        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        Date expectedDate = expectedFormat.parse("2023-03-17 14:32:50 EDT");
        assertEquals(expectedDate, date);
    }

    @Test
    public void testDeserializeWithUseTimestamp() throws IOException, ParseException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);

        JsonParser parser = mapper.getFactory().createParser(new ByteArrayInputStream("1679360320000".getBytes()));
        DateTimeSerializerBase<Date> serializerWithUseTimestamp = new DateTimeSerializerBase<Date>(Date.class, true, null);
        StdDeserializer<Date> deserializer = (StdDeserializer<Date>) serializerWithUseTimestamp.createContextual(mapper.getSerializerProvider(null), null);

        Date date = mapper.readValue(parser, Date.class);
        assertEquals(new Date(1679360320000L), date);
    }
}