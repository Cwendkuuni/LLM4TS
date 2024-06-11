
package com.fasterxml.jackson.databind.introspect;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.HashMap;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap._annotations = new HashMap<>();

        // Test when no annotations exist
        Assert.assertNull(annotationMap.get(TestAnnotation.class));

        // Test when annotation exists
        TestAnnotation testAnnotation = new TestAnnotation();
        annotationMap._annotations.put(TestAnnotation.class, testAnnotation);
        Assert.assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));

    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap._annotations = new HashMap<>();

        // Test when no annotations exist
        Assert.assertTrue(annotationMap.annotations().isEmpty());

        // Test when annotations exist
        TestAnnotation testAnnotation = new TestAnnotation();
        annotationMap._annotations.put(TestAnnotation.class, testAnnotation);
        Assert.assertEquals(1, annotationMap.annotations().size());
        Assert.assertEquals(testAnnotation, annotationMap.annotations().iterator().next());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        primary._annotations = new HashMap<>();
        TestAnnotation primaryAnnotation = new TestAnnotation();
        primary._annotations.put(TestAnnotation.class, primaryAnnotation);

        AnnotationMap secondary = new AnnotationMap();
        secondary._annotations = new HashMap<>();
        TestAnnotation secondaryAnnotation = new TestAnnotation();
        secondary._annotations.put(TestAnnotation.class, secondaryAnnotation);

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);

        Assert.assertEquals(2, merged.size());
        Assert.assertEquals(primaryAnnotation, merged.get(TestAnnotation.class));
        Assert.assertEquals(secondaryAnnotation, merged.get(TestAnnotation.class));

    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();

        // Test when annotations is null
        Assert.assertEquals(0, annotationMap.size());

        // Test when annotations size is 1
        annotationMap._annotations = new HashMap<>();
        annotationMap._annotations.put(TestAnnotation.class, new TestAnnotation());
        Assert.assertEquals(1, annotationMap.size());

        // Test when annotations size is 5
        annotationMap._annotations.put(TestAnnotation2.class, new TestAnnotation2());
        annotationMap._annotations.put(TestAnnotation3.class, new TestAnnotation3());
        annotationMap._annotations.put(TestAnnotation4.class, new TestAnnotation4());
        annotationMap._annotations.put(TestAnnotation5.class, new TestAnnotation5());
        Assert.assertEquals(5, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap._annotations = new HashMap<>();

        // Test when annotation does not exist
        TestAnnotation testAnnotation = new TestAnnotation();
        Assert.assertTrue(annotationMap.addIfNotPresent(testAnnotation));
        Assert.assertEquals(testAnnotation, annotationMap._annotations.get(TestAnnotation.class));

        // Test when annotation already exists
        Assert.assertFalse(annotationMap.addIfNotPresent(testAnnotation));
        Assert.assertEquals(testAnnotation, annotationMap._annotations.get(TestAnnotation.class));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap._annotations = new HashMap<>();

        // Test when annotation does not exist
        TestAnnotation testAnnotation = new TestAnnotation();
        Assert.assertTrue(annotationMap.add(testAnnotation));
        Assert.assertEquals(testAnnotation, annotationMap._annotations.get(TestAnnotation.class));

        // Test when annotation already exists
        Assert.assertFalse(annotationMap.add(testAnnotation));
        Assert.assertEquals(testAnnotation, annotationMap._annotations.get(TestAnnotation.class));
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

    private static class TestAnnotation3 implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation3.class;
        }
    }

    private static class TestAnnotation4 implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation4.class;
        }
    }

    private static class TestAnnotation5 implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation5.class;
        }
    }

}
