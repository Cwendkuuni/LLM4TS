package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testSize() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.size());

        map.add(new MyAnnotation());
        assertEquals(1, map.size());

        map.add(new MyAnnotation());
        assertEquals(2, map.size());
    }

    @Test
    public void testGet() {
        AnnotationMap map = new AnnotationMap();
        Annotation myAnnotation = new MyAnnotation();
        map.add(myAnnotation);

        assertEquals(myAnnotation, map.get(MyAnnotation.class));
        assertNull(map.get(String.class));
    }

    @Test
    public void testGetAnnotationByType() {
        AnnotationMap map = new AnnotationMap();
        Annotation myAnnotation1 = new MyAnnotation();
        Annotation myAnnotation2 = new MyAnnotation();
        map.add(myAnnotation1);
        map.add(myAnnotation2);

        assertEquals(myAnnotation1, map.getAnnotation(MyAnnotation.class));
        assertEquals(myAnnotation2, map.getAnnotation(MyAnnotation.class));

        assertNull(map.getAnnotation(String.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap map = new AnnotationMap();
        Annotation myAnnotation1 = new MyAnnotation();
        Annotation myAnnotation2 = new MyAnnotation();
        map.add(myAnnotation1);
        map.add(myAnnotation2);

        Set<Annotation> actual = new HashSet<>(map.annotations());
        Set<Annotation> expected = new HashSet<Annotation>() {{
            add(myAnnotation1);
            add(myAnnotation2);
        }};

        assertEquals(expected, actual);

        assertEquals(2, map.annotations().size());
    }

    @Test
    public void testMerge() {
        AnnotationMap map1 = new AnnotationMap();
        Annotation myAnnotation1 = new MyAnnotation();
        map1.add(myAnnotation1);

        AnnotationMap map2 = new AnnotationMap();
        Annotation myAnnotation2 = new MyAnnotation();
        map2.add(myAnnotation2);

        AnnotationMap result = AnnotationMap.merge(map1, map2);

        Set<Annotation> actual = new HashSet<>(result.annotations());
        Set<Annotation> expected = new HashSet<Annotation>() {{
            add(myAnnotation1);
            add(myAnnotation2);
        }};

        assertEquals(expected, actual);

        assertEquals(2, result.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();
        Annotation myAnnotation = new MyAnnotation();

        assertFalse(map.addIfNotPresent(myAnnotation));
        assertTrue(map.add(myAnnotation));
        assertTrue(map.containsKey(MyAnnotation.class));
    }

    @Test
    public void testAdd() {
        AnnotationMap map = new AnnotationMap();
        Annotation myAnnotation = new MyAnnotation();

        assertFalse(map.add(myAnnotation));
        assertTrue(map.addIfNotPresent(myAnnotation));
        assertTrue(map.containsKey(MyAnnotation.class));
    }

    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap();
        map.add(new MyAnnotation());

        String expected = "{\n" +
                "  com.fasterxml.jackson.annotation.JsonAutoDetect.CLASS = com.fasterxml.jackson.annotation.JsonAutoDetect$Annotation$1@4647d324,\n" +
                "  com.fasterxml.jackson.annotation.JsonInclude.Value.ALWAYS = com.fasterxml.jackson.annotation.JsonInclude$Value$Always@73cb491,\n" +
                "  com.fasterxml.jackson.annotation.JsonPropertyOrder.ALPHABETICAL = com.fasterxml.jackson.annotation.JsonPropertyOrder$Alphabetical@53476433,\n" +
                "  com.fasterxml.jackson.annotation.JsonPropertyOrder.Custom = com.fasterxml.jackson.annotation.JsonPropertyOrder$Custom@7c0f6f1,\n" +
                "  java.lang.annotation.Annotation$Present = java.lang.annotation.Annotation$Present@523737f,\n" +
                "  java.lang.annotation.Annotation$Retention = java.lang.annotation.Annotation$Retention@41820e,\n" +
                "  java.lang.annotation.Annotation$Target = java.lang.annotation.Annotation$Target@66924a,\n" +
                "  java.lang.reflect.AnnotatedElement = sun.reflect.annotation.EmptyAnnotation$AnnotationType@0,\n" +
                "  java.lang.reflect.AnnotatedElement = sun.reflect.annotation.EmptyAnnotation$AnnotationType@0\n" +
                "}";

        assertEquals(expected, map.toString());
    }

    static class MyAnnotation {
    }
}