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
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.ser.std.StdDateFormatSerializer;
import com.fasterxml.jackson.databind.type.JavaType;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class DateTimeSerializerBaseTest extends MockObjectTestCase {

    @Test
    public void testCreateContextualWithNullProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Object> serializer = new StdDateFormatSerializer(mapper.getTypeFactory().constructType(Object.class), null, null);
        JsonFormatVisitorWrapper visitor = createMock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mapper.getTypeFactory().constructType(Object.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).visitStringFormat(eq(typeHint), eq(JsonValueFormat.DATE_TIME));
    }

    @Test
    public void testCreateContextualWithFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormat format = new JsonFormat();
        format.shape(JsonFormat.Shape.STRING);
        format.timeZone(TimeZone.getDefault());
        format.locale(Locale.getDefault());
        format.pattern("yyyy-MM-dd HH:mm:ss");
        DateTimeSerializerBase<Object> serializer = new StdDateFormatSerializer(mapper.getTypeFactory().constructType(Object.class), null, new SimpleDateFormat(format.getPattern(), format.getLocale()));
        JsonFormatVisitorWrapper visitor = createMock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mapper.getTypeFactory().constructType(Object.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).visitStringFormat(eq(typeHint), eq(new JSONStringFormat(format.getPattern(), format.getLocale(), format.getTimeZone())));
    }

    @Test
    public void testCreateContextualWithTimestamp() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Object> serializer = new StdDateFormatSerializer(mapper.getTypeFactory().constructType(Object.class), true, null);
        JsonFormatVisitorWrapper visitor = createMock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mapper.getTypeFactory().constructType(Object.class);
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor).visitIntFormat(eq(typeHint), eq(JsonParser.NumberType.LONG), eq(JsonValueFormat.UTC_MILLISEC));
    }

    @Test
    public void testSerializeNullValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Object> serializer = new StdDateFormatSerializer(mapper.getTypeFactory().constructType(Object.class), false, null);
        Object value = null;
        JsonGenerator gen = createMock(JsonGenerator.class);
        SerializerProvider serializers = createMock(SerializerProvider.class);
        serializer.serialize(value, gen, serializers);
        assertEquals("null", gen.getOutputValueAsString());
    }

    @Test
    public void testSerializeTimestampValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Long> serializer = new StdDateFormatSerializer(mapper.getTypeFactory().constructType(Long.class), true, null);
        Long value = 1234567890L;
        JsonGenerator gen = createMock(JsonGenerator.class);
        SerializerProvider serializers = createMock(SerializerProvider.class);
        serializer.serialize(value, gen, serializers);
        assertEquals("1234567890", gen.getOutputValueAsString());
    }

    @Test
    public void testSerializeDateFormatValue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DateTimeSerializerBase<String> serializer = new StdDateFormatSerializer(mapper.getTypeFactory().constructType(String.class), false, df);
        String value = "2023-02-14 15:30:00";
        JsonGenerator gen = createMock(JsonGenerator.class);
        SerializerProvider serializers = createMock(SerializerProvider.class);
        serializer.serialize(value, gen, serializers);
        assertEquals("\"2023-02-14 15:30:00\"", gen.getOutputValueAsString());
    }

    @Test(expected = JsonMappingException.class)
    public void testSerializeWithInvalidFormat() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Object> serializer = new StdDateFormatSerializer(mapper.getTypeFactory().constructType(Object.class), false, null);
        Object value = new Object();
        JsonGenerator gen = createMock(JsonGenerator.class);
        SerializerProvider serializers = createMock(SerializerProvider.class);
        Annotated annotated = createMock(Annotated.class);
        JsonFormat format = createMock(JsonFormat.class);
        AnnotationUtils.setAnnotation(value, JsonFormat.class, format);
        AnnotationUtils.setAnnotation(annotated, JsonFormat.class, format);
        when(serializers.getAnnotationIntrospector().findFormat((Annotated)anyObject())).thenReturn(annotated);
        serializer.serialize(value, gen, serializers);
    }

    @Test
    public void testEmpty() throws Exception {
        DateTimeSerializerBase<Object> serializer = new StdDateFormatSerializer(null, false, null);
        assertFalse(serializer.isEmpty(null));
        assertFalse(serializer.isEmpty(new Object()));
        assertTrue(serializer.isEmpty(null, null));
        assertTrue(serializer.isEmpty(new Object(), null));
    }

    @Test
    public void testGetSchema() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        DateTimeSerializerBase<Object> serializer = new StdDateFormatSerializer(mapper.getTypeFactory().constructType(Object.class), false, null);
        JsonNode schema = serializer.getSchema(mapper.getSerializerProvider(), null);
        assertEquals("string", schema.getClass().getSimpleName());
    }
}