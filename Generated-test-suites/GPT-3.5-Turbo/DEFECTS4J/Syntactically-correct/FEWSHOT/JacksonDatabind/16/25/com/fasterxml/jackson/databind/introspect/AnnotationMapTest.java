package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationMapTest {
    
    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test when map is empty
        assertNull(annotationMap.get(Test.class));
        
        // Test when map contains key
        annotationMap.add(new TestAnnotation());
        assertNotNull(annotationMap.get(TestAnnotation.class));
    }
    
    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test when map is empty
        assertEquals(0, annotationMap.annotations().size());
        
        // Test when map contains annotations
        annotationMap.add(new TestAnnotation());
        assertEquals(1, annotationMap.annotations().size());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primaryMap = new AnnotationMap();
        AnnotationMap secondaryMap = new AnnotationMap();
        
        // Test when both maps are empty
        assertEquals(secondaryMap, AnnotationMap.merge(primaryMap, secondaryMap));
        
        // Test when primary map is empty
        primaryMap.add(new TestAnnotation());
        assertEquals(primaryMap, AnnotationMap.merge(primaryMap, secondaryMap));
        
        // Test when secondary map is empty
        secondaryMap.add(new TestAnnotation());
        assertEquals(secondaryMap, AnnotationMap.merge(primaryMap, secondaryMap));
        
        // Test when both maps contain annotations
        AnnotationMap mergedMap = AnnotationMap.merge(primaryMap, secondaryMap);
        assertEquals(2, mergedMap.size());
        assertEquals(mergedMap, AnnotationMap.merge(secondaryMap, primaryMap));
    }
    
    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test when map is empty
        assertEquals(0, annotationMap.size());
        
        // Test when map contains annotations
        annotationMap.add(new TestAnnotation());
        assertEquals(1, annotationMap.size());
    }
    
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test when annotation is not present in map
        assertTrue(annotationMap.addIfNotPresent(new TestAnnotation()));
        assertEquals(1, annotationMap.size());
        
        // Test when annotation is already present in map
        assertFalse(annotationMap.addIfNotPresent(new TestAnnotation()));
        assertEquals(1, annotationMap.size());
    }
    
    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test when annotation is added to map
        assertTrue(annotationMap.add(new TestAnnotation()));
        assertEquals(1, annotationMap.size());
        
        // Test when annotation is already present in map
        assertFalse(annotationMap.add(new TestAnnotation()));
        assertEquals(1, annotationMap.size());
    }
    
    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test when map is empty
        assertEquals("[null]", annotationMap.toString());
        
        // Test when map contains annotations
        annotationMap.add(new TestAnnotation());
        assertEquals("{class com.fasterxml.jackson.databind.introspect.TestAnnotation=" 
                     + "com.fasterxml.jackson.databind.introspect.TestAnnotation}",
                     annotationMap.toString());
    }
}