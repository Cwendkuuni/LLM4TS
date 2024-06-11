import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.util.MockUtil;
import org.mockito.configuration.AnnotationEngine;

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
    public void testCreateMockFor_NullInstance_ThrowsException() {
        Field field = getMockField();
        TestClass testClass = new TestClass();
        Annotation annotation = mock(Spy.class);

        try {
            spyAnnotationEngine.createMockFor(annotation, field);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'mockField' field because the *instance* is missing\n" +
                         "The instance must be created *before* initMocks();\n" +
                         "Example of correct usage of @Spy:\n" +
                         "   @Spy List mock = new LinkedList();\n" +
                         "   //also, don't forget about MockitoAnnotations.initMocks();",
                    e.getMessage());
          }

        verifyZeroInteractions(annotation, field);
    }

    @Test
    public void testCreateMockFor_PreviousInstanceIsMock_ResetsMock() {
        Field field = getMockField();
        TestClass testClass = new TestClass();
        List<Object> mockList = mock(List.class);
        testClass.setMockField(mockList);
        Annotation annotation = mock(Spy.class);

        spyAnnotationEngine.createMockFor(annotation, field);

        verify(mockList).clear();
        verify(annotation).annotationType();
        verifyNoMoreInteractions(mockList, annotation);
    }

    @Test
    public void testCreateMockFor_PreviousInstanceIsNotMock_SpiesInstance() {
        Field field = getMockField();
        TestClass testClass = new TestClass();
        List<Object> instance = new LinkedList<>();
        testClass.setMockField(instance);
        Annotation annotation = mock(Spy.class);

        spyAnnotationEngine.createMockFor(annotation, field);

        assertNotSame(instance, testClass.getMockField());
        assertTrue(testClass.getMockField() instanceof List);
        assertTrue(new MockUtil().isSpy(testClass.getMockField()));
        verify(annotation).annotationType();
        verifyNoMoreInteractions(annotation);
    }

    @Test
    public void testProcess_OneFieldWithSpyAnnotation_ResetsMockAndVerifiesInvocation() {
        Class<?> context = TestClass.class;
        TestClass testClass = new TestClass();
        Field field = getMockField();
        testClass.setMockField(new LinkedList<>());

        spyAnnotationEngine.process(context, testClass);

        verify(testClass.getMockField()).clear();
        verifyNoMoreInteractions(testClass.getMockField());
    }

    @Test
    public void testProcess_OneFieldWithoutSpyAnnotation_DoesNothing() {
        Class<?> context = TestClass.class;
        TestClass testClass = new TestClass();
        Field field = getFieldWithoutAnnotations();
        testClass.setMockField(new LinkedList<>());

        spyAnnotationEngine.process(context, testClass);

        verifyZeroInteractions(testClass.getMockField());
    }

    @Test
    public void testProcess_OneFieldWithInvalidAnnotations_ThrowsException() {
        Class<?> context = TestClass.class;
        TestClass testClass = new TestClass();
        Field field = getFieldWithInvalidAnnotations();
        testClass.setMockField(new LinkedList<>());

        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Problems initiating spied field invalidField", e.getMessage());
        }

        verifyZeroInteractions(testClass.getMockField());
    }

    @Test
    public void testProcess_MultipleFieldsWithSpyAnnotation_ResetsMocksAndVerifiesInvocations() {
        Class<?> context = MultipleFieldsTestClass.class;
        MultipleFieldsTestClass testClass = new MultipleFieldsTestClass();
        Field field1 = getMockField();
        Field field2 = getMockField();
        testClass.setMockField1(new LinkedList<>());
        testClass.setMockField2(new LinkedList<>());

        spyAnnotationEngine.process(context, testClass);

        verify(testClass.getMockField1()).clear();
        verify(testClass.getMockField2()).clear();
        verifyNoMoreInteractions(testClass.getMockField1(), testClass.getMockField2());
    }

    @Test
    public void testAssertNoAnnotations_DoesNotThrowExceptionIfNoUndesiredAnnotations() {
        Field field = getMockField();

        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);

        verifyZeroInteractions(field);
    }

    @Test
    public void testAssertNoAnnotations_ThrowsExceptionIfUndesiredAnnotationsPresent() {
        Field field = getMockFieldWithDesiredAnnotations();

        try {
            spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
            fail("Expected Reporter to be thrown");
        } catch (Reporter e) {
            assertEquals("unsupportedCombinationOfAnnotations(\"Spy\", \"Spy\")", e.getMessageWithoutStackTrace());
        }

        verifyZeroInteractions(field);
    }

    private Field getMockField() {
        try {
            return TestClass.class.getDeclaredField("mockField");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Field getFieldWithoutAnnotations() {
        try {
            return TestClass.class.getDeclaredField("nonAnnotatedField");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Field getFieldWithInvalidAnnotations() {
        try {
            return TestClass.class.getDeclaredField("invalidField");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Field getMockFieldWithDesiredAnnotations() {
        try {
            return TestClass.class.getDeclaredField("mockFieldWithDesiredAnnotations");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class TestClass {
        @Mock
        private List<Object> mockField;

        private List<Object> nonAnnotatedField;

        @Captor
        @Mock
        private List<Object> mockFieldWithDesiredAnnotations;

        private List<Object> invalidField;

        public void setMockField(List<Object> mockField) {
            this.mockField = mockField;
        }

        public List<Object> getMockField() {
            return mockField;
        }
    }

    private class MultipleFieldsTestClass {
        @Mock
        private List<Object> mockField1;

        @Mock
        private List<Object> mockField2;

        public void setMockField1(List<Object> mockField1) {
            this.mockField1 = mockField1;
        }

        public List<Object> getMockField1() {
            return mockField1;
        }

        public void setMockField2(List<Object> mockField2) {
            this.mockField2 = mockField2;
        }

        public List<Object> getMockField2() {
            return mockField2;
        }
    }
}