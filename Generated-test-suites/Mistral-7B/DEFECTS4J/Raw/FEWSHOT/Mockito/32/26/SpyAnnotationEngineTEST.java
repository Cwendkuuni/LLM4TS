package org.mockito.internal.configuration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.util.nominstrutools.reflection.FieldAccessibleHelper;
import org.mockito.exceptions.Reporter;

public class SpyAnnotationEngineTest {

    private static final String FIELD_NAME = "testField";
    private static final Object TEST_INSTANCE = new Object() {};
    private static final Annotation SPY_ANNOTATION = new SpyAnnotation();

    @Mock
    private Reporter reporter;

    @Captor
    private FieldAccessibleHelper fieldAccessibleHelper;

    private SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setUp() {
        initMocks(this);
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testCreateMockFor() {
        assertNull(spyAnnotationEngine.createMockFor(SPY_ANNOTATION, null));
    }

    @Test
    public void testProcess_FieldIsNotAnnotatedWithSpyAnnotation() {
        Class context = getClass();
        Object testClass = this;

        Field field = createTestField();
        assertFalse(field.isAnnotationPresent(Spy.class));

        spyAnnotationEngine.process(context, testClass);

        verifyNoMoreInteractions(reporter);
    }

    @Test
    public void testProcess_FieldIsAnnotatedWithSpyAnnotation_ButInstanceIsAlreadyAMock() {
        Class context = getClass();
        Object testClass = this;

        Field field = createTestField();
        field.setAccessible(true);
        Object instance = spy(TEST_INSTANCE);
        field.set(testClass, instance);

        spyAnnotationEngine.process(context, testClass);

        verifyNoMoreInteractions(reporter);
        assertFalse(MockUtil.isMock(instance));
    }

    @Test
    public void testProcess_FieldIsAnnotatedWithSpyAnnotation_ButInstanceIsNotNull() {
        Class context = getClass();
        Object testClass = this;

        Field field = createTestField();
        field.setAccessible(true);
        Object instance = TEST_INSTANCE;
        field.set(testClass, instance);

        spyAnnotationEngine.process(context, testClass);

        verifyNoMoreInteractions(reporter);
        assertTrue(MockUtil.isMock(instance));
    }

    @Test
    public void testProcess_FieldIsAnnotatedWithSpyAnnotation_ButInstanceIsNull() {
        Class context = getClass();
        Object testClass = this;

        Field field = createTestField();
        field.setAccessible(true);

        spyAnnotationEngine.process(context, testClass);

        verify(reporter).unsupportedCombinationOfAnnotations("Spy", "null");
    }

    @Test
    public void testProcess_FieldIsAnnotatedWithSpyAnnotation_AndInstanceIsAMock() {
        Class context = getClass();
        Object testClass = this;

        Field field = createTestField();
        field.setAccessible(true);
        Object instance = mock(Object.class);
        field.set(testClass, instance);

        spyAnnotationEngine.process(context, testClass);

        verify(reporter).unsupportedCombinationOfAnnotations("Spy", "Mockito$1");
    }

    @Test
    public void testProcess_FieldIsAnnotatedWithSpyAnnotation_AndInstanceIsCapture() {
        Class context = getClass();
        Object testClass = this;

        Field field = createTestField();
        field.setAccessible(true);
        @Captor Captor<String> stringCaptor = mock(Captor.class);
        Object instance = new StringCaptor();
        field.set(testClass, instance);

        spyAnnotationEngine.process(context, testClass);

        verify(reporter).unsupportedCombinationOfAnnotations("Spy", "Capture");
    }

    @Test
    public void testProcess_FieldIsAnnotatedWithSpyAnnotation_AndInstanceIsMockitoAnnotationsMock() {
        Class context = getClass();
        Object testClass = this;

        Field field = createTestField();
        field.setAccessible(true);
        Object instance = Mockito.mock(Object.class, new MockitoAnnotationsMockAnnotation());
        field.set(testClass, instance);

        spyAnnotationEngine.process(context, testClass);

        verify(reporter).unsupportedCombinationOfAnnotations("Spy", "MockitoAnnotations$MockAnnotation");
    }

    @Test(expected = MockitoException.class)
    public void testProcess_ThrowsIllegalAccessException() {
        Class context = getClass();
        Object testClass = this;

        Field field = createTestField();

        spyAnnotationEngine.process(context, testClass);

        // should not reach here
    }

    private Field createTestField() {
        try {
            return context.getDeclaredField(FIELD_NAME);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    static class MockitoAnnotationsMockAnnotation {}

    static class StringCaptor {
        // empty class
    }
}