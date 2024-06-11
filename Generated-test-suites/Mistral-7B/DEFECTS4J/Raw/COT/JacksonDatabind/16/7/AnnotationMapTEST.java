package com.yourpackage; // replace with your test package name

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.annotation.*;

public class AnnotationMapTest {

 private AnnotationMap createAnnotationMap() {
 return new AnnotationMap();
 }

 // Test constructor
 @Test
 public void testAnnotationMapConstructor() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNull(annotationMap.get(JsonValueClass.class));
 }

 // Test get method
 @Test
 public void testGetMethod() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new JsonValueAnnotation());
 Annotation jsonValueAnnotation = annotationMap.get(JsonValueAnnotation.class);
 assertNotNull(jsonValueAnnotation);
 assertSame(jsonValueAnnotation, annotationMap.get(JsonValueAnnotation.class));
 }

 // Test annotations method
 @Test
 public void testAnnotationsMethod() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new JsonValueAnnotation());
 Iterable<Annotation> annotations = annotationMap.annotations();
 assertTrue(annotations.iterator().hasNext());
 Annotation jsonValueAnnotation = annotations.iterator().next();
 assertSame(jsonValueAnnotation, annotationMap.get(JsonValueAnnotation.class));
 }

 // Test merge method with empty maps
 @Test
 public void testMergeEmptyMaps() {
 AnnotationMap primaryAnnotationMap = createAnnotationMap();
 AnnotationMap secondaryAnnotationMap = createAnnotationMap();
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertTrue(mergedAnnotationMap.size() == 0);
 }

 // Test merge method with one annotation in primary map
 @Test
 public void testMergeOneAnnotationPrimary() {
 AnnotationMap primaryAnnotationMap = createAnnotationMap();
 primaryAnnotationMap.add(new JsonValueAnnotation());
 AnnotationMap secondaryAnnotationMap = createAnnotationMap();
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertEquals(1, mergedAnnotationMap.size());
 Annotation jsonValueAnnotation = mergedAnnotationMap.get(JsonValueAnnotation.class);
 assertSame(jsonValueAnnotation, primaryAnnotationMap.get(JsonValueAnnotation.class));
 }

 // Test merge method with one annotation in secondary map
 @Test
 public void testMergeOneAnnotationSecondary() {
 AnnotationMap primaryAnnotationMap = createAnnotationMap();
 AnnotationMap secondaryAnnotationMap = createAnnotationMap();
 secondaryAnnotationMap.add(new JsonValueAnnotation());
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertEquals(1, mergedAnnotationMap.size());
 Annotation jsonValueAnnotation = mergedAnnotationMap.get(JsonValueAnnotation.class);
 assertSame(jsonValueAnnotation, secondaryAnnotationMap.get(JsonValueAnnotation.class));
 }

 // Test merge method with multiple annotations in both maps
 @Test
 public void testMergeMultipleAnnotations() {
 AnnotationMap primaryAnnotationMap = createAnnotationMap();
 primaryAnnotationMap.add(new JsonValueAnnotation());
 primaryAnnotationMap.add(new JsonIncludeAnnotation());
 AnnotationMap secondaryAnnotationMap = createAnnotationMap();
 secondaryAnnotationMap.add(new JsonValueAnnotation());
 secondaryAnnotationMap.add(new JsonIncludeAnnotation());
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertEquals(4, mergedAnnotationMap.size());
 Annotation jsonValueAnnotation1 = mergedAnnotationMap.get(JsonValueAnnotation.class);
 assertSame(jsonValueAnnotation1, primaryAnnotationMap.get(JsonValueAnnotation.class));
 Annotation jsonValueAnnotation2 = mergedAnnotationMap.get(JsonValueAnnotation.class);
 assertNotSame(jsonValueAnnotation1, jsonValueAnnotation2);
 Annotation jsonIncludeAnnotation1 = mergedAnnotationMap.get(JsonIncludeAnnotation.class);
 assertSame(jsonIncludeAnnotation1, primaryAnnotationMap.get(JsonIncludeAnnotation.class));
 Annotation jsonIncludeAnnotation2 = mergedAnnotationMap.get(JsonIncludeAnnotation.class);
 assertNotSame(jsonIncludeAnnotation1, jsonIncludeAnnotation2);
 }

 // Test addIfNotPresent method
 @Test
 public void testAddIfNotPresentMethod() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation jsonValueAnnotation = new JsonValueAnnotation();
 assertTrue(annotationMap.addIfNotPresent(jsonValueAnnotation));
 assertSame(jsonValueAnnotation, annotationMap.get(JsonValueAnnotation.class));
 }

 // Test add method
 @Test
 public void testAddMethod() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation jsonValueAnnotation = new JsonValueAnnotation();
 assertTrue(annotationMap.add(jsonValueAnnotation));
 assertSame(jsonValueAnnotation, annotationMap.get(JsonValueAnnotation.class));
 }

 // Test add method with existing annotation
 @Test(expected = IllegalStateException.class)
 public void testAddMethodWithExistingAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation jsonValueAnnotation = new JsonValueAnnotation();
 annotationMap.add(jsonValueAnnotation);
 try {
 annotationMap.add(jsonValueAnnotation);
 } catch (IllegalStateException e) {
 assertEquals("Annotation [" + jsonValueAnnotation.annotationType() + "] already present", e.getMessage());
 }
 }

 // Test size method
 @Test
 public void testSizeMethod() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertEquals(0, annotationMap.size());
 AnnotationMap annotationMapWithAnnotations = createAnnotationMap();
 annotationMapWithAnnotations.add(new JsonValueAnnotation());
 annotationMapWithAnnotations.add(new JsonIncludeAnnotation());
 assertEquals(2, annotationMapWithAnnotations.size());
 }

 // Test toString method
 @Test
 public void testToStringMethod() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertEquals("[]", annotationMap.toString());
 AnnotationMap annotationMapWithAnnotations = createAnnotationMap();
 annotationMapWithAnnotations.add(new JsonValueAnnotation());
 annotationMapWithAnnotations.add(new JsonIncludeAnnotation());
 assertEquals("[Annotation[" + JsonValueAnnotation.class.getName() + "], Annotation[" + JsonIncludeAnnotation.class.getName() + "]]", annotationMapWithAnnotations.toString());
 }

}