import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.LinkedList;

import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

    @Test
    public void testCreateMockFor() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        assertNull(result);
    }

    @Test
    public void testProcess() throws Exception {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();

        Field field = context.getDeclaredField("fields");
        field.setAccessible(true);
        Field[] fields = {field};

        SpyAnnotationEngine spyAnnotationEngine = spy(SpyAnnotationEngine.class);
        doNothing().when(spyAnnotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
        doReturn(true).when(spyAnnotationEngine).isAccessible(field);
        doReturn(testClass).when(field).get(testClass);
        doReturn(true).when(new MockUtil()).isMock(testClass);

        spyAnnotationEngine.process(context, testClass);

        verify(spyAnnotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
        verify(field).setAccessible(true);
        verify(field).get(testClass);
        verify(new MockUtil()).isMock(testClass);
        verify(Mockito).reset(testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcess_NullInstance() throws Exception {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();

        Field field = context.getDeclaredField("fields");
        field.setAccessible(true);
        Field[] fields = {field};

        SpyAnnotationEngine spyAnnotationEngine = spy(SpyAnnotationEngine.class);
        doNothing().when(spyAnnotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
        doReturn(true).when(spyAnnotationEngine).isAccessible(field);
        doReturn(null).when(field).get(testClass);

        spyAnnotationEngine.process(context, testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcess_IllegalAccessException() throws Exception {
        Class<?> context = SpyAnnotationEngine.class;
        Object testClass = new Object();

        Field field = context.getDeclaredField("fields");
        field.setAccessible(true);
        Field[] fields = {field};

        SpyAnnotationEngine spyAnnotationEngine = spy(SpyAnnotationEngine.class);
        doNothing().when(spyAnnotationEngine).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
        doReturn(true).when(spyAnnotationEngine).isAccessible(field);
        doThrow(new IllegalAccessException()).when(field).get(testClass);

        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void testAssertNoAnnotations() throws Exception {
        Class annotation = Spy.class;
        Field field = mock(Field.class);
        Class[] undesiredAnnotations = {Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class};

        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        new Reporter().unsupportedCombinationOfAnnotations(annotation.getSimpleName(), annotation.getClass().getSimpleName());
        spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotations);
    }

}