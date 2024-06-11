package com.example.junit.test;

import static org.junit.Assert.*;

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
        assertNull(annotationMap.get(null));
        assertNull(annotationMap.get(Annotation.class));
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = AnnotationUtils.createTestAnnotation("TestAnnotation");
        annotationMap.add(annotation);
        assertEquals(annotation, annotationMap.get(annotation.annotationType()));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation1 = AnnotationUtils.createTestAnnotation("TestAnnotation1");
        Annotation annotation2 = AnnotationUtils.createTestAnnotation("TestAnnotation2");
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);

        Set<Annotation> expected = new HashSet<Annotation>();
        expected.add(annotation1);
        expected.add(annotation2);

        Set<Annotation> actual = new HashSet<Annotation>(annotationMap.annotations());
        assertEquals(expected, actual);
    }

    @Test
    public void testMerge() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        Annotation annotation1 = AnnotationUtils.createTestAnnotation("TestAnnotation1");
        annotationMap1.add(annotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        Annotation annotation2 = AnnotationUtils.createTestAnnotation("TestAnnotation2");
        annotationMap2.add(annotation2);

        AnnotationMap result = AnnotationMap.merge(annotationMap1, annotationMap2);

        Set<Annotation> expected = new HashSet<Annotation>();
        expected.add(annotation1);
        expected.add(annotation2);

        Set<Annotation> actual = new HashSet<Annotation>(result.annotations());
        assertEquals(expected, actual);
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        Annotation annotation = AnnotationUtils.createTestAnnotation("TestAnnotation");
        annotationMap.add(annotation);
        assertEquals(1, annotationMap.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIfNotPresentNullAnnotationType() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.addIfNotPresent(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddIfNotPresentNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.addIfNotPresent(new Object());
    }

    @Test
    public void testAddIfNotPresentAnnotationAlreadyPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = AnnotationUtils.createTestAnnotation("TestAnnotation");
        annotationMap.add(annotation);
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAddAnnotationAlreadyPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = AnnotationUtils.createTestAnnotation("TestAnnotation");
        annotationMap.add(annotation);
        assertFalse(annotationMap.add(annotation));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAnnotationWithNullAnnotationType() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new Object();
        annotationMap.add(annotation);
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation1 = AnnotationUtils.createTestAnnotation("TestAnnotation1");
        annotationMap.add(annotation1);
        Annotation annotation2 = AnnotationUtils.createTestAnnotation("TestAnnotation2");
        annotationMap.add(annotation2);

        String expected = "{" +
                "AnnotationMap{annotations=[Annotation@7e5b11e4 TestAnnotation1$$EnhancerBySpringCGLIB$$36f71b81, Annotation@3073e56a TestAnnotation2$$EnhancerBySpringCGLIB$$3f8f35a2]}," +
                "size=2}" +
                "";

        String actual = annotationMap.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testIterableAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation1 = AnnotationUtils.createTestAnnotation("TestAnnotation1");
        annotationMap.add(annotation1);
        Annotation annotation2 = AnnotationUtils.createTestAnnotation("TestAnnotation2");
        annotationMap.add(annotation2);

        Iterator<Annotation> it = annotationMap.annotations().iterator();
        Annotation annotation = it.next();
        assertEquals(annotation1, annotation);
        it.hasNext();
        annotation = it.next();
        assertEquals(annotation2, annotation);
        assertFalse(it.hasNext());
    }

    @Test
    public void testEmptyAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.isEmpty());

        Annotation annotation = AnnotationUtils.createTestAnnotation("TestAnnotation");
        annotationMap.add(annotation);
        assertFalse(annotationMap.isEmpty());
    }

    @Test
    public void testContainsAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = AnnotationUtils.createTestAnnotation("TestAnnotation");
        annotationMap.add(annotation);

        assertTrue(annotationMap.containsKey(annotation.annotationType()));
        assertTrue(annotationMap.containsValue(annotation));
    }

    @Test
    public void testContainsKeyNull() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertFalse(annotationMap.containsKey(null));
    }

    @Test
    public void testContainsValueNull() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertFalse(annotationMap.containsValue(null));
    }

    private AnnotationUtils createTestAnnotation(String name) {
        return new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return getClass();
            }

            @Override
            public String name() {
                return name;
            }
        };
    }

}