package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DateTimeSerializerBaseTest {

    private DateTimeSerializerBase dateTimeSerializerBase;

    @Before
    public void setup() {
        dateTimeSerializerBase = Mockito.mock(DateTimeSerializerBase.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void test_withFormat() {
        Boolean timestamp = true;
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        DateTimeSerializerBase result = dateTimeSerializerBase.withFormat(timestamp, format);
        assertEquals(timestamp, result._useTimestamp);
        assertEquals(format, result._customFormat);
    }

    @Test
    public void test_createContextual() throws JsonMappingException {
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);
        DateTimeSerializerBase result = dateTimeSerializerBase.createContextual(serializers, property);
        assertEquals(dateTimeSerializerBase, result);
    }

    @Test
    public void test_isEmpty() {
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        T value = Mockito.mock(T.class);
        Boolean result = dateTimeSerializerBase.isEmpty(serializers, value);
        assertEquals(false, result);
    }

    @Test
    public void test_getSchema() {
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        T typeHint = Mockito.mock(T.class);
        JsonNode result = dateTimeSerializerBase.getSchema(serializers, typeHint);
        assertEquals(true, result.isObject());
    }

    @Test
    public void test_acceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        dateTimeSerializerBase.acceptJsonFormatVisitor(visitor, typeHint);
        verify(visitor, times(1)).getProvider();
        verify(visitor, times(1)).setProvider(dateTimeSerializerBase);
    }

    @Test
    public void test_visitIntFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        DateTimeSerializerBase.JsonParser.NumberType numberType = Mockito.mock(DateTimeSerializerBase.JsonParser.NumberType.class);
        JsonValueFormat jsonValueFormat = Mockito.mock(JsonValueFormat.class);
        dateTimeSerializerBase.visitIntFormat(visitor, typeHint, numberType, jsonValueFormat);
        verify(visitor, times(1)).getProvider();
        verify(visitor, times(1)).setProvider(dateTimeSerializerBase);
    }

    @Test
    public void test_visitStringFormat() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        JsonValueFormat jsonValueFormat = Mockito.mock(JsonValueFormat.class);
        dateTimeSerializerBase.visitStringFormat(visitor, typeHint, jsonValueFormat);
        verify(visitor, times(1)).getProvider();
        verify(visitor, times(1)).setProvider(dateTimeSerializerBase);
    }

    @Test
    public void test_serialize() throws IOException {
        T value = Mockito.mock(T.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider serializers = Mockito.mock(SerializerProvider.class);
        dateTimeSerializerBase.serialize(value, gen, serializers);
        verify(gen, times(2)).writeString(Mockito.anyString());
    }

    @Test
    public void test__serializeAsString() throws IOException {
        Date value = Mockito.mock(Date.class);
        JsonGenerator g = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        dateTimeSerializerBase._serializeAsString(value, g, provider);
        verify(provider, times(1)).defaultSerializeDateValue(value, g);
    }
}