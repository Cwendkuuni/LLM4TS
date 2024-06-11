import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.*;

import static org.junit.Assert.*;

public class AnnotationMapTest {
    
    @Test
    public void testGetWhenAnnotationMapIsEmpty() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = annotationMap.get(Test.class);
        assertNull(annotation);
    }
    
    @Test
    public void testGetWhenAnnotationMapContainsAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(Test.class.getAnnotation(Test.class));
        Annotation annotation = annotationMap.get(Test.class);
        assertNotNull(annotation);
    }
    
    @Test
    public void testGetWhenAnnotationMapDoesNotContainAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(Override.class.getAnnotation(Override.class));
        Annotation annotation = annotationMap.get(Test.class);
        assertNull(annotation);
    }
    
    @Test
    public void testAnnotationsWhenAnnotationMapIsEmpty() {
        AnnotationMap annotationMap = new AnnotationMap();
        Iterable<Annotation> annotations = annotationMap.annotations();
        assertEquals(Collections.emptyList(), annotations);
    }
    
    @Test
    public void testAnnotationsWhenAnnotationMapContainsAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(Test.class.getAnnotation(Test.class));
        annotationMap.add(Override.class.getAnnotation(Override.class));
        Iterable<Annotation> annotations = annotationMap.annotations();
        List<Annotation> expected = Arrays.asList(Test.class.getAnnotation(Test.class), Override.class.getAnnotation(Override.class));
        assertEquals(expected, annotations);
    }
    
    @Test
    public void testMergeWhenPrimaryMapIsEmpty() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(Test.class.getAnnotation(Test.class));
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(secondary, merged);
    }
    
    @Test
    public void testMergeWhenSecondaryMapIsEmpty() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(Test.class.getAnnotation(Test.class));
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, merged);
    }
    
    @Test
    public void testMergeWhenBothMapsContainAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(Test.class.getAnnotation(Test.class));
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(Override.class.getAnnotation(Override.class));
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        AnnotationMap expected = new AnnotationMap();
        expected.add(Test.class.getAnnotation(Test.class));
        expected.add(Override.class.getAnnotation(Override.class));
        assertEquals(expected, merged);
    }
    
    @Test
    public void testSizeWhenAnnotationMapIsEmpty() {
        AnnotationMap annotationMap = new AnnotationMap();
        int size = annotationMap.size();
        assertEquals(0, size);
    }
    
    @Test
    public void testSizeWhenAnnotationMapContainsAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(Test.class.getAnnotation(Test.class));
        annotationMap.add(Override.class.getAnnotation(Override.class));
        int size = annotationMap.size();
        assertEquals(2, size);
    }
    
    @Test
    public void testAddIfNotPresentWhenAnnotationMapIsEmpty() {
        AnnotationMap annotationMap = new AnnotationMap();
        boolean added = annotationMap.addIfNotPresent(Test.class.getAnnotation(Test.class));
        assertTrue(added);
    }
    
    @Test
    public void testAddIfNotPresentWhenAnnotationMapContainsAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(Test.class.getAnnotation(Test.class));
        boolean added = annotationMap.addIfNotPresent(Test.class.getAnnotation(Test.class));
        assertFalse(added);
    }
    
    @Test
    public void testAddWhenAnnotationMapIsEmpty() {
        AnnotationMap annotationMap = new AnnotationMap();
        boolean added = annotationMap.add(Test.class.getAnnotation(Test.class));
        assertTrue(added);
    }
    
    @Test
    public void testAddWhenAnnotationMapContainsAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(Test.class.getAnnotation(Test.class));
        boolean added = annotationMap.add(Test.class.getAnnotation(Test.class));
        assertFalse(added);
    }
    
    @Test
    public void testToStringWhenAnnotationMapIsEmpty() {
        AnnotationMap annotationMap = new AnnotationMap();
        String toString = annotationMap.toString();
        assertEquals("[null]", toString);
    }
    
    @Test
    public void testToStringWhenAnnotationMapContainsAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(Test.class.getAnnotation(Test.class));
        annotationMap.add(Override.class.getAnnotation(Override.class));
        String toString = annotationMap.toString();
        assertEquals("{interface org.junit.Test=@org.junit.Test(timeout=0, expected=class org.junit.Test$None), interface java.lang.Override=@java.lang.Override}", toString);
    }
}