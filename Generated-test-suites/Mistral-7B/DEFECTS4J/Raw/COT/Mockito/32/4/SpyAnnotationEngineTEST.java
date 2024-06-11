import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.configuration.AnnotationEngine;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Assert.assertNull(spyAnnotationEngine.createMockFor(null, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMockForWithNullAnnotation() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Annotation annotation = null;
        Field field = new Field(String.class, "testField");
        try {
            spyAnnotationEngine.createMockFor(annotation, field);
        } catch (Exception e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMockForWithNullField() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Annotation annotation = new Annotation() {};
        try {
            spyAnnotationEngine.createMockFor(annotation, null);
        } catch (Exception e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
    }

    @Test
    public void testProcessWithNoSpyAnnotation() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class context = Object.class;
        Object testClass = new Object() {};
        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void testProcessWithSpyAnnotation() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class context = Object.class;
        Object testClass = new Object() {
            @Spy
            public Object spyField;
        };
        spyAnnotationEngine.process(context, testClass);
        Field spyField = null;
        try {
            spyField = context.getDeclaredField("spyField");
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
        Assert.assertNotNull(spyField);
        Assert.assertFalse(spyField.isAccessible());
        try {
            Object instance = spyField.get(testClass);
            Assert.assertNotNull(instance);
            Assert.assertTrue(instance instanceof Object); // Spy annotation can be used on any type
            spyAnnotationEngine.process(context, testClass); // reset the field access to test the reset functionality
            Assert.assertTrue(spyField.isAccessible());
            Assert.assertEquals(instance, spyField.get(testClass));
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
    }

    @Test(expected = MockitoException.class)
    public void testProcessWithMissingInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class context = Object.class;
        Object testClass = new Object() {
            @Spy
            public Object spyField;
        };
        Field spyField = null;
        try {
            spyField = context.getDeclaredField("spyField");
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
        spyField.setAccessible(true);
        spyAnnotationEngine.process(context, testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcessWithMockAnnotation() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class context = Object.class;
        Object testClass = new Object() {
            @Mock
            public Object mockField;
        };
        Field mockField = null;
        try {
            mockField = context.getDeclaredField("mockField");
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
        spyAnnotationEngine.process(context, testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcessWithCaptorAnnotation() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class context = Object.class;
        Object testClass = new Object() {
            @Captor
            public Object captorField;
        };
        Field captorField = null;
        try {
            captorField = context.getDeclaredField("captorField");
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
        spyAnnotationEngine.process(context, testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcessWithMixedAnnotations() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class context = Object.class;
        Object testClass = new Object() {
            @Spy
            @Mock
            public Object mixedAnnotationField;
        };
        Field mixedAnnotationField = null;
        try {
            mixedAnnotationField = context.getDeclaredField("mixedAnnotationField");
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
        spyAnnotationEngine.process(context, testClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssertNoAnnotationsWithInvalidAnnotationClass() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class context = Object.class;
        Object testClass = new Object() {};
        Field field = new Field(String.class, "testField");
        try {
            spyAnnotationEngine.assertNoAnnotations(null, field, Mockito.Mock.class, Captor.class);
        } catch (IllegalArgumentException e) {
            return;
        }
        throw new IllegalStateException("Unexpected result");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssertNoAnnotationsWithFieldWithoutAnnotation() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class context = Object.class;
        Object testClass = new Object() {};
        Field field = new Field(String.class, "testField");
        try {
            spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mockito.Mock.class, Captor.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
        try {
            spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mockito.Mock.class, Captor.class, Mockito.Spy.class);
        } catch (IllegalArgumentException e) {
            return;
        }
        throw new IllegalStateException("Unexpected result");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssertNoAnnotationsWithMultipleUndesiredAnnotations() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class context = Object.class;
        Object testClass = new Object() {};
        Field field = new Field(String.class, "testField");
        try {
            spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mockito.Mock.class, Captor.class, Mockito.Mock.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
        try {
            spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mockito.Mock.class, Mockito.Captor.class, Mockito.Mock.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
        try {
            spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mockito.Mock.class, Mockito.Mock.class, Mockito.Mock.class);
        } catch (IllegalArgumentException e) {
            return;
        }
        throw new IllegalStateException("Unexpected result");
    }

    @Test(expected = org.mockito.exceptions.base.MockitoException.class)
    public void testAssertNoAnnotationsWithAnnotationAndMock() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class context = Object.class;
        Object testClass = new Object() {
            @Spy
            @Mock
            public Object annotatedAndMockedField;
        };
        Field annotatedAndMockedField = null;
        try {
            annotatedAndMockedField = context.getDeclaredField("annotatedAndMockedField");
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Unexpected exception", e);
        }
        try {
            spyAnnotationEngine.assertNoAnnotations(Spy.class, annotatedAndMockedField, Mockito.Mock.class);
        } catch (IllegalArgumentException e) {
            throw new MockitoException("Unexpected exception", e);
        }
    }
}