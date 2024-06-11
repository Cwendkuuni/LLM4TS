package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class MethodPropertyTest {

    @Test
    public void testMethodPropertyConstruction() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        assertNull(property._annotated);
        assertNull(property._setter);
        assertFalse(property._skipNulls);

        AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
        Method setter = Mockito.mock(Method.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);

        MethodProperty propertyWithAnnotations = new MethodProperty(
                new BeanPropertyDefinition(), type, typeDeserializer, contextAnnotations, annotatedMethod);
        assertEquals(annotatedMethod, propertyWithAnnotations._annotated);
        assertEquals(setter, propertyWithAnnotations._setter);
        assertTrue(propertyWithAnnotations._skipNulls);
    }

    @Test
    public void testMethodPropertyWithName() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        PropertyName newName = Mockito.mock(PropertyName.class);
        MethodProperty propertyWithName = new MethodProperty(property, newName);
        assertEquals(newName, propertyWithName.getName());
        assertEquals(property._annotated, propertyWithName._annotated);
        assertEquals(property._setter, propertyWithName._setter);
        assertEquals(property._skipNulls, propertyWithName._skipNulls);
    }

    @Test
    public void testMethodPropertyWithValueDeserializer() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);
        MethodProperty propertyWithValueDeserializer = new MethodProperty(property, valueDeserializer, null);
        assertEquals(valueDeserializer, propertyWithValueDeserializer._valueDeserializer);
        assertEquals(property._annotated, propertyWithValueDeserializer._annotated);
        assertEquals(property._setter, propertyWithValueDeserializer._setter);
        assertEquals(property._skipNulls, propertyWithValueDeserializer._skipNulls);
    }

    @Test
    public void testMethodPropertyWithNullProvider() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        NullValueProvider nullValueProvider = Mockito.mock(NullValueProvider.class);
        MethodProperty propertyWithNullProvider = new MethodProperty(property, null, nullValueProvider);
        assertEquals(nullValueProvider, propertyWithNullProvider._nullProvider);
        assertEquals(property._annotated, propertyWithNullProvider._annotated);
        assertEquals(property._setter, propertyWithNullProvider._setter);
        assertEquals(property._skipNulls, propertyWithNullProvider._skipNulls);
    }

    @Test
    public void testMethodPropertyWithTypeDeserializer() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);
        MethodProperty propertyWithTypeDeserializer = new MethodProperty(property, null, null, null, typeDeserializer);
        assertEquals(typeDeserializer, propertyWithTypeDeserializer._valueTypeDeserializer);
        assertEquals(property._annotated, propertyWithTypeDeserializer._annotated);
        assertEquals(property._setter, propertyWithTypeDeserializer._setter);
        assertEquals(property._skipNulls, propertyWithTypeDeserializer._skipNulls);
    }

    @Test
    public void testMethodPropertyWithDeserializer() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        MethodProperty propertyWithDeserializer = new MethodProperty(property, deser, nva);
        assertEquals(deser, propertyWithDeserializer._valueDeserializer);
        assertEquals(property._annotated, propertyWithDeserializer._annotated);
        assertEquals(property._setter, propertyWithDeserializer._setter);
        assertEquals(property._skipNulls, propertyWithDeserializer._skipNulls);
        assertEquals(nva, propertyWithDeserializer._nullProvider);
    }

    @Test
    public void testMethodPropertyFixAccess() {
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        property.fixAccess(config);
        verify(property._annotated).fixAccess(eq(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullAnnotatedMethod() {
        new MethodProperty(null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullSetterMethod() {
        new MethodProperty(null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullJavaType() {
        new MethodProperty(null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMethodPropertyWithNullTypeDeserializer() {
        new MethodProperty(null, null, null, null, null);
    }

    @Test
    public void testMethodPropertyDeserializeAndSet() throws IOException {
        Object instance = new Object();
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        Object value = new Object();
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("value");

        property.deserializeAndSet(jsonParser, deserializationContext, instance);
        verify(instance._setter).invoke(instance, eq(value));
    }

    @Test
    public void testMethodPropertyDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        Object value = new Object();
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.getText()).thenReturn("value");

        Object result = property.deserializeSetAndReturn(jsonParser, deserializationContext, instance);
        assertEquals(instance, result);
    }

    @Test
    public void testMethodPropertySet() throws IOException {
        Object instance = new Object();
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        Object value = new Object();

        property.set(instance, value);
        verify(instance._setter).invoke(instance, eq(value));
    }

    @Test
    public void testMethodPropertySetAndReturn() throws IOException {
        Object instance = new Object();
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        Object value = new Object();

        Object result = property.setAndReturn(instance, value);
        assertEquals(instance, result);
    }

    @Test
    public void testMethodPropertyReadResolve() throws IOException {
        Object instance = new Object();
        MethodProperty property = new MethodProperty(null, null, null, null, null);
        Object value = new Object();

        property.set(instance, value);
        MethodProperty propertyReadResolve = property.readResolve();

        propertyReadResolve.set(instance, value);
        assertEquals(property, propertyReadResolve);
    }
}