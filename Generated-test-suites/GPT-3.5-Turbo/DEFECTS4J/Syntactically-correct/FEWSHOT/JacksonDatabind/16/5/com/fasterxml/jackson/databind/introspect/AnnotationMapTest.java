package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AnnotationMapTest {
    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Test.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(Test.class.getAnnotation(Override.class));

        AnnotationMap secondary = new AnnotationMap();
        secondary.add(Test.class.getAnnotation(Deprecated.class));

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(2, merged.size());
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation ann = Test.class.getAnnotation(Deprecated.class);
        assertTrue(annotationMap.addIfNotPresent(ann));
        assertFalse(annotationMap.addIfNotPresent(ann));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation ann = Test.class.getAnnotation(Deprecated.class);
        assertTrue(annotationMap.add(ann));
        assertFalse(annotationMap.add(ann));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());
    }
}