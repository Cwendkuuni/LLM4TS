
package com.fasterxml.jackson.databind.introspect;

import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import static org.junit.Assert.*;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGetNoAnnotations() {
        assertNull(annotationMap.get(Annotation.class));
    }

    @Test
    public void testGetWithAnnotation() {
        Annotation annotation = new TestAnnotation();
        annotationMap.add(annotation);
        assertEquals(annotation, annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testGetWithoutAnnotation() {
        Annotation annotation = new TestAnnotation();
        annotationMap.add(annotation);
        assertNull(annotationMap.get(AnotherAnnotation.class));
    }

    @Test
    public void testAnnotationsNoAnnotations() {
        assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testAnnotationsWithAnnotations() {
        Annotation annotation1 = new TestAnnotation();
        Annotation annotation2 = new AnotherAnnotation();
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);
        assertEquals(Arrays.asList(annotation1, annotation2), annotationMap.annotations());
    }

    @Test
    public void testAnnotationsEmptyAnnotations() {
        AnnotationMap emptyAnnotationMap = new AnnotationMap();
        assertEquals(Collections.emptyList(), emptyAnnotationMap.annotations());
    }

    @Test
    public void testMergeWithNoAnnotations() {
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, secondaryAnnotationMap);
        assertSame(annotationMap, mergedAnnotationMap);
    }

    @Test
    public void testMergeWithPrimaryAnnotationsOnly() {
        Annotation annotation = new TestAnnotation();
        annotationMap.add(annotation);
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, secondaryAnnotationMap);
        assertSame(annotationMap, mergedAnnotationMap);
    }

    @Test
    public void testMergeWithSecondaryAnnotationsOnly() {
        Annotation annotation = new AnotherAnnotation();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        secondaryAnnotationMap.add(annotation);
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, secondaryAnnotationMap);
        assertSame(secondaryAnnotationMap, mergedAnnotationMap);
    }

    @Test
    public void testMergeWithBothAnnotations() {
        Annotation annotation1 = new TestAnnotation();
        annotationMap.add(annotation1);
        Annotation annotation2 = new AnotherAnnotation();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        secondaryAnnotationMap.add(annotation2);
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, secondaryAnnotationMap);
        assertEquals(Arrays.asList(annotation2, annotation1), mergedAnnotationMap.annotations());
    }

    @Test
    public void testSizeNoAnnotations() {
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testSizeWithAnnotations() {
        annotationMap.add(new TestAnnotation());
        annotationMap.add(new AnotherAnnotation());
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresentWithoutAnnotation() {
        Annotation annotation = new TestAnnotation();
        assertTrue(annotationMap.addIfNotPresent(annotation));
        assertEquals(annotation, annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testAddIfNotPresentWithAnnotation() {
        Annotation annotation = new TestAnnotation();
        annotationMap.add(annotation);
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAddWithoutAnnotation() {
        Annotation annotation = new TestAnnotation();
        assertTrue(annotationMap.add(annotation));
        assertEquals(annotation, annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testAddWithAnnotation() {
        Annotation annotation = new TestAnnotation();
        annotationMap.add(annotation);
        assertFalse(annotationMap.add(annotation));
    }

    private static class TestAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation.class;
        }
    }

    private static class AnotherAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return AnotherAnnotation.class;
        }
    }
}
