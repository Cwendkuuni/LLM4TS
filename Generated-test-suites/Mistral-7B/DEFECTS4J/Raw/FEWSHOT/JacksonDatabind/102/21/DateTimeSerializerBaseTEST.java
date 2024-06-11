package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
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
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest<T> {

    @SuppressWarnings("unchecked")
    private DateTimeSerializerBase<T> createSerializer(Class<T> type, Boolean useTimestamp, DateFormat customFormat) {
        return new DateTimeSerializerBase<T>() {
            @Override
            protected DateTimeSerializerBase<T> withFormat(Boolean timestamp, DateFormat customFormat) {
                return this;
            }
        }.withType(type).withFormat(useTimestamp, customFormat);
    }

    @Test
    public void testSerializerBaseCreation() {
        DateTimeSerializerBase<Date> dateSerializer = createSerializer(Date.class, false, null);
        assertNotNull(dateSerializer);
    }

    @Test
    public void testSerializerBaseUseTimestamp() {
        DateTimeSerializerBase<Date> dateSerializer = createSerializer(Date.class, true, null);
        assertNotNull(dateSerializer);
    }

    @Test
    public void testSerializerBaseWithCustomFormat() {
        DateTimeSerializerBase<Date> dateSerializer = createSerializer(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        assertNotNull(dateSerializer);
    }

    @Test
    public void testCreateContextualWithNullProperty() {
        DateTimeSerializerBase<Date> dateSerializer = createSerializer(Date.class, false, null);
        assertNotNull(dateSerializer.createContextual(null, null));
        assertEquals(dateSerializer, dateSerializer.createContextual(null, null));
    }

    @Test
    public void testCreateContextualWithValidProperties() throws JsonMappingException {
        DateTimeSerializerBase<Date> dateSerializer = createSerializer(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();

        JsonFormat.Value format = JsonFormat.value()
                .shape(JsonFormat.Shape.STRING)
                .timeZone(TimeZone.getTimeZone("UTC"))
                .locale(Locale.US)
                .pattern("yyyy-MM-dd HH:mm:ss");

        JsonSerializer<Date> jsonSerializer = dateSerializer.createContextual(mapper.getSerializerProvider(), mapper.findProperty("testProperty"));

        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        expectedFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        expectedFormat.setLocale(Locale.US);

        assertEquals(expectedFormat.getClass(), jsonSerializer.getFormatValue().getFormat().getClass());
        assertEquals(expectedFormat, jsonSerializer.getFormatValue().getFormat());
    }

    @Test
    public void testSerializeAsString() throws IOException {
        DateTimeSerializerBase<Date> dateSerializer = createSerializer(Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);
        Date date = new Date();

        dateSerializer.serialize(date, gen, mapper.getSerializerProvider());
        gen.flush();

        String expected = "2023-03-22 15:33:51";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        assertEquals(expected, formatter.format(date));
    }

    @Test
    public void testSerializeAsNumber() throws IOException {
        DateTimeSerializerBase<Date> dateSerializer = createSerializer(Date.class, true, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out, null);
        Date date = new Date();

        dateSerializer.serialize(date, gen, mapper.getSerializerProvider());
        long expected = date.getTime();
        assertEquals(expected, gen.getOutputValueAsLong());
    }

    @Test
    public void testIsEmpty() throws IOException {
        DateTimeSerializerBase<Date> dateSerializer = createSerializer(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();

        assertFalse(dateSerializer.isEmpty(mapper.getSerializerProvider(), date));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Date> dateSerializer = createSerializer(Date.class, false, null);

        SchemaNode schema = dateSerializer.getSchema(null, null);
        assertEquals("string", schema.getType());
    }
}