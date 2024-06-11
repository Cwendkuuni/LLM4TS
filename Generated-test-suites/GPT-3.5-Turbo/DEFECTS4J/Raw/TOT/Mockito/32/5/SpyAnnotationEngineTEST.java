
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;
    private Field field;
    private Object testClass;

    @Before
    public void setUp() {
        spyAnnotationEngine = new SpyAnnotationEngine();
        field = null;
        testClass = new TestClass();
    }

    // Test createMockFor method

    @Test
    public void testCreateMockFor_NullAnnotation_ReturnsNull() {
        Annotation annotation = null;
        Object result = spyAnnotationEngine.createMockFor(annotation, field);
        Assert.assertNull(result);
    }

    // Test process method

    @Test
    public void testProcess_FieldWithSpyAnnotation_NoOtherAnnotations_SuccessfulSpyCreation() throws Exception {
        field = TestClass.class.getDeclaredField("fieldWithSpyAnnotation");
        field.setAccessible(true);

        spyAnnotationEngine.process(TestClass.class, testClass);

        Object instance = field.get(testClass);
        Assert.assertNotNull(instance);
        Assert.assertTrue(new MockUtil().isMock(instance));
    }

    @Test(expected = MockitoException.class)
    public void testProcess_FieldWithSpyAnnotation_MissingInstance_ExceptionThrown() throws Exception {
        field = TestClass.class.getDeclaredField("fieldWithSpyAnnotation_MissingInstance");
        field.setAccessible(true);

        spyAnnotationEngine.process(TestClass.class, testClass);
    }

    @Test
    public void testProcess_FieldWithSpyAnnotation_SpiedInstanceReset() throws Exception {
        field = TestClass.class.getDeclaredField("fieldWithSpyAnnotation_SpiedInstance");
        field.setAccessible(true);
        Object instance = Mockito.spy(new LinkedList<String>());
        field.set(testClass, instance);

        spyAnnotationEngine.process(TestClass.class, testClass);

        Mockito.verify(instance, times(1)).clear();
    }

    @Test
    public void testProcess_FieldWithSpyAnnotation_PreviouslySpiedInstance_NotReset() throws Exception {
        field = TestClass.class.getDeclaredField("fieldWithSpyAnnotation_PreviouslySpiedInstance");
        field.setAccessible(true);
        Object instance = Mockito.spy(new LinkedList<String>());
        field.set(testClass, instance);

        spyAnnotationEngine.process(TestClass.class, testClass);

        Mockito.verify(instance, never()).clear();
    }

    @Test(expected = MockitoException.class)
    public void testProcess_SpyField_IllegalAccessException_ExceptionThrown() throws Exception {
        field = TestClass.class.getDeclaredField("illegalAccessField");

        spyAnnotationEngine.process(TestClass.class, testClass);
    }

    // Test assertNoAnnotations method

    @Test(expected = Reporter.class)
    public void testAssertNoAnnotations_SpyFieldWithMockAnnotation_ReportUnsupportedCombination() throws NoSuchFieldException {
        field = TestClass.class.getDeclaredField("fieldWithSpyAndMockAnnotation");

        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class);
    }

    @Test(expected = Reporter.class)
    public void testAssertNoAnnotations_SpyFieldWithDifferentMockAnnotations_ReportUnsupportedCombination() throws NoSuchFieldException {
        field = TestClass.class.getDeclaredField("fieldWithSpyAndDifferentMockAnnotations");

        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, MockitoAnnotations.Mock.class, Captor.class);
    }

    // Test class and fields used for testing

    private static class TestClass {

        @Spy
        private List<String> fieldWithSpyAnnotation;

        @Spy
        private List<String> fieldWithSpyAnnotation_MissingInstance;

        @Spy
        private List<String> fieldWithSpyAnnotation_SpiedInstance;

        @Spy
        private List<String> fieldWithSpyAnnotation_PreviouslySpiedInstance;

        @Mock
        private List<String> mockField;

        @Captor
        private List<String> captorField;

        @MockitoAnnotations.Mock
        private List<String> mockitoAnnotationsMockField;

        @Spy
        @Mock
        private List<String> fieldWithSpyAndMockAnnotation;

        @Spy
        @MockitoAnnotations.Mock
        private List<String> fieldWithSpyAndDifferentMockAnnotations;

        private String illegalAccessField;

    }
}

