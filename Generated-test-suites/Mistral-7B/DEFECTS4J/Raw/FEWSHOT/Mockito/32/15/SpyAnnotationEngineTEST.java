package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;

public class SpyAnnotationEngineTest {

    @Test
    public void testProcess_NoSpyAnnotation_DoesNotModifyField() throws IllegalAccessException {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();

        Class context = Mockito.class;
        Object testClass = mock(Object.class);

        Field field1 = context.getDeclaredField("testField1");
        field1.setAccessible(true);
        field1.set(testClass, "testValue1");

        engine.process(context, testClass);

        // The field was not modified
        assertSame("testValue1", field1.get(testClass));
    }

    @Test
    public void testProcess_SpyAnnotationPresent_SpyInstanceIsSet() throws IllegalAccessException {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();

        Class context = Mockito.class;
        Object testClass = mock(Object.class);

        Field field1 = context.getDeclaredField("testField1");
        field1.setAccessible(true);
        field1.set(testClass, mock(String.class));

        engine.process(context, testClass);

        Object spiedInstance = mock(String.class);
        field1.set(testClass, spiedInstance);

        // The field is replaced with the spied instance
        assertSame(spiedInstance, field1.get(testClass));
    }

    @Test(expected = MockitoException.class)
    public void testProcess_SpyAnnotationPresent_InstanceIsNull() throws IllegalAccessException {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();

        Class context = Mockito.class;
        Object testClass = mock(Object.class);

        Field field1 = context.getDeclaredField("testField1");
        field1.setAccessible(true);
        field1.set(testClass, null);

        engine.process(context, testClass);

        // Throws an exception if the instance is missing
        throw new RuntimeException();
    }

    @Test(expected = MockitoException.class)
    public void testProcess_SpyAnnotationPresent_InstanceIsAMock() throws IllegalAccessException {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();

        Class context = Mockito.class;
        Object testClass = mock(Object.class);

        Field field1 = context.getDeclaredField("testField1");
        field1.setAccessible(true);
        when(field1.get(testClass)).thenReturn(mock(String.class));

        engine.process(context, testClass);

        // Throws an exception if the instance is already a mock
        throw new RuntimeException();
    }

    @Test
    public void testProcess_SpyAnnotationPresent_ResettingAMock() throws IllegalAccessException {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();

        Class context = Mockito.class;
        Object testClass = mock(Object.class);

        Field field1 = context.getDeclaredField("testField1");
        field1.setAccessible(true);
        when(field1.get(testClass)).thenReturn(mock(String.class));

        engine.process(context, testClass);

        Object mockInstance = mock(String.class);
        field1.set(testClass, mockInstance);

        engine.process(context, testClass);

        // The mock is reset
        verifyNoMoreInteractions(mockInstance);
        verifyZeroInteractions(mockInstance);
    }

    @Test
    public void testProcess_MultipleFieldsWithSpyAnnotation_AllAreProcessed() throws IllegalAccessException {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();

        Class context = Mockito.class;
        Object testClass = mock(Object.class);

        Field field1 = context.getDeclaredField("testField1");
        field1.setAccessible(true);
        field1.set(testClass, "testValue1");

        Field field2 = context.getDeclaredField("testField2");
        field2.setAccessible(true);
        field2.set(testClass, "testValue2");

        engine.process(context, testClass);

        // Both fields are processed
        Object spiedInstance1 = mock(String.class);
        assertSame(spiedInstance1, field1.get(testClass));

        Object spiedInstance2 = mock(String.class);
        assertSame(spiedInstance2, field2.get(testClass));
    }

    @Test
    public void testProcess_MultipleFieldsWithSpyAndMockAnnotations_ThrowsAnException() throws IllegalAccessException {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();

        Class context = Mockito.class;
        Object testClass = mock(Object.class);

        Field field1 = context.getDeclaredField("testField1");
        field1.setAccessible(true);
        field1.set(testClass, "testValue1");
        field1.setAnnotation(new SpyAnnotation());
        field1.setAnnotation(new MockAnnotation());

        Field field2 = context.getDeclaredField("testField2");
        field2.setAccessible(true);
        field2.set(testClass, "testValue2");
        field2.setAnnotation(new MockAnnotation());

        try {
            engine.process(context, testClass);
            throw new RuntimeException();
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test
    public void testAssertNoAnnotations_AnnotationPresent_ThrowsAnException() throws IllegalAccessException {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();

        Class context = Mockito.class;
        Object testClass = mock(Object.class);

        Field field1 = context.getDeclaredField("testField1");
        field1.setAccessible(true);
        field1.set(testClass, "testValue1");
        field1.setAnnotation(new SpyAnnotation());

        try {
            engine.assertNoAnnotations(SpyAnnotation.class, field1, MockAnnotation.class, MockitoMock.class, Captor.class);
            throw new RuntimeException();
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test
    public void testAssertNoAnnotations_NoAnnotationPresent_DoesNotThrowAnException() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();

        Class context = Mockito.class;
        Object testClass = mock(Object.class);

        Field field1 = context.getDeclaredField("testField1");
        field1.setAccessible(true);
        field1.set(testClass, "testValue1");

        try {
            engine.assertNoAnnotations(SpyAnnotation.class, field1, MockAnnotation.class, MockitoMock.class, Captor.class);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}