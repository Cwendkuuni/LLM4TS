package com.fasterxml.jackson.databind.introspect.test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.annotation.Annotation;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AnnotationMapTest {

    @Test
    public void testCreateAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(JsonDeserialize.class));
        assertNull(annotationMap.get(JsonSerialize.class));
        assertTrue(annotationMap.size() == 0);
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation);
        assertSame(jsonDeserializeAnnotation, annotationMap.get(JsonDeserialize.class));
    }

    @Test
    public void testGetEmptyList() {
        AnnotationMap annotationMap = new AnnotationMap();
        List<Annotation> annotations = annotationMap.annotations();
        assertTrue(annotations.isEmpty());
    }

    @Test
    public void testGetNonEmptyList() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        primaryAnnotationMap.add(jsonDeserializeAnnotation1);
        JsonSerialize jsonSerializeAnnotation1 = new JsonSerialize();
        primaryAnnotationMap.add(jsonSerializeAnnotation1);

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation2 = new JsonDeserialize();
        secondaryAnnotationMap.add(jsonDeserializeAnnotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        List<Annotation> annotations = mergedAnnotationMap.annotations();

        assertTrue(annotations.size() == 2);
        assertTrue(annotations.contains(jsonDeserializeAnnotation1));
        assertTrue(annotations.contains(jsonDeserializeAnnotation2));
        assertTrue(annotations.contains(jsonSerializeAnnotation1));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize());
        annotationMap.add(new JsonSerialize());
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        assertTrue(annotationMap.addIfNotPresent(jsonDeserializeAnnotation));
        assertSame(jsonDeserializeAnnotation, annotationMap.get(JsonDeserialize.class));

        JsonSerialize jsonSerializeAnnotation = new JsonSerialize();
        assertTrue(annotationMap.addIfNotPresent(jsonSerializeAnnotation));
        assertSame(jsonSerializeAnnotation, annotationMap.get(JsonSerialize.class));

        JsonDeserialize jsonDeserializeAnnotation2 = new JsonDeserialize();
        assertFalse(annotationMap.addIfNotPresent(jsonDeserializeAnnotation2));
        assertSame(jsonDeserializeAnnotation1, annotationMap.get(JsonDeserialize.class));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation);
        assertSame(jsonDeserializeAnnotation, annotationMap.get(JsonDeserialize.class));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        jsonDeserializeAnnotation.value();
        annotationMap.add(jsonDeserializeAnnotation);
        assertEquals("[AnnotationClass(value=class com.fasterxml.jackson.databind.annotation.JsonDeserialize)]", annotationMap.toString());
    }
}