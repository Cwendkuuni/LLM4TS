import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import org.mockito.exceptions.base.MockitoException;

import static org.junit.Assert.*;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine;

    @Before
    public void setup() {
        spyAnnotationEngine = new SpyAnnotationEngine();
    }

    @Test
    public void testCreateMockFor_NullInstance() {
        Object mock = spyAnnotationEngine.createMockFor(null, null);
        assertNull(mock);
    }

    @Test
    public void testProcess_SpyFieldInitialized() {
        class TestClass {
            @Spy
            List<String> list = new LinkedList<>();
        }
        TestClass testInstance = new TestClass();
        spyAnnotationEngine.process(testInstance.getClass(), testInstance);
        assertNotNull(testInstance.list);
    }

    @Test(expected = MockitoException.class)
    public void testProcess_NullInstanceError() {
        class TestClass {
            @Spy
            List<String> list = null;
        }
        TestClass testInstance = new TestClass();
        spyAnnotationEngine.process(testInstance.getClass(), testInstance);
    }

    @Test
    public void testProcess_ResetMockInstance() {
        class TestClass {
            @Spy
            List<String> list = new LinkedList<>();
        }
        TestClass testInstance = new TestClass();
        Mockito.when(testInstance.list.size()).thenReturn(10);
        spyAnnotationEngine.process(testInstance.getClass(), testInstance);
        assertEquals(0, testInstance.list.size());
    }

    @Test(expected = MockitoException.class)
    public void testProcess_IllegalAccessException() {
        class TestClass {
            @Spy
            List<String> list = new LinkedList<>();
        }
        TestClass testInstance = new TestClass();
        Field[] fields = testInstance.getClass().getDeclaredFields();
        fields[0].setAccessible(false); // Cause IllegalAccessException
        spyAnnotationEngine.process(testInstance.getClass(), testInstance);
    }

    @Test
    public void testProcess_UnsupportedAnnotations() {
        class TestClass {
            @Mock
            @Spy
            List<String> list = new LinkedList<>();
        }
        TestClass testInstance = new TestClass();
        spyAnnotationEngine.process(testInstance.getClass(), testInstance);
    }

}