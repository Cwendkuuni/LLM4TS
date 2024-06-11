
package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class AnnotationMapTest {
    
    // Test Case 1: Test get() method when annotation exists
    @Test
    public void testGetAnnotationExists() {
        // Arrange
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        annotationMap.addIfNotPresent(annotation);
        
        // Act
        MyAnnotation result = annotationMap.get(MyAnnotation.class);
        
        // Assert
        assertEquals(annotation, result);
    }
    
    // Test Case 2: Test get() method when annotation does not exist
    @Test
    public void testGetAnnotationNotExists() {
        // Arrange
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Act
        MyAnnotation result = annotationMap.get(MyAnnotation.class);
        
        // Assert
        assertNull(result);
    }
    
    // Test Case 3: Test annotations() method when map is empty
    @Test
    public void testAnnotationsEmptyMap() {
        // Arrange
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Act
        Iterable<Annotation> result = annotationMap.annotations();
        
        // Assert
        assertEquals(Collections.emptyList(), result);
    }
    
    // Test Case 4: Test annotations() method when map is not empty
    @Test
    public void testAnnotationsNonEmptyMap() {
        // Arrange
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap.addIfNotPresent(annotation1);
        annotationMap.addIfNotPresent(annotation2);
        List<MyAnnotation> expectedResult = Arrays.asList(annotation1, annotation2);
        
        // Act
        Iterable<Annotation> result = annotationMap.annotations();
        
        // Assert
        assertEquals(expectedResult, result);
    }
    
    // Test Case 5: Test merge() method when primary and secondary AnnotationMaps are null
    @Test
    public void testMergeNullAnnotationMaps() {
        // Arrange
        AnnotationMap primary = null;
        AnnotationMap secondary = null;
        
        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        // Assert
        assertNull(result);
    }
    
    // Test Case 6: Test merge() method when primary AnnotationMap is null
    @Test
    public void testMergePrimaryNull() {
        // Arrange
        AnnotationMap primary = null;
        AnnotationMap secondary = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        secondary.addIfNotPresent(annotation);
        
        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        // Assert
        assertEquals(secondary, result);
    }
    
    // Test Case 7: Test merge() method when secondary AnnotationMap is null
    @Test
    public void testMergeSecondaryNull() {
        // Arrange
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = null;
        MyAnnotation annotation = new MyAnnotation();
        primary.addIfNotPresent(annotation);
        
        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        // Assert
        assertEquals(primary, result);
    }
    
    // Test Case 8: Test merge() method when both primary and secondary AnnotationMaps are not null
    @Test
    public void testMergeBothNotNull() {
        // Arrange
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();
        primary.addIfNotPresent(annotation1);
        secondary.addIfNotPresent(annotation2);
        List<MyAnnotation> expectedResult = Arrays.asList(annotation1, annotation2);
        
        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        // Assert
        assertEquals(expectedResult, result.annotations());
    }
    
    // Test Case 9: Test size() method when map is empty
    @Test
    public void testSizeEmptyMap() {
        // Arrange
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Act
        int result = annotationMap.size();
        
        // Assert
        assertEquals(0, result);
    }
    
    // Test Case 10: Test size() method when map is not empty
    @Test
    public void testSizeNonEmptyMap() {
        // Arrange
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap.addIfNotPresent(annotation1);
        annotationMap.addIfNotPresent(annotation2);
        
        // Act
        int result = annotationMap.size();
        
        // Assert
        assertEquals(2, result);
    }
    
    // Test Case 11: Test addIfNotPresent() method when annotation does not exist
    @Test
    public void testAddIfNotPresentAnnotationNotExists() {
        // Arrange
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        
        // Act
        boolean result = annotationMap.addIfNotPresent(annotation);
        
        // Assert
        assertTrue(result);
    }
    
    // Test Case 12: Test addIfNotPresent() method when annotation already exists
    @Test
    public void testAddIfNotPresentAnnotationExists() {
        // Arrange
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        annotationMap.addIfNotPresent(annotation);
        
        // Act
        boolean result = annotationMap.addIfNotPresent(annotation);
        
        // Assert
        assertFalse(result);
    }
    
    // Test Case 13: Test add() method when annotation does not exist
    @Test
    public void testAddAnnotationNotExists() {
        // Arrange
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        
        // Act
        boolean result = annotationMap.add(annotation);
        
        // Assert
        assertTrue(result);
    }
    
    // Test Case 14: Test add() method when annotation already exists
    @Test
    public void testAddAnnotationExists() {
        // Arrange
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        annotationMap.addIfNotPresent(annotation);
        
        // Act
        boolean result = annotationMap.add(annotation);
        
        // Assert
        assertFalse(result);
    }
    
    // Test Case 15: Test toString() method when map is empty
    @Test
    public void testToStringEmptyMap() {
        // Arrange
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Act
        String result = annotationMap.toString();
        
        // Assert
        assertEquals("[null]", result);
    }
    
    // Test Case 16: Test toString() method when map is not empty
    @Test
    public void testToStringNonEmptyMap() {
        // Arrange
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap.addIfNotPresent(annotation1);
        annotationMap.addIfNotPresent(annotation2);
        String expectedResult = new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(annotation1.annotationType(), annotation1);
            put(annotation2.annotationType(), annotation2);
        }}.toString();
        
        // Act
        String result = annotationMap.toString();
        
        // Assert
        assertEquals(expectedResult, result);
    }
    
    private static class MyAnnotation implements Annotation {
        
        @Override
        public Class<? extends Annotation> annotationType() {
            return MyAnnotation.class;
        }
    }
    
}
