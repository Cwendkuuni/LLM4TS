package com.fasterxml.jackson.databind.introspect.test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.*;

import static org.junit.Assert.*;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;
    private AnnotationMap mergedAnnotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
        mergedAnnotationMap = new AnnotationMap();
    }

    @Test
    public void testGetAnnotation() {
        annotationMap.add(new JsonDeserialize(MyClass.class));
        assertSame(annotationMap.get(JsonDeserialize.class), annotationMap.get(JsonDeserialize.class));
    }

    @Test
    public void testGetNullAnnotation() {
        assertNull(annotationMap.get(Object.class));
    }

    @Test
    public void testannotations() {
        annotationMap.add(new JsonDeserialize(MyClass.class));
        annotationMap.add(new JsonSerialize(MyClass.class));
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        assertEquals(2, annotations.size());
        assertSame(annotations.get(0), annotationMap.get(JsonDeserialize.class));
        assertSame(annotations.get(1), annotationMap.get(JsonSerialize.class));
    }

    @Test
    public void testMerge() {
        annotationMap.add(new JsonDeserialize(MyClass.class));
        mergedAnnotationMap.add(new JsonSerialize(MyClass.class));
        AnnotationMap merged = AnnotationMap.merge(annotationMap, mergedAnnotationMap);
        List<Annotation> mergedAnnotations = new ArrayList<>(merged.annotations());
        assertEquals(2, mergedAnnotations.size());
        assertSame(mergedAnnotations.get(0), annotationMap.get(JsonDeserialize.class));
        assertSame(mergedAnnotations.get(1), mergedAnnotationMap.get(JsonSerialize.class));
    }

    @Test
    public void testSize() {
        annotationMap.add(new JsonDeserialize(MyClass.class));
        annotationMap.add(new JsonSerialize(MyClass.class));
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        annotationMap.add(new JsonDeserialize(MyClass.class));
        assertFalse(annotationMap.addIfNotPresent(new JsonDeserialize(MyClass.class)));
        assertTrue(annotationMap.addIfNotPresent(new JsonSerialize(MyClass.class)));
    }

    @Test
    public void testAdd() {
        annotationMap.add(new JsonDeserialize(MyClass.class));
        annotationMap.add(new JsonSerialize(MyClass.class));
        assertTrue(annotationMap.add(new JsonDeserialize(MyClass.class)));
        assertTrue(annotationMap.add(new JsonSerialize(MyClass.class)));
        assertEquals(4, annotationMap.size());
    }
}

class MyClass {

    @JsonDeserialize
    @JsonSerialize
    public static class InnerClass {

    }
}