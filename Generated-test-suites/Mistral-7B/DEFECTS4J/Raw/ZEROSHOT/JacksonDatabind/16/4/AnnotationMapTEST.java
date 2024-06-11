package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {

 private AnnotationMap createAnnotationMap() {
 return new AnnotationMap();
 }

 @Test
 public void testEmptyAnnotationMap() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertEquals(0, annotationMap.size());
 assertNull(annotationMap.get(JsonInclude.class));
 }

 @Test
 public void testGetAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new JsonInclude(JsonInclude.Include.NON_NULL));
 Annotation annotation = annotationMap.get(JsonInclude.class);
 assertEquals(JsonInclude.Include.NON_NULL, annotation.annotationType().getMemberValues()[0].getDefaultValue());
 }

 @Test
 public void testGetAnnotationNull() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNull(annotationMap.get(JsonProperty.class));
 }

 @Test
 public void testAnnotations() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new JsonInclude(JsonInclude.Include.NON_NULL));
 List<Annotation> annotations = Arrays.asList(new JsonInclude(JsonInclude.Include.NON_NULL));
 assertEquals(annotations, annotationMap.annotations());
 }

 @Test
 public void testEmptyAnnotations() {
 AnnotationMap annotationMap = createAnnotationMap();
 List<Annotation> annotations = Collections.emptyList();
 assertEquals(annotations, annotationMap.annotations());
 }

 @Test
 public void testMergeSameAnnotations() {
 AnnotationMap annotationMap1 = createAnnotationMap();
 annotationMap1.add(new JsonInclude(JsonInclude.Include.NON_NULL));
 AnnotationMap annotationMap2 = createAnnotationMap();
 annotationMap2.add(new JsonInclude(JsonInclude.Include.NON_NULL));
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
 assertEquals(1, mergedAnnotationMap.size());
 assertTrue(mergedAnnotationMap.containsKey(JsonInclude.class));
 }

 @Test
 public void testMergeDifferentAnnotations() {
 AnnotationMap annotationMap1 = createAnnotationMap();
 annotationMap1.add(new JsonInclude(JsonInclude.Include.NON_NULL));
 AnnotationMap annotationMap2 = createAnnotationMap();
 annotationMap2.add(new JsonProperty("name"));
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
 assertEquals(2, mergedAnnotationMap.size());
 assertTrue(mergedAnnotationMap.containsKey(JsonInclude.class));
 assertTrue(mergedAnnotationMap.containsKey(JsonProperty.class));
 }

 @Test
 public void testMergeNullAnnotationMap() {
 AnnotationMap annotationMap1 = createAnnotationMap();
 annotationMap1.add(new JsonInclude(JsonInclude.Include.NON_NULL));
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, null);
 assertEquals(1, mergedAnnotationMap.size());
 assertTrue(mergedAnnotationMap.containsKey(JsonInclude.class));
 }

 @Test
 public void testMergeEmptyAnnotationMap() {
 AnnotationMap annotationMap1 = createAnnotationMap();
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, createAnnotationMap());
 assertEquals(0, mergedAnnotationMap.size());
 }
}