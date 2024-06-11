package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.introspect.AnnotationMap;

/**
 * Test class for AnnotationMap.java
 */
public class AnnotationMapTest {

    /**
     * Test constructor AnnotationMap().
     */
    @Test
    public void testAnnotationMapConstructor() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Annotation.class));
        assertFalse(annotationMap.annotations().hasNext());
        assertEquals(0, annotationMap.size());
    }

    /**
     * Test method get.
     */
    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation1());
        MyAnnotation1 myAnnotation1 = annotationMap.get(MyAnnotation1.class);
        assertNotNull(myAnnotation1);
        assertEquals(myAnnotation1, annotationMap.get(MyAnnotation1.class));
        assertNull(annotationMap.get(MyAnnotation2.class));
    }

    /**
     * Test method annotations.
     */
    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation1());
        Iterator<Annotation> iterator = annotationMap.annotations().iterator();
        assertTrue(iterator.hasNext());
        Annotation annotation = iterator.next();
        assertEquals(annotation, annotationMap.get(MyAnnotation1.class));
        assertFalse(iterator.hasNext());
        assertFalse(annotationMap.annotations().hasNext());
    }

    /**
     * Test method merge.
     */
    @Test
    public void testMerge() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        primaryAnnotationMap.add(new MyAnnotation1());

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        secondaryAnnotationMap.add(new MyAnnotation2());
        secondaryAnnotationMap.add(new MyAnnotation1());

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        assertEquals(2, mergedAnnotationMap.size());
        assertTrue(mergedAnnotationMap.annotations().hasNext());
        Annotation mergedAnnotation1 = mergedAnnotationMap.annotations().next();
        assertSame(mergedAnnotation1, primaryAnnotationMap.get(MyAnnotation1.class));
        assertTrue(mergedAnnotationMap.annotations().hasNext());
        Annotation mergedAnnotation2 = mergedAnnotationMap.annotations().next();
        assertSame(mergedAnnotation2, secondaryAnnotationMap.get(MyAnnotation2.class));
    }

    /**
     * Test method size.
     */
    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
        annotationMap.add(new MyAnnotation1());
        assertEquals(1, annotationMap.size());
        annotationMap.add(new MyAnnotation2());
        assertEquals(2, annotationMap.size());
    }

    /**
     * Test method addIfNotPresent.
     */
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertFalse(annotationMap.addIfNotPresent(new MyAnnotation1()));
        assertTrue(annotationMap.addIfNotPresent(new MyAnnotation2()));
        assertEquals(1, annotationMap.size());
        assertTrue(annotationMap.annotations().hasNext());
        Annotation annotation = annotationMap.annotations().next();
        assertSame(annotation, annotationMap.get(MyAnnotation2.class));
    }

    /**
     * Test method add.
     */
    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertFalse(annotationMap.add(new MyAnnotation1()));
        assertEquals(0, annotationMap.size());
        annotationMap.add(new MyAnnotation1());
        assertEquals(1, annotationMap.size());
        assertTrue(annotationMap.annotations().hasNext());
        Annotation annotation = annotationMap.annotations().next();
        assertSame(annotation, annotationMap.get(MyAnnotation1.class));
    }

    /**
     * Test method toString.
     */
    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation1());
        annotationMap.add(new MyAnnotation2());
        assertEquals("[{class com.fasterxml.jackson.databind.introspect.tests.MyAnnotation1}, {class com.fasterxml.jackson.databind.introspect.tests.MyAnnotation2}]", annotationMap.toString());
    }

    /**
     * Test helper method _add.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testAddException() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap._add(new MyAnnotation1()); // should not be accessible from outside
    }

    /**
     * Test class MyAnnotation1.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @interface MyAnnotation1 {
    }

    /**
     * Test class MyAnnotation2.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @interface MyAnnotation2 {
    }
}