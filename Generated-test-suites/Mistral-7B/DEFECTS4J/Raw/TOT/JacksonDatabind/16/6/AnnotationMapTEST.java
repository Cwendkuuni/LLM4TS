import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.*;

public class AnnotationMapTest {

 @Test
 public void testGetAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap();
 // Add annotation using add method
 annotationMap.add(new JsonDeserialize(String.class));
 // Test getting the added annotation
 Annotation actualAnnotation = annotationMap.get(JsonDeserialize.class);
 JsonDeserialize expectedAnnotation = new JsonDeserialize(String.class);
 assertTrue(actualAnnotation != null);
 assertEquals(expectedAnnotation, actualAnnotation);
 }

 @Test
 public void testGetNullAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap();
 // Test getting null annotation
 assertNull(annotationMap.get(JsonSerialize.class));
 }

 @Test
 public void testAnnotations() {
 AnnotationMap annotationMap = new AnnotationMap();
 // Add annotations using add method
 annotationMap.add(new JsonDeserialize(String.class));
 annotationMap.add(new JsonSerialize(String.class));
 // Test getting all annotations
 List<Annotation> actualAnnotations = new ArrayList<>(annotationMap.annotations());
 List<Annotation> expectedAnnotations = Arrays.asList(new JsonDeserialize(String.class), new JsonSerialize(String.class));
 assertEquals(expectedAnnotations, actualAnnotations);
 }

 @Test
 public void testMergeEmptyAnnotationMaps() {
 AnnotationMap primaryAnnotationMap = null;
 AnnotationMap secondaryAnnotationMap = null;
 // Test merging two empty AnnotationMaps
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertTrue(mergedAnnotationMap != null);
 assertTrue(mergedAnnotationMap.size() == 0);
 }

 @Test
 public void testMergePrimaryAnnotationMap() {
 AnnotationMap primaryAnnotationMap = new AnnotationMap();
 primaryAnnotationMap.add(new JsonDeserialize(String.class));
 // Test merging primary and secondary empty AnnotationMaps
 AnnotationMap secondaryAnnotationMap = null;
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertEquals(primaryAnnotationMap, mergedAnnotationMap);
 }

 @Test
 public void testMergeSecondaryAnnotationMap() {
 AnnotationMap primaryAnnotationMap = new AnnotationMap();
 AnnotationMap secondaryAnnotationMap = new AnnotationMap();
 secondaryAnnotationMap.add(new JsonSerialize(String.class));
 // Test merging primary and secondary AnnotationMaps
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 List<Annotation> actualAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
 List<Annotation> expectedAnnotations = Arrays.asList(new JsonDeserialize(String.class), new JsonSerialize(String.class));
 assertEquals(expectedAnnotations, actualAnnotations);
 }

 @Test
 public void testMergeDuplicateAnnotations() {
 AnnotationMap primaryAnnotationMap = new AnnotationMap();
 primaryAnnotationMap.add(new JsonDeserialize(String.class));
 AnnotationMap secondaryAnnotationMap = new AnnotationMap();
 secondaryAnnotationMap.add(new JsonDeserialize(String.class));
 // Test merging primary and secondary AnnotationMaps with duplicate annotations
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 List<Annotation> actualAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
 List<Annotation> expectedAnnotations = Arrays.asList(new JsonDeserialize(String.class), new JsonDeserialize(String.class));
 assertEquals(expectedAnnotations, actualAnnotations);
 }

 @Test
 public void testSize() {
 AnnotationMap annotationMap = new AnnotationMap();
 // Test size of empty AnnotationMap
 assertEquals(0, annotationMap.size());
 // Test size of AnnotationMap with annotations
 annotationMap.add(new JsonDeserialize(String.class));
 annotationMap.add(new JsonSerialize(String.class));
 assertEquals(2, annotationMap.size());
 }

 @Test
 public void testAddIfNotPresent() {
 AnnotationMap annotationMap = new AnnotationMap();
 // Test adding annotation that does not already exist
 annotationMap.addIfNotPresent(new JsonDeserialize(String.class));
 assertTrue(annotationMap.size() == 1);
 // Test adding annotation that already exists
 annotationMap.add(new JsonDeserialize(String.class));
 assertTrue(annotationMap.size() == 1);
 }

 @Test
 public void testAdd() {
 AnnotationMap annotationMap = new AnnotationMap();
 // Test adding annotation that does not already exist
 annotationMap.add(new JsonDeserialize(String.class));
 assertTrue(annotationMap.size() == 1);
 // Test adding annotation that already exists
 annotationMap.add(new JsonDeserialize(String.class));
 assertTrue(annotationMap.size() == 1);
 }
}