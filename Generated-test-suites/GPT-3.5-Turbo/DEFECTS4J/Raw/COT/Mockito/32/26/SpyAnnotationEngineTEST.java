
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);

        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        assertNull(result);
    }

    @Test
    public void testProcess() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();

        spyAnnotationEngine.process(context, testClass);

        // Verify the processed fields were properly spied
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class)) {
                field.setAccessible(true);
                Object instance = field.get(testClass);
                assertNotNull(instance);
                assertTrue(new MockUtil().isMock(instance));
            }
        }
    }
    
    @Test
    public void testProcess_missingInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();

        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'mock' field because the *instance* is missing\n" +
                    "The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void testProcess_existingMock() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();
        List mock = Mockito.mock(List.class);
        Mockito.when(mock.size()).thenReturn(10);

        try {
            Field field = context.getDeclaredField("mock");
            field.setAccessible(true);
            field.set(testClass, mock);
        } catch (NoSuchFieldException e) {
            fail(e.getMessage());
        }

        spyAnnotationEngine.process(context, testClass);

        // Verify the processed fields were properly reset
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class)) {
                field.setAccessible(true);
                Object instance = field.get(testClass);
                assertNotNull(instance);
                assertEquals(0, ((List)instance).size());
            }
        }
    }

    @Test
    public void testProcess_existingNonMock() throws IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationEngineTest();
        List instance = new LinkedList();

        try {
            Field field = context.getDeclaredField("mock");
            field.setAccessible(true);
            field.set(testClass, instance);
        } catch (NoSuchFieldException e) {
            fail(e.getMessage());
        }

        spyAnnotationEngine.process(context, testClass);

        // Verify the processed fields were properly spied
        Field[] fields = context.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Spy.class)) {
                field.setAccessible(true);
                Object spiedInstance = field.get(testClass);
                assertNotNull(spiedInstance);
                assertNotSame(instance, spiedInstance);
            }
        }
    }

    @Test
    public void testAssertNoAnnotations_noAnnotations() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> annotation = Spy.class;
        Field field = Mockito.mock(Field.class);

        spyAnnotationEngine.assertNoAnnotations(annotation, field);

        // No exception thrown
    }

    @Test
    public void testAssertNoAnnotations_withAnnotations() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Class<?> annotation = Spy.class;
        Field field = Mockito.mock(Field.class);

        Mockito.when(field.isAnnotationPresent(Mock.class)).thenReturn(true);

        try {
            spyAnnotationEngine.assertNoAnnotations(annotation, field);
            fail("Expected Reporter.unsupportedCombinationOfAnnotations() to be called");
        } catch (MockitoException e) {
            assertEquals("Supported combinations of annotations are: @Spy and any other annotations\n" +
                    "Invalid use of @Spy annotation on field 'null'\n" +
                    "This annotation is not applicable here.", e.getMessage());
        }
    }

}
