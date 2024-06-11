import static org.junit.Assert.*;

import java.lang.reflect.Field;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.internal.util.MockUtil;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setUp() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testCreateMockFor() {
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);
        assertNull(spyAnnotationEngine.createMockFor(annotation, field));
    }

    @Test(expected = MockitoException.class)
    public void testProcess_InstanceNull() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();

        MockitoAnnotations.initMocks(this);

        Field field = Mockito.mock(Field.class);
        Mockito.when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(field.getName()).thenReturn("field");
        Mockito.when(field.get(testClass)).thenReturn(null);

        spyAnnotationEngine.process(context, testClass);

        Mockito.verify(field).isAnnotationPresent(Spy.class);
        Mockito.verify(field).setAccessible(true);
        Mockito.verify(field).get(testClass);

        Mockito.verify(field).setAccessible(false);
    }

    @Test
    public void testProcess_InstanceNotMock() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();

        MockitoAnnotations.initMocks(this);

        Field field = Mockito.mock(Field.class);
        Mockito.when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(field.getName()).thenReturn("field");

        Object instance = Mockito.mock(Object.class);
        Mockito.when(field.get(testClass)).thenReturn(instance);
        Mockito.when(new MockUtil().isMock(instance)).thenReturn(false);

        spyAnnotationEngine.process(context, testClass);

        Mockito.verify(field).isAnnotationPresent(Spy.class);
        Mockito.verify(field).setAccessible(true);
        Mockito.verify(field).get(testClass);

        Mockito.verify(field).set(testClass, Mockito.spy(instance));

        Mockito.verify(field).setAccessible(false);
    }

    @Test
    public void testProcess_InstanceIsMock() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();

        MockitoAnnotations.initMocks(this);

        Field field = Mockito.mock(Field.class);
        Mockito.when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(field.getName()).thenReturn("field");

        Object instance = Mockito.mock(Object.class);
        Mockito.when(field.get(testClass)).thenReturn(instance);
        Mockito.when(new MockUtil().isMock(instance)).thenReturn(true);

        spyAnnotationEngine.process(context, testClass);

        Mockito.verify(field).isAnnotationPresent(Spy.class);
        Mockito.verify(field).setAccessible(true);
        Mockito.verify(field).get(testClass);

        Mockito.verify(Mockito.reset(instance));

        Mockito.verify(field).setAccessible(false);
    }

    @Test
    public void testAssertNoAnnotations_NoUndesiredAnnotations() throws IllegalAccessException {
        Class annotation = Mockito.mock(Class.class);
        Field field = Mockito.mock(Field.class);

        spyAnnotationEngine.assertNoAnnotations(annotation, field);

        Mockito.verifyNoInteractions(annotation);
        Mockito.verifyNoInteractions(field);
    }

    @Test(expected = Reporter.class)
    public void testAssertNoAnnotations_UndesiredAnnotationsPresent() throws IllegalAccessException {
        Class annotation = Mockito.mock(Class.class);
        Field field = Mockito.mock(Field.class);

        Class undesiredAnnotation = Mockito.mock(Class.class);
        Mockito.when(field.isAnnotationPresent(undesiredAnnotation)).thenReturn(true);

        spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotation);

        Mockito.verify(field).isAnnotationPresent(undesiredAnnotation);
        Mockito.verifyNoInteractions(annotation);
    }
}