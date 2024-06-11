package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.introspect.AnnotationMap;

/**
 * Test class for AnnotationMap.java
 */
public class AnnotationMapTest {

    @Test
    public void testConstructor() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNotNull(annotationMap);
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(NullAnnotation.class));

        Annotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        assertEquals(annotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testGetNull() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(NullAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertFalse(annotationMap.annotations().hasNext());

        Annotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        List<Annotation> list = new ArrayList<Annotation>(annotationMap.annotations());
        assertEquals(annotation, list.get(0));
        assertEquals(1, list.size());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        Annotation annotation1 = new MyAnnotation1();
        primary.add(annotation1);

        AnnotationMap secondary = new AnnotationMap();
        Annotation annotation2 = new MyAnnotation2();
        secondary.add(annotation2);

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(2, merged.size());
        List<Annotation> list = new ArrayList<Annotation>(merged.annotations());
        assertEquals(annotation1, list.get(0));
        assertEquals(annotation2, list.get(1));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        Annotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertFalse(annotationMap.addIfNotPresent(new MyAnnotation()));
        assertNull(annotationMap.get(MyAnnotation.class));

        Annotation annotation = new MyAnnotation();
        assertTrue(annotationMap.addIfNotPresent(annotation));
        assertEquals(annotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new MyAnnotation();
        assertFalse(annotationMap.add(annotation));
        assertNull(annotationMap.get(MyAnnotation.class));

        annotationMap.addIfNotPresent(annotation);
        assertTrue(annotationMap.add(annotation));
        assertEquals(annotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[]", annotationMap.toString());

        Annotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        String string = annotationMap.toString();
        assertTrue(string.contains(annotation.annotationType().getName()));
    }

    @Test
    public void testMergeEmpty() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, merged);
    }

    @Test
    public void testMergeBothNull() {
        AnnotationMap primary = null;
        AnnotationMap secondary = null;
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(secondary, merged);
    }

    @Test
    public void testMergePrimaryNull() {
        AnnotationMap primary = null;
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(secondary, merged);
    }

    @Test
    public void testMergeSecondaryNull() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = null;
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, merged);
    }

    @Test
    public void testMergeBothNotNull() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(AnnotationMap.merge(primary.annotations(), secondary.annotations()), merged.annotations());
    }

    @Test(expected = NullPointerException.class)
    public void testNullAnnotationType() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.get(null);
    }

    private static class MyAnnotation implements Annotation {
        // Empty implementation of Annotation interface
    }

    private static class MyAnnotation1 implements Annotation {
        // Empty implementation of Annotation interface
    }

    private static class MyAnnotation2 implements Annotation {
        // Empty implementation of Annotation interface
    }

    private static class NullAnnotation {
        // Empty class representing null annotation
    }
}