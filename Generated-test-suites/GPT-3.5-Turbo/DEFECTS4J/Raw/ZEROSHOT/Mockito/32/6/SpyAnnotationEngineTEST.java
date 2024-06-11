import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    @Mock
    private Object testClass;

    @Captor
    private Object testCaptor;

    @Spy
    private List<String> testList;

    private SpyAnnotationEngine spyAnnotationEngine;

    @Test
    public void testCreateMockFor() {
        Annotation mockAnnotation = mock(Annotation.class);
        Field mockField = mock(Field.class);

        Object result = spyAnnotationEngine.createMockFor(mockAnnotation, mockField);

        assertNull(result);
    }

    @Test
    public void testProcess() throws IllegalAccessException {
        Field mockField = mock(Field.class);
        Object mockInstance = mock(Object.class);

        when(mockField.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(mockField.get(testClass)).thenReturn(mockInstance);
        when(mockInstance == null).thenReturn(false);
        when(mockInstance.getClass()).thenReturn(List.class);
        when(new MockUtil().isMock(mockInstance)).thenReturn(false);

        spyAnnotationEngine.process(testClass.getClass(), testClass);

        verify(mockField).setAccessible(true);
        verify(mockField).get(testClass);
        verify(mockField).set(testClass, Mockito.spy(mockInstance));
        verify(mockField).setAccessible(false);
    }

    @Test(expected = MockitoException.class)
    public void testProcess_NullInstance() throws IllegalAccessException {
        Field mockField = mock(Field.class);

        when(mockField.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(mockField.get(testClass)).thenReturn(null);

        spyAnnotationEngine.process(testClass.getClass(), testClass);
    }

    @Test
    public void testProcess_MockedInstance() throws IllegalAccessException {
        Field mockField = mock(Field.class);
        List<String> mockInstance = mock(List.class);

        when(mockField.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(mockField.get(testClass)).thenReturn(mockInstance);
        when(new MockUtil().isMock(mockInstance)).thenReturn(true);

        spyAnnotationEngine.process(testClass.getClass(), testClass);

        verify(mockInstance).reset();
    }

    @Test
    public void testAssertNoAnnotations() throws NoSuchFieldException {
        Field mockField = mock(Field.class);
        Annotation mockAnnotation = mock(Annotation.class);
        Class<?> mockClass = mock(Class.class);

        when(mockField.isAnnotationPresent(Mock.class)).thenReturn(true);
        when(mockAnnotation.getClass()).thenReturn(mockClass);
        when(mockClass.getSimpleName()).thenReturn("Mock");
        doNothing().when(Reporter.class);
        Reporter.unsupportedCombinationOfAnnotations("Spy", "Mock");

        spyAnnotationEngine.assertNoAnnotations(Spy.class, mockField, Mock.class);

        verify(mockField).isAnnotationPresent(Mock.class);
        verify(mockAnnotation).getClass();
        verify(mockClass).getSimpleName();
        verify(Reporter.class);
        Reporter.unsupportedCombinationOfAnnotations("Spy", "Mock");
    }
}