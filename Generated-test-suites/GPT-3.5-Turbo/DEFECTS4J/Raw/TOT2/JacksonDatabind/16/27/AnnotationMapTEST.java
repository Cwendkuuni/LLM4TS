import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.Collections;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Deprecated.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(0, merged.size());
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.addIfNotPresent(Deprecated.class.getAnnotation(Deprecated.class)));
        assertFalse(annotationMap.addIfNotPresent(Deprecated.class.getAnnotation(Deprecated.class)));
    }


    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.add(Deprecated.class.getAnnotation(Deprecated.class)));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());
    }

}