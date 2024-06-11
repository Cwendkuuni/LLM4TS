package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.annotation.Annotation;
import java.util.Collections;

import org.junit.Test;

public class AnnotationMapTest {
    @Test
    public void testGet() {
        AnnotationMap map = new AnnotationMap();

        assertNull(map.get(Override.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap map = new AnnotationMap();

        assertEquals(Collections.emptyList(), map.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        assertEquals(secondary, AnnotationMap.merge(null, secondary));
        assertEquals(primary, AnnotationMap.merge(primary, null));

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(secondary, merged);

    }

    @Test
    public void testSize() {
        AnnotationMap map = new AnnotationMap();

        assertEquals(0, map.size());

        map.add(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });

        assertEquals(1, map.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();

        Annotation deprecatedAnn = createDeprecatedAnnotation();

        assertEquals(true, map.addIfNotPresent(deprecatedAnn));
        assertEquals(false, map.addIfNotPresent(deprecatedAnn));
    }

    @Test
    public void testAdd() {
        AnnotationMap map = new AnnotationMap();

        Annotation deprecatedAnn = createDeprecatedAnnotation();

        assertEquals(true, map.add(deprecatedAnn));
        assertEquals(false, map.add(deprecatedAnn));
    }

    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap();

        assertEquals("[null]", map.toString());
    }

    /*
    /**********************************************************
    /* Helper methods
    /**********************************************************
     */

    private Annotation createDeprecatedAnnotation() {
        return new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
    }
}