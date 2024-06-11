
import static org.junit.Assert.*;
import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        assertNull(annotationMap.get(TestAnnotation.class));
        
        TestAnnotation testAnnotation = new TestAnnotation("test");
        annotationMap.add(testAnnotation);
        
        assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));
    }
    
    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        assertTrue(annotationMap.annotations().isEmpty());
        
        TestAnnotation testAnnotation = new TestAnnotation("test");
        annotationMap.add(testAnnotation);
        
        assertFalse(annotationMap.annotations().isEmpty());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        primary.add(new TestAnnotation("primary"));
        secondary.add(new TestAnnotation("secondary"));
        
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        
        assertEquals(2, merged.size());
    }
}
