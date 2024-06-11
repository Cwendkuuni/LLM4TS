package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Collections;

import static org.junit.Assert.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(SomeAnnotation.class));

        SomeAnnotation someAnnotation = new SomeAnnotationImpl();
        annotationMap.add(someAnnotation);
        assertEquals(someAnnotation, annotationMap.get(SomeAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.annotations().isEmpty());

        SomeAnnotation someAnnotation = new SomeAnnotationImpl();
        annotationMap.add(someAnnotation);
        assertEquals(Collections.singletonList(someAnnotation), annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        SomeAnnotation someAnnotation1 = new SomeAnnotationImpl();
        primary.add(someAnnotation1);

        SomeAnnotation someAnnotation2 = new SomeAnnotationImpl();
        secondary.add(someAnnotation2);

        AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
        assertEquals(someAnnotation1, mergedMap.get(SomeAnnotation.class));
        assertEquals(someAnnotation2, mergedMap.get(SomeAnnotation.class));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        SomeAnnotation someAnnotation1 = new SomeAnnotationImpl();
        annotationMap.add(someAnnotation1);
        assertEquals(1, annotationMap.size());

        SomeAnnotation someAnnotation2 = new SomeAnnotationImpl();
        annotationMap.add(someAnnotation2);
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();

        SomeAnnotation someAnnotation1 = new SomeAnnotationImpl();
        assertTrue(annotationMap.addIfNotPresent(someAnnotation1));

        SomeAnnotation someAnnotation2 = new SomeAnnotationImpl();
        assertFalse(annotationMap.addIfNotPresent(someAnnotation2));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());

        SomeAnnotation someAnnotation = new SomeAnnotationImpl();
        annotationMap.add(someAnnotation);
        assertEquals("{SomeAnnotation=SomeAnnotationImpl}", annotationMap.toString());
    }

    private interface SomeAnnotation extends Annotation {

    }

    private static class SomeAnnotationImpl implements SomeAnnotation {

    }
}