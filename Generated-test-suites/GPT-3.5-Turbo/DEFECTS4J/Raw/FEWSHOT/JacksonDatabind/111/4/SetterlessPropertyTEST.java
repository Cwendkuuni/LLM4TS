package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping.NON_FINAL;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class SetterlessPropertyTest {
    
    @Test
    public void testWithNullProvider() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        NullValueProvider nvp = mock(NullValueProvider.class);

        SettableBeanProperty result = property.withNullProvider(nvp);

        assertEquals(nvp, result.getValueNullProvider());
    }

    @Test
    public void testWithDefaultValueDeserializer() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);

        SettableBeanProperty result = property.withValueDeserializer(deser);

        assertEquals(deser, result.getValueDeserializer());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void testDeserializeAndSet() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        property.deserializeAndSet(parser, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        Object result = property.deserializeSetAndReturn(parser, ctxt, instance);

        assertEquals(instance, result);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        property.set(instance, value);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSetAndReturn() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Object instance = mock(Object.class);
        Object value = mock(Object.class);

        Object result = property.setAndReturn(instance, value);

        assertEquals(instance, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFixAccess() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        DeserializationConfig config = mock(DeserializationConfig.class);

        property.fixAccess(config);
    }

    @Test
    public void testGetAnnotation() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        Class<Annotation> acls = Annotation.class;

        Annotation result = property.getAnnotation(acls);

        assertNull(result);
    }

    @Test
    public void testGetMember() {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);

        assertNull(property.getMember());
    }
}