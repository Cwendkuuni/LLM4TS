import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Test case 1: Check if the map returns null when no annotations are present
        assertNull(annotationMap.get(Override.class));
        
        // Test case 2: Check if the map returns the correct annotation when present
        Test annotation = annotationMap.get(Test.class);
        assertNotNull(annotation);
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Test case 1: Check if the map returns an empty iterable when no annotations are present
        assertTrue(annotationMap.annotations().isEmpty());
        
        // Test case 2: Check if the map returns the correct iterable when annotations are present
        annotationMap.addIfNotPresent(new TestAnnotation());
        assertFalse(annotationMap.annotations().isEmpty());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        // Test case 1: Check if the merge returns the secondary map when the primary map is empty
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertSame(result, secondary);
        
        // Test case 2: Check if the merge returns the primary map when the secondary map is empty
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertSame(result, primary);
        
        // Test case 3: Check if the merge combines the annotations when both maps are non-empty
        primary.addIfNotPresent(new TestAnnotation());
        secondary.addIfNotPresent(new AnotherAnnotation());
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertEquals(2, result.size());
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Test case 1: Check if the size is 0 when no annotations are present
        assertEquals(0, annotationMap.size());
        
        // Test case 2: Check if the size is correct when annotations are added
        annotationMap.addIfNotPresent(new TestAnnotation());
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Test case 1: Check if the annotation is added when not already present
        assertTrue(annotationMap.addIfNotPresent(new TestAnnotation()));
        
        // Test case 2: Check if the annotation is not added when already present
        assertFalse(annotationMap.addIfNotPresent(new TestAnnotation()));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Test case 1: Check if the annotation is added
        assertTrue(annotationMap.add(new TestAnnotation()));
        
        // Test case 2: Check if the annotation is added and changes the contents
        assertFalse(annotationMap.add(new TestAnnotation()));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Test case 1: Check if the string representation is "[null]" when no annotations are present
        assertEquals("[null]", annotationMap.toString());
        
        // Test case 2: Check if the string representation is correct when annotations are present
        annotationMap.addIfNotPresent(new TestAnnotation());
        assertEquals("{class com.example.TestAnnotation=TestAnnotation}", annotationMap.toString());
    }
}