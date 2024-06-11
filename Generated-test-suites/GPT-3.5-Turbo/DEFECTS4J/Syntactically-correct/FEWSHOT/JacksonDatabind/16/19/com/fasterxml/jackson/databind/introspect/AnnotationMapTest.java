package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AnnotationMapTest {
    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }
    
    @Test
    public void testGet() {
        assertNull(annotationMap.get(Override.class));
    }

    @Test
    public void testSize() {
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testToString() {
        assertEquals("[null]", annotationMap.toString());
    }

    @Test
    public void testAddIfNotPresent() {
        assertTrue(annotationMap.addIfNotPresent(createAnnotation(One.class)));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAdd() {
        assertTrue(annotationMap.add(createAnnotation(One.class)));
        assertEquals(1, annotationMap.size());
    }
    
    @Test
    public void testAnnotations() {
        List<Annotation> expected = Arrays.asList(createAnnotation(One.class), createAnnotation(Two.class));
        annotationMap.add(createAnnotation(One.class));
        annotationMap.add(createAnnotation(Two.class));
        assertEquals(expected, annotationMap.annotations());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primaryMap = new AnnotationMap(new HashMap<>());
        AnnotationMap secondaryMap = new AnnotationMap(new HashMap<>());
        
        primaryMap.add(createAnnotation(One.class));
        primaryMap.add(createAnnotation(Two.class));
        secondaryMap.add(createAnnotation(Three.class));
        
        AnnotationMap result = AnnotationMap.merge(primaryMap, secondaryMap);
        
        assertEquals(3, result.size());
    }
    
    private Annotation createAnnotation(Class<? extends Annotation> cls) {
        return new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return cls;
            }
        };
    }

    @One
    private static class TestClass {}
    
    @interface One {}
    
    @interface Two {}
    
    @interface Three {}
}