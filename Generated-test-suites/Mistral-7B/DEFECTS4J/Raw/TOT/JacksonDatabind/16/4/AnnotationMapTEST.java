import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AnnotationMapTest {

 private AnnotationMap map;
 private AnnotationMap mergedMap;

 @Before
 public void setup() {
 map = new AnnotationMap();
 }

 @Test
 public void testGetAnnotation() {
 JsonSerialize ann1 = new JsonSerialize();
 map.add(ann1);
 assertEquals(ann1, map.get(JsonSerialize.class));
 }

 @Test
 public void testGetNullAnnotation() {
 assertEquals(null, map.get(JsonDeserialize.class));
 }

 @Test
 public void testAnnotations() {
 JsonSerialize ann1 = new JsonSerialize();
 JsonDeserialize ann2 = new JsonDeserialize();
 map.add(ann1);
 map.add(ann2);
 List<Annotation> actual = new ArrayList<>(map.annotations());
 List<Annotation> expected = Arrays.asList(ann1, ann2);
 assertEquals(expected, actual);
 }

 @Test
 public void testMerge() {
 AnnotationMap map1 = new AnnotationMap();
 map1.add(new JsonSerialize());
 AnnotationMap map2 = new AnnotationMap();
 map2.add(new JsonDeserialize());
 AnnotationMap mergedMap = AnnotationMap.merge(map1, map2);
 AnnotationMap expectedMap = new AnnotationMap();
 expectedMap.add(new JsonSerialize());
 expectedMap.add(new JsonDeserialize());
 List<Annotation> actual = new ArrayList<>(mergedMap.annotations());
 List<Annotation> expected = Arrays.asList(new JsonSerialize(), new JsonDeserialize());
 assertEquals(expected, actual);
 }

 @Test
 public void testSize() {
 AnnotationMap map1 = new AnnotationMap();
 map1.add(new JsonSerialize());
 AnnotationMap map2 = new AnnotationMap();
 map2.add(new JsonDeserialize());
 AnnotationMap mergedMap = AnnotationMap.merge(map1, map2);
 assertEquals(2, mergedMap.size());
 }

 @Test
 public void testAddIfNotPresent() {
 AnnotationMap map = new AnnotationMap();
 JsonSerialize ann1 = new JsonSerialize();
 JsonDeserialize ann2 = new JsonDeserialize();
 map.add(ann1);
 map.add(ann1); // should not add duplicates
 map.addIfNotPresent(ann2);
 assertEquals(2, map.size());
 }

 @Test
 public void testAdd() {
 AnnotationMap map = new AnnotationMap();
 JsonSerialize ann1 = new JsonSerialize();
 map.add(ann1);
 map.add(new JsonSerialize()); // should add duplicates
 assertEquals(2, map.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testModifyMap() {
 AnnotationMap map = new AnnotationMap();
 map.add(new JsonSerialize());
 map.get(_annotations).clear(); // should not be able to modify the map
 }

 @Test
 public void testToString() {
 AnnotationMap map = new AnnotationMap();
 map.add(new JsonSerialize());
 map.add(new JsonDeserialize());
 assertEquals("[Lcom.fasterxml.jackson.databind.annotation.JsonSerialize;@7f9d5a5d, Lcom.fasterxml.jackson.databind.annotation.JsonDeserialize;@40cb298c]", map.toString());
 }

 @Test
 public void testEmptyList() {
 AnnotationMap map = new AnnotationMap();
 assertEquals(Collections.emptyList(), map.annotations());
 }
}