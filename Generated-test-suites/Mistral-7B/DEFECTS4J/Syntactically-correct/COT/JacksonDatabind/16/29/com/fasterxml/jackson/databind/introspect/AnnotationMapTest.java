package com.example.junit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {

 private AnnotationMap createAnnotationMap() {
 return new AnnotationMap();
 }

 @Test
 public void testConstructor() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNotNull(annotationMap);
 }

 @Test
 public void testGetAnnotationNullMap() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNull(annotationMap.get(Annotation.class));
 }

 @Test
 public void testGetAnnotationPresent() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation = new Annotation() {
 };
 annotationMap.add(annotation);
 assertEquals(annotation, annotationMap.get(annotation.annotationType()));
 }

 @Test
 public void testGetAnnotationDifferentType() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation1 = new Annotation1();
 Annotation annotation2 = new Annotation2();
 annotationMap.add(annotation1);
 assertNull(annotationMap.get(annotation2.annotationType()));
 }

 @Test
 public void testAnnotationsEmptyList() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertTrue(annotationMap.annotations().isEmpty());
 }

 @Test
 public void testAnnotationsListWithAnnotations() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation1 = new Annotation1();
 Annotation annotation2 = new Annotation2();
 annotationMap.add(annotation1);
 annotationMap.add(annotation2);
 List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
 assertEquals(2, annotations.size());
 assertEquals(annotation1, annotations.get(0));
 assertEquals(annotation2, annotations.get(1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnnotationsUnmodifiableList() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation = new Annotation();
 annotationMap.add(annotation);
 List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
 annotations.clear();
 }

 @Test
 public void testSizeZero() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertEquals(0, annotationMap.size());
 }

 @Test
 public void testSizeOne() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation = new Annotation();
 annotationMap.add(annotation);
 assertEquals(1, annotationMap.size());
 }

 @Test
 public void testSizeMultiple() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation1 = new Annotation1();
 Annotation annotation2 = new Annotation2();
 annotationMap.add(annotation1);
 annotationMap.add(annotation2);
 assertEquals(2, annotationMap.size());
 }

 @Test
 public void testAddIfNotPresentTrue() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation = new Annotation();
 assertTrue(annotationMap.addIfNotPresent(annotation));
 }

 @Test
 public void testAddIfNotPresentFalse() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation = new Annotation();
 annotationMap.add(annotation);
 assertFalse(annotationMap.addIfNotPresent(annotation));
 }

 @Test
 public void testAddTrue() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation = new Annotation();
 assertTrue(annotationMap.add(annotation));
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertFalse(annotationMap.add(null));
 }

 @Test
 public void testMergeNullPrimary() {
 AnnotationMap primary = null;
 AnnotationMap secondary = createAnnotationMap();
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(secondary, merged);
 }

 @Test
 public void testMergeEmptyPrimary() {
 AnnotationMap primary = new AnnotationMap();
 AnnotationMap secondary = createAnnotationMap();
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(secondary, merged);
 }

 @Test
 public void testMergeEmptySecondary() {
 AnnotationMap primary = createAnnotationMap();
 AnnotationMap secondary = new AnnotationMap();
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(primary, merged);
 }

 @Test
 public void testMergeSameAnnotations() {
 AnnotationMap primary = createAnnotationMap();
 Annotation annotation = new Annotation();
 primary.add(annotation);
 AnnotationMap secondary = createAnnotationMap();
 secondary.add(annotation);
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(primary, merged);
 }

 @Test
 public void testMergeDifferentAnnotationsPrimaryFirst() {
 AnnotationMap primary = createAnnotationMap();
 Annotation annotation1 = new Annotation1();
 primary.add(annotation1);
 AnnotationMap secondary = createAnnotationMap();
 Annotation annotation2 = new Annotation2();
 secondary.add(annotation2);
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(primary, merged);
 }

 @Test
 public void testMergeDifferentAnnotationsSecondaryFirst() {
 AnnotationMap primary = createAnnotationMap();
 Annotation annotation1 = new Annotation1();
 primary.add(annotation1);
 AnnotationMap secondary = createAnnotationMap();
 Annotation annotation2 = new Annotation2();
 secondary.add(annotation2);
 AnnotationMap merged = AnnotationMap.merge(secondary, primary);
 assertEquals(secondary, merged);
 }

 @Test
 public void testMergeSameAnnotationsDifferentOrder() {
 AnnotationMap primary = createAnnotationMap();
 Annotation annotation = new Annotation();
 primary.add(annotation);
 AnnotationMap secondary = createAnnotationMap();
 secondary.add(annotation);
 AnnotationMap merged = AnnotationMap.merge(secondary, primary);
 assertEquals(primary, merged);
 }

 @Test
 public void testToStringEmptyMap() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertEquals("[null]", annotationMap.toString());
 }

 @Test
 public void testToStringMapWithAnnotations() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation1 = new Annotation1();
 Annotation annotation2 = new Annotation2();
 annotationMap.add(annotation1);
 annotationMap.add(annotation2);
 assertEquals("[Ljava.lang.Class;@7852f142, Ljava.lang.Class;@16a2d842]", annotationMap.toString());
 }

}