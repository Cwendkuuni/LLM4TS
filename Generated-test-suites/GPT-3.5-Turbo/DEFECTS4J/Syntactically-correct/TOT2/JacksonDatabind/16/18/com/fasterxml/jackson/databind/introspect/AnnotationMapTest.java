import static org.junit.Assert.*;
import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(SomeAnnotation.class));

        SomeAnnotation annotation = new SomeAnnotation();
        annotationMap.add(annotation);

        assertEquals(annotation, annotationMap.get(SomeAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.annotations().iterator().hasNext());

        SomeAnnotation annotation = new SomeAnnotation();
        annotationMap.add(annotation);

        assertEquals(1, annotationMap.annotations().spliterator().estimateSize());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        SomeAnnotation annotation1 = new SomeAnnotation();
        SomeAnnotation annotation2 = new SomeAnnotation();

        primary.add(annotation1);
        secondary.add(annotation2);

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);

        assertEquals(2, merged.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();

        SomeAnnotation annotation = new SomeAnnotation();
        annotationMap.add(annotation);

        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());

        SomeAnnotation annotation = new SomeAnnotation();
        annotationMap.add(annotation);

        assertEquals("{class SomeAnnotation=SomeAnnotation}", annotationMap.toString());
    }
}