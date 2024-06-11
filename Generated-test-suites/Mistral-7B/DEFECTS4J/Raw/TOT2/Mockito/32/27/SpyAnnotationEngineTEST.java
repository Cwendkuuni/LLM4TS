package org.mockito.internal.configuration;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(org.mockito.internal.configuration.JUnit4.class)
public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);

        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        assertNull(result);
    }

    @Test
    public void testProcess() throws IllegalAccessException {
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);

        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.process(context, testClass);

        verify(context).getDeclaredFields();
    }

    @Test
    public void testAssertNoAnnotations() {
        Class<Spy> spyAnnotation = Spy.class;
        Field field = mock(Field.class);

        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.assertNoAnnotations(spyAnnotation, field);

        // No exception should be thrown
    }

    @Test(expected = org.mockito.exceptions.base.MockitoException.class)
    public void testAssertNoAnnotationsWithAnnotationPresent() {
        Class<Spy> spyAnnotation = Spy.class;
        Field field = mock(Field.class);
        verify(field).isAnnotationPresent(spyAnnotation);

        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.assertNoAnnotations(spyAnnotation, field);

        // MockitoException should be thrown
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMockForNullAnnotation() {
        Field field = mock(Field.class);
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.createMockFor(null, field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateMockForNullField() {
        Annotation annotation = mock(Annotation.class);
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.createMockFor(annotation, null);
    }

    @Test
    public void testCreateMockForNonNullInstance() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);
        Object instance = mock(Object.class);
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Object result = spyAnnotationEngine.createMockFor(annotation, field, instance);

        assertNull(result);
        verifyNoInteractions(instance);
    }

    @Test(expected = NullPointerException.class)
    public void testProcessNullContext() {
        Object testClass = mock(Object.class);
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.process(null, testClass);
    }

    @Test(expected = NullPointerException.class)
    public void testProcessNullTestClass() {
        Class<?> context = mock(Class.class);
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.process(context, null);
    }

    @Test
    public void testProcessEmptyFields() {
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.process(context, testClass);

        verifyNoInteractions(testClass, context.getDeclaredFields());
    }

    @Test
    public void testProcessMultipleSpyAnnotations() throws IllegalAccessException {
        Class<?> context = mock(Class.class);
        Object testClass = mock(Object.class);
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);
        doReturn(true).when(field1).isAnnotationPresent(Spy.class);
        doReturn(true).when(field2).isAnnotationPresent(Spy.class);
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.process(context, testClass);

        verify(testClass).setField(field1, any());
        verify(testClass).setField(field2, any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssertNoAnnotationsNullAnnotation() {
        Field field = mock(Field.class);
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.assertNoAnnotations(null, field);
    }

    @Test(expected = NullPointerException.class)
    public void testAssertNoAnnotationsNullField() {
        Class<Spy> spyAnnotation = Spy.class;
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.assertNoAnnotations(spyAnnotation, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssertNoAnnotationsMultipleAnnotations() {
        Class<Spy> spyAnnotation = Spy.class;
        Field field = mock(Field.class);
        doReturn(true).when(field).isAnnotationPresent(spyAnnotation);
        doReturn(true).when(field).isAnnotationPresent(MockitoAnnotations.Mock.class);

        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        spyAnnotationEngine.assertNoAnnotations(spyAnnotation, field);
    }
}