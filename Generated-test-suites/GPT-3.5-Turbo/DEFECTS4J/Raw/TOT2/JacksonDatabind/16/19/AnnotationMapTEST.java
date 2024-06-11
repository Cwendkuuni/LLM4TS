package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Override.class));
        
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Override.class, mock(Annotation.class));
        AnnotationMap annotationMapWithAnnotations = new AnnotationMap(annotations);
        assertNotNull(annotationMapWithAnnotations.get(Override.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap emptyAnnotationMap = new AnnotationMap();
        assertEquals(Collections.emptyList(), emptyAnnotationMap.annotations());
        
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Override.class, mock(Annotation.class));
        AnnotationMap annotationMap = new AnnotationMap(annotations);
        assertFalse(annotationMap.annotations().equals(Collections.emptyList()));
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertTrue(merged.size() == 0);

        HashMap<Class<? extends Annotation>, Annotation> primaryAnnotations = new HashMap<>();
        primaryAnnotations.put(Override.class, mock(Annotation.class));
        primary = new AnnotationMap(primaryAnnotations);

        merged = AnnotationMap.merge(primary, secondary);
        assertNotNull(merged.get(Override.class));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
        
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Override.class, mock(Annotation.class));
        AnnotationMap annotationMapWithAnnotations = new AnnotationMap(annotations);
        assertTrue(annotationMapWithAnnotations.size() == 1);
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation mockAnnotation = mock(Annotation.class);

        assertTrue(annotationMap.addIfNotPresent(mockAnnotation));
        assertFalse(annotationMap.addIfNotPresent(mockAnnotation));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation mockAnnotation = mock(Annotation.class);

        assertTrue(annotationMap.add(mockAnnotation));
        assertFalse(annotationMap.add(mockAnnotation));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());
        
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Override.class, mock(Annotation.class));
        AnnotationMap annotationMapWithAnnotations = new AnnotationMap(annotations);
        assertNotNull(annotationMapWithAnnotations.toString());
    }

    @Test
    public void testAddHelper() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation mockAnnotation = mock(Annotation.class);

        assertTrue(annotationMap.add(mockAnnotation));
        assertFalse(annotationMap.add(mockAnnotation));
    }
}