   
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
    public void testGet_NullAnnotation_ReturnsNull() {
        assertNull(annotationMap.get(Annotation.class));
    }

    @Test
    public void testGet_NonNullAnnotation_ReturnsAnnotation() {
        Annotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        assertEquals(annotation, annotationMap.get(Annotation.class));
    }

    @Test
    public void testAnnotations_EmptyMap_ReturnsEmptyList() {
        assertTrue(annotationMap.annotations().isEmpty());
    }

    @Test
    public void testAnnotations_NonEmptyMap_ReturnsListOfAnnotations() {
        Annotation annotation1 = new MyAnnotation();
        Annotation annotation2 = new AnotherAnnotation();
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);
        List<Annotation> expected = Arrays.asList(annotation1, annotation2);
        assertEquals(expected, annotationMap.annotations());
    }

    @Test
    public void testMerge_NullPrimaryAndSecondary_ReturnsNull() {
        assertNull(AnnotationMap.merge(null, null));
    }

    @Test
    public void testMerge_NullPrimary_ReturnsSecondary() {
        AnnotationMap secondary = new AnnotationMap();
        Annotation annotation = new MyAnnotation();
        secondary.add(annotation);
        assertEquals(secondary, AnnotationMap.merge(null, secondary));
    }

    @Test
    public void testMerge_NullSecondary_ReturnsPrimary() {
        AnnotationMap primary = new AnnotationMap();
        Annotation annotation = new MyAnnotation();
        primary.add(annotation);
        assertEquals(primary, AnnotationMap.merge(primary, null));
    }

    @Test
    public void testMerge_NonNullPrimaryAndSecondary_ReturnsMergedMap() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        Annotation annotation1 = new MyAnnotation();
        Annotation annotation2 = new AnotherAnnotation();
        primary.add(annotation1);
        secondary.add(annotation2);
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertTrue(merged.get(MyAnnotation.class) != null);
        assertTrue(merged.get(AnotherAnnotation.class) != null);
    }

    @Test
    public void testSize_EmptyMap_ReturnsZero() {
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testSize_NonEmptyMap_ReturnsSize() {
        Annotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent_NewAnnotation_ReturnsTrue() {
        Annotation annotation = new MyAnnotation();
        assertTrue(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAddIfNotPresent_ExistingAnnotation_ReturnsFalse() {
        Annotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAdd_NewAnnotation_ReturnsTrue() {
        Annotation annotation = new MyAnnotation();
        assertTrue(annotationMap.add(annotation));
    }

    @Test
    public void testAdd_ExistingAnnotation_ReturnsFalse() {
        Annotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        assertFalse(annotationMap.add(annotation));
    }

    @Test
    public void testToString_NullMap_ReturnsNullString() {
        assertEquals("[null]", annotationMap.toString());
    }

    @Test
    public void testToString_NonNullMap_ReturnsMapString() {
        Annotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        assertEquals("{class MyAnnotation=MyAnnotation}", annotationMap.toString());
    }
   
    // Additional test cases for potential edge cases and exception handling scenarios
    
    // Test for adding an annotation of a different type with the same annotationType
    
    @Test
    public void testAdd_DifferentTypeSameAnnotationType_ReturnsTrue() {
        Annotation annotation1 = new MyAnnotation();
        Annotation annotation2 = new MyDifferentAnnotation();
        annotationMap.add(annotation1);
        assertTrue(annotationMap.add(annotation2));
    }
    
    // Test for merging two empty maps
    
    @Test
    public void testMerge_EmptyMaps_ReturnsEmptyMap() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertTrue(merged.size() == 0);
    }
    
    // Test for adding an annotation with a null annotationType
    
    @Test(expected = NullPointerException.class)
    public void testAdd_NullAnnotationType_ThrowsNullPointerException() {
        Annotation annotation = new NullAnnotation();
        annotationMap.add(annotation);
    }

}
