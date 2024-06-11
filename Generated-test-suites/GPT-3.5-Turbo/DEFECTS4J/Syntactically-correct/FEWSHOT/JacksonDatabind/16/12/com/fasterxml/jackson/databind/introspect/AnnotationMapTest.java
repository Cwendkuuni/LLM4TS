package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class AnnotationMapTest {
    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGetShouldReturnNullIfAnnotationMapIsEmpty() {
        assertNull(annotationMap.get(Annotation.class));
    }

    @Test
    public void testGetShouldReturnNullIfAnnotationMapDoesNotContainRequestedAnnotation() {
        annotationMap._annotations = new HashMap<>();
        assertNull(annotationMap.get(Annotation.class));
    }

    @Test
    public void testGetShouldReturnRequestedAnnotationIfPresentInAnnotationMap() {
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        annotationMap._annotations = new HashMap<>();
        annotationMap._annotations.put(Annotation.class, annotation);
        assertEquals(annotation, annotationMap.get(Annotation.class));
    }

    @Test
    public void testAnnotationsShouldReturnEmptyCollectionIfAnnotationMapIsEmpty() {
        assertTrue(Collections.emptyList().equals(annotationMap.annotations()));
    }

    @Test
    public void testAnnotationsShouldReturnCollectionOfAnnotationsIfAnnotationMapIsNotEmpty() {
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        annotationMap._annotations = new HashMap<>();
        annotationMap._annotations.put(Annotation.class, annotation);
        assertTrue(Collections.singleton(annotation).equals(annotationMap.annotations()));
    }

    @Test
    public void testMergeShouldReturnNullIfBothAnnotationMapsAreNull() {
        assertNull(AnnotationMap.merge(null, null));
    }

    @Test
    public void testMergeShouldReturnPrimaryAnnotationMapIfSecondaryAnnotationMapIsNull() {
        AnnotationMap primary = new AnnotationMap();
        primary._annotations = new HashMap<>();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, null);
        assertEquals(primary, mergedAnnotationMap);
    }

    @Test
    public void testMergeShouldReturnSecondaryAnnotationMapIfPrimaryAnnotationMapIsNull() {
        AnnotationMap secondary = new AnnotationMap();
        secondary._annotations = new HashMap<>();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(null, secondary);
        assertEquals(secondary, mergedAnnotationMap);
    }

    @Test
    public void testMergeShouldReturnPrimaryAnnotationMapIfSecondaryAnnotationMapIsEmpty() {
        AnnotationMap primary = new AnnotationMap();
        primary._annotations = new HashMap<>();
        AnnotationMap secondary = new AnnotationMap();
        secondary._annotations = new HashMap<>();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, mergedAnnotationMap);
    }

    @Test
    public void testMergeShouldReturnSecondaryAnnotationMapIfPrimaryAnnotationMapIsEmpty() {
        AnnotationMap primary = new AnnotationMap();
        primary._annotations = new HashMap<>();
        AnnotationMap secondary = new AnnotationMap();
        secondary._annotations = new HashMap<>();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(secondary, primary);
        assertEquals(secondary, mergedAnnotationMap);
    }

    // Add more test cases for the remaining methods
}