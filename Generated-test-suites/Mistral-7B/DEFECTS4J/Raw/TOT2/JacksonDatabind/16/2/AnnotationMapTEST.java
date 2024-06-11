import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class AnnotationMapTest {

    @Test
    public void testAnnotationMapCreation() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNotNull(annotationMap);
    }
}