import org.junit.Test;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import static org.junit.Assert.*;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        assertNull(annotationEngine.createMockFor(null, null));
    }
}