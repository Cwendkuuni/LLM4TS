package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.annotation.Annotation;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class AnnotationMapTest {
    
    private AnnotationMap testAnnotationMap;

    @Before
    public void setup() {
        testAnnotationMap = new AnnotationMap();
    }
    
    @Test
    public void testGet() {
        assertNull(testAnnotationMap.get(Override.class));
        
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(Override.class, new OverrideImpl());
        AnnotationMap annotationMap = new AnnotationMap(annotations);
        
        assertEquals(annotationMap.get(Override.class), new OverrideImpl());
    }
    
    @Test
    public void testAnnotations() {
        assertEquals(testAnnotationMap.annotations(), Collections.emptyList());
        
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(Override.class, new OverrideImpl());
        AnnotationMap annotationMap = new AnnotationMap(annotations);
        
        assertEquals(annotationMap.annotations(), new ArrayList<>(Collections.singletonList(new OverrideImpl())));
    }
    
    @Test
    public void testMerge() {
        assertNull(AnnotationMap.merge(null, null));
        
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(Override.class, new OverrideImpl());
        AnnotationMap annotationMap1 = new AnnotationMap(annotations);
        
        assertEquals(AnnotationMap.merge(annotationMap1, null), annotationMap1);
        
        assertEquals(AnnotationMap.merge(null, annotationMap1), annotationMap1);
        
        AnnotationMap annotationMap2 = new AnnotationMap(annotations);
        
        assertEquals(AnnotationMap.merge(annotationMap1, annotationMap2), annotationMap1);
    }
    
    @Test
    public void testSize() {
        assertEquals(testAnnotationMap.size(), 0);
        
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(Override.class, new OverrideImpl());
        AnnotationMap annotationMap = new AnnotationMap(annotations);
        
        assertEquals(annotationMap.size(), 1);
    }
    
    @Test
    public void testAddIfNotPresent() {
        assertEquals(testAnnotationMap.addIfNotPresent(new OverrideImpl()), true);
        
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(Override.class, new OverrideImpl());
        AnnotationMap annotationMap = new AnnotationMap(annotations);
        
        assertEquals(annotationMap.addIfNotPresent(new OverrideImpl()), false);
    }
    
    @Test
    public void testAdd() {
        assertEquals(testAnnotationMap.add(new OverrideImpl()), true);
        
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(Override.class, new OverrideImpl());
        AnnotationMap annotationMap = new AnnotationMap(annotations);
        
        assertEquals(annotationMap.add(new OverrideImpl()), false);
    }
    
    @Override
    public void testToString() {
        assertEquals(testAnnotationMap.toString(), "[null]");
        
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(Override.class, new OverrideImpl());
        AnnotationMap annotationMap = new AnnotationMap(annotations);
        
        assertEquals(annotationMap.toString(), annotations.toString());
    }
    
    private class OverrideImpl implements Override {
        
        @Override
        public Class<? extends Annotation> annotationType() {
            return Override.class;
        }
        
        @Override
        public String toString() {
            return "Override";
        }
    }
}