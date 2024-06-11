import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

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
    public void testProcess_withSpyAnnotation_NotNullInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        TestClassWithSpyAnnotation testClass = new TestClassWithSpyAnnotation();

        spyAnnotationEngine.process(TestClassWithSpyAnnotation.class, testClass);

        assertNotNull(testClass.objectWithSpy);
        assertTrue(Mockito.mockingDetails(testClass.objectWithSpy).isMock());
    }

    @Test(expected = MockitoException.class)
    public void testProcess_withSpyAnnotation_NullInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        TestClassWithSpyAnnotationWithNullInstance testClass = new TestClassWithSpyAnnotationWithNullInstance();

        spyAnnotationEngine.process(TestClassWithSpyAnnotationWithNullInstance.class, testClass);
    }

    @Test(expected = MockitoException.class)
    public void testProcess_withSpyAnnotation_InvalidFieldAccess() throws Exception {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        TestClassWithSpyAnnotation testClass = new TestClassWithSpyAnnotation();
        Field field = TestClassWithSpyAnnotation.class.getDeclaredField("objectWithSpy");
        field.setAccessible(false);

        spyAnnotationEngine.process(TestClassWithSpyAnnotation.class, testClass);
    }

    @Test
    public void testProcess_withSpyAnnotation_MockInstanceReset() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        TestClassWithMockInstance testClass = new TestClassWithMockInstance();

        spyAnnotationEngine.process(TestClassWithMockInstance.class, testClass);

        assertNotNull(testClass.mockInstance);
        assertFalse(Mockito.mockingDetails(testClass.mockInstance).isMock());
    }

    @Test
    public void testProcess_withSpyAnnotation_NewSpyInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        TestClassWithFieldWithoutInstance testClass = new TestClassWithFieldWithoutInstance();

        spyAnnotationEngine.process(TestClassWithFieldWithoutInstance.class, testClass);

        assertNotNull(testClass.fieldWithoutInstance);
        assertTrue(Mockito.mockingDetails(testClass.fieldWithoutInstance).isMock());
    }

    @Test
    public void testAssertNoAnnotations_NoUndesiredAnnotations() throws Exception {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = TestClassWithFieldWithoutInstance.class.getDeclaredField("fieldWithoutInstance");

        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, MockitoAnnotations.Mock.class, Captor.class);
    }

    @Test(expected = MockitoException.class)
    public void testAssertNoAnnotations_WithUndesiredAnnotations() throws Exception {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Field field = TestClassWithSpyAnnotation.class.getDeclaredField("objectWithSpy");

        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, MockitoAnnotations.Mock.class, Captor.class);
    }

    private static class TestClassWithSpyAnnotation {
        @Spy
        private List<String> objectWithSpy = new LinkedList<>();
    }

    private static class TestClassWithSpyAnnotationWithNullInstance {
        @Spy
        private List<String> objectWithSpy;
    }

    private static class TestClassWithMockInstance {
        @Mock
        private List<String> mockInstance = Mockito.mock(List.class);
    }

    private static class TestClassWithFieldWithoutInstance {
        @Spy
        private List<String> fieldWithoutInstance;
    }
}