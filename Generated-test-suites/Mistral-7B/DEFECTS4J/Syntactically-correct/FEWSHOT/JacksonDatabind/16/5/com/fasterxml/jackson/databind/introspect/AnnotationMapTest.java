package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;
    private AnnotationMap mergedAnnotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
        mergedAnnotationMap = new AnnotationMap();
    }

    @Test
    public void testCreateAnnotationMap() {
        assertNull(annotationMap.get(null));
        assertNull(annotationMap.get(Object.class));
    }

    @Test
    public void testSize() {
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testAddAnnotation() {
        annotationMap.add(new JacksonAnnotation1());
        assertEquals(1, annotationMap.size());
        assertSame(annotationMap.get(JacksonAnnotation1.class), new JacksonAnnotation1());
    }

    @Test
    public void testAddAnnotationIfNotPresent() {
        annotationMap.add(new JacksonAnnotation1());
        assertFalse(annotationMap.addIfNotPresent(new JacksonAnnotation1()));
        assertTrue(annotationMap.size() == 1);

        Annotation jacksonAnnotation2 = new JacksonAnnotation2();
        assertTrue(annotationMap.addIfNotPresent(jacksonAnnotation2));
        assertEquals(2, annotationMap.size());
        assertSame(annotationMap.get(JacksonAnnotation1.class), new JacksonAnnotation1());
        assertSame(annotationMap.get(JacksonAnnotation2.class), jacksonAnnotation2);
    }

    @Test
    public void testMergeAnnotationMaps() {
        annotationMap.add(new JacksonAnnotation1());
        mergedAnnotationMap.add(new JacksonAnnotation2());

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, mergedAnnotationMap);

        assertEquals(3, mergedAnnotationMap.size());
        assertSame(mergedAnnotationMap.get(JacksonAnnotation1.class), new JacksonAnnotation1());
        assertSame(mergedAnnotationMap.get(JacksonAnnotation2.class), new JacksonAnnotation2());
    }

    @Test
    public void testMergeEmptyAnnotationMaps() {
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(null, mergedAnnotationMap);
        assertEquals(0, mergedAnnotationMap.size());

        mergedAnnotationMap = AnnotationMap.merge(annotationMap, null);
        assertEquals(1, mergedAnnotationMap.size());
        assertSame(mergedAnnotationMap.get(JacksonAnnotation1.class), new JacksonAnnotation1());
    }

    @Test
    public void testMergeAnnotationMapsWithSameAnnotation() {
        annotationMap.add(new JacksonAnnotation1());
        mergedAnnotationMap.add(new JacksonAnnotation1());

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, mergedAnnotationMap);
        assertEquals(2, mergedAnnotationMap.size());
        assertSame(mergedAnnotationMap.get(JacksonAnnotation1.class), new JacksonAnnotation1());
    }

    @Test
    public void testToString() {
        annotationMap.add(new JacksonAnnotation1());
        assertEquals("[JacksonAnnotation1]", annotationMap.toString());
    }

    @Test
    public void testMergeAnnotationMapsWithSameKey() {
        annotationMap.add(new JacksonAnnotation1());
        mergedAnnotationMap.add(new JacksonAnnotation1());

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, mergedAnnotationMap);
        assertEquals(2, mergedAnnotationMap.size());
        assertSame(mergedAnnotationMap.get(JacksonAnnotation1.class), new JacksonAnnotation1());
    }

    @Test
    public void testAnnotations() {
        annotationMap.add(new JacksonAnnotation1());
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        assertEquals(1, annotations.size());
        assertSame(annotations.get(0), new JacksonAnnotation1());
    }

    @Test
    public void testEmptyAnnotations() {
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        assertEquals(0, annotations.size());
    }

    @Test
    public void testGetAnnotationFromEmptyAnnotationMap() {
        assertNull(annotationMap.get(JacksonAnnotation1.class));
    }

    @Test
    public void testGetAnnotationFromNonEmptyAnnotationMap() {
        annotationMap.add(new JacksonAnnotation1());
        assertSame(annotationMap.get(JacksonAnnotation1.class), new JacksonAnnotation1());
    }

    @Test
    public void testGetAnnotationFromNullAnnotationMap() {
        assertNull(AnnotationMap.merge(null, mergedAnnotationMap).get(JacksonAnnotation1.class));
    }

    @Test
    public void testGetAnnotationFromNullAnnotationMapWithNullAnnotation() {
        assertNull(AnnotationMap.merge(annotationMap, null).get(null));
    }

    @Test
    public void testGetAnnotationFromAnnotationMapWithNullAnnotation() {
        assertNull(annotationMap.get(null));
    }

    @Test
    public void testGetAnnotationFromAnnotationMapWithNonExistentAnnotation() {
        assertNull(annotationMap.get(Object.class));
    }

    private static class JacksonAnnotation1 {}

    private static class JacksonAnnotation2 {}
}