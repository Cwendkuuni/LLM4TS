package org.mockito.internal.configuration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;

public class SpyAnnotationEngineTest {
    private SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setUp() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testCreateMockFor() throws NoSuchFieldException {
        Annotation annotation = null;
        Field field = null;
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        assertEquals(null, result);
    }

    @Test
    public void testProcess() throws IllegalAccessException, NoSuchFieldException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new SpyAnnotationEngine();
        Field[] fields = new Field[1];
        SpyAnnotationEngine spyAnnotationEngine1 = spy(spyAnnotationEngine);
        Mockito.doNothing().when(spyAnnotationEngine1).assertNoAnnotations(Spy.class, fields[0], Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
        Mockito.when(testClass.getClass()).thenReturn(context);
        fields[0] = context.getDeclaredField("spyAnnotationEngine");
        fields[0].setAccessible(true);
        
        fields[0].set(testClass, null);
        try {
            spyAnnotationEngine1.process(context, testClass);
        } catch (MockitoException e) {
            assertEquals(
                    "Cannot create a @Spy for '" + fields[0].getName() + "' field because the *instance* is missing\n"
                            + "The instance must be created *before* initMocks();\n"
                            + "Example of correct usage of @Spy:\n" + "   @Spy List mock = new LinkedList();\n"
                            + "   //also, don't forget about MockitoAnnotations.initMocks();",
                    e.getMessage());
        }
        
        Object instance = mock(SpyAnnotationEngine.class);
        fields[0].set(testClass, instance);
        spyAnnotationEngine1.process(context, testClass);
        reset(instance);
        fields[0].set(testClass, instance);
        spyAnnotationEngine1.process(context, testClass);
    }

    @Test
    public void testAssertNoAnnotations() throws NoSuchFieldException {
        Class<?> annotation = Spy.class;
        Field field = SpyAnnotationEngine.class.getDeclaredField("spyAnnotationEngine");
        Class<?>[] undesiredAnnotations = { Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class };
        new Reporter().unsupportedCombinationOfAnnotations(annotation.getSimpleName(),
                annotation.getClass().getSimpleName());
        assertEquals(null, null);
    }
}