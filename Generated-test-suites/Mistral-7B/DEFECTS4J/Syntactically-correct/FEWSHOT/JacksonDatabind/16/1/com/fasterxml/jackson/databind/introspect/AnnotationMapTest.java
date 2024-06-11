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
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;
    private AnnotationMap anotherAnnotationMap;
    private Annotation annotation;
    private Annotation anotherAnnotation;

    @Before
    public void setup() {
        annotation = new MyAnnotation();
        anotherAnnotation = new MyAnnotation2();
        annotationMap = new AnnotationMap();
        anotherAnnotationMap = new AnnotationMap();
    }

    @Test
    public void testGet() {
        annotationMap.add(annotation);
        assertEquals(annotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testGetNull() {
        assertNull(annotationMap.get(MyAnnotation2.class));
    }

    @Test
    public void testAnnotations() {
        annotationMap.add(annotation);
        List<Annotation> annotations = Arrays.asList(annotation);
        assertEquals(annotations, annotationMap.annotations());
    }

    @Test
    public void testAnnotationsEmptyList() {
        assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        annotationMap.add(annotation);
        anotherAnnotationMap.add(anotherAnnotation);
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, anotherAnnotationMap);
        assertEquals(3, mergedAnnotationMap.size());
        assertEquals(annotation, mergedAnnotationMap.get(MyAnnotation.class));
        assertEquals(anotherAnnotation, mergedAnnotationMap.get(MyAnnotation2.class));
    }

    @Test
    public void testMergePrimaryEmpty() {
        assertEquals(anotherAnnotationMap, AnnotationMap.merge(null, anotherAnnotationMap));
    }

    @Test
    public void testMergeSecondaryEmpty() {
        assertEquals(annotationMap, AnnotationMap.merge(annotationMap, null));
    }

    @Test
    public void testSize() {
        annotationMap.add(annotation);
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        assertFalse(annotationMap.addIfNotPresent(annotation));
        assertTrue(annotationMap.addIfNotPresent(anotherAnnotation));
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAdd() {
        assertFalse(annotationMap.add(annotation));
        assertTrue(annotationMap.add(anotherAnnotation));
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testToString() {
        annotationMap.add(annotation);
        String expected = "{" +
                "MyAnnotation=org.junit.Test@1, " +
                "class java.lang.annotation.Annotation=org.junit.Test@2" +
                "}";
        assertEquals(expected, annotationMap.toString());
    }
}

class MyAnnotation extends java.lang.annotation.Annotation {
}

class MyAnnotation2 extends java.lang.annotation.Annotation {
}