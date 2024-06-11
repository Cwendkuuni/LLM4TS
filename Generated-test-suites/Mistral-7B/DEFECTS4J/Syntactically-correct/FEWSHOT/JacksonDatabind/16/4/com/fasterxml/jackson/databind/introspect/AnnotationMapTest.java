package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testCreateAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(JsonValue.class));
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);
        assertEquals(myAnnotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testGetNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(JsonValue.class));
    }

    @Test
    public void testAnnotationsEmptyList() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNotNull(annotationMap);
        assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testAnnotationsWithAnnotations() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        MyAnnotation primaryAnnotation1 = new MyAnnotation();
        MyAnnotation primaryAnnotation2 = new MyAnnotation();
        primaryAnnotationMap.add(primaryAnnotation1);
        primaryAnnotationMap.add(primaryAnnotation2);

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        MyAnnotation secondaryAnnotation1 = new MyAnnotation();

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        assertEquals(primaryAnnotation1, mergedAnnotationMap.get(MyAnnotation.class));
        assertEquals(primaryAnnotation2, mergedAnnotationMap.get(MyAnnotation.class));
        assertEquals(secondaryAnnotation1, mergedAnnotationMap.get(MyAnnotation.class));

        assertEquals(3, mergedAnnotationMap.size());
        assertIterableEquals(Arrays.asList(primaryAnnotation1, primaryAnnotation2, secondaryAnnotation1), mergedAnnotationMap.annotations());
    }

    @Test
    public void testAnnotationsWithNullAnnotationMap() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        MyAnnotation primaryAnnotation = new MyAnnotation();
        primaryAnnotationMap.add(primaryAnnotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, null);
        assertEquals(primaryAnnotation, mergedAnnotationMap.get(MyAnnotation.class));
        assertEquals(1, mergedAnnotationMap.size());
    }

    @Test
    public void testAnnotationsWithEmptyAnnotationMap() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        MyAnnotation primaryAnnotation = new MyAnnotation();
        primaryAnnotationMap.add(primaryAnnotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, new AnnotationMap());
        assertEquals(primaryAnnotation, mergedAnnotationMap.get(MyAnnotation.class));
        assertEquals(1, mergedAnnotationMap.size());
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation());
        annotationMap.add(new MyAnnotation());
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        MyAnnotation myAnnotation2 = new MyAnnotation();

        assertTrue(annotationMap.addIfNotPresent(myAnnotation1));
        assertFalse(annotationMap.addIfNotPresent(myAnnotation1));
        assertTrue(annotationMap.addIfNotPresent(myAnnotation2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIfNotPresentWithNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.addIfNotPresent(null);
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        MyAnnotation myAnnotation2 = new MyAnnotation();

        assertTrue(annotationMap.add(myAnnotation1));
        assertTrue(annotationMap.add(myAnnotation2));
        assertEquals(myAnnotation1, annotationMap.get(MyAnnotation.class));
        assertEquals(myAnnotation2, annotationMap.get(MyAnnotation.class));
        assertEquals(2, annotationMap.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddWithNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(null);
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation());
        assertEquals("{class com.fasterxml.jackson.databind.util.AnnotationMap$AnnotationMapImpl@6646ee4f}", annotationMap.toString());
    }

    @Test
    public void testMergeNullPrimaryAnnotationMap() {
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        MyAnnotation secondaryAnnotation = new MyAnnotation();
        secondaryAnnotationMap.add(secondaryAnnotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(null, secondaryAnnotationMap);
        assertEquals(secondaryAnnotation, mergedAnnotationMap.get(MyAnnotation.class));
        assertEquals(1, mergedAnnotationMap.size());
    }

    @Test
    public void testMergeEmptyPrimaryAnnotationMap() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, new AnnotationMap());
        assertEquals(0, mergedAnnotationMap.size());
    }

    @Test
    public void testMergeWithEqualAnnotationTypes() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        MyAnnotation primaryAnnotation1 = new MyAnnotation();
        MyAnnotation primaryAnnotation2 = new MyAnnotation();
        primaryAnnotationMap.add(primaryAnnotation1);
        primaryAnnotationMap.add(primaryAnnotation2);

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        MyAnnotation secondaryAnnotation1 = new MyAnnotation();
        secondaryAnnotationMap.add(secondaryAnnotation1);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        assertEquals(primaryAnnotation1, mergedAnnotationMap.get(MyAnnotation.class));
        assertEquals(primaryAnnotation2, mergedAnnotationMap.get(MyAnnotation.class));
        assertEquals(3, mergedAnnotationMap.size());
        assertIterableEquals(Arrays.asList(primaryAnnotation1, primaryAnnotation2, secondaryAnnotation1), mergedAnnotationMap.annotations());
    }

    @Test
    public void testMergeWithDifferentAnnotationTypes() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        MyAnnotation primaryAnnotation = new MyAnnotation();
        primaryAnnotationMap.add(primaryAnnotation);

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        DifferentAnnotation secondaryAnnotation = new DifferentAnnotation();
        secondaryAnnotationMap.add(secondaryAnnotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        assertEquals(primaryAnnotation, mergedAnnotationMap.get(MyAnnotation.class));
        assertEquals(secondaryAnnotation, mergedAnnotationMap.get(DifferentAnnotation.class));
        assertEquals(2, mergedAnnotationMap.size());
    }

    private interface MyAnnotation extends Annotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface DifferentAnnotation {
    }
}