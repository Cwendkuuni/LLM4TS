import org.junit.*;
import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.util.*;

public class AnnotationMapTest {

    // Test case for get method
    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Add some annotations to the map
        annotationMap.add(new Annotation1());
        annotationMap.add(new Annotation2());
        annotationMap.add(new Annotation3());
        
        // Test for a valid annotation class
        Annotation1 actualAnnotation = annotationMap.get(Annotation1.class);
        assertNotNull(actualAnnotation);
        
        // Test for an invalid annotation class
        Annotation4 actualNullAnnotation = annotationMap.get(Annotation4.class);
        assertNull(actualNullAnnotation);
    }
    
    // Test case for annotations method
    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Add some annotations to the map
        annotationMap.add(new Annotation1());
        annotationMap.add(new Annotation2());
        annotationMap.add(new Annotation3());
        
        // Test for non-empty map
        Iterable<Annotation> actualAnnotations1 = annotationMap.annotations();
        assertNotNull(actualAnnotations1);
        assertTrue(actualAnnotations1.iterator().hasNext());
        
        // Test for empty map
        annotationMap = new AnnotationMap(); // Reset map to empty
        Iterable<Annotation> actualAnnotations2 = annotationMap.annotations();
        assertNotNull(actualAnnotations2);
        assertFalse(actualAnnotations2.iterator().hasNext());
    }
    
    // Test case for merge method
    @Test
    public void testMerge() {
        AnnotationMap primaryMap = new AnnotationMap();
        AnnotationMap secondaryMap = new AnnotationMap();
        AnnotationMap mergedMap;
        
        // Test for both maps being empty
        mergedMap = AnnotationMap.merge(primaryMap, secondaryMap);
        assertNotNull(mergedMap);
        assertEquals(0, mergedMap.size());
        
        // Test for primary map being empty
        secondaryMap.add(new Annotation1());
        mergedMap = AnnotationMap.merge(primaryMap, secondaryMap);
        assertNotNull(mergedMap);
        assertEquals(1, mergedMap.size());
        
        // Test for secondary map being empty
        primaryMap.add(new Annotation2());
        mergedMap = AnnotationMap.merge(primaryMap, secondaryMap);
        assertNotNull(mergedMap);
        assertEquals(2, mergedMap.size());
        
        // Test for both maps being non-empty
        primaryMap.add(new Annotation3());
        secondaryMap.add(new Annotation4());
        mergedMap = AnnotationMap.merge(primaryMap, secondaryMap);
        assertNotNull(mergedMap);
        assertEquals(4, mergedMap.size());
    }
    
    // Test case for size method
    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test for empty map
        assertEquals(0, annotationMap.size());
        
        // Test for non-empty map
        annotationMap.add(new Annotation1());
        annotationMap.add(new Annotation2());
        annotationMap.add(new Annotation3());
        assertEquals(3, annotationMap.size());
    }
    
    // Test case for addIfNotPresent method
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Add a new annotation
        Annotation1 annotation1 = new Annotation1();
        boolean added1 = annotationMap.addIfNotPresent(annotation1);
        assertTrue(added1);
        
        // Try to add the same annotation again
        boolean added2 = annotationMap.addIfNotPresent(annotation1);
        assertFalse(added2);
    }
    
    // Test case for add method
    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Add a new annotation
        Annotation1 annotation1 = new Annotation1();
        boolean added1 = annotationMap.add(annotation1);
        assertTrue(added1);
        
        // Add the same annotation again
        boolean added2 = annotationMap.add(annotation1);
        assertFalse(added2);
    }
    
    // Test case for toString method
    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test for empty map
        assertEquals("[null]", annotationMap.toString());
        
        // Test for non-empty map
        annotationMap.add(new Annotation1());
        annotationMap.add(new Annotation2());
        annotationMap.add(new Annotation3());
        assertEquals("{class a.Annotation2=@b.Annotation2(), class a.Annotation1=@b.Annotation1(), class a.Annotation3=@b.Annotation3()}", annotationMap.toString());
    }
    
    // Test case for private helper method _add
    @Test
    public void testAddHelper() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test for null map
        annotationMap._add(new Annotation1());
        assertEquals(1, annotationMap.size());
        
        // Test for non-null map
        annotationMap._add(new Annotation2());
        assertEquals(2, annotationMap.size());
    }
    
    // Custom annotation classes for testing
    @Retention(RetentionPolicy.RUNTIME)
    @interface Annotation1 { }
    
    @Retention(RetentionPolicy.RUNTIME)
    @interface Annotation2 { }
    
    @Retention(RetentionPolicy.RUNTIME)
    @interface Annotation3 { }
    
    @Retention(RetentionPolicy.RUNTIME)
    @interface Annotation4 { }
}