import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    @Mock
    private Field field;

    @Test
    public void createMockFor_shouldReturnNull() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Object result = spyAnnotationEngine.createMockFor(null, null);
        assertNull(result);
    }

    @Test
    public void assertNoAnnotations_shouldThrowExceptionForUnsupportedCombinationOfAnnotations() throws NoSuchFieldException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        try {
            spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, MockitoAnnotations.Mock.class, Captor.class);
            fail("Expected exception not thrown");
        } catch (MockitoException e) {
            assertEquals("Unsupported combination of annotations: Spy and Mock", e.getMessage());
        }
    }

    @Test
    public void assertNoAnnotations_shouldNotThrowException() throws NoSuchFieldException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        try {
            spyAnnotationEngine.assertNoAnnotations(null, field);
        } catch (MockitoException e) {
            fail("Unexpected exception thrown");
        }
    }

    @Test
    public void process_shouldSetAccessibleToTrueWhenAccessibleIsFalse() throws NoSuchFieldException, IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field testField = spyAnnotationEngine.getClass().getDeclaredField("field");
        testField.setAccessible(false);

        spyAnnotationEngine.process(SpyAnnotationEngine.class, this);

        assertTrue(testField.isAccessible());
    }

    @Test
    public void process_shouldResetMockInstanceIfInstanceIsMock() throws NoSuchFieldException, IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field testField = spyAnnotationEngine.getClass().getDeclaredField("field");
        testField.setAccessible(true);
        TestClass testClass = new TestClass();

        when(field.get(testClass)).thenReturn(mock(Object.class));
        spyAnnotationEngine.process(SpyAnnotationEngine.class, testClass);

        verify(field, times(1)).get(testClass);
        verify(field, times(1)).set(testClass, Mockito.spy(testClass));
        verify(field, times(1)).set(testClass, null);
    }

    @Test
    public void process_shouldThrowExceptionIfInstanceIsNull() throws NoSuchFieldException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field testField = spyAnnotationEngine.getClass().getDeclaredField("field");
        testField.setAccessible(true);
        TestClass testClass = new TestClass();

        try {
            spyAnnotationEngine.process(SpyAnnotationEngine.class, testClass);
            fail("Expected exception not thrown");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'field' field because the *instance* is missing\n" +
            		"The instance must be created *before* initMocks();\n" +
            		"Example of correct usage of @Spy:\n" +
            		"   @Spy List mock = new LinkedList();\n" +
            		"   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void process_shouldSetFieldToSpyInstance() throws NoSuchFieldException, IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field testField = spyAnnotationEngine.getClass().getDeclaredField("field");
        testField.setAccessible(true);
        TestClass testClass = new TestClass();

        spyAnnotationEngine.process(SpyAnnotationEngine.class, testClass);

        assertFalse(Mockito.mockingDetails(testField.get(testClass)).isMock());
        assertTrue(Mockito.mockingDetails(testField.get(testClass)).isSpy());
    }

    private static class TestClass {
        @Spy
        private Object field;
    }
}