package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.lang.annotation.Annotation;
import java.util.*;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testEmptyAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.size() == 0);
        assertNull(annotationMap.get(Object.class));
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation());
        assertEquals(MyAnnotation.class, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testGetAnnotationNull() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Object.class));
    }

    @Test
    public void testGetAnnotationAbsent() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAddAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        assertEquals(annotation, annotationMap.get(annotation.annotationType()));
    }

    @Test
    public void testAddAnnotationPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation1 = new MyAnnotation();
        Annotation annotation2 = new MyAnnotation();
        annotationMap.add(annotation1);
        assertFalse(annotationMap.add(annotation2));
    }

    @Test
    public void testMergeAnnotationMapsEmpty() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertTrue(merged == secondary);
    }

    @Test
    public void testMergeAnnotationMapsSame() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new MyAnnotation());
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new MyAnnotation());
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, merged);
    }

    @Test
    public void testMergeAnnotationMapsDifferent() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new MyAnnotation());
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new OtherAnnotation());
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        AnnotationMap expected = new AnnotationMap();
        expected.add(new MyAnnotation());
        expected.add(new OtherAnnotation());
        assertEquals(expected, merged);
    }

    @Test
    public void testMergeAnnotationMapsNull() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = null;
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertTrue(primary == merged);
    }

    @Test
    public void testSizeAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation());
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testIterableAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation());
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        assertEquals(1, annotations.size());
        assertEquals(MyAnnotation.class, annotations.get(0).annotationType());
    }

    @Test
    public void testToStringAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation());
        String expected = "{\n" +
                "  com.fasterxml.jackson.annotation.JsonPropertyOrder=[class com.fasterxml.jackson.annotation.JsonPropertyOrder]\n" +
                "}";
        assertEquals(expected, annotationMap.toString());
    }

    static class MyAnnotation implements Annotation {
        public static final Class<MyAnnotation> ANNOTATION_TYPE = MyAnnotation.class;
    }

    static class OtherAnnotation implements Annotation {
        public static final Class<OtherAnnotation> ANNOTATION_TYPE = OtherAnnotation.class;
    }
}