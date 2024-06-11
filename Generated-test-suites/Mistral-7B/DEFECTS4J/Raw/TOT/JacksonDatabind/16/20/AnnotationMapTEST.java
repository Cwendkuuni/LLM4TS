package com.fasterxml.jackson.databind.introspect.test;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AnnotationMapTest {

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);

        Assert.assertEquals(myAnnotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        annotationMap.add(annotation1);
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap.add(annotation2);

        Set<Annotation> annotations = new HashSet<>(annotationMap.annotations());
        Assert.assertEquals(2, annotations.size());
        Assert.assertTrue(annotations.contains(annotation1));
        Assert.assertTrue(annotations.contains(annotation2));
    }

    @Test
    public void testMergingAnnotationMaps() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        annotationMap1.add(annotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap2.add(annotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);

        Set<Annotation> mergedAnnotations = new HashSet<>(mergedAnnotationMap.annotations());
        Assert.assertEquals(2, mergedAnnotations.size());
        Assert.assertTrue(mergedAnnotations.contains(annotation1));
        Assert.assertTrue(mergedAnnotations.contains(annotation2));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        annotationMap.add(annotation1);
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap.add(annotation2);

        Assert.assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddAnnotationIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        Assert.assertTrue(annotationMap.addIfNotPresent(annotation1));
        Assert.assertEquals(1, annotationMap.size());

        MyAnnotation annotation2 = new MyAnnotation();
        Assert.assertTrue(annotationMap.addIfNotPresent(annotation2));
        Assert.assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        Assert.assertTrue(annotationMap.add(annotation));
        Assert.assertEquals(1, annotationMap.size());
    }

    @Test(expected = NullPointerException.class)
    public void testNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownAnnotationType() {
        AnnotationMap annotationMap = new AnnotationMap();
        Object unknownAnnotationType = new Object();
        annotationMap.add(new Annotation() {
            @SuppressWarnings("unchecked")
            Class<Annotation> getAnnotationType() {
                return (Class<Annotation>) unknownAnnotationType;
            }
        });
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);

        Assert.assertFalse(annotationMap.toString().isEmpty());
    }

    static class MyAnnotation implements Annotation {
    }
}