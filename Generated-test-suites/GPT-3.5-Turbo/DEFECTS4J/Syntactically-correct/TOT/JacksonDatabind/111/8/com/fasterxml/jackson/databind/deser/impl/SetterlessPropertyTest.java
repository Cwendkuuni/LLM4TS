package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.BeanDeserializer;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.SetterlessProperty;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeSystem;
import com.fasterxml.jackson.databind.util.Annotations;

public class SetterlessPropertyTest {

    @Test
    public void testWithNullValueDeserializer() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;
        SetterlessProperty prop = new SetterlessProperty(propDef, type,
            typeDeser, contextAnnotations, method);

        JsonDeserializer<?> deser = null;
        NullValueProvider nva = null;

        SetterlessProperty newProp = prop.withValueDeserializer(deser);
        assertSame("ValueDeserializer is not same", deser, newProp.getValueDeserializer());

        newProp = newProp.withNullProvider(nva);
        assertSame("NullValueProvider is not same", nva, newProp.getNullValueProvider());
    }

    @Test
    public void testWithName() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;
        SetterlessProperty prop = new SetterlessProperty(propDef, type,
            typeDeser, contextAnnotations, method);

        PropertyName newName = null;
        SetterlessProperty newProp = prop.withName(newName);
        assertSame("PropertyName is not same", newName, newProp.getFullName());
    }

    @Test
    public void testFixAccess() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;
        SetterlessProperty prop = new SetterlessProperty(propDef, type,
            typeDeser, contextAnnotations, method);
        DeserializationConfig config = null;
        prop.fixAccess(config);
        // No assertions, expect no exceptions
    }

    @Test
    public void testGetAnnotation() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;
        SetterlessProperty prop = new SetterlessProperty(propDef, type,
            typeDeser, contextAnnotations, method);

        Class<A> acls = null;
        A annotation = prop.getAnnotation(acls);
        assertNull("Annotation is not null", annotation);
    }

    @Test
    public void testGetMember() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;
        SetterlessProperty prop = new SetterlessProperty(propDef, type,
            typeDeser, contextAnnotations, method);

        AnnotatedMember member = prop.getMember();
        assertNotNull("Member is null", member);
        assertSame("AnnotatedMethod is not same", method, member);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;
        SetterlessProperty prop = new SetterlessProperty(propDef, type,
            typeDeser, contextAnnotations, method);

        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        SetterlessPropertyTestObject instance = new SetterlessPropertyTestObject();
        instance.getProperty().clear();

        JsonParser parser = mapper.getFactory().createParser("[1,2,3]");
        prop.deserializeAndSet(parser, ctxt, instance);
        Collection<?> propValue = instance.getProperty();
        assertNotNull("Property is null", propValue);
        assertEquals("Property size is incorrect", 3, propValue.size());
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullValue() throws IOException {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;
        SetterlessProperty prop = new SetterlessProperty(propDef, type,
            typeDeser, contextAnnotations, method);

        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        SetterlessPropertyTestObject instance = new SetterlessPropertyTestObject();
        instance.getProperty().clear();

        JsonParser parser = mapper.getFactory().createParser("null");
        prop.deserializeAndSet(parser, ctxt, instance);
    }

    @Test
    public void testDeserializeAndSetWithNullReturnValue() throws IOException {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;
        SetterlessProperty prop = new SetterlessProperty(propDef, type,
            typeDeser, contextAnnotations, method);

        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        SetterlessPropertyTestObject instance = new SetterlessPropertyTestObject();
        instance.setProperty(null);

        JsonParser parser = mapper.getFactory().createParser("[1,2,3]");
        prop.deserializeAndSet(parser, ctxt, instance);
        Collection<?> propValue = instance.getProperty();
        assertNotNull("Property is null", propValue);
        assertEquals("Property size is incorrect", 3, propValue.size());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;
        SetterlessProperty prop = new SetterlessProperty(propDef, type,
            typeDeser, contextAnnotations, method);

        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        SetterlessPropertyTestObject instance = new SetterlessPropertyTestObject();
        instance.getProperty().clear();

        JsonParser parser = mapper.getFactory().createParser("[1,2,3]");
        Object result = prop.deserializeSetAndReturn(parser, ctxt, instance);
        assertSame("Return object is not same as instance", instance, result);
        Collection<?> propValue = instance.getProperty();
        assertNotNull("Property is null", propValue);
        assertEquals("Property size is incorrect", 3, propValue.size());
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithNullValue() throws IOException {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;
        SetterlessProperty prop = new SetterlessProperty(propDef, type,
            typeDeser, contextAnnotations, method);

        ObjectMapper mapper = new ObjectMapper();
        DeserializationContext ctxt = mapper.getDeserializationContext();
        SetterlessPropertyTestObject instance = new SetterlessPropertyTestObject();
        instance.getProperty().clear();

        JsonParser parser = mapper.getFactory().createParser("null");
        prop.deserializeSetAndReturn(parser, ctxt, instance);
    }

    @Test
    public void testSet() throws IOException {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;
        SetterlessProperty prop = new SetterlessProperty(propDef, type,
            typeDeser, contextAnnotations, method);

        SetterlessPropertyTestObject instance = new SetterlessPropertyTestObject();
        instance.setProperty(null);

        Object value = new Object();
        try {
            prop.set(instance, value);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        Collection<?> propValue = instance.getProperty();
        assertNull("Property is not null", propValue);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = null;
        SetterlessProperty prop = new SetterlessProperty(propDef, type,
            typeDeser, contextAnnotations, method);

        SetterlessPropertyTestObject instance = new SetterlessPropertyTestObject();
        instance.setProperty(null);

        Object value = new Object();
        try {
            prop.setAndReturn(instance, value);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        Collection<?> propValue = instance.getProperty();
        assertNull("Property is not null", propValue);
    }

    private static class SetterlessPropertyTestObject {
        private Collection<Integer> _property;

        public Collection<Integer> getProperty() {
            return _property;
        }

        public void setProperty(Collection<Integer> property) {
            _property = property;
        }
    }
}