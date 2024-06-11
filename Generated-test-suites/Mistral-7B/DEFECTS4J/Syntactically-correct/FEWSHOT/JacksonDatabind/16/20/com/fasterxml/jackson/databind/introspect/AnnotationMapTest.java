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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testConstructor() {
        AnnotationMap am = new AnnotationMap();
        assertEquals(0, am.size());
        assertNull(am.get(Object.class));
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        am.add(annotation);
        assertEquals(annotation, am.get(MyAnnotation.class));
        assertNull(am.get(String.class));
    }

    @Test
    public void testGetAnnotationNull() {
        AnnotationMap am = new AnnotationMap();
        assertNull(am.get(Object.class));
    }

    @Test
    public void testAnnotationsEmptyList() {
        AnnotationMap am = new AnnotationMap();
        assertTrue(am.annotations().isEmpty());
    }

    @Test
    public void testAnnotationsNonEmptyList() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();
        am.add(annotation1);
        am.add(annotation2);
        List<Annotation> annotations = new ArrayList<>(am.annotations());
        assertEquals(2, annotations.size());
        assertEquals(annotation1, annotations.get(0));
        assertEquals(annotation2, annotations.get(1));
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
        MyAnnotation annotation = new MyAnnotation();
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(annotation);
        assertEquals(secondary, AnnotationMap.merge(primary, secondary));
    }

    @Test
    public void testMergeSecondaryEmpty() {
        AnnotationMap primary = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        primary.add(annotation);
        assertEquals(primary, AnnotationMap.merge(primary, new AnnotationMap()));
    }

    @Test
    public void testMergeAddition() {
        AnnotationMap primary = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();
        primary.add(annotation1);
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(annotation2);
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        List<Annotation> annotations = new ArrayList<>(merged.annotations());
        assertEquals(2, annotations.size());
        assertEquals(annotation1, annotations.get(0));
        assertEquals(annotation2, annotations.get(1));
    }

    @Test
    public void testMergeOverwrite() {
        AnnotationMap primary = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        primary.add(annotation1);
        AnnotationMap secondary = new AnnotationMap();
        MyAnnotation annotation2 = new MyAnnotation();
        secondary.add(annotation2);
        primary.add(annotation2);
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        List<Annotation> annotations = new ArrayList<>(merged.annotations());
        assertEquals(1, annotations.size());
        assertEquals(annotation2, annotations.get(0));
    }

    @Test
    public void testMergeOverwriteSameType() {
        AnnotationMap primary = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        primary.add(annotation1);
        AnnotationMap secondary = new AnnotationMap();
        MyAnnotation annotation2 = new MyAnnotation();
        secondary.add(annotation2);
        primary.add(annotation2);
        primary.add(new MyAnnotation());
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        List<Annotation> annotations = new ArrayList<>(merged.annotations());
        assertEquals(2, annotations.size());
        assertNotSame(annotation1, annotations.get(0));
        assertSame(annotation2, annotations.get(1));
    }

    @Test
    public void testMergeOverwriteDifferentTypes() {
        AnnotationMap primary = new AnnotationMap();
        MyAnnotation1 annotation1 = new MyAnnotation1();
        primary.add(annotation1);
        AnnotationMap secondary = new AnnotationMap();
        MyAnnotation2 annotation2 = new MyAnnotation2();
        secondary.add(annotation2);
        primary.add(annotation2);
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        List<Annotation> annotations = new ArrayList<>(merged.annotations());
        assertEquals(2, annotations.size());
        assertNotSame(annotation1, annotations.get(0));
        assertSame(annotation2, annotations.get(1));
    }

    @Test
    public void testSize() {
        AnnotationMap am = new AnnotationMap();
        assertEquals(0, am.size());
        MyAnnotation annotation = new MyAnnotation();
        am.add(annotation);
        assertEquals(1, am.size());
    }

    @Test
    public void testSizeAddition() {
        AnnotationMap am1 = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        am1.add(annotation1);
        AnnotationMap am2 = new AnnotationMap();
        MyAnnotation annotation2 = new MyAnnotation();
        am2.add(annotation2);
        AnnotationMap merged = AnnotationMap.merge(am1, am2);
        assertEquals(2, merged.size());
    }

    @Test
    public void testSizeOverwrite() {
        AnnotationMap am1 = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        am1.add(annotation1);
        AnnotationMap am2 = new AnnotationMap();
        MyAnnotation annotation2 = new MyAnnotation();
        am2.add(annotation2);
        AnnotationMap merged = new AnnotationMap(am1._annotations);
        merged.add(annotation2);
        assertEquals(1, merged.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        assertTrue(am.addIfNotPresent(annotation));
        assertEquals(annotation, am.get(MyAnnotation.class));
        assertTrue(am.addIfNotPresent(annotation));
        assertFalse(am.addIfNotPresent(annotation));
    }

    @Test
    public void testAddIfNotPresentNull() {
        AnnotationMap am = new AnnotationMap();
        assertFalse(am.addIfNotPresent(null));
    }

    @Test
    public void testAdd() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        assertTrue(am.add(annotation));
        assertEquals(annotation, am.get(MyAnnotation.class));
    }

    @Test
    public void testAddOverwrite() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();
        am.add(annotation1);
        assertTrue(am.add(annotation2));
        assertEquals(annotation2, am.get(MyAnnotation.class));
    }

    @Test
    public void testToString() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        am.add(annotation);
        String expected = "AnnotationMap{annotations=[Annotation(MyAnnotation)]}";
        assertEquals(expected, am.toString());
    }

    @Test
    public void testToStringEmpty() {
        AnnotationMap am = new AnnotationMap();
        String expected = "AnnotationMap{annotations=[]}";
        assertEquals(expected, am.toString());
    }

    @Test
    public void testToStringMerge() {
        AnnotationMap am1 = new AnnotationMap();
        MyAnnotation1 annotation1 = new MyAnnotation1();
        am1.add(annotation1);
        AnnotationMap am2 = new AnnotationMap();
        MyAnnotation2 annotation2 = new MyAnnotation2();
        am2.add(annotation2);
        AnnotationMap merged = AnnotationMap.merge(am1, am2);
        String expected = "AnnotationMap{annotations=[Annotation(MyAnnotation1), Annotation(MyAnnotation2)]}";
        assertEquals(expected, merged.toString());
    }

    private static class MyAnnotation extends JacksonAnnotation {}

    private static class MyAnnotation1 extends JacksonAnnotation {}

    private static class MyAnnotation2 extends JacksonAnnotation {}

    private static class JacksonAnnotation {}

}