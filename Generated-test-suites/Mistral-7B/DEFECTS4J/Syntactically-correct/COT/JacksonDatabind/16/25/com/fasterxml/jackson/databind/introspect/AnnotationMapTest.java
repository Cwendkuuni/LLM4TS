package com.example.annotationmap; // replace with your package name

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Test class for AnnotationMap
 */
public class AnnotationMapTest {

    /**
     * Test constructor
     */
    @Test
    public void testConstructor() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(JsonDeserialize.class));
        assertEquals(0, annotationMap.size());
    }

    /**
     * Test get method
     */
    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize());
        JsonDeserialize jsonDeserialize = annotationMap.get(JsonDeserialize.class);
        assertNotNull(jsonDeserialize);
        assertEquals(JsonDeserialize.class, jsonDeserialize.annotationType());
    }

    /**
     * Test get null return
     */
    @Test
    public void testGetNullReturn() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Object.class));
    }

    /**
     * Test annotations method
     */
    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize());
        annotationMap.add(new JsonSerialize());

        List<Annotation> annotations = Arrays.asList(new JsonDeserialize(), new JsonSerialize());
        List<Annotation> annotationsFromAnnotationMap = new ArrayList<>(annotationMap.annotations());

        assertEquals(annotations, annotationsFromAnnotationMap);
    }

    /**
     * Test empty annotations method
     */
    @Test
    public void testEmptyAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        List<Annotation> annotations = Collections.emptyList();
        List<Annotation> annotationsFromAnnotationMap = new ArrayList<>(annotationMap.annotations());

        assertEquals(annotations, annotationsFromAnnotationMap);
    }

    /**
     * Test merge method
     */
    @Test
    public void testMerge() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        annotationMap1.add(new JsonDeserialize());

        AnnotationMap annotationMap2 = new AnnotationMap();
        annotationMap2.add(new JsonSerialize());

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);

        List<Annotation> expectedAnnotations = Arrays.asList(new JsonDeserialize(), new JsonSerialize());
        List<Annotation> annotationsFromMergedAnnotationMap = new ArrayList<>(mergedAnnotationMap.annotations());

        assertEquals(expectedAnnotations, annotationsFromMergedAnnotationMap);
    }

    /**
     * Test merge primary empty map
     */
    @Test
    public void testMergePrimaryEmptyMap() {
        AnnotationMap primaryAnnotationMap = null;
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        secondaryAnnotationMap.add(new JsonSerialize());

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        List<Annotation> annotationsFromMergedAnnotationMap = new ArrayList<>(mergedAnnotationMap.annotations());
        List<Annotation> expectedAnnotations = Collections.singletonList(new JsonSerialize());

        assertEquals(expectedAnnotations, annotationsFromMergedAnnotationMap);
    }

    /**
     * Test merge secondary empty map
     */
    @Test
    public void testMergeSecondaryEmptyMap() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        primaryAnnotationMap.add(new JsonDeserialize());

        AnnotationMap secondaryAnnotationMap = null;

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        List<Annotation> annotationsFromMergedAnnotationMap = new ArrayList<>(mergedAnnotationMap.annotations());
        List<Annotation> expectedAnnotations = Arrays.asList(new JsonDeserialize());

        assertEquals(expectedAnnotations, annotationsFromMergedAnnotationMap);
    }

    /**
     * Test size method
     */
    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        int size = annotationMap.size();
        assertEquals(0, size);

        annotationMap.add(new JsonDeserialize());
        size = annotationMap.size();
        assertEquals(1, size);

        annotationMap.add(new JsonSerialize());
        size = annotationMap.size();
        assertEquals(2, size);
    }

    /**
     * Test addIfNotPresent method
     */
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertFalse(annotationMap.addIfNotPresent(new JsonDeserialize()));
        assertTrue(annotationMap.addIfNotPresent(new JsonSerialize()));
        assertEquals(1, annotationMap.size());
    }

    /**
     * Test add method
     */
    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.add(new JsonDeserialize()));
        assertEquals(1, annotationMap.size());

        assertTrue(annotationMap.add(new JsonSerialize()));
        assertEquals(2, annotationMap.size());
    }

    /**
     * Test toString method
     */
    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize());
        annotationMap.add(new JsonSerialize());

        String expectedToString = "{" +
                " JsonDeserialize(AnnotationType: class com.fasterxml.jackson.databind.annotation.JsonDeserialize)" +
                ", JsonSerialize(AnnotationType: class com.fasterxml.jackson.databind.annotation.JsonSerialize)" +
                "}";

        String toString = annotationMap.toString();
        assertEquals(expectedToString, toString);
    }

    /**
     * Test add existing annotation
     */
    @Test(expected = IllegalStateException.class)
    public void testAddExistingAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize());
        annotationMap.add(new JsonDeserialize());
    }
}