package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Test
    public void testConstructor() {
        // Test default constructor
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        assertNotNull(dateSerializerBase);

        // Test constructor with useTimestamp and customFormat
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
        DateTimeSerializerBase<Date> customFormatSerializerBase = new DateTimeSerializerBase<Date>(Date.class, true, customFormat);
        assertNotNull(customFormatSerializerBase);
        assertEquals(true, customFormatSerializerBase._useTimestamp);
        assertEquals(customFormat, customFormatSerializerBase._customFormat);
    }

    @Test
    public void testWithFormat() {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);

        // Test with timestamp = false and customFormat = null
        DateTimeSerializerBase<Date> timestampFalseCustomFormatNull = dateSerializerBase.withFormat(false, null);
        assertEquals(dateSerializerBase, timestampFalseCustomFormatNull);

        // Test with timestamp = true and customFormat = null
        DateTimeSerializerBase<Date> timestampTrueCustomFormatNull = dateSerializerBase.withFormat(true, null);
        assertNotSame(dateSerializerBase, timestampTrueCustomFormatNull);
        assertTrue(timestampTrueCustomFormatNull._useTimestamp);
        assertNull(timestampTrueCustomFormatNull._customFormat);

        // Test with timestamp = false and customFormat
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
        DateTimeSerializerBase<Date> timestampFalseCustomFormat = dateSerializerBase.withFormat(false, customFormat);
        assertNotSame(dateSerializerBase, timestampFalseCustomFormat);
        assertFalse(timestampFalseCustomFormat._useTimestamp);
        assertEquals(customFormat, timestampFalseCustomFormat._customFormat);

        // Test with timestamp = true and customFormat
        DateTimeSerializerBase<Date> timestampTrueCustomFormat = dateSerializerBase.withFormat(true, customFormat);
        assertNotSame(dateSerializerBase, timestampTrueCustomFormat);
        assertTrue(timestampTrueCustomFormat._useTimestamp);
        assertNull(timestampTrueCustomFormat._customFormat);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);

        // Test with null property
        JsonSerializer<?> serializer = dateSerializerBase.createContextual(null, null);
        assertEquals(dateSerializerBase, serializer);

        // Test with non-null property
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setLocale(Locale.ENGLISH);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("dd-MM-yyyy HH:mm:ss z");
        JsonSerializer<?> serializerWithFormat = dateSerializerBase.createContextual(mapper.getSerializerProvider(), new StdFieldProperty("testProperty"));

        // Test if serializer instance is the same as the original one
        assertNotSame(dateSerializerBase, serializerWithFormat);

        // Test if serializer instance has the custom format
        assertEquals(customFormat, serializerWithFormat.getFormat());
    }

    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);

        // Test serialization with default format
        ObjectMapper mapper = new ObjectMapper();
        Date date = new Date();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        dateSerializerBase.serialize(date, gen, mapper.getSerializerProvider());
        gen.flush();
        String result = new String(gen.getOutputBuffer().toByteArray());
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date parsedDate = defaultFormat.parse(result);
        assertEquals(date, parsedDate);

        // Test serialization with custom format
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
        DateTimeSerializerBase<Date> customFormatSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator gen2 = mapper.getFactory().createGenerator(outputStream, null);
        customFormatSerializerBase.serialize(date, gen2, mapper.getSerializerProvider());
        gen2.flush();
        String result2 = new String(outputStream.toByteArray());
        assertEquals(customFormat.format(date), result2);
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);

        // Test acceptJsonFormatVisitor with default format
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getJsonFormatVisitor(mapper.getSerializerProvider().getDefaultTyping());
        dateSerializerBase.acceptJsonFormatVisitor(visitor, null);
        assertEquals("number", visitor.getFormatType());
        assertEquals(JsonParser.NumberType.LONG, visitor.getNumberType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getValueFormat());

        // Test acceptJsonFormatVisitor with custom format
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
        DateTimeSerializerBase<Date> customFormatSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
        visitor = mapper.getSerializerProvider().getJsonFormatVisitor(mapper.getSerializerProvider().getDefaultTyping());
        customFormatSerializerBase.acceptJsonFormatVisitor(visitor, null);
        assertEquals("string", visitor.getFormatType());
        assertEquals(JsonParser.NumberType.TEXT, visitor.getNumberType());
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getValueFormat());
    }

    @Test
    public void testSerializeAsString() throws IOException {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);

        // Test serializeAsString with default format
        Date date = new Date();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator gen = mapper.getFactory().createGenerator(outputStream, null);
        dateSerializerBase._serializeAsString(date, gen, mapper.getSerializerProvider());
        gen.flush();
        String result = new String(outputStream.toByteArray());
        SimpleDateFormat defaultFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date parsedDate = defaultFormat.parse(result);
        assertEquals(date, parsedDate);

        // Test serializeAsString with custom format
        DateFormat customFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
        DateTimeSerializerBase<Date> customFormatSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, customFormat);
        ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
        JsonGenerator gen2 = mapper.getFactory().createGenerator(outputStream2, null);
        customFormatSerializerBase._serializeAsString(date, gen2, mapper.getSerializerProvider());
        gen2.flush();
        String result2 = new String(outputStream2.toByteArray());
        assertEquals(customFormat.format(date), result2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullSerializerProvider() {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        dateSerializerBase.createContextual(null, null);
    }

    @Test
    public void testSerializeNullValue() throws IOException {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        dateSerializerBase.serialize(null, gen, mapper.getSerializerProvider());
        gen.flush();
        String result = new String(gen.getOutputBuffer().toByteArray());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testParseEmptyString() throws IOException, JsonParseException {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("", null);
        Date date = dateSerializerBase.deserialize(parser, Date.class, null);
        assertNull(date);
    }

    @Test(expected = JsonMappingException.class)
    public void testParseInvalidStringFormat() throws IOException, JsonParseException {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("invalidStringFormat", null);
        Date date = dateSerializerBase.deserialize(parser, Date.class, null);
        assertNull(date);
    }

    @Test
    public void testParseValidStringFormat() throws IOException, JsonParseException {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
        JsonParser parser = mapper.getFactory().createParser("25-12-2022 12:34:56 PST", null);
        Date date = dateSerializerBase.deserialize(parser, Date.class, null);
        assertEquals(format.parse("25-12-2022 12:34:56 PST"), date);
    }

    @Test
    public void testParseValidStringFormatWithLocale() throws IOException, JsonParseException {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.US);
        JsonParser parser = mapper.getFactory().createParser("25-12-2022 12:34:56 PST", null);
        Date date = dateSerializerBase.deserialize(parser, Date.class, null);
        assertEquals(format.parse("25-12-2022 12:34:56 PST"), date);
    }

    @Test(expected = JsonMappingException.class)
    public void testParseValidStringFormatWithInvalidTimeZone() throws IOException, JsonParseException {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
        JsonParser parser = mapper.getFactory().createParser("25-12-2022 12:34:56 XYZ", null);
        Date date = dateSerializerBase.deserialize(parser, Date.class, null);
        assertNull(date);
    }

    @Test
    public void testParseValidStringFormatWithTimeZone() throws IOException, JsonParseException {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.ENGLISH);
        JsonParser parser = mapper.getFactory().createParser("25-12-2022 12:34:56 PST", null);
        Date date = dateSerializerBase.deserialize(parser, Date.class, null);
        assertEquals(format.parse("25-12-2022 12:34:56 PST"), date);
    }

    @Test
    public void testParseValidStringFormatWithLocaleAndTimeZone() throws IOException, JsonParseException {
        DateTimeSerializerBase<Date> dateSerializerBase = new DateTimeSerializerBase<Date>(Date.class, false, null);
        ObjectMapper mapper = new ObjectMapper();
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z", Locale.US);
        JsonParser parser = mapper.getFactory().createParser("25-12-2022 12:34:56 PST", null);
        Date date = dateSerializerBase.deserialize(parser, Date.class, null);
        assertEquals(format.parse("25-12-2022 12:34:56 PST"), date);
    }
}