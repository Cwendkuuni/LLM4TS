import org.junit.Assert; import org.junit.Test; import static org.junit.Assert.assertEquals; import static org.junit.Assert.assertNotNull; import static org.junit.Assert.assertNull;import static org.junit.Assert.assertTrue;

import java.util.Arrays; import java.util.Collections; import java.util.HashMap; import java.util.List; import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize; import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class AnnotationMapTest {

 @Test public void testGetAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap(); Annotation annotation = new JsonDeserialize(); annotationMap.add(annotation); Assert.assertEquals(annotation, annotationMap.get(JsonDeserialize.class)); }

 @Test public void testGetNullAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap(); Assert.assertNull(annotationMap.get(Object.class)); }

 @Test public void testGetEmptyAnnotations() {
 AnnotationMap annotationMap = new AnnotationMap(); Assert.assertEquals(Collections.emptyList(), annotationMap.annotations()); }

 @Test public void testMergeEmptyPrimaryAndSecondary() {
 AnnotationMap primary = new AnnotationMap(); AnnotationMap secondary = new AnnotationMap(); AnnotationMap merged = AnnotationMap.merge(primary, secondary); assertEquals(Collections.emptyList(), merged.annotations()); }

 @Test public void testMergePrimaryNotEmptySecondaryEmpty() {
 AnnotationMap primary = new AnnotationMap(); AnnotationMap secondary = new AnnotationMap(); primary.add(new JsonSerialize()); merged = AnnotationMap.merge(primary, secondary); assertEquals(Arrays.asList(primary.get(JsonSerialize.class)), merged.annotations()); }

 @Test public void testMergePrimaryEmptySecondaryNotEmpty() {
 AnnotationMap primary = new AnnotationMap(); AnnotationMap secondary = new AnnotationMap(); secondary.add(new JsonDeserialize()); merged = AnnotationMap.merge(primary, secondary); assertEquals(Arrays.asList(secondary.get(JsonDeserialize.class)), merged.annotations()); }

 @Test public void testMergeBothNotEmpty() {
 AnnotationMap primary = new AnnotationMap(); primary.add(new JsonSerialize()); AnnotationMap secondary = new AnnotationMap(); secondary.add(new JsonDeserialize()); merged = AnnotationMap.merge(primary, secondary); assertEquals(Arrays.asList(primary.get(JsonSerialize.class), secondary.get(JsonDeserialize.class)), merged.annotations()); }

 @Test public void testSize() {
 AnnotationMap annotationMap = new AnnotationMap(); annotationMap.add(new JsonDeserialize()); annotationMap.add(new JsonSerialize()); assertEquals(2, annotationMap.size()); }

 @Test public void testAddIfNotPresent() {
 AnnotationMap annotationMap = new AnnotationMap(); Annotation annotation = new JsonDeserialize(); assertFalse(annotationMap.addIfNotPresent(annotation)); annotationMap.add(new JsonSerialize()); assertTrue(annotationMap.addIfNotPresent(annotation)); }

 @Test public void testAdd() {
 AnnotationMap annotationMap = new AnnotationMap(); Annotation annotation = new JsonDeserialize(); annotationMap.add(annotation); assertEquals(1, annotationMap.size()); }

 @Test public void testToString() {
 AnnotationMap annotationMap = new AnnotationMap(); annotationMap.add(new JsonDeserialize()); annotationMap.add(new JsonSerialize()); String result = annotationMap.toString(); assertNotNull(result); assertTrue(result.contains("JsonDeserialize")); assertTrue(result.contains("JsonSerialize")); }

}