
/*
 * Copyright (c) 2007 Mockito contributors
 * This program is made available under the terms of the MIT License.
 */
package org.mockito.internal.configuration;

import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;

@SuppressWarnings({"unchecked"})
public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Annotation annotation = new Spy() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Spy.class;
            }
        };
        Field field = getField("list");
        Object mock = engine.createMockFor(annotation, field);
        assertNotNull(mock);
    }

    @Test
    public void testProcess_withExistingInstance() throws IllegalAccessException {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Object testClass = new TestClass();
        Field field = getField("list");
        field.set(testClass, new LinkedList<>());

        engine.process(TestClass.class, testClass);

        List<?> instance = (List<?>) field.get(testClass);
        assertNotNull(instance);
        assertEquals(0, instance.size());
    }

    @Test
    public void testProcess_withNullInstance() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Object testClass = new TestClass();
        Field field = getField("list");

        try {
            engine.process(TestClass.class, testClass);
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'list' field because the *instance* is missing\n" +
                    "The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void testProcess_withExistingMock() throws IllegalAccessException {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Object testClass = new TestClass();
        Field field = getField("list");
        List<Object> mockInstance = spy(new LinkedList<>());
        field.set(testClass, mockInstance);

        engine.process(TestClass.class, testClass);

        reset(mockInstance);
    }

    @Test
    public void testProcess_withUnsupportedAnnotations() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Object testClass = new TestClass();
        Field field = getField("listWithAnnotations");

        try {
            engine.process(TestClass.class, testClass);
        } catch (MockitoException e) {
            assertEquals("Unsupported combination of annotations found: Spy, Mock", e.getMessage());
        }
    }

    private Field getField(String name) {
        try {
            return TestClass.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Failed to get field " + name, e);
        }
    }

    private static class TestClass {
        @Spy
        private List<Object> list;

        @Captor
        @Mock
        private List<Object> listWithAnnotations;
    }
} 
