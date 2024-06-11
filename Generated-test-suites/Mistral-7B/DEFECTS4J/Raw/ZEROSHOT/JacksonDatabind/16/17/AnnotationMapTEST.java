package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.introspect.AnnotationMap;

public class AnnotationMapTest {

    private AnnotationMap emptyAnnotationMap;
    private AnnotationMap singleAnnotationMap;
    private AnnotationMap multipleAnnotationMap;

    @Before
    public void setUp() {
        emptyAnnotationMap = new AnnotationMap();
        singleAnnotationMap = new AnnotationMap();
        singleAnnotationMap.add(new MyAnnotation());
        multipleAnnotationMap = new AnnotationMap();
        multipleAnnotationMap.add(new MyAnnotation());
        multipleAnnotationMap.add(new MyOtherAnnotation());
    }

    @Test
    public void testSize() {
        assertEquals(0, emptyAnnotationMap.size());
        assertEquals(1, singleAnnotationMap.size());
        assertEquals(2, multipleAnnotationMap.size());
    }

    @Test
    public void testGet() {
        assertNull(emptyAnnotationMap.get(MyAnnotation.class));
        assertNotNull(singleAnnotationMap.get(MyAnnotation.class));
        assertNotNull(multipleAnnotationMap.get(MyAnnotation.class));
        assertNull(multipleAnnotationMap.get(MyOtherAnnotation.class));
    }

    @Test
    public void testGetNull() {
        assertNull(emptyAnnotationMap.get(null));
        assertNull(singleAnnotationMap.get(null));
        assertNull(multipleAnnotationMap.get(null));
    }

    @Test
    public void testGetUnknown() {
        assertNull(emptyAnnotationMap.get(UnknownAnnotation.class));
        assertNull(singleAnnotationMap.get(UnknownAnnotation.class));
        assertNull(multipleAnnotationMap.get(UnknownAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        assertEquals(Collections.emptyList(), emptyAnnotationMap.annotations());
        assertEquals(Arrays.asList(new MyAnnotation()), singleAnnotationMap.annotations());
        List<Annotation> annotations = new ArrayList<>(multipleAnnotationMap.annotations());
        assertEquals(2, annotations.size());
        assertEquals(MyAnnotation.class, annotations.get(0).annotationType());
        assertEquals(MyOtherAnnotation.class, annotations.get(1).annotationType());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(singleAnnotationMap.isEmpty());
        assertFalse(multipleAnnotationMap.isEmpty());
        assertTrue(emptyAnnotationMap.isEmpty());
    }

    @Test
    public void testMerge() {
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(singleAnnotationMap, multipleAnnotationMap);

        Set<Class<? extends Annotation>> annotationsClasses = new HashSet<>(Arrays.asList(MyAnnotation.class, MyOtherAnnotation.class));
        assertEquals(annotationsClasses, new HashSet<>(mergedAnnotationMap.annotations().stream().map(Annotation::annotationType).collect(Collectors.toSet())));
    }

    @Test
    public void testMergeEmpty() {
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(emptyAnnotationMap, multipleAnnotationMap);
        assertEquals(multipleAnnotationMap, mergedAnnotationMap);
    }

    @Test
    public void testMergeNull() {
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(singleAnnotationMap, null);
        assertEquals(singleAnnotationMap, mergedAnnotationMap);
    }

    @Test
    public void testAddIfNotPresent() {
        assertTrue(singleAnnotationMap.addIfNotPresent(new MyAnnotation()));
        assertFalse(singleAnnotationMap.addIfNotPresent(new MyAnnotation()));
        assertFalse(singleAnnotationMap.addIfNotPresent(null));

        AnnotationMap mergedAnnotationMap = new AnnotationMap();
        assertTrue(mergedAnnotationMap.addIfNotPresent(new MyAnnotation()));
        assertEquals(1, mergedAnnotationMap.size());
        assertEquals(MyAnnotation.class, mergedAnnotationMap.get(MyAnnotation.class).annotationType());
    }

    @Test
    public void testAdd() {
        assertTrue(singleAnnotationMap.add(new MyAnnotation()));
        assertEquals(1, singleAnnotationMap.size());
        assertEquals(MyAnnotation.class, singleAnnotationMap.get(MyAnnotation.class).annotationType());

        assertTrue(multipleAnnotationMap.add(new MyOtherAnnotation()));
        assertEquals(2, multipleAnnotationMap.size());
        assertEquals(MyOtherAnnotation.class, multipleAnnotationMap.get(MyOtherAnnotation.class).annotationType());
    }

    @Test
    public void testToString() {
        assertEquals("{}", emptyAnnotationMap.toString());
        assertEquals("{@" + MyAnnotation.class.getCanonicalName() + "=Annotation@2a0b1c}", singleAnnotationMap.toString());
        assertEquals("{@" + MyAnnotation.class.getCanonicalName() + "=Annotation@2a0b1c, @" + MyOtherAnnotation.class.getCanonicalName() + "=Annotation@3a0b2c}", multipleAnnotationMap.toString());
    }
}