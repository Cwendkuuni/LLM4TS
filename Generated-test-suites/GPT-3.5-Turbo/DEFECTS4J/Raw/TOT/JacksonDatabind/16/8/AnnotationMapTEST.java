import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;
    
    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }
    
    @Test
    public void testGet_ReturnsNull_WhenAnnotationsIsNull() {
        assertNull(annotationMap.get(TestAnnotation.class));
    }
    
    @Test
    public void testGet_ReturnsAnnotation_WhenAnnotationsIsNotEmpty() {
        TestAnnotation annotation = new TestAnnotation();
        annotationMap.add(annotation);
        
        assertEquals(annotation, annotationMap.get(TestAnnotation.class));
    }
    
    @Test
    public void testSize_ReturnsZero_WhenAnnotationsIsNull() {
        assertEquals(0, annotationMap.size());
    }
    
    @Test
    public void testSize_ReturnsNumberOfAnnotations_WhenAnnotationsIsNotEmpty() {
        annotationMap.add(new TestAnnotation());
        annotationMap.add(new AnotherAnnotation());
        
        assertEquals(2, annotationMap.size());
    }
    
    @Test
    public void testAddIfNotPresent_ReturnsTrue_WhenAnnotationDoesNotExist() {
        TestAnnotation annotation = new TestAnnotation();
        assertTrue(annotationMap.addIfNotPresent(annotation));
    }
    
    @Test
    public void testAddIfNotPresent_ReturnsFalse_WhenAnnotationExists() {
        TestAnnotation annotation = new TestAnnotation();
        annotationMap.add(annotation);
        
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }
    
    @Test
    public void testAdd_ReturnsTrue_WhenAnnotationIsAdded() {
        TestAnnotation annotation = new TestAnnotation();
        assertTrue(annotationMap.add(annotation));
    }
    
    @Test
    public void testAdd_ReturnsFalse_WhenAnnotationAlreadyExists() {
        TestAnnotation annotation = new TestAnnotation();
        annotationMap.add(annotation);
        
        assertFalse(annotationMap.add(annotation));
    }
    
    @Test
    public void testToString_ReturnsCorrectStringRepresentation_WhenAnnotationsIsNull() {
        assertEquals("[null]", annotationMap.toString());
    }
    
    @Test
    public void testToString_ReturnsCorrectStringRepresentation_WhenAnnotationsIsNotEmpty() {
        annotationMap.add(new TestAnnotation());
        annotationMap.add(new AnotherAnnotation());
        
        String expected = "{class TestAnnotation=TestAnnotation, class AnotherAnnotation=AnotherAnnotation}";
        assertEquals(expected, annotationMap.toString());
    }
    
    @Test
    public void testAnnotations_ReturnsEmptyList_WhenAnnotationsIsNull() {
        Iterable<Annotation> annotations = annotationMap.annotations();
        assertFalse(annotations.iterator().hasNext());
    }
    
    @Test
    public void testAnnotations_ReturnsListOfAnnotations_WhenAnnotationsIsNotEmpty() {
        TestAnnotation annotation1 = new TestAnnotation();
        AnotherAnnotation annotation2 = new AnotherAnnotation();
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);
        
        Iterable<Annotation> annotations = annotationMap.annotations();
        
        assertNotNull(annotations.iterator().next());
        assertTrue(annotations.iterator().next() instanceof TestAnnotation);
        assertTrue(annotations.iterator().next() instanceof AnotherAnnotation);
    }
    
    @Test
    public void testMerge_ReturnsPrimary_WhenPrimaryIsNull() {
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new TestAnnotation());
        
        AnnotationMap merged = AnnotationMap.merge(null, secondary);
        
        assertNotNull(merged);
        assertEquals(1, merged.size());
    }
    
    @Test
    public void testMerge_ReturnsSecondary_WhenSecondaryIsNull() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new TestAnnotation());
        
        AnnotationMap merged = AnnotationMap.merge(primary, null);
        
        assertNotNull(merged);
        assertEquals(1, merged.size());
    }
    
    @Test
    public void testMerge_ReturnsPrimary_WhenPrimaryAndSecondaryAreNull() {
        AnnotationMap merged = AnnotationMap.merge(null, null);
        
        assertNull(merged);
    }
    
    @Test
    public void testMerge_ReturnsMergedAnnotations_WhenBothPrimaryAndSecondaryAreNonNull() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new TestAnnotation());
        
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new AnotherAnnotation());
        
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        
        assertNotNull(merged);
        assertEquals(2, merged.size());
    }
}