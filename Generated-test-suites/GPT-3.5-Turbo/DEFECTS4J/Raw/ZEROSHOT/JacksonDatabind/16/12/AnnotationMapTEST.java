import static org.junit.Assert.*;
import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(SomeAnnotation.class));
        
        SomeAnnotation annotation = new SomeAnnotation();
        map.add(annotation);
        assertEquals(annotation, map.get(SomeAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap map = new AnnotationMap();
        assertNotNull(map.annotations());
        
        SomeAnnotation annotation = new SomeAnnotation();
        map.add(annotation);
        assertEquals(Collections.singletonList(annotation), map.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        assertNull(AnnotationMap.merge(null, secondary));
        assertNull(AnnotationMap.merge(primary, null));
        
        SomeAnnotation primaryAnnotation = new SomeAnnotation();
        SomeAnnotation secondaryAnnotation = new SomeAnnotation();
        
        primary.add(primaryAnnotation);
        secondary.add(secondaryAnnotation);
        
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertNotNull(merged);
        assertEquals(primaryAnnotation, merged.get(SomeAnnotation.class));
        assertEquals(secondaryAnnotation, merged.get(SomeAnnotation.class));
    }

    @Test
    public void testSize() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.size());
        
        SomeAnnotation annotation = new SomeAnnotation();
        map.add(annotation);
        assertEquals(1, map.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();
        SomeAnnotation annotation = new SomeAnnotation();
        assertTrue(map.addIfNotPresent(annotation));
        assertFalse(map.addIfNotPresent(annotation));
    }

    @Test
    public void testAdd() {
        AnnotationMap map = new AnnotationMap();
        SomeAnnotation annotation = new SomeAnnotation();
        assertTrue(map.add(annotation));
        assertFalse(map.add(annotation));
    }

    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap();
        assertEquals("[null]", map.toString());
        
        SomeAnnotation annotation = new SomeAnnotation();
        map.add(annotation);
        assertEquals("{class SomeAnnotation=SomeAnnotation}", map.toString());
    }
    
    @SomeAnnotation
    private static class SomeAnnotatedClass {}
    
    @interface SomeAnnotation {}
}