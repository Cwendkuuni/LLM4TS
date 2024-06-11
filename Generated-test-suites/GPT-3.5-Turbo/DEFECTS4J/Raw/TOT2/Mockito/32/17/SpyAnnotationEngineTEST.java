import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        assertNull(spyAnnotationEngine.createMockFor(null, null));
    }
    
    @Test
    public void testProcess() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        try {
            spyAnnotationEngine.process(null, null);
        } catch (MockitoException e) {
            assertEquals("Cannot create a @Spy for 'null' field because the *instance* is missing\n" +
                         "The instance must be created *before* initMocks();\n" +
                         "Example of correct usage of @Spy:\n" +
                         "   @Spy List mock = new LinkedList();\n" +
                         "   //also, don't forget about MockitoAnnotations.initMocks();", e.getMessage());
        }
    }

    @Test
    public void testAssertNoAnnotations() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        try {
            spyAnnotationEngine.assertNoAnnotations(Spy.class, null, Mock.class, org.mockito.MockitoAnnotations.Mock.class, Captor.class);
        } catch (Reporter e) {
            assertEquals("unsupportedCombinationOfAnnotations", e.getMessage());
        }
    }

}