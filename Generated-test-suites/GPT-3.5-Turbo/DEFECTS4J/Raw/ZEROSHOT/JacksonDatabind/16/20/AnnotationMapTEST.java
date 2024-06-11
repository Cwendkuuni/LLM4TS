package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;

import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationMapTest {

    @Test
    public void testGet_NullAnnotations_ReturnsNull() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Test.class));
    }

    @Test
    public void testGet_ValidAnnotations_ReturnsAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        assertNotNull(annotationMap.get(Test.class));
    }

    @Test
    public void testAnnotations_NullAnnotations_ReturnsEmptyList() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testAnnotations_ValidAnnotations_ReturnsListWithAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        assertEquals(Arrays.asList(new TestAnnotation()), annotationMap.annotations());
    }

    @Test
    public void testMerge_NullPrimary_ReturnsSecondary() {
        AnnotationMap primary = null;
        AnnotationMap secondary = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        secondary = new AnnotationMap(annotations);
        assertEquals(secondary, AnnotationMap.merge(primary, secondary));
    }

    @Test
    public void testMerge_NullSecondary_ReturnsPrimary() {
        AnnotationMap primary = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        primary = new AnnotationMap(annotations);
        AnnotationMap secondary = null;
        assertEquals(primary, AnnotationMap.merge(primary, secondary));
    }

    @Test
    public void testMerge_ValidAnnotations_ReturnsMergedAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        HashMap<Class<? extends Annotation>, Annotation> primaryAnnotations = new HashMap<>();
        primaryAnnotations.put(Test.class, new TestAnnotation());
        primary = new AnnotationMap(primaryAnnotations);

        HashMap<Class<? extends Annotation>, Annotation> secondaryAnnotations = new HashMap<>();
        secondaryAnnotations.put(Test.class, new TestAnnotation2());
        secondary = new AnnotationMap(secondaryAnnotations);

        HashMap<Class<? extends Annotation>, Annotation> mergedAnnotations = new HashMap<>();
        mergedAnnotations.put(Test.class, new TestAnnotation2());
        mergedAnnotations.put(Test.class, new TestAnnotation());
        AnnotationMap expected = new AnnotationMap(mergedAnnotations);

        assertEquals(expected, AnnotationMap.merge(primary, secondary));
    }

    @Test
    public void testSize_NullAnnotations_ReturnsZero() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testSize_ValidAnnotations_ReturnsSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent_NullAnnotations_ReturnsTrue() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.addIfNotPresent(new TestAnnotation()));
    }

    @Test
    public void testAddIfNotPresent_AnnotationPresent_ReturnsFalse() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        assertFalse(annotationMap.addIfNotPresent(new TestAnnotation()));
    }

    @Test
    public void testAddIfNotPresent_AnnotationNotPresent_ReturnsTrue() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        assertTrue(annotationMap.addIfNotPresent(new TestAnnotation2()));
    }

    @Test
    public void testAdd_AnnotationNotPresent_ReturnsTrue() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.add(new TestAnnotation()));
    }

    @Test
    public void testAdd_AnnotationPresent_ReturnsFalse() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        assertFalse(annotationMap.addIfNotPresent(new TestAnnotation()));
    }

    @Test
    public void testToString_NullAnnotations_ReturnsStringRepresentation() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());
    }

    @Test
    public void testToString_ValidAnnotations_ReturnsStringRepresentation() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        assertEquals(annotations.toString(), annotationMap.toString());
    }

    /*
    /**********************************************************
    /* Helper classes
    /**********************************************************
     */

    private class TestAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return Test.class;
        }
    }

    private class TestAnnotation2 implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return Test.class;
        }
    }
}