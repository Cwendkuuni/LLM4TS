package com.example.annotationmap; // replace with your package name

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOClass;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationMapTest {

    @Test
    public void testConstructor() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertNotNull(annotationMap);
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize(String.class));
        JsonDeserialize jsonDeserialize = annotationMap.get(JsonDeserialize.class);
        Assert.assertNotNull(jsonDeserialize);
    }

    @Test
    public void testGetEmptyAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        Collection<Annotation> annotations = annotationMap.annotations();
        Assert.assertFalse(annotations.isEmpty());
        Assert.assertEquals(0, annotations.size());
    }

    @Test
    public void testMergeEmptyAnnotationMaps() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        Assert.assertSame(primary, merged);
    }

    @Test
    public void testMergeSecondaryIsEmpty() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new JsonDeserialize(String.class));
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        Collection<Annotation> annotations = merged.annotations();
        Assert.assertFalse(annotations.isEmpty());
        Assert.assertEquals(1, annotations.size());
    }

    @Test
    public void testMergePrimaryIsEmpty() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new JsonDeserialize(String.class));
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        Collection<Annotation> annotations = merged.annotations();
        Assert.assertFalse(annotations.isEmpty());
        Assert.assertEquals(1, annotations.size());
    }

    @Test
    public void testMergeBothHaveAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new JsonDeserialize(String.class));
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new JsonPOJOClass(Object.class));
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        Collection<Annotation> annotations = merged.annotations();
        Assert.assertFalse(annotations.isEmpty());
        Assert.assertEquals(2, annotations.size());
    }

    @Test(expected = NullPointerException.class)
    public void testNullPrimary() {
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(null, secondary);
    }

    @Test(expected = NullPointerException.class)
    public void testNullSecondary() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(primary, null);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSizeWhenNull() {
        AnnotationMap annotationMap = new AnnotationMap();
        int size = annotationMap.size();
    }

    @Test
    public void testSizeWhenNotEmpty() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize(String.class));
        int size = annotationMap.size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new JsonDeserialize(String.class);
        boolean added = annotationMap.addIfNotPresent(annotation);
        Assert.assertTrue(added);
        Annotation existingAnnotation = annotationMap.get(annotation.annotationType());
        Assert.assertSame(annotation, existingAnnotation);
    }

    @Test
    public void testAddIfNotPresentAlreadyExists() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize(String.class));
        Annotation existingAnnotation = annotationMap.get(JsonDeserialize.class);
        boolean added = annotationMap.addIfNotPresent(existingAnnotation);
        Assert.assertFalse(added);
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new JsonDeserialize(String.class);
        boolean added = annotationMap.add(annotation);
        Assert.assertTrue(added);
        Annotation existingAnnotation = annotationMap.get(JsonDeserialize.class);
        Assert.assertSame(annotation, existingAnnotation);
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize(String.class));
        String toString = annotationMap.toString();
        String expectedToString = "{" +
                "\"class java.lang.annotation.Annotation\":[" +
                "{\"class com.fasterxml.jackson.databind.annotation.JsonDeserialize\":{\"class java.lang.reflect.TypeVariable[0]\":null,\"value\":{}}]" +
                "}";
        Assert.assertEquals(expectedToString, toString);
    }

    @Test(expected = ClassCastException.class)
    public void testGetAnnotationWithWrongType() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize(String.class));
        Object wrongTypeAnnotation = new Object();
        Annotation wrongTypeAnnotationCast = annotationMap.get(wrongTypeAnnotation.getClass());
    }
}