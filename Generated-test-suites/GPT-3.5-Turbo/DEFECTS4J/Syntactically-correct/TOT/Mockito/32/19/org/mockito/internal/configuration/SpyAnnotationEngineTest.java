import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testCreateMockFor_NullInstance_ThrowsException() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("mockField");
        Assert.assertThrows(MockitoException.class, () -> {
            spyAnnotationEngine.createMockFor(Spy.class, field);
        });
    }

    @Test
    public void testProcess_FieldIsMock_ResetCalled() throws NoSuchFieldException, IllegalAccessException {
        Field field = TestClass.class.getDeclaredField("mockField");
        TestClass testClass = new TestClass();
        field.set(testClass, new LinkedList<>());

        spyAnnotationEngine.process(TestClass.class, testClass);

        List<?> instance = (List<?>) field.get(testClass);
        Assert.assertTrue(instance.isEmpty());
    }

    @Test
    public void testProcess_FieldIsNotMock_SpyCreated() throws NoSuchFieldException, IllegalAccessException {
        Field field = TestClass.class.getDeclaredField("mockField");
        TestClass testClass = new TestClass();
        field.set(testClass, new LinkedList<>());

        spyAnnotationEngine.process(TestClass.class, testClass);

        List<?> instance = (List<?>) field.get(testClass);
        Assert.assertNotNull(instance);
        Assert.assertNotEquals(testClass.mockField, instance);
    }

    @Test
    public void testAssertNoAnnotations_FieldWithUndesiredAnnotation_ThrowsException() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("wrongAnnotationField");
        Assert.assertThrows(MockitoException.class, () -> {
            spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class);
        });
    }

    @Test
    public void testAssertNoAnnotations_FieldWithoutUndesiredAnnotation_NoExceptionThrown() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("noAnnotationField");
        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class);
    }

    private static class TestClass {
        @Spy
        private List<?> mockField;

        @Mock
        private List<?> wrongAnnotationField;

        private List<?> noAnnotationField;
    }
}