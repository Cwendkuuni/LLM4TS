package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationMapTest {

    private AnnotationMap map;
    
    @Before
    public void setUp() {
        map = new AnnotationMap();
    }
    
    @Test
    public void testGetShouldReturnNullWhenAnnotationsIsNull() {
        Class<TestAnnotation> cls = TestAnnotation.class;
        
        TestAnnotation result = map.get(cls);
        
        assertNull(result);
    }
    
    @Test
    public void testGetShouldReturnAnnotationWhenAnnotationsContainsKey() {
        Class<TestAnnotation> cls = TestAnnotation.class;
        TestAnnotation annotation = new TestAnnotation();
        map._add(annotation);
        
        TestAnnotation result = map.get(cls);
        
        assertEquals(annotation, result);
    }
    
    @Test
    public void testGetShouldReturnNullWhenAnnotationsDoesNotContainKey() {
        Class<TestAnnotation> cls = TestAnnotation.class;
        TestAnnotation annotation = new TestAnnotation();
        map._add(annotation);
        AnnotationMap emptyMap = new AnnotationMap();
        
        TestAnnotation result = emptyMap.get(cls);
        
        assertNull(result);
    }
    
    @Test
    public void testAnnotationsShouldReturnEmptyListWhenAnnotationsIsNull() {
        Iterable<Annotation> result = map.annotations();
        
        assertEquals(Collections.emptyList(), result);
    }
    
    @Test
    public void testAnnotationsShouldReturnListWithAnnotationsWhenAnnotationsIsNotEmpty() {
        TestAnnotation annotation1 = new TestAnnotation();
        TestAnnotation annotation2 = new TestAnnotation();
        map._add(annotation1);
        map._add(annotation2);
        List<Annotation> expected = Arrays.asList(annotation1, annotation2);
        
        Iterable<Annotation> result = map.annotations();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testMergeShouldReturnSecondaryWhenPrimaryIsNull() {
        AnnotationMap primary = null;
        AnnotationMap secondary = new AnnotationMap();
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        assertEquals(secondary, result);
    }
    
    @Test
    public void testMergeShouldReturnPrimaryWhenSecondaryIsNull() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = null;
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        assertEquals(primary, result);
    }
    
    @Test
    public void testMergeShouldReturnPrimaryWhenSecondaryIsEmpty() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        assertEquals(primary, result);
    }
    
    @Test
    public void testMergeShouldReturnSecondaryWhenPrimaryIsEmpty() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        AnnotationMap result = AnnotationMap.merge(secondary, primary);
        
        assertEquals(secondary, result);
    }
    
    @Test
    public void testMergeShouldReturnMergedMapWhenBothPrimaryAndSecondaryAreNotEmpty() {
        TestAnnotation annotation1 = new TestAnnotation();
        TestAnnotation annotation2 = new TestAnnotation();
        TestAnnotation annotation3 = new TestAnnotation();
        TestAnnotation annotation4 = new TestAnnotation();
        AnnotationMap primary = new AnnotationMap();
        primary._add(annotation1);
        primary._add(annotation2);
        AnnotationMap secondary = new AnnotationMap();
        secondary._add(annotation3);
        secondary._add(annotation4);
        AnnotationMap expected = new AnnotationMap();
        expected._add(annotation3);
        expected._add(annotation4);
        expected._add(annotation1);
        expected._add(annotation2);
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testSizeShouldReturnZeroWhenAnnotationsIsNull() {
        int result = map.size();
        
        assertEquals(0, result);
    }
    
    @Test
    public void testSizeShouldReturnSizeOfAnnotationsWhenAnnotationsIsNotEmpty() {
        TestAnnotation annotation1 = new TestAnnotation();
        TestAnnotation annotation2 = new TestAnnotation();
        map._add(annotation1);
        map._add(annotation2);
        
        int result = map.size();
        
        assertEquals(2, result);
    }
    
    @Test
    public void testAddIfNotPresentShouldReturnTrueAndAddAnnotationWhenAnnotationsIsNull() {
        TestAnnotation annotation = new TestAnnotation();
        
        boolean result = map.addIfNotPresent(annotation);
        
        assertTrue(result);
        assertEquals(annotation, map.get(TestAnnotation.class));
    }
    
    @Test
    public void testAddIfNotPresentShouldReturnTrueAndAddAnnotationWhenAnnotationsDoesNotContainKey() {
        TestAnnotation annotation1 = new TestAnnotation();
        TestAnnotation annotation2 = new TestAnnotation();
        map._add(annotation1);
        
        boolean result = map.addIfNotPresent(annotation2);
        
        assertTrue(result);
        assertEquals(annotation2, map.get(TestAnnotation.class));
    }
    
    @Test
    public void testAddIfNotPresentShouldReturnFalseAndNotAddAnnotationWhenAnnotationsContainsKey() {
        TestAnnotation annotation = new TestAnnotation();
        map._add(annotation);
        
        boolean result = map.addIfNotPresent(annotation);
        
        assertFalse(result);
        assertEquals(annotation, map.get(TestAnnotation.class));
    }
    
    @Test
    public void testAddShouldReturnTrueAndAddAnnotation() {
        TestAnnotation annotation = new TestAnnotation();
        
        boolean result = map.add(annotation);
        
        assertTrue(result);
        assertEquals(annotation, map.get(TestAnnotation.class));
    }
    
    @Test
    public void testToStringShouldReturnNullWhenAnnotationsIsNull() {
        String result = map.toString();
        
        assertEquals("[null]", result);
    }
    
    @Test
    public void testToStringShouldReturnStringRepresentationOfAnnotations() {
        TestAnnotation annotation1 = new TestAnnotation();
        TestAnnotation annotation2 = new TestAnnotation();
        map._add(annotation1);
        map._add(annotation2);
        String expected = "{" + annotation1.toString() + ", " + annotation2.toString() + "}";
        
        String result = map.toString();
        
        assertEquals(expected, result);
    }
    
}