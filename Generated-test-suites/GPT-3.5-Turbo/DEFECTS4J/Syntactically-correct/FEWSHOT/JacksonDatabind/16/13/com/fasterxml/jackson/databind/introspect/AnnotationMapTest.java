
package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Deprecated.class));

        Deprecated deprecated = getClass().getAnnotation(Deprecated.class);
        annotationMap.add(deprecated);
        assertEquals(deprecated, annotationMap.get(Deprecated.class));

        annotationMap.addIfNotPresent(deprecated);
        assertEquals(deprecated, annotationMap.get(Deprecated.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(Collections.emptyList(), annotationMap.annotations());

        Annotation annotation1 = getClass().getAnnotation(Deprecated.class);
        Annotation annotation2 = getClass().getAnnotation(SuppressWarnings.class);
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);
        List<Annotation> expected = new ArrayList<>();
        expected.add(annotation1);
        expected.add(annotation2);
        assertEquals(expected, annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        assertNull(AnnotationMap.merge(null, null));
        assertEquals(primary, AnnotationMap.merge(primary, null));
        assertEquals(secondary, AnnotationMap.merge(null, secondary));

        Annotation annotation1 = getClass().getAnnotation(Deprecated.class);
        Annotation annotation2 = getClass().getAnnotation(SuppressWarnings.class);
        primary.add(annotation1);
        secondary.add(annotation2);
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(annotation1, merged.get(Deprecated.class));
        assertEquals(annotation2, merged.get(SuppressWarnings.class));
        assertEquals(2, merged.size());
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        Annotation annotation = getClass().getAnnotation(Deprecated.class);
        annotationMap.add(annotation);
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = getClass().getAnnotation(Deprecated.class);
        assertTrue(annotationMap.addIfNotPresent(annotation));
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = getClass().getAnnotation(Deprecated.class);
        assertTrue(annotationMap.add(annotation));
        assertFalse(annotationMap.add(annotation));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());

        Annotation annotation = getClass().getAnnotation(Deprecated.class);
        annotationMap.add(annotation);
        assertEquals("{interface java.lang.Deprecated=@java.lang.Deprecated()}", annotationMap.toString());
    }
}
