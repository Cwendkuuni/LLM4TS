package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testDefaultConstruction() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.size());
        assertNull(map.get(JsonInclude.class));
    }

    @Test
    public void testConstructionWithMap() {
        AnnotationMap map = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {
            {
                put(JsonInclude.class, new JsonInclude.Include(JsonInclude.Include.NonEmpty));
            }
        });
        assertEquals(1, map.size());
        assertEquals(JsonInclude.Include.NON_EMPTY, map.get(JsonInclude.class).memberValue());
    }

    @Test
    public void testGet() {
        AnnotationMap map = new AnnotationMap();
        map.add(new JsonInclude(JsonInclude.Include.NON_EMPTY));
        assertEquals(JsonInclude.Include.NON_EMPTY, map.get(JsonInclude.class).memberValue());
    }

    @Test
    public void testGetNull() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(JsonInclude.class));
    }

    @Test
    public void testAnnotationsEmpty() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(Collections.emptyList(), map.annotations());
    }

    @Test
    public void testAnnotationsWithAnnotations() {
        AnnotationMap map = new AnnotationMap();
        map.add(new JsonInclude(JsonInclude.Include.NON_EMPTY));
        map.add(new JsonPropertyOrder());
        Collection<Annotation> annotations = map.annotations();
        Set<Class<? extends Annotation>> expectedTypes = new HashSet<Class<? extends Annotation>>() {{
            add(JsonInclude.class);
            add(JsonPropertyOrder.class);
        }};
        assertEquals(expectedTypes.size(), annotations.size());
        for (Annotation annotation : annotations) {
            assertTrue(expectedTypes.contains(annotation.annotationType()));
        }
    }

    @Test
    public void testMergeEmptyWithEmpty() {
        AnnotationMap map1 = new AnnotationMap();
        AnnotationMap map2 = new AnnotationMap();
        AnnotationMap result = AnnotationMap.merge(map1, map2);
        assertEquals(0, result.size());
    }

    @Test
    public void testMergeEmptyWithNonEmpty() {
        AnnotationMap map1 = new AnnotationMap();
        AnnotationMap map2 = new AnnotationMap();
        map2.add(new JsonInclude(JsonInclude.Include.NON_EMPTY));
        AnnotationMap result = AnnotationMap.merge(map1, map2);
        assertEquals(1, result.size());
        assertEquals(JsonInclude.Include.NON_EMPTY, result.get(JsonInclude.class).memberValue());
    }

    @Test
    public void testMergeNonEmptyWithEmpty() {
        AnnotationMap map1 = new AnnotationMap() {{
            add(new JsonInclude(JsonInclude.Include.NON_EMPTY));
        }};
        AnnotationMap map2 = new AnnotationMap();
        AnnotationMap result = AnnotationMap.merge(map1, map2);
        assertEquals(1, result.size());
        assertEquals(JsonInclude.Include.NON_EMPTY, result.get(JsonInclude.class).memberValue());
    }

    @Test
    public void testMergeWithSameAnnotations() {
        AnnotationMap map1 = new AnnotationMap() {{
            add(new JsonInclude(JsonInclude.Include.NON_EMPTY));
        }};
        AnnotationMap map2 = new AnnotationMap() {{
            add(new JsonInclude(JsonInclude.Include.NON_EMPTY));
        }};
        AnnotationMap result = AnnotationMap.merge(map1, map2);
        assertEquals(1, result.size());
        assertEquals(JsonInclude.Include.NON_EMPTY, result.get(JsonInclude.class).memberValue());
    }

    @Test
    public void testMergeWithDifferentAnnotations() {
        AnnotationMap map1 = new AnnotationMap() {{
            add(new JsonInclude(JsonInclude.Include.NON_EMPTY));
        }};
        AnnotationMap map2 = new AnnotationMap() {{
            add(new JsonPropertyOrder());
        }};
        AnnotationMap result = AnnotationMap.merge(map1, map2);
        assertEquals(2, result.size());
        assertEquals(JsonInclude.Include.NON_EMPTY, result.get(JsonInclude.class).memberValue());
        assertEquals(JsonPropertyOrder.class, result.get(JsonPropertyOrder.class).annotationType());
    }

    @Test
    public void testSize() {
        AnnotationMap map = new AnnotationMap() {{
            add(new JsonInclude(JsonInclude.Include.NON_EMPTY));
            add(new JsonPropertyOrder());
        }};
        assertEquals(2, map.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();
        assertFalse(map.addIfNotPresent(new JsonInclude(JsonInclude.Include.NON_EMPTY)));
        assertTrue(map.addIfNotPresent(new JsonInclude(JsonInclude.Include.NON_EMPTY)));
        assertEquals(1, map.size());
    }

    @Test
    public void testAdd() {
        AnnotationMap map = new AnnotationMap();
        assertFalse(map.add(new JsonInclude(JsonInclude.Include.NON_EMPTY)));
        map.add(new JsonInclude(JsonInclude.Include.NON_EMPTY));
        assertEquals(1, map.size());
    }

    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap() {{
            add(new JsonInclude(JsonInclude.Include.NON_EMPTY));
            add(new JsonPropertyOrder());
        }};
        assertEquals("{@org.codehaus.jackson.annotate.JsonInclude(include=NON_EMPTY), @com.fasterxml.jackson.annotation.JsonPropertyOrder}", map.toString());
    }
}