import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Mock
    private Annotation annotation;

    @Spy
    private Field field;

    @Mock
    private Field mockField;

    @Mock
    private Field mockitoAnnotationsMockField;

    @Captor
    private Field captorField;

    @Mock
    private Class<?> context;

    @Mock
    private Object testClass;

    @Test
    public void testCreateMockFor() {
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        assertEquals(null, result);
    }

    @Before
    public void setUp() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testProcess() throws Exception {
        Field[] fields = new Field[]{field, mockField, mockitoAnnotationsMockField, captorField};
        Mockito.when(context.getDeclaredFields()).thenReturn(fields);
        Mockito.when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(field.getName()).thenReturn("testField");
        Mockito.when(field.isAccessible()).thenReturn(false);
        Mockito.when(field.get(testClass)).thenReturn(null);

        spyAnnotationEngine.process(context, testClass);

        Mockito.verify(field, Mockito.times(1)).setAccessible(true);
        Mockito.verify(field, Mockito.times(1)).setAccessible(false);
        Mockito.verify(field, Mockito.times(1)).get(testClass);
        Mockito.verify(field, Mockito.times(1)).getName();
        Mockito.verify(context, Mockito.times(1)).getDeclaredFields();
        Mockito.verifyNoMoreInteractions(field, context);
    }

    @Test(expected = MockitoException.class)
    public void testProcess_withNullInstance() throws Exception {
        Mockito.when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(field.getName()).thenReturn("testField");
        Mockito.when(field.isAccessible()).thenReturn(true);
        Mockito.when(field.get(testClass)).thenReturn(null);

        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void testProcess_withSpiedInstance() throws Exception {
        Object instance = spy(new LinkedList<String>());
        Mockito.when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(field.getName()).thenReturn("testField");
        Mockito.when(field.isAccessible()).thenReturn(true);
        Mockito.when(field.get(testClass)).thenReturn(instance);

        spyAnnotationEngine.process(context, testClass);

        Mockito.verify(field, Mockito.times(1)).set(testClass, instance);
        Mockito.verify(mockField, Mockito.times(1)).set(testClass, instance);
        Mockito.verify(mockitoAnnotationsMockField, Mockito.times(1)).set(testClass, instance);
        Mockito.verifyNoMoreInteractions(field, mockField, mockitoAnnotationsMockField);
    }

    @Test(expected = MockitoException.class)
    public void testProcess_withIllegalAccessException() throws Exception {
        Mockito.when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(field.getName()).thenReturn("testField");
        Mockito.when(field.isAccessible()).thenReturn(true);
        Mockito.when(field.get(testClass)).thenThrow(new IllegalAccessException());

        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void testAssertNoAnnotations_withUndesiredAnnotations() {
        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
        assertEquals(true, true);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAssertNoAnnotations_withAnnotations() {
        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
    }
}