package com.example.junit; // replace with your package name

import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;
import java.util.*;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize testAnnotation = new JsonDeserialize();
        annotationMap.add(testAnnotation);
        JsonDeserialize returnedAnnotation = annotationMap.get(JsonDeserialize.class);
        assertEquals(testAnnotation, returnedAnnotation);
    }

    @Test
    public void testGetEmptyList() {
        AnnotationMap annotationMap = new AnnotationMap();
        Iterable<Annotation> annotations = annotationMap.annotations();
        assertEmpty(annotations);
    }

    @Test
    public void testMergeEmptyAnnotationMaps() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
        assertTrue(mergedAnnotationMap.isEmpty());
    }

    @Test
    public void testMergeAnnotationMapsWithSingleAnnotation() {
        AnnotationMap primary = new AnnotationMap();
        JsonDeserialize testAnnotation1 = new JsonDeserialize();
        primary.add(testAnnotation1);

        AnnotationMap secondary = new AnnotationMap();
        JsonSerialize testAnnotation2 = new JsonSerialize();
        secondary.add(testAnnotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
        assertEquals(2, mergedAnnotationMap.size());
        assertTrue(mergedAnnotationMap.containsKey(JsonDeserialize.class));
        assertTrue(mergedAnnotationMap.containsKey(JsonSerialize.class));
    }

    @Test
    public void testMergeAnnotationMapsWithSameAnnotation() {
        AnnotationMap primary = new AnnotationMap();
        JsonDeserialize testAnnotation1 = new JsonDeserialize();
        primary.add(testAnnotation1);

        AnnotationMap secondary = new AnnotationMap();
        JsonDeserialize testAnnotation2 = new JsonDeserialize();
        secondary.add(testAnnotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
        assertEquals(1, mergedAnnotationMap.size());
        assertTrue(mergedAnnotationMap.containsKey(JsonDeserialize.class));
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddDifferentAnnotationTypes() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize testAnnotation1 = new JsonDeserialize();
        JsonSerialize testAnnotation2 = new JsonSerialize();
        annotationMap.add(testAnnotation1);
        annotationMap.add(testAnnotation2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize testAnnotation = new JsonDeserialize();
        annotationMap.add(testAnnotation);
        annotationMap._annotations.remove(JsonDeserialize.class);
    }

    @Test
    public void testAnnotationMapSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation testAnnotation = new JsonDeserialize();
        annotationMap.add(testAnnotation);
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize testAnnotation = new JsonDeserialize();
        annotationMap.add(testAnnotation);
        String annotationMapString = annotationMap.toString();
        assertTrue(annotationMapString.contains("{\n  " + JsonDeserialize.class.getName() + "=["));
    }

}