package com.fasterxml.jackson.databind.introspect;

import java.lang.annotation.Annotation;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testAnnotationMap() {
        AnnotationMap am = new AnnotationMap();
        // test size
        assertEquals(0, am.size());

        // test get null
        assertNull(am.get(String.class));

        // test add and get
        Annotation annotation1 = new MyAnnotation();
        am.add(annotation1);
        assertEquals(annotation1, am.get(annotation1.annotationType()));

        // test add and get multiple annotations
        Annotation annotation2 = new MyAnnotation();
        am.add(annotation2);
        Annotation annotation3 = new MyAnnotation();
        am.add(annotation3);
        assertEquals(annotation1, am.get(annotation1.annotationType()));
        assertEquals(annotation2, am.get(annotation2.annotationType()));
        assertEquals(annotation3, am.get(annotation3.annotationType()));

        // test size
        assertEquals(3, am.size());

        // test annotations
        List<Annotation> annotations = new ArrayList<>(am.annotations());
        assertEquals(annotation1, annotations.get(0));
        assertEquals(annotation2, annotations.get(1));
        assertEquals(annotation3, annotations.get(2));
    }

    @Test
    public void testMerge() {
        AnnotationMap am1 = new AnnotationMap();
        AnnotationMap am2 = new AnnotationMap();

        Annotation annotation1 = new MyAnnotation();
        Annotation annotation2 = new MyAnnotation();
        Annotation annotation3 = new MyAnnotation();

        am1.add(annotation1);
        am1.add(annotation2);

        am2.add(annotation2);
        am2.add(annotation3);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(am1, am2);

        // test size
        assertEquals(4, mergedAnnotationMap.size());

        // test get
        assertEquals(annotation1, mergedAnnotationMap.get(annotation1.annotationType()));
        assertEquals(annotation2, mergedAnnotationMap.get(annotation2.annotationType()));
        assertEquals(annotation3, mergedAnnotationMap.get(annotation3.annotationType()));

        // test annotations
        List<Annotation> annotations = new ArrayList<>(mergedAnnotationMap.annotations());
        assertEquals(annotation1, annotations.get(0));
        assertEquals(annotation2, annotations.get(1));
        assertEquals(annotation3, annotations.get(2));
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap am = new AnnotationMap();

        Annotation annotation1 = new MyAnnotation();
        Annotation annotation2 = new MyAnnotation();

        // test addIfNotPresent - add annotation1
        assertTrue(am.addIfNotPresent(annotation1));
        assertEquals(annotation1, am.get(annotation1.annotationType()));

        // test addIfNotPresent - add annotation1 again
        assertFalse(am.addIfNotPresent(annotation1));

        // test addIfNotPresent - add annotation2
        assertTrue(am.addIfNotPresent(annotation2));
        assertEquals(annotation2, am.get(annotation2.annotationType()));
    }

    @Test
    public void testAdd() {
        AnnotationMap am = new AnnotationMap();

        Annotation annotation1 = new MyAnnotation();
        Annotation annotation2 = new MyAnnotation();

        // test add - add annotation1
        assertTrue(am.add(annotation1));
        assertEquals(annotation1, am.get(annotation1.annotationType()));

        // test add - add annotation2
        assertTrue(am.add(annotation2));
        assertEquals(annotation2, am.get(annotation2.annotationType()));

        // test add - add annotation1 again
        assertTrue(am.add(annotation1));
        assertEquals(annotation1, am.get(annotation1.annotationType()));
    }

    @Test
    public void testToString() {
        AnnotationMap am = new AnnotationMap();
        Annotation annotation1 = new MyAnnotation();
        Annotation annotation2 = new MyAnnotation();
        am.add(annotation1);
        am.add(annotation2);

        String expected = "{" +
                "MyAnnotation: java.lang.reflect.AnnotatedElement@6b8495e5, " +
                "MyAnnotation: java.lang.reflect.AnnotatedElement@4f7918b1}";

        assertEquals(expected, am.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullAnnotation() {
        AnnotationMap am = new AnnotationMap();
        am.add(null);
    }

    @Test
    public void testGetNullAnnotationType() {
        AnnotationMap am = new AnnotationMap();
        assertNull(am.get(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetUnknownAnnotationType() {
        AnnotationMap am = new AnnotationMap();
        am.get(Object.class);
    }

    @Test
    public void testMergeNullPrimary() {
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new MyAnnotation());
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(null, secondary);
        assertEquals(secondary, mergedAnnotationMap);
    }

    @Test
    public void testMergeNullSecondary() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new MyAnnotation());
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, null);
        assertEquals(primary, mergedAnnotationMap);
    }

    @Test
    public void testMergeEmptyPrimary() {
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new MyAnnotation());
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(new AnnotationMap(), secondary);
        assertEquals(secondary, mergedAnnotationMap);
    }

    @Test
    public void testMergeEmptySecondary() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new MyAnnotation());
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(secondary, primary);
        assertEquals(primary, mergedAnnotationMap);
    }

    @Test
    public void testMergeDifferentAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        Annotation annotation1 = new MyAnnotation1();
        Annotation annotation2 = new MyAnnotation2();
        primary.add(annotation1);
        primary.add(annotation2);

        AnnotationMap secondary = new AnnotationMap();
        Annotation annotation3 = new MyAnnotation1();
        secondary.add(annotation3);
        secondary.add(new MyAnnotation3());

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);

        // test size
        assertEquals(5, mergedAnnotationMap.size());

        // test get
        assertEquals(annotation1, mergedAnnotationMap.get(annotation1.annotationType()));
        assertEquals(annotation2, mergedAnnotationMap.get(annotation2.annotationType()));
        assertEquals(annotation3, mergedAnnotationMap.get(annotation3.annotationType()));
        assertNull(mergedAnnotationMap.get(annotation2.annotationType()));
        assertNull(mergedAnnotationMap.get(annotation3.annotationType()));

        // test annotations
        List<Annotation> annotations = new ArrayList<>(mergedAnnotationMap.annotations());
        assertEquals(annotation1, annotations.get(0));
        assertEquals(annotation2, annotations.get(1));
        assertEquals(annotation3, annotations.get(2));
    }

    @Test
    public void testSize() {
        AnnotationMap am = new AnnotationMap();
        assertEquals(0, am.size());
        Annotation annotation1 = new MyAnnotation();
        Annotation annotation2 = new MyAnnotation();
        am.add(annotation1);
        am.add(annotation2);
        assertEquals(2, am.size());
    }

    static class MyAnnotation extends RuntimeAnnotation {}
    static class MyAnnotation1 extends MyAnnotation {}
    static class MyAnnotation2 extends MyAnnotation {}
    static class MyAnnotation3 extends Annotation {}
}