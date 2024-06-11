package com.fasterxml.jackson.databind.introspect;

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

import org.junit.Test;

public class AnnotationMapTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testDefaultConstructor() {
        AnnotationMap am = new AnnotationMap();
        assertEquals(0, am.size());
        assertNull(am.get(Object.class));
    }

    @Test
    public void testGetNull() {
        AnnotationMap am = new AnnotationMap();
        assertNull(am.get(Object.class));
    }

    @Test
    public void testGetAnnotation() {
        Annotation testAnnotation = Annotations.JsonValue.class;
        AnnotationMap am = new AnnotationMap();
        am.add(testAnnotation);

        Annotation gotAnnotation = am.get(Annotations.JsonValue.class);
        assertSame(testAnnotation, gotAnnotation);
    }

    @Test
    public void testGetNonExistentAnnotation() {
        AnnotationMap am = new AnnotationMap();
        assertNull(am.get(Object.class));
    }

    @Test
    public void testAnnotationsEmptyList() {
        AnnotationMap am = new AnnotationMap();
        Iterable<Annotation> annotations = am.annotations();
        assertTrue(annotations instanceof Collection);
        assertTrue(annotations.isEmpty());
    }

    @Test
    public void testAnnotationsWithAnnotations() {
        AnnotationMap am = new AnnotationMap();
        Annotation testAnnotation = Annotations.JsonValue.class;
        am.add(testAnnotation);
        List<Annotation> expectedAnnotations = Arrays.asList(testAnnotation);

        Iterable<Annotation> annotations = am.annotations();
        assertTrue(annotations instanceof Collection);
        assertEquals(expectedAnnotations.size(), annotations.size());
        List<Annotation> annotationsList = (List<Annotation>) annotations;
        assertEquals(expectedAnnotations.get(0), annotationsList.get(0));
    }

    @Test
    public void testMergeNullPrimary() {
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(Annotation1.class);
        AnnotationMap merged = AnnotationMap.merge(null, secondary);
        assertEquals(secondary, merged);
    }

    @Test
    public void testMergeNullSecondary() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(Annotation1.class);
        AnnotationMap merged = AnnotationMap.merge(primary, null);
        assertEquals(primary, merged);
    }

    @Test
    public void testMergeEmptyPrimary() {
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(Annotation1.class);
        AnnotationMap merged = AnnotationMap.merge(new AnnotationMap(), secondary);
        assertEquals(secondary, merged);
    }

    @Test
    public void testMergeEmptySecondary() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(Annotation1.class);
        AnnotationMap merged = AnnotationMap.merge(secondary, primary);
        assertEquals(primary, merged);
    }

    @Test
    public void testMergeWithAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(Annotation1.class);

        AnnotationMap secondary = new AnnotationMap();
        secondary.add(Annotation2.class);
        secondary.add(Annotation1.class);

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        List<Annotation> expectedAnnotations = Arrays.asList(Annotation1.class, Annotation2.class);

        Iterable<Annotation> annotations = merged.annotations();
        assertTrue(annotations instanceof Collection);
        assertEquals(expectedAnnotations.size(), annotations.size());
        List<Annotation> annotationsList = (List<Annotation>) annotations;
        assertTrue(annotationsList.containsAll(expectedAnnotations));
    }

    @Test
    public void testSizeZero() {
        AnnotationMap am = new AnnotationMap();
        assertEquals(0, am.size());
    }

    @Test
    public void testSizeWithAnnotations() {
        AnnotationMap am = new AnnotationMap();
        Annotation testAnnotation = Annotations.JsonValue.class;
        am.add(testAnnotation);
        assertEquals(1, am.size());
    }

    @Test
    public void testAddIfNotPresentNullAnnotation() {
        AnnotationMap am = new AnnotationMap();
        assertFalse(am.addIfNotPresent(null));
    }

    @Test
    public void testAddIfNotPresentExistingAnnotation() {
        AnnotationMap am = new AnnotationMap();
        Annotation testAnnotation = Annotations.JsonValue.class;
        am.add(testAnnotation);
        assertFalse(am.addIfNotPresent(testAnnotation));
    }

    @Test
    public void testAddIfNotPresentNewAnnotation() {
        AnnotationMap am = new AnnotationMap();
        Annotation testAnnotation1 = Annotations.JsonValue.class;
        Annotation testAnnotation2 = Annotations.JsonValue.class;
        am.addIfNotPresent(testAnnotation1);
        assertTrue(am.addIfNotPresent(testAnnotation2));
        assertEquals(2, am.size());
    }

    @Test
    public void testAddNewAnnotation() {
        AnnotationMap am = new AnnotationMap();
        Annotation testAnnotation = Annotations.JsonValue.class;
        am.add(testAnnotation);
        assertEquals(1, am.size());
        Annotation newAnnotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotations.JsonIgnore.class;
            }
        };
        am.add(newAnnotation);
        assertEquals(2, am.size());
    }

    @Test
    public void testToStringEmptyMap() {
        AnnotationMap am = new AnnotationMap();
        assertEquals("[null]", am.toString());
    }

    @Test
    public void testToStringWithAnnotations() {
        AnnotationMap am = new AnnotationMap();
        Annotation testAnnotation = Annotations.JsonValue.class;
        am.add(testAnnotation);
        String expectedString = "[class com.fasterxml.jackson.databind.annotation.JsonValue]";
        assertEquals(expectedString, am.toString());
    }

    private static class Annotation1 {}

    private static class Annotation2 {}
}