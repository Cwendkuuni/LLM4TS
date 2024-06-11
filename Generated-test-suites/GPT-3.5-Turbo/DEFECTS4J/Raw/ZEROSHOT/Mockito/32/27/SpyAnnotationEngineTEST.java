import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Mock
    private Reporter reporter;

    @Mock
    private MockitoException mockitoException;

    private Field field;

    private Object testClass;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        spyAnnotationEngine = new SpyAnnotationEngine();
        field = null;
        testClass = null;
    }

    @Test
    public void testCreateMockFor() {
        Annotation annotation = Mockito.mock(Annotation.class);
        Object mock = spyAnnotationEngine.createMockFor(annotation, field);
        assertEquals(null, mock);
    }

    @Test
    public void testProcess() throws IllegalAccessException {
        field = SpyAnnotationEngine.class.getDeclaredFields()[0];
        field.setAccessible(true);
        testClass = new Object();

        spyAnnotationEngine.process(SpyAnnotationEngine.class, testClass);

        assertNotNull(field.get(testClass));
    }

    @Test
    public void testProcess_withExistingSpy() throws IllegalAccessException {
        field = SpyAnnotationEngine.class.getDeclaredFields()[0];
        field.setAccessible(true);
        testClass = new Object();
        field.set(testClass, spy(new LinkedList<>()));

        spyAnnotationEngine.process(SpyAnnotationEngine.class, testClass);

        reset(field.get(testClass));
    }

    @Test
    public void testProcess_withMissingInstance() throws IllegalAccessException {
        field = SpyAnnotationEngine.class.getDeclaredFields()[0];
        field.setAccessible(true);
        testClass = new Object();

        try {
            spyAnnotationEngine.process(SpyAnnotationEngine.class, testClass);
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for '" + field.getName() + "' field because the *instance* is missing\n" +
                    "The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void testProcess_withIllegalAccessException() throws IllegalAccessException {
        field = SpyAnnotationEngine.class.getDeclaredFields()[0];
        field.setAccessible(false);
        testClass = new Object();

        try {
            spyAnnotationEngine.process(SpyAnnotationEngine.class, testClass);
        } catch (MockitoException e) {
            assertEquals("Problems initiating spied field " + field.getName(), e.getMessage());
        }
    }

    @Test
    public void testAssertNoAnnotations() throws NoSuchFieldException {
        Field field = SpyAnnotationEngine.class.getDeclaredField("field");
        reporter.unsupportedCombinationOfAnnotations(annotation.getSimpleName(), annotation.getClass().getSimpleName());
    }
}