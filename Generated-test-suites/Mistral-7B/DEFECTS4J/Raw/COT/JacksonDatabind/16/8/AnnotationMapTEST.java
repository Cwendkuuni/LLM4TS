package com.example.annotationmap; // replace with your package name

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {

 private AnnotationMap createAnnotationMap() {
 return new AnnotationMap();
 }

 @Test
 public void testConstructor() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNull(annotationMap.get(NullPointerAnnotation.class));
 assertFalse(annotationMap.isEmpty());
 }

 @Test
 public void testGetNullAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNull(annotationMap.get(NullPointerAnnotation.class));
 }

 @Test(expected = NullPointerException.class)
 public void testGetAnnotationWithNullClass() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNull(annotationMap.get(null));
 }

 @Test
 public void testAnnotations() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new MyAnnotation());
 Set<Annotation> annotations = new HashSet<>(annotationMap.annotations());
 assertTrue(annotations.contains(annotationMap.get(MyAnnotation.class)));
 }

 @Test
 public void testMergeEmptyPrimaryAnnotationMap() {
 AnnotationMap primary = createAnnotationMap();
 AnnotationMap secondary = createAnnotationMap();
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(primary, merged);
 }

 @Test
 public void testMergeEmptySecondaryAnnotationMap() {
 AnnotationMap primary = createAnnotationMap();
 AnnotationMap secondary = createAnnotationMap();
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(secondary, merged);
 }

 @Test
 public void testMergeAnnotationMapsWithSameAnnotations() {
 AnnotationMap primary = new AnnotationMap();
 primary.add(new MyAnnotation());
 AnnotationMap secondary = new AnnotationMap();
 secondary.add(new MyAnnotation());
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(primary, merged);
 }

 @Test
 public void testMergeAnnotationMapsWithDifferentAnnotations() {
 AnnotationMap primary = new AnnotationMap();
 primary.add(new MyAnnotation());
 AnnotationMap secondary = new AnnotationMap();
 secondary.add(new OtherAnnotation());
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertNotEquals(primary, merged);
 assertTrue(merged.containsKey(MyAnnotation.class));
 assertTrue(merged.containsKey(OtherAnnotation.class));
 }

 @Test
 public void testMergeAnnotationMapsWithSameAnnotationsDifferentInstances() {
 AnnotationMap primary = new AnnotationMap();
 primary.add(new MyAnnotation());
 AnnotationMap secondary = new AnnotationMap();
 secondary.add(new MyAnnotation());
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(primary, merged);
 }

 @Test
 public void testMergeAnnotationMapsWithSameAnnotationInstances() {
 AnnotationMap primary = new AnnotationMap();
 primary.add(new MyAnnotation());
 AnnotationMap secondary = new AnnotationMap();
 secondary.add(primary.get(MyAnnotation.class));
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(primary, merged);
 }

 @Test
 public void testSize() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new MyAnnotation());
 annotationMap.add(new OtherAnnotation());
 assertEquals(2, annotationMap.size());
 }

 @Test
 public void testAddIfNotPresent() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertFalse(annotationMap.addIfNotPresent(new MyAnnotation()));
 assertTrue(annotationMap.addIfNotPresent(new OtherAnnotation()));
 }

 @Test
 public void testAdd() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation annotation = new MyAnnotation();
 assertFalse(annotationMap.add(annotation));
 annotationMap.addIfNotPresent(annotation);
 assertTrue(annotationMap.add(annotation));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(null);
 }

 @Test
 public void testToString() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new MyAnnotation());
 annotationMap.add(new OtherAnnotation());
 String expected = "{" +
 "  AnnotationMap@6d61f321[annotations=[" +
 "    com.example.annotationmap.MyAnnotation@3273e1a2," +
 "    com.example.annotationmap.OtherAnnotation@18b4e58d]]" +
 "}";
 assertEquals(expected, annotationMap.toString());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemove() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new MyAnnotation());
 annotationMap.remove(MyAnnotation.class);
 }

 @Test
 public void testClear() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new MyAnnotation());
 annotationMap.clear();
 assertFalse(annotationMap.containsKey(MyAnnotation.class));
 }

 @Test
 public void testClone() {
 AnnotationMap originalAnnotationMap = createAnnotationMap();
 originalAnnotationMap.add(new MyAnnotation());
 AnnotationMap clonedAnnotationMap = (AnnotationMap) originalAnnotationMap.clone();
 assertNotEquals(originalAnnotationMap, clonedAnnotationMap);
 assertEquals(originalAnnotationMap, clonedAnnotationMap);
 }

 @Test
 public void testHashCode() {
 AnnotationMap annotationMap1 = createAnnotationMap();
 AnnotationMap annotationMap2 = createAnnotationMap();
 annotationMap1.add(new MyAnnotation());
 annotationMap2.add(new MyAnnotation());
 assertEquals(annotationMap1.hashCode(), annotationMap2.hashCode());
 }

 @Test
 public void testEquals() {
 AnnotationMap annotationMap1 = createAnnotationMap();
 AnnotationMap annotationMap2 = createAnnotationMap();
 AnnotationMap annotationMap3 = new AnnotationMap(annotationMap1);
 assertTrue(annotationMap1.equals(annotationMap1));
 assertTrue(annotationMap1.equals(annotationMap3));
 assertFalse(annotationMap1.equals(annotationMap2));
 }

 @Test
 public void testEqualsWithNullObject() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertFalse(annotationMap.equals(null));
 }

 @Test
 public void testCloneWithNullAnnotationMap() {
 AnnotationMap annotationMap = createAnnotationMap();
 try {
 AnnotationMap clonedAnnotationMap = (AnnotationMap) annotationMap.clone();
 fail("Expected CloneNotSupportedException");
 } catch (CloneNotSupportedException e) {
 // Expected
 }
 }

 @Test
 public void testMergeWithNullPrimaryAnnotationMap() {
 AnnotationMap secondaryAnnotationMap = createAnnotationMap();
 try {
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(null, secondaryAnnotationMap);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testMergeWithNullSecondaryAnnotationMap() {
 AnnotationMap primaryAnnotationMap = createAnnotationMap();
 try {
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, null);
 fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testGetAnnotationWithInvalidClass() {
 AnnotationMap annotationMap = createAnnotationMap();
 try {
 annotationMap.get(Object.class);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }
}