package com.fasterxml.jackson.databind.introspect;

import org.junit.*;
import static org.junit.Assert.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test null case
        assertNull(annotationMap.get(TestAnnotation.class));
        
        // Test getting existing annotation
        TestAnnotation testAnnotation = new TestAnnotationImpl();
        annotationMap.add(testAnnotation);
        assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));
    }
    
    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test empty case
        assertEquals(Collections.emptyList(), annotationMap.annotations());
        
        // Test non-empty case
        TestAnnotation testAnnotation = new TestAnnotationImpl();
        annotationMap.add(testAnnotation);
        assertEquals(Collections.singletonList(testAnnotation), annotationMap.annotations());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        // Test merging null with null
        assertNull(AnnotationMap.merge(null, null));
        
        // Test merging null with non-empty
        AnnotationMap mergedMap = AnnotationMap.merge(null, primary);
        assertEquals(primary, mergedMap);
        
        // Test merging non-empty with null
        mergedMap = AnnotationMap.merge(primary, null);
        assertEquals(primary, mergedMap);
        
        // Test merging non-empty with non-empty
        primary.add(new TestAnnotationImpl());
        secondary.add(new TestAnnotationImpl());
        mergedMap = AnnotationMap.merge(primary, secondary);
        // The merged map should contain all annotations from both primary and secondary
        assertEquals(2, mergedMap.size());
        assertTrue(mergedMap.get(TestAnnotation.class) instanceof TestAnnotationImpl);
    }
    
    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test empty case
        assertEquals(0, annotationMap.size());
        
        // Test non-empty case
        annotationMap.add(new TestAnnotationImpl());
        assertEquals(1, annotationMap.size());
    }
    
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test adding when the annotation type doesn't exist
        assertTrue(annotationMap.addIfNotPresent(new TestAnnotationImpl()));
        
        // Test adding when the annotation type already exists
        assertFalse(annotationMap.addIfNotPresent(new TestAnnotationImpl()));
    }
    
    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test adding an annotation that doesn't exist
        assertTrue(annotationMap.add(new TestAnnotationImpl()));
        
        // Test adding an annotation that already exists
        assertFalse(annotationMap.add(new TestAnnotationImpl()));
    }
    
    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test empty case
        assertEquals("[null]", annotationMap.toString());
        
        // Test non-empty case
        annotationMap.add(new TestAnnotationImpl());
        assertEquals("{@com.fasterxml.jackson.databind.introspect.TestAnnotationImpl=Test}", annotationMap.toString());
    }
    
    private static class TestAnnotationImpl implements TestAnnotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation.class;
        }
    }
}