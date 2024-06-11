package com.yourpackagename.test; // replace with your package name

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DateTimeSerializerBaseTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
    }

    @Test
    public void testDateTimeSerializerBaseConstructor() {
        // Test the default constructor
        DateTimeSerializerBase<Long> dateTimeSerializerBase = new DateTimeSerializerBase<Long>(Long.class, false, null);
        assertNotNull(dateTimeSerializerBase);
    }

    @Test
    public void testDateTimeSerializerBaseWithFormat() {
        // Test the constructor with format
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS zzz");
        DateTimeSerializerBase<Long> dateTimeSerializerBase = new DateTimeSerializerBase<>(Long.class, false, customFormat);
        assertNotNull(dateTimeSerializerBase);
        assertEquals(customFormat, dateTimeSerializerBase._customFormat);
    }

    @Test
    public void testIsEmpty() {
        Long date = null;
        assertTrue(DateTimeSerializerBase.isEmpty(date));
        date = 0L;
        assertTrue(DateTimeSerializerBase.isEmpty(null, date));
        assertFalse(DateTimeSerializerBase.isEmpty(mapper.valueToTree(date)));
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<Long> dateTimeSerializerBase = new DateTimeSerializerBase<>(Long.class, false, null);
        JsonNode schema = dateTimeSerializerBase.getSchema(mapper.getSerializerProvider(), Long.class);
        assertEquals("number", schema.getClass().getSimpleName());
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBase<Long> dateTimeSerializerBase = new DateTimeSerializerBase<>(Long.class, false, null);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("yyyy-MM-dd HH:mm:ss.SSS zzz");
        dateTimeSerializerBase.acceptJsonFormatVisitor(objectMapper.getSerializerProvider().getSerializerProvider(), JsonType.LONG_TYPE, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAcceptJsonFormatVisitorWithNullProvider() {
        DateTimeSerializerBase<Long> dateTimeSerializerBase = new DateTimeSerializerBase<>(Long.class, false, null);
        dateTimeSerializerBase.acceptJsonFormatVisitor(null, JsonType.LONG_TYPE, false);
    }

    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase<Long> dateTimeSerializerBase = new DateTimeSerializerBase<>(Long.class, false, null);
        Long date = System.currentTimeMillis();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new StringWriter());
        dateTimeSerializerBase.serialize(date, jsonGenerator, mapper.getSerializerProvider());
        JsonParser jsonParser = mapper.getFactory().createParser(new StringReader(jsonGenerator.getOutputStr()));
        assertEquals(date.toString(), mapper.readValue(jsonParser, Long.class));
    }

    @Test
    public void testSerializeWithCustomFormat() throws IOException {
        DateTimeSerializerBase<Long> dateTimeSerializerBase = new DateTimeSerializerBase<>(Long.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        Long date = System.currentTimeMillis();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new StringWriter());
        dateTimeSerializerBase.serialize(date, jsonGenerator, mapper.getSerializerProvider());
        JsonParser jsonParser = mapper.getFactory().createParser(new StringReader(jsonGenerator.getOutputStr()));
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        assertEquals(customFormat.format(new Date(date)), mapper.readValue(jsonParser, String.class));
    }

    @Test
    public void testSerializeWithUseTimestamp() throws IOException {
        DateTimeSerializerBase<Long> dateTimeSerializerBase = new DateTimeSerializerBase<>(Long.class, true, null);
        Long date = System.currentTimeMillis();
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new StringWriter());
        dateTimeSerializerBase.serialize(date, jsonGenerator, mapper.getSerializerProvider());
        JsonParser jsonParser = mapper.getFactory().createParser(new StringReader(jsonGenerator.getOutputStr()));
        assertEquals(String.valueOf(date), mapper.readValue(jsonParser, Long.class));
    }

    @Test
    public void testSerializeWithNullValue() throws IOException {
        DateTimeSerializerBase<Long> dateTimeSerializerBase = new DateTimeSerializerBase<>(Long.class, false, null);
        JsonGenerator jsonGenerator = mapper.getFactory().createGenerator(new StringWriter());
        dateTimeSerializerBase.serialize(null, jsonGenerator, mapper.getSerializerProvider());
        assertNull(mapper.readValue(new StringReader(jsonGenerator.getOutputStr()), Long.class));
    }
}