import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

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
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);

        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        Assert.assertNull(result);
    }

    @Test
    public void testProcess() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngine.class;
        Field field = Mockito.mock(Field.class);
        Field[] fields = { field };
        Object instance = new Object();

        Mockito.when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(field.getName()).thenReturn("fieldName");
        Mockito.when(field.get(testClass)).thenReturn(instance);
        Mockito.when(new MockUtil().isMock(instance)).thenReturn(false);

        spyAnnotationEngine.process(context, testClass);

        Mockito.verify(field).isAccessible();
        Mockito.verify(field).setAccessible(true);
        Mockito.verify(field).get(testClass);
        Mockito.verify(field).set(testClass, Mockito.spy(instance));
        Mockito.verify(field).setAccessible(false);
    }

    @Test(expected = Reporter.class)
    public void testAssertNoAnnotations() throws NoSuchFieldException {
        Class<Mock> annotation = Mock.class;
        Field field = getClass().getDeclaredField("testClass");
        Class[] undesiredAnnotations = { Mock.class };

        spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotations);
    }

    @Test
    public void testAssertNoAnnotations_withNoAnnotations() throws NoSuchFieldException {
        Class<Mock> annotation = Mock.class;
        Field field = getClass().getDeclaredField("testClass");
        Class[] undesiredAnnotations = { Captor.class };

        spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotations);
    }

    @Test
    public void testAssertNoAnnotations_withDifferentAnnotations() throws NoSuchFieldException {
        Class<Spy> annotation = Spy.class;
        Field field = getClass().getDeclaredField("testClass");
        Class[] undesiredAnnotations = { Mock.class };

        spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotations);
    }
}