package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.jmock.lib.legacy.AnnotationUtils;
import org.jmock.lib.legacy.MockObjectTestCase;
import org.jmock.lib.legacy.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JsonFormatIntrospector;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.TestUtil;

public class DateTimeSerializerBaseTest extends MockObjectTestCase {

    @Test
    public void testDefaultConstructor() {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
        assertNull(serializer._customFormat);
        assertTrue(serializer._useTimestamp);
    }

    @Test
    public void testWithFormatConstructor() {
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, customFormat);
        assertEquals(customFormat, serializer._customFormat);
        assertFalse(serializer._useTimestamp);
    }

    @Test
    public void testWithFormatMethod() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatIntrospector introspector = mapper.getSerializationConfig().getIntrospector();
        Annotated property = introspector.findProperty("testDate", Long.class, null);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setLocale(Locale.US);
        property.setValue(format);
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
        DateTimeSerializerBase<Long> newSerializer = serializer.withFormat(false, format);
        assertEquals(format, AnnotationUtils.getValue(newSerializer, JsonFormat.class));
    }

    @Test
    public void testCreateContextual() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatIntrospector introspector = mapper.getSerializationConfig().getIntrospector();
        Annotated property = introspector.findProperty("testDate", Long.class, null);
        JsonFormat.Value format = new JsonFormat.Value();
        format.setShape(JsonFormat.Shape.STRING);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        format.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setLocale(Locale.US);
        property.setValue(format);

        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
        JsonSerializer<?> result = serializer.createContextual(mapper.getSerializationConfig(), property);
        JsonFormat.Value jsonFormat = result.getValue(JsonFormat.class);
        assertEquals("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", jsonFormat.getPattern());
        assertEquals(Locale.US, jsonFormat.getLocale());
        assertEquals(TimeZone.getTimeZone("UTC"), jsonFormat.getTimeZone());
        assertEquals(JsonFormat.Shape.STRING, jsonFormat.getShape());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextualNullProvider() throws Exception {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
        serializer.createContextual(null, null);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextualNullProperty() throws Exception {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
        serializer.createContextual(new SerializerProvider(), null);
    }

    @Test
    public void testSerialize() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        Long testDate = 1482328123000L;
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
        if (serializer._useTimestamp) {
            writer.writeValueAsNumber(new JsonStreamWriter(new ByteArrayOutputStream()), testDate);
            String result = new String(writer.getOutputBuffer().toByteArray(), 0, writer.getOutputBuffer().size());
            assertEquals("1482328123000", result);
        } else {
            JsonSerializer<Long> jsonSerializer = mapper.findValueSerializer(Long.class, new SerializerProvider());
            JsonGenerator gen = mapper.getSerializationConfig().getFactory().createGenerator(new ByteArrayOutputStream(), new JsonFactory());
            serializer.serialize(testDate, gen, new SerializerProvider());
            gen.flush();
            String result = new String(new ByteArrayInputStream(gen.getOutputBuffer().toByteArray()).readAllBytes(), 0, gen.getOutputBuffer().size());
            assertEquals("\"2017-01-01T00:02:03.456Z\"", result);
        }
    }

    @Test
    public void testEmpty() {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
        assertTrue(serializer.isEmpty(null));
        assertTrue(serializer.isEmpty(new SerializerProvider(), null));
    }

    @Test
    public void testGetSchema() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
        JsonNode schema = serializer.getSchema(mapper.getSerializationConfig(), Long.class);
        assertEquals("string", schema.getClass().getSimpleName());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Long> jsonSerializer = mapper.findValueSerializer(Long.class, new SerializerProvider());
        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().getFactory().createFormatVisitor(Long.class, null);
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
        serializer.acceptJsonFormatVisitor(visitor, Long.class);
        assertEquals("number", visitor.getJsonFormatVisitor().getFormat().getShape().asString());
    }

    @Test
    public void testAcceptJsonFormatVisitorAsString() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Long> jsonSerializer = mapper.findValueSerializer(Long.class, new SerializerProvider());
        JsonFormatVisitorWrapper visitor = mapper.getSerializationConfig().getFactory().createFormatVisitor(String.class, null);
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
        serializer.acceptJsonFormatVisitor(visitor, Long.class);
        assertEquals("string", visitor.getJsonFormatVisitor().getFormat().getShape().asString());
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getJsonFormatVisitor().getFormat().getValueClass());
    }
}