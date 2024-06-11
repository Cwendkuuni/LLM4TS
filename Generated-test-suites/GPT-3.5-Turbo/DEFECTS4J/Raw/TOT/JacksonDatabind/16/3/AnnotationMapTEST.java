
package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGet_AnnotationExists_ReturnsAnnotation() {
        // Arrange
        DummyAnnotation dummy = new DummyAnnotation();

        // Act
        annotationMap.add(dummy);

        // Assert
        assertEquals(dummy, annotationMap.get(DummyAnnotation.class));
    }

    @Test
    public void testGet_AnnotationDoesNotExist_ReturnsNull() {
        // Act
        Annotation result = annotationMap.get(DummyAnnotation.class);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGet_AnnotationWithMultipleTypes_ReturnsCorrectAnnotation() {
        // Arrange
        DummyAnnotationWithTypes dummy = new DummyAnnotationWithTypes();

        // Act
        annotationMap.add(dummy);

        // Assert
        assertEquals(dummy, annotationMap.get(DummyAnnotation.class));
        assertEquals(dummy, annotationMap.get(DummyMarkerAnnotation.class));
    }

    @Test
    public void testAnnotations_EmptyAnnotationMap_ReturnsEmptyIterable() {
        // Act
        Iterable<Annotation> result = annotationMap.annotations();

        // Assert
        assertFalse(result.iterator().hasNext());
    }

    @Test
    public void testAnnotations_NonEmptyAnnotationMap_ReturnsIterableWithAnnotations() {
        // Arrange
        DummyAnnotation dummy = new DummyAnnotation();

        // Act
        annotationMap.add(dummy);
        Iterable<Annotation> result = annotationMap.annotations();

        // Assert
        assertTrue(result.iterator().hasNext());
        assertEquals(dummy, result.iterator().next());
    }

    @Test
    public void testMerge_PrimaryAndSecondaryAreNull_ReturnsNull() {
        // Act
        AnnotationMap result = AnnotationMap.merge(null, null);

        // Assert
        assertNull(result);
    }

    @Test
    public void testMerge_PrimaryIsNull_ReturnsSecondary() {
        // Arrange
        AnnotationMap secondary = new AnnotationMap();

        // Act
        AnnotationMap result = AnnotationMap.merge(null, secondary);

        // Assert
        assertEquals(secondary, result);
    }

    @Test
    public void testMerge_SecondaryIsNull_ReturnsPrimary() {
        // Arrange
        AnnotationMap primary = new AnnotationMap();

        // Act
        AnnotationMap result = AnnotationMap.merge(primary, null);

        // Assert
        assertEquals(primary, result);
    }

    @Test
    public void testMerge_BothMapsAreEmpty_ReturnsEmptyMap() {
        // Arrange
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testMerge_PrimaryMapIsEmpty_ReturnsSecondaryMap() {
        // Arrange
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        DummyAnnotation dummy = new DummyAnnotation();
        secondary.add(dummy);

        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        // Assert
        assertEquals(secondary, result);
        assertEquals(dummy, result.get(DummyAnnotation.class));
    }

    @Test
    public void testMerge_SecondaryMapIsEmpty_ReturnsPrimaryMap() {
        // Arrange
        AnnotationMap primary = new AnnotationMap();
        DummyAnnotation dummy = new DummyAnnotation();
        primary.add(dummy);
        AnnotationMap secondary = new AnnotationMap();

        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        // Assert
        assertEquals(primary, result);
        assertEquals(dummy, result.get(DummyAnnotation.class));
    }

    @Test
    public void testMerge_BothMapsHaveSameAnnotation_ReturnsCombinedMap() {
        // Arrange
        AnnotationMap primary = new AnnotationMap();
        DummyAnnotation dummy = new DummyAnnotation();
        primary.add(dummy);
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(dummy);

        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        // Assert
        assertEquals(1, result.size());
        assertEquals(dummy, result.get(DummyAnnotation.class));
    }

    @Test
    public void testMerge_BothMapsHaveDifferentAnnotations_ReturnsCombinedMap() {
        // Arrange
        AnnotationMap primary = new AnnotationMap();
        DummyAnnotation dummy1 = new DummyAnnotation();
        primary.add(dummy1);
        AnnotationMap secondary = new AnnotationMap();
        DummyAnnotation dummy2 = new DummyAnnotation();
        secondary.add(dummy2);

        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        // Assert
        assertEquals(2, result.size());
        assertEquals(dummy1, result.get(DummyAnnotation.class));
        assertEquals(dummy2, result.get(DummyAnnotation.class));
    }

    @Test
    public void testSize_EmptyAnnotationMap_ReturnsZero() {
        // Act
        int result = annotationMap.size();

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testSize_NonEmptyAnnotationMap_ReturnsNumberOfAnnotations() {
        // Arrange
        DummyAnnotation dummy = new DummyAnnotation();
        annotationMap.add(dummy);

        // Act
        int result = annotationMap.size();

        // Assert
        assertEquals(1, result);
    }

    @Test
    public void testAddIfNotPresent_AnnotationDoesNotExist_AddsAnnotation() {
        // Arrange
        DummyAnnotation dummy = new DummyAnnotation();

        // Act
        boolean result = annotationMap.addIfNotPresent(dummy);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAddIfNotPresent_AnnotationExist_DoesNotAddAnnotation() {
        // Arrange
        DummyAnnotation dummy = new DummyAnnotation();
        annotationMap.add(dummy);

        // Act
        boolean result = annotationMap.addIfNotPresent(dummy);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAdd_AnnotationDoesNotExist_AddsAnnotation() {
        // Arrange
        DummyAnnotation dummy = new DummyAnnotation();

        // Act
        boolean result = annotationMap.add(dummy);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAdd_AnnotationExist_OverwritesPreviousAnnotationAndReturnsFalse() {
        // Arrange
        DummyAnnotation dummy = new DummyAnnotation();
        annotationMap.add(dummy);
        DummyAnnotation updatedDummy = new DummyAnnotation();

        // Act
        boolean result = annotationMap.add(updatedDummy);

        // Assert
        assertFalse(result);
        assertEquals(updatedDummy, annotationMap.get(DummyAnnotation.class));
    }

    @Test
    public void testToString_EmptyAnnotationMap_ReturnsStringRepresentationOfEmptyMap() {
        // Act
        String result = annotationMap.toString();

        // Assert
        assertEquals("[null]", result);
    }

    @Test
    public void testToString_NonEmptyAnnotationMap_ReturnsStringRepresentationOfMap() {
        // Arrange
        DummyAnnotation dummy = new DummyAnnotation();
        annotationMap.add(dummy);

        // Act
        String result = annotationMap.toString();

        // Assert
        assertEquals("{class com.fasterxml.jackson.databind.introspect.AnnotationMapTest$DummyAnnotation=DUMMY}", result);
    }

    // Dummy annotations for testing
    private static class DummyAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return DummyAnnotation.class;
        }

        @Override
        public boolean equals(Object obj) {
            return obj.getClass().equals(DummyAnnotation.class);
        }

        @Override
        public int hashCode() {
            return DummyAnnotation.class.hashCode();
        }

        @Override
        public String toString() {
            return "DUMMY";
        }
    }

    private static class DummyAnnotationWithTypes implements DummyAnnotation, DummyMarkerAnnotation {
        // Empty class
    }

    private static interface DummyMarkerAnnotation extends Annotation {
        // Empty interface
    }
}

