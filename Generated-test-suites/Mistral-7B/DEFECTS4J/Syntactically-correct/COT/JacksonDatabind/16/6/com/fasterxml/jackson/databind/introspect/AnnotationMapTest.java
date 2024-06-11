import org.junit.Assert;
import org.junit.Test;
import java.lang.annotation.*;
import java.util.*;

public class AnnotationMapTest {

 private AnnotationMap createAnnotationMap() {
 return new AnnotationMap();
 }

 @Test
 public void testGetAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation ann = new MyAnnotation();
 annotationMap.add(ann);
 Annotation retrievedAnnotation = annotationMap.get(MyAnnotation.class);
 Assert.assertEquals(ann, retrievedAnnotation);
 }

 @Test
 public void testGetNullAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 Assert.assertNull(annotationMap.get(NonExistentAnnotation.class));
 }

 @Test
 public void testGetEmptyAnnotationMap() {
 AnnotationMap annotationMap = createAnnotationMap();
 Assert.assertNull(annotationMap.get(AnyAnnotation.class));
 }

 @Test
 public void testAnnotations() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation ann1 = new MyAnnotation();
 Annotation ann2 = new MyAnnotation();
 annotationMap.add(ann1);
 annotationMap.add(ann2);
 List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
 Assert.assertEquals(2, annotations.size());
 Assert.assertTrue(annotations.contains(ann1));
 Assert.assertTrue(annotations.contains(ann2));
 }

 @Test
 public void testMergeEmptyAnnotationMaps() {
 AnnotationMap primary = createAnnotationMap();
 AnnotationMap secondary = createAnnotationMap();
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
 Assert.assertEquals(primary, mergedAnnotationMap);
 }

 @Test
 public void testMergeAnnotationMapsWithSameAnnotation() {
 AnnotationMap primary = createAnnotationMap();
 AnnotationMap secondary = createAnnotationMap();
 Annotation ann = new MyAnnotation();
 primary.add(ann);
 secondary.add(ann);
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
 Set<Annotation> mergedAnnotations = new HashSet<>(mergedAnnotationMap.annotations());
 Assert.assertEquals(1, mergedAnnotations.size());
 Assert.assertTrue(mergedAnnotations.contains(ann));
 }

 @Test
 public void testMergeAnnotationMapsWithDifferentAnnotations() {
 AnnotationMap primary = createAnnotationMap();
 AnnotationMap secondary = createAnnotationMap();
 Annotation ann1 = new MyAnnotation();
 primary.add(ann1);
 Annotation ann2 = new DifferentAnnotation();
 secondary.add(ann2);
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
 Set<Annotation> mergedAnnotations = new HashSet<>(mergedAnnotationMap.annotations());
 Assert.assertEquals(2, mergedAnnotations.size());
 Assert.assertTrue(mergedAnnotations.contains(ann1));
 Assert.assertTrue(mergedAnnotations.contains(ann2));
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 annotationMap.add(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateAnnotation() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation ann = new MyAnnotation();
 annotationMap.add(ann);
 annotationMap.add(ann);
 }

 @Test
 public void testSize() {
 AnnotationMap annotationMap = createAnnotationMap();
 Annotation ann1 = new MyAnnotation();
 Annotation ann2 = new MyAnnotation();
 annotationMap.add(ann1);
 annotationMap.add(ann2);
 Assert.assertEquals(2, annotationMap.size());
 }
}