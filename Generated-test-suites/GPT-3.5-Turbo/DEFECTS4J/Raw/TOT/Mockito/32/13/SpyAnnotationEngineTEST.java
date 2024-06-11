import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

    @Test
    public void testCreateMockFor_shouldReturnNull() {
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);
        assertNull(spyAnnotationEngine.createMockFor(annotation, field));
    }

    @Test
    public void testProcess_shouldCreateSpyAndResetMockInstance() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new SpyAnnotationEngine();

        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.get(testClass)).thenReturn(null);

        MockUtil mockUtil = mock(MockUtil.class);
        doReturn(true).when(mockUtil).isMock(null);

        reset(mockUtil);

        spyAnnotationEngine.process(context, testClass);
        
        verify(mockUtil, never()).isMock(null);
        assertNotEquals(null, testClass);
        Mockito.verify(field, Mockito.never()).set(testClass, Mockito.spy(null));
    }

    @Test
    public void testProcess_shouldCreateSpyAndResetMockInstanceIfInstanceAlreadyMocked() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new SpyAnnotationEngine();

        List testInstance = Mockito.mock(List.class);

        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.get(testClass)).thenReturn(testInstance);

        MockUtil mockUtil = mock(MockUtil.class);
        doReturn(true).when(mockUtil).isMock(testInstance);

        reset(mockUtil);

        spyAnnotationEngine.process(context, testClass);

        verify(mockUtil, never()).isMock(testInstance);
        assertNotEquals(null, testClass);
        verify(testInstance, times(1)).reset();
        Mockito.verify(field, Mockito.never()).set(testClass, Mockito.spy(null));
    }

    @Test
    public void testProcess_shouldCreateSpyAndSetMockInstance() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new SpyAnnotationEngine();

        List testInstance = new LinkedList();

        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.get(testClass)).thenReturn(testInstance);

        MockUtil mockUtil = mock(MockUtil.class);
        doReturn(false).when(mockUtil).isMock(testInstance);

        reset(mockUtil);

        spyAnnotationEngine.process(context, testClass);

        verify(mockUtil).isMock(testInstance);
        assertNotEquals(null, testClass);
        Mockito.verify(field).set(testClass, Mockito.spy(testInstance));
    }

    @Test
    public void testProcess_shouldThrowMockitoExceptionIfIllegalAccessException() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new SpyAnnotationEngine();

        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.get(testClass)).thenThrow(new IllegalAccessException());

        assertThrows(MockitoException.class, () -> spyAnnotationEngine.process(context, testClass));
    }

    @Test
    public void testProcess_shouldAssertNoAnnotations() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new SpyAnnotationEngine();

        Field field = mock(Field.class);
        Annotation annotation = Mockito.mock(Annotation.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAnnotationPresent(Mock.class)).thenReturn(true);
        when(field.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class)).thenReturn(true);
        when(field.isAnnotationPresent(Captor.class)).thenReturn(true);
        when(annotation.getClass()).thenReturn(Annotation.class);

        assertThrows(RuntimeException.class, () -> spyAnnotationEngine.process(context, testClass));

        verify(field, times(4)).isAnnotationPresent(any(Class.class));
    }

}