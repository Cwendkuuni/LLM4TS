import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.*;

public class AnnotationMapTest {

 private AnnotationMap annotationMap;
 private AnnotationMap mergedAnnotationMaps;

 @Before
 public void setup() {
 annotationMap = new AnnotationMap();
 mergedAnnotationMaps = new AnnotationMap();
 }

 @Test
 public void testGetAnnotation() {
 // Test getting annotation that exists in the map
 JsonDeserialize annotation1 = new JsonDeserialize();
 annotationMap.add(annotation1);
 Annotation gotAnnotation1 = annotationMap.get(JsonDeserialize.class);
 assertTrue(gotAnnotation1 != null);
 assertTrue(gotAnnotation1.equals(annotation1));

 // Test getting null for annotation that does not exist in the map
 JsonSerialize annotation2 = new JsonSerialize();
 Annotation gotAnnotation2 = annotationMap.get(JsonDeserialize.class);
 assertNull(gotAnnotation2);
 }

 @Test
 public void testAnnotations() {
 // Test empty map
 assertFalse(annotationMap.size() > 0);
 assertFalse(annotationMap.annotations().hasNext());

 // Test map with one annotation
 annotationMap.add(new MyAnnotation());
 List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
 Annotation gotAnnotation = annotations.get(0);
 assertTrue(gotAnnotation != null);

 // Test map with multiple annotations
 annotationMap.add(new MyAnnotation2());
 annotations = new ArrayList<>(annotationMap.annotations());
 assertEquals(2, annotations.size());
 }

 @Test
 public void testMerge() {
 // Test merging empty maps
 AnnotationMap emptyMap1 = new AnnotationMap();
 AnnotationMap emptyMap2 = new AnnotationMap();
 AnnotationMap mergedMap = AnnotationMap.merge(emptyMap1, emptyMap2);
 assertSame(mergedMap, emptyMap2);

 // Test merging maps with same annotations
 AnnotationMap map1 = new AnnotationMap();
 map1.add(new MyAnnotation());
 AnnotationMap map2 = new AnnotationMap();
 map2.add(new MyAnnotation());
 AnnotationMap mergedMap12 = AnnotationMap.merge(map1, map2);
 assertEquals(1, mergedMap12.size());

 // Test merging maps with different annotations
 map1 = new AnnotationMap();
 map1.add(new MyAnnotation());
 map2.add(new MyAnnotation2());
 AnnotationMap mergedMap12a = AnnotationMap.merge(map1, map2);
 assertEquals(2, mergedMap12a.size());
 }

 @Test
 public void testSize() {
 // Test size of empty map
 assertEquals(0, annotationMap.size());

 // Test size of map with one annotation
 annotationMap.add(new MyAnnotation());
 assertEquals(1, annotationMap.size());

 // Test size of map with multiple annotations
 annotationMap.add(new MyAnnotation2());
 assertEquals(2, annotationMap.size());
 }

 @Test
 public void testAddIfNotPresent() {
 // Test adding annotation that does not exist in the map
 JsonDeserialize annotation1 = new JsonDeserialize();
 assertTrue(annotationMap.addIfNotPresent(annotation1));
 assertEquals(1, annotationMap.size());

 // Test adding annotation that already exists in the map
 JsonSerialize annotation2 = new JsonSerialize();
 annotationMap.add(annotation2);
 annotationMap.addIfNotPresent(annotation2);
 assertEquals(1, annotationMap.size());
 }

 @Test
 public void testAdd() {
 // Test adding annotation that does not exist in the map
 JsonDeserialize annotation1 = new JsonDeserialize();
 assertTrue(annotationMap.add(annotation1));
 assertEquals(1, annotationMap.size());

 // Test adding annotation that already exists in the map
 JsonSerialize annotation2 = new JsonSerialize();
 annotationMap.add(annotation2);
 annotationMap.add(annotation2);
 assertEquals(1, annotationMap.size());
 }

 @Retention(RetentionPolicy.RUNTIME)
 @Target(ElementType.METHOD)
 private @interface MyAnnotation {
 }

 @Retention(RetentionPolicy.RUNTIME)
 @Target(ElementType.METHOD)
 private @interface MyAnnotation2 {
 }
}