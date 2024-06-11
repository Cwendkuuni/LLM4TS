import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setUp() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testCreateMockFor_shouldReturnNull() {
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);

        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        assertNull(result);
    }

    @Test
    public void testProcess_shouldHandleNoAnnotatedFields() {
        Class<?> context = TestClass.class;
        Object testClass = Mockito.mock(TestClass.class);
        Field[] fields = new Field[0];

        when(context.getDeclaredFields()).thenReturn(fields);

        spyAnnotationEngine.process(context, testClass);

        verify(context).getDeclaredFields();
        verifyNoInteractions(testClass);
    }

    @Test
    public void testProcess_shouldHandleSpyAnnotatedField_nullInstance() throws IllegalAccessException {
        Class<?> context = TestClass.class;
        TestClass testClass = new TestClass();
        Field spyAnnotatedField = null;

        try {
            spyAnnotationEngine.process(context, testClass);
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'spyAnnotatedField' field because the *instance* is missing\n" +
                    "The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }

        assertNull(testClass.getSpyAnnotatedField());
    }

    @Test
    public void testProcess_shouldHandleSpyAnnotatedField_existingInstance_notMock() throws IllegalAccessException {
        Class<?> context = TestClass.class;
        TestClass testClass = new TestClass();
        Field spyAnnotatedField = testClass.getClass().getDeclaredField("spyAnnotatedField");

        spyAnnotationEngine.process(context, testClass);

        assertNotNull(testClass.getSpyAnnotatedField());
        assertEquals(LinkedList.class, testClass.getSpyAnnotatedField().getClass());
        assertNotEquals(Mockito.class, testClass.getSpyAnnotatedField().getClass());
    }

    @Test
    public void testProcess_shouldHandleSpyAnnotatedField_existingInstance_mock() throws IllegalAccessException {
        Class<?> context = TestClass.class;
        TestClass testClass = new TestClass();
        List<String> mockedInstance = Mockito.mock(List.class);
        testClass.setSpyAnnotatedField(mockedInstance);
        Field spyAnnotatedField = testClass.getClass().getDeclaredField("spyAnnotatedField");

        spyAnnotationEngine.process(context, testClass);

        assertNotNull(testClass.getSpyAnnotatedField());
        assertEquals(LinkedList.class, testClass.getSpyAnnotatedField().getClass());
        assertNotEquals(mockedInstance, testClass.getSpyAnnotatedField());
        verify(mockedInstance).clear();
    }

    @Test(expected = MockitoException.class)
    public void testProcess_shouldThrowExceptionForIllegalAccessException() throws IllegalAccessException {
        Class<?> context = TestClass.class;
        TestClass testClass = new TestClass();
        Field spyAnnotatedField = testClass.getClass().getDeclaredField("spyAnnotatedField");
        spyAnnotatedField.setAccessible(false);

        spyAnnotationEngine.process(context, testClass);
    }

    @Test
    public void testAssertNoAnnotations_shouldNotThrowExceptionForNoUndesiredAnnotations() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("spyAnnotatedField");

        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class);

        verifyNoInteractions(Mockito.mock(Reporter.class));
    }

    @Test(expected = MockitoException.class)
    public void testAssertNoAnnotations_shouldThrowExceptionForUnsupportedCombinationOfAnnotations() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("spyAnnotatedField");

        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, Captor.class);

        verify(Mockito.mock(Reporter.class)).unsupportedCombinationOfAnnotations(
                Mockito.anyString(), Mockito.anyString());
    }

    private static class TestClass {
        @Spy
        private List<String> spyAnnotatedField;

        public List<String> getSpyAnnotatedField() {
            return spyAnnotatedField;
        }

        public void setSpyAnnotatedField(List<String> spyAnnotatedField) {
            this.spyAnnotatedField = spyAnnotatedField;
        }
    }
}