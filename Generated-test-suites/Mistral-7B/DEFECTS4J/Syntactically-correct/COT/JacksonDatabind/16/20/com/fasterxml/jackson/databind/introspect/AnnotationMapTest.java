package com.example.annotationmap; // replace with your package name

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.AnnotationMap;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationMapTest {

    @Test
    public void testAnnotationMapConstructor() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertNull(annotationMap.get(JsonDeserialize.class));
        Assert.assertEquals(0, annotationMap.size());
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize());
        JsonDeserialize jsonDeserialize = annotationMap.get(JsonDeserialize.class);
        Assert.assertNotNull(jsonDeserialize);
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize());
        annotationMap.add(new JsonSerialize());
        List<Annotation> annotations = Arrays.asList(annotationMap.annotations().toArray(new Annotation[0]));
        Assert.assertFalse(annotations.isEmpty());
        Assert.assertEquals(2, annotations.size());
        Assert.assertTrue(annotations.contains(new JsonDeserialize()));
        Assert.assertTrue(annotations.contains(new JsonSerialize()));
    }

    @Test
    public void testMerge() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        annotationMap1.add(new JsonDeserialize());

        AnnotationMap annotationMap2 = new AnnotationMap();
        annotationMap2.add(new JsonSerialize());

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);

        List<Annotation> mergedAnnotations = Arrays.asList(mergedAnnotationMap.annotations().toArray(new Annotation[0]));
        Assert.assertFalse(mergedAnnotations.isEmpty());
        Assert.assertEquals(2, mergedAnnotations.size());
        Assert.assertTrue(mergedAnnotations.contains(new JsonDeserialize()));
        Assert.assertTrue(mergedAnnotations.contains(new JsonSerialize()));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize());
        Assert.assertEquals(1, annotationMap.size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddIfNotPresentNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.addIfNotPresent(null);
    }

    @Test
    public void testAddIfNotPresentExistingAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize());
        Assert.assertFalse(annotationMap.addIfNotPresent(new JsonDeserialize()));
    }

    @Test
    public void testAddExistingAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize());
        Assert.assertFalse(annotationMap.add(new JsonDeserialize()));
    }

    @Test
    public void testAddNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation nullAnnotation = null;
        Assert.assertFalse(annotationMap.add(nullAnnotation));
    }

    @Test
    public void testAddAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return new TestAnnotation().getClass();
            }
        };
        Assert.assertTrue(annotationMap.add(annotation));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize());
        annotationMap.add(new JsonSerialize());
        String toString = annotationMap.toString();
        Assert.assertFalse(toString.isEmpty());
    }

    @Test
    public void testEmptyAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals(Collections.emptyList(), annotationMap.annotations());
        Assert.assertEquals(0, annotationMap.size());
    }
}