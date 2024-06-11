package com.example.annotationmap; // Replace with the actual package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testConstructor() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(String.class));
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(String.class));

        Annotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        assertSame(annotation, annotationMap.get(annotation.annotationType()));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        List<Annotation> annotations = new ArrayList<>();
        annotations.add(new MyAnnotation());
        annotations.add(new OtherAnnotation());
        annotationMap = new AnnotationMap(new HashMap<>(annotations));
        assertEquals(annotations, new ArrayList<>(annotationMap.annotations()));
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        Annotation annotation1 = new MyAnnotation();
        Annotation annotation2 = new OtherAnnotation();
        primary.add(annotation1);
        secondary.add(annotation2);
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(2, merged.size());
        Set<Class<? extends Annotation>> classes = new HashSet<>(3);
        classes.add(annotation1.annotationType());
        classes.add(annotation2.annotationType());
        classes.add(Object.class.getAnnotation(Deprecated.class)); // Edge case
        List<Annotation> mergedAnnotations = new ArrayList<>(merged.annotations());
        assertEquals(classes, new HashSet<>(mergedAnnotations.stream().map(a -> a.annotationType()).collect(Collectors.toList())));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
        Annotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        assertEquals(1, annotationMap.size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddIfNotPresentNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.addIfNotPresent(null);
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new MyAnnotation();
        assertFalse(annotationMap.addIfNotPresent(annotation));
        annotationMap.clear();
        assertTrue(annotationMap.addIfNotPresent(annotation));
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(null);
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation1 = new MyAnnotation();
        Annotation annotation2 = new OtherAnnotation();
        assertTrue(annotationMap.add(annotation1));
        assertTrue(annotationMap.add(annotation2));
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddDuplicateAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new MyAnnotation();
        assertTrue(annotationMap.add(annotation));
        assertFalse(annotationMap.add(annotation));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        assertEquals("[com.example.AnnotationMap$MyAnnotation]", annotationMap.toString());
    }
}