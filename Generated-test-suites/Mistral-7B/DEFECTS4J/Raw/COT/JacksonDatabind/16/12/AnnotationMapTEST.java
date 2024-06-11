package com.example.annotationmap; // replace with the actual package name if necessary

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.junit.Test;
import org.junit.Assert.*;
import java.util.*;

public class AnnotationMapTest {

    @Test
    public void testConstructor() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertNotNull(annotationMap);
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation ann = new JsonSerialize();
        annotationMap.add(ann);
        Annotation retrievedAnn = annotationMap.get(JsonSerialize.class);
        Assert.assertNotNull(retrievedAnn);
        Assert.assertEquals(ann, retrievedAnn);
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation ann1 = new JsonSerialize();
        Annotation ann2 = new JsonSerialize();
        annotationMap.add(ann1);
        annotationMap.add(ann2);
        Iterable<Annotation> annotations = annotationMap.annotations();
        List<Annotation> annotationList = new ArrayList<>(annotations);
        Assert.assertEquals(2, annotationList.size());
        Assert.assertEquals(ann1, annotationList.get(0));
        Assert.assertEquals(ann2, annotationList.get(1));
    }

    @Test
    public void testMergeNullPrimary() {
        AnnotationMap primary = null;
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
        Assert.assertEquals(secondary, mergedMap);
    }

    @Test
    public void testMergeEmptyPrimary() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
        Assert.assertEquals(primary, mergedMap);
    }

    @Test
    public void testMergeNonEmptyPrimary() {
        AnnotationMap primary = new AnnotationMap();
        Annotation ann1 = new JsonSerialize();
        primary.add(ann1);
        AnnotationMap secondary = new AnnotationMap();
        Annotation ann2 = new JsonSerialize();
        secondary.add(ann2);
        AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
        Iterable<Annotation> mergedAnnotations = mergedMap.annotations();
        List<Annotation> mergedAnnotationList = new ArrayList<>(mergedAnnotations);
        Assert.assertEquals(2, mergedAnnotationList.size());
        Assert.assertEquals(ann1, mergedAnnotationList.get(0));
        Assert.assertEquals(ann2, mergedAnnotationList.get(1));
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(null);
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals(0, annotationMap.size());
        Annotation ann = new JsonSerialize();
        annotationMap.add(ann);
        Assert.assertEquals(1, annotationMap.size());
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation ann = new JsonSerialize();
        annotationMap.add(ann);
        String expectedToString = "{'class com.fasterxml.jackson.databind.annotation.JsonSerialize': Annotation}";
        Assert.assertEquals(expectedToString, annotationMap.toString());
    }

    @Test
    public void testAddIfNotPresentTrue() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation ann = new JsonSerialize();
        boolean added = annotationMap.addIfNotPresent(ann);
        Assert.assertTrue(added);
        Assert.assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresentFalse() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation ann = new JsonSerialize();
        annotationMap.add(ann);
        boolean added = annotationMap.addIfNotPresent(ann);
        Assert.assertFalse(added);
        Assert.assertEquals(1, annotationMap.size());
    }
}