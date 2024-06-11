
import org.junit.*;
import static org.junit.Assert.*;
import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationMapTest {

    @Test
    public void testGet_WhenAnnotationExists_ShouldReturnAnnotation() {
        // Arrange
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        annotationMap.add(annotation);

        // Act
        Annotation result = annotationMap.get(Annotation.class);

        // Assert
        assertNotNull(result);
        assertEquals(annotation, result);
    }

    @Test
    public void testGet_WhenAnnotationDoesNotExist_ShouldReturnNull() {
        // Arrange
        AnnotationMap annotationMap = new AnnotationMap();

        // Act
        Annotation result = annotationMap.get(Annotation.class);

        // Assert
        assertNull(result);
    }

    @Test
    public void testAnnotations_WhenAnnotationsExist_ShouldReturnIterableWithAnnotations() {
        // Arrange
        Annotation annotation1 = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        Annotation annotation2 = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(annotation1.annotationType(), annotation1);
        annotations.put(annotation2.annotationType(), annotation2);
        AnnotationMap annotationMap = new AnnotationMap(annotations);

        // Act
        Iterable<Annotation> result = annotationMap.annotations();

        // Assert
        assertNotNull(result);
        List<Annotation> resultList = new ArrayList<>();
        for (Annotation annotation : result) {
            resultList.add(annotation);
        }
        assertEquals(2, resultList.size());
        assertTrue(resultList.contains(annotation1));
        assertTrue(resultList.contains(annotation2));
    }

    @Test
    public void testAnnotations_WhenAnnotationsDoNotExist_ShouldReturnEmptyList() {
        // Arrange
        AnnotationMap annotationMap = new AnnotationMap();

        // Act
        Iterable<Annotation> result = annotationMap.annotations();

        // Assert
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
    }

    @Test
    public void testMerge_WhenPrimaryAndSecondaryAnnotationsExist_ShouldReturnMergedAnnotationMap() {
        // Arrange
        Annotation annotation1 = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        primaryAnnotationMap.add(annotation1);

        Annotation annotation2 = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        secondaryAnnotationMap.add(annotation2);

        // Act
        AnnotationMap result = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(annotation1, result.get(annotation1.annotationType()));
        assertEquals(annotation2, result.get(annotation2.annotationType()));
    }

    @Test
    public void testMerge_WhenPrimaryAnnotationsDoNotExist_ShouldReturnSecondaryAnnotationMap() {
        // Arrange
        AnnotationMap primaryAnnotationMap = new AnnotationMap();

        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        secondaryAnnotationMap.add(annotation);

        // Act
        AnnotationMap result = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(annotation, result.get(annotation.annotationType()));
    }

    @Test
    public void testMerge_WhenSecondaryAnnotationsDoNotExist_ShouldReturnPrimaryAnnotationMap() {
        // Arrange
        AnnotationMap primaryAnnotationMap = new AnnotationMap();

        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        primaryAnnotationMap.add(annotation);

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();

        // Act
        AnnotationMap result = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(annotation, result.get(annotation.annotationType()));
    }

    @Test
    public void testMerge_WhenPrimaryAndSecondaryAnnotationsDoNotExist_ShouldReturnNull() {
        // Arrange
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();

        // Act
        AnnotationMap result = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        // Assert
        assertNull(result);
    }

    @Test
    public void testSize_WhenAnnotationsExist_ShouldReturnSize() {
        // Arrange
        Annotation annotation1 = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        Annotation annotation2 = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(annotation1.annotationType(), annotation1);
        annotations.put(annotation2.annotationType(), annotation2);
        AnnotationMap annotationMap = new AnnotationMap(annotations);

        // Act
        int result = annotationMap.size();

        // Assert
        assertEquals(2, result);
    }

    @Test
    public void testSize_WhenAnnotationsDoNotExist_ShouldReturnZero() {
        // Arrange
        AnnotationMap annotationMap = new AnnotationMap();

        // Act
        int result = annotationMap.size();

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testAddIfNotPresent_WhenAnnotationDoesNotExist_ShouldAddAndReturnTrue() {
        // Arrange
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        AnnotationMap annotationMap = new AnnotationMap();

        // Act
        boolean result = annotationMap.addIfNotPresent(annotation);

        // Assert
        assertTrue(result);
        assertEquals(annotation, annotationMap.get(annotation.annotationType()));
    }

    @Test
    public void testAddIfNotPresent_WhenAnnotationExists_ShouldNotAddAndReturnFalse() {
        // Arrange
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(annotation);

        // Act
        boolean result = annotationMap.addIfNotPresent(annotation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAdd_WhenAnnotationDoesNotExist_ShouldAddAndReturnTrue() {
        // Arrange
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        AnnotationMap annotationMap = new AnnotationMap();

        // Act
        boolean result = annotationMap.add(annotation);

        // Assert
        assertTrue(result);
        assertEquals(annotation, annotationMap.get(annotation.annotationType()));
    }

    @Test
    public void testAdd_WhenAnnotationExists_ShouldAddAndReturnTrue() {
        // Arrange
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(annotation);

        // Act
        boolean result = annotationMap.add(annotation);

        // Assert
        assertTrue(result);
        assertEquals(annotation, annotationMap.get(annotation.annotationType()));
    }
    
    @Test
    public void testToString_WhenAnnotationsExist_ShouldReturnStringRepresentation() {
        // Arrange
        Annotation annotation1 = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        Annotation annotation2 = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(annotation1.annotationType(), annotation1);
        annotations.put(annotation2.annotationType(), annotation2);
        AnnotationMap annotationMap = new AnnotationMap(annotations);

        // Act
        String result = annotationMap.toString();

        // Assert
        assertEquals(annotations.toString(), result);
    }

    @Test
    public void testToString_WhenAnnotationsDoNotExist_ShouldReturnNull() {
        // Arrange
        AnnotationMap annotationMap = new AnnotationMap();

        // Act
        String result = annotationMap.toString();

        // Assert
        assertEquals("[null]", result);
    }

}

