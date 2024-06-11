import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;
    private Object testClass;

    @Before
    public void setUp() {
        spyAnnotationEngine = new SpyAnnotationEngine();
        testClass = new Object();
    }

    @Test
    public void createMockFor_shouldReturnNull() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);

        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        assertNull(result);
    }

    @Test
    public void process_shouldResetMockInstance_ifInstanceIsMock() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Field field = mock(Field.class);

        Object instance = mock(Object.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAccessible()).thenReturn(true);
        when(field.get(testClass)).thenReturn(instance);
        when(instance.getClass()).thenReturn(Object.class);
        when(new MockUtil().isMock(instance)).thenReturn(true);

        spyAnnotationEngine.process(context, testClass);

        verify(Mockito, times(1)).reset(instance);
    }

    @Test
    public void process_shouldSpyField_ifInstanceIsNull() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Field field = mock(Field.class);

        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAccessible()).thenReturn(true);
        when(field.get(testClass)).thenReturn(null);

        spyAnnotationEngine.process(context, testClass);

        verify(field, times(1)).set(any(), any());
    }

    @Test(expected = MockitoException.class)
    public void process_shouldThrowException_ifInstanceIsNull() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Field field = mock(Field.class);

        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAccessible()).thenReturn(true);
        when(field.get(testClass)).thenReturn(null);

        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void process_shouldSpyField_ifInstanceIsNotMock() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Field field = mock(Field.class);

        Object instance = new LinkedList();
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAccessible()).thenReturn(true);
        when(field.get(testClass)).thenReturn(instance);
        when(new MockUtil().isMock(instance)).thenReturn(false);

        spyAnnotationEngine.process(context, testClass);

        verify(field, times(1)).set(any(), any());
    }

    @Test(expected = MockitoException.class)
    public void process_shouldThrowException_ifIllegalAccessExceptionOccurs() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Field field = mock(Field.class);

        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAccessible()).thenReturn(true);
        when(field.get(testClass)).thenThrow(IllegalAccessException.class);

        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void assertNoAnnotations_shouldDoNothing_ifNoUndesiredAnnotationsPresent() throws NoSuchFieldException {
        Class annotation = Spy.class;
        Field field = SpyAnnotationEngine.class.getDeclaredField("testClass");

        spyAnnotationEngine.assertNoAnnotations(annotation, field);
    }

    @Test(expected = Reporter.class)
    public void assertNoAnnotations_shouldThrowException_ifUndesiredAnnotationsPresent() throws NoSuchFieldException {
        Class annotation = Spy.class;
        Field field = SpyAnnotationEngine.class.getDeclaredField("testClass");

        spyAnnotationEngine.assertNoAnnotations(annotation, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
    }
}