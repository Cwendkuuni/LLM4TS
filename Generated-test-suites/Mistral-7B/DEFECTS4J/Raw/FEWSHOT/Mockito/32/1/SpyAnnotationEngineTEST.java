package org.mockito.internal.configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.doThrow;
import static org.mockito.MockitoAnnotations.initMocks;

import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.exceptions.base.MockitoException;

public class SpyAnnotationEngineTest {

    @Test
    public void testProcess_NoSpyAnnotationPresent() {
        Class<?> context = Mockito.class;
        Object testClass = new Object();
        new SpyAnnotationEngine().process(context, testClass);
        verifyZeroInteractions(testClass);
    }

    @Test
    public void testProcess_SpyAnnotationPresent_MockInstanceNotPresent() {
        Class<?> context = Mockito.class;
        Object testClass = new Object();
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.getName()).thenReturn("spyField");
        when(field.get(testClass)).thenReturn(null);

        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.process(context, testClass);

        verify(field).setAccessible(true);
        verify(field).setAccessible(true);
        verify(testClass).equals(testClass);
        verify(field).get(testClass);
        verify(testClass).equals(testClass);
        verify(field).set(testClass, isA(Mockito.spy(any())));
        verifyNoMoreInteractions(testClass);
    }

    @Test
    public void testProcess_SpyAnnotationPresent_MockInstanceAlreadyPresent() {
        Class<?> context = Mockito.class;
        Object testClass = new Object();
        Object instanceToSpy = mock(Object.class);
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.getName()).thenReturn("spyField");
        when(field.get(testClass)).thenReturn(instanceToSpy);
        when(new MockUtil().isMock(instanceToSpy)).thenReturn(true);

        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.process(context, testClass);

        verify(field).setAccessible(true);
        verify(field).setAccessible(true);
        verify(testClass).equals(testClass);
        verify(field).get(testClass);
        verify(field).set(testClass, isA(instanceToSpy));
        verify(instanceToSpy).equals(instanceToSpy);
        verify(instanceToSpy).equals(instanceToSpy);
        verify(testClass).equals(testClass);
        verify(field).set(testClass, isA(instanceToSpy));
        verifyNoMoreInteractions(instanceToSpy);
    }

    @Test(expected = MockitoException.class)
    public void testProcess_SpyAnnotationPresent_InstanceMissing() {
        Class<?> context = Mockito.class;
        Object testClass = new Object();
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.getName()).thenReturn("spyField");

        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.process(context, testClass);

        verify(field).setAccessible(true);
        verify(field).setAccessible(true);
        verify(testClass).equals(testClass);
        verify(field).get(testClass);
        verify(testClass).equals(testClass);

        String errorMessage = "Cannot create a @Spy for 'spyField' field because the *instance* is missing\n" +
                "The instance must be created *before* initMocks();\n" +
                "Example of correct usage of @Spy:\n" +
                "   @Spy List mock = new LinkedList();\n" +
                "   //also, don't forget about MockitoAnnotations.initMocks();";

        verifyErrorMessage(errorMessage, annotationEngine);
    }

    @Test(expected = MockitoException.class)
    public void testProcess_SpyAnnotationPresent_IsMockAlreadySet() {
        Class<?> context = Mockito.class;
        Object testClass = new Object();
        Object instanceToSpy = mock(Object.class);
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.getName()).thenReturn("spyField");
        when(field.get(testClass)).thenReturn(instanceToSpy);
        when(new MockUtil().isMock(instanceToSpy)).thenReturn(true);

        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.process(context, testClass);

        verify(field).setAccessible(true);
        verify(field).setAccessible(true);
        verify(testClass).equals(testClass);
        verify(field).get(testClass);
        verify(field).set(testClass, isA(instanceToSpy));
        verify(instanceToSpy).equals(instanceToSpy);
        verify(instanceToSpy).equals(instanceToSpy);
        verify(testClass).equals(testClass);
        verify(field).set(testClass, isA(instanceToSpy));

        String errorMessage = "Cannot create a @Spy for 'spyField' field because the instance is already a mock";

        verifyErrorMessage(errorMessage, annotationEngine);
    }

    @Test(expected = IllegalAccessException.class)
    public void testProcess_SpyAnnotationPresent_IllegalAccessException() {
        Class<?> context = Mockito.class;
        Object testClass = new Object();
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.getName()).thenReturn("spyField");

        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        doThrow(new IllegalAccessException("Illegal access")).when(field).get(testClass);

        annotationEngine.process(context, testClass);

        verify(field).setAccessible(true);
        verify(field).setAccessible(true);
        verify(testClass).equals(testClass);
        verify(field).get(testClass);
        verify(field).setAccessible(true);
        verify(field).setAccessible(true);

        String errorMessage = "Problems initiating spied field spyField";

        verifyErrorMessage(errorMessage, annotationEngine);
    }

    @Test
    public void testAssertNoAnnotations() {
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAnnotationPresent(Mockito.MockitoAnnotations.Mock.class)).thenReturn(true);
        when(field.isAnnotationPresent(Captor.class)).thenReturn(true);

        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.assertNoAnnotations(Spy.class, field, Mockito.MockitoAnnotations.Mock.class, Captor.class);

        verify(field).isAnnotationPresent(Spy.class);
        verify(field).isAnnotationPresent(Mockito.MockitoAnnotations.Mock.class);
        verify(field).isAnnotationPresent(Captor.class);

        new Reporter().unsupportedCombinationOfAnnotations("Spy", "Spy");

        verify(field).isAnnotationPresent(Spy.class);
        verify(field).isAnnotationPresent(Mockito.MockitoAnnotations.Mock.class);
        verify(field).isAnnotationPresent(Captor.class);
    }

    private void verifyErrorMessage(String errorMessage, SpyAnnotationEngine annotationEngine) {
        verify(annotationEngine).throwNewMockitoException(errorMessage);
    }
}