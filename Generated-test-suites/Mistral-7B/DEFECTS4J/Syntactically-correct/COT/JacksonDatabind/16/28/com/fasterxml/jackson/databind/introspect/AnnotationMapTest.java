package com.yourpackage.test;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
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
 public void testConstructor() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNull(annotationMap.get(NullPointerTest.class));
 }

 @Test
 public void testGetAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new NullPointerTest());
 Annotation annotation = annotationMap.get(NullPointerTest.class);
 assertNotNull(annotation);
 }

 @Test
 public void testGetNullAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNull(annotationMap.get(NullPointerTest.class));
 }

 @Test
 public void testAnnotations() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new NullPointerTest());
 List<Annotation> annotations = new ArrayList<Annotation>(annotationMap.annotations());
 assertEquals(1, annotations.size());
 assertTrue(annotations.contains(new NullPointerTest()));
 }

 @Test
 public void testEmptyAnnotations() {
 AnnotationMap annotationMap = createAnnotationMap();
 List<Annotation> annotations = new ArrayList<Annotation>(annotationMap.annotations());
 assertEquals(0, annotations.size());
 }

 @Test
 public void testMergeNullPrimaryAnnotationMap() {
 AnnotationMap primaryAnnotationMap = null;
 AnnotationMap secondaryAnnotationMap = createAnnotationMap();
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertEquals(secondaryAnnotationMap, mergedAnnotationMap);
 }

 @Test
 public void testMergeNullSecondaryAnnotationMap() {
 AnnotationMap primaryAnnotationMap = createAnnotationMap();
 AnnotationMap secondaryAnnotationMap = null;
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertEquals(primaryAnnotationMap, mergedAnnotationMap);
 }

 @Test
 public void testMergeEmptyPrimaryAnnotationMap() {
 AnnotationMap primaryAnnotationMap = createAnnotationMap();
 AnnotationMap secondaryAnnotationMap = createAnnotationMap();
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertEquals(secondaryAnnotationMap, mergedAnnotationMap);
 }

 @Test
 public void testMergeEmptySecondaryAnnotationMap() {
 AnnotationMap primaryAnnotationMap = createAnnotationMap();
 AnnotationMap secondaryAnnotationMap = createAnnotationMap();
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertEquals(primaryAnnotationMap, mergedAnnotationMap);
 }

 @Test
 public void testMergeSameAnnotations() {
 AnnotationMap primaryAnnotationMap = createAnnotationMap();
 primaryAnnotationMap.add(new NullPointerTest());
 AnnotationMap secondaryAnnotationMap = createAnnotationMap();
 secondaryAnnotationMap.add(new NullPointerTest());
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertEquals(2, mergedAnnotationMap.size());
 assertTrue(mergedAnnotationMap.containsKey(NullPointerTest.class));
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(null);
 }

 @Test
 public void testSizeZeroAnnotationMap() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertEquals(0, annotationMap.size());
 }

 @Test
 public void testSizeOneAnnotationMap() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new NullPointerTest());
 assertEquals(1, annotationMap.size());
 }

 @Test
 public void testSizeTwoAnnotationMap() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new NullPointerTest());
 annotationMap.add(new NullPointerTest());
 assertEquals(2, annotationMap.size());
 }

 @Test
 public void testToString() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new NullPointerTest());
 String str = annotationMap.toString();
 assertTrue(str.contains("class [Ljava.lang.annotation.Annotation;"));
 }
}