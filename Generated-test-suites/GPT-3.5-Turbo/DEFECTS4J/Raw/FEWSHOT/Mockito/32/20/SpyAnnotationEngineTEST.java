
package org.mockito.internal.configuration;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.Mockito;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;
import java.lang.reflect.Field;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);

        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        assertNull(result);
    }

    @Test
    public void testProcess() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();

        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void testProcess_spies() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

        Class<?> context = SpyAnnotationEngine.class;

        @Spy
        Object testClass = new Object();

        spyAnnotationEngine.process(context, testClass);

        //assertions for spied fields
    }

    @Test
    public void testProcess_mockExists() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

        Class<?> context = SpyAnnotationEngine.class;

        @Spy
        @Mock
        Object testClass = new Object();

        spyAnnotationEngine.process(context, testClass);

        //assertions for spied fields
    }

    @Test
    public void testProcess_instanceNull() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

        Class<?> context = SpyAnnotationEngine.class;

        @Spy
        Object testClass = null;

        try {
            spyAnnotationEngine.process(context, testClass);
        } catch (MockitoException e) {
            //assert exception message
            //assert exception cause
        }
    }

    @Test
    public void testProcess_isMock() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

        Class<?> context = SpyAnnotationEngine.class;

        Object instance = Mockito.mock(Object.class);

        @Spy
        Object testClass = instance;

        spyAnnotationEngine.process(context, testClass);

        //assertions for spied fields
    }

    //TODO test other process branches

    @Test
    public void testAssertNoAnnotations() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

        Class<?> annotation = Annotation.class;
        Field field = Mockito.mock(Field.class);
        Class<?> undesiredAnnotations = Mockito.mock(Class.class);

        spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotations);
    }
}
