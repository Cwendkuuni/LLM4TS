package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.util.*;

import org.junit.*;

import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;

@SuppressWarnings("unchecked")
public class AnnotationMapTest {

 @BeforeClass
 public static void setup() {
 System.out.println("--- AnnotationMapTest.java ---");
 }

 @Test
 public void testconstructor() {
 AnnotationMap annotationMap = new AnnotationMap();
 assertNull(annotationMap.get(String.class.getAnnotation(Retarget.class)));
 }

 @Test
 public void testgetAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap();
 annotationMap.add(new Retarget());
 Annotation annotation = annotationMap.get(Retarget.class);
 assertEquals(annotation, annotationMap.get(Retarget.class));
 }

 @Test(expected = NullPointerException.class)
 public void testgetNullAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap();
 annotationMap.get(NullAnnotation.class);
 }

 @Test
 public void testannotations() {
 AnnotationMap annotationMap = new AnnotationMap();
 annotationMap.add(new Retarget());
 annotationMap.add(new Retarget());
 List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
 assertEquals(2, annotations.size());
 assertEquals(Retarget.class, annotations.get(0).annotationType());
 assertEquals(Retarget.class, annotations.get(1).annotationType());
 }

 @Test
 public void testmergeEmptyAnnotationMap() {
 AnnotationMap primary = new AnnotationMap();
 AnnotationMap secondary = new AnnotationMap();
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(0, merged.size());
 }

 @Test
 public void testmergeOneAnnotationMap() {
 AnnotationMap primary = new AnnotationMap();
 AnnotationMap secondary = new AnnotationMap();
 secondary.add(new Retarget());
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(1, merged.size());
 }

 @Test
 public void testmergeBothAnnotationMaps() {
 AnnotationMap primary = new AnnotationMap();
 primary.add(new Retarget());
 AnnotationMap secondary = new AnnotationMap();
 secondary.add(new Value());
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(2, merged.size());
 }

 @Test
 public void testsize() {
 AnnotationMap annotationMap = new AnnotationMap();
 annotationMap.add(new Retarget());
 annotationMap.add(new Retarget());
 assertEquals(2, annotationMap.size());
 }

 @Test
 public void testaddIfNotPresent() {
 AnnotationMap annotationMap = new AnnotationMap();
 assertFalse(annotationMap.addIfNotPresent(new Retarget()));
 annotationMap.clear();
 assertTrue(annotationMap.addIfNotPresent(new Retarget()));
 }

 @Test
 public void testadd() {
 AnnotationMap annotationMap = new AnnotationMap();
 assertFalse(annotationMap.add(new Retarget()));
 annotationMap.clear();
 assertTrue(annotationMap.add(new Retarget()));
 }

 @Test
 public void testtoString() {
 AnnotationMap annotationMap = new AnnotationMap();
 annotationMap.add(new Retarget());
 annotationMap.add(new Retarget());
 String toString = annotationMap.toString();
 assertTrue(toString.contains("Retarget"));
 }

 @Test
 public void testequals() {
 AnnotationMap annotationMap1 = new AnnotationMap();
 AnnotationMap annotationMap2 = new AnnotationMap();
 annotationMap1.add(new Retarget());
 annotationMap2.add(new Retarget());
 assertEquals(annotationMap1, annotationMap2);
 AnnotationMap annotationMap3 = new AnnotationMap();
 annotationMap3.add(new Value());
 assertNotEquals(annotationMap1, annotationMap3);
 }

 @Test
 public void testhashCode() {
 AnnotationMap annotationMap1 = new AnnotationMap();
 AnnotationMap annotationMap2 = new AnnotationMap();
 annotationMap1.add(new Retarget());
 annotationMap2.add(new Retarget());
 assertEquals(annotationMap1.hashCode(), annotationMap2.hashCode());
 AnnotationMap annotationMap3 = new AnnotationMap();
 annotationMap3.add(new Value());
 assertNotEquals(annotationMap1.hashCode(), annotationMap3.hashCode());
 }
}