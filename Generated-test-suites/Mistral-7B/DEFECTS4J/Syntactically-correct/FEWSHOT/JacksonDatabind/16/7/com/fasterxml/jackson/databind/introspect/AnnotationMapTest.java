package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.lang.annotation.Annotation;
import java.util.*;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testConstructor() {
        AnnotationMap am = new AnnotationMap();
        assertNotNull(am);
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap am = new AnnotationMap();
        Annotation ann1 = Mockito.mock(Annotation.class);
        Class<Annotation> ann1Type = ann1.annotationType();
        am.add(ann1);

        Annotation result = am.get(ann1Type);
        assertEquals(ann1, result);
    }

    @Test
    public void testGetAnnotationNull() {
        AnnotationMap am = new AnnotationMap();
        assertNull(am.get(Mockito.mock(Annotation.class).annotationType()));
    }

    @Test
    public void testAnnotationsEmptyList() {
        AnnotationMap am = new AnnotationMap();
        assertEquals(Collections.emptyList(), am.annotations());
    }

    @Test
    public void testAnnotationsWithAnnotations() {
        AnnotationMap am = new AnnotationMap();
        Annotation ann1 = Mockito.mock(Annotation.class);
        Annotation ann2 = Mockito.mock(Annotation.class);
        Class<Annotation> ann1Type = ann1.annotationType();
        Class<Annotation> ann2Type = ann2.annotationType();
        am.add(ann1);
        am.add(ann2);

        List<Annotation> annotations = new ArrayList<>(am.annotations());
        assertEquals(2, annotations.size());
        assertTrue(annotations.contains(ann1));
        assertTrue(annotations.contains(ann2));
    }

    @Test
    public void testMergeEmpty() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, result);
    }

    @Test
    public void testMergePrimaryEmpty() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(Mockito.mock(Annotation.class));
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertEquals(secondary, result);
    }

    @Test
    public void testMergeSecondaryEmpty() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(Mockito.mock(Annotation.class));
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, result);
    }

    @Test
    public void testMergeWithSameAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        Annotation ann1 = Mockito.mock(Annotation.class);
        Class<Annotation> ann1Type = ann1.annotationType();
        primary.add(ann1);
        secondary.add(ann1);

        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, result);
    }

    @Test
    public void testMergeNewAnnotationInSecondary() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        Annotation ann1 = Mockito.mock(Annotation.class);
        Class<Annotation> ann1Type = ann1.annotationType();
        secondary.add(ann1);
        Annotation ann2 = Mockito.mock(Annotation.class);
        primary.add(ann2);

        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        List<Annotation> annotations = new ArrayList<>(result.annotations());
        assertEquals(2, annotations.size());
        assertTrue(annotations.contains(ann1));
        assertTrue(annotations.contains(ann2));
    }

    @Test
    public void testMergeExistingAnnotationInPrimary() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        Annotation ann1 = Mockito.mock(Annotation.class);
        Class<Annotation> ann1Type = ann1.annotationType();
        primary.add(ann1);
        Annotation ann2 = Mockito.mock(Annotation.class);
        secondary.add(ann1);

        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        List<Annotation> annotations = new ArrayList<>(result.annotations());
        assertEquals(1, annotations.size());
        assertTrue(annotations.contains(ann1));
    }

    @Test
    public void testSizeZero() {
        AnnotationMap am = new AnnotationMap();
        assertEquals(0, am.size());
    }

    @Test public void testSizeNonZero() {
        AnnotationMap am = new AnnotationMap();
        Annotation ann1 = Mockito.mock(Annotation.class);
        Annotation ann2 = Mockito.mock(Annotation.class);
        am.add(ann1);
        am.add(ann2);
        assertEquals(2, am.size());
    }

    @Test
    public void testAddIfNotPresentTrue() {
        AnnotationMap am = new AnnotationMap();
        Annotation ann1 = Mockito.mock(Annotation.class);
        Class<Annotation> ann1Type = ann1.annotationType();
        assertFalse(am.addIfNotPresent(ann1));
        am.clear();
        assertTrue(am.addIfNotPresent(ann1));
    }

    @Test
    public void testAddIfNotPresentFalse() {
        AnnotationMap am = new AnnotationMap();
        Annotation ann1 = Mockito.mock(Annotation.class);
        Class<Annotation> ann1Type = ann1.annotationType();
        am.add(ann1);
        assertFalse(am.addIfNotPresent(ann1));
    }

    @Test
    public void testAddTrue() {
        AnnotationMap am = new AnnotationMap();
        Annotation ann1 = Mockito.mock(Annotation.class);
        Class<Annotation> ann1Type = ann1.annotationType();
        am.add(ann1);
        assertTrue(am.add(ann1));
    }

    @Test
    public void testToString() {
        AnnotationMap am = new AnnotationMap();
        Annotation ann1 = Mockito.mock(Annotation.class);
        Annotation ann2 = Mockito.mock(Annotation.class);
        am.add(ann1);
        am.add(ann2);
        String expected = String.format("{%s=%s, %s=%s}", ann1.annotationType().getName(), ann1, ann2.annotationType().getName(), ann2);
        assertEquals(expected, am.toString());
    }

    @Test
    public void testToStringEmpty() {
        AnnotationMap am = new AnnotationMap();
        assertEquals("[null]", am.toString());
    }
}