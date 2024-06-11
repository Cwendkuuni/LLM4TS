import org.junit.Test;
import org.mockito.internal.configuration.SpyAnnotationEngine;
import static org.assertj.core.api.Assertions.assertThat;

public class SpyAnnotationEngineTest {

    @Test
    public void testCreateMockFor() {
        SpyAnnotationEngine annotationEngine = new SpyAnnotationEngine();
        Annotation annotation = Mockito.mock(Annotation.class);
        Field field = Mockito.mock(Field.class);

        Object result = annotationEngine.createMockFor(annotation, field);

        assertThat(result).isNull();
    }
}