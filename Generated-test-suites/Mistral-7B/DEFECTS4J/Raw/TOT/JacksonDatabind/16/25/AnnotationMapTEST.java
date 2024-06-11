package com.fasterxml.jackson.databind.introspect.test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOClass;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;

import static org.junit.Assert.*;

public class AnnotationMapTest {

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation);
        JsonPOJOClass jsonPOJOClassAnnotation = new JsonPOJOClass();
        annotationMap.add(jsonPOJOClassAnnotation);

        Assert.assertEquals(jsonDeserializeAnnotation, annotationMap.get(JsonDeserialize.class));
        Assert.assertEquals(jsonPOJOClassAnnotation, annotationMap.get(JsonPOJOClass.class));
        Assert.assertNull(annotationMap.get(JsonSerialize.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new Retention(RetentionPolicy.RUNTIME));
        annotationMap.add(new Target(ElementType.FIELD));
        annotationMap.add(new JsonDeserialize());
        annotationMap.add(new JsonPOJOClass());
        annotationMap.add(new JsonSerialize());

        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        Assert.assertEquals(4, annotations.size());
        Assert.assertTrue(annotations.contains(AnnotationMapTest.class.getAnnotation(Retention.class)));
        Assert.assertTrue(annotations.contains(AnnotationMapTest.class.getAnnotation(Target.class)));
        Assert.assertTrue(annotations.contains(JsonDeserialize.class));
        Assert.assertTrue(annotations.contains(JsonPOJOClass.class));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new Retention(RetentionPolicy.RUNTIME));
        annotationMap.add(new Target(ElementType.FIELD));
        annotationMap.add(new JsonDeserialize());
        annotationMap.add(new JsonPOJOClass());
        annotationMap.add(new JsonSerialize());

        Assert.assertEquals(5, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        annotationMap.addIfNotPresent(jsonDeserializeAnnotation);
        Assert.assertEquals(1, annotationMap.size());
        Assert.assertEquals(jsonDeserializeAnnotation, annotationMap.get(JsonDeserialize.class));

        AnnotationMap annotationMapWithPresentAnnotation = new AnnotationMap();
        annotationMapWithPresentAnnotation.add(jsonDeserializeAnnotation);
        Assert.assertTrue(annotationMapWithPresentAnnotation.addIfNotPresent(jsonDeserializeAnnotation));
        Assert.assertEquals(1, annotationMapWithPresentAnnotation.size());
        Assert.assertEquals(jsonDeserializeAnnotation, annotationMapWithPresentAnnotation.get(JsonDeserialize.class));

        AnnotationMap annotationMapWithMultiplePresentAnnotations = new AnnotationMap();
        annotationMapWithMultiplePresentAnnotations.add(jsonDeserializeAnnotation);
        Annotation annotationToAdd = new JsonSerialize();
        Assert.assertFalse(annotationMapWithMultiplePresentAnnotations.addIfNotPresent(annotationToAdd));
        Assert.assertEquals(1, annotationMapWithMultiplePresentAnnotations.size());
        Assert.assertEquals(jsonDeserializeAnnotation, annotationMapWithMultiplePresentAnnotations.get(JsonDeserialize.class));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation);
        Assert.assertEquals(1, annotationMap.size());
        Assert.assertEquals(jsonDeserializeAnnotation, annotationMap.get(JsonDeserialize.class));

        AnnotationMap annotationMapWithPresentAnnotation = new AnnotationMap();
        annotationMapWithPresentAnnotation.add(jsonDeserializeAnnotation);
        Annotation annotationToAdd = new JsonSerialize();
        annotationMapWithPresentAnnotation.add(annotationToAdd);
        Assert.assertEquals(2, annotationMapWithPresentAnnotation.size());
        Assert.assertTrue(annotationMapWithPresentAnnotation.containsKey(JsonDeserialize.class));
        Assert.assertTrue(annotationMapWithPresentAnnotation.containsKey(JsonSerialize.class));
    }

    @Test
    public void testMerge() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        annotationMap1.add(new Retention(RetentionPolicy.RUNTIME));
        annotationMap1.add(new Target(ElementType.FIELD));
        annotationMap1.add(new JsonDeserialize());

        AnnotationMap annotationMap2 = new AnnotationMap();
        annotationMap2.add(new JsonPOJOClass());
        annotationMap2.add(new JsonSerialize());

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        List<Annotation> mergedAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
        Assert.assertEquals(5, mergedAnnotationMap.size());
        Assert.assertTrue(mergedAnnotations.containsAll(annotationMap1.annotations()));
        Assert.assertTrue(mergedAnnotations.containsAll(annotationMap2.annotations()));
    }
}