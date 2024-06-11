import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.MockitoAnnotations;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setUp() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void createMockFor_WhenCalled_ShouldReturnNull() {
        Annotation annotation = mock(Annotation.class);
        Field field = mock(Field.class);

        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        assertNull(result);
    }

    @Test
    public void process_WithSpyAnnotation_ShouldResetMockWhenAlreadyMocked() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationTestClass();
        MockitoAnnotations.initMocks(testClass);

        spyAnnotationEngine.process(context, testClass);

        SpyAnnotationTestClass spyAnnotationTestClass = (SpyAnnotationTestClass) testClass;
        verify(spyAnnotationTestClass.mockObj, times(1)).reset();
    }

    @Test
    public void process_WithSpyAnnotation_WhenInstanceIsNull_ShouldThrowException() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationTestClassWithNullInstance();

        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'mockObj' field because the *instance* is missing\n" +
                    "The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void process_WithSpyAnnotation_WhenInstanceIsMock_ShouldResetMock() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationTestClassWithMockInstance();
        MockitoAnnotations.initMocks(testClass);

        spyAnnotationEngine.process(context, testClass);

        SpyAnnotationTestClassWithMockInstance spyAnnotationTestClass = (SpyAnnotationTestClassWithMockInstance) testClass;
        verify(spyAnnotationTestClass.mockObj, times(1)).reset();
    }

    @Test
    public void process_WithSpyAnnotation_WhenInstanceIsNotMock_ShouldSetFieldAsSpy() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationTestClassWithNonMockInstance();

        spyAnnotationEngine.process(context, testClass);

        SpyAnnotationTestClassWithNonMockInstance spyAnnotationTestClass = (SpyAnnotationTestClassWithNonMockInstance) testClass;
        assertNotNull(spyAnnotationTestClass.mockObj);
        assertTrue(spyAnnotationTestClass.mockObj instanceof List);
        assertTrue(spyAnnotationTestClass.mockObj instanceof LinkedList);
    }

    @Test
    public void process_WithUnsupportedCombinationOfAnnotations_ShouldThrowException() throws IllegalAccessException {
        Class<?> context = SpyAnnotationEngineTest.class;
        Object testClass = new SpyAnnotationTestClassWithUnsupportedAnnotations();

        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Unsupported combination of annotations: Spy + Mock", e.getMessage());
        }
    }

    @SuppressWarnings({"unchecked"})
    private class SpyAnnotationTestClass {
        @Spy
        private List<Object> mockObj;
    }

    @SuppressWarnings({"unchecked"})
    private class SpyAnnotationTestClassWithNullInstance {
        @Spy
        private List<Object> mockObj = null;
    }

    @SuppressWarnings({"unchecked"})
    private class SpyAnnotationTestClassWithMockInstance {
        @Spy
        private List<Object> mockObj = mock(LinkedList.class);
    }

    @SuppressWarnings({"unchecked"})
    private class SpyAnnotationTestClassWithNonMockInstance {
        private List<Object> mockObj = new LinkedList<>();
    }

    private class SpyAnnotationTestClassWithUnsupportedAnnotations {
        @Spy
        @Mock
        private List<Object> mockObj;
    }
}