package com.example.junit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

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
 public void testConstructor() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNull(annotationMap.get(Object.class));
 assertEquals(0, annotationMap.size());
 }

 @Test
 public void testGetAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNull(annotationMap.get(Object.class));
 annotationMap.add(new MyAnnotation());
 Annotation myAnnotation = annotationMap.get(MyAnnotation.class);
 assertSame(myAnnotation, annotationMap.get(MyAnnotation.class));
 }

 @Test
 public void testAnnotations() {
 AnnotationMap annotationMap = createAnnotationMap();
 List<Annotation> emptyList = Collections.emptyList();
 assertSame(emptyList, annotationMap.annotations());
 annotationMap.add(new MyAnnotation());
 List<Annotation> annotations = Arrays.asList(new MyAnnotation());
 assertSame(annotations, annotationMap.annotations());
 }

 @Test
 public void testMergeEmptyAnnotationMap() {
 AnnotationMap primary = createAnnotationMap();
 AnnotationMap secondary = createAnnotationMap();
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
 assertSame(primary, mergedAnnotationMap);
 }

 @Test
 public void testMergeNonEmptyPrimaryAnnotationMap() {
 AnnotationMap primary = createAnnotationMap();
 primary.add(new MyAnnotation());
 AnnotationMap secondary = createAnnotationMap();
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
 Annotation myAnnotation = new MyAnnotation();
 assertTrue(mergedAnnotationMap.addIfNotPresent(myAnnotation));
 assertEquals(2, mergedAnnotationMap.size());
 Annotation annInPrimary = primary.get(MyAnnotation.class);
 assertSame(annInPrimary, mergedAnnotationMap.get(MyAnnotation.class));
 }

 @Test
 public void testMergeNonEmptySecondaryAnnotationMap() {
 AnnotationMap primary = createAnnotationMap();
 AnnotationMap secondary = createAnnotationMap();
 secondary.add(new MyAnnotation());
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
 assertFalse(primary.containsKey(MyAnnotation.class));
 assertSame(secondary.get(MyAnnotation.class), mergedAnnotationMap.get(MyAnnotation.class));
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullAnnotationClass() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.get(null);
 }

 @Test
 public void testAddNullAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertFalse(annotationMap.addIfNotPresent(null));
 assertFalse(annotationMap.add(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAnnotationWithNullAnnotationType() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation = new MyAnnotation();
 annotationMap.add(null);
 }

 @Test
 public void testSizeWithEmptyAnnotationMap() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertEquals(0, annotationMap.size());
 }

 @Test
 public void testSizeWithNonEmptyAnnotationMap() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new MyAnnotation());
 assertEquals(1, annotationMap.size());
 }

 @Test
 public void testAddSameAnnotationTwice() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new MyAnnotation());
 assertTrue(annotationMap.add(new MyAnnotation()));
 assertEquals(1, annotationMap.size());
 }

 @Test
 public void testToString() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new MyAnnotation());
 assertEquals("{class com.example.MyAnnotation=Annotation@41102892}", annotationMap.toString());
 }

}