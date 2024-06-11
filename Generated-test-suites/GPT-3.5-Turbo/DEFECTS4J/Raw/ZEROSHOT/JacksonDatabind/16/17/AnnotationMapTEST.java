package com.fasterxml.jackson.databind.introspect;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;

public class AnnotationMapTest {

    @Test
    public void testGetWithExistingAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        TestAnnotation testAnnotation = new TestAnnotation();
        annotations.put(testAnnotation.annotationType(), testAnnotation);
        annotationMap._annotations = annotations;

        TestAnnotation result = annotationMap.get(TestAnnotation.class);

        Assert.assertEquals(testAnnotation, result);
    }

    @Test
    public void testGetWithNonExistingAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();

        TestAnnotation result = annotationMap.get(TestAnnotation.class);

        Assert.assertEquals(null, result);
    }

    @Test
    public void testGetWithNullAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();

        TestAnnotation result = annotationMap.get(TestAnnotation.class);

        Assert.assertEquals(null, result);
    }

    @Test
    public void testAnnotationsWithEmptyAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();

        Iterable<Annotation> result = annotationMap.annotations();

        Assert.assertEquals(Arrays.asList(), result);
    }

    @Test
    public void testAnnotationsWithNonNullAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        TestAnnotation testAnnotation = new TestAnnotation();
        annotations.put(testAnnotation.annotationType(), testAnnotation);
        annotationMap._annotations = annotations;

        Iterable<Annotation> result = annotationMap.annotations();

        Assert.assertEquals(Arrays.asList(testAnnotation), result);
    }

    @Test
    public void testMergeWithNullPrimaryAndNullSecondary() {
        AnnotationMap result = AnnotationMap.merge(null, null);

        Assert.assertEquals(null, result);
    }

    @Test
    public void testMergeWithNullPrimaryAndNonEmptySecondary() {
        AnnotationMap secondary = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        TestAnnotation testAnnotation = new TestAnnotation();
        annotations.put(testAnnotation.annotationType(), testAnnotation);
        secondary._annotations = annotations;

        AnnotationMap result = AnnotationMap.merge(null, secondary);

        Assert.assertEquals(secondary, result);
    }

    @Test
    public void testMergeWithEmptyPrimaryAndNullSecondary() {
        AnnotationMap primary = new AnnotationMap();

        AnnotationMap result = AnnotationMap.merge(primary, null);

        Assert.assertEquals(primary, result);
    }

    @Test
    public void testMergeWithEmptyPrimaryAndEmptySecondary() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        Assert.assertEquals(primary, result);
    }

    @Test
    public void testMergeWithNonEmptyPrimaryAndEmptySecondary() {
        AnnotationMap primary = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        TestAnnotation testAnnotation = new TestAnnotation();
        annotations.put(testAnnotation.annotationType(), testAnnotation);
        primary._annotations = annotations;

        AnnotationMap result = AnnotationMap.merge(primary, null);

        Assert.assertEquals(primary, result);
    }

    @Test
    public void testMergeWithNonEmptyPrimaryAndNonEmptySecondary() {
        AnnotationMap primary = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> primaryAnnotations = new HashMap<>();
        TestAnnotation testAnnotation1 = new TestAnnotation();
        primaryAnnotations.put(testAnnotation1.annotationType(), testAnnotation1);
        primary._annotations = primaryAnnotations;

        AnnotationMap secondary = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> secondaryAnnotations = new HashMap<>();
        TestAnnotation testAnnotation2 = new TestAnnotation();
        secondaryAnnotations.put(testAnnotation2.annotationType(), testAnnotation2);
        secondary._annotations = secondaryAnnotations;

        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        HashMap<Class<? extends Annotation>, Annotation> expectedAnnotations = new HashMap<>();
        expectedAnnotations.put(testAnnotation2.annotationType(), testAnnotation2);
        expectedAnnotations.put(testAnnotation1.annotationType(), testAnnotation1);
        AnnotationMap expected = new AnnotationMap(expectedAnnotations);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSizeWithNullAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();

        int result = annotationMap.size();

        Assert.assertEquals(0, result);
    }

    @Test
    public void testSizeWithEmptyAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotationMap._annotations = annotations;

        int result = annotationMap.size();

        Assert.assertEquals(0, result);
    }

    @Test
    public void testSizeWithNonEmptyAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        TestAnnotation testAnnotation = new TestAnnotation();
        annotations.put(testAnnotation.annotationType(), testAnnotation);
        annotationMap._annotations = annotations;

        int result = annotationMap.size();

        Assert.assertEquals(1, result);
    }

    @Test
    public void testAddIfNotPresentWithNullAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();

        boolean result = annotationMap.addIfNotPresent(testAnnotation);

        Assert.assertTrue(result);
        Assert.assertEquals(1, annotationMap.size());
        Assert.assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testAddIfNotPresentWithEmptyAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotationMap._annotations = annotations;
        TestAnnotation testAnnotation = new TestAnnotation();

        boolean result = annotationMap.addIfNotPresent(testAnnotation);

        Assert.assertTrue(result);
        Assert.assertEquals(1, annotationMap.size());
        Assert.assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testAddIfNotPresentWithExistingAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        TestAnnotation testAnnotation = new TestAnnotation();
        annotations.put(testAnnotation.annotationType(), testAnnotation);
        annotationMap._annotations = annotations;

        boolean result = annotationMap.addIfNotPresent(testAnnotation);

        Assert.assertFalse(result);
        Assert.assertEquals(1, annotationMap.size());
        Assert.assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testAddWithNullAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();

        boolean result = annotationMap.add(testAnnotation);

        Assert.assertTrue(result);
        Assert.assertEquals(1, annotationMap.size());
        Assert.assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testAddWithEmptyAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotationMap._annotations = annotations;
        TestAnnotation testAnnotation = new TestAnnotation();

        boolean result = annotationMap.add(testAnnotation);

        Assert.assertTrue(result);
        Assert.assertEquals(1, annotationMap.size());
        Assert.assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testAddWithExistingAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        TestAnnotation testAnnotation = new TestAnnotation();
        annotations.put(testAnnotation.annotationType(), testAnnotation);
        annotationMap._annotations = annotations;

        boolean result = annotationMap.add(testAnnotation);

        Assert.assertTrue(result);
        Assert.assertEquals(1, annotationMap.size());
        Assert.assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testToStringWithNullAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();

        String result = annotationMap.toString();

        Assert.assertEquals("[null]", result);
    }

    @Test
    public void testToStringWithEmptyAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotationMap._annotations = annotations;

        String result = annotationMap.toString();

        Assert.assertEquals("{}", result);
    }

    @Test
    public void testToStringWithNonEmptyAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        TestAnnotation testAnnotation = new TestAnnotation();
        annotations.put(testAnnotation.annotationType(), testAnnotation);
        annotationMap._annotations = annotations;

        String result = annotationMap.toString();

        Assert.assertEquals("{interface com.fasterxml.jackson.databind.introspect.TestAnnotation=@com.fasterxml.jackson.databind.introspect.TestAnnotation()}", result);
    }

    private static class TestAnnotation implements Annotation {

        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation.class;
        }
    }
}