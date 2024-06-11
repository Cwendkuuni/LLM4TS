
import org.junit.Assert;
import org.junit.Test;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Annotation annotation = null;
        Field field = null;
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        Assert.assertNull(result);
    }

    @Test
    public void testProcess_noAnnotations() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        spyAnnotationEngine.process(context, testClass);
        // No annotations to test, functionality is not applicable
    }

    @Test(expected = MockitoException.class)
    public void testProcess_missingInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        Field field = getField("instance");

        try {
            spyAnnotationEngine.process(context, testClass);
        } catch (MockitoException e) {
            Assert.assertEquals("Cannot create a @Spy for 'instance' field because the *instance* is missing\n" +
                    "The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testProcess_resetMock() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        List<String> mockInstance = new LinkedList<>();
        Field field = getField("mockInstance");
        setField(testClass, field, mockInstance);

        spyAnnotationEngine.process(context, testClass);

        Assert.assertTrue(mockInstance.isEmpty());
    }

    @Test
    public void testProcess_spyField() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        List<String> instance = new LinkedList<>();
        Field field = getField("instance");
        setField(testClass, field, instance);

        spyAnnotationEngine.process(context, testClass);

        Assert.assertTrue(new org.mockito.internal.util.MockUtil().isMock(instance));
    }

    private Field getField(String fieldName) {
        try {
            return SpyAnnotationEngine.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Field not found: " + fieldName, e);
        }
    }

    private void setField(Object object, Field field, Object value) {
        field.setAccessible(true);
        try {
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to set field value", e);
        }
    }
}
