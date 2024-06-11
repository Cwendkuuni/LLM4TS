import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.*;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class AnnotationMapTest {

 @Test
 public void testGetAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap();
 MyAnnotation myAnnotation = new MyAnnotation();
 Class<MyAnnotation> myAnnotationClass = MyAnnotation.class;
 annotationMap.add(myAnnotation);

 Annotation result = annotationMap.get(myAnnotationClass);

 assertEquals(myAnnotation, result);
 }

 @Test
 public void testGetNullAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap();
 Class<MyAnnotation> myAnnotationClass = MyAnnotation.class;

 Annotation result = annotationMap.get(myAnnotationClass);

 assertNull(result);
 }

 @Test
 public void testMergeEmptyPrimaryAnnotationMap() {
 AnnotationMap primaryAnnotationMap = new AnnotationMap();
 AnnotationMap secondaryAnnotationMap = new AnnotationMap();

 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

 assertTrue(mergedAnnotationMap.isEmpty());
 }

 @Test
 public void testMergePrimaryAnnotationMapWithEmptySecondaryAnnotationMap() {
 AnnotationMap primaryAnnotationMap = new AnnotationMap();
 primaryAnnotationMap.add(new MyAnnotation());
 AnnotationMap secondaryAnnotationMap = new AnnotationMap();

 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

 assertEquals(1, mergedAnnotationMap.size());
 }

 @Test
 public void testMergePrimaryAnnotationMapWithNonEmptySecondaryAnnotationMap() {
 AnnotationMap primaryAnnotationMap = new AnnotationMap();
 primaryAnnotationMap.add(new MyAnnotation());
 AnnotationMap secondaryAnnotationMap = new AnnotationMap();
 secondaryAnnotationMap.add(new OtherAnnotation());

 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

 assertEquals(2, mergedAnnotationMap.size());
 }

 @Test
 public void testMergePrimaryAnnotationMapWithSameAnnotationTypes() {
 AnnotationMap primaryAnnotationMap = new AnnotationMap();
 primaryAnnotationMap.add(new MyAnnotation());
 AnnotationMap secondaryAnnotationMap = new AnnotationMap();
 secondaryAnnotationMap.add(new MyAnnotation());

 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

 assertEquals(2, mergedAnnotationMap.size());
 }

 @Test
 public void testMergePrimaryAnnotationMapWithDifferentAnnotationTypes() {
 AnnotationMap primaryAnnotationMap = new AnnotationMap();
 primaryAnnotationMap.add(new MyAnnotation());
 AnnotationMap secondaryAnnotationMap = new AnnotationMap();
 secondaryAnnotationMap.add(new OtherAnnotation());

 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

 assertEquals(2, mergedAnnotationMap.size());
 }

 @Test
 public void testSize() {
 AnnotationMap annotationMap = new AnnotationMap();
 annotationMap.add(new MyAnnotation());
 annotationMap.add(new OtherAnnotation());

 assertEquals(2, annotationMap.size());
 }

 @Test
 public void testAddIfNotPresent() {
 AnnotationMap annotationMap = new AnnotationMap();
 MyAnnotation myAnnotation = new MyAnnotation();

 PowerMockito.mockStatic(Annotations.class);
 when(Annotations.class.getInstance(AnnotationMap.class)).thenReturn(annotationMap);

 assertTrue(annotationMap.addIfNotPresent(myAnnotation));
 }

 @Test
 public void testAddIfNotPresentAlreadyPresent() {
 AnnotationMap annotationMap = new AnnotationMap();
 MyAnnotation myAnnotation = new MyAnnotation();
 annotationMap.add(myAnnotation);

 PowerMockito.mockStatic(Annotations.class);
 when(Annotations.class.getInstance(AnnotationMap.class)).thenReturn(annotationMap);

 assertFalse(annotationMap.addIfNotPresent(myAnnotation));
 }

 @Test
 public void testAdd() {
 AnnotationMap annotationMap = new AnnotationMap();
 MyAnnotation myAnnotation = new MyAnnotation();

 PowerMockito.mockStatic(Annotations.class);
 when(Annotations.class.getInstance(AnnotationMap.class)).thenReturn(annotationMap);

 annotationMap.add(myAnnotation);
 assertEquals(1, annotationMap.size());
 }

 @Test
 public void testAddAlreadyPresent() {
 AnnotationMap annotationMap = new AnnotationMap();
 MyAnnotation myAnnotation = new MyAnnotation();
 annotationMap.add(myAnnotation);

 PowerMockito.mockStatic(Annotations.class);
 when(Annotations.class.getInstance(AnnotationMap.class)).thenReturn(annotationMap);

 annotationMap.add(myAnnotation);
 assertEquals(1, annotationMap.size());
 }

 @Test
 public void testToString() {
 AnnotationMap annotationMap = new AnnotationMap();
 MyAnnotation myAnnotation = new MyAnnotation();
 annotationMap.add(myAnnotation);

 assertEquals("[AnnotationMap{_annotations=HashMap{MyAnnotation=Annotation@655411}}]", annotationMap.toString());
 }
}