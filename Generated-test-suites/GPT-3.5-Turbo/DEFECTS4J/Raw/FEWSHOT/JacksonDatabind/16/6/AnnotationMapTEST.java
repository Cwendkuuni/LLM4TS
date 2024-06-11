package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.Collections;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(Override.class));
        
        Annotation override = new OverrideAnnotation();
        map.add(override);
        assertEquals(override, map.get(Override.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(Collections.emptyList(), map.annotations());
        
        Annotation override = new OverrideAnnotation();
        Annotation deprecated = new DeprecatedAnnotation();
        map.add(override);
        map.add(deprecated);
        
        assertEquals(2, map.annotations().size());
        assertTrue(map.annotations().contains(override));
        assertTrue(map.annotations().contains(deprecated));
    }

    @Test
    public void testMerge() {
        AnnotationMap map1 = new AnnotationMap();
        Annotation override = new OverrideAnnotation();
        map1.add(override);
        
        AnnotationMap map2 = new AnnotationMap();
        Annotation deprecated = new DeprecatedAnnotation();
        map2.add(deprecated);
        
        AnnotationMap merged = AnnotationMap.merge(map1, map2);
        assertNotNull(merged);
        assertEquals(2, merged.size());
        assertEquals(merged.get(Override.class), override);
        assertEquals(merged.get(Deprecated.class), deprecated);
    }

    @Test
    public void testSize() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.size());
        
        Annotation override = new OverrideAnnotation();
        map.add(override);
        assertEquals(1, map.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();
        
        Annotation override = new OverrideAnnotation();
        assertTrue(map.addIfNotPresent(override));
        assertEquals(override, map.get(Override.class));
        
        Annotation duplicateOverride = new OverrideAnnotation();
        assertFalse(map.addIfNotPresent(duplicateOverride));
    }

    @Test
    public void testAdd() {
        AnnotationMap map = new AnnotationMap();
        
        Annotation override = new OverrideAnnotation();
        assertTrue(map.add(override));
        assertEquals(override, map.get(Override.class));
        
        Annotation duplicateOverride = new OverrideAnnotation();
        assertFalse(map.add(duplicateOverride));
    }

    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap();
        assertEquals("[null]", map.toString());
        
        Annotation override = new OverrideAnnotation();
        map.add(override);
        assertEquals("{class java.lang.Override=OverrideAnnotation}", map.toString());
    }

    // Custom annotations for testing
    private static class OverrideAnnotation implements Override {

        @Override
        public Class<? extends Annotation> annotationType() {
            return Override.class;
        }
    }

    private static class DeprecatedAnnotation implements Deprecated {

        @Override
        public Class<? extends Annotation> annotationType() {
            return Deprecated.class;
        }
    }
}