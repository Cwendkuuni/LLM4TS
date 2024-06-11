package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotationMapTest {

   // ... Previous test cases ...

   @Test(expected = NullPointerException.class)
   public void testMergeNullPrimaryMap() {
      AnnotationMap secondary = new AnnotationMap();
      AnnotationMap primary = null;
      AnnotationMap actual = AnnotationMap.merge(primary, secondary);
      assertEquals(secondary, actual);
   }

   @Test(expected = NullPointerException.class)
   public void testMergeNullSecondaryMap() {
      AnnotationMap primary = new AnnotationMap();
      AnnotationMap secondary = null;
      AnnotationMap actual = AnnotationMap.merge(primary, secondary);
      assertEquals(primary, actual);
   }

   @Test(expected = NullPointerException.class)
   public void testMergeNullAnnotations() {
      AnnotationMap primary = new AnnotationMap();
      AnnotationMap secondary = new AnnotationMap();
      secondary._annotations = null;
      AnnotationMap actual = AnnotationMap.merge(primary, secondary);
      assertEquals(primary, actual);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testMergeDifferentAnnotationTypes() {
      AnnotationMap primary = new AnnotationMap();
      primary.add(new MyAnnotation1());
      AnnotationMap secondary = new AnnotationMap();
      secondary.add(new MyAnnotation2());
      AnnotationMap actual = AnnotationMap.merge(primary, secondary);
      List<Annotation> expected = Collections.emptyList();
      List<Annotation> actualAnnotations = actual.annotations();
      assertEquals(expected, actualAnnotations);
   }

   @Test
   public void testMergeSameAnnotationTypes() {
      AnnotationMap primary = new AnnotationMap();
      primary.add(new MyAnnotation1());
      AnnotationMap secondary = new AnnotationMap();
      secondary.add(new MyAnnotation1());
      AnnotationMap expected = new AnnotationMap();
      expected.add(new MyAnnotation1());
      AnnotationMap actual = AnnotationMap.merge(primary, secondary);
      List<Annotation> expectedAnnotations = Arrays.asList(new MyAnnotation1());
      List<Annotation> actualAnnotations = actual.annotations();
      assertEquals(expectedAnnotations, actualAnnotations);
   }

   @Test
   public void testMergeSameAnnotationTypesWithDifferentInstances() {
      AnnotationMap primary = new AnnotationMap();
      MyAnnotation annotation1 = new MyAnnotation();
      primary.add(annotation1);
      AnnotationMap secondary = new AnnotationMap();
      MyAnnotation annotation2 = new MyAnnotation();
      secondary.add(annotation2);
      AnnotationMap expected = new AnnotationMap();
      expected.add(annotation1);
      AnnotationMap actual = AnnotationMap.merge(primary, secondary);
      List<Annotation> expectedAnnotations = Arrays.asList(annotation1);
      List<Annotation> actualAnnotations = actual.annotations();
      assertEquals(expectedAnnotations, actualAnnotations);
   }

   @Test
   public void testMergeDifferentAnnotationInstancesWithSameType() {
      AnnotationMap primary = new AnnotationMap();
      MyAnnotation annotation1 = new MyAnnotation();
      primary.add(annotation1);
      AnnotationMap secondary = new AnnotationMap();
      MyAnnotation annotation2 = new MyAnnotation();
      secondary.add(annotation2);
      AnnotationMap expected = new AnnotationMap();
      expected.add(annotation1);
      AnnotationMap actual = AnnotationMap.merge(primary, secondary);
      List<Annotation> expectedAnnotations = Arrays.asList(annotation1);
      List<Annotation> actualAnnotations = actual.annotations();
      assertEquals(expectedAnnotations, actualAnnotations);
   }

}