package org.mockito.internal.configuration;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.reset;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Annotation annotation = null;
        Field field = null;
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        assertNull(result);
    }

    @Test
    public void testProcess() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();
        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void testProcess_withNullInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();
        
        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'null' field because the *instance* is missing\n" +
                    "The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void testProcess_withExistingMockInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();
        Field field;
        try {
            field = SpyAnnotationEngineTest.class.getDeclaredField("mockInstance");
            field.setAccessible(true);
            field.set(testClass, Mockito.mock(List.class));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set up test");
        }
        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void testProcess_withNewSpyInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();
        Field field;
        try {
            field = SpyAnnotationEngineTest.class.getDeclaredField("spyInstance");
            field.setAccessible(true);
            field.set(testClass, new LinkedList<>());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Failed to set up test");
        }
        spyAnnotationEngine.process(context, testClass);
    }

    private List mockInstance;

    @Spy
    private List spyInstance;
}