package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.util.Collections;

import org.junit.Test;

public class SetterlessPropertyTest {

    @Test
    public void testConstructor() throws NoSuchMethodException {
        Method annotatedMethod = SetterlessProperty.class.getDeclaredMethod("_annotated");
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                null,
                null,
                null,
                null,
                new AnnotatedMethod(null, null, "", null, annotatedMethod)
        );

        assertNull(setterlessProperty.getName());
        assertNull(setterlessProperty.getType());
        assertNull(setterlessProperty.getTypeDeserializer());
        assertNull(setterlessProperty.getContextAnnotations());
        assertEquals(annotatedMethod, setterlessProperty.getMember().getAnnotated());
    }

    @Test
    public void testWithNullProvider() throws NoSuchMethodException {
        Method annotatedMethod = SetterlessProperty.class.getDeclaredMethod("_annotated");
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                null,
                null,
                null,
                null,
                new AnnotatedMethod(null, null, "", null, annotatedMethod)
        ).withNullProvider(null);

        assertNull(setterlessProperty.getNullValueProvider());
    }

    @Test
    public void testWithname() throws NoSuchMethodException {
        Method annotatedMethod = SetterlessProperty.class.getDeclaredMethod("_annotated");
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                null,
                null,
                null,
                null,
                new AnnotatedMethod(null, null, "", null, annotatedMethod)
        ).withName(null);

        assertNull(setterlessProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() throws NoSuchMethodException {
        Method annotatedMethod = SetterlessProperty.class.getDeclaredMethod("_annotated");
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                null,
                null,
                null,
                null,
                new AnnotatedMethod(null, null, "", null, annotatedMethod)
        ).withValueDeserializer(null);

        assertNull(setterlessProperty.getValueDeserializer());
    }

    @Test
    public void testFixAccess() throws NoSuchMethodException {
        Method annotatedMethod = SetterlessProperty.class.getDeclaredMethod("_annotated");
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                null,
                null,
                null,
                null,
                new AnnotatedMethod(null, null, "", null, annotatedMethod)
        );

        // Call the method to ensure code coverage
        setterlessProperty.fixAccess(null);

        assertTrue(setterlessProperty.getMember().getAnnotated().accessible);
    }

    @Test
    public void testGetAnnotation() throws NoSuchMethodException {
        Method annotatedMethod = SetterlessProperty.class.getDeclaredMethod("_annotated");
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                null,
                null,
                null,
                null,
                new AnnotatedMethod(null, null, "", null, annotatedMethod)
        );

        assertNull(setterlessProperty.getAnnotation(null));
    }

    @Test
    public void testSerializeAndSet() throws NoSuchMethodException {
        Method annotatedMethod = SetterlessProperty.class.getDeclaredMethod("_annotated");
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                null,
                null,
                null,
                null,
                new AnnotatedMethod(null, null, "", null, annotatedMethod)
        );

        setterlessProperty.deserializeAndSet(null, null, null);
    }

    @Test
    public void testDeserializeSetAndReturn() throws NoSuchMethodException {
    	Method annotatedMethod = SetterlessProperty.class.getDeclaredMethod("_annotated");
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                null,
                null,
                null,
                null,
                new AnnotatedMethod(null, null, "", null, annotatedMethod)
        );

        assertNull(setterlessProperty.deserializeSetAndReturn(null, null, null));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws NoSuchMethodException {
        Method annotatedMethod = SetterlessProperty.class.getDeclaredMethod("_annotated");
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                null,
                null,
                null,
                null,
                new AnnotatedMethod(null, null, "", null, annotatedMethod)
        );

        setterlessProperty.set(null, null);
    }

    @Test
    public void testSetAndReturn() throws NoSuchMethodException {
        Method annotatedMethod = SetterlessProperty.class.getDeclaredMethod("_annotated");
        SetterlessProperty setterlessProperty = new SetterlessProperty(
                null,
                null,
                null,
                null,
                new AnnotatedMethod(null, null, "", null, annotatedMethod)
        );

        assertNull(setterlessProperty.setAndReturn(null, null));
    }
}