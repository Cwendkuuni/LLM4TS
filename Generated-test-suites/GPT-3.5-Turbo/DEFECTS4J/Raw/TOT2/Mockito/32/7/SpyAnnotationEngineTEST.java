import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.SpyAnnotationEngine;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor_NullInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        Object mock = spyAnnotationEngine.createMockFor(null, null);
        assert mock == null;
    }

    @Test
    public void testProcess_NullInstance() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        try {
            spyAnnotationEngine.process(null, null);
        } catch (Exception e) {
            assert e instanceof NullPointerException;
        }
    }

    @Test
    public void testAssertNoAnnotations() {
        SpyAnnotationEngine spyAnnotationEngine = new SpyAnnotationEngine();
        try {
            Class<?> annotation = Class.forName("org.mockito.Spy");
            Class<?> field = Class.forName("org.mockito.Mock");
            Class<?>[] undesiredAnnotations = {Class.forName("org.mockito.Captor")};
            spyAnnotationEngine.assertNoAnnotations(annotation, field, undesiredAnnotations);
        } catch (Exception e) {
            assert e instanceof Reporter;
        }
    }
}