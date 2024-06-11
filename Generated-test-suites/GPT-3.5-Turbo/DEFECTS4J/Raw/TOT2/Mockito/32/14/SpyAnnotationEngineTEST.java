import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.configuration.SpyAnnotationEngine;

import java.lang.reflect.Field;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Object mockObject = engine.createMockFor(null, null);
        assertNull(mockObject);
    }

    @Test
    public void testProcess_withNullInstance() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Field field = mock(Field.class);
        Mockito.when(field.getName()).thenReturn("mockField");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> engine.process(SpyAnnotationEngine.class, null));
        assertEquals("Cannot create a @Spy for 'mockField' field because the *instance* is missing\n" +
                     "The instance must be created *before* initMocks();\n" +
                     "Example of correct usage of @Spy:\n" +
                     "   @Spy List mock = new LinkedList();\n" +
                     "   //also, don't forget about MockitoAnnotations.initMocks();", exception.getMessage());
    }

    @Test
    public void testProcess_resetSpy() throws IllegalAccessException {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Object testClass = new Object();
        Field field = mock(Field.class);
        Mockito.when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(field.isAccessible()).thenReturn(true);
        Mockito.when(field.getName()).thenReturn("mockField");
        Object instance = new Object();
        field.set(testClass, instance);

        engine.process(SpyAnnotationEngine.class, testClass);

        Mockito.verify(field, Mockito.times(1)).get(testClass);
        Mockito.verify(field, Mockito.times(1)).set(testClass, instance);
        Mockito.verify(field, Mockito.times(1)).setAccessible(true);
        Mockito.verify(field, Mockito.times(1)).setAccessible(false);
        assertTrue(new MockUtil().isMock(instance));
    }

    @Test
    public void testProcess_mockUtilIsMock() throws IllegalAccessException {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Object testClass = new Object();
        Field field = mock(Field.class);
        Mockito.when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(field.isAccessible()).thenReturn(true);
        Mockito.when(field.getName()).thenReturn("mockField");
        Object instance = Mockito.mock(Object.class);
        field.set(testClass, instance);

        engine.process(SpyAnnotationEngine.class, testClass);

        Mockito.verify(field, Mockito.times(1)).get(testClass);
        Mockito.verify(field, Mockito.times(1)).set(testClass, instance);
        Mockito.verify(field, Mockito.times(1)).setAccessible(true);
        Mockito.verify(field, Mockito.times(1)).setAccessible(false);
        Mockito.verify(Mockito.class).reset(instance);
    }

    @Test
    public void testProcess_illegalAccessException() throws IllegalAccessException {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Object testClass = new Object();
        Field field = mock(Field.class);
        Mockito.when(field.isAnnotationPresent(Spy.class)).thenReturn(true);
        Mockito.when(field.isAccessible()).thenReturn(true);
        Mockito.when(field.getName()).thenReturn("mockField");
        Object instance = new Object();
        field.set(testClass, instance);
        Mockito.when(field.get(testClass)).thenThrow(IllegalAccessException.class);

        MockitoException exception = assertThrows(MockitoException.class, () -> engine.process(SpyAnnotationEngine.class, testClass));
        assertEquals("Problems initiating spied field mockField", exception.getMessage());
    }

    @Test
    public void testAssertNoAnnotations() throws NoSuchFieldException {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        EngineTest.class.getDeclaredField("testField"); // Dummy test field to check annotation with AssertNoAnnotations
        Field testField = EngineTest.class.getDeclaredField("testField");
        assertThrows(Exception.class, () -> engine.assertNoAnnotations(Spy.class, testField, Mock.class, MockitoAnnotations.Mock.class, Captor.class));
    }

    private class EngineTest {
        @Mock
        private Object testField;

        @Spy
        private Object spyField;
    }
}