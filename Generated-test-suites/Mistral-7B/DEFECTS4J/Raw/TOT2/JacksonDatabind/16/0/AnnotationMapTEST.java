import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class AnnotationMapTest {

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonSerialize annotation = new JsonSerialize();
        annotationMap.add(annotation);
        JsonSerialize expectedAnnotation = new JsonSerialize();

        JsonSerialize actualAnnotation = annotationMap.get(JsonSerialize.class);

        assertEquals(expectedAnnotation, actualAnnotation);
    }
}