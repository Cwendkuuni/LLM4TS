package com.example.annotationmap; // replace with your package name

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOClass;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationMapTest {

    @Test
    public void testConstructor() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertNull(annotationMap._annotations);
    }

    @Test
    public void testGetAnnotationNullMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertNull(annotationMap.get(JsonDeserialize.class));
    }

    @Test
    public void testGetAnnotationPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation);
        Assert.assertEquals(jsonDeserializeAnnotation, annotationMap.get(JsonDeserialize.class));
    }

    @Test
    public void testGetAnnotationsEmptyList() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testGetAnnotationsPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        JsonDeserialize jsonDeserializeAnnotation2 = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation1);
        annotationMap.add(jsonDeserializeAnnotation2);
        Assert.assertEquals(Arrays.asList(jsonDeserializeAnnotation1, jsonDeserializeAnnotation2), annotationMap.annotations());
    }

    @Test
    public void testMergeEmptyPrimary() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        secondaryAnnotationMap.add(jsonDeserializeAnnotation);
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        Assert.assertEquals(Arrays.asList(jsonDeserializeAnnotation), mergedAnnotationMap.annotations());
    }

    @Test
    public void testMergeEmptySecondary() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        primaryAnnotationMap.add(jsonDeserializeAnnotation1);
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        Assert.assertEquals(Arrays.asList(jsonDeserializeAnnotation1), mergedAnnotationMap.annotations());
    }

    @Test
    public void testMergeBothPresent() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        primaryAnnotationMap.add(jsonDeserializeAnnotation1);
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation2 = new JsonDeserialize();
        secondaryAnnotationMap.add(jsonDeserializeAnnotation2);
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        Assert.assertEquals(Arrays.asList(jsonDeserializeAnnotation1, jsonDeserializeAnnotation2), mergedAnnotationMap.annotations());
    }

    @Test
    public void testSizeNullMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals(0, annotationMap.size());
    }

    @Test
    public void testSizePresentMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation);
        Assert.assertEquals(1, annotationMap.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAnnotationNullMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(null);
    }

    @Test
    public void testAddAnnotationAlreadyPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation);
        boolean added = annotationMap.add(jsonDeserializeAnnotation);
        Assert.assertFalse(added);
    }

    @Test
    public void testAddAnnotationNewEntry() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation1);
        JsonSerialize jsonSerializeAnnotation = new JsonSerialize();
        boolean added = annotationMap.add(jsonSerializeAnnotation);
        Assert.assertTrue(added);
        Assert.assertEquals(Arrays.asList(jsonDeserializeAnnotation1, jsonSerializeAnnotation), annotationMap.annotations());
    }

    @Test
    public void testToStringEmptyMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals("[null]", annotationMap.toString());
    }

    @Test
    public void testToStringPresentMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation);
        Assert.assertEquals("[Lcom.fasterxml.jackson.databind.annotation.JsonDeserialize;@5e15038b]", annotationMap.toString());
    }

    @Test
    public void testAddAnnotationWithSameAnnotationTypeTwice() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation);
        boolean added = annotationMap.add(jsonDeserializeAnnotation);
        Assert.assertFalse(added);
    }

    @Test
    public void testAddAnnotationWithDifferentAnnotationTypes() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        JsonPOJOClass jsonPOJOPojoClassAnnotation = new JsonPOJOClass();
        annotationMap.add(jsonDeserializeAnnotation);
        annotationMap.add(jsonPOJOPojoClassAnnotation);
        Assert.assertEquals(Arrays.asList(jsonDeserializeAnnotation, jsonPOJOPojoClassAnnotation), annotationMap.annotations());
    }

    @Test
    public void testMergeEmptyMaps() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        Assert.assertEquals(Collections.emptyList(), mergedAnnotationMap.annotations());
    }

    @Test(expected = NullPointerException.class)
    public void testAddAnnotationNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAnnotationWithNonAnnotationObject() {
        AnnotationMap annotationMap = new AnnotationMap();
        Object nonAnnotationObject = new Object();
        annotationMap.add(nonAnnotationObject);
    }
}