package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertIterableEquals;

import java.lang.annotation.Annotation;
import java.util.*;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        Annotation annotation = mock(Annotation.class);
        annotationMap.add(annotation);
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();

        Annotation annotation = mock(Annotation.class);
        annotationMap.add(annotation);

        assertEquals(annotation, annotationMap.get(annotation.annotationType()));
    }

    @Test
    public void testGetNull() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Mockito.mock(Annotation.class.getClass())));
    }

    @Test
    public void testGetEmpty() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertIterableEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testMergeEmpty() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        assertEquals(primary, AnnotationMap.merge(primary, secondary));
    }

    @Test
    public void testMergePrimaryEmpty() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        Annotation annotation = mock(Annotation.class);
        secondary.add(annotation);
        assertEquals(secondary, AnnotationMap.merge(primary, secondary));
    }

    @Test
    public void testMergeSecondaryEmpty() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        primary.add(mock(Annotation.class));
        assertEquals(primary, AnnotationMap.merge(primary, secondary));
    }

    @Test
    public void testMergeAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        Annotation annotation1 = mock(Annotation.class);
        primary.add(annotation1);

        AnnotationMap secondary = new AnnotationMap();
        Annotation annotation2 = mock(Annotation.class);
        secondary.add(annotation2);

        HashSet<Annotation> expected = new HashSet<>();
        expected.add(annotation1);
        expected.add(annotation2);

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertFalse(primary == merged);
        assertFalse(secondary == merged);
        assertEquals(expected.size(), merged.size());
        assertIterableEquals(expected, merged.annotations());
    }

    @Test
    public void testAddIfNotPresentFalse() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = mock(Annotation.class);
        annotationMap.add(annotation);
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAddIfNotPresentTrue() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = mock(Annotation.class);
        assertNotNull(annotationMap.get(annotation.annotationType()));
        assertTrue(annotationMap.addIfNotPresent(annotation));
        assertIterableEquals(Arrays.asList(annotation), annotationMap.annotations());
    }

    @Test
    public void testAddFalse() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = mock(Annotation.class);
        assertFalse(annotationMap.add(annotation));
        assertNull(annotationMap.get(annotation.annotationType()));
    }

    @Test
    public void testAddTrue() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = mock(Annotation.class);
        assertTrue(annotationMap.add(annotation));
        assertIterableEquals(Arrays.asList(annotation), annotationMap.annotations());
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = mock(Annotation.class);
        annotationMap.add(annotation);

        String expected = "AnnotationMap(annotations=[Annotation@" + annotation.hashCode() + "])";
        assertEquals(expected, annotationMap.toString());
    }
}