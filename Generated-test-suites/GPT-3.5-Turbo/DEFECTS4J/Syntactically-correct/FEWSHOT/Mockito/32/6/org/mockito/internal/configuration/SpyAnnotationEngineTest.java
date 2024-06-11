package org.mockito.internal.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;
import org.junit.Test;

public class SpyAnnotationEngineTest {
    @Test
    public void testCreateMockFor() {
        Annotation annotation = new Spy();
        Field field = new Field();
        Object expectedResult = null;
        Object actualResult = new SpyAnnotationEngine().createMockFor(annotation, field);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testProcess() {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        Field[] fields = new Field[] { new Field() };
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        MockUtil mockUtil = mock(MockUtil.class);
        MockitoAnnotations mockitoAnnotations = mock(MockitoAnnotations.class);
        Mockito.reset(mockUtil);
        Mockito.reset(mockitoAnnotations);
        when(mockitoAnnotations.initMocks()).thenReturn(null);
        when(context.getDeclaredFields()).thenReturn(fields);
        when(fields[0].isAnnotationPresent(Spy.class)).thenReturn(true);
        when(mockUtil.isMock(anyObject())).thenReturn(true);
        spyAnnotationEngine.process(context, testClass);
        verify(fields[0]).isAnnotationPresent(any(Class.class));
        verify(fields[0]).setAccessible(true);
        verify(fields[0]).get(testClass);
        verify(mockUtil).isMock(anyObject());
        verify(fields[0]).set(testClass, Mockito.spy(anyObject()));
        verify(fields[0]).setAccessible(false);
        verifyNoMoreInteractions(fields[0]);
        verifyNoMoreInteractions(mockUtil);
        verifyNoMoreInteractions(mockitoAnnotations);
    }
    
    @Test
    public void testProcess_nullInstance() {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        Field[] fields = new Field[] { new Field() };
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        MockUtil mockUtil = mock(MockUtil.class);
        MockitoAnnotations mockitoAnnotations = mock(MockitoAnnotations.class);
        Mockito.reset(mockUtil);
        Mockito.reset(mockitoAnnotations);
        when(mockitoAnnotations.initMocks()).thenReturn(null);
        when(context.getDeclaredFields()).thenReturn(fields);
        when(fields[0].isAnnotationPresent(Spy.class)).thenReturn(true);
        when(mockUtil.isMock(anyObject())).thenReturn(false);
        spyAnnotationEngine.process(context, testClass);
        verify(fields[0]).isAnnotationPresent(any(Class.class));
        verify(fields[0]).setAccessible(true);
        verify(fields[0]).get(testClass);
        verify(mockUtil).isMock(anyObject());
        verify(fields[0], never()).set(testClass, Mockito.spy(anyObject()));
        verify(fields[0]).setAccessible(false);
        verifyNoMoreInteractions(fields[0]);
        verifyNoMoreInteractions(mockUtil);
        verifyNoMoreInteractions(mockitoAnnotations);
    }
}