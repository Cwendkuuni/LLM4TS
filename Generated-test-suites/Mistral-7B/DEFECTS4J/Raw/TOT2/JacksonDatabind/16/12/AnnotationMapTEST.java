
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class AnnotationMapTest {

    @Test
    public void testGetAnnotation() {
        // ... get test case from above
    }

    @Test
    public void testAnnotations() {
        // ... annotations test case from above
    }

    @Test
    public void testMergeAnnotationMaps() {
        // ... merge test case from above
    }

    // Add test cases for other methods: size(), addIfNotPresent(Annotation ann), add(Annotation ann), and _add(Annotation ann)

    // Add test cases for edge cases and exception handling scenarios

    @Test
    public void testGetAnnotationWithNullAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAnnotationsWithNullAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        List<Annotation> annotations = annotationMap.annotations();
        assertTrue(annotations.isEmpty());
    }

    @Test
    public void testMergeAnnotationMapsWithEmptySecondary() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();

        MyAnnotation primaryAnnotation = new MyAnnotation();
        primaryAnnotationMap.add(primaryAnnotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        assertEquals(primaryAnnotation, mergedAnnotationMap.get(primaryAnnotation.annotationType()));
    }

    @Test
    public void testMergeAnnotationMapsWithEmptyPrimary() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();

        MyAnnotation secondaryAnnotation = new MyAnnotation();
        secondaryAnnotationMap.add(secondaryAnnotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        assertEquals(secondaryAnnotation, mergedAnnotationMap.get(secondaryAnnotation.annotationType()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIfNotPresentWithDuplicateAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        annotationMap.add(annotation);

        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithDuplicateAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        annotationMap.add(annotation);

        assertFalse(annotationMap.add(annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAnnotationWithNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertFalse(annotationMap.add(null));
    }
}