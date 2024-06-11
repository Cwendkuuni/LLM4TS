import static org.junit.Assert.*;
import org.junit.Test;

public class AnnotationMapTest {
    
    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(TestAnnotation.class));
        
        TestAnnotation testAnnotation = new TestAnnotation();
        annotationMap.add(testAnnotation);
        
        assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));
    }
    
    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(Collections.emptyList(), annotationMap.annotations());
        
        TestAnnotation testAnnotation = new TestAnnotation();
        annotationMap.add(testAnnotation);
        
        assertEquals(Collections.singletonList(testAnnotation), annotationMap.annotations());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primaryMap = new AnnotationMap();
        AnnotationMap secondaryMap = new AnnotationMap();
        
        TestAnnotation testAnnotation = new TestAnnotation();
        secondaryMap.add(testAnnotation);
        
        AnnotationMap mergedMap = AnnotationMap.merge(primaryMap, secondaryMap);
        
        assertEquals(testAnnotation, mergedMap.get(TestAnnotation.class));
    }
    
    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
        
        TestAnnotation testAnnotation = new TestAnnotation();
        annotationMap.add(testAnnotation);
        
        assertEquals(1, annotationMap.size());
    }
    
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        
        assertTrue(annotationMap.addIfNotPresent(testAnnotation));
        assertFalse(annotationMap.addIfNotPresent(testAnnotation));
    }
    
    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        
        assertTrue(annotationMap.add(testAnnotation));
        assertFalse(annotationMap.add(testAnnotation));
    }
    
    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());
        
        TestAnnotation testAnnotation = new TestAnnotation();
        annotationMap.add(testAnnotation);
        
        assertEquals("{class TestAnnotation=TestAnnotation}", annotationMap.toString());
    }
    
    private static class TestAnnotation implements Annotation {
        // Test Annotation class for testing purposes
    }
}