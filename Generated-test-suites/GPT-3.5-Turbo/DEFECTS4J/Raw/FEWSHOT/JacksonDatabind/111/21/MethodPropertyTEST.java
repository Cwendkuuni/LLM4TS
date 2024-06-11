package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.DeserializationContext;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class MethodPropertyTest {

    private SettableBeanProperty property;
    private AnnotatedMethod annotatedMethod;
    private Method setterMethod;
    private PropertyName propertyName;
    private JsonDeserializer jsonDeserializer;
    private NullValueProvider nullValueProvider;
    private DeserializationContext deserializationContext;
    private ObjectMapper objectMapper;
    private JsonParser jsonParser;
    private TypeDeserializer typeDeserializer;
    private ValueInstantiator valueInstantiator;
    private Annotations annotations;

    @Before
    public void setUp() throws Exception {
        annotatedMethod = mock(AnnotatedMethod.class);
        annotatedMethod.fixAccess(true);
        setterMethod = annotatedMethod.getAnnotated();
        propertyName = new PropertyName("testProperty");

        jsonDeserializer = mock(JsonDeserializer.class);
        nullValueProvider = mock(NullValueProvider.class);

        property = new MethodProperty(annotatedMethod, jsonDeserializer, nullValueProvider);

        deserializationContext = mock(DeserializationContext.class);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule());

        jsonParser = mock(JsonParser.class);
        typeDeserializer = mock(TypeDeserializer.class);
        valueInstantiator = mock(ValueInstantiator.class);
        annotations = mock(Annotations.class);
    }

    @Test
    public void testConstructor() {
        assertEquals(propertyName, property.getFullName());

        assertEquals(annotatedMethod, property.getMember());

        assertEquals(jsonDeserializer, property.getValueDeserializer());
        assertEquals(nullValueProvider, property.getValueNullProvider());

        assertNull(property.getInjectableValueId());
        assertNull(property.getTypeId());
        assertNull(property.getTypeDeserializer());
        assertNull(property.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("test");

        Object instance = new Object();
        property.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(setterMethod).invoke(instance, "test");
    }

    @Test
    public void testDeserializeAndSet_NullValue() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        Object instance = new Object();
        property.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(setterMethod, never()).invoke(any(), any());
    }

    @Test
    public void testDeserializeAndSet_NullValueAndSkipNulls() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(nullValueProvider.getNullValue(deserializationContext)).thenReturn(null);
        property = property.withNullProvider(nullValueProvider);

        Object instance = new Object();
        property.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(setterMethod, never()).invoke(any(), any());
    }

    @Test
    public void testDeserializeAndSet_NullValueAndSkipNulls_ReturnsInstance() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(nullValueProvider.getNullValue(deserializationContext)).thenReturn(null);
        property = property.withNullProvider(nullValueProvider);

        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertEquals(instance, result);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("test");

        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertEquals(instance, result);
        verify(setterMethod).invoke(instance, "test");
    }

    @Test
    public void testDeserializeSetAndReturn_NullValue() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertEquals(instance, result);
        verify(setterMethod, never()).invoke(any(), any());
    }

    @Test
    public void testDeserializeSetAndReturn_NullValueAndSkipNulls() throws IOException {
        when(jsonParser.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        when(nullValueProvider.getNullValue(deserializationContext)).thenReturn(null);
        property = property.withNullProvider(nullValueProvider);

        Object instance = new Object();
        Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertEquals(instance, result);
        verify(setterMethod, never()).invoke(any(), any());
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        property.set(instance, "test");

        verify(setterMethod).invoke(instance, "test");
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object result = property.setAndReturn(instance, "test");

        assertEquals(instance, result);
        verify(setterMethod).invoke(instance, "test");
    }

}