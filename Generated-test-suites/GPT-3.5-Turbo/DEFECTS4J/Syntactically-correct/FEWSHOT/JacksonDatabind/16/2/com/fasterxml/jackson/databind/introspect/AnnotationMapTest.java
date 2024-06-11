package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AnnotationMapTest {
    
    private AnnotationMap annotationMap;
    
    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }
    
    @Test
    public void testGetWithExistingAnnotation() {
        Annotation annotation = new TestAnnotation();
        annotationMap.add(annotation);
        
        Annotation result = annotationMap.get(TestAnnotation.class);
        
        assertEquals(annotation, result);
    }
    
    @Test
    public void testGetWithNonExistingAnnotation() {
        Annotation result = annotationMap.get(TestAnnotation.class);
        
        assertNull(result);
    }
    
    @Test
    public void testAnnotationsWithEmptyMap() {
        Iterable<Annotation> annotations = annotationMap.annotations();
        
        assertEquals(Collections.emptyList(), annotations);
    }
    
    @Test
    public void testAnnotationsWithNonEmptyMap() {
        Annotation annotation = new TestAnnotation();
        annotationMap.add(annotation);
        
        Iterable<Annotation> annotations = annotationMap.annotations();
        
        assertTrue(annotations.contains(annotation));
    }
    
    @Test
    public void testMergeWithPrimaryNull() {
        AnnotationMap secondary = new AnnotationMap();
        Annotation annotation = new TestAnnotation();
        secondary.add(annotation);
        
        AnnotationMap result = AnnotationMap.merge(null, secondary);
        
        assertEquals(secondary, result);
    }
    
    @Test
    public void testMergeWithSecondaryNull() {
        AnnotationMap primary = new AnnotationMap();
        Annotation annotation = new TestAnnotation();
        primary.add(annotation);
        
        AnnotationMap result = AnnotationMap.merge(primary, null);
        
        assertEquals(primary, result);
    }
    
    @Test
    public void testMergeWithBothNotNull() {
        AnnotationMap primary = new AnnotationMap();
        Annotation primaryAnnotation = new TestAnnotation();
        primary.add(primaryAnnotation);
        
        AnnotationMap secondary = new AnnotationMap();
        Annotation secondaryAnnotation = new AnotherTestAnnotation();
        secondary.add(secondaryAnnotation);
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        assertTrue(result.get(TestAnnotation.class).equals(primaryAnnotation));
        assertTrue(result.get(AnotherTestAnnotation.class).equals(secondaryAnnotation));
    }
    
    @Test
    public void testSizeWithEmptyMap() {
        int size = annotationMap.size();
        
        assertEquals(0, size);
    }
    
    @Test
    public void testSizeWithNonEmptyMap() {
        Annotation annotation = new TestAnnotation();
        annotationMap.add(annotation);
        
        int size = annotationMap.size();
        
        assertEquals(1, size);
    }
    
    @Test
    public void testAddIfNotPresentWithNewAnnotation() {
        Annotation annotation = new TestAnnotation();
        
        boolean result = annotationMap.addIfNotPresent(annotation);
        
        assertTrue(result);
        assertTrue(annotationMap.get(TestAnnotation.class).equals(annotation));
    }
    
    @Test
    public void testAddIfNotPresentWithExistingAnnotation() {
        Annotation annotation = new TestAnnotation();
        annotationMap.add(annotation);
        
        boolean result = annotationMap.addIfNotPresent(annotation);
        
        assertFalse(result);
    }
    
    @Test
    public void testAddWithNewAnnotation() {
        Annotation annotation = new TestAnnotation();
        
        boolean result = annotationMap.add(annotation);
        
        assertTrue(result);
        assertTrue(annotationMap.get(TestAnnotation.class).equals(annotation));
    }
    
    @Test
    public void testAddWithExistingAnnotation() {
        Annotation annotation = new TestAnnotation();
        annotationMap.add(annotation);
        
        boolean result = annotationMap.add(annotation);
        
        assertFalse(result);
    }
    
    private static class TestAnnotation implements Annotation {
        
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation.class;
        }
        
        @Override
        public boolean equals(Object obj) {
            return obj instanceof TestAnnotation;
        }
        
        @Override
        public int hashCode() {
            return TestAnnotation.class.hashCode();
        }
    }
    
    private static class AnotherTestAnnotation implements Annotation {
        
        @Override
        public Class<? extends Annotation> annotationType() {
            return AnotherTestAnnotation.class;
        }
        
        @Override
        public boolean equals(Object obj) {
            return obj instanceof AnotherTestAnnotation;
        }
        
        @Override
        public int hashCode() {
            return AnotherTestAnnotation.class.hashCode();
        }
    }
}