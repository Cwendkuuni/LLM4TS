package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AnnotationMapTest {
    @Test
    public void testGet() {
        AnnotationMap map = new AnnotationMap();
        
        assertNull(map.get(Test.class));
    }
    
    @Test
    public void testAnnotations() {
        AnnotationMap map = new AnnotationMap();
        
        assertEquals(Collections.emptyList(), map.annotations());
        
        map.add(new TestAnnotation1());
        map.add(new TestAnnotation2());
        
        assertEquals(new ArrayList<>(Arrays.asList(new TestAnnotation1(), new TestAnnotation2())), map.annotations());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        assertNull(result);
        
        primary.add(new TestAnnotation1());
        secondary.add(new TestAnnotation2());
        
        result = AnnotationMap.merge(primary, secondary);
        
        assertEquals(new ArrayList<>(Arrays.asList(new TestAnnotation2(), new TestAnnotation1())), result.annotations());
    }
    
    @Test
    public void testSize() {
        AnnotationMap map = new AnnotationMap();
        
        assertEquals(0, map.size());
        
        map.add(new TestAnnotation1());
        map.add(new TestAnnotation2());
        
        assertEquals(2, map.size());
    }
    
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();
        
        assertEquals(true, map.addIfNotPresent(new TestAnnotation1()));
        assertEquals(false, map.addIfNotPresent(new TestAnnotation1()));
        
        assertEquals(new ArrayList<>(Arrays.asList(new TestAnnotation1())), map.annotations());
    }
    
    @Test
    public void testAdd() {
        AnnotationMap map = new AnnotationMap();
        
        assertEquals(true, map.add(new TestAnnotation1()));
        assertEquals(false, map.add(new TestAnnotation1()));
        
        assertEquals(new ArrayList<>(Arrays.asList(new TestAnnotation1())), map.annotations());
    }
    
    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap();
        
        assertEquals("[null]", map.toString());
        
        map.add(new TestAnnotation1());
        map.add(new TestAnnotation2());
        
        assertEquals("{"+TestAnnotation1.class.getSimpleName()+"="+new TestAnnotation1()+", "+TestAnnotation2.class.getSimpleName()+"="+new TestAnnotation2()+"}", map.toString());
    }
}