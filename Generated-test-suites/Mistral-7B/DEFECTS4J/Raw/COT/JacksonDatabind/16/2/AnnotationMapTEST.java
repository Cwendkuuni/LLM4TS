package com.example.annotationmap; // replace with your test package name

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testConstructor() {
        // Test default constructor
        assertNotNull(annotationMap);
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testGetAnnotation() {
        // Add an annotation
        annotationMap.add(new MyAnnotation());

        // Test getting the annotation
        MyAnnotation myAnnotation = annotationMap.get(MyAnnotation.class);
        assertNotNull(myAnnotation);
        assertEquals(myAnnotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testGetAnnotationNull() {
        // Test getting a null annotation
        assertNull(annotationMap.get(NullAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        // Add multiple annotations
        annotationMap.add(new MyAnnotation());
        annotationMap.add(new OtherAnnotation());

        // Test getting all annotations
        Collection<Annotation> annotations = annotationMap.annotations();
        assertNotNull(annotations);
        assertEquals(2, annotations.size());
        assertTrue(annotations.contains(new MyAnnotation()));
        assertTrue(annotations.contains(new OtherAnnotation()));
    }

    @Test
    public void testAnnotationsEmpty() {
        // Test getting an empty list of annotations
        AnnotationMap emptyAnnotationMap = new AnnotationMap();
        Collection<Annotation> annotations = emptyAnnotationMap.annotations();
        assertNotNull(annotations);
        assertEquals(0, annotations.size());
    }

    @Test
    public void testMerge() {
        // Create two AnnotationMaps with different annotations
        AnnotationMap annotationMap1 = new AnnotationMap();
        annotationMap1.add(new MyAnnotation());

        AnnotationMap annotationMap2 = new AnnotationMap();
        annotationMap2.add(new OtherAnnotation());

        // Merge the two AnnotationMaps
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);

        // Test the size of the merged AnnotationMap
        assertEquals(2, mergedAnnotationMap.size());

        // Test getting the annotations from the merged AnnotationMap
        Collection<Annotation> mergedAnnotations = mergedAnnotationMap.annotations();
        assertTrue(mergedAnnotations.contains(new MyAnnotation()));
        assertTrue(mergedAnnotations.contains(new OtherAnnotation()));
    }

    @Test(expected = NullPointerException.class)
    public void testMergeNullPrimary() {
        // Test merging a null primary AnnotationMap
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        secondaryAnnotationMap.add(new OtherAnnotation());

        AnnotationMap mergeResult = AnnotationMap.merge(null, secondaryAnnotationMap);
    }

    @Test(expected = NullPointerException.class)
    public void testMergeNullSecondary() {
        // Test merging a primary AnnotationMap with a null secondary one
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        primaryAnnotationMap.add(new MyAnnotation());

        AnnotationMap mergeResult = AnnotationMap.merge(primaryAnnotationMap, null);
    }

    @Test
    public void testSize() {
        // Test the size of an empty AnnotationMap
        AnnotationMap emptyAnnotationMap = new AnnotationMap();
        assertEquals(0, emptyAnnotationMap.size());

        // Test the size of an AnnotationMap with annotations
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation());
        annotationMap.add(new OtherAnnotation());
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        // Test adding an annotation that doesn't exist yet
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        assertTrue(annotationMap.addIfNotPresent(myAnnotation));
        assertEquals(1, annotationMap.size());

        // Test adding an annotation that already exists
        annotationMap.add(new MyAnnotation());
        assertFalse(annotationMap.addIfNotPresent(myAnnotation));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAdd() {
        // Test adding an annotation that doesn't exist yet
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);
        assertEquals(1, annotationMap.size());

        // Test adding an annotation that already exists
        annotationMap.add(new MyAnnotation());
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testToString() {
        // Test the default toString() method
        AnnotationMap emptyAnnotationMap = new AnnotationMap();
        assertEquals("[null]", emptyAnnotationMap.toString());

        // Test the toString() method with annotations
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation());
        annotationMap.add(new OtherAnnotation());
        assertEquals("[Lcom.example.MyAnnotation; Lcom.example.OtherAnnotation;]", annotationMap.toString());
    }

    private static class MyAnnotation {}

    private static class OtherAnnotation {}

    private static class NullAnnotation {}

}