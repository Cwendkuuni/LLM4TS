import static org.junit.Assert.*;
import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        assertEquals(null, annotationMap.get(MyAnnotation.class));
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new MyAnnotation());
        
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new AnotherAnnotation());
        
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        
        assertEquals(2, merged.size());
    }
    
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        assertTrue(annotationMap.add(new MyAnnotation()));
        assertFalse(annotationMap.addIfNotPresent(new MyAnnotation()));
    }
    
    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation());
        
        assertEquals("{class com.fasterxml.jackson.databind.introspect.MyAnnotation=MyAnnotation@123456}", annotationMap.toString());
    }

}