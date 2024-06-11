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
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonSerializer.Typing;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonFormat.Shape;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.std.StdDateFormat;
import com.fasterxml.jackson.databind.util.StdDateFormat.DateFormatType;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBase() {
        // Test default constructor
        DateTimeSerializerBase<?> serializer = new DateTimeSerializerBase<Object>(Object.class, false, null);
        assertEquals(Object.class, serializer.getType());
        assertFalse(serializer.useTimestamp());
        assertNull(serializer.customFormat());

        // Test constructor with useTimestamp=true, customFormat=null
        DateTimeSerializerBase<Long> timestampSerializer = new DateTimeSerializerBase<Long>(Long.class, true, null);
        assertEquals(Long.class, timestampSerializer.getType());
        assertTrue(timestampSerializer.useTimestamp());
        assertNull(timestampSerializer.customFormat());

        // Test constructor with useTimestamp=false, customFormat=SimpleDateFormat
        DateTimeSerializerBase<java.util.Date> dateSerializer = new DateTimeSerializerBase<java.util.Date>(java.util.Date.class, false, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        assertEquals(java.util.Date.class, dateSerializer.getType());
        assertFalse(dateSerializer.useTimestamp());
        assertSame(dateSerializer.customFormat(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormat<Object> jsonFormat = new JsonFormat();

        // Test null property
        JsonSerializer<Object> serializer = DateTimeSerializerBase.class.newInstance().createContextual(null, null);
        assertSame(DateTimeSerializerBase.class, serializer.getClass());

        // Test non-DateTime property
        BeanProperty property = new BeanProperty("nonDateTimeProperty", java.lang.String.class, null, null);
        serializer = DateTimeSerializerBase.class.newInstance().createContextual(mapper.getSerializerProvider(), property);
        assertSame(DateTimeSerializerBase.class, serializer.getClass());

        // Test serialization as numeric timestamp
        property = new BeanProperty("numericTimestampProperty", Long.class, null, null);
        jsonFormat.setShape(JsonFormat.Shape.NUMBER);
        Annotated annotated = new Annotated(property, mapper.getAnnotationIntrospector().findAnnotation(property, JsonFormat.class));
        serializer = DateTimeSerializerBase.class.newInstance().createContextual(mapper.getSerializerProvider(), property);
        assertTrue(serializer instanceof DateTimeSerializerBase);
        assertTrue(((DateTimeSerializerBase<?>) serializer).useTimestamp());
        assertNull(((DateTimeSerializerBase<?>) serializer).customFormat());

        // Test serialization as string
        property = new BeanProperty("stringDateProperty", java.util.Date.class, null, null);
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        jsonFormat.setTimeZone(TimeZone.UTC);
        jsonFormat.setPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        annotated = new Annotated(property, mapper.getAnnotationIntrospector().findAnnotation(property, JsonFormat.class));
        serializer = DateTimeSerializerBase.class.newInstance().createContextual(mapper.getSerializerProvider(), property);
        assertTrue(serializer instanceof DateTimeSerializerBase);
        assertFalse(((DateTimeSerializerBase<?>) serializer).useTimestamp());
        assertEquals(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"), ((DateTimeSerializerBase<?>) serializer).customFormat());
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Test serialization as numeric timestamp
        long timestamp = 1516041620000L;
        DateTimeSerializerBase<Long> timestampSerializer = new DateTimeSerializerBase<Long>(Long.class, true, null);
        JsonGenerator generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        timestampSerializer.serialize(timestamp, generator, mapper.getSerializerProvider());
        String json = generator.getOutputBuffer().toString();
        assertEquals("1516041620000", json);

        // Test serialization as string
        Date date = new Date(1516041620000L);
        DateTimeSerializerBase<Date> dateSerializer = new DateTimeSerializerBase<Date>(Date.class, false, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
        generator = mapper.getFactory().createGenerator(new ByteArrayOutputStream(), null);
        dateSerializer.serialize(date, generator, mapper.getSerializerProvider());
        json = generator.getOutputBuffer().toString();
        assertEquals("2018-01-22T13:10:20.000Z", json);
    }

    @Test
    public void testIsEmpty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<java.util.Date> dateSerializer = new DateTimeSerializerBase<java.util.Date>(java.util.Date.class, false, null);

        // Test null value
        assertTrue(dateSerializer.isEmpty(null));

        // Test empty date
        Date emptyDate = new Date(0);
        assertTrue(dateSerializer.isEmpty(mapper.getSerializerProvider(), emptyDate));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<java.util.Date> dateSerializer = new DateTimeSerializerBase<java.util.Date>(java.util.Date.class, false, null);

        JsonNode schema = dateSerializer.getSchema(mapper.getSerializerProvider(), Date.class);
        assertEquals("string", schema.getNodeType().name());
        assertEquals("date-time", schema.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializers = mapper.getSerializerProvider();

        DateTimeSerializerBase<java.util.Date> dateSerializer = new DateTimeSerializerBase<java.util.Date>(java.util.Date.class, false, null);
        JavaType javaType = mapper.getTypeFactory().constructType(Object.class);

        JsonFormatVisitorWrapper visitor = mapper.getSerializerProvider().getJsonSerializerProvider().getConfig().findVisitor(JsonFormatVisitorWrapper.class, jsonFormat -> jsonFormat.getShape() == Shape.STRING);
        dateSerializer.acceptJsonFormatVisitor(visitor, javaType);
        assertEquals(StdDateFormat.DateFormatType.DATE_TIME, visitor.getFormatType());
        assertEquals("date-time", visitor.getFormatFeature().getName());
    }

    @Test
    public void testWithFormat() {
        Boolean useTimestamp = true;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeSerializerBase<java.util.Date> serializer = new DateTimeSerializerBase<java.util.Date>(java.util.Date.class, useTimestamp, customFormat);

        DateTimeSerializerBase<java.util.Date> timestampSerializer = serializer.withFormat(useTimestamp, null);
        assertSame(serializer, timestampSerializer);

        DateTimeSerializerBase<java.util.Date> dateSerializer = serializer.withFormat(useTimestamp, customFormat);
        assertEquals(customFormat, dateSerializer.customFormat());
    }
}