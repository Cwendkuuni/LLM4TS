package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

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
    public void testSize() {
        AnnotationMap am = new AnnotationMap();
        assertEquals(0, am.size());

        AnnotationMap amWithAnnotations = new AnnotationMap();
        Annotation ann1 = new MyAnnotation1();
        Annotation ann2 = new MyAnnotation2();
        amWithAnnotations.add(ann1);
        amWithAnnotations.add(ann2);
        assertEquals(2, amWithAnnotations.size());
    }

    @Test
    public void testGet() {
        AnnotationMap am = new AnnotationMap();
        Annotation ann1 = new MyAnnotation1();
        Annotation ann2 = new MyAnnotation2();
        am.add(ann1);
        am.add(ann2);

        MyAnnotation1 expectedAnn1 = new MyAnnotation1();
        MyAnnotation2 expectedAnn2 = new MyAnnotation2();

        assertEquals(expectedAnn1, am.get(MyAnnotation1.class));
        assertEquals(expectedAnn2, am.get(MyAnnotation2.class));
        assertNull(am.get(MyOtherAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap am = new AnnotationMap();
        Annotation ann1 = new MyAnnotation1();
        Annotation ann2 = new MyAnnotation2();
        am.add(ann1);
        am.add(ann2);

        List<Annotation> annotations = new ArrayList<>();
        annotations.add(ann1);
        annotations.add(ann2);

        assertEquals(annotations, am.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap am1 = new AnnotationMap();
        Annotation ann1 = new MyAnnotation1();
        am1.add(ann1);

        AnnotationMap am2 = new AnnotationMap();
        Annotation ann2 = new MyAnnotation2();
        am2.add(ann2);

        AnnotationMap mergedMap = AnnotationMap.merge(am1, am2);

        MyAnnotation1 expectedAnn1 = new MyAnnotation1();
        MyAnnotation2 expectedAnn2 = new MyAnnotation2();

        assertEquals(expectedAnn1, mergedMap.get(MyAnnotation1.class));
        assertEquals(expectedAnn2, mergedMap.get(MyAnnotation2.class));

        List<Annotation> expectedAnnotations = new ArrayList<>();
        expectedAnnotations.add(expectedAnn1);
        expectedAnnotations.add(expectedAnn2);

        List<Annotation> mergedAnnotations = new ArrayList<>();
        for (Annotation annotation : mergedMap.annotations()) {
            mergedAnnotations.add(annotation);
        }

        assertEquals(expectedAnnotations, mergedAnnotations);
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap am = new AnnotationMap();
        Annotation ann1 = new MyAnnotation1();

        boolean added = am.addIfNotPresent(ann1);
        assertTrue(added);
        assertEquals(ann1, am.get(MyAnnotation1.class));

        boolean notAdded = am.addIfNotPresent(ann1);
        assertFalse(notAdded);
        assertEquals(ann1, am.get(MyAnnotation1.class));
    }

    @Test
    public void testAdd() {
        AnnotationMap am = new AnnotationMap();
        Annotation ann1 = new MyAnnotation1();

        boolean added = am.add(ann1);
        assertTrue(added);
        assertEquals(ann1, am.get(MyAnnotation1.class));

        boolean alreadyAdded = am.add(ann1);
        assertFalse(alreadyAdded);
        assertEquals(ann1, am.get(MyAnnotation1.class));
    }

    @Test
    public void testToString() {
        AnnotationMap am = new AnnotationMap();
        Annotation ann1 = new MyAnnotation1();
        Annotation ann2 = new MyAnnotation2();
        am.add(ann1);
        am.add(ann2);

        String expectedString = "[class com.fasterxml.jackson.databind.introspect.AnnotationMap@16a8d29, class MyAnnotation1@248a557b, class MyAnnotation2@16c8d29]";

        assertEquals(expectedString, am.toString());
    }

    @Test
    public void testEmptyStringWhenNull() {
        AnnotationMap am = new AnnotationMap();
        assertEquals("[null]", am.toString());
    }

    static class MyAnnotation1 {}
    static class MyAnnotation2 {}
    static class MyOtherAnnotation {}
}