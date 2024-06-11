package com.example.annotationmap; //Replace "com.example" with the actual package name in your project

import static org.junit.Assert.*;

import java.lang.annotation.*;
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
 public void testGetAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation = new MyAnnotation();
 annotationMap.add(annotation);
 assertSame(annotation, annotationMap.get(MyAnnotation.class));
 }

 @Test(expected = NullPointerException.class)
 public void testGetAnnotationWithNullClass() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNull(annotationMap.get(null));
 }

 @Test
 public void testAnnotations() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation1 = new MyAnnotation();
 Annotation annotation2 = new MyAnnotation();
 annotationMap.add(annotation1);
 annotationMap.add(annotation2);
 List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
 assertEquals(2, annotations.size());
 assertEquals(annotation1, annotations.get(0));
 assertEquals(annotation2, annotations.get(1));
 }

 @Test
 public void testMerge() {
 AnnotationMap annotationMap1 = createAnnotationMap();
 Annotation annotation1 = new MyAnnotation();
 annotationMap1.add(annotation1);

 AnnotationMap annotationMap2 = createAnnotationMap();
 Annotation annotation2 = new MyAnnotation();
 annotationMap2.add(annotation2);

 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
 List<Annotation> mergedAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
 assertEquals(2, mergedAnnotations.size());
 assertSame(annotation1, mergedAnnotations.get(0));
 assertSame(annotation2, mergedAnnotations.get(1));
 }

 @Test
 public void testSize() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertEquals(0, annotationMap.size());
 Annotation annotation = new MyAnnotation();
 annotationMap.add(annotation);
 assertEquals(1, annotationMap.size());
 }

 @Test
 public void testAddIfNotPresent() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation = new MyAnnotation();
 assertFalse(annotationMap.addIfNotPresent(annotation));
 annotationMap.clear();
 assertTrue(annotationMap.addIfNotPresent(annotation));
 }

 @Test
 public void testAdd() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation = new MyAnnotation();
 assertFalse(annotationMap.add(annotation));
 annotationMap.clear();
 assertTrue(annotationMap.add(annotation));
 }

 @Test
 public void testToString() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation1 = new MyAnnotation();
 Annotation annotation2 = new MyAnnotation();
 annotationMap.add(annotation1);
 annotationMap.add(annotation2);
 String expectedToString = "{" +
 "  " + MyAnnotation.class.getName() + ": [" +
 "    " + annotation1 + ", " +
 "    " + annotation2 +
 "  ]" +
 "}";
 assertEquals(expectedToString, annotationMap.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation ann = null;
 annotationMap.add(ann);
 }

 @Test
 public void testContainsKey() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation = new MyAnnotation();
 annotationMap.add(annotation);
 assertTrue(annotationMap.containsKey(MyAnnotation.class));
 }

 @Test
 public void testContainsValue() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation = new MyAnnotation();
 annotationMap.add(annotation);
 assertTrue(annotationMap.containsValue(annotation));
 }

 @Test
 public void testIsEmpty() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertTrue(annotationMap.isEmpty());
 Annotation annotation = new MyAnnotation();
 annotationMap.add(annotation);
 assertFalse(annotationMap.isEmpty());
 }

 @Test
 public void testClear() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation = new MyAnnotation();
 annotationMap.add(annotation);
 assertFalse(annotationMap.isEmpty());
 annotationMap.clear();
 assertTrue(annotationMap.isEmpty());
 }

 @Test
 public void testRemove() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation = new MyAnnotation();
 annotationMap.add(annotation);
 assertFalse(annotationMap.isEmpty());
 annotationMap.remove(annotation.annotationType());
 assertTrue(annotationMap.isEmpty());
 }

 @Test
 public void testRemoveNullKey() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertFalse(annotationMap.remove(null));
 }

 @Test
 public void testRemoveNonExistentKey() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertFalse(annotationMap.remove(MyAnnotation2.class));
 }
}