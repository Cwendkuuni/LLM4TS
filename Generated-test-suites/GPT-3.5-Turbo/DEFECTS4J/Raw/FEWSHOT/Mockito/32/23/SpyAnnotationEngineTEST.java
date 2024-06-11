
package org.mockito.internal.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);

        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        assertNull(result);
    }

    @Test
    public void testProcess_withNonNullInstance() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        Field field = mock(Field.class);
        Spy spyAnnotation = mock(Spy.class);

        when(field.isAnnotationPresent(eq(spyAnnotation.getClass()))).thenReturn(true);
        when(field.get(testClass)).thenReturn(new Object());
        when(field.isAccessible()).thenReturn(true);

        spyAnnotationEngine.process(context, testClass);

        verify(field, times(1)).isAnnotationPresent(eq(spyAnnotation.getClass()));
        verify(field, times(1)).get(testClass);
        verify(field, times(1)).isAccessible();
        verify(field, times(1)).setAccessible(eq(true));
        verifyNoMoreInteractions(field);
    }

    @Test(expected = MockitoException.class)
    public void testProcess_withNullInstance() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        Field field = mock(Field.class);
        Spy spyAnnotation = mock(Spy.class);

        when(field.isAnnotationPresent(eq(spyAnnotation.getClass()))).thenReturn(true);
        when(field.get(testClass)).thenReturn(null);
        when(field.getName()).thenReturn("field");

        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void testProcess_withMockInstance() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        Field field = mock(Field.class);
        Spy spyAnnotation = mock(Spy.class);
        Object instance = mock(Object.class);

        when(field.isAnnotationPresent(eq(spyAnnotation.getClass()))).thenReturn(true);
        when(field.get(testClass)).thenReturn(instance);
        when(field.isAccessible()).thenReturn(true);
        when(new MockUtil().isMock(instance)).thenReturn(true);

        spyAnnotationEngine.process(context, testClass);

        verify(field, times(1)).isAnnotationPresent(eq(spyAnnotation.getClass()));
        verify(field, times(1)).get(testClass);
        verify(field, times(1)).isAccessible();
        verify(field, times(1)).setAccessible(eq(true));
        verify(instance, times(1)).resetMock();
        verifyNoMoreInteractions(field, instance);
    }

    @Test
    public void testProcess_withNonMockInstance() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        Field field = mock(Field.class);
        Spy spyAnnotation = mock(Spy.class);
        Object instance = new Object();

        when(field.isAnnotationPresent(eq(spyAnnotation.getClass()))).thenReturn(true);
        when(field.get(testClass)).thenReturn(instance);
        when(field.isAccessible()).thenReturn(true);
        when(new MockUtil().isMock(instance)).thenReturn(false);

        spyAnnotationEngine.process(context, testClass);

        verify(field, times(1)).isAnnotationPresent(eq(spyAnnotation.getClass()));
        verify(field, times(1)).get(testClass);
        verify(field, times(1)).isAccessible();
        verify(field, times(1)).setAccessible(eq(true));
        verify(field, times(1)).set(testClass, eq(Mockito.spy(instance)));
        verifyNoMoreInteractions(field);
    }

    @Test
    public void testAssertNoAnnotations() throws Exception {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = mock(Field.class);
        Mock mockAnnotation = mock(Mock.class);
        org.mockito.MockitoAnnotations.Mock deprecatedMockAnnotation = mock(org.mockito.MockitoAnnotations.Mock.class);
        Captor captorAnnotation = mock(Captor.class);

        when(field.isAnnotationPresent(eq(mockAnnotation.getClass()))).thenReturn(true);
        when(field.isAnnotationPresent(eq(deprecatedMockAnnotation.getClass()))).thenReturn(true);
        when(field.isAnnotationPresent(eq(captorAnnotation.getClass()))).thenReturn(false);
        
        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, mockAnnotation.getClass(), deprecatedMockAnnotation.getClass(), captorAnnotation.getClass());

        verify(field, times(1)).isAnnotationPresent(eq(mockAnnotation.getClass()));
        verify(field, times(1)).isAnnotationPresent(eq(deprecatedMockAnnotation.getClass()));
        verify(field, times(1)).isAnnotationPresent(eq(captorAnnotation.getClass()));
        verifyNoMoreInteractions(field);
    }

    @Test(expected = MockitoException.class)
    public void testAssertNoAnnotations_withInvalidAnnotations() throws Exception {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = mock(Field.class);
        Mock mockAnnotation = mock(Mock.class);
        org.mockito.MockitoAnnotations.Mock deprecatedMockAnnotation = mock(org.mockito.MockitoAnnotations.Mock.class);
        Captor captorAnnotation = mock(Captor.class);

        when(field.isAnnotationPresent(eq(mockAnnotation.getClass()))).thenReturn(true);
        when(field.isAnnotationPresent(eq(deprecatedMockAnnotation.getClass()))).thenReturn(true);
        when(field.isAnnotationPresent(eq(captorAnnotation.getClass()))).thenReturn(true);
        
        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, mockAnnotation.getClass(), deprecatedMockAnnotation.getClass(), captorAnnotation.getClass());
    }
}
