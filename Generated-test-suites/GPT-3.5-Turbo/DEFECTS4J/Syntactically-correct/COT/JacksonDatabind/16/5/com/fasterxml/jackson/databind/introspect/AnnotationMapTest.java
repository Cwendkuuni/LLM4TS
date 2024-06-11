
import org.junit.Test;

import static org.junit.Assert.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Test.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.annotations().isEmpty());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        assertNull(AnnotationMap.merge(null, null));
        assertEquals(primary, AnnotationMap.merge(primary, null));
        assertEquals(secondary, AnnotationMap.merge(null, secondary));
        assertEquals(primary, AnnotationMap.merge(primary, secondary));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        TestAnnotation annotation = new TestAnnotation();
        assertTrue(annotationMap.addIfNotPresent(annotation));
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        TestAnnotation annotation = new TestAnnotation();
        assertTrue(annotationMap.add(annotation));
        assertFalse(annotationMap.add(annotation));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());
    }

    @Test
    public void testAddHelperMethod() {
        AnnotationMap annotationMap = new AnnotationMap();
        TestAnnotation annotation = new TestAnnotation();
        assertTrue(annotationMap._add(annotation));
        assertFalse(annotationMap._add(annotation));
    }

    @Test
    public void testPrivateConstructor() throws Exception {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNotNull(annotationMap.getClass().getDeclaredConstructor());
        annotationMap.getClass().getDeclaredConstructor().setAccessible(true);
        annotationMap.getClass().getDeclaredConstructor().newInstance();
    }
}
