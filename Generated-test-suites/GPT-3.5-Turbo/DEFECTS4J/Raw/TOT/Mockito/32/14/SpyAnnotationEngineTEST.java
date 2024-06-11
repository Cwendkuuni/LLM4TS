import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;
    private Class<?> context;
    private Object testClass;

    @Before
    public void setUp() {
        spyAnnotationEngine = new SpyAnnotationEngine();
        context = SpyAnnotationEngine.class;
        testClass = new Object();
    }

    @Test
    public void createMockFor_NullReturned() {
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);

        assertNull(spyAnnotationEngine.createMockFor(annotation, field));
    }

    @Test
    public void process_FieldNotAnnotatedWithSpy() {
        Field field = Mockito.mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(false);

        spyAnnotationEngine.process(context, testClass);

        // Since the field is not annotated with @Spy, no operations should be performed on the field
        verify(field, never()).isAccessible();
        verify(field, never()).setAccessible(anyBoolean());
        verify(field, never()).get(testClass);
        verify(field, never()).set(testClass, anyObject());
    }

    @Test
    public void process_FieldAnnotatedWithSpy_InstanceNull() {
        Field field = Mockito.mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.getName()).thenReturn("mockField");
        when(field.get(testClass)).thenReturn(null);

        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'mockField' field because the *instance* is missing\n" +
                            "The instance must be created *before* initMocks();\n" +
                            "Example of correct usage of @Spy:\n" +
                            "   @Spy List mock = new LinkedList();\n" +
                            "   //also, don't forget about MockitoAnnotations.initMocks();",
                    e.getMessage());
        }

        // Verify that no further operations were performed on the field
        verify(field, never()).isAccessible();
        verify(field, never()).setAccessible(anyBoolean());
        verify(field).get(testClass);
        verify(field, never()).set(testClass, anyObject());
    }

    @Test
    public void process_FieldAnnotatedWithSpy_InstanceMock() throws IllegalAccessException {
        Field field = Mockito.mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.getName()).thenReturn("mockField");

        // Create a mock instance
        LinkedList<?> mockInstance = Mockito.mock(LinkedList.class);
        when(field.get(testClass)).thenReturn(mockInstance);

        spyAnnotationEngine.process(context, testClass);

        // Verify that the mock instance was reset
        verify(mockInstance).reset();

        // Verify that no further operations were performed on the field
        verify(field, never()).isAccessible();
        verify(field, never()).setAccessible(anyBoolean());
        verify(field).get(testClass);
        verify(field, never()).set(testClass, anyObject());
    }

    @Test
    public void process_FieldAnnotatedWithSpy_InstanceNotMock() throws IllegalAccessException {
        Field field = Mockito.mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.getName()).thenReturn("mockField");

        // Create a non-mock instance
        LinkedList<?> nonMockInstance = new LinkedList<>();
        when(field.get(testClass)).thenReturn(nonMockInstance);

        spyAnnotationEngine.process(context, testClass);

        // Verify that the field was spied on
        verify(field).set(testClass, Mockito.spy(nonMockInstance));

        // Verify that no further operations were performed on the field
        verify(field, never()).isAccessible();
        verify(field, never()).setAccessible(anyBoolean());
        verify(field).get(testClass);
    }

    @Test
    public void process_FieldAnnotatedWithSpy_IllegalAccessException() throws IllegalAccessException {
        Field field = Mockito.mock(Field.class);
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.getName()).thenReturn("mockField");
        when(field.get(testClass)).thenThrow(new IllegalAccessException("Illegal Access"));

        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Problems initiating spied field mockField", e.getMessage());
            assertEquals("Illegal Access", e.getCause().getMessage());
        }

        // Verify that no further operations were performed on the field
        verify(field, never()).isAccessible();
        verify(field, never()).setAccessible(anyBoolean());
        verify(field).get(testClass);
        verify(field, never()).set(testClass, anyObject());
    }

    @Test
    public void assertNoAnnotations_NoUndesiredAnnotations() {
        Field field = Mockito.mock(Field.class);
        Annotation annotation = Mockito.mock(Annotation.class);

        spyAnnotationEngine.assertNoAnnotations(annotation.getClass(), field);

        // Verify that no exceptions were thrown
    }

    @Test(expected = Reporter.class)
    public void assertNoAnnotations_UndesiredAnnotationsPresent() {
        Field field = Mockito.mock(Field.class);
        Annotation annotation = Mockito.mock(Annotation.class);
        Class<String> undesiredAnnotation = String.class;

        when(field.isAnnotationPresent(undesiredAnnotation)).thenReturn(true);

        spyAnnotationEngine.assertNoAnnotations(annotation.getClass(), field);
    }

}