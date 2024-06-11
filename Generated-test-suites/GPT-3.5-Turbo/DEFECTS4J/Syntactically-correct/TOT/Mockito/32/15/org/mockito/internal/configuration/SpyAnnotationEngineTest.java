import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Mock
    private Field field;

    private Object testClass;

    @Before
    public void setup() {
        spyAnnotationEngine = new SpyAnnotationEngine();
        field = mock(Field.class);
        testClass = new Object();
    }

    @Test
    public void testCreateMockFor_SpyAnnotation_ReturnsNull() {
        Annotation annotation = mock(Spy.class);

        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        assertNull(result);
    }

    @Test
    public void testProcess_FieldWithNoAnnotation_NoActionTaken() {
        Field[] fields = new Field[1];
        fields[0] = field;

        spyAnnotationEngine.process(Object.class, testClass);

        verify(field, never()).isAnnotationPresent(Spy.class);
    }

    @Test
    public void testProcess_FieldWithSpyAnnotation_AssertNoAnnotationsCalled() {
        Field[] fields = new Field[1];
        fields[0] = field;
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAccessible()).thenReturn(false);

        spyAnnotationEngine.process(Object.class, testClass);

        verify(spyAnnotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
    }

    @Test
    public void testProcess_FieldWithSpyAnnotation_NullInstance_ThrowsMockitoException() throws IllegalAccessException {
        Field[] fields = new Field[1];
        fields[0] = field;
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAccessible()).thenReturn(true);
        when(field.get(testClass)).thenReturn(null);

        try {
            spyAnnotationEngine.process(Object.class, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'fieldName' field because the *instance* is missing\n" +
                         "The instance must be created *before* initMocks();\n" +
                         "Example of correct usage of @Spy:\n" +
                         "   @Spy List mock = new LinkedList();\n" +
                         "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void testProcess_FieldWithSpyAnnotation_ValidInstance_ResetMockCalled() throws IllegalAccessException {
        Field[] fields = new Field[1];
        fields[0] = field;
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAccessible()).thenReturn(true);
        List<String> instance = new LinkedList<>();
        when(field.get(testClass)).thenReturn(instance);

        spyAnnotationEngine.process(Object.class, testClass);

        verify(Mockito).reset(instance);
    }

    @Test
    public void testProcess_FieldWithSpyAnnotation_ValidInstance_FieldSetAsSpy() throws IllegalAccessException {
        Field[] fields = new Field[1];
        fields[0] = field;
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAccessible()).thenReturn(true);
        List<String> instance = new LinkedList<>();
        when(field.get(testClass)).thenReturn(instance);

        spyAnnotationEngine.process(Object.class, testClass);

        verify(field).set(testClass, Mockito.spy(instance));
    }

    @Test
    public void testProcess_IllegalAccessException_ThrowsMockitoException() throws IllegalAccessException {
        Field[] fields = new Field[1];
        fields[0] = field;
        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAccessible()).thenReturn(true);
        List<String> instance = new LinkedList<>();
        when(field.get(testClass)).thenThrow(new IllegalAccessException());

        try {
            spyAnnotationEngine.process(Object.class, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Problems initiating spied field fieldName", e.getMessage());
        }
    }

    @Test
    public void testAssertNoAnnotations_UndesiredAnnotationPresent_ThrowsReporterException() {
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Mock.class)).thenReturn(true);
        when(field.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class)).thenReturn(false);

        try {
            spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
            fail("Expected ReporterException to be thrown");
        } catch (ReporterException e) {
            assertEquals("The combination of annotations Spy and org.mockito.Mock is not supported", e.getMessage());
        }
    }

    @Test
    public void testAssertNoAnnotations_UndesiredAnnotationsNotPresent_NoExceptionThrown() {
        Field field = mock(Field.class);
        when(field.isAnnotationPresent(Mock.class)).thenReturn(false);
        when(field.isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class)).thenReturn(false);

        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        verify(field).isAnnotationPresent(Mock.class);
        verify(field).isAnnotationPresent(org.mockito.MockitoAnnotations.Mock.class);
    }
}