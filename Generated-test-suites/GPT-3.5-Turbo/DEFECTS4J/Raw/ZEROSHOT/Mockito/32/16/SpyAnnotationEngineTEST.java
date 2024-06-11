import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;
    private Object testClass;

    @Before
    public void setup() {
        spyAnnotationEngine = new SpyAnnotationEngine();
        testClass = new Object();
    }

    @Test
    public void testCreateMockFor() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);

        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        Assert.assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateMockFor_nullAnnotation() {
        Field field = mock(Field.class);

        spyAnnotationEngine.createMockFor(null, field);
    }

    @Test(expected = NullPointerException.class)
    public void testCreateMockFor_nullField() {
        Annotation annotation = mock(Annotation.class);

        spyAnnotationEngine.createMockFor(annotation, null);
    }

    @Test
    public void testProcess_withSpyAnnotation() throws Exception {
        Class<?> context = SpyAnnotationEngine.class;
        Field field = context.getDeclaredField("fields");

        spyAnnotationEngine.process(context, testClass);

        verify(field, times(1)).isAnnotationPresent(Spy.class);
    }

    @Test
    public void testProcess_noSpyAnnotation() throws Exception {
        Class<?> context = SpyAnnotationEngine.class;

        spyAnnotationEngine.process(context, testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcess_missingInstance() throws Exception {
        Class<?> context = SpyAnnotationEngine.class;
        Field field = context.getDeclaredField("fields");

        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void testProcess_mockInstance() throws Exception {
        Class<?> context = SpyAnnotationEngine.class;
        Field field = context.getDeclaredField("fields");
        Object instance = mock(Object.class);
        field.set(testClass, instance);

        spyAnnotationEngine.process(context, testClass);

        verify(instance, times(1)).reset();
    }

    @Test
    public void testProcess_spyInstance() throws Exception {
        Class<?> context = SpyAnnotationEngine.class;
        Field field = context.getDeclaredField("fields");
        List instance = new LinkedList();
        field.set(testClass, instance);

        spyAnnotationEngine.process(context, testClass);

        Assert.assertEquals(Mockito.spy(instance), field.get(testClass));
    }

    @Test(expected = MockitoException.class)
    public void testProcess_illegalAccessException() throws Exception {
        Class<?> context = SpyAnnotationEngine.class;
        Field field = context.getDeclaredField("fields");
        field.setAccessible(false);

        spyAnnotationEngine.process(context, testClass);
    }

}