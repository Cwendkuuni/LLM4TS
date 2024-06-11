package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.configuration.AnnotationEngine;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.exceptions.Reporter;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

    @Test
    public void testProcess_noSpyAnnotation() {
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);

        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.process(context, testClass);

        verifyZeroInteractions(context);
        verifyZeroInteractions(testClass);
    }

    @Test
    public void testProcess_emptyField() {
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field emptyField = mock(Field.class);

        when(emptyField.getName()).thenReturn("emptyField");
        when(emptyField.isAnnotationPresent(Spy.class)).thenReturn(false);

        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.process(context, testClass);

        verifyNoMoreInteractions(emptyField);
        verifyZeroInteractions(context);
        verifyZeroInteractions(testClass);
    }

    @Test
    public void testProcess_existingSpyAnnotation() {
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field fieldToSpy = mock(Field.class);
        Object instanceToSpy = mock(Object.class);

        when(fieldToSpy.getName()).thenReturn("fieldToSpy");
        when(fieldToSpy.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(fieldToSpy.isAccessible()).thenReturn(true);
        when(fieldToSpy.get(testClass)).thenReturn(instanceToSpy);
        when(new MockUtil().isMock(instanceToSpy)).thenReturn(false);

        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.process(context, testClass);

        verify(fieldToSpy).set(testClass, spy(instanceToSpy));
        verifyZeroInteractions(context);
        verifyZeroInteractions(instanceToSpy);
    }

    @Test
    public void testProcess_existingMockedAnnotation() {
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field fieldToSpy = mock(Field.class);
        Object instanceToSpy = mock(Object.class);

        when(fieldToSpy.getName()).thenReturn("fieldToSpy");
        when(fieldToSpy.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(fieldToSpy.isAccessible()).thenReturn(true);
        when(fieldToSpy.get(testClass)).thenReturn(instanceToSpy);
        when(new MockUtil().isMock(instanceToSpy)).thenReturn(true);

        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.process(context, testClass);

        verify(fieldToSpy).set(testClass, Mockito.reset(instanceToSpy));
        verifyZeroInteractions(context);
        verifyZeroInteractions(instanceToSpy);
    }

    @Test
    public void testProcess_nullInstanceToSpy() {
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field fieldToSpy = mock(Field.class);

        when(fieldToSpy.getName()).thenReturn("fieldToSpy");
        when(fieldToSpy.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(fieldToSpy.isAccessible()).thenReturn(true);

        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.process(context, testClass);

        verifyZeroInteractions(fieldToSpy);
        verifyZeroInteractions(context);
        verifyZeroInteractions(testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcess_missingInstanceToSpy() {
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field fieldToSpy = mock(Field.class);

        when(fieldToSpy.getName()).thenReturn("fieldToSpy");
        when(fieldToSpy.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(fieldToSpy.isAccessible()).thenReturn(true);

        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.process(context, testClass);

        verifyZeroInteractions(fieldToSpy);
        verifyZeroInteractions(context);
        verifyZeroInteractions(testClass);
    }

    @Test
    public void testProcess_multipleSpyAnnotations() {
        Class context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field field1ToSpy = mock(Field.class);
        Field field2ToSpy = mock(Field.class);
        Object instance1ToSpy = mock(Object.class);
        Object instance2ToSpy = mock(Object.class);

        when(field1ToSpy.getName()).thenReturn("field1ToSpy");
        when(field1ToSpy.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field1ToSpy.isAccessible()).thenReturn(true);
        when(field1ToSpy.get(testClass)).thenReturn(instance1ToSpy);
        when(new MockUtil().isMock(instance1ToSpy)).thenReturn(false);

        when(field2ToSpy.getName()).thenReturn("field2ToSpy");
        when(field2ToSpy.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field2ToSpy.isAccessible()).thenReturn(true);
        when(field2ToSpy.get(testClass)).thenReturn(instance2ToSpy);
        when(new MockUtil().isMock(instance2ToSpy)).thenReturn(false);

        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        annotationEngine.process(context, testClass);

        verify(field1ToSpy).set(testClass, spy(instance1ToSpy));
        verify(field2ToSpy).set(testClass, spy(instance2ToSpy));
        verifyZeroInteractions(context);
        verifyZeroInteractions(instance1ToSpy);
        verifyZeroInteractions(instance2ToSpy);
    }

    @Test
    public void testAssertNoAnnotations_noUndesiredAnnotations() {
        Field field = mock(Field.class);
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class);

        verifyZeroInteractions(field);
    }

    @Test
    public void testAssertNoAnnotations_noSpyAnnotation() {
        Field field = mock(Field.class);
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        verifyZeroInteractions(field);
    }

    @Test
    public void testAssertNoAnnotations_SpyAnnotationPresent() {
        Field field = mock(Field.class);
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);

        annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        verify(field).isAnnotationPresent(Spy.class);
        verify(new Reporter()).unsupportedCombinationOfAnnotations("Spy", Spy.class.getSimpleName());
    }

    @Test
    public void testAssertNoAnnotations_MockAnnotationPresent() {
        Field field = mock(Field.class);
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        when(field.isAnnotationPresent(Mock.class)).thenReturn(true);

        annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        verify(field).isAnnotationPresent(Mock.class);
        verify(new Reporter()).unsupportedCombinationOfAnnotations("Spy", Spy.class.getSimpleName());
    }

    @Test
    public void testAssertNoAnnotations_CaptorAnnotationPresent() {
        Field field = mock(Field.class);
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        when(field.isAnnotationPresent(Captor.class)).thenReturn(true);

        annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        verify(field).isAnnotationPresent(Captor.class);
        verify(new Reporter()).unsupportedCombinationOfAnnotations("Spy", Spy.class.getSimpleName());
    }
}