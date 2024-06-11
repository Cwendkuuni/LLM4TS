import static org.junit.Assert.*;
import org.junit.Test;

public class AnnotationMapTest {
    
    @Test
    public void testGet() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(SomeAnnotation.class));
        
        SomeAnnotation ann = new SomeAnnotation();
        map.add(ann);
        
        assertEquals(ann, map.get(SomeAnnotation.class));
    }
    
    @Test
    public void testAnnotations() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(Collections.emptyList(), map.annotations());
        
        SomeAnnotation ann = new SomeAnnotation();
        map.add(ann);
        
        assertEquals(Collections.singletonList(ann), map.annotations());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        SomeAnnotation ann1 = new SomeAnnotation();
        SomeAnnotation ann2 = new SomeAnnotation2();
        
        primary.add(ann1);
        secondary.add(ann2);
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        assertTrue(result.contains(ann1));
        assertTrue(result.contains(ann2));
    }
    
    @Test
    public void testSize() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.size());
        
        SomeAnnotation ann = new SomeAnnotation();
        map.add(ann);
        
        assertEquals(1, map.size());
    }
    
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();
        SomeAnnotation ann = new SomeAnnotation();
        
        assertTrue(map.addIfNotPresent(ann));
        assertFalse(map.addIfNotPresent(ann)); // Already present
        
        assertEquals(1, map.size());
    }
    
    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap();
        assertEquals("[null]", map.toString());
        
        SomeAnnotation ann = new SomeAnnotation();
        map.add(ann);
        
        assertEquals("{SomeAnnotation=SomeAnnotation}", map.toString());
    }
    
    // Add more test cases for edge cases and exception handling scenarios
    
}