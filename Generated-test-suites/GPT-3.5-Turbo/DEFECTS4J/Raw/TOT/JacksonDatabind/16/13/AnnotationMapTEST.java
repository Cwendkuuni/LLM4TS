import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.introspect.AnnotationMap;

import static org.junit.Assert.*;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGet_ExistingAnnotation_ReturnsAnnotation() {
        // Arrange
        Deprecated deprecatedAnnotation = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        annotationMap.add(deprecatedAnnotation);

        // Act
        Deprecated result = annotationMap.get(Deprecated.class);

        // Assert
        assertEquals(deprecatedAnnotation, result);
    }

    @Test
    public void testGet_NonExistingAnnotation_ReturnsNull() {
        // Act
        Deprecated result = annotationMap.get(Deprecated.class);

        // Assert
        assertNull(result);
    }

    @Test
    public void testAnnotations_EmptyMap_ReturnsEmptyIterable() {
        // Act
        Iterable<Annotation> result = annotationMap.annotations();

        // Assert
        assertFalse(result.iterator().hasNext());
    }

    @Test
    public void testAnnotations_MapWithAnnotations_ReturnsIterableOfAnnotations() {
        // Arrange
        Deprecated deprecatedAnnotation = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        annotationMap.add(deprecatedAnnotation);

        // Act
        Iterable<Annotation> result = annotationMap.annotations();

        // Assert
        assertTrue(result.iterator().hasNext());
        assertEquals(deprecatedAnnotation, result.iterator().next());
    }

    @Test
    public void testMerge_BothMapsAreNull_ReturnsNull() {
        // Act
        AnnotationMap result = AnnotationMap.merge(null, null);

        // Assert
        assertNull(result);
    }

    @Test
    public void testMerge_PrimaryMapIsNull_ReturnsSecondaryMap() {
        // Arrange
        AnnotationMap secondaryMap = new AnnotationMap();

        // Act
        AnnotationMap result = AnnotationMap.merge(null, secondaryMap);

        // Assert
        assertEquals(secondaryMap, result);
    }

    @Test
    public void testMerge_SecondaryMapIsNull_ReturnsPrimaryMap() {
        // Arrange
        AnnotationMap primaryMap = new AnnotationMap();

        // Act
        AnnotationMap result = AnnotationMap.merge(primaryMap, null);

        // Assert
        assertEquals(primaryMap, result);
    }

    @Test
    public void testMerge_BothMapsAreEmpty_ReturnsEmptyMap() {
        // Arrange
        AnnotationMap primaryMap = new AnnotationMap();
        AnnotationMap secondaryMap = new AnnotationMap();

        // Act
        AnnotationMap result = AnnotationMap.merge(primaryMap, secondaryMap);

        // Assert
        assertEquals(primaryMap, result);
    }

    @Test
    public void testMerge_OnlyPrimaryMapIsNonEmpty_ReturnsPrimaryMap() {
        // Arrange
        AnnotationMap primaryMap = new AnnotationMap();
        Deprecated deprecatedAnnotation = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        primaryMap.add(deprecatedAnnotation);

        // Act
        AnnotationMap result = AnnotationMap.merge(primaryMap, null);

        // Assert
        assertEquals(primaryMap, result);
    }

    @Test
    public void testMerge_OnlySecondaryMapIsNonEmpty_ReturnsSecondaryMap() {
        // Arrange
        AnnotationMap primaryMap = new AnnotationMap();
        AnnotationMap secondaryMap = new AnnotationMap();
        Deprecated deprecatedAnnotation = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        secondaryMap.add(deprecatedAnnotation);

        // Act
        AnnotationMap result = AnnotationMap.merge(primaryMap, secondaryMap);

        // Assert
        assertEquals(secondaryMap, result);
    }

    @Test
    public void testMerge_BothMapsAreNonEmpty_ReturnsMergedMapWithOverriddenAnnotations() {
        // Arrange
        AnnotationMap primaryMap = new AnnotationMap();
        Deprecated deprecatedAnnotation1 = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        primaryMap.add(deprecatedAnnotation1);

        AnnotationMap secondaryMap = new AnnotationMap();
        Deprecated deprecatedAnnotation2 = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        secondaryMap.add(deprecatedAnnotation2);

        // Act
        AnnotationMap result = AnnotationMap.merge(primaryMap, secondaryMap);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        // The merged map should contain overridden annotations from the secondary map
        Deprecated resultAnnotation1 = result.get(Deprecated.class);
        Deprecated resultAnnotation2 = result.get(Deprecated.class);
        assertEquals(deprecatedAnnotation2, resultAnnotation1);
        assertEquals(deprecatedAnnotation2, resultAnnotation2);
        assertNotEquals(deprecatedAnnotation1, resultAnnotation1);
        assertNotEquals(deprecatedAnnotation1, resultAnnotation2);
    }

    @Test
    public void testSize_EmptyMap_ReturnsZero() {
        // Act
        int result = annotationMap.size();

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testSize_MapWithAnnotations_ReturnsNumberOfAnnotations() {
        // Arrange
        Deprecated deprecatedAnnotation = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        annotationMap.add(deprecatedAnnotation);

        // Act
        int result = annotationMap.size();

        // Assert
        assertEquals(1, result);
    }

    @Test
    public void testAddIfNotPresent_NonExistingAnnotation_ReturnsTrue() {
        // Arrange
        Deprecated deprecatedAnnotation = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };

        // Act
        boolean result = annotationMap.addIfNotPresent(deprecatedAnnotation);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAddIfNotPresent_ExistingAnnotation_ReturnsFalse() {
        // Arrange
        Deprecated deprecatedAnnotation = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        annotationMap.add(deprecatedAnnotation);

        // Act
        boolean result = annotationMap.addIfNotPresent(deprecatedAnnotation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAdd_AddsAnnotationToMap_ReturnsTrue() {
        // Arrange
        Deprecated deprecatedAnnotation = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };

        // Act
        boolean result = annotationMap.add(deprecatedAnnotation);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testToString_EmptyMap_ReturnsNullString() {
        // Act
        String result = annotationMap.toString();

        // Assert
        assertEquals("[null]", result);
    }

    @Test
    public void testToString_MapWithAnnotations_ReturnsStringRepresentation() {
        // Arrange
        Deprecated deprecatedAnnotation = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        annotationMap.add(deprecatedAnnotation);

        // Act
        String result = annotationMap.toString();

        // Assert
        assertTrue(result.startsWith("{"));
        assertTrue(result.endsWith("}"));
        assertTrue(result.contains(Deprecated.class.getSimpleName()));
    }
}