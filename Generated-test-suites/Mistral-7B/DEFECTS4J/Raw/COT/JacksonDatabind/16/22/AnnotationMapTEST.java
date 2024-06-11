package com.example.annotationmap; // replace with the actual package name if necessary

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {

    @Test
    public void testConstructor() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Annotation.class));
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = Annotations.findAnnotation(Annotation.class, Annotation.class);
        annotationMap.add(annotation);
        assertEquals(annotation, annotationMap.get(Annotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation1 = Annotations.findAnnotation(Annotation1.class, Annotation.class);
        Annotation annotation2 = Annotations.findAnnotation(Annotation2.class, Annotation.class);
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);

        Set<Annotation> annotations = new HashSet<>();
        annotations.add(annotation1);
        annotations.add(annotation2);

        Iterator<Annotation> iterator = annotationMap.annotations().iterator();
        while (iterator.hasNext()) {
            Annotation next = iterator.next();
            annotations.remove(next);
        }
        assertTrue(annotations.isEmpty());
    }

    @Test
    public void testMergeEmptyAnnotationMaps() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        AnnotationMap annotationMap2 = new AnnotationMap();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        assertEquals(annotationMap1, mergedAnnotationMap);
    }

    @Test
    public void testMergeAnnotationMapsWithSameAnnotations() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        Annotation annotation = Annotations.findAnnotation(Annotation1.class, Annotation.class);
        annotationMap1.add(annotation);

        AnnotationMap annotationMap2 = new AnnotationMap();
        annotationMap2.add(annotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        assertEquals(annotationMap1, mergedAnnotationMap);
    }

    @Test
    public void testMergeAnnotationMapsWithDifferentAnnotations() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        Annotation annotation1 = Annotations.findAnnotation(Annotation1.class, Annotation.class);
        annotationMap1.add(annotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        Annotation annotation2 = Annotations.findAnnotation(Annotation2.class, Annotation.class);
        annotationMap2.add(annotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        Set<Annotation> annotations = new HashSet<>();
        annotations.add(annotation1);
        annotations.add(annotation2);
        assertEquals(annotations, mergedAnnotationMap.annotations());
    }

    @Test
    public void testMergeAnnotationMapsWithNullAnnotationMap() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        Annotation annotation = Annotations.findAnnotation(Annotation1.class, Annotation.class);
        annotationMap1.add(annotation);

        AnnotationMap annotationMap2 = null;
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        assertEquals(annotationMap1, mergedAnnotationMap);
    }

    @Test(expected = NullPointerException.class)
    public void testMergeAnnotationMapsWithNullPrimaryAnnotationMap() {
        AnnotationMap annotationMap1 = null;
        AnnotationMap annotationMap2 = new AnnotationMap();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        AnnotationMap annotationMapWithAnnotations = new AnnotationMap();
        Annotation annotation1 = Annotations.findAnnotation(Annotation1.class, Annotation.class);
        Annotation annotation2 = Annotations.findAnnotation(Annotation2.class, Annotation.class);
        annotationMapWithAnnotations.add(annotation1);
        annotationMapWithAnnotations.add(annotation2);
        assertEquals(2, annotationMapWithAnnotations.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation1 = Annotations.findAnnotation(Annotation1.class, Annotation.class);
        assertFalse(annotationMap.addIfNotPresent(annotation1));
        assertFalse(annotationMap.addIfNotPresent(annotation1));

        Annotation annotation2 = Annotations.findAnnotation(Annotation2.class, Annotation.class);
        assertTrue(annotationMap.addIfNotPresent(annotation2));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation1 = Annotations.findAnnotation(Annotation1.class, Annotation.class);
        assertTrue(annotationMap.add(annotation1));
        assertEquals(1, annotationMap.size());

        Annotation annotation2 = Annotations.findAnnotation(Annotation2.class, Annotation.class);
        assertTrue(annotationMap.add(annotation2));
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation1 = Annotations.findAnnotation(Annotation1.class, Annotation.class);
        annotationMap.add(annotation1);
        Annotation annotation2 = Annotations.findAnnotation(Annotation2.class, Annotation.class);
        annotationMap.add(annotation2);

        String expectedString = "{" +
                "AnnotationMap{annotations=[Annotation@1, Annotation@2]}," +
                "getClass()=class AnnotationMap," +
                "toString()='AnnotationMap{" +
                "annotations=[Annotation@1, Annotation@2]," +
                "getClass()=class AnnotationMap,'" +
                "hashCode()=247669520," +
                "size()=2}'";
        assertEquals(expectedString, annotationMap.toString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMergeWithSelfAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation1 = Annotations.findAnnotation(Annotation1.class, Annotation.class);
        annotationMap.add(annotation1);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, annotationMap);
        assertEquals(annotationMap, mergedAnnotationMap);
    }

    @Test
    public void testAddSameAnnotationTwice() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = Annotations.findAnnotation(Annotation1.class, Annotation.class);
        assertTrue(annotationMap.add(annotation));
        assertFalse(annotationMap.add(annotation));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertFalse(annotationMap.add(null));
        assertEquals(0, annotationMap.size());
    }

    private static class Annotation1 {}

    private static class Annotation2 {}
}