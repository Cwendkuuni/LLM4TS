package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testAnnotationMapCreation() {
        AnnotationMap am = new AnnotationMap();
        assertNotNull(am);
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap am = new AnnotationMap();
        am.add(new MyAnnotation());
        MyAnnotation annotation = am.get(MyAnnotation.class);
        assertNotNull(annotation);
    }

    @Test
    public void testGetAnnotationNull() {
        AnnotationMap am = new AnnotationMap();
        assertNull(am.get(Object.class));
    }

    @Test
    public void testAnnotationsEmptyList() {
        AnnotationMap am = new AnnotationMap();
        assertTrue(am.annotations().isEmpty());
    }

    @Test
    public void testAnnotationsNonEmptyList() {
        AnnotationMap am = new AnnotationMap();
        am.add(new MyAnnotation());
        List<Annotation> annotations = Arrays.asList(new MyAnnotation(), new MyAnnotation2());
        am.addAll(annotations);
        assertEquals(annotations, am.annotations());
    }

    @Test
    public void testMergeEmptyPrimary() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap(Collections.singletonMap(MyAnnotation.class, new MyAnnotation()));
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(secondary, merged);
    }

    @Test
    public void testMergeEmptySecondary() {
        AnnotationMap primary = new AnnotationMap(Collections.singletonMap(MyAnnotation.class, new MyAnnotation()));
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, merged);
    }

    @Test
    public void testMergeNonEmpty() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new MyAnnotation());
        AnnotationMap secondary = new AnnotationMap(Collections.singletonMap(MyAnnotation2.class, new MyAnnotation2()));
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        List<Annotation> expected = Arrays.asList(new MyAnnotation(), new MyAnnotation2());
        assertEquals(expected, merged.annotations());
    }

    @Test
    public void testSizeZero() {
        AnnotationMap am = new AnnotationMap();
        assertEquals(0, am.size());
    }

    @Test
    public void testSizeNonZero() {
        AnnotationMap am = new AnnotationMap();
        am.add(new MyAnnotation());
        assertEquals(1, am.size());
    }

    @Test
    public void testAddIfNotPresentTrue() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        assertTrue(am.addIfNotPresent(annotation));
        assertTrue(am.containsKey(MyAnnotation.class));
    }

    @Test
    public void testAddIfNotPresentFalse() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        assertFalse(am.addIfNotPresent(annotation));
        assertTrue(am.containsKey(MyAnnotation.class));
    }

    @Test
    public void testAddTrue() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        assertTrue(am.add(annotation));
        assertTrue(am.containsKey(MyAnnotation.class));
    }

    @Test
    public void testToString() {
        AnnotationMap am = new AnnotationMap();
        am.add(new MyAnnotation());
        String expected = "{\n" +
                "  com.fasterxml.jackson.annotation.JsonInclude.Value(CLASS): com.fasterxml.jackson.annotation.JsonInclude$Value@66b3651d\n" +
                "}";
        assertEquals(expected, am.toString());
    }

    public static class MyAnnotation {}

    public static class MyAnnotation2 {}
}