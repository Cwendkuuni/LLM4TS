package com.example;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import java.util.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {
    // ... Previous test cases

    // Edge cases and exception handling tests

    @Test
    public void testAnnotationMapNullConstructor() {
        assertNull(new AnnotationMap(null));
    }

    @Test
    public void testAnnotationMapEmptyConstructor() {
        AnnotationMap emptyAnnotationMap = new AnnotationMap();
        assertNotNull(emptyAnnotationMap);
        assertEquals(0, emptyAnnotationMap.size());
    }

    @Test
    public void testAnnotationMapAddSameAnnotationTypeTwice() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = Annotations.create(MyAnnotation.class);
        annotationMap.add(annotation);
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAnnotationMapAddInvalidAnnotationType() {
        AnnotationMap annotationMap = new AnnotationMap();
        Object invalidAnnotationType = new Object() {
        };
        assertFalse(annotationMap.addIfNotPresent(Annotations.create(invalidAnnotationType.getClass())));
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testAnnotationMapAddNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertFalse(annotationMap.addIfNotPresent(null));
        assertEquals(0, annotationMap.size());
    }

    @Test(expected = NullPointerException.class)
    public void testAnnotationMapAddNullAnnotationMap() {
        _add(null, Annotations.create(MyAnnotation.class));
    }

    // ... Previous test cases
}