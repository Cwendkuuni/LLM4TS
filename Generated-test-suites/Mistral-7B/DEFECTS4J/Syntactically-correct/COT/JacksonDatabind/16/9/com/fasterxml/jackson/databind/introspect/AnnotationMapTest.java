package com.example.annotationmap; // replace with your package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {

    @Test
    public void testConstructor() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(JsonInclude.class));
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonInclude(JsonInclude.Include.NON_NULL));
        Annotation jsonIncludeAnnotation = annotationMap.get(JsonInclude.class);
        assertNotNull(jsonIncludeAnnotation);
        assertEquals(JsonInclude.Include.NON_NULL, jsonIncludeAnnotation.annotationType().getMemberValues()[0].getValue());
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonInclude(JsonInclude.Include.NON_NULL));
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        assertFalse(annotations.isEmpty());
        assertEquals(1, annotations.size());
        Annotation jsonIncludeAnnotation = annotations.get(0);
        assertEquals(JsonInclude.class, jsonIncludeAnnotation.annotationType());
    }

    @Test
    public void testMergeEmptyAnnotationMaps() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
        assertTrue(mergedAnnotationMap instanceof AnnotationMap);
        assertEquals(0, mergedAnnotationMap.size());
    }

    @Test
    public void testMergeAnnotationMapsWithSameAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new JsonInclude(JsonInclude.Include.NON_NULL));

        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new JsonInclude(JsonInclude.Include.NON_NULL));

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
        assertEquals(1, mergedAnnotationMap.size());
    }

    @Test
    public void testMergeAnnotationMapsWithDifferentAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new JsonInclude(JsonInclude.Include.NON_NULL));

        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new JsonSerializer(String.class));

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
        assertEquals(2, mergedAnnotationMap.size());
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonInclude(JsonInclude.Include.NON_NULL));
        annotationMap.add(new JsonSerializer(String.class));
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation jsonIncludeAnnotation = new JsonInclude(JsonInclude.Include.NON_NULL);

        assertFalse(annotationMap.addIfNotPresent(jsonIncludeAnnotation));
        annotationMap.add(new JsonInclude(JsonInclude.Include.NON_NULL));
        assertTrue(annotationMap.addIfNotPresent(jsonIncludeAnnotation));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation jsonIncludeAnnotation = new JsonInclude(JsonInclude.Include.NON_NULL);

        assertFalse(annotationMap.add(jsonIncludeAnnotation));
        annotationMap.add(new JsonInclude(JsonInclude.Include.NON_NULL));
        assertTrue(annotationMap.add(jsonIncludeAnnotation));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonInclude(JsonInclude.Include.NON_NULL));
        annotationMap.add(new JsonSerializer(String.class));
        String toStringResult = annotationMap.toString();
        String expectedToStringResult = "{"
                + "\"class com.fasterxml.jackson.annotation.JsonInclude\":["
                + "{"
                + "\"annotationClass\":\"com.fasterxml.jackson.annotation.JsonInclude\","
                + "\"annotation\":{"
                + "\"class\":\"com.fasterxml.jackson.annotation.JsonInclude\","
                + "\"annotationType\":{\"name\":\"JsonInclude\","
                + "\"value\":{\"elementValue\":\"NON_NULL\",\"memberName\":\"Include\",\"memberValue\":\"NON_NULL\"}}}"
                + "},"
                + "\"class com.fasterxml.jackson.databind.JsonSerializer\":{"
                + "\"annotationClass\":\"com.fasterxml.jackson.databind.JsonSerializer\","
                + "\"annotation\":{"
                + "\"class\":\"com.fasterxml.jackson.databind.JsonSerializer\","
                + "\"annotationType\":{"
                + "\"name\":\"JsonSerializer\","
                + "\"value\":{"
                + "\"parameterTypes\":[\"class java.lang.String\"]}"
                + "}}}"
                + "}]}";
        assertTrue(toStringResult.equals(expectedToStringResult));
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAnnotationOfDifferentType() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonInclude(JsonInclude.Include.NON_NULL));
        annotationMap.add(new JsonSerializer(String[].class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testChangeAnnotationMapSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonInclude(JsonInclude.Include.NON_NULL));
        annotationMap.size(); // calling the size() method to populate the _annotations hashmap
        annotationMap._annotations.clear();
    }
}