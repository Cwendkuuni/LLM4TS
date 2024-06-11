import org.junit.Test;
import org.junit.Before;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setUp() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void createMockFor_shouldReturnNull() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);

        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        assertNull(result);
    }

    @Test(expected = MockitoException.class)
    public void process_shouldThrowExceptionIfInstanceIsNull() {
        Class<?> context = getClass();
        Object testClass = new Object();
        Field field = getField("mockListField");

        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void process_shouldResetMockIfInstanceIsAlreadySpied() throws IllegalAccessException {
        Class<?> context = getClass();
        Object testClass = new Object();
        Field field = getField("spiedListField");
        List<String> instance = spy(new LinkedList<>());
        field.setAccessible(true);
        field.set(testClass, instance);

        spyAnnotationEngine.process(context, testClass);

        verifyZeroInteractions(instance);
    }

    @Test
    public void process_shouldSpyInstanceIfNotAlreadySpied() throws IllegalAccessException {
        Class<?> context = getClass();
        Object testClass = new Object();
        Field field = getField("spiedListField");
        List<String> instance = new LinkedList<>();
        field.setAccessible(true);
        field.set(testClass, instance);

        spyAnnotationEngine.process(context, testClass);

        assertTrue(instance instanceof List);
        verifyZeroInteractions(instance);
        verifyNoMoreInteractions(instance);
        verify(field).set(testClass, instance);
    }

    @Test(expected = MockitoException.class)
    public void process_shouldThrowExceptionIfIllegalAccessExceptionOccurs() throws IllegalAccessException {
        Class<?> context = getClass();
        Object testClass = new Object();
        Field field = getField("spiedListField");
        List<String> instance = new LinkedList<>();
        field.setAccessible(true);
        field.set(testClass, instance);
        when(field.get(testClass)).thenThrow(new IllegalAccessException());

        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void assertNoAnnotations_shouldDoNothingIfNoUndesiredAnnotationsPresent() throws NoSuchFieldException {
        Field field = getField("mockListField");

        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, Reporter.class);

        verify(field).isAnnotationPresent(Mock.class);
        verify(field).isAnnotationPresent(Reporter.class);
    }

    @Test(expected = Reporter.class)
    public void assertNoAnnotations_shouldThrowReporterExceptionIfUndesiredAnnotationsPresent() throws NoSuchFieldException {
        Field field = getField("mockListField");
        when(field.isAnnotationPresent(Mock.class)).thenReturn(true);
        when(field.isAnnotationPresent(Reporter.class)).thenReturn(true);

        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, Reporter.class);
    }

    private Field getField(String name) {
        try {
            return SpyAnnotationEngine.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }
}