import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {

 private AnnotationMap annotationMap;
 private AnnotationMap mergedAnnotationMap;

 @Before
 public void setUp() {
 annotationMap = new AnnotationMap();
 mergedAnnotationMap = new AnnotationMap();
 }

 @Test
 public void testGetAnnotation() {
 // Test getting annotation from empty AnnotationMap
 assertNull(annotationMap.get(Object.class));
 // Test getting annotation from AnnotationMap with one annotation
 Annotation annotation1 = new MyAnnotation();
 annotationMap.add(annotation1);
 assertEquals(annotation1, annotationMap.get(annotation1.annotationType()));
 // Test getting annotation from AnnotationMap with multiple annotations
 Annotation annotation2 = new MyAnnotation();
 annotationMap.add(annotation2);
 assertSame(annotation1, annotationMap.get(annotation1.annotationType()));
 assertSame(annotation2, annotationMap.get(annotation2.annotationType()));
 }

 @Test
 public void testAnnotations() {
 // Test getting empty list from empty AnnotationMap
 assertNotNull(annotationMap.annotations());
 assertTrue(annotationMap.annotations().isEmpty());
 // Test getting list with one annotation from AnnotationMap
 annotationMap.add(new MyAnnotation());
 List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
 assertEquals(1, annotations.size());
 assertSame(annotations.get(0), annotationMap.get(MyAnnotation.class));
 // Test getting list with multiple annotations from AnnotationMap
 annotationMap.add(new MyAnnotation2());
 annotations = new ArrayList<>(annotationMap.annotations());
 assertEquals(2, annotations.size());
 assertSame(annotations.get(0), annotationMap.get(MyAnnotation.class));
 assertSame(annotations.get(1), annotationMap.get(MyAnnotation2.class));
 }

 @Test
 public void testMerge() {
 // Test merging empty AnnotationMaps
 assertEquals(mergedAnnotationMap, AnnotationMap.merge(null, mergedAnnotationMap));
 assertEquals(mergedAnnotationMap, AnnotationMap.merge(mergedAnnotationMap, null));
 // Test merging AnnotationMaps with no common annotations
 annotationMap.add(new MyAnnotation());
 mergedAnnotationMap.add(new MyAnnotation2());
 AnnotationMap mergedAnnotationMap1 = AnnotationMap.merge(annotationMap, mergedAnnotationMap);
 assertEquals(2, mergedAnnotationMap1.size());
 assertSame(mergedAnnotationMap1.get(MyAnnotation.class), annotationMap.get(MyAnnotation.class));
 assertSame(mergedAnnotationMap1.get(MyAnnotation2.class), mergedAnnotationMap.get(MyAnnotation2.class));
 // Test merging AnnotationMaps with common annotations
 annotationMap.add(new MyAnnotation());
 mergedAnnotationMap.add(new MyAnnotation());
 mergedAnnotationMap1 = AnnotationMap.merge(annotationMap, mergedAnnotationMap);
 assertEquals(2, mergedAnnotationMap1.size());
 assertSame(mergedAnnotationMap1.get(MyAnnotation.class), annotationMap.get(MyAnnotation.class));
 // Test merging AnnotationMaps where one is overridden by the other
 mergedAnnotationMap.add(new MyAnnotation3());
 mergedAnnotationMap1 = AnnotationMap.merge(annotationMap, mergedAnnotationMap);
 assertEquals(3, mergedAnnotationMap1.size());
 assertSame(mergedAnnotationMap1.get(MyAnnotation.class), annotationMap.get(MyAnnotation.class));
 assertSame(mergedAnnotationMap1.get(MyAnnotation3.class), mergedAnnotationMap.get(MyAnnotation3.class));
 }

 @Test
 public void testSize() {
 // Test size of empty AnnotationMap
 assertEquals(0, annotationMap.size());
 // Test size of AnnotationMap with one annotation
 annotationMap.add(new MyAnnotation());
 assertEquals(1, annotationMap.size());
 // Test size of AnnotationMap with multiple annotations
 annotationMap.add(new MyAnnotation2());
 assertEquals(2, annotationMap.size());
 }

 @Test
 public void testAddIfNotPresent() {
 // Test adding non-existent annotation
 assertFalse(annotationMap.addIfNotPresent(new MyAnnotation()));
 // Test adding existing annotation
 annotationMap.add(new MyAnnotation());
 assertTrue(annotationMap.addIfNotPresent(new MyAnnotation()));
 // Test adding existing annotation again
 assertTrue(annotationMap.addIfNotPresent(new MyAnnotation()));
 // Test adding null annotation
 assertFalse(annotationMap.addIfNotPresent(null));
 }

 @Test
 public void testAdd() {
 // Test adding non-existent annotation
 annotationMap.add(new MyAnnotation());
 assertTrue(annotationMap.add(new MyAnnotation()));
 // Test adding existing annotation
 annotationMap.add(new MyAnnotation());
 assertFalse(annotationMap.add(new MyAnnotation()));
 // Test adding null annotation
 assertFalse(annotationMap.add(null));
 }

 @Test
 public void testToString() {
 // Test toString of empty AnnotationMap
 assertEquals("{}", annotationMap.toString());
 // Test toString of AnnotationMap with one annotation
 annotationMap.add(new MyAnnotation());
 assertEquals("[MyAnnotation]", annotationMap.toString());
 // Test toString of AnnotationMap with multiple annotations
 annotationMap.add(new MyAnnotation2());
 assertEquals("[MyAnnotation, MyAnnotation2]", annotationMap.toString());
 }

 @Test
 public void testMethodCallOrder() {
 // Test that adding annotations in different orders results in the same AnnotationMap
 AnnotationMap annotationMap1 = new AnnotationMap();
 AnnotationMap annotationMap2 = new AnnotationMap();
 Annotation annotation1 = new MyAnnotation();
 Annotation annotation2 = new MyAnnotation2();
 annotationMap1.add(annotation1);
 annotationMap2.add(annotation2);
 AnnotationMap mergedAnnotationMap1 = AnnotationMap.merge(annotationMap1, annotationMap2);
 assertEquals(2, mergedAnnotationMap1.size());
 AnnotationMap mergedAnnotationMap2 = AnnotationMap.merge(annotationMap2, annotationMap1);
 assertEquals(2, mergedAnnotationMap2.size());
 assertEquals(mergedAnnotationMap1, mergedAnnotationMap2);
 }

 @Test
 public void testIsEmpty() {
 // Test isEmpty method on empty AnnotationMaps
 assertTrue(AnnotationMap.isEmpty(null));
 assertTrue(AnnotationMap.isEmpty(new AnnotationMap()));
 // Test isEmpty method on non-empty AnnotationMaps
 AnnotationMap annotationMap = new AnnotationMap();
 annotationMap.add(new MyAnnotation());
 assertFalse(AnnotationMap.isEmpty(annotationMap));
 }

 private static class MyAnnotation extends OverriddenAnnotation {}
 private static class MyAnnotation2 extends OverriddenAnnotation {}
 private static class MyAnnotation3 extends OverriddenAnnotation {}
 private static class OverriddenAnnotation extends Annotation {}
}