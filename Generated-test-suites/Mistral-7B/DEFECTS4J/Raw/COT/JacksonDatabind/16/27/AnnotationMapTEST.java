package com.yourpackage.test;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterable;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOClass;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {

 private AnnotationMap createAnnotationMap() {
 return new AnnotationMap();
 }

 @Test
 public void testAnnotationMapConstructor() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNull(annotationMap.get(JsonDeserialize.class));
 assertNull(annotationMap.get(JsonPOJOClass.class));
 assertEquals(0, annotationMap.size());
 }

 @Test
 public void testGetAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 JsonDeserialize jsonDeserialize = new JsonDeserialize();
 annotationMap.add(jsonDeserialize);
 Annotation gotAnnotation = annotationMap.get(JsonDeserialize.class);
 assertEquals(jsonDeserialize, gotAnnotation);
 }

 @Test
 public void testGetAnnotationNull() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNull(annotationMap.get(JsonSerialize.class));
 }

 @Test
 public void testAnnotations() {
 AnnotationMap annotationMap = createAnnotationMap();
 JsonDeserialize jsonDeserialize = new JsonDeserialize();
 JsonSerialize jsonSerialize = new JsonSerialize();
 annotationMap.add(jsonDeserialize);
 annotationMap.add(jsonSerialize);
 Iterable<Annotation> annotations = annotationMap.annotations();
 Set<Class<? extends Annotation>> expectedClasses = new HashSet<>();
 expectedClasses.add(JsonDeserialize.class);
 expectedClasses.add(JsonSerialize.class);
 for (Annotation annotation : annotations) {
 Class<? extends Annotation> annotationType = annotation.annotationType();
 assertTrue(expectedClasses.contains(annotationType));
 }
 }

 @Test
 public void testMergeEmptyAnnotationMap() {
 AnnotationMap primaryAnnotationMap = createAnnotationMap();
 AnnotationMap secondaryAnnotationMap = createAnnotationMap();
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertEquals(primaryAnnotationMap, mergedAnnotationMap);
 }

 @Test
 public void testMergeNonEmptyAnnotationMap() {
 AnnotationMap primaryAnnotationMap = createAnnotationMap();
 JsonDeserialize jsonDeserialize1 = new JsonDeserialize();
 primaryAnnotationMap.add(jsonDeserialize1);
 AnnotationMap secondaryAnnotationMap = createAnnotationMap();
 JsonDeserialize jsonDeserialize2 = new JsonDeserialize();
 secondaryAnnotationMap.add(jsonDeserialize2);
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 AnnotationMap expectedMergedAnnotationMap = createAnnotationMap();
 expectedMergedAnnotationMap.add(jsonDeserialize1);
 expectedMergedAnnotationMap.add(jsonDeserialize2);
 assertEquals(expectedMergedAnnotationMap, mergedAnnotationMap);
 }

 @Test(expected = NullPointerException.class)
 public void testMergeNullPrimaryAnnotationMap() {
 AnnotationMap secondaryAnnotationMap = createAnnotationMap();
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(null, secondaryAnnotationMap);
 }

 @Test(expected = NullPointerException.class)
 public void testMergeNullSecondaryAnnotationMap() {
 AnnotationMap primaryAnnotationMap = createAnnotationMap();
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, null);
 }

 @Test
 public void testSize() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertEquals(0, annotationMap.size());
 JsonDeserialize jsonDeserialize = new JsonDeserialize();
 annotationMap.add(jsonDeserialize);
 assertEquals(1, annotationMap.size());
 }

 @Test
 public void testAddIfNotPresent() {
 AnnotationMap annotationMap = createAnnotationMap();
 JsonDeserialize jsonDeserialize1 = new JsonDeserialize();
 assertTrue(annotationMap.addIfNotPresent(jsonDeserialize1));
 JsonDeserialize jsonDeserialize2 = new JsonDeserialize();
 assertFalse(annotationMap.addIfNotPresent(jsonDeserialize2));
 }

 @Test
 public void testAdd() {
 AnnotationMap annotationMap = createAnnotationMap();
 JsonDeserialize jsonDeserialize1 = new JsonDeserialize();
 assertTrue(annotationMap.add(jsonDeserialize1));
 JsonDeserialize jsonDeserialize2 = new JsonDeserialize();
 assertTrue(annotationMap.add(jsonDeserialize2));
 }

 @Test
 public void testToString() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertEquals("[]", annotationMap.toString());
 JsonDeserialize jsonDeserialize = new JsonDeserialize();
 annotationMap.add(jsonDeserialize);
 assertEquals("[Lcom.fasterxml.jackson.databind.annotation.JsonDeserialize;@7f5840a2]", annotationMap.toString());
 }

}