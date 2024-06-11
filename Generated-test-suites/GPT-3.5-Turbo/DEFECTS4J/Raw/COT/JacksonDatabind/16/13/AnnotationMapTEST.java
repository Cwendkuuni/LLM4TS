import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Add test for non-existing annotation class
        assertNull(annotationMap.get(TestAnnotation.class));

        // Add test for existing annotation class
        TestAnnotation testAnnotation = new TestAnnotationImpl();
        annotationMap.add(testAnnotation);
        assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Add test for empty annotations
        assertFalse(annotationMap.annotations().iterator().hasNext());

        // Add test for non-empty annotations
        TestAnnotation testAnnotation = new TestAnnotationImpl();
        annotationMap.add(testAnnotation);
        assertTrue(annotationMap.annotations().iterator().hasNext());
        assertEquals(testAnnotation, annotationMap.annotations().iterator().next());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        // Add test when both primary and secondary are null
        assertNull(AnnotationMap.merge(null, null));

        // Add test when primary is null
        AnnotationMap merged = AnnotationMap.merge(null, secondary);
        assertEquals(secondary, merged);

        // Add test when secondary is null
        merged = AnnotationMap.merge(primary, null);
        assertEquals(primary, merged);

        // Add test when primary and secondary are both empty
        merged = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, merged);

        // Add test when primary is not empty and secondary is empty
        primary.add(new TestAnnotationImpl());
        merged = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, merged);

        // Add test when primary is empty and secondary is not empty
        secondary.add(new TestAnnotationImpl());
        merged = AnnotationMap.merge(primary, secondary);
        assertEquals(secondary, merged);

        // Add test when primary and secondary are both non-empty
        primary.add(new TestAnnotationImpl());
        merged = AnnotationMap.merge(primary, secondary);
        assertNotEquals(primary, merged);
        assertNotEquals(secondary, merged);
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Add test for empty annotations
        assertEquals(0, annotationMap.size());

        // Add test for non-empty annotations
        TestAnnotation testAnnotation = new TestAnnotationImpl();
        annotationMap.add(testAnnotation);
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Add test for adding non-existing annotation class
        TestAnnotation testAnnotation = new TestAnnotationImpl();
        assertTrue(annotationMap.addIfNotPresent(testAnnotation));

        // Add test for adding existing annotation class
        assertFalse(annotationMap.addIfNotPresent(testAnnotation));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Add test for adding annotation class
        TestAnnotation testAnnotation = new TestAnnotationImpl();
        assertTrue(annotationMap.add(testAnnotation));
        assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Add test for empty annotations
        assertEquals("[null]", annotationMap.toString());

        // Add test for non-empty annotations
        TestAnnotation testAnnotation = new TestAnnotationImpl();
        annotationMap.add(testAnnotation);
        assertTrue(annotationMap.toString().contains(testAnnotation.toString()));
    }
}