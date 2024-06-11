import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.*;

public class AnnotationMapTest {

 private AnnotationMap annotationMap1;
 private AnnotationMap annotationMap2;
 private AnnotationMap mergedAnnotationMap;

 @Before
 public void setup() {
 annotationMap1 = new AnnotationMap();
 annotationMap2 = new AnnotationMap();
 }

 @Test
 public void testGetAnnotation() {
 // Test getting annotation from empty AnnotationMap
 Assert.assertEquals(null, annotationMap1.get(JsonSerialize.class));

 // Test getting annotation from non-empty AnnotationMap
 JsonSerialize jsonSerializeAnnotation = new JsonSerialize();
 annotationMap1.add(jsonSerializeAnnotation);
 Assert.assertEquals(jsonSerializeAnnotation, annotationMap1.get(JsonSerialize.class));

 // Test getting non-existent annotation from AnnotationMap
 Assert.assertEquals(null, annotationMap1.get(JsonDeserialize.class));
 }

 @Test
 public void testAnnotations() {
 // Test getting empty collection from empty AnnotationMap
 Assert.assertTrue(annotationMap1.annotations().isEmpty());

 // Test getting non-empty collection from non-empty AnnotationMap
 JsonSerialize jsonSerializeAnnotation = new JsonSerialize();
 JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
 annotationMap1.add(jsonSerializeAnnotation);
 annotationMap1.add(jsonDeserializeAnnotation);
 List<Annotation> annotations = new ArrayList<>(annotationMap1.annotations());
 Assert.assertFalse(annotations.isEmpty());
 Assert.assertEquals(2, annotations.size());
 }

 @Test
 public void testMerge() {
 // Test merging two empty AnnotationMap
 AnnotationMap mergedAnnotationMap1 = AnnotationMap.merge(annotationMap1, annotationMap2);
 Assert.assertEquals(annotationMap2, mergedAnnotationMap1);

 // Test merging non-empty AnnotationMap with empty one
 JsonSerialize jsonSerializeAnnotation = new JsonSerialize();
 annotationMap1.add(jsonSerializeAnnotation);
 AnnotationMap mergedAnnotationMap2 = AnnotationMap.merge(annotationMap1, annotationMap2);
 Assert.assertEquals(annotationMap1, mergedAnnotationMap2);

 // Test merging two non-empty AnnotationMap
 JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
 annotationMap2.add(jsonDeserializeAnnotation);
 List<Annotation> mergedAnnotations = new ArrayList<>(mergedAnnotationMap1.annotations());
 Assert.assertFalse(mergedAnnotations.isEmpty());
 Assert.assertEquals(2, mergedAnnotations.size());
 }

 @Test
 public void testSize() {
 // Test size of empty AnnotationMap
 Assert.assertEquals(0, annotationMap1.size());

 // Test size of non-empty AnnotationMap
 annotationMap1.add(new JsonSerialize());
 Assert.assertEquals(1, annotationMap1.size());
 }

 @Test
 public void testAddIfNotPresent() {
 // Test adding annotation that doesn't exist in AnnotationMap
 JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
 Assert.assertTrue(annotationMap1.addIfNotPresent(jsonDeserializeAnnotation));
 Assert.assertEquals(1, annotationMap1.size());

 // Test adding annotation that already exists in AnnotationMap
 JsonSerialize jsonSerializeAnnotation = new JsonSerialize();
 annotationMap1.add(jsonSerializeAnnotation);
 Assert.assertFalse(annotationMap1.addIfNotPresent(jsonSerializeAnnotation));
 Assert.assertEquals(1, annotationMap1.size());
 }

 @Test
 public void testAdd() {
 // Test adding annotation that doesn't exist in AnnotationMap
 JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
 Assert.assertTrue(annotationMap1.add(jsonDeserializeAnnotation));
 Assert.assertEquals(1, annotationMap1.size());

 // Test adding annotation that already exists in AnnotationMap
 JsonSerialize jsonSerializeAnnotation = new JsonSerialize();
 annotationMap1.add(jsonSerializeAnnotation);
 Assert.assertFalse(annotationMap1.add(jsonSerializeAnnotation));
 Assert.assertEquals(1, annotationMap1.size());
 }
}