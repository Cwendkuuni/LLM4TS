import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.SpyAnnotationEngine;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;

public class SpyAnnotationEngineTest {

    private SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();

    @Test
    public void testCreateMockFor() {
        // TODO: Implement test case for createMockFor method
    }

    @Test
    public void testProcess() {
        Class<?> context = DummyTestClass.class;
        DummyTestClass testClass = new DummyTestClass();
        
        spyAnnotationEngine.process(context, testClass);
        
        List<String> list = testClass.list;
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testProcessWithNullInstance() {
        Class<?> context = DummyTestClass.class;
        DummyTestClass testClass = new DummyTestClass();
        testClass.list = null;

        try {
            spyAnnotationEngine.process(context, testClass);
            fail("Expected MockitoException");
        } catch (RuntimeException e) {
            assertEquals("Cannot create a @Spy for 'list' field because the *instance* is missing\n" +
                        		  "The instance must be created *before* initMocks();\n" +
                                  "Example of correct usage of @Spy:\n" +
                            	  "   @Spy List mock = new LinkedList();\n" +
                            	  "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void testProcessWithMockInstance() {
        Class<?> context = DummyTestClass.class;
        DummyTestClass testClass = new DummyTestClass();
        testClass.list = Mockito.mock(List.class);

        spyAnnotationEngine.process(context, testClass);
        
        List<String> list = testClass.list;
        Mockito.verify(list).clear();
    }

    static class DummyTestClass {
        @Spy
        List<String> list = new LinkedList<>();
    }
}