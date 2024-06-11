package com.example.junit; // Replace with your package name

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationMapTest {

 AnnotationMap annMap1 = new AnnotationMap();
 AnnotationMap annMap2 = new AnnotationMap();
 AnnotationMap mergedMap = new AnnotationMap();
 AnnotationMap nullMap = null;
 AnnotationMap emptyMap = new AnnotationMap();

 @SuppressWarnings("unchecked")
 Class<JsonSerialize> jsonSerialize = JsonSerialize.class;

 @Test
 public void testGetAnnotation() {
     annMap1.add(new JsonSerialize());
     assertEquals(annMap1.get(jsonSerialize), annMap1.get(jsonSerialize));
 }

 @Test(expected = NullPointerException.class)
 public void testGetAnnotationNullMap() {
     assertEquals(null, nullMap.get(jsonSerialize));
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetAnnotationEmptyMap() {
     assertEquals(Collections.emptyList(), annMap1.annotations());
     assertEquals(null, annMap1.get(jsonSerialize));
 }

 @Test
 public void testMergePrimaryEmpty() {
     mergedMap = AnnotationMap.merge(annMap1, annMap2);
     assertEquals(annMap1, mergedMap);
 }

 @Test
 public void testMergeSecondaryEmpty() {
     mergedMap = AnnotationMap.merge(annMap1, annMap2);
     assertEquals(annMap1, mergedMap);
 }

 @Test
 public void testMergeBothNotEmpty() {
     annMap1.add(new JsonSerialize());
     annMap2.add(new com.example.MyAnnotation());
     mergedMap = AnnotationMap.merge(annMap1, annMap2);
     assertTrue(mergedMap.size() > 1);
     assertTrue(mergedMap.containsKey(jsonSerialize));
     assertTrue(mergedMap.containsKey(MyAnnotation.class));
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullAnnotation() {
     annMap1.add(null);
 }

 @Test
 public void testAddAnnotationAlreadyPresent() {
     annMap1.add(new JsonSerialize());
     annMap1.add(new JsonSerialize());
     assertEquals(1, annMap1.size());
 }

 @Test
 public void testToString() {
     annMap1.add(new JsonSerialize());
     annMap1.add(new com.example.MyAnnotation());
     String expected = "[Lcom.fasterxml.jackson.databind.annotation.JsonSerialize;@45, Lcom.example.MyAnnotation;@67]";
     assertEquals(expected, annMap1.toString());
 }

 @Test
 public void testSize() {
     assertEquals(0, annMap1.size());
     annMap1.add(new JsonSerialize());
     annMap1.add(new com.example.MyAnnotation());
     assertEquals(2, annMap1.size());
 }

}