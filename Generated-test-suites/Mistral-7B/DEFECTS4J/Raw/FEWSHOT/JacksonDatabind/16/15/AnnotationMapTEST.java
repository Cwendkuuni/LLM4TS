package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.lang.annotation.Annotation;
import java.util.*;

import org.junit.Test;

public class AnnotationMapTest {

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
        assertNull(annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        MyAnnotation myAnnotation2 = new MyAnnotation();
        annotationMap.add(myAnnotation1);
        annotationMap.add(myAnnotation2);
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        assertEquals(2, annotations.size());
        assertEquals(myAnnotation1, annotations.get(0));
        assertEquals(myAnnotation2, annotations.get(1));
    }

    @Test
    public void testMerge() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        annotationMap1.add(myAnnotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        MyAnnotation myAnnotation2 = new MyAnnotation();
        annotationMap2.add(myAnnotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        List<Annotation> mergedAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
        assertEquals(2, mergedAnnotations.size());
        assertEquals(myAnnotation1, mergedAnnotations.get(0));
        assertEquals(myAnnotation2, mergedAnnotations.get(1));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);
        assertEquals(1, annotationMap.size());
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

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        MyAnnotation myAnnotation2 = new MyAnnotation();
        assertTrue(annotationMap.add(myAnnotation1));
        assertTrue(annotationMap.add(myAnnotation2));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);
        String expected = String.format("{%s=Annotation@%s}", AnnotationMap.class.getName(), Long.toHexString(myAnnotation.hashCode()));
        assertEquals(expected, annotationMap.toString());
    }

    @Test
    public void testNullAnnotationMap() {
        assertNull(new AnnotationMap(null));
    }

    @Test
    public void testEmptyAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    private static class MyAnnotation implements Annotation {}
}