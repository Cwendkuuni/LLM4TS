package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;
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

import org.junit.Before;
import org.junit.Test;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;
    private AnnotationMap emptyAnnotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
        emptyAnnotationMap = new AnnotationMap();
    }

    @Test
    public void testConstructor() {
        assertEquals(0, emptyAnnotationMap.size());
        assertNull(emptyAnnotationMap.get(Object.class));
    }

    @Test
    public void testGet() {
        Annotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        assertEquals(annotation, annotationMap.get(MyAnnotation.class));
        assertNull(annotationMap.get(String.class));
    }

    @Test
    public void testGetNull() {
        assertEquals(null, annotationMap.get(Object.class));
        assertEquals(null, emptyAnnotationMap.get(Object.class));
    }

    @Test
    public void testAnnotationsEmptyList() {
        assertEquals(Collections.emptyList(), annotationMap.annotations());
        assertEquals(Collections.emptyList(), emptyAnnotationMap.annotations());
    }

    @Test
    public void testAnnotationsWithAnnotations() {
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);

        List<Annotation> annotations = Arrays.asList(annotation1, annotation2);
        List<Annotation> expectedAnnotations = annotationMap.annotations();
        assertEquals(annotations, expectedAnnotations);
    }

    @Test
    public void testAnnotationsEmptyMap() {
        assertEquals(Collections.emptyList(), emptyAnnotationMap.annotations());
    }

    @Test
    public void testSize() {
        assertEquals(0, emptyAnnotationMap.size());
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testMerge() {
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();
        emptyAnnotationMap.add(annotation1);
        annotationMap.add(annotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, emptyAnnotationMap);

        List<Annotation> mergedAnnotations = Arrays.asList(annotation1, annotation2);
        List<Annotation> expectedMergedAnnotations = mergedAnnotationMap.annotations();
        assertEquals(mergedAnnotations, expectedMergedAnnotations);
    }

    @Test
    public void testMergeNull() {
        assertEquals(annotationMap, AnnotationMap.merge(null, annotationMap));
    }

    @Test
    public void testMergeEmptyMap() {
        assertEquals(annotationMap, AnnotationMap.merge(annotationMap, emptyAnnotationMap));
    }

    @Test
    public void testAddIfNotPresent() {
        MyAnnotation annotation = new MyAnnotation();
        assertFalse(annotationMap.addIfNotPresent(annotation));
        assertTrue(annotationMap.addIfNotPresent(new MyAnnotation()));
        assertEquals(annotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAdd() {
        MyAnnotation annotation = new MyAnnotation();
        assertFalse(annotationMap.add(annotation));
        assertTrue(annotationMap.add(new MyAnnotation()));
        assertEquals(annotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testToString() {
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);

        String expectedToString = "{" +
                "MyAnnotation(class com.fasterxml.jackson.databind.introspect.AnnotationMapTest$MyAnnotation)" +
                ", MyAnnotation(class com.fasterxml.jackson.databind.introspect.AnnotationMapTest$MyAnnotation)" +
                "}";
        String actualToString = annotationMap.toString();
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testAddSameAnnotation() {
        MyAnnotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        assertFalse(annotationMap.add(annotation));
    }

    private static class MyAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return MyAnnotation.class;
        }

        @Override
        public String toString() {
            return "MyAnnotation";
        }
    }
}