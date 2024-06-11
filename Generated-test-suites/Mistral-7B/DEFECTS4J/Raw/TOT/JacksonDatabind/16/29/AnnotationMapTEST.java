package com.fasterxml.jackson.databind.introspect.test;

import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;
    private AnnotationMap mergedAnnotationMap;

    @Before
    public void setup() {
        // Initialize annotationMap with some annotations
        Class<MyAnnotation1> myAnnotation1 = MyAnnotation1.class;
        Class<MyAnnotation2> myAnnotation2 = MyAnnotation2.class;
        annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation1());
        annotationMap.add(new MyAnnotation2());

        // Initialize mergedAnnotationMap with some additional annotations
        Map<Class<? extends Annotation>, Annotation> mergedAnnotationMapMap = new HashMap<>();
        mergedAnnotationMapMap.putAll(annotationMap._annotations);
        mergedAnnotationMapMap.put(MyAnnotation3.class, new MyAnnotation3());
        mergedAnnotationMap = new AnnotationMap(mergedAnnotationMapMap);
    }

    @Test
    public void testGetAnnotation() {
        MyAnnotation1 myAnnotation1 = annotationMap.get(MyAnnotation1.class);
        assertNotNull(myAnnotation1);
        assertEquals(myAnnotation1.getClass(), MyAnnotation1.class);
    }

    @Test
    public void testGetAnnotationNull() {
        assertNull(annotationMap.get(MyAnnotation3.class));
    }

    @Test
    public void testAnnotations() {
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        assertEquals(annotations.size(), 2);
        assertTrue(annotations.contains(annotationMap.get(MyAnnotation1.class)));
        assertTrue(annotations.contains(annotationMap.get(MyAnnotation2.class)));
    }

    @Test
    public void testMerge() {
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, mergedAnnotationMap);
        List<Annotation> mergedAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
        assertEquals(mergedAnnotations.size(), 3);
        assertTrue(mergedAnnotations.contains(annotationMap.get(MyAnnotation1.class)));
        assertTrue(mergedAnnotations.contains(annotationMap.get(MyAnnotation2.class)));
        assertTrue(mergedAnnotations.contains(mergedAnnotationMap.get(MyAnnotation3.class)));
    }

    @Test
    public void testSize() {
        assertEquals(annotationMap.size(), 2);
        assertEquals(mergedAnnotationMap.size(), 3);
    }

    @Test
    public void testAddIfNotPresent() {
        // Add an existing annotation, should not change the map
        MyAnnotation1 myAnnotation1 = new MyAnnotation1();
        assertFalse(annotationMap.addIfNotPresent(myAnnotation1));

        // Add a new annotation, should change the map
        MyAnnotation3 myAnnotation3 = new MyAnnotation3();
        assertTrue(annotationMap.addIfNotPresent(myAnnotation3));
        assertEquals(annotationMap.size(), 3);
    }

    @Test
    public void testAdd() {
        // Add an existing annotation, should not change the map
        MyAnnotation1 myAnnotation1 = new MyAnnotation1();
        assertFalse(annotationMap.add(myAnnotation1));

        // Add a new annotation, should change the map
        MyAnnotation3 myAnnotation3 = new MyAnnotation3();
        assertTrue(annotationMap.add(myAnnotation3));
        assertEquals(annotationMap.size(), 3);
    }
}