package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class AnnotationMapTest {

    @Test
    public void testGet_ReturnsNullWhenAnnotationsIsEmpty() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Annotation.class));
    }

    @Test
    public void testGet_ReturnsAnnotationWhenItExists() {
        Annotation annotation = new TestAnnotation();
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(annotation);

        assertEquals(annotation, annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testGet_ReturnsNullWhenAnnotationDoesNotExist() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testAnnotations_ReturnsEmptyListWhenAnnotationsIsEmpty() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.annotations().size());
    }

    @Test
    public void testAnnotations_ReturnsListOfAnnotations() {
        Annotation annotation1 = new TestAnnotation();
        Annotation annotation2 = new TestAnnotation2();
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);

        List<Annotation> expectedAnnotations = new ArrayList<>();
        expectedAnnotations.add(annotation1);
        expectedAnnotations.add(annotation2);

        assertEquals(expectedAnnotations, annotationMap.annotations());
    }

    @Test
    public void testMerge_ReturnsSecondaryWhenPrimaryIsNull() {
        Annotation annotation = new TestAnnotation();
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(annotation);

        assertNull(AnnotationMap.merge(null, secondary).get(TestAnnotation.class));
    }

    @Test
    public void testMerge_ReturnsPrimaryWhenSecondaryIsNull() {
        Annotation annotation = new TestAnnotation();
        AnnotationMap primary = new AnnotationMap();
        primary.add(annotation);

        assertEquals(annotation, AnnotationMap.merge(primary, null).get(TestAnnotation.class));
    }

    @Test
    public void testMerge_ReturnsPrimaryWhenSecondaryIsEmpty() {
        Annotation annotation = new TestAnnotation();
        AnnotationMap primary = new AnnotationMap();
        primary.add(annotation);

        AnnotationMap secondary = new AnnotationMap();

        assertEquals(annotation, AnnotationMap.merge(primary, secondary).get(TestAnnotation.class));
    }

    @Test
    public void testMerge_ReturnsSecondaryWhenPrimaryIsEmpty() {
        Annotation annotation = new TestAnnotation();
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(annotation);

        AnnotationMap primary = new AnnotationMap();

        assertEquals(annotation, AnnotationMap.merge(primary, secondary).get(TestAnnotation.class));
    }

    @Test
    public void testMerge_ReturnsMergedMap() {
        Annotation annotation1 = new TestAnnotation();
        Annotation annotation2 = new TestAnnotation2();
        AnnotationMap primary = new AnnotationMap();
        primary.add(annotation1);
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(annotation2);

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);

        assertEquals(annotation1, merged.get(TestAnnotation.class));
        assertEquals(annotation2, merged.get(TestAnnotation2.class));
    }

    @Test
    public void testSize_ReturnsZeroWhenAnnotationsIsEmpty() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testSize_ReturnsNumberOfAnnotations() {
        Annotation annotation1 = new TestAnnotation();
        Annotation annotation2 = new TestAnnotation2();
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);

        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent_ReturnsTrueAndAddsAnnotationWhenItDoesNotExist() {
        Annotation annotation = new TestAnnotation();
        AnnotationMap annotationMap = new AnnotationMap();

        assertTrue(annotationMap.addIfNotPresent(annotation));
        assertEquals(annotation, annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testAddIfNotPresent_ReturnsFalseWhenAnnotationExists() {
        Annotation annotation = new TestAnnotation();
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(annotation);

        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAdd_ReturnsTrueAndAddsAnnotation() {
        Annotation annotation = new TestAnnotation();
        AnnotationMap annotationMap = new AnnotationMap();

        assertTrue(annotationMap.add(annotation));
        assertEquals(annotation, annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testToString_ReturnsNullWhenAnnotationsIsNull() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());
    }

    @Test
    public void testToString_ReturnsAnnotationsToString() {
        Annotation annotation1 = new TestAnnotation();
        Annotation annotation2 = new TestAnnotation2();
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);

        HashMap<Class<? extends Annotation>, Annotation> expectedMap = new HashMap<>();
        expectedMap.put(TestAnnotation.class, annotation1);
        expectedMap.put(TestAnnotation2.class, annotation2);

        assertEquals(expectedMap.toString(), annotationMap.toString());
    }

    private static class TestAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation.class;
        }
    }

    private static class TestAnnotation2 implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation2.class;
        }
    }
}