package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.InvocationOnMockException;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;

public class SpyAnnotationEngineTest {

    @Mock
    private Annotation annotation;

    @Mock
    private Field field;

    @Mock
    private Object testClass;

    @Spy
    private SpyAnnotationEngine engine;

    @Captor
    private FieldCaptor<Object> fieldCaptor;

    @Before
    public void setup() {
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.get(testClass)).thenReturn(mock(Object.class));
    }

    @Test
    public void testCreateMockFor() {
        assertNull(engine.createMockFor(annotation, field));
    }

    @Test
    public void testProcessNullContext() {
        engine.process(null, testClass);
    }

    @Test
    public void testProcessNullTestClass() {
        engine.process(testClass.getClass(), null);
    }

    @Test
    public void testProcessNoAnnotation() {
        when(field.isAnnotationPresent(Spy.class)).thenReturn(false);
        engine.process(testClass.getClass(), testClass);
        verifyZeroInteractions(field);
    }

    @Test
    public void testProcessSpyAnnotation() {
        engine.process(testClass.getClass(), testClass);
        Object instance = field.get(testClass);
        assertNotNull(instance);
        assertFalse(Mockito.isMock(instance));
        verify(field).set(testClass, engine.spy(instance));
    }

    @Test(expected = MockitoException.class)
    public void testProcessMissingInstance() {
        engine.process(testClass.getClass(), testClass);
        when(field.get(testClass)).thenReturn(null);
        engine.process(testClass.getClass(), testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcessDuplicateSpyAnnotation() {
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        engine.process(testClass.getClass(), testClass);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        engine.process(testClass.getClass(), testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcessAnnotationWithMocks() {
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAnnotationPresent(Mock.class)).thenReturn(true);
        engine.process(testClass.getClass(), testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcessAnnotationWithCaptors() {
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAnnotationPresent(Captor.class)).thenReturn(true);
        engine.process(testClass.getClass(), testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcessAnnotationWithOtherAnnotations() {
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAnnotationPresent(Annotation.class)).thenReturn(true);
        engine.process(testClass.getClass(), testClass);
    }

    @Test
    public void testResetSpyAfterCreation() {
        Object instance = mock(Object.class);
        when(field.get(testClass)).thenReturn(instance);
        engine.process(testClass.getClass(), testClass);
        engine.reset(instance);
        verify(instance).reset();
    }
}