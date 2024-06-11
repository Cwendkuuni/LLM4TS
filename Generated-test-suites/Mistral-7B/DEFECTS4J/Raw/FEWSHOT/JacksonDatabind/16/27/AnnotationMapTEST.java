package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertIterableEquals;
import static org.junit.Assert.assertNull;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testEmptyAnnotationMap() {
        AnnotationMap am = new AnnotationMap();
        assertNull(am.get(Object.class));
        assertEquals(0, am.size());
        assertIterableEquals(Collections.emptyList(), am.annotations());
    }

    @Test
    public void testSingleAnnotationMap() {
        Annotation a = new MyAnnotation();
        AnnotationMap am = new AnnotationMap();
        am.add(a);
        assertEquals(a, am.get(a.getClass()));
        assertEquals(1, am.size());
        List<Annotation> annotations = Arrays.asList(a);
        assertIterableEquals(annotations, am.annotations());
    }

    @Test
    public void testMergeAnnotationMaps() {
        Annotation a1 = new MyAnnotation();
        AnnotationMap am1 = new AnnotationMap();
        am1.add(a1);

        Annotation a2 = new MyAnnotation();
        AnnotationMap am2 = new AnnotationMap();
        am2.add(a2);

        AnnotationMap merged = AnnotationMap.merge(am1, am2);
        assertEquals(a1, merged.get(a1.getClass()));
        assertEquals(a2, merged.get(a2.getClass()));
        assertEquals(2, merged.size());
        Set<Annotation> mergedAnnotations = new HashSet<>();
        mergedAnnotations.addAll(Arrays.asList(a1, a2));
        assertIterableEquals(mergedAnnotations, merged.annotations());
    }

    @Test
    public void testMergePrimaryAnnotationMap() {
        Annotation a1 = new MyAnnotation();
        AnnotationMap am1 = new AnnotationMap();
        am1.add(a1);

        Annotation a2 = new MyAnnotation();
        AnnotationMap am2 = new AnnotationMap(am1);
        am2.add(a2);

        AnnotationMap merged = AnnotationMap.merge(am1, am2);
        assertEquals(a1, merged.get(a1.getClass()));
        assertEquals(a2, merged.get(a2.getClass()));
        assertEquals(2, merged.size());
        Set<Annotation> mergedAnnotations = new HashSet<>();
        mergedAnnotations.addAll(Arrays.asList(a1, a2));
        assertIterableEquals(mergedAnnotations, merged.annotations());
    }

    @Test
    public void testMergeSecondaryAnnotationMap() {
        Annotation a1 = new MyAnnotation();
        AnnotationMap am1 = new AnnotationMap();
        am1.add(a1);

        Annotation a2 = new MyAnnotation();
        AnnotationMap am2 = new AnnotationMap();
        am2 = new AnnotationMap(am1);
        am2.add(a2);

        AnnotationMap merged = AnnotationMap.merge(am2, am1);
        assertEquals(a2, merged.get(a2.getClass()));
        assertEquals(a1, merged.get(a1.getClass()));
        assertEquals(2, merged.size());
        Set<Annotation> mergedAnnotations = new HashSet<>();
        mergedAnnotations.addAll(Arrays.asList(a1, a2));
        assertIterableEquals(mergedAnnotations, merged.annotations());
    }

    @Test
    public void testMergeIdenticalAnnotationMaps() {
        Annotation a = new MyAnnotation();
        AnnotationMap am1 = new AnnotationMap();
        am1.add(a);

        AnnotationMap am2 = new AnnotationMap(am1);
        am2.add(a);

        AnnotationMap merged = AnnotationMap.merge(am1, am2);
        assertEquals(a, merged.get(a.getClass()));
        assertEquals(1, merged.size());
        Set<Annotation> mergedAnnotations = new HashSet<>();
        mergedAnnotations.add(a);
        assertIterableEquals(mergedAnnotations, merged.annotations());
    }

    @Test
    public void testAddIfNotPresent() {
        Annotation a = new MyAnnotation();
        AnnotationMap am = new AnnotationMap();
        assertFalse(am.addIfNotPresent(a));
        assertNull(am.get(a.getClass()));
        assertEquals(0, am.size());

        am.add(a);
        assertTrue(am.addIfNotPresent(a));
        assertEquals(a, am.get(a.getClass()));
        assertEquals(1, am.size());
    }

    @Test
    public void testAdd() {
        Annotation a = new MyAnnotation();
        AnnotationMap am = new AnnotationMap();
        assertTrue(am.add(a));
        assertEquals(a, am.get(a.getClass()));
        assertEquals(1, am.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testUnmodifiableAnnotationMap() {
        AnnotationMap am = Collections.unmodifiableMap(new AnnotationMap());
        am.add(new MyAnnotation());
    }

    @Test
    public void testToString() {
        Annotation a = new MyAnnotation();
        AnnotationMap am = new AnnotationMap();
        am.add(a);
        String toString = am.toString();
        String expectedToString = String.format("[AnnotationMap{annotations=[%s]}]", Arrays.toString(am.annotations().toArray()));
        assertEquals(expectedToString, toString);
    }

    static class MyAnnotation {}
}