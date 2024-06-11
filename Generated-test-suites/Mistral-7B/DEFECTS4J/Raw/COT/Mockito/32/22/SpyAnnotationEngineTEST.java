import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.AnnotationEngine;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.Assert.fail;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

    @SuppressWarnings({"unchecked"})
    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Field testField = null;
        try {
            testField = AnnotationEngine.class.getDeclaredField("testField");
        } catch (NoSuchFieldException e) {
            fail("Class AnnotationEngine should have a field named 'testField'");
        }

        Annotation annotation = Mockito.mock(Annotation.class);
        try {
            Object result = annotationEngine.createMockFor(annotation, testField);
            fail("Expected null, but got: " + result);
        } catch (AssertionError e) {
            // Expected
        }
    }

    @Test
    public void testProcessNullContext() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        try {
            annotationEngine.process(null, new Object());
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test
    public void testProcessEmptyClass() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        try {
            Object testClass = new Object();
            annotationEngine.process(AnnotationEngine.class, testClass);
            fail("Expected no fields to be processed");
        } catch (IllegalAccessException e) {
            // Expected
        }
    }

    @Test
    @Spy
    public void testProcessFieldWithoutAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object() {
            Object field;
        };

        try {
            annotationEngine.process(AnnotationEngine.class, testClass);
            fail("Expected no fields to be processed");
        } catch (IllegalAccessException e) {
            // Expected
        }

        assert testClass.field != null;
    }

    @Test
    public void testProcessFieldWithMockAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object() {
            @Mock
            Object field;
        };

        try {
            annotationEngine.process(AnnotationEngine.class, testClass);
            fail("Expected field 'field' to be reset");
        } catch (IllegalAccessException e) {
            // Expected
        }

        Mockito.verify(testClass.field).reset();
    }

    @Test
    public void testProcessFieldWithSpyAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object() {
            @Spy
            Object field;
        };

        try {
            annotationEngine.process(AnnotationEngine.class, testClass);
            fail("Expected field 'field' to be spied");
        } catch (IllegalAccessException e) {
            // Expected
        }

        Object spiedField = Mockito.spy(testClass.field);
        assert testClass.field != spiedField;
        assert testClass.field.getClass().isAssignableFrom(spiedField.getClass());
    }

    @Test
    public void testProcessFieldWithTwoAnnotations() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object() {
            @Mock @Spy
            Object field;
        };

        try {
            annotationEngine.process(AnnotationEngine.class, testClass);
            fail("Expected Reporter.unsupportedCombinationOfAnnotations() to be thrown");
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test(expected = MockitoException.class)
    public void testProcessFieldWithInvalidAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object() {
            @SuppressWarnings("unused")
            @org.mockito.internal.configuration.Spy
            Object field;
        };

        annotationEngine.process(AnnotationEngine.class, testClass);
    }

    @Test
    public void testProcessFieldWithAccessibleFlagFalse() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object() {
            Object field;
        };

        try {
            Field testField = AnnotationEngine.class.getDeclaredField("testField");
            testField.setAccessible(false);
            annotationEngine.process(AnnotationEngine.class, testClass);
            fail("Expected IllegalAccessException");
        } catch (IllegalAccessException e) {
            // Expected
        }

        try {
            Field testField = AnnotationEngine.class.getDeclaredField("testField");
            testField.setAccessible(true);
            annotationEngine.process(AnnotationEngine.class, testClass);
            assert testClass.field != null;
        } catch (IllegalAccessException e) {
            fail("Unexpected IllegalAccessException");
        }
    }

    @Test
    public void testProcessFieldWithNullInstance() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object() {
            Object field;
        };

        try {
            Field testField = AnnotationEngine.class.getDeclaredField("testField");
            testField.setAccessible(true);
            Object instance = null;
            annotationEngine.process(AnnotationEngine.class, instance);
            fail("Expected MockitoException");
        } catch (IllegalAccessException e) {
            // Expected
        } catch (MockitoException e) {
            // Expected
        }

        try {
            Field testField = AnnotationEngine.class.getDeclaredField("testField");
            testField.setAccessible(true);
            annotationEngine.process(AnnotationEngine.class, testClass);
            assert testClass.field != null;
        } catch (IllegalAccessException e) {
            fail("Unexpected IllegalAccessException");
        }
    }

    @Test
    public void testProcessFieldWithNullAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object() {
            Object field;
        };

        try {
            Field testField = AnnotationEngine.class.getDeclaredField("testField");
            testField.setAccessible(true);
            Annotation annotation = null;
            annotationEngine.process(AnnotationEngine.class, testClass);
            fail("Expected MockitoException");
        } catch (IllegalAccessException e) {
            // Expected
        } catch (MockitoException e) {
            // Expected
        }

        try {
            Field testField = AnnotationEngine.class.getDeclaredField("testField");
            testField.setAccessible(true);
            annotationEngine.process(AnnotationEngine.class, testClass);
            assert testClass.field != null;
        } catch (IllegalAccessException e) {
            fail("Unexpected IllegalAccessException");
        }
    }

    @Test
    public void testAssertNoAnnotations() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object() {
            @Mock
            Object field1;
            @Captor
            org.mockito.ArgumentCaptor<String> field2;
        };

        try {
            Field testField = SpyAnnotationEngine.class.getDeclaredField("assertNoAnnotations");
            testField.setAccessible(true);
            annotationEngine.assertNoAnnotations(Spy.class, null, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
            fail("Expected Reporter.unsupportedCombinationOfAnnotations() to be thrown");
        } catch (MockitoException e) {
            // Expected
        }

        try {
            Field testField = SpyAnnotationEngine.class.getDeclaredField("assertNoAnnotations");
            testField.setAccessible(true);
            annotationEngine.assertNoAnnotations(Spy.class, testClass.field1, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
            // Expected no exception
        } catch (MockitoException e) {
            fail("Unexpected MockitoException");
        } catch (IllegalAccessException e) {
            fail("Unexpected IllegalAccessException");
        }

        try {
            Field testField = SpyAnnotationEngine.class.getDeclaredField("assertNoAnnotations");
            testField.setAccessible(true);
            annotationEngine.assertNoAnnotations(Spy.class, testClass.field1, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class, Captor.class);
            fail("Expected Reporter.unsupportedCombinationOfAnnotations() to be thrown");
        } catch (MockitoException e) {
            // Expected
        }
    }

    @Test
    public void testProcessWithDuplicateAnnotationEngines() {
        SpyAnnotationEngine annotationEngine1 = new SpyAnnotationEngine();
        SpyAnnotationEngine annotationEngine2 = new SpyAnnotationEngine();
        Object testClass = new Object() {
            Object field;
        };

        try {
            Field testField = AnnotationEngine.class.getDeclaredField("testField");
            testField.setAccessible(true);
            annotationEngine1.process(AnnotationEngine.class, testClass);
            annotationEngine2.process(AnnotationEngine.class, testClass);
            fail("Expected IllegalStateException");
        } catch (IllegalAccessException e) {
            // Expected
        } catch (MockitoException e) {
            // Expected
        }

        try {
            Field testField = AnnotationEngine.class.getDeclaredField("testField");
            testField.setAccessible(true);
            annotationEngine1.process(AnnotationEngine.class, testClass);
            MockitoAnnotations.initMocks(testClass);
            annotationEngine2.process(AnnotationEngine.class, testClass);
            // Expected no exception
        } catch (IllegalAccessException e) {
            fail("Unexpected IllegalAccessException");
        }
    }
}