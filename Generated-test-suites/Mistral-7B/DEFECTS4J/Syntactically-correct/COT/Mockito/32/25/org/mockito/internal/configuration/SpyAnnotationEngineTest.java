import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.JUnit4;
import org.mockito.internal.configuration.AnnotationEngine;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.FieldUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.ProxyInvocationHandler;
import org.mockito.internal.configuration.utils.ClassUtil;
import org.mockito.internal.configuration.utils.Reflections;
import org.mockito.internal.configuration.utils.FieldNameUtils;
import org.mockito.internal.exceptions.MockitoAnnotationException;
import org.mockito.internal.exceptions.base.MockitoExceptionMessage;
import org.mockito.plugins.AnnotationProcessingException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.assertThat;
import static org.mockito.Mockito.assertThrows;
import static org.mockito.internal.util.reflection.FieldUtil.getFieldValue;
import static org.mockito.internal.exceptions.MockitoAnnotationException.noInstanceProvided;
import static org.mockito.internal.exceptions.MockitoAnnotationException.fieldAccessException;
import static org.mockito.internal.exceptions.base.MockitoExceptionMessage.spyInstanceMissing;
import static org.mockito.internal.exceptions.base.MockitoExceptionMessage.cannotCreateSpyFor;
import static org.mockito.internal.exceptions.base.MockitoExceptionMessage.illegalAccessException;
import static org.mockito.internal.exceptions.base.MockitoExceptionMessage.unsupportedAnnotationsCombination;

@RunWith(JUnit4.class)
public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockForNullAnnotationAndField() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Annotation annotation = null;
        Field field = new Field(this.getClass(), "testField", Integer.TYPE);

        Object result = annotationEngine.createMockFor(annotation, field);
        assertNull(result);
    }

    @Test
    public void testCreateMockForAnnotationAbsentField() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Annotation annotation = new Annotation("TestAnnotation") {
        };
        Field field = new Field(this.getClass(), "testField", Integer.TYPE);

        Object result = annotationEngine.createMockFor(annotation, field);
        assertNull(result);
    }

    @Test
    public void testCreateMockForSpyAnnotationPresentField() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Annotation annotation = new Spy();
        Field field = new Field(this.getClass(), "testField", String.class);

        Object instance = mock(String.class);
        field.setAccessible(true);
        try {
            field.set(this, instance);
        } catch (IllegalAccessException e) {
            // ignored
        }

        Object result = annotationEngine.createMockFor(annotation, field);
        assertSame(instance, result);
    }

    @Test
    public void testProcessNullContext() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object();

        annotationEngine.process(null, testClass);
    }

    @Test
    public void testProcessEmptyClass() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object();

        annotationEngine.process(this.getClass(), testClass);
    }

    @Test
    public void testProcessFieldNotPresentSpyAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object() {
            @Mock
            public String testField;
        };

        annotationEngine.process(this.getClass(), testClass);
        verifyZeroInteractions(testClass.testField);
    }

    @Test
    public void testProcessFieldAccessibleButNullInstance() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object() {
            @Spy
            public String testField;
        };

        Field testField = FieldUtil.getField(testClass, "testField", String.class);

        annotationEngine.process(this.getClass(), testClass);
        assertThrows(MockitoException.class, () -> annotationEngine.createMockFor(new Spy(), testField));
    }

    @Test
    public void testProcessFieldAccessibleButMockInstance() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object() {
            @Spy
            public String testField = "test";
        };

        Field testField = FieldUtil.getField(testClass, "testField", String.class);

        Object result = annotationEngine.createMockFor(new Spy(), testField);
        assertSame(testClass.testField, result);
        verify(testClass.testField).toString();
    }

    @Test
    public void testProcessFieldNotAccessible() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object() {
            @Spy
            private String testField;
        };

        Field testField = FieldUtil.getField(testClass, "testField", String.class);
        testField.setAccessible(false);

        annotationEngine.process(this.getClass(), testClass);

        try {
            annotationEngine.createMockFor(new Spy(), testField);
            fail();
        } catch (IllegalAccessException e) {
            // expected
        }
    }

    @Test
    public void testProcessFieldInstanceIsMock() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object() {
            @Mock
            public String testField;
        };

        Field testField = FieldUtil.getField(testClass, "testField", String.class);

        annotationEngine.process(this.getClass(), testClass);

        Object result = annotationEngine.createMockFor(new Spy(), testField);
        assertSame(testField, result);
        verifyZeroInteractions(testField);
    }

    @Test
    public void testAssertNoAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        Class<?> context = this.getClass();
        Object testClass = new Object();

        Field field = new Field(testClass, "testField", String.class);

        annotationEngine.assertNoAnnotations(Spy.class, field, MockitoMock.class, Captor.class, MockitoCaptor.class);
    }

    @Test
    public void testAssertNoAnnotation_FieldAnnotatedWithSpyAnnotationAndMockAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        Class<?> context = this.getClass();
        Object testClass = new Object() {
            @Spy
            @Mock
            public String testField;
        };

        Field field = new Field(testClass, "testField", String.class);

        try {
            annotationEngine.assertNoAnnotations(Spy.class, field, MockitoMock.class, Captor.class, MockitoCaptor.class);
            fail();
        } catch (MockitoAnnotationException e) {
            // expected
        }
    }

    @Test
    public void testAssertNoAnnotation_FieldAnnotatedWithMockAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        Class<?> context = this.getClass();
        Object testClass = new Object() {
            @Mock
            public String testField;
        };

        Field field = new Field(testClass, "testField", String.class);

        try {
            annotationEngine.assertNoAnnotations(Spy.class, field, MockitoMock.class);
            fail();
        } catch (MockitoAnnotationException e) {
            // expected
        }
    }

    @Test
    public void testAssertNoAnnotation_FieldAnnotatedWithCaptorAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        Class<?> context = this.getClass();
        Object testClass = new Object() {
            @Captor
            public ArgumentCaptor<String> testField;
        };

        Field field = new Field(testClass, "testField", ArgumentCaptor.class);

        try {
            annotationEngine.assertNoAnnotations(Spy.class, field, MockitoMock.class, Captor.class);
            fail();
        } catch (MockitoAnnotationException e) {
            // expected
        }
    }

    @Test(expected = AnnotationProcessingException.class)
    public void testAssertNoAnnotation_FieldAnnotatedWithInvalidAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();

        Class<?> context = this.getClass();
        Object testClass = new Object() {
            @MyAnnotation
            public String testField;
        };

        Field field = new Field(testClass, "testField", String.class);

        annotationEngine.assertNoAnnotations(Spy.class, field, MockitoMock.class, Captor.class);
    }

    @Test
    public void testProcessWithException() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object() {
            @Spy
            public String testField = new Exception("test");
        };

        Field testField = FieldUtil.getField(testClass, "testField", String.class);

        try {
            annotationEngine.process(this.getClass(), testClass);
            fail();
        } catch (IllegalAccessException e) {
            // expected
        }
    }

    @Test
    public void testProcessWithMockitoException() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object() {
            @Spy
            public String testField = mock(String.class);
        };

        Field testField = FieldUtil.getField(testClass, "testField", String.class);

        try {
            annotationEngine.process(this.getClass(), testClass);
            fail();
        } catch (MockitoException e) {
            // expected
        }
    }

    private static class MyAnnotation {}

    private static class MockitoMock {}

    private static class MockitoCaptor {}
}