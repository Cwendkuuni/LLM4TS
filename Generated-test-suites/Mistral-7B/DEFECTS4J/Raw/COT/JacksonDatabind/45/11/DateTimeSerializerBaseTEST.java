package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.DateTimeSerializerBase;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.test.BaseTest;

import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JUnit4Mockito;

@RunWith(JUnit4Mockito.class)
public class DateTimeSerializerBaseTest {

    @Test
    public void testConstructor() {
        Class<String> stringType = String.class;
        Boolean useTimestamp = false;
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(stringType, useTimestamp, customFormat);
        assertTrue(serializer instanceof DateTimeSerializerBase);
        assertTrue(serializer instanceof StdScalarSerializer);
        assertTrue(serializer instanceof ContextualSerializer);
        assertNull(serializer._useTimestamp);
        assertSame(customFormat, serializer._customFormat);
    }

    @Test
    public void testWithFormat_useTimestampTrue() {
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, true, null);
        DateTimeSerializerBase<Long> serializedSerializer = serializer.withFormat(true, null);
        assertTrue(serializedSerializer instanceof DateTimeSerializerBase);
        assertTrue(serializedSerializer._useTimestamp.booleanValue());
        assertNull(serializedSerializer._customFormat);
    }

    @Test
    public void testWithFormat_useTimestampFalseAndCustomFormatSet() {
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<>(String.class, false, null);
        DateFormat customFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US);
        DateTimeSerializerBase<String> serializedSerializer = serializer.withFormat(false, customFormat);
        assertTrue(serializedSerializer instanceof DateTimeSerializerBase);
        assertFalse(serializedSerializer._useTimestamp.booleanValue());
        assertSame(customFormat, serializedSerializer._customFormat);
    }

    @Test
    public void testCreateContextual_withFormatIsNull() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JsonFormat.Value jsonFormat = Mockito.mock(JsonFormat.Value.class);
        when(jsonFormat.getShape()).thenReturn(JsonFormat.Shape.ARRAY);
        when(objectMapper.getAnnotationIntrospector()).thenReturn(Mockito.mock(AnnotationIntrospector.class));
        when(objectMapper.getAnnotationIntrospector().findFormat((Annotated)property)).thenReturn(jsonFormat);

        DateTimeSerializerBase<String> serializer = Mockito.mock(DateTimeSerializerBase.class);
        when(serializer.createContextual(Mockito.any(SerializerProvider.class), Mockito.any(BeanProperty.class))).thenCallRealMethod();

        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        serializers.setProviderDefaults(objectMapper);

        serializer.createContextual(serializers, property);
        verify(serializer, times(1)).createContextual(serializers, property);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextual_withFormatNumeric() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JsonFormat.Value jsonFormat = Mockito.mock(JsonFormat.Value.class);
        when(jsonFormat.getShape()).thenReturn(JsonFormat.Shape.NUMBER);
        when(objectMapper.getAnnotationIntrospector()).thenReturn(Mockito.mock(AnnotationIntrospector.class));
        when(objectMapper.getAnnotationIntrospector().findFormat((Annotated)property)).thenReturn(jsonFormat);

        DateTimeSerializerBase<String> serializer = Mockito.mock(DateTimeSerializerBase.class);
        when(serializer.createContextual(Mockito.any(SerializerProvider.class), Mockito.any(BeanProperty.class))).thenCallRealMethod();

        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        serializers.setProviderDefaults(objectMapper);

        serializer.createContextual(serializers, property);
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextual_withFormatString() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JsonFormat.Value jsonFormat = Mockito.mock(JsonFormat.Value.class);
        when(jsonFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(objectMapper.getAnnotationIntrospector()).thenReturn(Mockito.mock(AnnotationIntrospector.class));
        when(objectMapper.getAnnotationIntrospector().findFormat((Annotated)property)).thenReturn(jsonFormat);

        DateTimeSerializerBase<String> serializer = Mockito.mock(DateTimeSerializerBase.class);
        when(serializer.createContextual(Mockito.any(SerializerProvider.class), Mockito.any(BeanProperty.class))).thenCallRealMethod();

        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        serializers.setProviderDefaults(objectMapper);

        serializer.createContextual(serializers, property);

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z")
        String testDate = "2023-03-15 12:34:56 EST";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        when(jsonParser.getValueAsString()).thenReturn(testDate);

        try {
            serializer.deserialize(jsonParser, String.class, serializers);
        } catch (IOException e) {
            throw new JsonMappingException(e);
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextual_withFormatStringInvalidTimeZone() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JsonFormat.Value jsonFormat = Mockito.mock(JsonFormat.Value.class);
        when(jsonFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(objectMapper.getAnnotationIntrospector()).thenReturn(Mockito.mock(AnnotationIntrospector.class));
        when(objectMapper.getAnnotationIntrospector().findFormat((Annotated)property)).thenReturn(jsonFormat);

        DateTimeSerializerBase<String> serializer = Mockito.mock(DateTimeSerializerBase.class);
        when(serializer.createContextual(Mockito.any(SerializerProvider.class), Mockito.any(BeanProperty.class))).thenCallRealMethod();

        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        serializers.setProviderDefaults(objectMapper);

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z", timezone = "XXX")
        String testDate = "2023-03-15 12:34:56 XXX";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        when(jsonParser.getValueAsString()).thenReturn(testDate);

        try {
            serializer.deserialize(jsonParser, String.class, serializers);
        } catch (IOException e) {
            throw new JsonMappingException(e);
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testCreateContextual_withFormatStringInvalidLocale() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        BeanProperty property = Mockito.mock(BeanProperty.class);
        JsonFormat.Value jsonFormat = Mockito.mock(JsonFormat.Value.class);
        when(jsonFormat.getShape()).thenReturn(JsonFormat.Shape.STRING);
        when(objectMapper.getAnnotationIntrospector()).thenReturn(Mockito.mock(AnnotationIntrospector.class));
        when(objectMapper.getAnnotationIntrospector().findFormat((Annotated)property)).thenReturn(jsonFormat);

        DateTimeSerializerBase<String> serializer = Mockito.mock(DateTimeSerializerBase.class);
        when(serializer.createContextual(Mockito.any(SerializerProvider.class), Mockito.any(BeanProperty.class))).thenCallRealMethod();

        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        serializers.setProviderDefaults(objectMapper);

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss z", locale = "XX")
        String testDate = "2023-03-15 12:34:56 z";
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        when(jsonParser.getValueAsString()).thenReturn(testDate);

        try {
            serializer.deserialize(jsonParser, String.class, serializers);
        } catch (IOException e) {
            throw new JsonMappingException(e);
        }
    }

    @Test
    public void testSerialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);
        T t = "2023-03-15 12:34:56";

        when(jsonGenerator.getOverridingOutput(JsonParser.NumberType.LONG)).thenReturn(1L);
        when(jsonGenerator.writeNumber(1L)).thenCallRealMethod();
        when(serializer.serialize(t, jsonGenerator, serializerProvider)).thenCallRealMethod();
        when(serializerProvider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(true);

        serializer.serialize("2023-03-15 12:34:56", jsonGenerator, serializerProvider);
        verify(jsonGenerator, times(1)).writeNumber(1L);

        when(serializerProvider.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)).thenReturn(false);
        when(jsonGenerator.getOverridingOutput(JsonParser.NumberType.LONG)).thenReturn(null);
        when(jsonGenerator.writeString("2023-03-15 12:34:56")).thenCallRealMethod();
        when(serializer.serialize(t, jsonGenerator, serializerProvider)).thenCallRealMethod();

        serializer.serialize("2023-03-15 12:34:56", jsonGenerator, serializerProvider);
        verify(jsonGenerator, times(1)).writeString("2023-03-15 12:34:56");
    }

    @Test
    public void testIsEmpty() {
        Date date = new Date(0L);
        assertTrue(DateTimeSerializerBase.class.newInstance().isEmpty((Object) date));
        assertTrue(DateTimeSerializerBase.class.newInstance().isEmpty(null));
    }

    @Test
    public void testGetSchema() {
        ObjectMapper objectMapper = new ObjectMapper();
        DateTimeSerializerBase<Long> serializer = new DateTimeSerializerBase<Long>(Long.class, false, null);
        JsonNode schema = serializer.getSchema(objectMapper.getSerializerProvider(), Long.class);
        assertEquals("number", schema.getClass().getSimpleName());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper objectMapper = new ObjectMapper();
        DateTimeSerializerBase<String> serializer = new DateTimeSerializerBase<String>(String.class, false, null);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);

        when(visitor.getProvider()).thenReturn(objectMapper.getSerializerProvider());
        when(visitor.acceptIntFormat(null, null, JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC)).thenCallRealMethod();
        when(visitor.acceptStringFormat(null, null, JsonValueFormat.DATE_TIME)).thenCallRealMethod();

        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor, times(1)).acceptStringFormat(null, null, JsonValueFormat.DATE_TIME);
    }
}