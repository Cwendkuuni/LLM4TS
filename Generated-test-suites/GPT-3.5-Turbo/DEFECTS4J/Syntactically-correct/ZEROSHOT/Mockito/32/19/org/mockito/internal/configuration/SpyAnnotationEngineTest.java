import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setUp() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testCreateMockFor() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);
        assertNull(spyAnnotationEngine.createMockFor(annotation, field));
    }

    @Test
    public void testProcess() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new Object();
        Field field = getField("mockField");
        field.setAccessible(true);
        Object instance = new Object();

        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.get(testClass)).thenReturn(instance);
        when(new MockUtil().isMock(instance)).thenReturn(false);

        spyAnnotationEngine.process(context, testClass);

        verify(field).get(testClass);
        verify(new MockUtil()).isMock(instance);
        verify(field).set(testClass, Mockito.spy(instance));
    }

    @Test
    public void testProcess_withNullInstance() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new Object();
        Field field = getField("mockField");
        field.setAccessible(true);

        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.get(testClass)).thenReturn(null);

        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Should throw MockitoException");
        } catch (MockitoException e) {
            String expectedMessage = "Cannot create a @Spy for 'mockField' field because the *instance* is missing\n" +
                    "The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();";
            assertEquals(expectedMessage, e.getMessage());
        }

        verify(field).get(testClass);
    }

    @Test
    public void testProcess_withExistingMock() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new Object();
        Field field = getField("mockField");
        field.setAccessible(true);
        Object instance = mock(Object.class);

        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.get(testClass)).thenReturn(instance);
        when(new MockUtil().isMock(instance)).thenReturn(true);

        spyAnnotationEngine.process(context, testClass);

        verify(field).get(testClass);
        verify(new MockUtil()).isMock(instance);
        verify(instance).reset();
    }

    @Test
    public void testProcess_withUnwantedAnnotations() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new Object();
        Field field = getField("mockField");
        field.setAccessible(true);

        when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        when(field.isAnnotationPresent(Mock.class)).thenReturn(true);

        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Should throw MockitoException");
        } catch (MockitoException e) {
            String expectedMessage = "Unsupported combination of annotations: Spy and Mock";
            assertEquals(expectedMessage, e.getMessage());
        }

        verify(field).isAnnotationPresent(Spy.class);
        verify(field).isAnnotationPresent(Mock.class);
    }
    
    private Field getField(String fieldName) {
        Field[] fields = SpyAnnotationEngineTest.class.getDeclaredFields();
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }
}