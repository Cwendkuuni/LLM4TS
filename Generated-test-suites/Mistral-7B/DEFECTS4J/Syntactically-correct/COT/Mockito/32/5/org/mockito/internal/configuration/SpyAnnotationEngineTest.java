import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.exceptions.Reporter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@RunWith(MockitoJUnitRunner.class)
public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockForNullAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Assert.assertNull(annotationEngine.createMockFor(null, null));
    }

    @Test
    public void testCreateMockForSpyAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class testClass = Mockito.mock(Class.class);
        Field field = Mockito.mock(Field.class);
        Annotation spyAnnotation = Mockito.mock(Spy.class);

        Assert.assertNull(annotationEngine.createMockFor(null, field));
        Assert.assertNull(annotationEngine.createMockFor(spyAnnotation, null));

        field.when(testClass).getDeclaredField("testField");
        field.when(testClass.getDeclaredFields()).thenReturn(new Field[]{field});

        try {
            annotationEngine.process(testClass, testClass);
            Assert.assertNotNull(field.get(testClass));
        } catch (IllegalAccessException e) {
            Assert.fail("Unexpected IllegalAccessException: " + e.getMessage());
        }

        Object instance = Mockito.mock(Object.class);
        field.set(testClass, instance);

        Assert.assertEquals(instance, annotationEngine.createMockFor(spyAnnotation, field));
    }

    @Test(expected = MockitoException.class)
    public void testProcessNullContext() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Object testClass = Mockito.mock(Object.class);
        annotationEngine.process(null, testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcessNullTestClass() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class<?> context = Mockito.mock(Class.class);
        annotationEngine.process(context, null);
    }

    @Test
    public void testProcessNoSpyAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class testClass = Mockito.mock(Class.class);
        Object instance = Mockito.mock(Object.class);
        Field field = Mockito.mock(Field.class);

        field.when(testClass.getDeclaredFields()).thenReturn(new Field[]{field});
        field.when(testClass.getDeclaredField("testField")).thenReturn(field);

        annotationEngine.process(testClass, testClass);
        Assert.assertNull(field.get(testClass));
    }

    @Test
    public void testProcessAlreadySpyedInstance() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class testClass = Mockito.mock(Class.class);
        Object instance = Mockito.mock(Object.class);
        Field field = Mockito.mock(Field.class);

        field.when(testClass.getDeclaredFields()).thenReturn(new Field[]{field});
        field.when(testClass.getDeclaredField("testField")).thenReturn(field);

        field.when(testClass).getDeclaredField("testField");
        field.when(testClass.getDeclaredFields()).thenReturn(new Field[]{field});

        field.setAccessible(true);
        try {
            field.set(testClass, instance);
            field.set(testClass, Mockito.spy(instance));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            field.setAccessible(false);
        }

        assertNoExceptionThrown(()->annotationEngine.process(testClass, testClass));
        Assert.assertEquals(instance, field.get(testClass));
    }

    @Test(expected = MockitoException.class)
    public void testProcessNoAccessToField() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class testClass = Mockito.mock(Class.class);
        Object instance = Mockito.mock(Object.class);
        Field field = Mockito.mock(Field.class);

        field.when(testClass.getDeclaredFields()).thenReturn(new Field[]{field});
        field.when(testClass.getDeclaredField("testField")).thenReturn(field);

        field.when(field).isAccessible().thenReturn(false);

        annotationEngine.process(testClass, testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcessNullInstance() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class testClass = Mockito.mock(Class.class);
        Object instance = null;
        Field field = Mockito.mock(Field.class);

        field.when(testClass.getDeclaredFields()).thenReturn(new Field[]{field});
        field.when(testClass.getDeclaredField("testField")).thenReturn(field);

        annotationEngine.process(testClass, testClass);
        field.set(testClass, instance);
    }

    @Test(expected = MockitoException.class)
    public void testProcessEmptyFields() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class testClass = Mockito.mock(Class.class);

        annotationEngine.process(testClass, testClass);
    }

    @Test
    public void testAssertNoAnnotationsNoSpyAnnotation() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class testClass = Mockito.mock(Class.class);
        Field field = Mockito.mock(Field.class);

        Assert.assertDoesNotThrow(() -> annotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class));
    }

    @Test(expected = Reporter.UnsupportedCombinationOfAnnotations.class)
    public void testAssertNoAnnotationsSpyAndMockAnnotations() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Class testClass = Mockito.mock(Class.class);
        Field field = Mockito.mock(Field.class);

        field.when(testClass.getDeclaredFields()).thenReturn(new Field[]{field});
        field.when(testClass.getDeclaredField("testField")).thenReturn(field);
        field.when(field).isAnnotationPresent(Mockito.Mock.class).thenReturn(true);

        Assert.assertThrows(Reporter.UnsupportedCombinationOfAnnotations.class, () -> annotationEngine.assertNoAnnotations(Spy.class, field, Mockito.Mock.class));
    }

    private void assertNoExceptionThrown(Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}