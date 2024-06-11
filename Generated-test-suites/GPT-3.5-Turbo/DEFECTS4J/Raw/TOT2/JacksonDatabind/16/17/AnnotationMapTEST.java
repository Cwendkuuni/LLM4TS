import static org.junit.Assert.*;
import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        // Create a new AnnotationMap
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test getting annotation when map is empty
        assertNull(annotationMap.get(SomeAnnotation.class));
        
        // Add an annotation to the map
        SomeAnnotation annotation = new SomeAnnotation();
        annotationMap.add(annotation);
        
        // Test getting the added annotation
        assertEquals(annotation, annotationMap.get(SomeAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        // Create a new AnnotationMap
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test returning empty iterable when map is empty
        assertEquals(Collections.emptyList(), annotationMap.annotations());
        
        // Add some annotations to the map
        annotationMap.add(new SomeAnnotation());
        annotationMap.add(new AnotherAnnotation());
        
        // Test returning correct iterable with added annotations
        assertEquals(2, ((List<Annotation>) annotationMap.annotations()).size());
    }

    @Test
    public void testMerge() {
        // Create two AnnotationMaps
        AnnotationMap primary = new AnnotationMap();
        primary.add(new SomeAnnotation());
        
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new AnotherAnnotation());
        
        // Test merging primary and secondary AnnotationMaps
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        
        // Ensure merged map contains all annotations from both maps
        assertTrue(merged.get(SomeAnnotation.class) != null);
        assertTrue(merged.get(AnotherAnnotation.class) != null);
    }

    @Test
    public void testAddIfNotPresent() {
        // Create a new AnnotationMap
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Add an annotation to the map
        SomeAnnotation annotation = new SomeAnnotation();
        assertTrue(annotationMap.addIfNotPresent(annotation));
        
        // Attempt to add the same annotation again
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAdd() {
        // Create a new AnnotationMap
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Add an annotation to the map
        SomeAnnotation annotation = new SomeAnnotation();
        assertTrue(annotationMap.add(annotation));
        
        // Add the same annotation again
        assertFalse(annotationMap.add(annotation));
    }

    @Test
    public void testToString() {
        // Create a new AnnotationMap
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test toString when map is empty
        assertEquals("[null]", annotationMap.toString());
        
        // Add an annotation to the map
        SomeAnnotation annotation = new SomeAnnotation();
        annotationMap.add(annotation);
        
        // Test toString when map contains annotations
        assertEquals(annotationMap._annotations.toString(), annotationMap.toString());
    }
}