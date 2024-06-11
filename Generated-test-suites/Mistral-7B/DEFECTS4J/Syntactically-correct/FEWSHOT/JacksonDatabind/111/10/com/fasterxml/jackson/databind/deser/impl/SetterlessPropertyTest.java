package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Test;

public class SetterlessPropertyTest {

    @Test
    public void testGetAnnotation() {
        SetterlessProperty setterlessProperty = createSetterlessProperty();
        Class<?> annotationType = Annotation.class;
        Annotation annotation = setterlessProperty.getAnnotation(annotationType);
        assertNotNull(annotation);
        assertEquals(AnnotatedMethod.class, annotation.getClass());
    }

    @Test
    public void testGetMember() {
        SetterlessProperty setterlessProperty = createSetterlessProperty();
        AnnotatedMember annotatedMember = setterlessProperty.getMember();
        assertNotNull(annotatedMember);
        assertEquals(AnnotatedMethod.class, annotatedMember.getClass());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        SetterlessProperty setterlessProperty = createSetterlessProperty();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");

        setterlessProperty.deserializeAndSet(jsonParser, null, new Object());

        JsonToken currentToken = jsonParser.getCurrentToken();
        assertEquals(JsonToken.END_ARRAY, currentToken);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        SetterlessProperty setterlessProperty = createSetterlessProperty();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("[]");

        Object result = setterlessProperty.deserializeSetAndReturn(jsonParser, null, new Object());

        JsonToken currentToken = jsonParser.getCurrentToken();
        assertEquals(JsonToken.END_ARRAY, currentToken);
        assertEquals(result, new Object());
    }

    @Test
    public void testSet() throws Exception {
        SetterlessProperty setterlessProperty = createSetterlessProperty();

        Object instance = new Object();
        Object value = new Object();

        try {
            setterlessProperty.set(instance, value);
            fail("Should not reach here");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }

    @Test
    public void testSetAndReturn() throws Exception {
        SetterlessProperty setterlessProperty = createSetterlessProperty();

        Object instance = new Object();
        Object value = new Object();

        try {
            setterlessProperty.setAndReturn(instance, value);
            fail("Should not reach here");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        assertEquals(instance, instance);
    }

    @Test
    public void testFixAccess() throws Exception {
        SetterlessProperty setterlessProperty = createSetterlessProperty();
        DeserializationConfig deserializationConfig = new DeserializationConfig();

        setterlessProperty.fixAccess(deserializationConfig);
        assertTrue(deserializationConfig.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    private SetterlessProperty createSetterlessProperty() {
        BeanPropertyDefinition propDef = null;
        JavaType type = null;
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);

        return new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
    }
}