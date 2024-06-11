

// JUnit 4 test cases for SpyAnnotationEngine

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
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
    public void testCreateMockFor() {
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);

        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        assertNull(result);
    }

    @Test
    public void testProcess_withSpyField() throws IllegalAccessException {
        class TestClass {
            @Spy
            List<String> list;
        }

        TestClass testClass = new TestClass();

        spyAnnotationEngine.process(TestClass.class, testClass);

        assertNotNull(testClass.list);
        assertTrue(MockUtil.isMock(testClass.list));
    }

    @Test
    public void testProcess_withNullInstance() {
        class TestClass {
            @Spy
            List<String> list;
        }

        TestClass testClass = new TestClass();

        try {
            spyAnnotationEngine.process(TestClass.class, testClass);
            fail("Expected MockitoException to be thrown");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'list' field because the *instance* is missing\n" +
                    "The instance must be created *before* initMocks();\n" +
                    "Example of correct usage of @Spy:\n" +
                    "   @Spy List mock = new LinkedList();\n" +
                    "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
        assertNull(testClass.list);
    }

    @Test
    public void testProcess_withMockInstance() throws IllegalAccessException {
        class TestClass {
            @Spy
            List<String> list;
        }

        TestClass testClass = new TestClass();
        testClass.list = mock(List.class);

        spyAnnotationEngine.process(TestClass.class, testClass);

        assertNotNull(testClass.list);
        verifyZeroInteractions(testClass.list);
    }

    @Test
    public void testProcess_withReset() throws IllegalAccessException {
        class TestClass {
            @Spy
            List<String> list;
        }

        TestClass testClass = new TestClass();
        testClass.list = Mockito.spy(new LinkedList<>());

        spyAnnotationEngine.process(TestClass.class, testClass);

        assertNotNull(testClass.list);
        verify(testClass.list).clear();
    }

    @Test
    public void testAssertNoAnnotations_withNoAnnotations() throws NoSuchFieldException {
        Class<?> annotation = Mock.class;
        Field field = TestClass.class.getDeclaredField("list");
        Class<?>[] undesiredAnnotations = {};

        spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotations);
    }

    @Test(expected = MockitoException.class)
    public void testAssertNoAnnotations_withInvalidAnnotations() throws NoSuchFieldException {
        Class<?> annotation = Spy.class;
        Field field = TestClass.class.getDeclaredField("list");
        Class<?>[] undesiredAnnotations = {Mock.class};

        spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotations);
    }

    @SuppressWarnings("all")
    private static class TestClass {
        @Mock
        List<String> list;
    }
}
