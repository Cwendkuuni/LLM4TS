
package com.fasterxml.jackson.databind.introspect;

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
        Iterable<Annotation> annotations = annotationMap.annotations();
        
        assertFalse(annotations.iterator().hasNext());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        Annotation merged = primary.merge(primary, secondary);
        
        assertNull(merged);
    }
    
    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
    }
    
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation ann = new Test();
        boolean added = annotationMap.addIfNotPresent(ann);
        
        assertTrue(added);
    }
    
    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation ann = new Test();
        boolean added = annotationMap.add(ann);
        
        assertTrue(added);
    }
    
    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        String str = annotationMap.toString();
        
        assertEquals("[null]", str);
    }
}
