package com.fasterxml.jackson.databind.introspect;

import java.lang.annotation.Annotation;
import java.util.*;

import static org.junit.Assert.*;
import org.junit.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();

        // Test when map is null
        assertNull(annotationMap.get(TestAnnotation.class));

        // Test when map is empty
        annotationMap._annotations = new HashMap<>();
        assertNull(annotationMap.get(TestAnnotation.class));

        // Test when map is not empty
        TestAnnotation annotation = new TestAnnotation();
        annotationMap._annotations.put(TestAnnotation.class, annotation);
        assertEquals(annotation, annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();

        // Test when map is null
        assertEquals(Collections.emptyList(), annotationMap.annotations());

        // Test when map is empty
        annotationMap._annotations = new HashMap<>();
        assertEquals(Collections.emptyList(), annotationMap.annotations());

        // Test when map is not empty
        TestAnnotation annotation = new TestAnnotation();
        annotationMap._annotations.put(TestAnnotation.class, annotation);
        List<Annotation> expected = new ArrayList<>();
        expected.add(annotation);
        assertEquals(expected, annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        // Test when primary is null
        secondary._annotations = new HashMap<>();
        TestAnnotation annotation = new TestAnnotation();
        secondary._annotations.put(TestAnnotation.class, annotation);
        assertEquals(secondary, AnnotationMap.merge(null, secondary));

        // Test when primary is not null and secondary is null
        primary._annotations = new HashMap<>();
        primary._annotations.put(TestAnnotation.class, annotation);
        assertEquals(primary, AnnotationMap.merge(primary, null));

        // Test when both primary and secondary are not null
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertEquals(annotation, result.get(TestAnnotation.class));
        assertEquals(primary, result);
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();

        // Test when map is null
        assertEquals(0, annotationMap.size());

        // Test when map is empty
        annotationMap._annotations = new HashMap<>();
        assertEquals(0, annotationMap.size());

        // Test when map is not empty
        annotationMap._annotations.put(TestAnnotation.class, new TestAnnotation());
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();

        // Test when map is null
        TestAnnotation annotation = new TestAnnotation();
        assertTrue(annotationMap.addIfNotPresent(annotation));
        assertEquals(annotation, annotationMap._annotations.get(TestAnnotation.class));

        // Test when map has the same annotation
        assertFalse(annotationMap.addIfNotPresent(annotation));

        // Test when map has a different annotation
        annotationMap._annotations.put(TestAnnotation2.class, new TestAnnotation2());
        assertTrue(annotationMap.addIfNotPresent(new TestAnnotation()));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();

        // Test when map is null
        TestAnnotation annotation = new TestAnnotation();
        assertTrue(annotationMap.add(annotation));
        assertEquals(annotation, annotationMap._annotations.get(TestAnnotation.class));

        // Test when map has the same annotation
        assertFalse(annotationMap.add(annotation));

        // Test when map has a different annotation
        annotationMap._annotations.put(TestAnnotation2.class, new TestAnnotation2());
        assertTrue(annotationMap.add(new TestAnnotation()));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();

        // Test when map is null
        assertEquals("[null]", annotationMap.toString());

        // Test when map is empty
        annotationMap._annotations = new HashMap<>();
        assertEquals("{}", annotationMap.toString());

        // Test when map is not empty
        TestAnnotation annotation = new TestAnnotation();
        annotationMap._annotations.put(TestAnnotation.class, annotation);
        assertEquals("{class com.fasterxml.jackson.databind.introspect.AnnotationMapTest$TestAnnotation=com.fasterxml.jackson.databind.introspect.AnnotationMapTest$TestAnnotation@<hashCode>}", annotationMap.toString());
    }

    /*
    /**********************************************************
    /* Helper classes
    /**********************************************************
     */

    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation2 {
    }
}