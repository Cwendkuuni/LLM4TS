package com.fasterxml.jackson.databind.introspect;

import java.lang.annotation.Annotation;
import java.util.*;

import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.*;
import static org.junit.Assert.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();

        // Test when annotations is null
        assertNull(annotationMap.get(TestAnnotation.class));

        // Test when annotations is empty
        annotationMap._annotations = new HashMap<>();
        assertNull(annotationMap.get(TestAnnotation.class));

        // Test when annotations contains the annotation of specified class
        TestAnnotation testAnnotation = new TestAnnotation();
        annotationMap._annotations = new HashMap<>();
        annotationMap._annotations.put(TestAnnotation.class, testAnnotation);
        assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));

        // Test when annotations doesn't contain the annotation of specified class
        annotationMap._annotations = new HashMap<>();
        assertNull(annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();

        // Test when annotations is null
        assertEquals(Collections.emptyList(), annotationMap.annotations());

        // Test when annotations is empty
        annotationMap._annotations = new HashMap<>();
        assertEquals(Collections.emptyList(), annotationMap.annotations());

        // Test when annotations is not empty
        TestAnnotation testAnnotation = new TestAnnotation();
        annotationMap._annotations = new HashMap<>();
        annotationMap._annotations.put(TestAnnotation.class, testAnnotation);
        assertEquals(Collections.singletonList(testAnnotation), annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        // Test when primary is null, secondary is null
        assertNull(AnnotationMap.merge(null, null));

        // Test when primary is null, secondary is empty
        AnnotationMap expectedResult = new AnnotationMap();
        AnnotationMap actualResult = AnnotationMap.merge(null, secondary);
        assertEquals(expectedResult._annotations, actualResult._annotations);

        // Test when primary is null, secondary is not empty
        AnnotationMap secondaryAnnotations = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        secondaryAnnotations._annotations = new HashMap<>();
        secondaryAnnotations._annotations.put(TestAnnotation.class, testAnnotation);

        expectedResult = new AnnotationMap();
        expectedResult._annotations = new HashMap<>();
        expectedResult._annotations.put(TestAnnotation.class, testAnnotation);
        actualResult = AnnotationMap.merge(null, secondaryAnnotations);
        assertEquals(expectedResult._annotations, actualResult._annotations);

        // Test when primary is empty, secondary is null
        expectedResult = new AnnotationMap();
        actualResult = AnnotationMap.merge(primary, null);
        assertEquals(expectedResult._annotations, actualResult._annotations);

        // Test when primary is empty, secondary is empty
        expectedResult = new AnnotationMap();
        actualResult = AnnotationMap.merge(primary, secondary);
        assertEquals(expectedResult._annotations, actualResult._annotations);

        // Test when primary is empty, secondary is not empty
        secondaryAnnotations._annotations = new HashMap<>();
        secondaryAnnotations._annotations.put(TestAnnotation.class, testAnnotation);

        expectedResult = new AnnotationMap();
        expectedResult._annotations = new HashMap<>();
        expectedResult._annotations.put(TestAnnotation.class, testAnnotation);
        actualResult = AnnotationMap.merge(primary, secondaryAnnotations);
        assertEquals(expectedResult._annotations, actualResult._annotations);

        // Test when primary is not empty, secondary is null
        TestAnnotation primaryTestAnnotation = new TestAnnotation();
        primary._annotations = new HashMap<>();
        primary._annotations.put(TestAnnotation.class, primaryTestAnnotation);

        expectedResult = new AnnotationMap();
        expectedResult._annotations = new HashMap<>();
        expectedResult._annotations.put(TestAnnotation.class, primaryTestAnnotation);
        actualResult = AnnotationMap.merge(primary, null);
        assertEquals(expectedResult._annotations, actualResult._annotations);

        // Test when primary is not empty, secondary is empty
        expectedResult = new AnnotationMap();
        expectedResult._annotations = new HashMap<>();
        expectedResult._annotations.put(TestAnnotation.class, primaryTestAnnotation);
        actualResult = AnnotationMap.merge(primary, secondary);
        assertEquals(expectedResult._annotations, actualResult._annotations);

        // Test when primary is not empty, secondary is not empty
        secondaryAnnotations._annotations = new HashMap<>();
        TestAnnotation secondaryTestAnnotation = new TestAnnotation();
        secondaryAnnotations._annotations.put(TestAnnotation.class, secondaryTestAnnotation);

        expectedResult = new AnnotationMap();
        expectedResult._annotations = new HashMap<>();
        expectedResult._annotations.put(TestAnnotation.class, secondaryTestAnnotation);
        actualResult = AnnotationMap.merge(primary, secondaryAnnotations);
        assertEquals(expectedResult._annotations, actualResult._annotations);
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();

        // Test when annotations is null
        assertEquals(0, annotationMap.size());

        // Test when annotations is not null
        annotationMap._annotations = new HashMap<>();
        assertEquals(0, annotationMap.size());

        TestAnnotation testAnnotation = new TestAnnotation();
        annotationMap._annotations.put(TestAnnotation.class, testAnnotation);
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();

        // Test when annotations is null and annotation is not present
        TestAnnotation testAnnotation = new TestAnnotation();
        assertTrue(annotationMap.addIfNotPresent(testAnnotation));
        assertEquals(testAnnotation, annotationMap._annotations.get(TestAnnotation.class));

        // Test when annotations is null and annotation is present
        assertFalse(annotationMap.addIfNotPresent(testAnnotation));
        assertEquals(testAnnotation, annotationMap._annotations.get(TestAnnotation.class));

        // Test when annotations is not null and annotation is not present
        annotationMap._annotations = new HashMap<>();
        assertTrue(annotationMap.addIfNotPresent(testAnnotation));
        assertEquals(testAnnotation, annotationMap._annotations.get(TestAnnotation.class));

        // Test when annotations is not null and annotation is present
        assertFalse(annotationMap.addIfNotPresent(testAnnotation));
        assertEquals(testAnnotation, annotationMap._annotations.get(TestAnnotation.class));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();

        // Test when annotations is null
        TestAnnotation testAnnotation = new TestAnnotation();
        assertTrue(annotationMap.add(testAnnotation));
        assertEquals(testAnnotation, annotationMap._annotations.get(TestAnnotation.class));

        // Test when annotations is not null
        assertFalse(annotationMap.add(testAnnotation));
        assertEquals(testAnnotation, annotationMap._annotations.get(TestAnnotation.class));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();

        // Test when annotations is null
        assertEquals("[null]", annotationMap.toString());

        // Test when annotations is empty
        annotationMap._annotations = new HashMap<>();
        assertEquals("{}", annotationMap.toString());

        // Test when annotations is not empty
        TestAnnotation testAnnotation = new TestAnnotation();
        annotationMap._annotations = new HashMap<>();
        annotationMap._annotations.put(TestAnnotation.class, testAnnotation);
        assertEquals("{class com.fasterxml.jackson.databind.introspect.TestAnnotation=" + testAnnotation + "}", annotationMap.toString());
    }

    private static class TestAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation.class;
        }
    }
}