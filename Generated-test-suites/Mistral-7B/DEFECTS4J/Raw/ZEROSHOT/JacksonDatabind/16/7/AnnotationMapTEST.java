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

 private static <A extends Annotation> Annotation createAnnotation(Class<A> annotationType) {
 return (A) AnnotationConfiguration.createAnnotationInstance(annotationType);
 }

 @Test
 public void testCreateInstance() {
 AnnotationMap map = new AnnotationMap();
 Annotation ann1 = createAnnotation(MyAnnotation1.class);
 Annotation ann2 = createAnnotation(MyAnnotation2.class);

 map.add(ann1);
 map.add(ann2);

 assertEquals(2, map.size());
 }

 @Test
 public void testGetAnnotation() {
 AnnotationMap map = new AnnotationMap();
 Annotation ann1 = createAnnotation(MyAnnotation1.class);
 Annotation ann2 = createAnnotation(MyAnnotation2.class);

 map.add(ann1);
 map.add(ann2);

 assertEquals(ann1, map.get(MyAnnotation1.class));
 assertEquals(ann2, map.get(MyAnnotation2.class));
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullAnnotation() {
 AnnotationMap map = new AnnotationMap();
 assertNull(map.get(null));
 }

 @Test
 public void testGetAnnotationNotFound() {
 AnnotationMap map = new AnnotationMap();
 assertNull(map.get(MyOtherAnnotation.class));
 }

 @Test
 public void testAnnotations() {
 AnnotationMap map = new AnnotationMap();
 Annotation ann1 = createAnnotation(MyAnnotation1.class);
 Annotation ann2 = createAnnotation(MyAnnotation2.class);

 map.add(ann1);
 map.add(ann2);

 List<Annotation> annotations = new ArrayList<>(map.annotations());
 assertEquals(2, annotations.size());
 assertEquals(ann1, annotations.get(0));
 assertEquals(ann2, annotations.get(1));
 }

 @Test
 public void testEmptyAnnotations() {
 AnnotationMap map = new AnnotationMap();
 List<Annotation> annotations = new ArrayList<>(map.annotations());
 assertEquals(0, annotations.size());
 }

 @Test
 public void testMergeNullPrimary() {
 AnnotationMap primary = null;
 AnnotationMap secondary = new AnnotationMap();
 Annotation ann1 = createAnnotation(MyAnnotation1.class);
 Annotation ann2 = createAnnotation(MyAnnotation2.class);

 secondary.add(ann1);
 secondary.add(ann2);

 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(secondary, merged);
 }

 @Test
 public void testMergeNullSecondary() {
 AnnotationMap primary = new AnnotationMap();
 AnnotationMap secondary = null;
 Annotation ann1 = createAnnotation(MyAnnotation1.class);
 Annotation ann2 = createAnnotation(MyAnnotation2.class);

 primary.add(ann1);
 primary.add(ann2);

 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(primary, merged);
 }

 @Test
 public void testMergeSameAnnotations() {
 AnnotationMap primary = new AnnotationMap();
 AnnotationMap secondary = new AnnotationMap();
 Annotation ann1 = createAnnotation(MyAnnotation1.class);
 Annotation ann2 = createAnnotation(MyAnnotation2.class);

 primary.add(ann1);
 secondary.add(ann1);
 secondary.add(ann2);

 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(3, merged.size());
 assertEquals(ann1, merged.get(MyAnnotation1.class));
 assertEquals(ann2, merged.get(MyAnnotation2.class));
 }

 @Test
 public void testMergeDifferentAnnotations() {
 AnnotationMap primary = new AnnotationMap();
 AnnotationMap secondary = new AnnotationMap();
 Annotation ann1 = createAnnotation(MyAnnotation1.class);
 Annotation ann2 = createAnnotation(MyAnnotation2.class);

 primary.add(ann1);
 secondary.add(ann2);

 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(2, merged.size());
 assertEquals(ann1, merged.get(MyAnnotation1.class));
 assertEquals(ann2, merged.get(MyAnnotation2.class));
 }

 @Test
 public void testMergeEmptySecondary() {
 AnnotationMap primary = new AnnotationMap();
 AnnotationMap secondary = new AnnotationMap();
 Annotation ann1 = createAnnotation(MyAnnotation1.class);
 Annotation ann2 = createAnnotation(MyAnnotation2.class);

 primary.add(ann1);
 primary.add(ann2);

 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(2, merged.size());
 assertEquals(ann1, merged.get(MyAnnotation1.class));
 assertEquals(ann2, merged.get(MyAnnotation2.class));
 }

 @Test
 public void testMergeEmptyPrimary() {
 AnnotationMap primary = new AnnotationMap();
 AnnotationMap secondary = new AnnotationMap();
 Annotation ann1 = createAnnotation(MyAnnotation1.class);
 Annotation ann2 = createAnnotation(MyAnnotation2.class);

 secondary.add(ann1);
 secondary.add(ann2);

 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(2, merged.size());
 assertEquals(ann1, merged.get(MyAnnotation1.class));
 assertEquals(ann2, merged.get(MyAnnotation2.class));
 }

 @Test
 public void testAddIfNotPresent() {
 AnnotationMap map = new AnnotationMap();
 Annotation ann1 = createAnnotation(MyAnnotation1.class);
 Annotation ann2 = createAnnotation(MyAnnotation2.class);

 assertFalse(map.addIfNotPresent(ann1));
 assertTrue(map.addIfNotPresent(ann2));
 assertEquals(1, map.size());
 }

 @Test
 public void testAdd() {
 AnnotationMap map = new AnnotationMap();
 Annotation ann1 = createAnnotation(MyAnnotation1.class);
 Annotation ann2 = createAnnotation(MyAnnotation2.class);

 assertTrue(map.add(ann1));
 assertEquals(1, map.size());
 assertTrue(map.add(ann2));
 assertEquals(2, map.size());
 }

 @Test
 public void testToString() {
 AnnotationMap map = new AnnotationMap();
 Annotation ann1 = createAnnotation(MyAnnotation1.class);
 Annotation ann2 = createAnnotation(MyAnnotation2.class);

 map.add(ann1);
 map.add(ann2);

 assertEquals("{@com.fasterxml.jackson.databind.introspect.MyAnnotation1 class com.fasterxml.jackson.databind.introspect.MyAnnotation2 class}", map.toString());
 }
}