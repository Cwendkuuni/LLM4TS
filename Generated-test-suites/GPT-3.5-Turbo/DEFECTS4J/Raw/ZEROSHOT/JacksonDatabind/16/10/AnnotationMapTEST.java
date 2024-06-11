package com.fasterxml.jackson.databind.introspect;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;

public class AnnotationMapTest {
  
    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        Assert.assertNull(annotationMap.get(Test.class));
    }
    
    @Test
    public void testAnnotations_EmptyMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        Assert.assertTrue(annotationMap.annotations().isEmpty());
    }
    
    @Test
    public void testAnnotations_NonEmptyMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        annotationMap.add(Test.class.getAnnotation(Annotation.class));
        
        Assert.assertFalse(annotationMap.annotations().isEmpty());
    }
    
    @Test
    public void testMerge_PrimaryEmpty() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        
        Assert.assertSame(secondary, merged);
    }
    
    @Test
    public void testMerge_SecondaryEmpty() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        secondary.add(Test.class.getAnnotation(Annotation.class));
        
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        
        Assert.assertSame(primary, merged);
    }
    
    @Test
    public void testMerge_BothNonEmpty() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        primary.add(Test.class.getAnnotation(Annotation.class));
        secondary.add(Override.class.getAnnotation(Annotation.class));
        
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        
        Assert.assertFalse(merged.annotations().isEmpty());
        Assert.assertTrue(merged.annotations().contains(Test.class.getAnnotation(Annotation.class)));
        Assert.assertTrue(merged.annotations().contains(Override.class.getAnnotation(Annotation.class)));
    }
    
    @Test
    public void testSize_EmptyMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        Assert.assertEquals(0, annotationMap.size());
    }
    
    @Test
    public void testSize_NonEmptyMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        annotationMap.add(Test.class.getAnnotation(Annotation.class));
        
        Assert.assertEquals(1, annotationMap.size());
    }
    
    @Test
    public void testAddIfNotPresent_NotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        boolean added = annotationMap.addIfNotPresent(Test.class.getAnnotation(Annotation.class));
        
        Assert.assertTrue(added);
        Assert.assertEquals(1, annotationMap.size());
    }
    
    @Test
    public void testAddIfNotPresent_Present() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        annotationMap.add(Test.class.getAnnotation(Annotation.class));
        
        boolean added = annotationMap.addIfNotPresent(Test.class.getAnnotation(Annotation.class));
        
        Assert.assertFalse(added);
        Assert.assertEquals(1, annotationMap.size());
    }
    
    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        boolean added = annotationMap.add(Test.class.getAnnotation(Annotation.class));
        
        Assert.assertTrue(added);
        Assert.assertEquals(1, annotationMap.size());
    }
    
    @Test
    public void testToString_EmptyMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        String str = annotationMap.toString();
        
        Assert.assertEquals("[null]", str);
    }
    
    @Test
    public void testToString_NonEmptyMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        annotationMap.add(Test.class.getAnnotation(Annotation.class));
        
        String str = annotationMap.toString();
        
        Assert.assertFalse(str.isEmpty());
    }
}