import org.junit.Test;
import org.mockito.internal.util.MockUtil;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor_SpyAnnotation_ReturnsNull() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Annotation annotation = mock(Spy.class);
        Field field = mock(Field.class);

        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        assertNull(result);
    }

    @Test
    public void testProcess_SpyAnnotation_NoInstance() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();

        Field[] fields = {mock(Field.class)};
        when(context.getDeclaredFields()).thenReturn(fields);
        when(fields[0].isAnnotationPresent(Spy.class)).thenReturn(true);
        when(fields[0].getName()).thenReturn("field");
        when(fields[0].get(testClass)).thenReturn(null);

        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'field' field because the *instance* is missing\n" +
                    "The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void testProcess_SpyAnnotation_InstanceIsMock() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();

        Field[] fields = {mock(Field.class)};
        when(context.getDeclaredFields()).thenReturn(fields);
        when(fields[0].isAnnotationPresent(Spy.class)).thenReturn(true);
        when(fields[0].getName()).thenReturn("field");
        Object instance = mock(Object.class);
        when(fields[0].get(testClass)).thenReturn(instance);
        when(new MockUtil().isMock(instance)).thenReturn(true);

        spyAnnotationEngine.process(context, testClass);

        verifyStatic(Mockito.class);
        Mockito.reset(instance);
    }

    @Test
    public void testProcess_SpyAnnotation_InstanceIsNotMock() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();

        Field[] fields = {mock(Field.class)};
        when(context.getDeclaredFields()).thenReturn(fields);
        when(fields[0].isAnnotationPresent(Spy.class)).thenReturn(true);
        when(fields[0].getName()).thenReturn("field");
        Object instance = new LinkedList();
        when(fields[0].get(testClass)).thenReturn(instance);

        spyAnnotationEngine.process(context, testClass);

        verify(fields[0]).set(testClass, Mockito.spy(instance));
    }

    @Test
    public void testProcess_NoSpyAnnotation() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();

        Field[] fields = {};
        when(context.getDeclaredFields()).thenReturn(fields);

        spyAnnotationEngine.process(context, testClass);

        // No assertions needed, just checking that the method runs without throwing exceptions
    }

    @Test
    public void testAssertNoAnnotations_UnsupportedCombination_ThrowsException() throws NoSuchFieldException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = SpyAnnotationEngine.class.getDeclaredField("assertNoAnnotations");
        Class<?> annotation = Spy.class;
        Class<?>[] undesiredAnnotations = {Mock.class};

        try {
            spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotations);
            fail("Expected Reporter.unsupportedCombinationOfAnnotations() to be called");
        } catch (RuntimeException e) {
            verifyStatic(Reporter.class);
            Reporter.unsupportedCombinationOfAnnotations(annotation.getSimpleName(), annotation.getClass().getSimpleName());
        }
    }
}