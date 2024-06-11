import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;

import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);

        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Object result = engine.createMockFor(annotation, field);

        assertNull(result);
    }

    @Test
    public void testCreateMockFor_AnnotationPresent() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);

        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Object result = engine.createMockFor(annotation, field);

        assertNotNull(result);
    }

    @Test
    public void testProcess_NoSpyAnnotation() {
        Class mockitoInternalConfigurationClass = SpyAnnotationEngine.class;
        Object testClass = mock(Object.class);

        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(mockitoInternalConfigurationClass, testClass);
    }

    @Test
    public void testProcess_SpyAnnotationPresent() {
        Class mockitoInternalConfigurationClass = SpyAnnotationEngine.class;
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);
        Object instance = mock(Object.class);

        Mockito.doReturn(field).when(testClass).getDeclaredField("testField");
        Mockito.doReturn(instance).when(field).get(testClass);

        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(mockitoInternalConfigurationClass, testClass);

        verify(field).set(testClass, Mockito.isInstanceOf(SpyAnnotationEngine.Spy.class));
    }

    @Test(expected = MockitoException.class)
    public void testProcess_InstanceMissing() {
        Class mockitoInternalConfigurationClass = SpyAnnotationEngine.class;
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);

        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(mockitoInternalConfigurationClass, testClass);

        verify(field).set(testClass, Mockito.isInstanceOf(SpyAnnotationEngine.Spy.class));

        doThrow(new MockitoException("Instance missing"))
                .when(field)
                .get(testClass);

        engine.process(mockitoInternalConfigurationClass, testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcess_IllegalAccessException() {
        Class mockitoInternalConfigurationClass = SpyAnnotationEngine.class;
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);

        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(mockitoInternalConfigurationClass, testClass);

        verify(field).set(testClass, Mockito.isInstanceOf(SpyAnnotationEngine.Spy.class));

        doThrow(new IllegalAccessException("Exception"))
                .when(field)
                .get(testClass);

        engine.process(mockitoInternalConfigurationClass, testClass);
    }

    @Test
    public void testProcess_MultipleSpyAnnotations() {
        Class mockitoInternalConfigurationClass = SpyAnnotationEngine.class;
        Object testClass = mock(Object.class);
        Field field1 = mock(Field.class);
        Field field2 = mock(Field.class);

        Mockito.doReturn(field1).when(testClass).getDeclaredField("testField1");
        Mockito.doReturn(field2).when(testClass).getDeclaredField("testField2");
        Object instance1 = mock(Object.class);
        Object instance2 = mock(Object.class);

        Mockito.doReturn(instance1).when(field1).get(testClass);
        Mockito.doReturn(instance2).when(field2).get(testClass);

        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(mockitoInternalConfigurationClass, testClass);

        verify(field1, times(1)).set(testClass, Mockito.isInstanceOf(SpyAnnotationEngine.Spy.class));
        verify(field2, times(1)).set(testClass, Mockito.isInstanceOf(SpyAnnotationEngine.Spy.class));
    }

    @Test(expected = MockitoException.class)
    public void testProcess_AnnotationAndMockAnnotationPresent() {
        Class mockitoInternalConfigurationClass = SpyAnnotationEngine.class;
        Object testClass = mock(Object.class);
        Field field = mock(Field.class);
        Object instance = mock(Object.class);

        Mockito.doReturn(field).when(testClass).getDeclaredField("testField");
        Mockito.doReturn(instance).when(field).get(testClass);
        Annotation mockAnnotation = mock(Annotation.class);

        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        engine.process(mockitoInternalConfigurationClass, testClass);

        verify(field, times(1)).set(testClass, Mockito.isInstanceOf(SpyAnnotationEngine.Spy.class));

        doThrow(new MockitoException("Exception"))
                .when(field)
                .set(testClass, mockAnnotation);

        engine.process(mockitoInternalConfigurationClass, testClass);
    }
}