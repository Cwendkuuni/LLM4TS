import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap map = new AnnotationMap();
        
        // Test case 1: No annotations added, should return null
        assertNull(map.get(SomeAnnotation.class));
        
        // Test case 2: Add an annotation and test retrieval
        SomeAnnotation annotation = new SomeAnnotation();
        map.add(annotation);
        assertEquals(annotation, map.get(SomeAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap map = new AnnotationMap();
        
        // Test case 1: No annotations added, should return empty iterable
        assertFalse(map.annotations().iterator().hasNext());
        
        // Test case 2: Add an annotation and test retrieval
        SomeAnnotation annotation = new SomeAnnotation();
        map.add(annotation);
        assertTrue(map.annotations().iterator().hasNext());
        assertEquals(annotation, map.annotations().iterator().next());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        // Test case 1: Both maps are empty, should return empty map
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(0, merged.size());
        
        // Test case 2: Secondary map is empty, should return primary map
        SomeAnnotation annotation1 = new SomeAnnotation();
        primary.add(annotation1);
        merged = AnnotationMap.merge(primary, secondary);
        assertEquals(1, merged.size());
        assertEquals(annotation1, merged.get(SomeAnnotation.class));
        
        // Test case 3: Primary map is empty, should return secondary map
        SomeOtherAnnotation annotation2 = new SomeOtherAnnotation();
        secondary.add(annotation2);
        merged = AnnotationMap.merge(primary, secondary);
        assertEquals(1, merged.size());
        assertEquals(annotation2, merged.get(SomeOtherAnnotation.class));
        
        // Test case 4: Both maps have annotations, secondary should override primary
        merged = AnnotationMap.merge(primary, secondary);
        assertEquals(2, merged.size());
        assertEquals(annotation2, merged.get(SomeOtherAnnotation.class));
        assertEquals(annotation1, merged.get(SomeAnnotation.class));
    }

    @Test
    public void testSize() {
        AnnotationMap map = new AnnotationMap();
        
        // Test case 1: Empty map, should return 0
        assertEquals(0, map.size());
        
        // Test case 2: Add annotations and test size
        SomeAnnotation annotation1 = new SomeAnnotation();
        SomeOtherAnnotation annotation2 = new SomeOtherAnnotation();
        
        map.add(annotation1);
        map.add(annotation2);
        
        assertEquals(2, map.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();
        
        // Test case 1: Add an annotation for the first time
        SomeAnnotation annotation1 = new SomeAnnotation();
        assertTrue(map.addIfNotPresent(annotation1));
        assertEquals(annotation1, map.get(SomeAnnotation.class));
        
        // Test case 2: Add the same annotation again, should return false
        assertFalse(map.addIfNotPresent(annotation1));
        
        // Test case 3: Add a different annotation, should return true
        SomeOtherAnnotation annotation2 = new SomeOtherAnnotation();
        assertTrue(map.addIfNotPresent(annotation2));
        assertEquals(annotation2, map.get(SomeOtherAnnotation.class));
        
        // Test case 4: Add an annotation with the same type but different values, should return true
        SomeAnnotation annotation3 = new SomeAnnotation("value2");
        assertTrue(map.addIfNotPresent(annotation3));
        assertEquals(annotation3, map.get(SomeAnnotation.class));
    }

    @Test
    public void testAdd() {
        AnnotationMap map = new AnnotationMap();
        
        // Test case 1: Add an annotation and test retrieval
        SomeAnnotation annotation1 = new SomeAnnotation();
        assertTrue(map.add(annotation1));
        assertEquals(annotation1, map.get(SomeAnnotation.class));
        
        // Test case 2: Add the same annotation again, should return true
        assertFalse(map.add(annotation1));
        
        // Test case 3: Add a different annotation, should return true
        SomeOtherAnnotation annotation2 = new SomeOtherAnnotation();
        assertTrue(map.add(annotation2));
        assertEquals(annotation2, map.get(SomeOtherAnnotation.class));
        
        // Test case 4: Add an annotation with the same type but different values, should return true
        SomeAnnotation annotation3 = new SomeAnnotation("value2");
        assertTrue(map.add(annotation3));
        assertEquals(annotation3, map.get(SomeAnnotation.class));
    }

    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap();
        
        // Test case 1: Empty map
        assertEquals("[null]", map.toString());
        
        // Test case 2: Add annotations and test toString()
        SomeAnnotation annotation1 = new SomeAnnotation();
        SomeOtherAnnotation annotation2 = new SomeOtherAnnotation();
        
        map.add(annotation1);
        map.add(annotation2);
        
        String expected = "{" + SomeAnnotation.class.getName() + "=" + annotation1.toString() + ", " + SomeOtherAnnotation.class.getName() + "=" + annotation2.toString() + "}";
        
        assertEquals(expected, map.toString());
    }

    // Add additional test cases for potential edge cases and exception handling scenarios
    // ...

}