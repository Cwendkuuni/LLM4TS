package org.mockito.internal.configuration;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor_NullAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Assert.assertNull(annotationEngine.createMockFor(null, null));
    }

    @Test(expected = MockitoException.class)
    public void testProcess_MissingInstance_ExceptionThrown() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = getClass();
        Object testClass = new Object();
        Field field = createField(context, "testField");
        field.setAccessible(true);
        SpyAnnotationEngine.assertNoAnnotations(SpyAnnotationEngine.class, field, Mockito.Mock.class, Captor.class);
        annotationEngine.process(context, testClass);
        try {
            field.set(testClass, null);
            annotationEngine.process(context, testClass);
        } finally {
            field.setAccessible(false);
        }
    }

    @Test
    public void testProcess_NullField() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = getClass();
        Object testClass = new Object();
        SpyAnnotationEngine.assertNoAnnotations(SpyAnnotationEngine.class, null, Mockito.Mock.class, Captor.class);
        annotationEngine.process(context, testClass);
    }

    @Test
    public void testProcess_ExistsAnnotation_Spy() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = getClass();
        Object testClass = new Object();
        Field field = createField(context, "testField");
        field.setAccessible(true);
        Object instance = new Object();
        SpyAnnotationEngine.assertNoAnnotations(SpyAnnotationEngine.class, field, Mockito.Mock.class, Captor.class);
        field.set(testClass, instance);
        annotationEngine.process(context, testClass);
        Assert.assertNotNull(field.get(testClass));
        Assert.assertTrue(MockUtil.isSpy(field.get(testClass)));
    }

    @Test(expected = MockitoException.class)
    public void testProcess_ExistsAnnotation_DuplicateSpy() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = getClass();
        Object testClass = new Object();
        Field field1 = createField(context, "testField1");
        Field field2 = createField(context, "testField2");
        field1.setAccessible(true);
        field2.setAccessible(true);
        Object instance1 = new Object();
        Object instance2 = new Object();
        SpyAnnotationEngine.assertNoAnnotations(SpyAnnotationEngine.class, field1, Mockito.Mock.class, Captor.class);
        SpyAnnotationEngine.assertNoAnnotations(SpyAnnotationEngine.class, field2, Mockito.Mock.class, Captor.class);
        field1.set(testClass, instance1);
        field2.set(testClass, instance2);
        annotationEngine.process(context, testClass);
        try {
            annotationEngine.process(context, testClass);
        } finally {
            field1.setAccessible(false);
            field2.setAccessible(false);
        }
    }

    @Test(expected = MockitoException.class)
    public void testProcess_ExistsAnnotation_AnnotationTypeMismatch() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = getClass();
        Object testClass = new Object();
        Field field = createField(context, "testField");
        field.setAccessible(true);
        Object instance = new Object();
        SpyAnnotationEngine.assertNoAnnotations(SpyAnnotationEngine.class, field, Mockito.Mock.class, Captor.class, MyAnnotation.class);
        field.set(testClass, instance);
        annotationEngine.process(context, testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcess_ExistsAnnotation_AnnotationPresentMultipleTimes() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = getClass();
        Object testClass = new Object();
        Field field = createField(context, "testField");
        field.setAccessible(true);
        Object instance = new Object();
        SpyAnnotationEngine.assertNoAnnotations(SpyAnnotationEngine.class, field, Mockito.Mock.class, Captor.class, Spy.class);
        field.set(testClass, instance);
        annotationEngine.process(context, testClass);
        try {
            SpyAnnotationEngine.assertNoAnnotations(SpyAnnotationEngine.class, field, Mockito.Mock.class, Captor.class, Spy.class);
            field.set(testClass, instance);
            annotationEngine.process(context, testClass);
        } finally {
            field.setAccessible(false);
        }
    }

    @Test(expected = MockitoException.class)
    public void testProcess_ExistsAnnotation_IllegalAccessException() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = getClass();
        Object testClass = new Object();
        Field field = createField(context, "testField");
        field.setAccessible(true);
        Object instance = new Object();
        SpyAnnotationEngine.assertNoAnnotations(SpyAnnotationEngine.class, field, Mockito.Mock.class, Captor.class);
        try {
            field.set(testClass, instance);
            annotationEngine.process(context, testClass);
            Mockito.reset(instance);
            field.set(testClass, instance);
            annotationEngine.process(context, testClass);
        } finally {
            field.setAccessible(false);
        }
    }

    @Test
    public void testAssertNoAnnotations_AnnotationPresent() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = getClass();
        Object testClass = new Object();
        Field field = createField(context, "testField");
        field.setAccessible(true);
        Object instance = new Object();
        field.set(testClass, instance);
        SpyAnnotationEngine.assertNoAnnotations(SpyAnnotationEngine.class, field, Mockito.Mock.class, Captor.class, Spy.class);
    }

    @Test(expected = Reporter.UnsupportedCombinationOfAnnotations.class)
    public void testAssertNoAnnotations_AnnotationMismatch() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class context = getClass();
        Object testClass = new Object();
        Field field = createField(context, "testField");
        field.setAccessible(true);
        Object instance = new Object();
        field.set(testClass, instance);
        SpyAnnotationEngine.assertNoAnnotations(SpyAnnotationEngine.class, field, Spy.class, Captor.class, Mockito.Mock.class);
    }

    private Field createField(Class clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Field " + fieldName + " not found in " + clazz.getName(), e);
        }
    }

}