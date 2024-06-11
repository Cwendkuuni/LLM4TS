package com.example.junit.test.serializer;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdDateFormat;

public class DateTimeSerializerBaseTest {

    @Test
    public void testDateTimeSerializerBaseConstruction() {
        Class<Integer> intType = Integer.class;
        DateTimeSerializerBase<Integer> serializer = new DateTimeSerializerBase<>(intType, false, null);
        assertNotNull(serializer);
    }

    @Test
    public void testWithFormat_Boolean_Null() {
        DateTimeSerializerBase<Integer> serializer = new DateTimeSerializerBase<>(Integer.class, false, null);
        DateTimeSerializerBase<Integer> newSerializer = serializer.withFormat(Boolean.TRUE, null);
        assertEquals(serializer.getClass(), serializer.getClass());
        assertNotSame(serializer, newSerializer);
    }

    @Test
    public void testWithFormat_Boolean_SimpleDateFormat() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, null);
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        DateTimeSerializerBase<Date> newSerializer = serializer.withFormat(Boolean.FALSE, customFormat);
        assertEquals(serializer.getClass(), serializer.getClass());
        assertNotSame(serializer, newSerializer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextual_NullSerializerProvider() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, null);
        serializer.createContextual(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateContextual_NullProperty() {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, null);
        serializer.createContextual(new SerializerProvider(), null);
    }

    @Test
    public void testIsEmpty_Null() {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>(Object.class, false, null);
        assertTrue(serializer.isEmpty(null));
    }

    @Test
    public void testIsEmpty_Zero() {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(Long.class, false, null);
        assertTrue(serializer.isEmpty(0L));
    }

    @Test
    public void testGetSchema_Number() {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(Long.class, false, null);
        JsonNode schema = serializer.getSchema(new SerializerProvider(), Long.class);
        assertEquals(JsonNodeType.NUMBER, schema.getNodeType());
    }

    @Test
    public void testGetSchema_String() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false, null);
        JsonNode schema = serializer.getSchema(new SerializerProvider(), String.class);
        assertEquals(JsonNodeType.STRING, schema.getNodeType());
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitor_NullVisitor() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false, null);
        serializer.acceptJsonFormatVisitor(null, String.class);
    }

    @Test
    public void testAcceptJsonFormatVisitor_SerializerProvider_AsNumber() {
        DateTimeSerializerBase<Integer> serializer = new DateTimeSerializerBase<>(Integer.class, false, null);
        JsonFormat.Shape shape = new JsonFormat.Shape();
        shape.setShape(JsonFormat.Shape.INT);
        SerializerProvider serializerProvider = new SerializerProvider();
        serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(serializerProvider), Integer.class, shape);
    }

    @Test
    public void testAcceptJsonFormatVisitor_SerializerProvider_AsString() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false, null);
        JsonFormat.Shape shape = new JsonFormat.Shape();
        shape.setShape(JsonFormat.Shape.STRING);
        SerializerProvider serializerProvider = new SerializerProvider();
        serializer.acceptJsonFormatVisitor(new JsonFormatVisitorWrapper(serializerProvider), String.class, shape);
    }

    @Test
    public void testSerialize_NullValue() throws IOException {
        DateTimeSerializerBase<Object> serializer = new DateTimeSerializerBase<>(Object.class, false, null);
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        serializer.serialize(null, gen, new SerializerProvider());
        String result = gen.getOutputText();
        assertEquals("null", result);
    }

    @Test
    public void testSerialize_Timestamp() throws IOException {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<>(Long.class, false, null);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        Long value = calendar.getTimeInMillis();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        serializer.serialize(value, gen, new SerializerProvider());
        String result = gen.getOutputText();
        String expected = String.valueOf(value);
        assertEquals(expected, result);
    }

    @Test
    public void testSerialize_String() throws IOException {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false, null);
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = customFormat.parse("2022-12-31 23:59:59");
        Long timestamp = date.getTime();
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        serializer.serialize(String.valueOf(timestamp), gen, new SerializerProvider());
        String result = gen.getOutputText();
        String expected = customFormat.format(date);
        assertEquals(expected, result);
    }

    @Test
    public void testSerialize_CustomFormat() throws IOException {
        DateTimeSerializerBase<Date> serializer = new DateTimeSerializerBase<>(Date.class, false, null);
        SimpleDateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date date = customFormat.parse("2022-12-31 23:59:59");
        JsonGenerator gen = new JsonFactory().createGenerator(new StringWriter());
        serializer.serialize(date, gen, new SerializerProvider());
        String result = gen.getOutputText();
        String expected = customFormat.format(date);
        assertEquals(expected, result);
    }

    @Test
    public void testCreateContextual_AnnotatedClass() throws JsonMappingException {
        Object obj = new Object();
        Class<Object> handledType = obj.getClass();
        JsonFormat.Shape shape = new JsonFormat.Shape();
        shape.setShape(JsonFormat.Shape.STRING);
        AnnotatedClass annotatedClass = new AnnotatedClass(handledType);
        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.setShape(shape);
        AnnotatedMember member = new AnnotatedMember();
        BeanPropertyDefinition propertyDefinition = new BeanPropertyDefinition();
        jsonFormat.setPropertyName("testProperty");
        propertyDefinition.setName("testProperty");
        member.setAnnotation(jsonFormat);
        propertyDefinition.setValue(member);
        SerializerProvider serializerProvider = new SerializerProvider();
        JsonSerializer<Object> serializer = serializerProvider.findValueSerializer(handledType, serializerProvider.getDefaultSerializerProvider());
        JsonSerializer<Object> contextualSerializer = serializer.accept(serializerProvider, new AnnotatedClass(handledType));
        assertEquals(DateTimeSerializerBase.class, contextualSerializer.getClass());
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextual_AnnotatedClass_NullShape() throws JsonMappingException {
        Object obj = new Object();
        Class<Object> handledType = obj.getClass();
        AnnotatedClass annotatedClass = new AnnotatedClass(handledType);
        JsonFormat jsonFormat = new JsonFormat();
        JsonSerializer<Object> serializer = new DateTimeSerializerBase<Object>(handledType, false, null);
        JsonSerializer<Object> contextualSerializer = serializer.accept(new SerializerProvider(), annotatedClass);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextual_AnnotatedClass_InvalidFormatAnnotation() throws JsonMappingException {
        Object obj = new Object();
        Class<Object> handledType = obj.getClass();
        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.setShape(JsonFormat.Shape.INT);
        AnnotatedClass annotatedClass = new AnnotatedClass(handledType);
        JsonSerializer<Object> serializer = new DateTimeSerializerBase<Object>(handledType, false, null);
        JsonSerializer<Object> contextualSerializer = serializer.accept(new SerializerProvider(), annotatedClass);
    }

    @Test
    public void testCreateContextual_AnnotatedClass_ValidFormatAnnotation() throws JsonMappingException {
        Object obj = new Object();
        Class<Object> handledType = obj.getClass();
        JsonFormat jsonFormat = new JsonFormat();
        jsonFormat.setShape(JsonFormat.Shape.STRING);
        jsonFormat.setPattern("yyyy-MM-dd");
        AnnotatedClass annotatedClass = new AnnotatedClass(handledType);
        JsonSerializer<Object> serializer = new DateTimeSerializerBase<Object>(handledType, false, null);
        JsonSerializer<Object> contextualSerializer = serializer.accept(new SerializerProvider(), annotatedClass);
        assertTrue(contextualSerializer instanceof DateTimeSerializerBase);
        DateTimeSerializerBase<Object> dateTimeSerializer = (DateTimeSerializerBase<Object>) contextualSerializer;
        assertEquals(JsonFormat.Shape.STRING, dateTimeSerializer.getShape());
        assertEquals("yyyy-MM-dd", dateTimeSerializer.getFormat().getPattern());
    }
}