import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.Mockito;
import org.mockito.internal.configuration.SpyAnnotationEngine;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Object result = spyAnnotationEngine.createMockFor(Mock.class, null);
        assertNull(result);
    }
    
    @Test
    public void testProcess_withNullInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        try {
            spyAnnotationEngine.process(SpyAnnotationEngine.class, null);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'null' field because the *instance* is missing\n The instance must be created *before* initMocks();\n Example of correct usage of @Spy:\n @Spy List mock = new LinkedList();\n //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }
    
    @Test
    public void testProcess_withMockInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Object testClass = new Object();
        Mockito.mock(Object.class);
        try {
            spyAnnotationEngine.process(SpyAnnotationEngine.class, testClass);
            fail("Expected MockitoException");
        } catch (MockitoException e) {
            assertEquals("Problems initiating spied field null", e.getMessage());
        }
    }
}