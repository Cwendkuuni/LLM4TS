package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testAnnotationMapCreation() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map._annotations);

        AnnotationMap mapWithAnnotations = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(JsonProperty.class, new JsonProperty());
            put(JsonPropertyOrder.class, new JsonPropertyOrder());
        }});
        assertNotNull(mapWithAnnotations._annotations);
        assertEquals(2, mapWithAnnotations.size());
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap map = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(JsonProperty.class, new JsonProperty());
            put(JsonPropertyOrder.class, new JsonPropertyOrder());
        }});

        JsonProperty jsonProperty = map.get(JsonProperty.class);
        assertNotNull(jsonProperty);
        JsonPropertyOrder jsonPropertyOrder = map.get(JsonPropertyOrder.class);
        assertNotNull(jsonPropertyOrder);

        assertNull(map.get(JsonInclude.class));
    }

    @Test
    public void testGetAnnotationNullMap() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(JsonProperty.class));
    }

    @Test
    public void testAnnotationsEmptyMap() {
        AnnotationMap map = new AnnotationMap();
        assertTrue(map.annotations().isEmpty());
    }

    @Test
    public void testAnnotationsNonEmptyMap() {
        AnnotationMap map = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(JsonProperty.class, new JsonProperty());
            put(JsonPropertyOrder.class, new JsonPropertyOrder());
        }});
        List<Annotation> annotations = Arrays.asList(map.annotations().toArray(new Annotation[0]));
        assertEquals(2, annotations.size());
        assertSame(JsonProperty.class, annotations.get(0).annotationType());
        assertSame(JsonPropertyOrder.class, annotations.get(1).annotationType());
    }

    @Test
    public void testMergeEmptyPrimary() {
        AnnotationMap secondary = new AnnotationMap(Collections.singletonMap(JsonProperty.class, new JsonProperty()));
        AnnotationMap merged = AnnotationMap.merge(null, secondary);
        assertEquals(1, merged.size());
        List<Annotation> annotations = Arrays.asList(merged.annotations().toArray(new Annotation[0]));
        assertEquals(JsonProperty.class, annotations.get(0).annotationType());
    }

    @Test
    public void testMergeEmptySecondary() {
        AnnotationMap primary = new AnnotationMap(Collections.singletonMap(JsonProperty.class, new JsonProperty()));
        AnnotationMap merged = AnnotationMap.merge(primary, null);
        assertEquals(1, merged.size());
        List<Annotation> annotations = Arrays.asList(merged.annotations().toArray(new Annotation[0]));
        assertEquals(JsonProperty.class, annotations.get(0).annotationType());
    }

    @Test
    public void testMergePrimaryOverridesSecondary() {
        AnnotationMap primary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(JsonProperty.class, new JsonProperty());
            put(JsonPropertyOrder.class, new JsonPropertyOrder());
        }});
        AnnotationMap secondary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(JsonProperty.class, new JsonProperty());
            put(JsonPropertyOrder.class, new JsonPropertyOrder());
            put(JsonPropertyOrder.class, new JsonPropertyOrder() {{
                @SuppressWarnings("unchecked")
                List<String> alphabetical = (List<String>)getPropertyValue("alphabetical", new JsonPropertyOrder());
                alphabetical.add("Z");
            }});
        }});
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(2, merged.size());
        List<Annotation> annotations = Arrays.asList(merged.annotations().toArray(new Annotation[0]));
        assertEquals(JsonProperty.class, annotations.get(0).annotationType());
        JsonPropertyOrder mergedJsonPropertyOrder = (JsonPropertyOrder)annotations.get(1);
        List<String> alphabetical = (List<String>)getPropertyValue("alphabetical", mergedJsonPropertyOrder);
        assertEquals(7, alphabetical.size());
        assertTrue(alphabetical.containsAll( Arrays.asList("A", "E", "K", "Q", "U", "Z")));
    }

    @Test
    public void testMergePrimaryAndSecondarySameAnnotations() {
        AnnotationMap primary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(JsonProperty.class, new JsonProperty());
        }});
        AnnotationMap secondary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(JsonProperty.class, new JsonProperty());
        }});
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(1, merged.size());
        List<Annotation> annotations = Arrays.asList(merged.annotations().toArray(new Annotation[0]));
        assertEquals(JsonProperty.class, annotations.get(0).annotationType());
    }

    @Test
    public void testMergePrimaryAndSecondaryDifferentAnnotations() {
        AnnotationMap primary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(JsonProperty.class, new JsonProperty());
        }});
        AnnotationMap secondary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(JsonInclude.class, new JsonInclude());
        }});
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(2, merged.size());
        List<Annotation> annotations = Arrays.asList(merged.annotations().toArray(new Annotation[0]));
        assertEquals(JsonProperty.class, annotations.get(0).annotationType());
        assertEquals(JsonInclude.class, annotations.get(1).annotationType());
    }

    @Test
    public void testSize() {
        AnnotationMap map = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(JsonProperty.class, new JsonProperty());
            put(JsonPropertyOrder.class, new JsonPropertyOrder());
        }});
        assertEquals(2, map.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();
        assertFalse(map.addIfNotPresent(new JsonProperty()));
        assertTrue(map.addIfNotPresent(new JsonPropertyOrder()));
        assertEquals(1, map.size());
    }

    @Test
    public void testAdd() {
        AnnotationMap map = new AnnotationMap();
        assertFalse(map.add(new JsonProperty()));
        assertTrue(map.add(new JsonPropertyOrder()));
        assertEquals(1, map.size());
    }

    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(JsonProperty.class, new JsonProperty());
            put(JsonPropertyOrder.class, new JsonPropertyOrder());
        }});
        assertNotNull(map.toString());
    }

    private Object getPropertyValue(String propertyName, Annotation annotation) {
        Object value = Annotations.findValue(annotation, propertyName);
        if (value == null) {
            throw new IllegalArgumentException("Annotation has no value for property: " + propertyName);
        }
        return value;
    }
}