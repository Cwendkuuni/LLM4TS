package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testDefaultConstructor() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
        assertNull(annotationMap.get(JSON.class));
    }

    @Test
    public void testCopyConstructor() {
        AnnotationMap originalAnnotationMap = createAnnotationMap();
        AnnotationMap copyAnnotationMap = new AnnotationMap(originalAnnotationMap._annotations);

        assertEquals(originalAnnotationMap.size(), copyAnnotationMap.size());
        for (Map.Entry<Class<? extends Annotation>, Annotation> entry : originalAnnotationMap._annotations.entrySet()) {
            Class<? extends Annotation> key = entry.getKey();
            Annotation value = entry.getValue();
            assertSame(value, copyAnnotationMap.get(key));
        }
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = createAnnotationMap();
        assertEquals(annotationMap.size(), annotationMap._annotations.size());
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = createAnnotationMap();
        assertEquals(annotationMap.get(JSON.class), annotationMap._annotations.get(JSON.class));
        assertNull(annotationMap.get(Null.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = createAnnotationMap();
        List<Annotation> annotations = Arrays.asList(annotationMap._annotations.values().toArray(new Annotation[0]));
        assertEquals(annotations, annotationMap.annotations().asList());
    }

    @Test
    public void testMergeEmptyAnnotationMaps() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        assertEquals(primaryAnnotationMap, mergedAnnotationMap);
    }

    @Test
    public void testMergeSecondaryAnnotationMapIsEmpty() {
        AnnotationMap primaryAnnotationMap = createAnnotationMap();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        assertEquals(primaryAnnotationMap, mergedAnnotationMap);
    }

    @Test
    public void testMergePrimaryAnnotationMapIsEmpty() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        AnnotationMap secondaryAnnotationMap = createAnnotationMap();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        assertEquals(secondaryAnnotationMap, mergedAnnotationMap);
    }

    @Test
    public void testMergeAnnotationMaps() {
        AnnotationMap primaryAnnotationMap = createAnnotationMap();
        AnnotationMap secondaryAnnotationMap = createAnnotationMapWithAdditionalAnnotations();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        assertEquals(secondaryAnnotationMap.size(), mergedAnnotationMap.size());
        for (Map.Entry<Class<? extends Annotation>, Annotation> entry : secondaryAnnotationMap._annotations.entrySet()) {
            Class<? extends Annotation> key = entry.getKey();
            Annotation value = entry.getValue();
            assertSame(value, mergedAnnotationMap.get(key));
        }

        for (Map.Entry<Class<? extends Annotation>, Annotation> entry : primaryAnnotationMap._annotations.entrySet()) {
            Class<? extends Annotation> key = entry.getKey();
            Annotation value = entry.getValue();
            assertSame(value, mergedAnnotationMap.get(key));
        }
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation jsonAnnotation = createJsonAnnotation();
        boolean added = annotationMap.addIfNotPresent(jsonAnnotation);
        assertTrue(added);
        assertEquals(jsonAnnotation, annotationMap.get(JSON.class));
        added = annotationMap.addIfNotPresent(jsonAnnotation);
        assertFalse(added);
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation jsonAnnotation = createJsonAnnotation();
        boolean added = annotationMap.add(jsonAnnotation);
        assertTrue(added);
        assertEquals(jsonAnnotation, annotationMap.get(JSON.class));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = createAnnotationMap();
        String toString = annotationMap.toString();
        assertTrue(toString.startsWith("[") && toString.endsWith("]"));
        int size = annotationMap.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<Class<? extends Annotation>, Annotation> entry = annotationMap._annotations.entrySet().iterator().next();
            String expectedEntryString = String.format("%s=%s,", entry.getKey().getName(), entry.getValue().annotationType().getName());
            assertTrue(toString.contains(expectedEntryString));
        }
    }

    private AnnotationMap createAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        addJsonAnnotationToAnnotationMap(annotationMap);
        addXmlRootAnnotationToAnnotationMap(annotationMap);
        return annotationMap;
    }

    private AnnotationMap createAnnotationMapWithAdditionalAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        addJsonAnnotationToAnnotationMap(annotationMap);
        addXmlRootAnnotationToAnnotationMap(annotationMap);
        addJsonPropertyAnnotationToAnnotationMap(annotationMap, "name");
        return annotationMap;
    }

    private void addJsonAnnotationToAnnotationMap(AnnotationMap annotationMap) {
        addAnnotationToAnnotationMap(annotationMap, JSON.class, createJsonAnnotation());
    }

    private void addXmlRootAnnotationToAnnotationMap(AnnotationMap annotationMap) {
        addAnnotationToAnnotationMap(annotationMap, XMLRootElement.class, createXmlRootAnnotation());
    }

    private void addJsonPropertyAnnotationToAnnotationMap(AnnotationMap annotationMap, String propertyName) {
        addAnnotationToAnnotationMap(annotationMap, JSONProperty.class, createJsonPropertyAnnotation(propertyName));
    }

    private Annotation createJsonAnnotation() {
        return new JSON();
    }

    private Annotation createJsonPropertyAnnotation(String propertyName) {
        return new JSONProperty(propertyName);
    }

    private Annotation createXmlRootAnnotation() {
        return new XMLRootElement();
    }

    private void addAnnotationToAnnotationMap(AnnotationMap annotationMap, Class<? extends Annotation> annotationType, Annotation annotation) {
        annotationMap.add(annotation);
    }

    private static class JSON {
        // empty annotation
    }

    @JSONProperty
    private static class JSONProperty {
        private final String name;

        public JSONProperty(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "JSONProperty(name=" + name + ")";
        }
    }

    @XMLRootElement
    private static class XMLRootElement {
        // empty annotation
    }

    private static interface Null {
        // empty marker interface
    }
}