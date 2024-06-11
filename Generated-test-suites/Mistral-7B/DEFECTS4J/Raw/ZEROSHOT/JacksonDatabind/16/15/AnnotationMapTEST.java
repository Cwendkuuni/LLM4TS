package com.fasterxml.jackson.databind.introspect.test;

import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AnnotationMapTest {

    @Test
    public void testAnnotationMapEmpty() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals(0, annotationMap.size());
        Assert.assertNull(annotationMap.get(NullAnnotation.class));
        Assert.assertNull(annotationMap.get(ValidAnnotation.class));
        Assert.assertNull(annotationMap.get(InvalidAnnotation.class));
        Assert.assertNull(annotationMap.get(MergeAnnotation.class));
        Assert.assertNull(annotationMap.get(CustomAnnotation.class));

        Assert.assertEquals(0, annotationMap.annotations().size());
    }

    @Test
    public void testAnnotationMapGet() {
        AnnotationMap annotationMap = new AnnotationMap();

        annotationMap.add(new NullAnnotation());
        Assert.assertEquals(1, annotationMap.size());
        Assert.assertNull(annotationMap.get(ValidAnnotation.class));
        Assert.assertNull(annotationMap.get(InvalidAnnotation.class));
        Assert.assertNull(annotationMap.get(MergeAnnotation.class));
        Assert.assertNull(annotationMap.get(CustomAnnotation.class));
        Assert.assertNull(annotationMap.get(NullAnnotation.class));

        Annotation nullAnnotation = annotationMap.get(NullAnnotation.class);
        Assert.assertNotNull(nullAnnotation);
        Assert.assertEquals(NullAnnotation.class, nullAnnotation.annotationType());

        annotationMap.add(new ValidAnnotation());
        Assert.assertEquals(2, annotationMap.size());
        Assert.assertNotNull(annotationMap.get(NullAnnotation.class));
        Assert.assertNotNull(annotationMap.get(ValidAnnotation.class));
        Assert.assertNull(annotationMap.get(InvalidAnnotation.class));
        Assert.assertNull(annotationMap.get(MergeAnnotation.class));
        Assert.assertNull(annotationMap.get(CustomAnnotation.class));

        Annotation validAnnotation = annotationMap.get(ValidAnnotation.class);
        Assert.assertNotNull(validAnnotation);
        Assert.assertEquals(ValidAnnotation.class, validAnnotation.annotationType());

        annotationMap.add(new InvalidAnnotation());
        Assert.assertEquals(3, annotationMap.size());
        Assert.assertNotNull(annotationMap.get(NullAnnotation.class));
        Assert.assertNotNull(annotationMap.get(ValidAnnotation.class));
        Assert.assertNotNull(annotationMap.get(InvalidAnnotation.class));
        Assert.assertNull(annotationMap.get(MergeAnnotation.class));
        Assert.assertNull(annotationMap.get(CustomAnnotation.class));

        annotationMap.add(new MergeAnnotation());
        Assert.assertEquals(4, annotationMap.size());
        Assert.assertNotNull(annotationMap.get(NullAnnotation.class));
        Assert.assertNotNull(annotationMap.get(ValidAnnotation.class));
        Assert.assertNotNull(annotationMap.get(InvalidAnnotation.class));
        Assert.assertNotNull(annotationMap.get(MergeAnnotation.class));
        Assert.assertNull(annotationMap.get(CustomAnnotation.class));

        Annotation mergeAnnotation = annotationMap.get(MergeAnnotation.class);
        Assert.assertNotNull(mergeAnnotation);
        Assert.assertEquals(MergeAnnotation.class, mergeAnnotation.annotationType());

        annotationMap.add(new CustomAnnotation());
        Assert.assertEquals(5, annotationMap.size());
        Assert.assertNotNull(annotationMap.get(NullAnnotation.class));
        Assert.assertNotNull(annotationMap.get(ValidAnnotation.class));
        Assert.assertNotNull(annotationMap.get(InvalidAnnotation.class));
        Assert.assertNotNull(annotationMap.get(MergeAnnotation.class));
        Assert.assertNotNull(annotationMap.get(CustomAnnotation.class));
    }

    @Test
    public void testAnnotationMapMerge() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new NullAnnotation());
        primary.add(new ValidAnnotation());
        primary.add(new MergeAnnotation());

        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new InvalidAnnotation());
        secondary.add(new MergeAnnotation());
        secondary.add(new CustomAnnotation());

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);

        Assert.assertEquals(5, mergedAnnotationMap.size());
        Set<Class<? extends Annotation>> expectedAnnotationTypes = new HashSet<>(Arrays.asList(
                NullAnnotation.class,
                ValidAnnotation.class,
                MergeAnnotation.class,
                InvalidAnnotation.class,
                CustomAnnotation.class
        ));
        for (Annotation annotation : mergedAnnotationMap.annotations()) {
            Assert.assertTrue(expectedAnnotationTypes.contains(annotation.annotationType()));
        }
    }

    @Test
    public void testAnnotationMapSize() {
        AnnotationMap annotationMap = new AnnotationMap();

        Assert.assertEquals(0, annotationMap.size());

        annotationMap.add(new NullAnnotation());
        Assert.assertEquals(1, annotationMap.size());

        annotationMap.add(new ValidAnnotation());
        Assert.assertEquals(2, annotationMap.size());

        annotationMap.add(new InvalidAnnotation());
        Assert.assertEquals(3, annotationMap.size());

        annotationMap.add(new MergeAnnotation());
        Assert.assertEquals(4, annotationMap.size());

        annotationMap.add(new CustomAnnotation());
        Assert.assertEquals(5, annotationMap.size());
    }

    @Test
    public void testAnnotationMapToString() {
        AnnotationMap annotationMap = new AnnotationMap();

        annotationMap.add(new NullAnnotation());
        annotationMap.add(new ValidAnnotation());
        annotationMap.add(new InvalidAnnotation());
        annotationMap.add(new MergeAnnotation());
        annotationMap.add(new CustomAnnotation());

        String toString = annotationMap.toString();
        Assert.assertTrue(toString.contains("AnnotationMap@"));
        Assert.assertTrue(toString.contains("{"));
        Assert.assertTrue(toString.contains("NullAnnotation"));
        Assert.assertTrue(toString.contains("ValidAnnotation"));
        Assert.assertTrue(toString.contains("InvalidAnnotation"));
        Assert.assertTrue(toString.contains("MergeAnnotation"));
        Assert.assertTrue(toString.contains("CustomAnnotation"));
        Assert.assertTrue(toString.contains("}"));
    }

    @Test
    public void testAnnotationMapAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();

        Assert.assertTrue(annotationMap.addIfNotPresent(new NullAnnotation()));
        Assert.assertEquals(1, annotationMap.size());

        Assert.assertFalse(annotationMap.addIfNotPresent(new NullAnnotation()));
        Assert.assertEquals(1, annotationMap.size());

        Assert.assertTrue(annotationMap.addIfNotPresent(new ValidAnnotation()));
        Assert.assertEquals(2, annotationMap.size());

        Assert.assertFalse(annotationMap.addIfNotPresent(new ValidAnnotation()));
        Assert.assertEquals(2, annotationMap.size());

        Assert.assertTrue(annotationMap.addIfNotPresent(new InvalidAnnotation()));
        Assert.assertEquals(3, annotationMap.size());

        Assert.assertFalse(annotationMap.addIfNotPresent(new InvalidAnnotation()));
        Assert.assertEquals(3, annotationMap.size());

        Assert.assertTrue(annotationMap.addIfNotPresent(new MergeAnnotation()));
        Assert.assertEquals(4, annotationMap.size());

        Assert.assertFalse(annotationMap.addIfNotPresent(new MergeAnnotation()));
        Assert.assertEquals(4, annotationMap.size());

        Assert.assertTrue(annotationMap.addIfNotPresent(new CustomAnnotation()));
        Assert.assertEquals(5, annotationMap.size());

        Assert.assertFalse(annotationMap.addIfNotPresent(new CustomAnnotation()));
        Assert.assertEquals(5, annotationMap.size());
    }

    @Test
    public void testAnnotationMapAdd() {
        AnnotationMap annotationMap = new AnnotationMap();

        Assert.assertTrue(annotationMap.add(new NullAnnotation()));
        Assert.assertEquals(1, annotationMap.size());

        Assert.assertFalse(annotationMap.add(new NullAnnotation()));
        Assert.assertEquals(1, annotationMap.size());

        Assert.assertTrue(annotationMap.add(new ValidAnnotation()));
        Assert.assertEquals(2, annotationMap.size());

        Assert.assertFalse(annotationMap.add(new ValidAnnotation()));
        Assert.assertEquals(2, annotationMap.size());

        Assert.assertTrue(annotationMap.add(new InvalidAnnotation()));
        Assert.assertEquals(3, annotationMap.size());

        Assert.assertFalse(annotationMap.add(new InvalidAnnotation()));
        Assert.assertEquals(3, annotationMap.size());

        Assert.assertTrue(annotationMap.add(new MergeAnnotation()));
        Assert.assertEquals(4, annotationMap.size());

        Assert.assertFalse(annotationMap.add(new MergeAnnotation()));
        Assert.assertEquals(4, annotationMap.size());

        Assert.assertTrue(annotationMap.add(new CustomAnnotation()));
        Assert.assertEquals(5, annotationMap.size());

        Assert.assertFalse(annotationMap.add(new CustomAnnotation()));
        Assert.assertEquals(5, annotationMap.size());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored") // Suppressing Sonarqube warning because we don't care about null pointer exception here
    @Test(expected = NullPointerException.class)
    public void testAnnotationMapNullAnnotationType() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.addIfNotPresent(null);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored") // Suppressing Sonarqube warning because we don't care about null pointer exception here
    @Test(expected = NullPointerException.class)
    public void testAnnotationMapNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(null);
    }

    private static class NullAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface ValidAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface InvalidAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface MergeAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface CustomAnnotation {
    }
}