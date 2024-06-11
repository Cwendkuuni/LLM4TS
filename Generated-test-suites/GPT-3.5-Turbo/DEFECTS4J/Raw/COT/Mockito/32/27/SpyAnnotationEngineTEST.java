import org.junit.Before;
import org.junit.Test;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.configuration.AnnotationEngine;
import org.mockito.exceptions.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.internal.util.MockUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setUp() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void createMockFor_ShouldReturnNull() {
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);

        Object result = spyAnnotationEngine.createMockFor(annotation, field);

        assertNull(result);
    }

    @Test
    public void process_ShouldCreateSpyAndResetMock() throws IllegalAccessException {
        class TestClass {
            @Spy
            List<String> list;
        }
        TestClass testClass = new TestClass();

        spyAnnotationEngine.process(testClass.getClass(), testClass);

        assertNotNull(testClass.list);
        assertTrue(new MockUtil().isMock(testClass.list));
        Mockito.verify(testClass.list).clear();
    }

    @Test(expected = MockitoException.class)
    public void process_ShouldThrowExceptionWhenInstanceNotFound() throws IllegalAccessException {
        class TestClass {
            @Spy
            List<String> list;
        }
        TestClass testClass = new TestClass();
        testClass.list = null;

        spyAnnotationEngine.process(testClass.getClass(), testClass);
    }

    @Test
    public void process_ShouldResetMockIfInstanceAlreadySpied() throws IllegalAccessException {
        class TestClass {
            @Spy
            List<String> list;
        }
        TestClass testClass = new TestClass();
        testClass.list = Mockito.spy(new LinkedList<>());

        spyAnnotationEngine.process(testClass.getClass(), testClass);

        assertNotNull(testClass.list);
        assertTrue(new MockUtil().isMock(testClass.list));
        Mockito.verify(testClass.list).clear();
    }

    @Test(expected = MockitoException.class)
    public void process_ShouldThrowExceptionWhenIllegalAccessExceptionOccurs() throws IllegalAccessException {
        class TestClass {
            @Spy
            private List<String> list;
        }
        TestClass testClass = new TestClass();

        spyAnnotationEngine.process(testClass.getClass(), testClass);
    }

    @Test
    public void assertNoAnnotations_ShouldNotThrowExceptionWhenNoUndesiredAnnotationsPresent() throws NoSuchFieldException {
        class TestClass {
            @Spy
            @Mock
            @Captor
            private List<String> list;
        }
        Field field = TestClass.class.getDeclaredField("list");
        Class<Spy> annotationClass = Spy.class;
        Class<Mock> undesiredAnnotation = Mock.class;
        Class<Captor> anotherUndesiredAnnotation = Captor.class;

        spyAnnotationEngine.assertNoAnnotations(annotationClass, field, undesiredAnnotation, anotherUndesiredAnnotation);
    }

    @Test(expected = MockitoException.class)
    public void assertNoAnnotations_ShouldThrowExceptionWhenUndesiredAnnotationPresent() throws NoSuchFieldException {
        class TestClass {
            @Spy
            @Mock
            private List<String> list;
        }
        Field field = TestClass.class.getDeclaredField("list");
        Class<Spy> annotationClass = Spy.class;
        Class<Mock> undesiredAnnotation = Mock.class;
        Class<Captor> anotherUndesiredAnnotation = Captor.class;

        spyAnnotationEngine.assertNoAnnotations(annotationClass, field, undesiredAnnotation, anotherUndesiredAnnotation);
    }
}