import org.junit.Test;
import org.junit. runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.quality.DontWriteTests;
import org.mockito.quality.DontWriteTests.Rule;
import org.mockito.internal.configuration.SpyAnnotationEngine;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("deprecation")
public class SpyAnnotationEngineTest {

    @Rule
    public DontWriteTests dontWriteTests = new DontWriteTests();

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine engine = new SpyAnnotationEngine();
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);

        Object result = engine.createMockFor(annotation, field);
        assertNull(result);
    }
}