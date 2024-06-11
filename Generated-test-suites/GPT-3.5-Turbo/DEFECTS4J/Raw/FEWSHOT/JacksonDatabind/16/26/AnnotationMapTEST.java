package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.HashMap;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Test.class));
        
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        assertTrue(annotationMap.get(Test.class) instanceof TestAnnotation);
    }
    
    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.annotations().size());
        
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        assertEquals(1, annotationMap.annotations().size());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        assertNull(AnnotationMap.merge(primary, secondary));
        
        primary.add(new TestAnnotation());
        assertNull(AnnotationMap.merge(primary, secondary));
        
        secondary.add(new OverrideAnnotation());
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(2, merged.size());
    }
    
    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
        
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        assertEquals(1, annotationMap.size());
    }
    
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.addIfNotPresent(new TestAnnotation()));
        
        annotationMap.add(new TestAnnotation());
        assertFalse(annotationMap.addIfNotPresent(new TestAnnotation()));
    }
    
    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.add(new TestAnnotation()));
        
        assertFalse(annotationMap.add(new TestAnnotation()));
    }
    
    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());
        
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        assertEquals("{interface org.junit.Test=@com.fasterxml.jackson.databind.introspect.TestAnnotation}", annotationMap.toString());
    }
    
    private class TestAnnotation implements Annotation {
        
    }
    
    private class OverrideAnnotation implements Annotation {
        
    }
}