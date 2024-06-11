package com.example.junit; // replace with your test package

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonRootName;
import com.fasterxml.jackson.databind.util.AnnotationMap;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class AnnotationMapTest {

    @Test
    public void testAnnotationMapCreation() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertNull(annotationMap.get(JsonDeserialize.class));
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation);
        JsonDeserialize annotation = annotationMap.get(JsonDeserialize.class);
        Assert.assertNotNull(annotation);
        Assert.assertEquals(jsonDeserializeAnnotation, annotation);
    }

    @Test
    public void testAnnotationsMethod() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        JsonDeserialize jsonDeserializeAnnotation2 = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation1);
        annotationMap.add(jsonDeserializeAnnotation2);

        Iterable<Annotation> annotations = annotationMap.annotations();
        Set<Annotation> annotationsSet = new HashSet<>();
        for (Annotation annotation : annotations) {
            annotationsSet.add(annotation);
        }

        Assert.assertEquals(2, annotationsSet.size());
        Assert.assertTrue(annotationsSet.contains(jsonDeserializeAnnotation1));
        Assert.assertTrue(annotationsSet.contains(jsonDeserializeAnnotation2));
    }

    @Test
    public void testMergeAnnotationMaps() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        annotationMap1.add(jsonDeserializeAnnotation1);

        AnnotationMap annotationMap2 = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation2 = new JsonDeserialize();
        annotationMap2.add(jsonDeserializeAnnotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);

        Iterable<Annotation> mergedAnnotations = mergedAnnotationMap.annotations();
        Set<Annotation> mergedAnnotationsSet = new HashSet<>();
        for (Annotation annotation : mergedAnnotations) {
            mergedAnnotationsSet.add(annotation);
        }

        Assert.assertEquals(2, mergedAnnotationsSet.size());
        Assert.assertTrue(mergedAnnotationsSet.contains(jsonDeserializeAnnotation1));
        Assert.assertTrue(mergedAnnotationsSet.contains(jsonDeserializeAnnotation2));
    }

    @Test
    public void testSizeMethod() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals(0, annotationMap.size());

        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation);
        Assert.assertEquals(1, annotationMap.size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAnnotationWithNullAnnotationType() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new Object() {};
        annotationMap.add(annotation);
    }

    @Test
    public void testAddAnnotationWithSameAnnotationTypeAlreadyPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation);

        Annotation newJsonDeserializeAnnotation = new JsonDeserialize();
        boolean added = annotationMap.add(jsonDeserializeAnnotation);
        Assert.assertFalse(added);
    }

    @Test
    public void testToStringMethod() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        jsonDeserializeAnnotation1.elementAnnotationTypes().add(JsonPOJOBuilder.class);
        JsonDeserialize jsonDeserializeAnnotation2 = new JsonDeserialize();
        jsonDeserializeAnnotation2.elementAnnotationTypes().add(JsonRootName.class);
        annotationMap.add(jsonDeserializeAnnotation1);
        annotationMap.add(jsonDeserializeAnnotation2);

        Assert.assertEquals("[Lcom.fasterxml.jackson.databind.annotation.JsonDeserialize;@15db9742, Lcom.fasterxml.jackson.annotation.JsonRootName;@5d2d6426]", annotationMap.toString());
    }
}