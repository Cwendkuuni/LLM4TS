package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedProperty;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

@SuppressWarnings("result")
public class DateTimeSerializerBaseTest {

    private ObjectMapper objectMapper;
    private DateTimeSerializerBase<Long> longDateTimeSerializer;
    private DateTimeSerializerBase<String> stringDateTimeSerializer;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        longDateTimeSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, true, null);
        stringDateTimeSerializer = new DateTimeSerializerBase<String>(String.class, false, null);
    }

    // Testing the constructor
    @Test
    public void testConstructor() {
        assertNotNull(longDateTimeSerializer);
        assertNotNull(stringDateTimeSerializer);
    }

    // Testing the abstract method withFormat
    @Test(expected = UnsupportedOperationException.class)
    public void testAbstractMethodWithFormat() {
        longDateTimeSerializer.withFormat(false, null);
    }

    // Testing the createContextual method
    @Test
    public void testCreateContextualNullProperty() throws JsonMappingException {
        assertEquals(longDateTimeSerializer, stringDateTimeSerializer.createContextual(null, null));
    }

    @Test
    public void testCreateContextualJsonFormat() throws JsonMappingException {
        AnnotatedProperty property = new AnnotatedProperty(Object.class.getDeclaredField("testField"), new JsonFormat(Shape.STRING, new TimeZone("UTC"), Locale.US));
        assertEquals(stringDateTimeSerializer, longDateTimeSerializer.createContextual(objectMapper.getSerializerProvider(), property));
    }

    @Test
    public void testCreateContextualNoFormat() throws JsonMappingException {
        assertEquals(longDateTimeSerializer, longDateTimeSerializer.createContextual(objectMapper.getSerializerProvider(), null));
    }

    // Testing the isEmpty method
    @Test
    public void testIsEmptyNullValue() {
        assertTrue(longDateTimeSerializer.isEmpty(null));
    }

    @Test
    public void testIsEmptyZeroTimestamp() {
        assertTrue(longDateTimeSerializer.isEmpty(0L));
    }

    @Test
    public void testIsEmptyNotNullValue() {
        assertFalse(longDateTimeSerializer.isEmpty(new Date()));
    }

    // Testing the getSchema method
    @Test
    public void testGetSchema() {
        JsonNode schema = longDateTimeSerializer.getSchema(objectMapper.getSerializerProvider(), Long.TYPE);
        assertEquals("number", schema.asText());
    }

    // Testing the acceptJsonFormatVisitor method
    @Test
    public void testAcceptJsonFormatVisitorAsNumber() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        longDateTimeSerializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals("number", visitor.getFormat().getShapeName());
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitorAsStringNullFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        stringDateTimeSerializer.acceptJsonFormatVisitor(visitor, null);
    }

    @Test
    public void testAcceptJsonFormatVisitorAsString() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, JsonValueFormat.DATE_TIME);
        stringDateTimeSerializer.acceptJsonFormatVisitor(visitor, null);
        assertEquals("string", visitor.getFormat().getShapeName());
    }

    // Testing the serialize method with timestamp
    @Test
    public void testSerializeTimestamp() throws JsonProcessingException, IOException {
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        longDateTimeSerializer.serialize(new Date(0), gen, objectMapper.getSerializerProvider());
        String expected = "0";
        assertEquals(expected, gen.getOutputBuffer().toString());
    }

    // Testing the serialize method with custom format
    @Test
    public void testSerializeCustomFormat() throws JsonProcessingException, IOException {
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        longDateTimeSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, false, customFormat);
        longDateTimeSerializer.serialize(new Date(), gen, objectMapper.getSerializerProvider());
        String expected = customFormat.format(new Date()) + "Z";
        assertEquals(expected, gen.getOutputBuffer().toString());
    }

    // Testing the serialize method with empty value
    @Test
    public void testSerializeEmptyValue() throws JsonProcessingException, IOException {
        JsonGenerator gen = objectMapper.getFactory().createGenerator(System.out);
        longDateTimeSerializer.serialize(null, gen, objectMapper.getSerializerProvider());
        String expected = "null";
        assertEquals(expected, gen.getOutputBuffer().toString());
    }

    // Testing the _asTimestamp method
    @Test
    public void testAsTimestampNullProvider() {
        assertFalse(longDateTimeSerializer._asTimestamp(null));
    }

    @Test
    public void testAsTimestampTrue() {
        assertTrue(longDateTimeSerializer._asTimestamp(objectMapper.getSerializerProvider()));
    }

    @Test
    public void testAsTimestampFalse() {
        assertFalse(stringDateTimeSerializer._asTimestamp(objectMapper.getSerializerProvider()));
    }

    // Testing the _acceptJsonFormatVisitor method
    @Test
    public void testAcceptJsonFormatVisitorNullProvider() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        longDateTimeSerializer._acceptJsonFormatVisitor(visitor, null, true);
        assertNotNull(visitor.getFormat());
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitorNullTypeHint() throws JsonMappingException {
        longDateTimeSerializer._acceptJsonFormatVisitor(null, null, true);
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitorAsNumberNullProvider() throws JsonMappingException {
        longDateTimeSerializer._acceptJsonFormatVisitor(null, null, false);
    }

    @Test
    public void testAcceptJsonFormatVisitorAsNumber() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        longDateTimeSerializer._acceptJsonFormatVisitor(visitor, Long.TYPE, true);
        assertEquals("number", visitor.getFormat().getShapeName());
    }

    @Test
    public void testAcceptJsonFormatVisitorAsString() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper(null, null, null);
        stringDateTimeSerializer._acceptJsonFormatVisitor(visitor, String.class, false);
        assertEquals("string", visitor.getFormat().getShapeName());
    }
}