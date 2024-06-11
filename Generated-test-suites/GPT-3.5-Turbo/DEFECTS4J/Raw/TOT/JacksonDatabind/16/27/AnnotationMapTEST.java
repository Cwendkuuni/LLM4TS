import static org.junit.Assert.*;
import org.junit.Test;

public class AnnotationMapTest {
    
    @Test
    public void testGet_ReturnsNullWhenAnnotationsIsNull() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        assertNull(annotationMap.get(Override.class));
    }
    
    @Test
    public void testGet_ReturnsAnnotationWhenPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation mockAnnotation = new MockAnnotation();
        annotationMap.add(mockAnnotation);
        
        assertEquals(mockAnnotation, annotationMap.get(MockAnnotation.class));
    }
    
    @Test
    public void testGet_ReturnsNullWhenAnnotationNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation mockAnnotation = new MockAnnotation();
        annotationMap.add(mockAnnotation);
        
        assertNull(annotationMap.get(Override.class));
    }
    
    @Test
    public void testAnnotations_ReturnsEmptyListWhenAnnotationsIsNull() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        assertTrue(annotationMap.annotations().isEmpty());
    }
    
    @Test
    public void testAnnotations_ReturnsListWithAnnotationsWhenPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation mockAnnotation = new MockAnnotation();
        annotationMap.add(mockAnnotation);
        
        assertEquals(1, annotationMap.annotations().size());
    }
    
    @Test
    public void testAnnotations_ReturnsEmptyListWhenNoAnnotationsPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        assertTrue(annotationMap.annotations().isEmpty());
    }
    
    @Test
    public void testMerge_ReturnsSecondaryWhenPrimaryIsNull() {
        AnnotationMap primary = null;
        AnnotationMap secondary = new AnnotationMap();
        Annotation mockAnnotation = new MockAnnotation();
        secondary.add(mockAnnotation);
        
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        
        assertEquals(secondary, merged);
    }
    
    @Test
    public void testMerge_ReturnsPrimaryWhenSecondaryIsNull() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = null;
        Annotation mockAnnotation = new MockAnnotation();
        primary.add(mockAnnotation);
        
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        
        assertEquals(primary, merged);
    }
    
    @Test
    public void testMerge_ReturnsMergedAnnotationsWhenBothPrimaryAndSecondaryHaveAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        Annotation mockAnnotation1 = new MockAnnotation();
        Annotation mockAnnotation2 = new AnotherMockAnnotation();
        primary.add(mockAnnotation1);
        secondary.add(mockAnnotation2);
        
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        
        assertEquals(2, merged.size());
        assertTrue(merged.get(MockAnnotation.class) instanceof MockAnnotation);
        assertTrue(merged.get(AnotherMockAnnotation.class) instanceof AnotherMockAnnotation);
    }
    
    @Test
    public void testSize_ReturnsZeroWhenAnnotationsIsNull() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        assertEquals(0, annotationMap.size());
    }
    
    @Test
    public void testSize_ReturnsCorrectSizeWhenAnnotationsArePresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation mockAnnotation1 = new MockAnnotation();
        Annotation mockAnnotation2 = new AnotherMockAnnotation();
        annotationMap.add(mockAnnotation1);
        annotationMap.add(mockAnnotation2);
        
        assertEquals(2, annotationMap.size());
    }
    
    @Test
    public void testAddIfNotPresent_ReturnsTrueAndAddsAnnotationWhenAnnotationsIsNull() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation mockAnnotation = new MockAnnotation();
        
        boolean result = annotationMap.addIfNotPresent(mockAnnotation);
        
        assertTrue(result);
        assertEquals(1, annotationMap.size());
        assertTrue(annotationMap.get(MockAnnotation.class) instanceof MockAnnotation);
    }
    
    @Test
    public void testAddIfNotPresent_ReturnsFalseAndDoesNotAddAnnotationWhenAlreadyPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation mockAnnotation = new MockAnnotation();
        annotationMap.add(mockAnnotation);
        
        boolean result = annotationMap.addIfNotPresent(mockAnnotation);
        
        assertFalse(result);
        assertEquals(1, annotationMap.size());
    }
    
    @Test
    public void testAdd_ReturnsTrueAndAddsAnnotationWhenAnnotationsIsNull() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation mockAnnotation = new MockAnnotation();
        
        boolean result = annotationMap.add(mockAnnotation);
        
        assertTrue(result);
        assertEquals(1, annotationMap.size());
        assertTrue(annotationMap.get(MockAnnotation.class) instanceof MockAnnotation);
    }
    
    @Test
    public void testAdd_ReturnsTrueAndAddsAnnotationWhenAnnotationsArePresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation mockAnnotation1 = new MockAnnotation();
        Annotation mockAnnotation2 = new AnotherMockAnnotation();
        annotationMap.add(mockAnnotation1);
        
        boolean result = annotationMap.add(mockAnnotation2);
        
        assertTrue(result);
        assertEquals(2, annotationMap.size());
        assertTrue(annotationMap.get(MockAnnotation.class) instanceof MockAnnotation);
        assertTrue(annotationMap.get(AnotherMockAnnotation.class) instanceof AnotherMockAnnotation);
    }
    
    @Test
    public void testToString_ReturnsNullWhenAnnotationsIsNull() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        assertEquals("[null]", annotationMap.toString());
    }
    
    @Test
    public void testToString_ReturnsCorrectStringRepresentationWhenAnnotationsArePresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation mockAnnotation1 = new MockAnnotation();
        annotationMap.add(mockAnnotation1);
        
        assertEquals("{class MockAnnotation=MockAnnotation}", annotationMap.toString());
    }
    
    private static class MockAnnotation implements Annotation {
        // Mock implementation of Annotation
    }
    
    private static class AnotherMockAnnotation implements Annotation {
        // Another mock implementation of Annotation
    }
}