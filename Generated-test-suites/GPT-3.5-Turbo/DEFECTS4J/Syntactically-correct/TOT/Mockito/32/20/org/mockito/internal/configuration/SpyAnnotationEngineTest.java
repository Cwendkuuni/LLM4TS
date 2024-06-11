import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setup() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void createMockFor_validAnnotationAndField_returnsNull() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);

        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        assertNull(result);
    }

    @Test
    public void process_fieldsWithoutAnnotations_noSpyCreated() {
        Class<?> context = DummyClassWithoutAnnotations.class;
        Object testClass = new Object();

        spyAnnotationEngine.process(context, testClass);

        // Verify that no spy creation logic was executed
        verifyZeroInteractions(Mockito.class);
    }

    @Test
    public void process_fieldWithSpyAnnotationAndNullInstance_throwsMockitoException() {
        Field field = getField(DummyClassWithSpyAnnotation.class, "dummyField");
        Object testClass = new DummyClassWithSpyAnnotation();

        try {
            spyAnnotationEngine.process(DummyClassWithSpyAnnotation.class, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'dummyField' field because the *instance* is missing\n" +
                "The instance must be created *before* initMocks();\n" +
                "Example of correct usage of @Spy:\n" +
                "   @Spy List mock = new LinkedList();\n" +
                "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void process_fieldWithSpyAnnotationAndExistingMockInstance_resetsMock() throws IllegalAccessException {
        Field field = getField(DummyClassWithSpyAnnotation.class, "dummyField");
        Object testClass = new DummyClassWithSpyAnnotation();
        DummyClass dummyInstance = mock(DummyClass.class);
        setFieldValue(field, testClass, dummyInstance);

        spyAnnotationEngine.process(DummyClassWithSpyAnnotation.class, testClass);

        verify(Mockito.class).reset(dummyInstance);
    }

    @Test
    public void process_fieldWithSpyAnnotationAndNonMockInstance_createsNewSpy() throws IllegalAccessException {
        Field field = getField(DummyClassWithSpyAnnotation.class, "dummyField");
        Object testClass = new DummyClassWithSpyAnnotation();
        DummyClass nonMockInstance = new DummyClass();
        setFieldValue(field, testClass, nonMockInstance);

        spyAnnotationEngine.process(DummyClassWithSpyAnnotation.class, testClass);

        verify(Mockito.class, never()).reset(nonMockInstance);
        assertNotNull(field.get(testClass));
        assertTrue(field.get(testClass) instanceof DummyClass);
        assertNotSame(nonMockInstance, field.get(testClass));
    }

    @Test
    public void process_fieldWithSpyAnnotationAndIllegalAccess_throwsMockitoException() {
        Field field = getField(DummyClassWithSpyAnnotation.class, "dummyField");
        Object testClass = new DummyClassWithSpyAnnotation();
        field.setAccessible(false);

        try {
            spyAnnotationEngine.process(DummyClassWithSpyAnnotation.class, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Problems initiating spied field dummyField", e.getMessage());
        }
    }

    @Test
    public void assertNoAnnotations_noUndesiredAnnotation_assertNoExceptionThrown() throws NoSuchFieldException {
        Field field = getField(DummyClassWithSpyAnnotation.class, "dummyField");

        assertNoExceptionThrown(field);
    }

    @Test(expected = Reporter.class)
    public void assertNoAnnotations_containsUndesiredAnnotation_throwsReporter() throws NoSuchFieldException {
        Field field = getField(DummyClassWithSpyAnnotation.class, "dummyField");
        addAnnotation(field, Mock.class);

        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class);
    }

    private Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        return clazz.getDeclaredField(fieldName);
    }

    private void setFieldValue(Field field, Object object, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(object, value);
        field.setAccessible(false);
    }

    private void assertNoExceptionThrown(Field field) {
        try {
            spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    private void addAnnotation(Field field, Class<? extends Annotation> annotationClass) throws NoSuchFieldException {
        Annotation annotation = mock(annotationClass);
        field.setAccessible(true);
        field.setAnnotation(annotation);
        field.setAccessible(false);
    }

    private static class DummyClassWithoutAnnotations {
        private String field;
    }

    private static class DummyClassWithSpyAnnotation {
        @Spy
        private DummyClass dummyField;
    }

    private static class DummyClass {
        private String field;
    }
}