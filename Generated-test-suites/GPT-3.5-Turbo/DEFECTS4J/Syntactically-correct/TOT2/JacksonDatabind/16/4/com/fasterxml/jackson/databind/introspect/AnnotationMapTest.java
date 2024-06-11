package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationMapTest {
  
  @Test
  public void testGet() {
    AnnotationMap annotationMap = new AnnotationMap();
    assertNull(annotationMap.get(Deprecated.class));
    
    Deprecated deprecated = AnnotationBuilder.createDeprecatedAnnotation();
    annotationMap.add(deprecated);
    
    assertEquals(deprecated, annotationMap.get(Deprecated.class));
  }
  
  @Test
  public void testAnnotations() {
    AnnotationMap annotationMap = new AnnotationMap();
    assertEquals(Collections.emptyList(), annotationMap.annotations());
    
    Deprecated deprecated = AnnotationBuilder.createDeprecatedAnnotation();
    annotationMap.add(deprecated);
    
    List<Annotation> expectedAnnotations = new ArrayList<>();
    expectedAnnotations.add(deprecated);
    
    assertEquals(expectedAnnotations, annotationMap.annotations());
  }
  
  @Test
  public void testMerge() {
    AnnotationMap primary = new AnnotationMap();
    AnnotationMap secondary = new AnnotationMap();
    
    Deprecated primaryDeprecated = AnnotationBuilder.createDeprecatedAnnotation();
    primary.add(primaryDeprecated);
    
    SuppressWarnings secondarySuppressWarnings = AnnotationBuilder.createSuppressWarningsAnnotation();
    secondary.add(secondarySuppressWarnings);
    
    AnnotationMap merged = AnnotationMap.merge(primary, secondary);
    
    assertTrue(merged.get(Deprecated.class).equals(primaryDeprecated));
    assertTrue(merged.get(SuppressWarnings.class).equals(secondarySuppressWarnings));
  }
  
  @Test
  public void testSize() {
    AnnotationMap annotationMap = new AnnotationMap();
    assertEquals(0, annotationMap.size());
    
    Deprecated deprecated = AnnotationBuilder.createDeprecatedAnnotation();
    annotationMap.add(deprecated);
    
    assertEquals(1, annotationMap.size());
  }
  
  @Test
  public void testAddIfNotPresent() {
    AnnotationMap annotationMap = new AnnotationMap();
    
    Deprecated deprecated = AnnotationBuilder.createDeprecatedAnnotation();
    assertTrue(annotationMap.addIfNotPresent(deprecated));
    
    assertFalse(annotationMap.addIfNotPresent(deprecated));
  }
  
  @Test
  public void testAdd() {
    AnnotationMap annotationMap = new AnnotationMap();
    
    Deprecated deprecated = AnnotationBuilder.createDeprecatedAnnotation();
    assertTrue(annotationMap.add(deprecated));
    
    assertFalse(annotationMap.add(deprecated));
  }
  
  @Test
  public void testToString() {
    AnnotationMap annotationMap = new AnnotationMap();
    assertEquals("[null]", annotationMap.toString());
    
    Deprecated deprecated = AnnotationBuilder.createDeprecatedAnnotation();
    annotationMap.add(deprecated);
    
    assertEquals("{@java.lang.Deprecated()}]", annotationMap.toString());
  }
}