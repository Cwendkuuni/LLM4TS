
package org.mockito.internal.configuration;

import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.Mockito;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);

        assertNull(spyAnnotationEngine.createMockFor(annotation, field));
    }

    @Test
    public void testProcess() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new SpyAnnotationEngine();
        Field[] fields = context.getDeclaredFields();
        Field field = fields[5];
        field.set(testClass, null);

        spyAnnotationEngine.process(context, testClass);

        assertNull(field.get(testClass));
    }

    @Test(expected = MockitoException.class)
    public void testProcess_missingInstance() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new SpyAnnotationEngine();
        Field[] fields = context.getDeclaredFields();
        Field field = fields[5];

        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void testProcess_mockInstance() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new SpyAnnotationEngine();
        Field[] fields = context.getDeclaredFields();
        Field field = fields[5];
        field.set(testClass, mock(List.class));

        spyAnnotationEngine.process(context, testClass);

        verifyZeroInteractions(field.get(testClass));
    }

    @Test
    public void testProcess_spiedInstance() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new SpyAnnotationEngine();
        Field[] fields = context.getDeclaredFields();
        Field field = fields[5];
        field.set(testClass, spy(new LinkedList()));

        spyAnnotationEngine.process(context, testClass);

        verify(field.get(testClass), times(1)).clear();
    }

    @Test(expected = MockitoException.class)
    public void testProcess_illegalAccessException() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new SpyAnnotationEngine();
        Field[] fields = context.getDeclaredFields();
        Field field = fields[0];
        field.setAccessible(false);

        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void testAssertNoAnnotations() throws NoSuchFieldException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = SpyAnnotationEngine.class.getDeclaredField("assertNoAnnotations");
        field.setAccessible(true);

        assertNull(field.get(spyAnnotationEngine));
    }
}
