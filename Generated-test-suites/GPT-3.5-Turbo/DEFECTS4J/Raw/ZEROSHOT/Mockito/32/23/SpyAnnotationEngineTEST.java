import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setUp() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testCreateMockFor_Null() {
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);

        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        assert(result == null);
    }

    @Test
    public void testProcess_WithSpy() throws NoSuchFieldException, IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngineSpy = spy(spyAnnotationEngine);

        Class<?> context = DummyTestClass.class;
        DummyTestClass testClass = new DummyTestClass();
        Field field = context.getDeclaredField("spiedField");

        spyAnnotationEngineSpy.process(context, testClass);

        verify(spyAnnotationEngineSpy).assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
        verify(field).setAccessible(true);
        verify(field).get(testClass);
        verify(field).set(testClass, Mockito.spy(field.get(testClass)));
    }

    @Test
    public void testProcess_WithNullInstance() throws NoSuchFieldException, IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngineSpy = spy(spyAnnotationEngine);

        Class<?> context = DummyTestClass.class;
        DummyTestClass testClass = new DummyTestClass();
        Field field = context.getDeclaredField("nullField");

        try {
            spyAnnotationEngineSpy.process(context, testClass);
        } catch (Exception e) {
            assert(e.getMessage().equals("Cannot create a @Spy for 'nullField' field because the *instance* is missing\n" +
                    "The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();"));
        }
    }

    @Test
    public void testProcess_WithExistingMock() throws NoSuchFieldException, IllegalAccessException {
        SpyAnnotationEngine spyAnnotationEngineSpy = spy(spyAnnotationEngine);

        Class<?> context = DummyTestClass.class;
        DummyTestClass testClass = new DummyTestClass();
        Field field = context.getDeclaredField("mockField");

        Object mock = Mockito.mock(List.class);
        when(field.get(testClass)).thenReturn(mock);

        spyAnnotationEngineSpy.process(context, testClass);

        verify(reset(mock)).spy(mock);
    }

    @Test
    public void testAssertNoAnnotations() throws NoSuchFieldException {
        Field field = DummyTestClass.class.getDeclaredField("mockField");

        spyAnnotationEngine.assertNoAnnotations(Spy.class, field, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
    }

    private class DummyTestClass {
        @Spy
        private List<String> spiedField;
        @Spy
        private List<String> nullField;
        @Spy
        @Mock
        private List<String> mockField;
    }
}