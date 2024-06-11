
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
        
        assertNotNull(annotationMap.annotations());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        assertEquals(primary, AnnotationMap.merge(primary, null));
        assertEquals(secondary, AnnotationMap.merge(null, secondary));
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
        annotationMap.addIfNotPresent(annotation);

        assertTrue(annotationMap._annotations.containsKey(TestAnnotation.class));
    }
    
    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        TestAnnotation annotation = new TestAnnotation();
        annotationMap.add(annotation);

        assertTrue(annotationMap._annotations.containsKey(TestAnnotation.class));
    }
    
    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();

        assertEquals("[null]", annotationMap.toString());
    }
    
    static class TestAnnotation implements Annotation {
        // empty annotation implementation
    }
}
