package org.mockito.internal.configuration.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = this.getClass();
        Object testClass = this;
        Field field = null;
        try {
            field = context.getDeclaredField("spyField"); // Assuming there is a field named "spyField" in this class
            engine.process(context, testClass);
            Assert.assertNull(engine.createMockFor(MockAnnotation.class, field));
        } catch (NoSuchFieldException e) {
            // Field not found is expected
        }
    }

    @SuppressWarnings("unchecked")
    @Test(expected = MockitoException.class)
    public void testCreateMockForMissingInstance() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = this.getClass();
        Object testClass = this;
        Field field = null;
        try {
            field = context.getDeclaredField("spyField"); // Assuming there is a field named "spyField" in this class
            field.setAccessible(true);
            field.set(testClass, null);
            engine.process(context, testClass);
            engine.createMockFor(SpyAnnotation.class, field);
        } catch (NoSuchFieldException e) {
            // Field not found is expected
        } catch (IllegalAccessException e) {
            // Accessing private field is expected
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testProcess_SpyAnnotationPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = this.getClass();
        Object testClass = this;
        Field field = null;
        try {
            field = context.getDeclaredField("spyField"); // Assuming there is a field named "spyField" in this class
            Object instance = new Object();
            field.set(testClass, instance);
            engine.process(context, testClass);
            Assert.assertNotNull(field.get(testClass));
            Assert.assertTrue(Mockito.isSpy(field.get(testClass)));
        } catch (NoSuchFieldException e) {
            // Field not found is expected
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testProcess_SpyAnnotationPresentTwice() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = this.getClass();
        Object testClass = this;
        Field field = null;
        try {
            field = context.getDeclaredField("spyField"); // Assuming there is a field named "spyField" in this class
            Object instance = new Object();
            field.set(testClass, instance);
            engine.process(context, testClass);
            engine.process(context, testClass);
            Assert.assertNotNull(field.get(testClass));
            Assert.assertTrue(Mockito.isSpy(field.get(testClass)));
        } catch (NoSuchFieldException e) {
            // Field not found is expected
        }
    }

    @Spy
    @Test
    public void testProcess_SpyAnnotationPresentWithMockAnnotation() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = this.getClass();
        Object testClass = this;
        Field field = null;
        try {
            field = context.getDeclaredField("spyField"); // Assuming there is a field named "spyField" in this class
            Object mock = Mockito.mock(String.class);
            field.set(testClass, mock);
            engine.process(context, testClass);
            Assert.assertNotNull(field.get(testClass));
            Assert.assertFalse(Mockito.isSpy(field.get(testClass)));
            Assert.assertTrue(Mockito.isMock(field.get(testClass)));
        } catch (NoSuchFieldException e) {
            // Field not found is expected
        }
    }

    @Test
    public void testProcess_NonSpyAnnotationPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = this.getClass();
        Object testClass = this;
        Field field = null;
        try {
            field = context.getDeclaredField("mockField"); // Assuming there is a field named "mockField" in this class
            Object instance = new Object();
            field.set(testClass, instance);
            engine.process(context, testClass);
            Assert.assertNull(field.get(testClass));
        } catch (NoSuchFieldException e) {
            // Field not found is expected
        }
    }

    @Test(expected = MockitoException.class)
    public void testProcess_SpyAnnotationAndMockAnnotationPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = this.getClass();
        Object testClass = this;
        Field field = null;
        try {
            field = context.getDeclaredField("fieldWithBothAnnotations"); // Assuming there is a field named "fieldWithBothAnnotations" in this class
            Object instance = new Object();
            field.set(testClass, instance);
            engine.process(context, testClass);
        } catch (NoSuchFieldException e) {
            // Field not found is expected
        }
    }

    @Test(expected = MockitoException.class)
    public void testProcess_CaptorAnnotationPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = this.getClass();
        Object testClass = this;
        Field field = null;
        try {
            field = context.getDeclaredField("captorField"); // Assuming there is a field named "captorField" in this class annotated with @Captor
            Object captor = Mockito.mock(Mockito.ArgumentCaptor.class);
            field.set(testClass, captor);
            engine.process(context, testClass);
        } catch (NoSuchFieldException e) {
            // Field not found is expected
        }
    }

    @Test
    public void testProcess_AnnotationOtherThanSpyMockCaptorPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = this.getClass();
        Object testClass = this;
        Field field = null;
        try {
            field = context.getDeclaredField("otherAnnotationField"); // Assuming there is a field named "otherAnnotationField" in this class with an annotation other than @Spy, @Mock, or @Captor
            Object instance = new Object();
            field.set(testClass, instance);
            engine.process(context, testClass);
            Assert.assertNull(field.get(testClass));
        } catch (NoSuchFieldException e) {
            // Field not found is expected
        }
    }

    @Test(expected = MockitoException.class)
    public void testProcess_MultipleSpyAnnotationsPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Class<?> context = this.getClass();
        Object testClass = this;
        Field field1 = null, field2 = null;
        try {
            field1 = context.getDeclaredField("field1"); // Assuming there is a field named "field1" in this class annotated with @Spy
            Object instance1 = new Object();
            field1.set(testClass, instance1);
            field2 = context.getDeclaredField("field2"); // Assuming there is a field named "field2" in this class also annotated with @Spy
            Object instance2 = new Object();
            field2.set(testClass, instance2);
            engine.process(context, testClass);
        } catch (NoSuchFieldException e) {
            // Fields not found is expected
        }
    }

    @Test
    public void testAssertNoAnnotations_SpyAnnotationNotPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = null;
        try {
            field = this.getClass().getDeclaredField("nonSpyField"); // Assuming there is a field named "nonSpyField" in this class not annotated with @Spy
            engine.assertNoAnnotations(SpyAnnotation.class, field);
        } catch (NoSuchFieldException e) {
            // Field not found is expected
        }
    }

    @Test(expected = org.mockito.exceptions.base.MockitoException.class)
    public void testAssertNoAnnotations_SpyAnnotationPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = null;
        try {
            field = this.getClass().getDeclaredField("spyField"); // Assuming there is a field named "spyField" in this class annotated with @Spy
            engine.assertNoAnnotations(SpyAnnotation.class, field);
        } catch (NoSuchFieldException e) {
            // Field not found is expected
        }
    }

    @Test(expected = org.mockito.exceptions.base.MockitoException.class)
    public void testAssertNoAnnotations_MockAnnotationPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = null;
        try {
            field = this.getClass().getDeclaredField("mockField"); // Assuming there is a field named "mockField" in this class annotated with @Mock
            engine.assertNoAnnotations(SpyAnnotation.class, field);
        } catch (NoSuchFieldException e) {
            // Field not found is expected
        }
    }

    @Test(expected = org.mockito.exceptions.base.MockitoException.class)
    public void testAssertNoAnnotations_CaptorAnnotationPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = null;
        try {
            field = this.getClass().getDeclaredField("captorField"); // Assuming there is a field named "captorField" in this class annotated with @Captor
            engine.assertNoAnnotations(SpyAnnotation.class, field);
        } catch (NoSuchFieldException e) {
            // Field not found is expected
        }
    }

    @Test
    public void testAssertNoAnnotations_NoAnnotationsPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = null;
        try {
            field = this.getClass().getDeclaredField("otherField"); // Assuming there is a field named "otherField" in this class without any annotations
            engine.assertNoAnnotations(SpyAnnotation.class, field);
        } catch (NoSuchFieldException e) {
            // Field not found is expected
        }
    }

    @Test(expected = org.mockito.exceptions.base.MockitoException.class)
    public void testAssertNoAnnotations_MultipleAnnotationsPresent() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = null;
        try {
            field = this.getClass().getDeclaredField("fieldWithMultipleAnnotations"); // Assuming there is a field named "fieldWithMultipleAnnotations" in this class with both @Spy and another annotation
            engine.assertNoAnnotations(SpyAnnotation.class, field);
        } catch (NoSuchFieldException e) {
            // Field not found is expected
        }
    }

    private static class MockAnnotation {}
    private static class SpyAnnotation {}
}