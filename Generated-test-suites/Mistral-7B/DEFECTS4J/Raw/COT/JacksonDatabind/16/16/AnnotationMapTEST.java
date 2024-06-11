package com.example.annotationmap; // replace with the actual package name

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
 assertEquals(annotation, annotationMap.get(MyAnnotation.class));
 }

 @Test
 public void testGetNullAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNull(annotationMap.get(String.class));
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
 public void testMergeEmptyAnnotationMaps() {
 AnnotationMap primary = createAnnotationMap();
 AnnotationMap secondary = createAnnotationMap();
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
 assertEquals(primary, mergedAnnotationMap);
 }

 @Test
 public void testMergeAnnotationWithSameType() {
 AnnotationMap primary = createAnnotationMap();
 AnnotationMap secondary = createAnnotationMap();
 Annotation annotation = new MyAnnotation();
 primary.add(annotation);
 secondary.add(annotation);
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
 assertEquals(annotation, mergedAnnotationMap.get(MyAnnotation.class));
 }

 @Test
 public void testMergeAnnotationWithDifferentType() {
 AnnotationMap primary = createAnnotationMap();
 AnnotationMap secondary = createAnnotationMap();
 Annotation annotation1 = new MyAnnotation();
 primary.add(annotation1);
 Annotation annotation2 = new DifferentAnnotation();
 secondary.add(annotation2);
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
 assertEquals(annotation1, mergedAnnotationMap.get(MyAnnotation.class));
 assertEquals(annotation2, mergedAnnotationMap.get(DifferentAnnotation.class));
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
 public void testAddAnnotationIfNotPresent() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation = new MyAnnotation();
 assertFalse(annotationMap.addIfNotPresent(annotation));
 annotationMap.add(annotation);
 assertTrue(annotationMap.addIfNotPresent(annotation));
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(null);
 }

 @Test
 public void testToString() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation1 = new MyAnnotation();
 Annotation annotation2 = new MyAnnotation();
 annotationMap.add(annotation1);
 annotationMap.add(annotation2);
 String expected = "{" +
 "  AnnotationMap$AnnotationMapElement{class=[class com.example.AnnotationMapTest$MyAnnotation], annotation={@com.example.AnnotationMapTest$MyAnnotation()}}," +
 "  AnnotationMap$AnnotationMapElement{class=[class com.example.AnnotationMapTest$MyAnnotation], annotation={@com.example.AnnotationMapTest$MyAnnotation()}}" +
 "}";
 assertEquals(expected, annotationMap.toString());
 }
}