package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testSize() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.size());

        AnnotationMap mapWithAnnotations = new AnnotationMap();
        mapWithAnnotations.add(new TestAnnotation1());
        mapWithAnnotations.add(new TestAnnotation2());
        assertEquals(2, mapWithAnnotations.size());
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(TestAnnotation1.class));

        AnnotationMap mapWithAnnotations = new AnnotationMap();
        mapWithAnnotations.add(new TestAnnotation1());
        Annotation annotation = mapWithAnnotations.get(TestAnnotation1.class);
        assertSame(annotation, new TestAnnotation1());
    }

    @Test
    public void testGetAnnotationNullMap() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(TestAnnotation1.class));
    }

    @Test
    public void testGetAnnotationNullAnnotationType() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(null));
    }

    @Test
    public void testAnnotationsEmptyMap() {
        AnnotationMap map = new AnnotationMap();
        List<Annotation> annotations = map.annotations();
        assertTrue(annotations.isEmpty());
    }

    @Test
    public void testAnnotationsWithAnnotations() {
        AnnotationMap mapWithAnnotations = new AnnotationMap();
        mapWithAnnotations.add(new TestAnnotation1());
        mapWithAnnotations.add(new TestAnnotation2());

        List<Annotation> annotations = mapWithAnnotations.annotations();
        assertEquals(2, annotations.size());
        assertTrue(annotations.contains(new TestAnnotation1()));
        assertTrue(annotations.contains(new TestAnnotation2()));
    }

    @Test
    public void testAnnotationsNullMap() {
        AnnotationMap map = new AnnotationMap();
        List<Annotation> annotations = map.annotations();
        assertTrue(annotations.isEmpty());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation1 = new TestAnnotation1();
        Annotation annotation2 = new TestAnnotation2();

        assertFalse(map.addIfNotPresent(annotation1));
        assertFalse(map.addIfNotPresent(annotation2));

        map.add(annotation1);
        assertTrue(map.addIfNotPresent(annotation1));
        assertFalse(map.addIfNotPresent(annotation2));

        map.add(annotation2);
        assertTrue(map.addIfNotPresent(annotation2));
    }

    @Test
    public void testAdd() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation1 = new TestAnnotation1();
        Annotation annotation2 = new TestAnnotation2();

        assertFalse(map.add(annotation1));
        assertFalse(map.add(annotation2));

        map.add(annotation1);
        assertTrue(map.add(annotation1));
        assertTrue(map.add(annotation2));
    }

    @Test
    public void testMergeEmptyMaps() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
        assertEquals(0, mergedMap.size());
    }

    @Test
    public void testMergePrimaryEmptyMap() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new TestAnnotation1());

        AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
        assertEquals(1, mergedMap.size());
        assertTrue(mergedMap.containsKey(TestAnnotation1.class));
    }

    @Test
    public void testMergeSecondaryEmptyMap() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        primary.add(new TestAnnotation1());

        AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
        assertEquals(1, mergedMap.size());
        assertTrue(mergedMap.containsKey(TestAnnotation1.class));
    }

    @Test
    public void testMergeWithSameAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new TestAnnotation1());

        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new TestAnnotation1());

        AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
        assertEquals(1, mergedMap.size());
        assertTrue(mergedMap.containsKey(TestAnnotation1.class));
    }

    @Test
    public void testMergeWithDifferentAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new TestAnnotation1());

        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new TestAnnotation2());

        AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
        assertEquals(2, mergedMap.size());
        assertTrue(mergedMap.containsKey(TestAnnotation1.class));
        assertTrue(mergedMap.containsKey(TestAnnotation2.class));
    }

    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap();
        map.add(new TestAnnotation1());
        map.add(new TestAnnotation2());

        String expected = "{@com.fasterxml.jackson.annotation.JsonValue(value=value), @com.fasterxml.jackson.annotation.JsonProperty(value=name)}";
        String result = map.toString();
        assertEquals(expected, result);
    }

    @Test
    public void testToStringNullMap() {
        AnnotationMap map = new AnnotationMap();
        String result = map.toString();
        assertEquals("[null]", result);
    }

    static class TestAnnotation1 {}

    static class TestAnnotation2 {}
}