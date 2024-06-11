package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);

        Annotation result = annotationMap.get(MyAnnotation.class);
        assertEquals(myAnnotation, result);
    }

    @Test
    public void testGetNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();

        Annotation result = annotationMap.get(MyAnnotation.class);
        assertNull(result);
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);

        Iterable<Annotation> annotations = annotationMap.annotations();
        Set<Annotation> annotationSet = new HashSet<>();
        annotationSet.addAll(annotations);

        assertTrue(annotationSet.size() == 2);
        assertTrue(annotationSet.contains(annotation1));
        assertTrue(annotationSet.contains(annotation2));
    }

    @Test
    public void testMergeNullPrimary() {
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        MyAnnotation secondaryAnnotation = new MyAnnotation();
        secondaryAnnotationMap.add(secondaryAnnotation);

        AnnotationMap result = AnnotationMap.merge(null, secondaryAnnotationMap);

        assertEquals(secondaryAnnotationMap, result);
    }

    @Test
    public void testMergeEmptyPrimary() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        MyAnnotation secondaryAnnotation = new MyAnnotation();
        secondaryAnnotationMap.add(secondaryAnnotation);

        AnnotationMap result = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        assertEquals(secondaryAnnotationMap, result);
    }

    @Test
    public void testMergeNonEmptyPrimary() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        MyAnnotation primaryAnnotation = new MyAnnotation();
        primaryAnnotationMap.add(primaryAnnotation);

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        MyAnnotation secondaryAnnotation = new MyAnnotation();
        secondaryAnnotationMap.add(secondaryAnnotation);

        AnnotationMap result = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        Map<Class<? extends Annotation>, Annotation> mergedAnnotations = result._annotations;
        Set<Class<? extends Annotation>> keys = new HashSet<>(mergedAnnotations.keySet());
        Set<Class<? extends Annotation>> expectedKeys = new HashSet<>();
        expectedKeys.add(MyAnnotation.class);

        assertTrue(mergedAnnotations.size() == 1);
        assertTrue(keys.containsAll(expectedKeys));

        Iterator<Annotation> mergedAnnotationsIterator = mergedAnnotations.values().iterator();
        Annotation mergedPrimaryAnnotation = mergedAnnotationsIterator.next();
        assertEquals(primaryAnnotation, mergedPrimaryAnnotation);

        Annotation mergedSecondaryAnnotation = mergedAnnotationsIterator.next();
        assertEquals(secondaryAnnotation, mergedSecondaryAnnotation);
    }

    @Test(expected = NullPointerException.class)
    public void testSizeNullAnnotationMap() {
        AnnotationMap annotationMap = null;

        int size = annotationMap.size();
    }

    @Test
    public void testSizeEmptyAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();

        int size = annotationMap.size();

        assertEquals(0, size);
    }

    @Test
    public void testSizeNonEmptyAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        annotationMap.add(annotation);

        int size = annotationMap.size();

        assertEquals(1, size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();

        annotationMap.add(null);
    }

    @Test
    public void testAddAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();

        boolean result = annotationMap.add(annotation);

        assertTrue(result);
    }

    @Test
    public void testAddAnnotationAlreadyPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        annotationMap.add(annotation);

        boolean result = annotationMap.add(annotation);

        assertEquals(false, result);
    }

    @Test
    public void testToStringEmptyAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();

        String result = annotationMap.toString();

        assertEquals("[null]", result);
    }

    @Test
    public void testToStringNonEmptyAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        annotationMap.add(annotation);

        String result = annotationMap.toString();

        String expected = "[Lcom.fasterxml.jackson.databind.util.Annotation;@" + System.identityHashCode(annotation) + "]";
        assertEquals(expected, result);
    }

    private static class MyAnnotation extends Annotation {
        public static final Class<MyAnnotation> TYPE = MyAnnotation.class;
    }
}