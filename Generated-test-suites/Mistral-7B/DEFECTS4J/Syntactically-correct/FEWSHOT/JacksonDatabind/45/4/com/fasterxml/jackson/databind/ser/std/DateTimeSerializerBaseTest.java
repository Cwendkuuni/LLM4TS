package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest<T> {

    @Test
    public void testCreateContextual() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test serializing a Date using default settings
        DateTimeSerializerBase<Date> dateSerializer = mapper.findValueSerializer(Date.class);
        assertEquals(Date.class, dateSerializer.getClass());
        assertTrue(dateSerializer instanceof DateTimeSerializerBase);
        assertTrue(dateSerializer instanceof StdScalarSerializer);
        assertFalse(dateSerializer instanceof ContextualSerializer);

        // Test serializing a Date using custom serialization settings
        JsonFormat customFormat = new JsonFormat.Builder()
                .shape(JsonFormat.Shape.STRING)
                .timeZone(TimeZone.getTimeZone("UTC"))
                .pattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .build();
        ObjectWriter writer = mapper.writerWithView(new SimpleBeanPropertyView());
        JsonSerializer<Date> customDateSerializer = writer.findValueSerializer(Date.class);
        assertEquals(Date.class, customDateSerializer.getClass());
        assertTrue(customDateSerializer instanceof DateTimeSerializerBase);
        assertTrue(customDateSerializer instanceof StdScalarSerializer);
        assertTrue(customDateSerializer instanceof ContextualSerializer);
        assertEquals(customFormat, customDateSerializer.getSerializationConfig().getAnnotationIntrospector().findFormat((Annotated)customDateSerializer.getProvider().getProviderInstance(Date.class)));

        // Test serializing a LocalDateTime using custom serialization settings
        LocalDateTime localDateTime = LocalDateTime.of(2023, 1, 1, 12, 30);
        JsonFormat localDateTimeFormat = new JsonFormat.Builder()
                .shape(JsonFormat.Shape.STRING)
                .timeZone(TimeZone.getTimeZone("UTC"))
                .pattern("yyyy-MM-dd HH:mm")
                .build();
        ObjectWriter localDateTimeWriter = mapper.writerWithView(new SimpleBeanPropertyView());
        JsonSerializer<LocalDateTime> localDateTimeSerializer = localDateTimeWriter.findValueSerializer(LocalDateTime.class);
        assertEquals(LocalDateTime.class, localDateTimeSerializer.getClass());
        assertTrue(localDateTimeSerializer instanceof DateTimeSerializerBase);
        assertTrue(localDateTimeSerializer instanceof StdScalarSerializer);
        assertTrue(localDateTimeSerializer instanceof ContextualSerializer);
        assertEquals(localDateTimeFormat, localDateTimeSerializer.getSerializationConfig().getAnnotationIntrospector().findFormat((Annotated)localDateTimeSerializer.getProvider().getProviderInstance(LocalDateTime.class)));
    }

    @Test
    public void testSerialize() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test serializing a Date using default settings
        Date date = new Date();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        DateTimeSerializerBase<Date> dateSerializer = mapper.findValueSerializer(Date.class);
        dateSerializer.serialize(date, gen, null);
        gen.flush();
        byte[] serializedDate = ((ByteArrayOutputStream)gen.getOutputStream()).toByteArray();
        assertEquals("2022-12-31T23:59:59.000Z", new String(serializedDate));

        // Test serializing a Date using custom serialization settings
        JsonFormat customFormat = new JsonFormat.Builder()
                .shape(JsonFormat.Shape.STRING)
                .timeZone(TimeZone.getTimeZone("UTC"))
                .pattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .build();
        ObjectWriter writer = mapper.writerWithView(new SimpleBeanPropertyView());
        JsonSerializer<Date> customDateSerializer = writer.findValueSerializer(Date.class);
        Date customDate = new Date();
        gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        customDateSerializer.serialize(customDate, gen, null);
        gen.flush();
        byte[] serializedCustomDate = ((ByteArrayOutputStream)gen.getOutputStream()).toByteArray();
        assertEquals("2022-12-31T23:59:59.000Z", new String(serializedCustomDate));
        assertEquals(customFormat.getShape(), JsonFormat.Shape.STRING);
        assertEquals(customFormat.getTimeZone(), TimeZone.getTimeZone("UTC"));
        assertEquals(customFormat.getPattern(), "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        // Test serializing a LocalDateTime using custom serialization settings
        LocalDateTime localDateTime = LocalDateTime.of(2023, 1, 1, 12, 30);
        ObjectWriter localDateTimeWriter = mapper.writerWithView(new SimpleBeanPropertyView());
        JsonSerializer<LocalDateTime> localDateTimeSerializer = localDateTimeWriter.findValueSerializer(LocalDateTime.class);
        ByteArrayOutputStream localDateTimeOutput = new ByteArrayOutputStream();
        JsonGenerator localDateTimeGen = localDateTimeWriter.getFactory().createGenerator(localDateTimeOutput, null);
        localDateTimeSerializer.serialize(localDateTime, localDateTimeGen, null);
        localDateTimeGen.flush();
        byte[] serializedLocalDateTime = localDateTimeOutput.toByteArray();
        assertEquals("2023-01-01T12:30", new String(serializedLocalDateTime));
        assertEquals(localDateTimeFormat.getShape(), JsonFormat.Shape.STRING);
        assertEquals(localDateTimeFormat.getTimeZone(), TimeZone.getTimeZone("UTC"));
        assertEquals(localDateTimeFormat.getPattern(), "yyyy-MM-dd HH:mm");
    }

    @Test
    public void testIsEmpty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test that null and zero timestamp are considered empty
        assertTrue(mapper.findValueSerializer(Date.class).isEmpty(null));
        assertTrue(mapper.findValueSerializer(Date.class).isEmpty(new Date(0L)));

        // Test that non-null and non-zero timestamp are not considered empty
        Date nonEmptyDate = new Date();
        assertFalse(mapper.findValueSerializer(Date.class).isEmpty(nonEmptyDate));

        // Test that custom serialization settings do not affect empty checking
        JsonFormat customFormat = new JsonFormat.Builder()
                .shape(JsonFormat.Shape.STRING)
                .timeZone(TimeZone.getTimeZone("UTC"))
                .pattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .build();
        ObjectWriter writer = mapper.writerWithView(new SimpleBeanPropertyView());
        JsonSerializer<Date> customDateSerializer = writer.findValueSerializer(Date.class);
        assertFalse(customDateSerializer.isEmpty(null));
        assertFalse(customDateSerializer.isEmpty(new Date(0L)));
        Date nonEmptyCustomDate = new Date();
        assertFalse(customDateSerializer.isEmpty(nonEmptyCustomDate));
    }

    @Test
    public void testGetSchema() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        // Test getting schema for a Date using default settings
        JsonNode schema = mapper.findValueSerializer(Date.class).getSchema(mapper.getSerializationConfig(), Date.class);
        assertEquals("number", schema.asText());

        // Test getting schema for a Date using custom serialization settings
        JsonFormat customFormat = new JsonFormat.Builder()
                .shape(JsonFormat.Shape.STRING)
                .timeZone(TimeZone.getTimeZone("UTC"))
                .pattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .build();
        ObjectWriter writer = mapper.writerWithView(new SimpleBeanPropertyView());
        JsonSerializer<Date> customDateSerializer = writer.findValueSerializer(Date.class);
        JsonNode customSchema = customDateSerializer.getSchema(mapper.getSerializationConfig(), Date.class);
        assertEquals("string", customSchema.asText());
    }
}