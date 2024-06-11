import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.HashMap;


public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Annotation.class));
    }

    @Test
    public void testAnnotationsEmpty() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> primaryMap = new HashMap<>();
        primaryMap.put(Annotation.class, new Annotation());
        primary._annotations = primaryMap;

        AnnotationMap secondary = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> secondaryMap = new HashMap<>();
        secondaryMap.put(Annotation.class, new Annotation());
        secondary._annotations = secondaryMap;

        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertEquals(primaryMap.size() + secondaryMap.size(), result.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new Annotation();
        assertTrue(annotationMap.addIfNotPresent(annotation));
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new Annotation();
        assertTrue(annotationMap.add(annotation));
        assertFalse(annotationMap.add(annotation));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());
    }

}