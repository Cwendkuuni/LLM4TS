package org.mockito.internal.configuration;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.util.MockUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setup() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testCreateMockFor() {
        Annotation annotation = new Spy() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Spy.class;
            }
        };

        Field field = getField("mockField");

        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        Assert.assertNull(result);
    }

    @Test
    public void testProcess_withNullInstance_shouldThrowException() {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new DummyTestClass();

        try {
            spyAnnotationEngine.process(context, testClass);
            Assert.fail("Should have thrown an exception");
        } catch (MockitoException e) {
            Assert.assertEquals("Cannot create a @Spy for 'mockField' field because the *instance* is missing\n" +
            		            "The instance must be created *before* initMocks();\n" +
                                "Example of correct usage of @Spy:\n" +
                            	"   @Spy List mock = new LinkedList();\n" +
                            	"   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void testProcess_withExistingMockInstance() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        TestClass testClass = new TestClass();
        Field field = getField("mockField");

        field.set(testClass, Mockito.mock(List.class));

        spyAnnotationEngine.process(context, testClass);

        Assert.assertTrue(Mockito.mockingDetails(testClass.mockField).isMock());
        reset(testClass.mockField);
    }

    @Test
    public void testProcess_withNonMockInstance() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        TestClass testClass = new TestClass();
        Field field = getField("mockField");

        field.set(testClass, new LinkedList<>());

        spyAnnotationEngine.process(context, testClass);

        Assert.assertTrue(Mockito.mockingDetails(testClass.mockField).isMock());
        reset(testClass.mockField);
    }

    @Test
    public void testProcess_withSpyInstance() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        TestClass testClass = new TestClass();
        Field field = getField("mockField");

        field.set(testClass, spy(new LinkedList<>()));

        spyAnnotationEngine.process(context, testClass);

        Assert.assertTrue(Mockito.mockingDetails(testClass.mockField).isMock());
        reset(testClass.mockField);
    }

    private Field getField(String fieldName) {
        try {
            return SpyAnnotationEngineTest.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field not found: " + fieldName);
        }
    }

    private class TestClass {
        @Mock
        @Spy
        private List<String> mockField;
    }

    private class DummyTestClass {
        @Spy
        private List<String> mockField;
    }
}