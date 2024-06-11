package org.mockito.internal.configuration.test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockForNullAnnotation() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = MockUtil.getField(SpyAnnotationEngine.class, "engine"); //assuming engine is a private field
        Object instance = Mockito.mock(String.class);
        Annotation annotation = null;
        assertNull(engine.createMockFor(annotation, field));
    }

    @Test
    public void testCreateMockForAnnotationPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = MockUtil.getField(SpyAnnotationEngine.class, "engine");
        Object instance = Mockito.mock(String.class);
        Annotation annotation = Mockito.spy(Spy.class);
        assertNull(engine.createMockFor(annotation, field)); //should not mock an already spied object

        instance = new String("test");
        field.set(engine, instance);
        Mockito.reset(instance); //reset to make sure it's not spied before testing

        MockitoAnnotations.initMocks(this); //initialize mocks to make sure engine is initialized
        engine.process(SpyAnnotationEngine.class, engine); //process to spy the engine field

        assertNotNull(engine.createMockFor(annotation, field));
    }

    @Test(expected = MockitoException.class)
    public void testProcessNullContext() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Object testClass = new Object();
        engine.process(null, testClass);
    }

    @Test
    public void testProcessEmptyClass() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Object testClass = new Object();
        engine.process(SpyAnnotationEngine.class, testClass);
    }

    @Test
    public void testProcessClassWithSpyAnnotation() throws IllegalAccessException {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Object testClass = new Object() {
            @Spy
            public String field;
        };
        engine.process(SpyAnnotationEngine.class, testClass);

        Field field = MockUtil.getField(testClass.getClass(), "field");
        Object instance = field.get(testClass);
        assertNotNull(instance); //instance should not be null
        assertTrue(MockUtil.isSpy(instance)); //instance should be spied

        field.setAccessible(false); //make sure field is not accessible after testing
    }

    @Test(expected = MockitoException.class)
    public void testProcessClassWithDuplicateAnnotations() throws IllegalAccessException {
        Object testClass = new Object() {
            @Spy
            @Mock
            public String field;
        };

        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(SpyAnnotationEngine.class, testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcessClassWithMockAnnotation() throws IllegalAccessException {
        Object testClass = new Object() {
            @Mock
            public String field;
        };

        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(SpyAnnotationEngine.class, testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcessClassWithCaptorAnnotation() throws IllegalAccessException {
        Object testClass = new Object() {
            @Captor
            public Mockito.ArgumentCaptor<String> captor;
        };

        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(SpyAnnotationEngine.class, testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcessClassWithMissingInstance() throws IllegalAccessException {
        Object testClass = new Object() {
            @Spy
            public String field;
        };

        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(SpyAnnotationEngine.class, testClass);

        Mockito.reset(testClass); //reset to make sure field is not spied before testing

        Field field = MockUtil.getField(testClass.getClass(), "field");
        field.setAccessible(true);
        assertNull(field.get(testClass)); //field should be null as it's not initialized before initMocks()

        engine.process(SpyAnnotationEngine.class, testClass); //process again to try and spy the field
    }

    @Test(expected = MockitoException.class)
    public void testProcessClassWithIllegalAccessException() throws IllegalAccessException {
        Object testClass = new Object() {
            private String field;
            @Spy
            public void setField(String value) {
                this.field = value;
            }
        };

        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(SpyAnnotationEngine.class, testClass);
    }
}