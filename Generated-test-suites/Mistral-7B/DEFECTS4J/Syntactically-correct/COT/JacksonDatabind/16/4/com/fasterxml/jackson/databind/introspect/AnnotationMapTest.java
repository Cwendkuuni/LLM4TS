package com.example.junit; // replace with your package name

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.introspect.AnnotationMap;

/**
 * Test class for the AnnotationMap class.
 */
public class AnnotationMapTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        AnnotationMap am = new AnnotationMap();
        assertNull(am.get(null));
    }

    /**
     * Test get method.
     */
    @Test
    public void testGet() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation ann = new MyAnnotation();
        am.add(ann);
        assertEquals(ann, am.get(MyAnnotation.class));
    }

    /**
     * Test annotations method.
     */
    @Test
    public void testAnnotations() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation ann1 = new MyAnnotation();
        MyAnnotation ann2 = new MyAnnotation();
        am.add(ann1);
        am.add(ann2);
        List<Annotation> annotations = new ArrayList<>();
        annotations.add(ann1);
        annotations.add(ann2);
        assertEquals(annotations, am.annotations());
    }

    /**
     * Test merge method.
     */
    @Test
    public void testMerge() {
        AnnotationMap am1 = new AnnotationMap();
        MyAnnotation ann1 = new MyAnnotation();
        MyAnnotation ann2 = new MyAnnotation();
        AnnotationMap am2 = new AnnotationMap();
        MyAnnotation ann3 = new MyAnnotation();
        am1.add(ann1);
        am1.add(ann2);
        am2.add(ann3);
        AnnotationMap merged = AnnotationMap.merge(am1, am2);
        List<Annotation> expected = new ArrayList<>();
        expected.add(ann1);
        expected.add(ann2);
        expected.add(ann3);
        assertEquals(expected, merged.annotations());
    }

    /**
     * Test size method.
     */
    @Test
    public void testSize() {
        AnnotationMap am = new AnnotationMap();
        assertEquals(0, am.size());
        MyAnnotation ann = new MyAnnotation();
        am.add(ann);
        assertEquals(1, am.size());
    }

    /**
     * Test addIfNotPresent method.
     */
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation ann1 = new MyAnnotation();
        MyAnnotation ann2 = new MyAnnotation();
        assertTrue(am.addIfNotPresent(ann1));
        assertFalse(am.addIfNotPresent(ann1));
        assertTrue(am.addIfNotPresent(ann2));
    }

    /**
     * Test add method.
     */
    @Test
    public void testAdd() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation ann1 = new MyAnnotation();
        MyAnnotation ann2 = new MyAnnotation();
        assertTrue(am.add(ann1));
        assertFalse(am.add(ann1));
        assertTrue(am.add(ann2));
    }

    /**
     * Test toString method.
     */
    @Test
    public void testToString() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation ann1 = new MyAnnotation();
        MyAnnotation ann2 = new MyAnnotation();
        am.add(ann1);
        am.add(ann2);
        String expected = "{" +
                "\"class com.example.junit.MyAnnotation\":[{" +
                "\"annotationType\":\"com.example.junit.MyAnnotation\", " +
                "\"annotationClass\":class [Ljava.lang.reflect.AnnotatedElement;," +
                "\"value\":null}]," +
                "\"class com.example.junit.MyAnnotation\":[{" +
                "\"annotationType\":\"com.example.junit.MyAnnotation\", " +
                "\"annotationClass\":class [Ljava.lang.reflect.AnnotatedElement;," +
                "\"value\":null}]" +
                "}";
        assertEquals(expected, am.toString());
    }

    /**
     * Test exception handling when adding annotation of a different type.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddDifferentAnnotationType() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation ann = new MyAnnotation();
        NonMyAnnotation nann = new NonMyAnnotation();
        am.add(ann);
        am.add(nann);
    }

    /**
     * Test empty HashMap creation in private constructor.
     */
    @Test
    public void testEmptyHashMapConstruction() {
        AnnotationMap am = new AnnotationMap((HashMap<Class<? extends Annotation>, Annotation>) Collections.emptyMap());
        assertTrue(am._annotations.isEmpty());
    }

    /**
     * Test null HashMap creation in private constructor.
     */
    @Test(expected = NullPointerException.class)
    public void testNullHashMapConstruction() {
        AnnotationMap am = new AnnotationMap(null);
    }

    /**
     * Test size when null HashMap.
     */
    @Test
    public void testSizeNullHashMap() {
        AnnotationMap am = new AnnotationMap(null);
        assertEquals(0, am.size());
    }

    /**
     * Test get when null HashMap.
     */
    @Test
    public void testGetNullHashMap() {
        AnnotationMap am = new AnnotationMap(null);
        assertNull(am.get(null));
    }

    /**
     * Test annotations when null HashMap.
     */
    @Test
    public void testAnnotationsNullHashMap() {
        AnnotationMap am = new AnnotationMap(null);
        assertEquals(Collections.emptyList(), am.annotations());
    }

    /**
     * Test merge when null primary or secondary AnnotationMaps.
     */
    @Test
    public void testMergeNullMaps() {
        AnnotationMap primary = null;
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(secondary, merged);
    }

    /**
     * Test merge when null secondary AnnotationMap.
     */
    @Test
    public void testMergeNullSecondaryMap() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = null;
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, merged);
    }

    /**
     * Test merge when both primary and secondary AnnotationMaps are empty.
     */
    @Test
    public void testMergeEmptyMaps() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(Collections.emptyList(), merged.annotations());
    }

    /**
     * Test addIfNotPresent when same annotation is already present.
     */
    @Test
    public void testAddIfNotPresentSameAnnotation() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation ann = new MyAnnotation();
        am.add(ann);
        assertFalse(am.addIfNotPresent(ann));
    }

    /**
     * Test add when same annotation is already present.
     */
    @Test
    public void testAddSameAnnotation() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation ann = new MyAnnotation();
        am.add(ann);
        assertFalse(am.add(ann));
    }

    /**
     * Test addIfNotPresent when null annotation.
     */
    @Test(expected = NullPointerException.class)
    public void testAddIfNotPresentNullAnnotation() {
        AnnotationMap am = new AnnotationMap();
        am.addIfNotPresent(null);
    }

    /**
     * Test add when null annotation.
     */
    @Test(expected = NullPointerException.class)
    public void testAddNullAnnotation() {
        AnnotationMap am = new AnnotationMap();
        am.add(null);
    }
}