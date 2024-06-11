package com.example.junit; // replace with your package name

import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AnnotationMap.class)
public class AnnotationMapTest {

    @Test
    public void testConstructor() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Basic constructor test
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = PowerMockito.mock(Annotation.class);
        Class<Annotation> annotationType = Annotation.class;
        annotationMap.add(annotation);
        // Positive test case: get existing annotation
        Assert.assertEquals(annotation, annotationMap.get(annotationType));

        // Negative test case: get non-existing annotation
        Assert.assertNull(annotationMap.get(String.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation1 = PowerMockito.mock(Annotation.class);
        Annotation annotation2 = PowerMockito.mock(Annotation.class);
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);

        // Test size and iteration
        Assert.assertEquals(2, annotationMap.size());
        Set<Annotation> annotations = new HashSet<>();
        for (Annotation annotation : annotationMap.annotations()) {
            annotations.add(annotation);
        }
        Assert.assertEquals(annotations, annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        Annotation annotation1 = PowerMockito.mock(Annotation.class);
        Annotation annotation2 = PowerMockito.mock(Annotation.class);
        primaryAnnotationMap.add(annotation1);
        secondaryAnnotationMap.add(annotation2);

        // Test merging empty maps
        Assert.assertEquals(secondaryAnnotationMap, AnnotationMap.merge(null, secondaryAnnotationMap));
        Assert.assertEquals(primaryAnnotationMap, AnnotationMap.merge(primaryAnnotationMap, null));

        // Test merging maps with no common annotations
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        Assert.assertNotSame(primaryAnnotationMap, mergedAnnotationMap);
        Assert.assertNotSame(secondaryAnnotationMap, mergedAnnotationMap);
        Assert.assertEquals(primaryAnnotationMap.size(), mergedAnnotationMap.size());
        Assert.assertEquals(primaryAnnotationMap.annotations(), mergedAnnotationMap.annotations());

        // Test merging maps with common annotations
        primaryAnnotationMap.add(annotation2);
        mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        Assert.assertNotSame(primaryAnnotationMap, mergedAnnotationMap);
        Assert.assertNotSame(secondaryAnnotationMap, mergedAnnotationMap);
        Assert.assertEquals(primaryAnnotationMap.size(), mergedAnnotationMap.size());
        Iterator<Annotation> mergedAnnotationsIterator = mergedAnnotationMap.annotations().iterator();
        Iterator<Annotation> primaryAnnotationsIterator = primaryAnnotationMap.annotations().iterator();
        while (mergedAnnotationsIterator.hasNext()) {
            Annotation mergedAnnotation = mergedAnnotationsIterator.next();
            Annotation primaryAnnotation = primaryAnnotationsIterator.next();
            Assert.assertEquals(mergedAnnotation, primaryAnnotation);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testSizeWhenMapIsNull() {
        // Test size when map is null
        AnnotationMap annotationMap = null;
        int size = annotationMap.size();
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation1 = PowerMockito.mock(Annotation.class);
        Annotation annotation2 = PowerMockito.mock(Annotation.class);

        // Test adding existing annotation
        annotationMap.add(annotation1);
        Assert.assertFalse(annotationMap.addIfNotPresent(annotation1));

        // Test adding non-existing annotation
        Assert.assertTrue(annotationMap.addIfNotPresent(annotation2));

        // Test adding existing annotation again
        Assert.assertFalse(annotationMap.addIfNotPresent(annotation1));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation1 = PowerMockito.mock(Annotation.class);
        Annotation annotation2 = PowerMockito.mock(Annotation.class);

        // Test adding existing annotation
        annotationMap.add(annotation1);
        Assert.assertFalse(annotationMap.add(annotation1));

        // Test adding non-existing annotation
        Assert.assertTrue(annotationMap.add(annotation2));

        // Test adding existing annotation again
        Assert.assertFalse(annotationMap.add(annotation1));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation1 = PowerMockito.mock(Annotation.class);
        Annotation annotation2 = PowerMockito.mock(Annotation.class);
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);

        // Test toString when map is empty
        Assert.assertEquals("[null]", new AnnotationMap().toString());

        // Test toString with annotations
        Assert.assertTrue(annotationMap.toString().contains(annotation1.toString()));
        Assert.assertTrue(annotationMap.toString().contains(annotation2.toString()));
    }

    // Add more test cases as needed for edge cases and exception handling
}