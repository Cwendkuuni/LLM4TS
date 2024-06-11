package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testCreateAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(NullAnnotation.class));
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new NullAnnotation());
        Annotation expectedAnnotation = new NullAnnotation();
        Annotation actualAnnotation = annotationMap.get(NullAnnotation.class);
        assertEquals(expectedAnnotation.annotationType(), actualAnnotation.annotationType());
        assertEquals(expectedAnnotation, actualAnnotation);
    }

    @Test
    public void testGetAnnotationNullMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(NullAnnotation.class));
    }

    @Test
    public void testGetAnnotationNullAnnotationType() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(null));
    }

    @Test
    public void testAnnotationsEmptyMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        List<Annotation> expectedAnnotations = Collections.emptyList();
        List<Annotation> actualAnnotations = annotationMap.annotations();
        assertEquals(expectedAnnotations, actualAnnotations);
    }

    @Test
    public void testMergeAnnotationMaps() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        primaryAnnotationMap.add(new NullAnnotation());

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        secondaryAnnotationMap.add(new OtherAnnotation());

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        List<Annotation> expectedAnnotations = new ArrayList<>();
        expectedAnnotations.add(new NullAnnotation());
        expectedAnnotations.add(new OtherAnnotation());

        List<Annotation> actualAnnotations = new ArrayList<>();
        for (Annotation annotation : mergedAnnotationMap.annotations()) {
            actualAnnotations.add(annotation);
        }

        assertEquals(expectedAnnotations, actualAnnotations);
    }

    @Test
    public void testMergeAnnotationMapsPrimaryEmpty() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        secondaryAnnotationMap.add(new OtherAnnotation());

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        List<Annotation> expectedAnnotations = new ArrayList<>();
        expectedAnnotations.add(new OtherAnnotation());

        List<Annotation> actualAnnotations = new ArrayList<>();
        for (Annotation annotation : mergedAnnotationMap.annotations()) {
            actualAnnotations.add(annotation);
        }

        assertEquals(expectedAnnotations, actualAnnotations);
    }

    @Test
    public void testMergeAnnotationMapsSecondaryEmpty() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        primaryAnnotationMap.add(new NullAnnotation());

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        List<Annotation> expectedAnnotations = new ArrayList<>();
        expectedAnnotations.add(new NullAnnotation());

        List<Annotation> actualAnnotations = new ArrayList<>();
        for (Annotation annotation : mergedAnnotationMap.annotations()) {
            actualAnnotations.add(annotation);
        }

        assertEquals(expectedAnnotations, actualAnnotations);
    }

    @Test
    public void testMergeAnnotationMapsSameAnnotations() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        primaryAnnotationMap.add(new NullAnnotation());

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        secondaryAnnotationMap.add(new NullAnnotation());

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        List<Annotation> expectedAnnotations = new ArrayList<>();
        expectedAnnotations.add(new NullAnnotation());

        List<Annotation> actualAnnotations = new ArrayList<>();
        for (Annotation annotation : mergedAnnotationMap.annotations()) {
            actualAnnotations.add(annotation);
        }

        assertEquals(expectedAnnotations, actualAnnotations);
    }

    @Test
    public void testMergeAnnotationMapsAddIfNotPresent() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        primaryAnnotationMap.add(new NullAnnotation());

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        secondaryAnnotationMap.add(new OtherAnnotation());

        AnnotationMap mergedAnnotationMap = new AnnotationMap();
        mergedAnnotationMap.addIfNotPresent(new OtherAnnotation());

        AnnotationMap expectedAnnotationMap = new AnnotationMap();
        expectedAnnotationMap.add(new NullAnnotation());
        expectedAnnotationMap.add(new OtherAnnotation());

        AnnotationMap actualAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, mergedAnnotationMap);

        List<Annotation> expectedAnnotations = new ArrayList<>();
        expectedAnnotations.add(new NullAnnotation());
        expectedAnnotations.add(new OtherAnnotation());

        List<Annotation> actualAnnotations = new ArrayList<>();
        for (Annotation annotation : actualAnnotationMap.annotations()) {
            actualAnnotations.add(annotation);
        }

        assertEquals(expectedAnnotations, actualAnnotations);
    }

    @Test
    public void testSizeAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        annotationMap.add(new NullAnnotation());
        assertEquals(1, annotationMap.size());

        annotationMap.add(new OtherAnnotation());
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddAnnotationIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertFalse(annotationMap.addIfNotPresent(new NullAnnotation()));

        annotationMap.add(new NullAnnotation());
        assertTrue(annotationMap.addIfNotPresent(new NullAnnotation()));

        annotationMap.add(new OtherAnnotation());
        assertFalse(annotationMap.addIfNotPresent(new OtherAnnotation()));
    }

    @Test
    public void testAddAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertFalse(annotationMap.add(new NullAnnotation()));

        annotationMap.add(new NullAnnotation());
        assertTrue(annotationMap.add(new NullAnnotation()));

        annotationMap.add(new OtherAnnotation());
        assertTrue(annotationMap.add(new OtherAnnotation()));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("{}", annotationMap.toString());

        annotationMap.add(new NullAnnotation());
        assertEquals("{@null=null}", annotationMap.toString());

        annotationMap.add(new OtherAnnotation());
        assertEquals("{@com.example.OtherAnnotation=AnnotationClassForOtherAnnotation}", annotationMap.toString());

        AnnotationMap annotationMapWithMultipleAnnotations = new AnnotationMap();
        annotationMapWithMultipleAnnotations.add(new NullAnnotation());
        annotationMapWithMultipleAnnotations.add(new OtherAnnotation());
        assertEquals("{@null=null, @com.example.OtherAnnotation=AnnotationClassForOtherAnnotation}", annotationMapWithMultipleAnnotations.toString());
    }

    static class NullAnnotation {}

    static class OtherAnnotation {}
}