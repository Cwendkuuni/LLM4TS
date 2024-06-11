package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Test;
import static org.junit.Assert.*;

public class SetterlessPropertyTest {

    @Test
    public void testConstructorAndMethods() {
        BeanPropertyDefinition propDef = new BeanPropertyDefinition();
        JavaType type = new JavaType();
        TypeDeserializer typeDeser = new TypeDeserializer();
        Annotations contextAnnotations = new Annotations();
        AnnotatedMethod method = new AnnotatedMethod();

        SetterlessProperty prop = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        assertNotNull(prop);

        SetterlessProperty prop1 = new SetterlessProperty(prop, null, null);
        assertNotNull(prop1);

        PropertyName newName = new PropertyName();
        SetterlessProperty prop2 = new SetterlessProperty(prop, newName);
        assertNotNull(prop2);

        SettableBeanProperty prop3 = prop2.withName(newName);
        assertNotNull(prop3);
        assertTrue(prop3 instanceof SetterlessProperty);

        JsonDeserializer<?> deser = new JsonDeserializer<>();
        NullValueProvider nva = new NullValueProvider();

        SetterlessProperty prop4 = prop.withValueDeserializer(deser);
        assertNotNull(prop4);
        assertTrue(prop4 instanceof SetterlessProperty);

        SetterlessProperty prop5 = prop.withNullProvider(nva);
        assertNotNull(prop5);
        assertTrue(prop5 instanceof SetterlessProperty);

        DeserializationConfig config = new DeserializationConfig();
        prop.fixAccess(config);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        SetterlessProperty prop = new SetterlessProperty();
        prop.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object instance = new Object();

        SetterlessProperty prop = new SetterlessProperty();
        prop.deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        SetterlessProperty prop = new SetterlessProperty();
        prop.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        SetterlessProperty prop = new SetterlessProperty();
        prop.setAndReturn(instance, value);
    }
}