import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.Collections;

public class AnnotationMapTest {

    // Test annotation retrieval when map is empty
    @Test
    public void testGet_EmptyMap_ReturnsNull() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(Annotation.class));
    }

    // Test annotation retrieval when map contains the requested annotation
    @Test
    public void testGet_AnnotationPresent_ReturnsAnnotation() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new Annotation() {};
        map.add(annotation);
        
        assertEquals(annotation, map.get(Annotation.class));
    }
    
    // Test annotation retrieval when map does not contain the requested annotation
    @Test
    public void testGet_AnnotationNotPresent_ReturnsNull() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(Annotation.class));
    }

    // Test annotation merging when both maps are empty
    @Test
    public void testMerge_BothMapsEmpty_ReturnsNull() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertNull(result);
    }
    
    // Test annotation merging when primary map is empty and secondary map contains annotations
    @Test
    public void testMerge_PrimaryMapEmpty_ReturnsSecondaryMap() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        Annotation annotation = new Annotation() {};
        secondary.add(annotation);
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertEquals(secondary, result);
    }
    
    // Test annotation merging when secondary map is empty and primary map contains annotations
    @Test
    public void testMerge_SecondaryMapEmpty_ReturnsPrimaryMap() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        Annotation annotation = new Annotation() {};
        primary.add(annotation);
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, result);
    }
    
    // Test annotation merging when both maps contain annotations
    @Test
    public void testMerge_BothMapsContainAnnotations_ReturnsMergedMap() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        Annotation annotation1 = new Annotation() {};
        Annotation annotation2 = new Annotation() {};
        primary.add(annotation1);
        secondary.add(annotation2);
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertEquals(annotation1, result.get(annotation1.annotationType()));
        assertEquals(annotation2, result.get(annotation2.annotationType()));
    }
    
    // Test adding annotation to empty map
    @Test
    public void testAddIfNotPresent_EmptyMap_AddsAnnotation() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new Annotation() {};
        
        assertTrue(map.addIfNotPresent(annotation));
        assertEquals(annotation, map.get(annotation.annotationType()));
    }
    
    // Test adding annotation to map that already contains the annotation
    @Test
    public void testAddIfNotPresent_AnnotationPresent_ReturnsFalse() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new Annotation() {};
        map.add(annotation);
        
        assertFalse(map.addIfNotPresent(annotation));
    }
    
    // Test adding annotation to map that does not contain the annotation
    @Test
    public void testAddIfNotPresent_AnnotationNotPresent_AddsAnnotation() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new Annotation() {};
        
        assertTrue(map.addIfNotPresent(annotation));
        assertEquals(annotation, map.get(annotation.annotationType()));
    }
    
    // Test adding annotation to empty map
    @Test
    public void testAdd_EmptyMap_AddsAnnotation() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new Annotation() {};
        
        assertTrue(map.add(annotation));
        assertEquals(annotation, map.get(annotation.annotationType()));
    }
    
    // Test adding annotation to map that already contains the annotation
    @Test
    public void testAdd_AnnotationPresent_ReturnsFalse() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new Annotation() {};
        map.add(annotation);
        
        assertFalse(map.add(annotation));
    }
    
    // Test adding annotation to map that does not contain the annotation
    @Test
    public void testAdd_AnnotationNotPresent_AddsAnnotation() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new Annotation() {};
        
        assertTrue(map.add(annotation));
        assertEquals(annotation, map.get(annotation.annotationType()));
    }
    
    // Test size of empty map
    @Test
    public void testSize_EmptyMap_ReturnsZero() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.size());
    }
    
    // Test size of map with annotations
    @Test
    public void testSize_MapWithAnnotations_ReturnsNumberOfAnnotations() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation1 = new Annotation() {};
        Annotation annotation2 = new Annotation() {};
        map.add(annotation1);
        map.add(annotation2);
        
        assertEquals(2, map.size());
    }
    
    // Test string representation of empty map
    @Test
    public void testToString_EmptyMap_ReturnsNull() {
        AnnotationMap map = new AnnotationMap();
        assertEquals("[null]", map.toString());
    }
    
    // Test string representation of map with annotations
    @Test
    public void testToString_MapWithAnnotations_ReturnsStringRepresentation() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation1 = new Annotation() {};
        Annotation annotation2 = new Annotation() {};
        map.add(annotation1);
        map.add(annotation2);
        
        assertEquals(annotation1.toString() + ", " + annotation2.toString(), map.toString());
    }
    
    // Test annotations method when map is empty
    @Test
    public void testAnnotations_EmptyMap_ReturnsEmptyList() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(Collections.emptyList(), map.annotations());
    }
    
    // Test annotations method when map contains annotations
    @Test
    public void testAnnotations_MapWithAnnotations_ReturnsListWithAnnotations() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation1 = new Annotation() {};
        Annotation annotation2 = new Annotation() {};
        map.add(annotation1);
        map.add(annotation2);
        
        List<Annotation> expected = Arrays.asList(annotation1, annotation2);
        assertEquals(expected, map.annotations());
    }
}