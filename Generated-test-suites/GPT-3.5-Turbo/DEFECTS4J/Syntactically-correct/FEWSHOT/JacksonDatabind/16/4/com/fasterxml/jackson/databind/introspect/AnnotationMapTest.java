package com.fasterxml.jackson.databind.introspect;

import java.lang.annotation.Annotation;
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
    
        assertTrue(annotationMap.annotations().isEmpty());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primaryMap = new AnnotationMap();
        AnnotationMap secondaryMap = new AnnotationMap();
        
        assertNull(AnnotationMap.merge(primaryMap, secondaryMap));
    }
    
    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
    
        assertEquals(0, annotationMap.size());
    }
    
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new TestAnnotation();
        
        assertTrue(annotationMap.addIfNotPresent(annotation));
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }
    
    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new TestAnnotation();
        
        assertTrue(annotationMap.add(annotation));
        assertFalse(annotationMap.add(annotation));
    }
    
    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
    
        assertEquals("[null]", annotationMap.toString());
    }
    
    @Test
    public void testAddPrivateMethod() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new TestAnnotation();
        
        assertTrue(annotationMap.add(annotation));
        assertFalse(annotationMap._add(annotation));
    }
    
    private static class TestAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation.class;
        }
    }
}