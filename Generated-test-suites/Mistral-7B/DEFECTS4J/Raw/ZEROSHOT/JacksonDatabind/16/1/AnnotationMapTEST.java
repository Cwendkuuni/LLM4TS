package com.fasterxml.jackson.databind.introspect.test;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {

 private AnnotationMap createAnnotationMap() {
 return new AnnotationMap();
 }

 @Test
 public void testGetNullAnnotationMap() {
 AnnotationMap annotationMap = null;
 assertNull(annotationMap.get(Annotation.class));
 }

 @Test
 public void testGetAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new MyAnnotation());
 MyAnnotation myAnnotation = annotationMap.get(MyAnnotation.class);
 assertNotNull(myAnnotation);
 }

 @Test
 public void testGetNullAnnotationType() {
 AnnotationMap annotationMap = createAnnotationMap();
 assertNull(annotationMap.get(null));
 }

 @Test
 public void testMergeEmptyAnnotationMaps() {
 AnnotationMap primary = createAnnotationMap();
 AnnotationMap secondary = createAnnotationMap();
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(primary, merged);
 }

 @Test
 public void testMergePrimaryEmptyAnnotationMap() {
 AnnotationMap primary = createAnnotationMap();
 AnnotationMap secondary = new AnnotationMap(Collections.singletonMap(MyAnnotation.class, new MyAnnotation()));
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(secondary, merged);
 }

 @Test
 public void testMergeSecondaryEmptyAnnotationMap() {
 AnnotationMap primary = new AnnotationMap(Collections.singletonMap(MyAnnotation.class, new MyAnnotation()));
 AnnotationMap secondary = createAnnotationMap();
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(primary, merged);
 }

 @Test
 public void testMergeAnnotationMaps() {
 AnnotationMap primary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
 put(MyAnnotation.class, new MyAnnotation());
 put(AnotherAnnotation.class, new AnotherAnnotation());
 }} );
 AnnotationMap secondary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
 put(MyAnnotation.class, new MyAnnotation());
 put(ThirdAnnotation.class, new ThirdAnnotation());
 }} );
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 Set<Class<? extends Annotation>> primaryKeys = new HashSet<Class<? extends Annotation>>(primary.size());
 primaryKeys.addAll(primary.keySet());
 Set<Class<? extends Annotation>> mergedKeys = new HashSet<Class<? extends Annotation>>(merged.size());
 mergedKeys.addAll(merged.keySet());
 assertTrue(mergedKeys.containsAll(primaryKeys));
 assertEquals(merged.size(), primary.size() + secondary.size());
 }

 @Test
 public void testMergeAnnotationMapsWithSameAnnotationType() {
 AnnotationMap primary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
 put(MyAnnotation.class, new MyAnnotation());
 }} );
 AnnotationMap secondary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
 put(MyAnnotation.class, new MyAnnotation());
 }} );
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 Set<Class<? extends Annotation>> mergedKeys = new HashSet<Class<? extends Annotation>>(merged.size());
 mergedKeys.addAll(merged.keySet());
 assertEquals(mergedKeys.size(), primary.size() + secondary.size());
 }

 @Test
 public void testMergeAnnotationMapsWithDifferentAnnotationTypes() {
 AnnotationMap primary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
 put(MyAnnotation.class, new MyAnnotation());
 put(AnotherAnnotation.class, new AnotherAnnotation());
 }} );
 AnnotationMap secondary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
 put(ThirdAnnotation.class, new ThirdAnnotation());
 }} );
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 Set<Class<? extends Annotation>> mergedKeys = new HashSet<Class<? extends Annotation>>(merged.size());
 mergedKeys.addAll(merged.keySet());
 assertTrue(mergedKeys.containsAll(primary.keySet()));
 assertTrue(mergedKeys.containsAll(secondary.keySet()));
 }

 @Test
 public void testSize() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new MyAnnotation());
 assertEquals(1, annotationMap.size());
 }

 @Test
 public void testAddIfNotPresent() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.addIfNotPresent(new MyAnnotation());
 assertTrue(annotationMap.containsKey(MyAnnotation.class));
 }

 @Test
 public void testAddIfNotPresentExistingAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
 put(MyAnnotation.class, new MyAnnotation());
 }} );
 assertTrue(annotationMap.addIfNotPresent(new MyAnnotation()));
 assertEquals(1, annotationMap.size());
 }

 @Test
 public void testAdd() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(new MyAnnotation());
 assertTrue(annotationMap.containsKey(MyAnnotation.class));
 }

 @Test
 public void testToString() {
 AnnotationMap annotationMap = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
 put(MyAnnotation.class, new MyAnnotation());
 put(AnotherAnnotation.class, new AnotherAnnotation());
 }} );
 assertEquals("{MyAnnotation=class com.example.MyAnnotation, AnotherAnnotation=class com.example.AnotherAnnotation}", annotationMap.toString());
 }

 @Test
 public void testMergeWithNullAnnotationMap() {
 AnnotationMap primary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
 put(MyAnnotation.class, new MyAnnotation());
 put(AnotherAnnotation.class, new AnotherAnnotation());
 }} );
 AnnotationMap secondary = null;
 AnnotationMap merged = AnnotationMap.merge(primary, secondary);
 assertEquals(primary, merged);
 }
}

class MyAnnotation {
}

class AnotherAnnotation {
}

class ThirdAnnotation {
}