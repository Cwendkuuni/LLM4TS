package com.fasterxml.jackson.databind.introspect;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setup() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGet_shouldReturnNull_whenAnnotationsIsNull() {
        // Arrange

        // Act
        Annotation result = annotationMap.get(Annotation.class);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGet_shouldReturnAnnotation_whenAnnotationsContainsRequestedClass() {
        // Arrange
        Annotation annotation = createAnnotation();

        Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Annotation.class, annotation);

        annotationMap = new AnnotationMap(annotations);

        // Act
        Annotation result = annotationMap.get(Annotation.class);

        // Assert
        assertEquals(annotation, result);
    }

    @Test
    public void testGet_shouldReturnNull_whenAnnotationsDoesNotContainRequestedClass() {
        // Arrange
        Annotation annotation = createAnnotation();

        Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Annotation.class, annotation);

        annotationMap = new AnnotationMap(annotations);

        // Act
        Annotation result = annotationMap.get(AnotherAnnotation.class);

        // Assert
        assertNull(result);
    }

    @Test
    public void testAnnotations_shouldReturnEmptyList_whenAnnotationsIsNull() {
        // Arrange

        // Act
        Iterable<Annotation> result = annotationMap.annotations();

        // Assert
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testAnnotations_shouldReturnValues_whenAnnotationsContainsValues() {
        // Arrange
        Annotation annotation1 = createAnnotation();
        Annotation annotation2 = createAnnotation();

        Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Annotation.class, annotation1);
        annotations.put(AnotherAnnotation.class, annotation2);

        annotationMap = new AnnotationMap(annotations);

        // Act
        Iterable<Annotation> result = annotationMap.annotations();

        // Assert
        assertTrue(result.contains(annotation1));
        assertTrue(result.contains(annotation2));
    }

    @Test
    public void testMerge_shouldReturnSecondary_whenPrimaryIsNull() {
        // Arrange
        AnnotationMap primary = null;

        AnnotationMap secondary = createAnnotationMap();

        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        // Assert
        assertEquals(secondary, result);
    }

    @Test
    public void testMerge_shouldReturnPrimary_whenSecondaryIsNull() {
        // Arrange
        AnnotationMap primary = createAnnotationMap();

        AnnotationMap secondary = null;

        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        // Assert
        assertEquals(primary, result);
    }

    @Test
    public void testMerge_shouldIncludeSecondaryAnnotations_whenPrimaryAndSecondaryBothContainAnnotations() {
        // Arrange
        AnnotationMap primary = createAnnotationMap();

        AnnotationMap secondary = createAnnotationMap();

        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        // Assert
        assertTrue(result.get(Annotation.class) instanceof Annotation);
        assertTrue(result.get(AnotherAnnotation.class) instanceof Annotation);
    }

    @Test
    public void testSize_shouldReturnZero_whenAnnotationsIsNull() {
        // Arrange

        // Act
        int result = annotationMap.size();

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testSize_shouldReturnNumberOfAnnotations_whenAnnotationsIsNotEmpty() {
        // Arrange
        Annotation annotation = createAnnotation();

        Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Annotation.class, annotation);

        annotationMap = new AnnotationMap(annotations);

        // Act
        int result = annotationMap.size();

        // Assert
        assertEquals(1, result);
    }

    @Test
    public void testAddIfNotPresent_shouldAddAnnotation_whenAnnotationDoesNotExist() {
        // Arrange
        Annotation annotation = createAnnotation();

        // Act
        boolean result = annotationMap.addIfNotPresent(annotation);

        // Assert
        assertTrue(result);
        assertTrue(annotationMap.get(Annotation.class) instanceof Annotation);
    }

    @Test
    public void testAddIfNotPresent_shouldNotAddAnnotation_whenAnnotationExists() {
        // Arrange
        Annotation annotation = createAnnotation();
        annotationMap.add(annotation);

        // Act
        boolean result = annotationMap.addIfNotPresent(annotation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAdd_shouldAddAnnotation() {
        // Arrange
        Annotation annotation = createAnnotation();

        // Act
        boolean result = annotationMap.add(annotation);

        // Assert
        assertTrue(result);
        assertTrue(annotationMap.get(Annotation.class) instanceof Annotation);
    }

    @Test
    public void testToString_shouldReturnNull_whenAnnotationsIsNull() {
        // Arrange

        // Act
        String result = annotationMap.toString();

        // Assert
        assertEquals("[null]", result);
    }

    @Test
    public void testToString_shouldReturnAnnotations_whenAnnotationsIsNotEmpty() {
        // Arrange
        Annotation annotation = createAnnotation();

        Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Annotation.class, annotation);

        annotationMap = new AnnotationMap(annotations);

        // Act
        String result = annotationMap.toString();

        // Assert
        assertEquals(annotations.toString(), result);
    }

    private Annotation createAnnotation() {
        return new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        };
    }

    private AnnotationMap createAnnotationMap() {
        Annotation annotation1 = createAnnotation();
        Annotation annotation2 = createAnnotation();

        Map<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Annotation.class, annotation1);
        annotations.put(AnotherAnnotation.class, annotation2);

        return new AnnotationMap(annotations);
    }

    private static class AnotherAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }
    }
}