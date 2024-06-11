package com.example.junit.test;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {

 private AnnotationMap createAnnotationMap() {
 return new AnnotationMap();
 }

 // Test constructor
 @Test
 public void testAnnotationMapConstructor() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNull(annotationMap.get(null));
 }

 // Test get method
 @Test
 public void testGetMethod() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new MyAnnotation());
 MyAnnotation myAnnotation = annotationMap.get(MyAnnotation.class);
 assertNotNull(myAnnotation);
 }

 // Test annotations method
 @Test
 public void testAnnotationsMethod() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new MyAnnotation1());
 annotationMap.add(new MyAnnotation2());
 List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
 assertEquals(2, annotations.size());
 }

 // Test merge method
 @Test
 public void testMergeMethod() {
 AnnotationMap primaryAnnotationMap = createAnnotationMap();
 AnnotationMap secondaryAnnotationMap = createAnnotationMap();
 primaryAnnotationMap.add(new MyAnnotation1());
 secondaryAnnotationMap.add(new MyAnnotation2());
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertEquals(2, mergedAnnotationMap.size());
 }

 // Test size method
 @Test
 public void testSizeMethod() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertEquals(0, annotationMap.size());
 annotationMap.add(new MyAnnotation());
 assertEquals(1, annotationMap.size());
 }

 // Test addIfNotPresent method
 @Test
 public void testAddIfNotPresentMethod() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertFalse(annotationMap.addIfNotPresent(new MyAnnotation()));
 annotationMap.add(null);
 assertTrue(annotationMap.addIfNotPresent(new MyAnnotation()));
 }

 // Test add method
 @Test
 public void testAddMethod() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertFalse(annotationMap.add(new MyAnnotation()));
 annotationMap.add(null);
 assertTrue(annotationMap.add(new MyAnnotation()));
 }

 // Test toString method
 @Test
 public void testToStringMethod() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new MyAnnotation());
 annotationMap.add(new MyAnnotation());
 String toString = annotationMap.toString();
 assertTrue(toString.contains("MyAnnotation"));
 }

 // Test edge cases: adding null
 @Test(expected = NullPointerException.class)
 public void testAddNullAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(null);
 }

 // Test edge cases: passing non-annotation class to get method
 @Test
 public void testGetMethodWithNonAnnotationClass() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNull(annotationMap.get(String.class));
 }

 // Test edge cases: passing non-existing annotation type to get method
 @Test
 public void testGetMethodWithNonExistingAnnotationType() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new MyAnnotation());
 assertNull(annotationMap.get(NonExistingAnnotation.class));
 }

 // Test edge cases: passing null to annotations method
 @Test
 public void testAnnotationsMethodWithNull() {
 AnnotationMap annotationMap = createAnnotationMap();
 List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
 assertEquals(0, annotations.size());
 }

 // Test exception handling: adding same annotation multiple times
 @Test(expected = IllegalStateException.class)
 public void testAddSameAnnotationTwice() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new MyAnnotation());
 annotationMap.add(new MyAnnotation());
 }

 // Test exception handling: adding null to merged map
 @Test(expected = NullPointerException.class)
 public void testMergeNullAnnotationMap() {
 AnnotationMap primaryAnnotationMap = createAnnotationMap();
 AnnotationMap secondaryAnnotationMap = new AnnotationMap();
 secondaryAnnotationMap.add(new MyAnnotation());
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 }
}