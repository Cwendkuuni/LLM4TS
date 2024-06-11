import static org.junit.Assert.*;
import org.junit.Test;

public class AnnotationMapTest {
    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        assertNull(annotationMap.get(Override.class));
        assertNull(annotationMap.get(Deprecated.class));
        
        annotationMap.addIfNotPresent(new Override() {});
        annotationMap.addIfNotPresent(new Deprecated() {});
        
        assertNotNull(annotationMap.get(Override.class));
        assertNotNull(annotationMap.get(Deprecated.class));
    }
    
    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        Iterable<Annotation> annotations = annotationMap.annotations();
        
        assertFalse(annotations.iterator().hasNext());
        
        annotationMap.addIfNotPresent(new Override() {});
        annotationMap.addIfNotPresent(new Deprecated() {});
        
        annotations = annotationMap.annotations();
        
        assertTrue(annotations.iterator().hasNext());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        AnnotationMap merged = AnnotationMap.merge(null, secondary);
        assertEquals(secondary, merged);
        
        merged = AnnotationMap.merge(primary, null);
        assertEquals(primary, merged);
        
        merged = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, merged);
        
        merged = AnnotationMap.merge(secondary, primary);
        assertEquals(secondary, merged);
        
        primary.addIfNotPresent(new Override() {});
        secondary.addIfNotPresent(new Deprecated() {});
        
        merged = AnnotationMap.merge(primary, secondary);
        assertNotNull(merged.get(Override.class));
        assertNotNull(merged.get(Deprecated.class));
    }
    
    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        assertEquals(0, annotationMap.size());
        
        annotationMap.addIfNotPresent(new Override() {});
        
        assertEquals(1, annotationMap.size());
    }
    
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        assertTrue(annotationMap.addIfNotPresent(new Override() {}));
        
        assertFalse(annotationMap.addIfNotPresent(new Override() {}));
    }
    
    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        assertTrue(annotationMap.add(new Override() {}));
        
        assertFalse(annotationMap.add(new Override() {}));
    }
    
    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        assertEquals("[null]", annotationMap.toString());
        
        annotationMap.addIfNotPresent(new Override() {});
        
        assertNotEquals("[null]", annotationMap.toString());
    }
}