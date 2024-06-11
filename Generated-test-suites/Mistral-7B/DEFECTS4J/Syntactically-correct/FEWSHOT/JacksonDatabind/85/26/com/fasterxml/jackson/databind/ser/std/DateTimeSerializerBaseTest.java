package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializers;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

@SuppressWarnings("serial")
public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase() {
        // Test default constructor
        DateTimeSerializerBase<Long> longSerializer = new DateTimeSerializerBase<Long>(Long.TYPE, null, null);
        assertNull(longSerializer._useTimestamp);
        assertNull(longSerializer._customFormat);

        DateTimeSerializerBase<String> stringSerializer = new DateTimeSerializerBase<String>(String.class, null, null);
        assertNull(stringSerializer._useTimestamp);
        assertNull(stringSerializer._customFormat);

        // Test constructor with useTimestamp and null customFormat
        DateTimeSerializerBase<java.util.Date> dateSerializer1 = new DateTimeSerializerBase<java.util.Date>(java.util.Date.class, true, null);
        assertTrue(dateSerializer1._useTimestamp.booleanValue());
        assertNull(dateSerializer1._customFormat);

        // Test constructor with null useTimestamp and customFormat
        DateTimeSerializerBase<java.sql.Date> dateSerializer2 = new DateTimeSerializerBase<java.sql.Date>(java.sql.Date.class, null, new SimpleDateFormat("dd-MM-yyyy"));
        assertNull(dateSerializer2._useTimestamp);
        assertNotNull(dateSerializer2._customFormat);
        assertEquals("dd-MM-yyyy", dateSerializer2._customFormat.toPattern());
    }

    @Test
    public void testWithFormat() throws JsonMappingException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = new DateTimeSerializerBase<java.util.Date>(java.util.Date.class, null, null);

        DateTimeSerializerBase<java.util.Date> timestampSerializer = dateSerializer.withFormat(Boolean.TRUE, null);
        assertNotNull(timestampSerializer._useTimestamp);
        assertNull(timestampSerializer._customFormat);

        DateTimeSerializerBase<java.util.Date> customFormatSerializer1 = dateSerializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy"));
        assertNull(customFormatSerializer1._useTimestamp);
        assertNotNull(customFormatSerializer1._customFormat);
        assertEquals("dd-MM-yyyy", customFormatSerializer1._customFormat.toPattern());

        DateTimeSerializerBase<java.util.Date> customFormatSerializer2 = dateSerializer.withFormat(Boolean.FALSE, new StdDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
        assertNull(customFormatSerializer2._useTimestamp);
        assertNotNull(customFormatSerializer2._customFormat);
        assertEquals("yyyy-MM-dd'T'HH:mm:ssZ", customFormatSerializer2._customFormat.toPattern());
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = new DateTimeSerializerBase<java.util.Date>(java.util.Date.class, null, null);

        JsonSerializer<Object> serializer1 = dateSerializer.createContextual(null, null);
        assertSame(dateSerializer, serializer1);

        JsonFormat.Value format1 = new JsonFormat.Value();
        format1.setShape(JsonFormat.Shape.STRING);
        JsonSerializer<Object> serializer2 = dateSerializer.createContextual(null, null, format1);
        assertNotSame(dateSerializer, serializer2);
        assertTrue(serializer2 instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<java.util.Date> castedSerializer2 = (DateTimeSerializerBase<java.util.Date>) serializer2;
        assertNotNull(castedSerializer2._customFormat);
        assertEquals("", castedSerializer2._customFormat.toPattern());

        JsonFormat.Value format2 = new JsonFormat.Value();
        format2.setShape(JsonFormat.Shape.STRING);
        format2.setPattern("dd-MM-yyyy");
        JsonSerializer<Object> serializer3 = dateSerializer.createContextual(null, null, format2);
        assertNotSame(dateSerializer, serializer3);
        assertTrue(serializer3 instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<java.util.Date> castedSerializer3 = (DateTimeSerializerBase<java.util.Date>) serializer3;
        assertNotNull(castedSerializer3._customFormat);
        assertEquals("dd-MM-yyyy", castedSerializer3._customFormat.toPattern());
    }

    @Test
    public void testSerialize() throws JsonGenerationException, IOException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = new DateTimeSerializerBase<java.util.Date>(java.util.Date.class, null, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonGenerator gen = mapper.getFactory().createGenerator(System.out);
        gen.setDateFormat(new SimpleDateFormat("dd-MM-yyyy"));

        // Test serialization as timestamp
        java.util.Date date = new java.util.Date();
        dateSerializer.serialize(date, gen, null);
        assertEquals(date.getTime(), gen.getOutputSize(), 0L);

        // Test serialization as string with default format
        dateSerializer.serialize(date, gen, null);
        String output = gen.getOutputBuffer().toString();
        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        String expected = expectedFormat.format(date);
        assertEquals(expected, output);

        // Test serialization as string with custom format
        DateTimeSerializerBase<java.util.Date> customFormatSerializer = dateSerializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy"));
        customFormatSerializer.serialize(date, gen, null);
        output = gen.getOutputBuffer().toString();
        assertEquals("dd-MM-yyyy", gen.getDateFormat().toPattern());
        assertEquals("dd-MM-yyyy", output);
    }

    @Test
    public void testDeserialize() throws JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(new StringReader("2023-03-31T12:34:56.789Z"));

        DateTimeSerializerBase<java.util.Date> dateSerializer = new DateTimeSerializerBase<java.util.Date>(java.util.Date.class, null, null);

        // Test deserialization as timestamp
        JsonDeserializer<Object> deserializer1 = dateSerializer.createContextual(null, null);
        java.util.Date deserialized = deserializer1.deserialize(parser, JsonTypeFactory.valueType(Object.class));
        assertEquals(2023 - 1900, deserialized.getYear());
        assertEquals(3, deserialized.getMonth());
        assertEquals(31, deserialized.getDate());
        assertEquals(12, deserialized.getHours());
        assertEquals(34, deserialized.getMinutes());
        assertEquals(56, deserialized.getSeconds());
        assertEquals(789, deserialized.getMilliseconds());

        // Test deserialization as string with default format
        JsonDeserializer<Object> deserializer2 = dateSerializer.createContextual(null, null, null);
        deserialized = deserializer2.deserialize(parser, JsonTypeFactory.valueType(Object.class));
        SimpleDateFormat expectedFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
        String expected = expectedFormat.format(deserialized);
        assertEquals(expected, parser.getValueAsString());

        // Test deserialization as string with custom format
        DateTimeSerializerBase<java.util.Date> customFormatSerializer = dateSerializer.withFormat(Boolean.FALSE, new SimpleDateFormat("dd-MM-yyyy"));
        JsonDeserializer<Object> deserializer3 = customFormatSerializer.createContextual(null, null, null);
        deserialized = deserializer3.deserialize(parser, JsonTypeFactory.valueType(Object.class));
        assertEquals("2023-03-31", parser.getText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase<java.util.Date> dateSerializer = new DateTimeSerializerBase<java.util.Date>(java.util.Date.class, null, null);

        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> serializer = mapper.findValueSerializer(JsonSerializers.findSerializerByAnnotation(Long.class, JsonFormat.Date.class));
        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getProviderInstance(JsonFormatVisitorWrapper.class);

        dateSerializer.acceptJsonFormatVisitor(visitor, JavaType.valueOf(Object.class), true);
        assertEquals("number", visitor.getFormat().getShapeKey());
        assertEquals("number", visitor.getFormat().getShape().asString());
        assertEquals(JsonParser.NumberType.LONG, visitor.getFormat().getNumberType());
        assertEquals(JsonValueFormat.UTC_MILLISEC, visitor.getFormat().getValueFormat());

        dateSerializer.acceptJsonFormatVisitor(visitor, JavaType.valueOf(Object.class), false);
        assertEquals("string", visitor.getFormat().getShapeKey());
        assertEquals("string", visitor.getFormat().getShape().asString());
        assertEquals(JsonValueFormat.DATE_TIME, visitor.getFormat().getValueFormat());
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase<java.util.Date> dateSerializer = new DateTimeSerializerBase<java.util.Date>(java.util.Date.class, null, null);

        JsonNode schema = dateSerializer.getSchema(null, null);
        assertEquals("number", schema.path("type").asText());
        assertEquals("true", schema.path("items/containsNull").asText());
    }
}