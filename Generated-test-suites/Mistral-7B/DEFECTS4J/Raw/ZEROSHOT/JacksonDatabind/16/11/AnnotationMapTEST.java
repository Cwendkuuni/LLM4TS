package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.databind.introspect.AnnotationMap;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface TestAnnotation {
 String value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@interface TestClassAnnotation {
 String value();
}

public class AnnotationMapTest {

 private AnnotationMap createAnnotationMap() {
 return new AnnotationMap();
 }

 private AnnotationMap createAnnotationMapWithAnnotation(Annotation annotation) {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(annotation);
 return annotationMap;
 }

 private AnnotationMap createAnnotationMapWithAnnotations(Annotation... annotations) {
 AnnotationMap annotationMap = createAnnotationMap();
 for (Annotation annotation : annotations) {
 annotationMap.add(annotation);
 }
 return annotationMap;
 }

 @Test
 public void testGetAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 TestAnnotation testAnnotation = new TestAnnotation() {
 @Override
 public String value() {
 return "TestAnnotationValue";
 }
 };
 AnnotationMap annotationMapWithAnnotation = createAnnotationMapWithAnnotation(testAnnotation);

 assertEquals("TestAnnotationValue", annotationMapWithAnnotation.get(TestAnnotation.class).value());
 }

 @Test
 public void testGetNullAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNull(annotationMap.get(TestAnnotation.class));
 }

 @Test
 public void testSize() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertEquals(0, annotationMap.size());

 AnnotationMap annotationMapWithAnnotation = createAnnotationMapWithAnnotation(new TestAnnotation() {
 @Override
 public String value() {
 return "TestAnnotationValue";
 }
 });
 assertEquals(1, annotationMapWithAnnotation.size());
 }

 @Test
 public void testAnnotations() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertEquals(Collections.emptyList(), annotationMap.annotations());

 AnnotationMap annotationMapWithAnnotation = createAnnotationMapWithAnnotation(new TestAnnotation() {
 @Override
 public String value() {
 return "TestAnnotationValue";
 }
 });
 List<Annotation> annotations = new ArrayList<>(annotationMapWithAnnotation.annotations());
 assertEquals(1, annotations.size());
 assertEquals(TestAnnotation.class, annotations.get(0).annotationType());
 }

 @Test
 public void testMerge() {
 AnnotationMap annotationMap1 = createAnnotationMapWithAnnotation(new TestAnnotation() {
 @Override
 public String value() {
 return "AnnotationMap1_TestAnnotationValue";
 }
 });
 AnnotationMap annotationMap2 = createAnnotationMapWithAnnotation(new TestAnnotation() {
 @Override
 public String value() {
 return "AnnotationMap2_TestAnnotationValue";
 }
 });

 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
 List<Annotation> mergedAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
 assertEquals(2, mergedAnnotations.size());
 assertEquals(TestAnnotation.class, mergedAnnotations.get(0).annotationType());
 assertEquals("AnnotationMap1_TestAnnotationValue", mergedAnnotations.get(0).value());
 assertEquals(TestAnnotation.class, mergedAnnotations.get(1).annotationType());
 assertEquals("AnnotationMap2_TestAnnotationValue", mergedAnnotations.get(1).value());
 }

 @Test
 public void testAddIfNotPresent() {
 AnnotationMap emptyAnnotationMap = createAnnotationMap();
 TestAnnotation testAnnotation1 = new TestAnnotation() {
 @Override
 public String value() {
 return "TestAnnotation1Value";
 }
 };
 TestAnnotation testAnnotation2 = new TestAnnotation() {
 @Override
 public String value() {
 return "TestAnnotation2Value";
 }
 };

 assertTrue(emptyAnnotationMap.addIfNotPresent(testAnnotation1));
 assertFalse(emptyAnnotationMap.addIfNotPresent(testAnnotation1));

 assertTrue(emptyAnnotationMap.addIfNotPresent(testAnnotation2));
 }

 @Test
 public void testAdd() {
 AnnotationMap emptyAnnotationMap = createAnnotationMap();
 TestAnnotation testAnnotation1 = new TestAnnotation() {
 @Override
 public String value() {
 return "TestAnnotation1Value";
 }
 };
 TestAnnotation testAnnotation2 = new TestAnnotation() {
 @Override
 public String value() {
 return "TestAnnotation2Value";
 }
 };

 assertTrue(emptyAnnotationMap.add(testAnnotation1));
 assertFalse(emptyAnnotationMap.add(testAnnotation1));

 assertTrue(emptyAnnotationMap.add(testAnnotation2));
 }

 @Test
 public void testToString() {
 AnnotationMap emptyAnnotationMap = createAnnotationMap();
 assertEquals("[]", emptyAnnotationMap.toString());

 AnnotationMap annotationMapWithAnnotation = createAnnotationMapWithAnnotation(new TestAnnotation() {
 @Override
 public String value() {
 return "TestAnnotationValue";
 }
 });
 assertEquals("[Lcom.fasterxml.jackson.databind.annotation.TestAnnotation;@5b5d1b32]", annotationMapWithAnnotation.toString());
 }

 @Test
 public void testCustomAnnotation() {
 TestClassAnnotation testClassAnnotation = new TestClassAnnotation() {
 @Override
 public String value() {
 return "TestClassAnnotationValue";
 }
 };
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(testClassAnnotation);
 assertEquals(TestClassAnnotation.class, annotationMap.get(TestClassAnnotation.class));
 }

}