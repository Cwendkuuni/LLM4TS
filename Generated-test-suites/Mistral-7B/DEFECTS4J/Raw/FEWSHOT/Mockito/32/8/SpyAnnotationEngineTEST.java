package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

public class SpyAnnotationEngineTest {

    @Test
    public void testProcess_NoSpyAnnotation_ShouldDoNothing() {
        Class context = Object.class;
        Object testClass = new Object();
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.process(context, testClass);
        verify(testClass, never()).getClass();
    }

    @Test
    public void testProcess_SpyAnnotationPresent_ShouldSpyInstance() throws IllegalAccessException {
        Class context = Object.class;
        Object testClass = new Object() {
            @Spy
            Object instanceToSpy = new Object();
        };
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.process(context, testClass);
        Field field = context.getDeclaredField("instanceToSpy");
        field.setAccessible(true);
        Object spiedInstance = field.get(testClass);
        assertNotNull(spiedInstance);
        verify(spiedInstance).getClass();
        assertFalse(new MockUtil().isMock(spiedInstance));
        assertSame(spiedInstance, spyAnnotationEngine.createMockFor(Spy.class, field));
    }

    @Test
    public void testProcess_SpyAnnotationPresent_ShouldResetMockIfAlreadySpyed() throws IllegalAccessException {
        Class context = Object.class;
        Object testClass = new Object() {
            @Spy
            Object instanceToMock = mock(Object.class);
            @Spy
            Object instanceToSpy = new Object();
        };
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.process(context, testClass);
        Field field = context.getDeclaredField("instanceToSpy");
        field.setAccessible(true);
        Object spiedInstance = field.get(testClass);
        assertNotNull(spiedInstance);
        verify(spiedInstance).getClass();
        assertFalse(new MockUtil().isMock(spiedInstance));
        reset(spiedInstance);
        verify(spiedInstance).getClass();
    }

    @Test
    public void testProcess_SpyAnnotationPresent_ShouldThrowExceptionIfInstanceIsMissing() throws IllegalAccessException {
        Class context = Object.class;
        Object testClass = new Object();
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        assertThrows(MockitoException.class, () -> spyAnnotationEngine.process(context, testClass));
    }

    @Test
    public void testProcess_SpyAnnotationPresent_ShouldThrowExceptionIfInstanceIsAMock() throws IllegalAccessException {
        Class context = Object.class;
        Object testClass = new Object() {
            @Mock
            Object instanceToMock = mock(Object.class);
            @Spy
            Object instanceToSpy = new Object();
        };
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        assertThrows(MockitoException.class, () -> spyAnnotationEngine.process(context, testClass));
    }

    @Test
    public void testProcess_SpyAnnotationPresent_ShouldThrowExceptionIfInstanceIsASpy() throws IllegalAccessException {
        Class context = Object.class;
        Object testClass = new Object() {
            @Spy
            Object instanceToSpy = spy(new Object());
            @Spy
            Object instanceToSpyAgain = spy(instanceToSpy);
        };
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        assertThrows(MockitoException.class, () -> spyAnnotationEngine.process(context, testClass));
    }

    @Test
    public void testProcess_SpyAnnotationAndMockAnnotationPresent_ShouldThrowException() throws IllegalAccessException {
        Class context = Object.class;
        Object testClass = new Object() {
            @Spy
            @Mock
            Object instanceToMockAndSpy = mock(Object.class);
        };
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        assertThrows(MockitoException.class, () -> spyAnnotationEngine.process(context, testClass));
    }

    @Test
    public void testProcess_SpyAnnotationAndCaptorAnnotationPresent_ShouldDoNothing() throws IllegalAccessException {
        Class context = Object.class;
        Object testClass = new Object() {
            @Spy
            @Captor
            ArgumentCaptor<Object> captor;
        };
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.process(context, testClass);
        verify(testClass, never()).getClass();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateMockFor_NotImplemented() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.createMockFor(Mockito.class, null);
    }
}