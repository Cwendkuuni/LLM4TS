
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
        
        assertEquals(0, annotationMap.annotations().spliterator().getExactSizeIfKnown());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        assertNotNull(result);
        assertEquals(0, result.size());
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
    
    private class TestAnnotation implements Annotation {}
}
