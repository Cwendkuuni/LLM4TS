import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.annotation.*;
import java.util.*;

public class AnnotationMapTest {

 private AnnotationMap annotationMap;
 private AnnotationMap mergedAnnotationMap;

 @Before
 public void setup() {
 annotationMap = new AnnotationMap();
 mergedAnnotationMap = new AnnotationMap();
 }

 @Test
 public void testGetAnnotation() {
 // Test getting annotation that exists in the map
 JsonTypeInfo annotation = new JsonTypeInfo();
 annotationMap.add(annotation);
 assertEquals(annotation, annotationMap.get(JsonTypeInfo.class));

 // Test getting null for annotation that does not exist in the map
 assertNull(annotationMap.get(JsonInclude.class));
 }

 @Test
 public void testAnnotations() {
 // Test empty list when map is empty
 assertFalse(annotationMap.annotations().hasNext());

 // Test returning annotations when map is not empty
 JsonTypeInfo annotation1 = new JsonTypeInfo();
 JsonFilter annotation2 = new JsonFilter();
 annotationMap.add(annotation1);
 annotationMap.add(annotation2);
 List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
 assertEquals(2, annotations.size());
 assertTrue(annotations.contains(annotation1));
 assertTrue(annotations.contains(annotation2));
 }

 @Test
 public void testMerge() {
 // Test merging empty maps
 assertEquals(annotationMap, mergedAnnotationMap.merge(annotationMap, mergedAnnotationMap));

 // Test merging maps with annotations
 JsonTypeInfo annotation1 = new JsonTypeInfo();
 JsonFilter annotation2 = new JsonFilter();
 JsonInclude annotation3 = new JsonInclude();
 annotationMap.add(annotation1);
 annotationMap.add(annotation2);
 mergedAnnotationMap.add(annotation3);
 AnnotationMap merged = AnnotationMap.merge(annotationMap, mergedAnnotationMap);
 List<Annotation> mergedAnnotations = new ArrayList<>(merged.annotations());
 assertEquals(3, mergedAnnotations.size());
 assertTrue(mergedAnnotations.contains(annotation1));
 assertTrue(mergedAnnotations.contains(annotation2));
 assertTrue(mergedAnnotations.contains(annotation3));
 }

 @Test
 public void testSize() {
 // Test size when map is empty
 assertEquals(0, annotationMap.size());

 // Test size when map is not empty
 JsonTypeInfo annotation1 = new JsonTypeInfo();
 JsonFilter annotation2 = new JsonFilter();
 annotationMap.add(annotation1);
 annotationMap.add(annotation2);
 assertEquals(2, annotationMap.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullAnnotation() {
 // Test exception when trying to add null annotation
 annotationMap.add(null);
 }
}