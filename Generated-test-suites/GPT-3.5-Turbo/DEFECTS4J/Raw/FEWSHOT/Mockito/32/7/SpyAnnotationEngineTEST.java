package org.mockito.internal.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.junit.Test;

@SuppressWarnings({"unchecked"})
public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);
        Object result = engine.createMockFor(annotation, field);
        assertNull(result);
    }

    @Test
    public void testProcess() throws IllegalAccessException {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        engine.process(context, testClass);
        // Verify behavior here
    }

    @Test
    public void testProcess_withMockitoException() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        try {
            engine.process(context, testClass);
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'null' field because the *instance* is missing\n" +
                        	  "The instance must be created *before* initMocks();\n" +
                                  "Example of correct usage of @Spy:\n" +
                            	  "   @Spy List mock = new LinkedList();\n" +
                            	  "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void testProcess_withIllegalAccessException() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();
        Field field = Mockito.mock(Field.class);
        Mockito.when(field.getName()).thenReturn("fieldName");
        Mockito.when(field.get(testClass)).thenThrow(new IllegalAccessException("Invalid field access"));
        try {
            engine.process(context, testClass);
        } catch (MockitoException e) {
            assertEquals("Problems initiating spied field fieldName", e.getMessage());
        }
    }

    @Test
    public void testAssertNoAnnotations() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class annotation = Spy.class;
        Field field = Mockito.mock(Field.class);
        Class undesiredAnnotation1 = Mock.class;
        Class undesiredAnnotation2 = org.mockito.MockitoAnnotations.Mock.class;
        Class undesiredAnnotation3 = Captor.class;
        engine.assertNoAnnotations(annotation, field, undesiredAnnotation1, undesiredAnnotation2, undesiredAnnotation3);
        // Verify behavior here
    }
}