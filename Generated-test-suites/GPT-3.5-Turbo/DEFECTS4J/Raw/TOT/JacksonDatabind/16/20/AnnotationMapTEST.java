import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Override.class));

        // Typical use case
        annotationMap.add(Override.class.getAnnotation(Override.class));
        assertEquals(Override.class.getAnnotation(Override.class), annotationMap.get(Override.class));

        // Edge case
        assertNull(annotationMap.get(Deprecated.class));

        // Error scenario
        assertNull(annotationMap.get(null));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(Collections.emptyList(), annotationMap.annotations());

        // Typical use case
        annotationMap.add(Override.class.getAnnotation(Override.class));
        assertEquals(Collections.singletonList(Override.class.getAnnotation(Override.class)), annotationMap.annotations());

        // Edge case
        AnnotationMap emptyAnnotationMap = new AnnotationMap();
        assertEquals(Collections.emptyList(), emptyAnnotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        AnnotationMap annotationMap2 = new AnnotationMap();

        assertNull(AnnotationMap.merge(annotationMap1, annotationMap2));

        // Typical use case
        annotationMap1.add(Override.class.getAnnotation(Override.class));
        assertEquals(annotationMap1, AnnotationMap.merge(annotationMap1, annotationMap2));

        // Edge case
        assertNull(AnnotationMap.merge(null, null));
        assertEquals(annotationMap1, AnnotationMap.merge(annotationMap1, null));
        assertEquals(annotationMap1, AnnotationMap.merge(null, annotationMap1));

        // Error scenarios
        annotationMap2.add(null);
        assertNull(AnnotationMap.merge(annotationMap1, annotationMap2));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        // Typical use case
        annotationMap.add(Override.class.getAnnotation(Override.class));
        assertEquals(1, annotationMap.size());

        // Edge case
        AnnotationMap emptyAnnotationMap = new AnnotationMap();
        assertEquals(0, emptyAnnotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.addIfNotPresent(Override.class.getAnnotation(Override.class)));

        // Typical use case
        assertFalse(annotationMap.addIfNotPresent(Override.class.getAnnotation(Override.class)));

        // Edge case
        AnnotationMap emptyAnnotationMap = new AnnotationMap();
        assertTrue(emptyAnnotationMap.addIfNotPresent(Override.class.getAnnotation(Override.class)));
        assertFalse(emptyAnnotationMap.addIfNotPresent(Override.class.getAnnotation(Override.class)));

        // Error scenario
        assertFalse(annotationMap.addIfNotPresent(null));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.add(Override.class.getAnnotation(Override.class)));

        // Typical use case
        assertFalse(annotationMap.add(Override.class.getAnnotation(Override.class)));

        // Edge case
        AnnotationMap emptyAnnotationMap = new AnnotationMap();
        assertTrue(emptyAnnotationMap.add(Override.class.getAnnotation(Override.class)));
        assertFalse(emptyAnnotationMap.add(Override.class.getAnnotation(Override.class)));

        // Error scenario
        assertFalse(annotationMap.add(null));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());

        // Typical use case
        annotationMap.add(Override.class.getAnnotation(Override.class));
        assertEquals(annotationMap._annotations.toString(), annotationMap.toString());

        // Edge case
        AnnotationMap emptyAnnotationMap = new AnnotationMap();
        assertEquals("[null]", emptyAnnotationMap.toString());
    }
}