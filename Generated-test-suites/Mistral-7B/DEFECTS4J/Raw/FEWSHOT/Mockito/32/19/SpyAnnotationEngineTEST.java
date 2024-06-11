package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;

import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

public class SpyAnnotationEngineTest {

    @Test
    public void testProcessWithNoSpyAnnotation() {
        Class context = getClass();
        Object testClass = mock(Object.class);
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.process(context, testClass);
        assertFalse(testClass.getClass().isAnnotationPresent(Spy.class));
    }

    @Test
    public void testProcessWithSpyAnnotationOnField() {
        Class context = getClass();
        Object testClass = mock(Object.class);
        Object fieldInstance = mock(String.class);
        Field field = createField("field", String.class, testClass);
        field.set(testClass, fieldInstance);
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        annotationEngine.process(context, testClass);

        assertFalse(field.isAnnotationPresent(Mock.class));
        assertFalse(field.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class));
        assertFalse(field.isAnnotationPresent(Captor.class));
        assertTrue(field.isAnnotationPresent(Spy.class));

        assertNull(MockUtil.isMock(fieldInstance));
        assertNotNull(field.get(testClass));
        assertEquals(fieldInstance, field.get(testClass));

        verify(fieldInstance, never()).equals(testClass);
        verify(fieldInstance, never()).equals(annotationEngine);
        verify(fieldInstance, never()).equals(context);
    }

    @Test(expected = MockitoException.class)
    public void testProcessWithSpyAnnotationOnNullField() {
        Class context = getClass();
        Object testClass = mock(Object.class);
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Field field = createField("field", String.class, testClass);
        field.setAccessible(true);
        annotationEngine.process(context, testClass);
        assertNull(field.get(testClass));
    }

    @Test(expected = MockitoException.class)
    public void testProcessWithSpyAnnotationOnNonAccessibleField() {
        Class context = getClass();
        Object testClass = mock(Object.class);
        Object fieldInstance = mock(String.class);
        Field field = createField("field", String.class, testClass);
        field.set(testClass, fieldInstance);
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        field.setAccessible(false);
        annotationEngine.process(context, testClass);
    }

    @Test
    public void testProcessWithSpyAnnotationOnMockInstance() {
        Class context = getClass();
        Object testClass = mock(Object.class);
        Object fieldInstance = mock(String.class);
        Field field = createField("field", String.class, testClass);
        field.set(testClass, fieldInstance);
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Mockito.reset(fieldInstance);

        annotationEngine.process(context, testClass);

        assertFalse(field.isAnnotationPresent(Mock.class));
        assertFalse(field.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class));
        assertFalse(field.isAnnotationPresent(Captor.class));
        assertTrue(field.isAnnotationPresent(Spy.class));

        assertTrue(MockUtil.isMock(fieldInstance));
        assertNotNull(field.get(testClass));
        assertEquals(fieldInstance, field.get(testClass));

        verify(fieldInstance, never()).equals(testClass);
        verify(fieldInstance, never()).equals(annotationEngine);
        verify(fieldInstance, never()).equals(context);
    }

    @Test
    public void testProcessWithSpyAnnotationOnMockAndSpyInstance() {
        Class context = getClass();
        Object testClass = mock(Object.class);
        Object fieldInstance = spy(mock(String.class));
        Field field = createField("field", String.class, testClass);
        field.set(testClass, fieldInstance);
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        annotationEngine.process(context, testClass);

        assertFalse(field.isAnnotationPresent(Mock.class));
        assertFalse(field.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class));
        assertFalse(field.isAnnotationPresent(Captor.class));
        assertTrue(field.isAnnotationPresent(Spy.class));

        assertTrue(MockUtil.isMock(fieldInstance));
        assertNotNull(field.get(testClass));
        assertEquals(fieldInstance, field.get(testClass));

        verify(fieldInstance, never()).equals(testClass);
        verify(fieldInstance, never()).equals(annotationEngine);
        verify(fieldInstance, never()).equals(context);
    }

    @Test
    public void testProcessWithSpyAnnotationOnCaptorField() {
        Class context = getClass();
        Object testClass = mock(Object.class);
        Object fieldInstance = mock(String[].class);
        Field field = createField("field", String[].class, testClass);
        field.set(testClass, fieldInstance);
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        annotationEngine.process(context, testClass);

        assertFalse(field.isAnnotationPresent(Mock.class));
        assertFalse(field.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class));
        assertTrue(field.isAnnotationPresent(Captor.class));
        assertFalse(field.isAnnotationPresent(Spy.class));

        assertNull(MockUtil.isMock(fieldInstance.getClass().getComponentType().getComponentType()));
        assertNotNull(field.get(testClass));
        assertEquals(fieldInstance, field.get(testClass));
    }

    @Test
    public void testProcessWithSpyAnnotationOnMockAndCaptorField() {
        Class context = getClass();
        Object testClass = mock(Object.class);
        Object fieldInstance = mock(String[].class);
        Field field = createField("field", String[].class, testClass);
        field.set(testClass, fieldInstance);
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        Mockito.reset(fieldInstance);
        annotationEngine.process(context, testClass);

        assertFalse(field.isAnnotationPresent(Mock.class));
        assertFalse(field.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class));
        assertTrue(field.isAnnotationPresent(Captor.class));
        assertFalse(field.isAnnotationPresent(Spy.class));

        assertTrue(MockUtil.isMock(Mockito.mock(fieldInstance.getClass().getComponentType().getComponentType())));
        assertNotNull(field.get(testClass));
        assertEquals(fieldInstance, field.get(testClass));

        verify(fieldInstance, never()).equals(testClass);
        verify(fieldInstance, never()).equals(annotationEngine);
        verify(fieldInstance, never()).equals(context);
    }

    @Test
    public void testProcessWithSpyAnnotationOnFieldWithMultipleAnnotations() {
        Class context = getClass();
        Object testClass = mock(Object.class);
        Object fieldInstance = mock(String.class);
        Field field = createField("field", String.class, testClass);
        field.set(testClass, fieldInstance);
        field.setAccessible(true);
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.process(context, testClass);

        assertFalse(field.isAnnotationPresent(Mock.class));
        assertFalse(field.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class));
        assertTrue(field.isAnnotationPresent(Spy.class));
        assertTrue(field.isAnnotationPresent(Captor.class));

        new Reporter().unsupportedCombinationOfAnnotations("Spy", Spy.class.getSimpleName());
    }

    @Test
    public void testProcessWithSpyAnnotationOnFieldWithMockAnnotation() {
        Class context = getClass();
        Object testClass = mock(Object.class);
        Object fieldInstance = mock(String.class);
        Field field = createField("field", String.class, testClass);
        field.set(testClass, fieldInstance);
        field.setAccessible(true);
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.process(context, testClass);

        assertTrue(field.isAnnotationPresent(Mock.class));
        assertFalse(field.isAnnotationPresent(Spy.class));
    }

    @Test
    public void testProcessWithSpyAnnotationOnFieldWithMockitoAnnotationsMockAnnotation() {
        Class context = getClass();
        Object testClass = mock(Object.class);
        Object fieldInstance = mock(String.class);
        Field field = createField("field", String.class, testClass);
        field.set(testClass, fieldInstance);
        field.setAccessible(true);
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.process(context, testClass);

        assertFalse(field.isAnnotationPresent(Mock.class));
        assertTrue(field.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class));
        assertFalse(field.isAnnotationPresent(Spy.class));
    }

    @Test
    public void testProcessWithSpyAnnotationOnFieldWithCaptorAnnotation() {
        Class context = getClass();
        Object testClass = mock(Object.class);
        Object fieldInstance = mock(String[].class);
        Field field = createField("field", String[].class, testClass);
        field.set(testClass, fieldInstance);
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.process(context, testClass);

        assertFalse(field.isAnnotationPresent(Mock.class));
        assertFalse(field.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class));
        assertTrue(field.isAnnotationPresent(Captor.class));
        assertFalse(field.isAnnotationPresent(Spy.class));
    }

    @Test
    public void testCreateMockForNullAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object result = annotationEngine.createMockFor(null, null);
        assertNull(result);
    }

    private Field createField(String name, Class<?> type, Object obj) {
        try {
            Field field = obj.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new AssertionError("Error creating field: " + e.getMessage());
        }
    }
}