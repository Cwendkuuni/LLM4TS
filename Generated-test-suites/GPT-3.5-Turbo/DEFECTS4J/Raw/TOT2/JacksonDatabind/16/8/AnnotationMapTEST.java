package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(SuppressWarnings.class));
        
        // Test adding annotation
        SuppressWarnings annotation = AnnotationMapTest.class.getAnnotation(SuppressWarnings.class);
        annotationMap.add(annotation);
        assertEquals(annotation, annotationMap.get(SuppressWarnings.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        Iterable<Annotation> emptyAnnotations = annotationMap.annotations();
        assertFalse(emptyAnnotations.iterator().hasNext());
        
        // Test adding annotations
        SuppressWarnings annotation1 = AnnotationMapTest.class.getAnnotation(SuppressWarnings.class);
        SuppressWarnings annotation2 = AnnotationMapTest.class.getAnnotation(SuppressWarnings.class);
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);
        Iterable<Annotation> annotations = annotationMap.annotations();
        assertTrue(annotations.iterator().hasNext());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        // Test merging with empty maps
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(0, merged.size());
        
        // Test merging with annotations
        SuppressWarnings annotation1 = AnnotationMapTest.class.getAnnotation(SuppressWarnings.class);
        SuppressWarnings annotation2 = AnnotationMapTest.class.getAnnotation(SuppressWarnings.class);
        primary.add(annotation1);
        secondary.add(annotation2);
        
        merged = AnnotationMap.merge(primary, secondary);
        assertEquals(1, merged.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test adding annotation if not present
        SuppressWarnings annotation = AnnotationMapTest.class.getAnnotation(SuppressWarnings.class);
        assertTrue(annotationMap.addIfNotPresent(annotation));
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test adding annotation
        SuppressWarnings annotation = AnnotationMapTest.class.getAnnotation(SuppressWarnings.class);
        assertTrue(annotationMap.add(annotation));
        assertFalse(annotationMap.add(annotation));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test string representation
        assertNotNull(annotationMap.toString());
    }
}