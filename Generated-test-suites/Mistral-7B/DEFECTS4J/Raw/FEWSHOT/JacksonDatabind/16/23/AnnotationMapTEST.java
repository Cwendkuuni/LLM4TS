package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testSimpleAnnotationMapCreation() {
        AnnotationMap annMap = new AnnotationMap();
        assertEquals(0, annMap.size());
    }

    @Test
    public void testAnnotationMapCreationWithAnnotations() {
        AnnotationMyAnnotation annotation1 = new AnnotationMyAnnotation();
        AnnotationMap annMap = new AnnotationMap();
        annMap.add(annotation1);
        assertEquals(1, annMap.size());
        assertEquals(annotation1, annMap.get(AnnotationMyAnnotation.class));
    }

    @Test
    public void testAnnotationMapCreationWithMultipleAnnotations() {
        AnnotationMyAnnotation annotation1 = new AnnotationMyAnnotation();
        AnnotationMyAnnotation annotation2 = new AnnotationMyAnnotation();
        AnnotationMap annMap = new AnnotationMap();
        annMap.add(annotation1);
        annMap.add(annotation2);
        assertEquals(2, annMap.size());
        assertEquals(annotation1, annMap.get(AnnotationMyAnnotation.class));
        assertEquals(annotation2, annMap.get(AnnotationMyAnnotation.class));
    }

    @Test
    public void testAnnotationMapCreationWithNullAnnotation() {
        AnnotationMap annMap = new AnnotationMap();
        assertFalse(annMap.add(null));
        assertEquals(0, annMap.size());
    }

    @Test
    public void testAnnotationMapCreationWithDuplicateAnnotation() {
        AnnotationMyAnnotation annotation = new AnnotationMyAnnotation();
        AnnotationMap annMap = new AnnotationMap();
        annMap.add(annotation);
        assertFalse(annMap.add(annotation));
        assertEquals(1, annMap.size());
    }

    @Test
    public void testAnnotationMapMerging() {
        AnnotationMyAnnotation annotation1 = new AnnotationMyAnnotation();
        AnnotationMyAnnotation annotation2 = new AnnotationMyAnnotation();
        AnnotationMap annMap1 = new AnnotationMap();
        AnnotationMap annMap2 = new AnnotationMap();
        annMap1.add(annotation1);
        annMap2.add(annotation2);
        AnnotationMap mergedMap = AnnotationMap.merge(annMap1, annMap2);
        assertEquals(2, mergedMap.size());
        assertEquals(annotation1, mergedMap.get(AnnotationMyAnnotation.class));
        assertEquals(annotation2, mergedMap.get(AnnotationMyAnnotation.class));
    }

    @Test
    public void testAnnotationMapMergingWithEmptyMap() {
        AnnotationMyAnnotation annotation = new AnnotationMyAnnotation();
        AnnotationMap annMap1 = new AnnotationMap();
        AnnotationMap annMap2 = new AnnotationMap();
        annMap1.add(annotation);
        AnnotationMap mergedMap = AnnotationMap.merge(annMap1, annMap2);
        assertEquals(1, mergedMap.size());
        assertEquals(annotation, mergedMap.get(AnnotationMyAnnotation.class));
    }

    @Test
    public void testAnnotationMapMergingWithNullMap() {
        AnnotationMyAnnotation annotation = new AnnotationMyAnnotation();
        AnnotationMap annMap1 = new AnnotationMap();
        annMap1.add(annotation);
        AnnotationMap mergedMap = AnnotationMap.merge(annMap1, null);
        assertEquals(1, mergedMap.size());
        assertEquals(annotation, mergedMap.get(AnnotationMyAnnotation.class));
    }

    @Test
    public void testAnnotationMapMergingWithSelf() {
        AnnotationMyAnnotation annotation = new AnnotationMyAnnotation();
        AnnotationMap annMap = new AnnotationMap();
        annMap.add(annotation);
        AnnotationMap mergedMap = AnnotationMap.merge(annMap, annMap);
        assertEquals(1, mergedMap.size());
        assertEquals(annotation, mergedMap.get(AnnotationMyAnnotation.class));
    }

    @Test
    public void testAnnotationMapIteration() {
        AnnotationMyAnnotation annotation1 = new AnnotationMyAnnotation();
        AnnotationMyAnnotation annotation2 = new AnnotationMyAnnotation();
        AnnotationMap annMap = new AnnotationMap();
        annMap.add(annotation1);
        annMap.add(annotation2);
        Set<Annotation> annotations = new HashSet<>();
        for (Annotation annotation : annMap.annotations()) {
            annotations.add(annotation);
        }
        assertEquals(2, annotations.size());
        assertTrue(annotations.contains(annotation1));
        assertTrue(annotations.contains(annotation2));
    }

    @Test
    public void testAnnotationMapToString() {
        AnnotationMyAnnotation annotation1 = new AnnotationMyAnnotation();
        AnnotationMyAnnotation annotation2 = new AnnotationMyAnnotation();
        AnnotationMap annMap = new AnnotationMap();
        annMap.add(annotation1);
        annMap.add(annotation2);
        String string = annMap.toString();
        assertTrue(string.contains("AnnotationMap"));
        assertTrue(string.contains("Size: 2"));
        assertTrue(string.contains("Class: com.fasterxml.jackson.databind.introspect.AnnotationMyAnnotation"));
    }

    private static class AnnotationMyAnnotation implements Annotation {

        @Override
        public Class<? extends Annotation> annotationType() {
            return AnnotationMyAnnotation.class;
        }

        @Override
        public String toString() {
            return "AnnotationMyAnnotation";
        }
    }
}