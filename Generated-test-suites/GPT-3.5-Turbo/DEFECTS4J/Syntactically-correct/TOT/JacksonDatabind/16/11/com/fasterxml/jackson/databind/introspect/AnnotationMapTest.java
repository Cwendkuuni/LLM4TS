import static org.junit.Assert.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGet_WhenAnnotationsMapIsNull_ShouldReturnNull() {
        assertNull(annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testGet_WhenAnnotationsMapIsEmpty_ShouldReturnNull() {
        annotationMap._annotations = Collections.emptyMap();
        assertNull(annotationMap.get(TestAnnotation.class));
    }

    @Retention(RetentionPolicy.RUNTIME)
    private @interface TestAnnotation {
    }

    @Test
    public void testGet_WhenAnnotationIsPresent_ShouldReturnAnnotation() {
        TestAnnotation testAnnotation = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        annotationMap._annotations.put(TestAnnotation.class, testAnnotation);

        assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testAnnotations_WhenAnnotationsMapIsNull_ShouldReturnEmptyList() {
        assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testAnnotations_WhenAnnotationsMapIsEmpty_ShouldReturnEmptyList() {
        annotationMap._annotations = Collections.emptyMap();
        assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testAnnotations_WhenAnnotationsMapIsNotEmpty_ShouldReturnListWithAllAnnotations() {
        TestAnnotation testAnnotation1 = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        TestAnnotation testAnnotation2 = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        annotationMap._annotations.put(TestAnnotation.class, testAnnotation1);
        annotationMap._annotations.put(TestAnnotation.class, testAnnotation2);

        List<Annotation> expectedAnnotations = new ArrayList<>();
        expectedAnnotations.add(testAnnotation1);
        expectedAnnotations.add(testAnnotation2);

        assertEquals(expectedAnnotations, annotationMap.annotations());
    }

    @Test
    public void testMerge_WhenPrimaryAnnotationMapIsNull_ShouldReturnSecondaryAnnotationMap() {
        AnnotationMap primaryAnnotationMap = null;
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();

        assertSame(secondaryAnnotationMap, AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap));
    }

    @Test
    public void testMerge_WhenPrimaryAnnotationMapIsEmpty_ShouldReturnSecondaryAnnotationMap() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        primaryAnnotationMap._annotations = Collections.emptyMap();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();

        assertSame(secondaryAnnotationMap, AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap));
    }

    @Test
    public void testMerge_WhenSecondaryAnnotationMapIsNull_ShouldReturnPrimaryAnnotationMap() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        AnnotationMap secondaryAnnotationMap = null;

        assertSame(primaryAnnotationMap, AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap));
    }

    @Test
    public void testMerge_WhenSecondaryAnnotationMapIsEmpty_ShouldReturnPrimaryAnnotationMap() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        secondaryAnnotationMap._annotations = Collections.emptyMap();

        assertSame(primaryAnnotationMap, AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap));
    }

    @Test
    public void testMerge_WhenPrimaryAndSecondaryAnnotationMapsAreEmpty_ShouldReturnEmptyAnnotationMap() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        primaryAnnotationMap._annotations = Collections.emptyMap();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        secondaryAnnotationMap._annotations = Collections.emptyMap();

        AnnotationMap expectedAnnotationMap = new AnnotationMap();
        expectedAnnotationMap._annotations = Collections.emptyMap();

        assertEquals(expectedAnnotationMap, AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap));
    }

    @Test
    public void testMerge_WhenPrimaryAnnotationMapHasElements_ShouldMergePrimaryAndSecondaryAnnotationMaps() {
        TestAnnotation testAnnotation1 = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        primaryAnnotationMap._annotations.put(TestAnnotation.class, testAnnotation1);

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        assertEquals(primaryAnnotationMap._annotations.size(), mergedAnnotationMap._annotations.size());
        assertTrue(mergedAnnotationMap._annotations.containsValue(testAnnotation1));
    }

    @Test
    public void testMerge_WhenSecondaryAnnotationMapHasElements_ShouldMergePrimaryAndSecondaryAnnotationMaps() {
        TestAnnotation testAnnotation2 = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        AnnotationMap primaryAnnotationMap = new AnnotationMap();

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        secondaryAnnotationMap._annotations.put(TestAnnotation.class, testAnnotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);

        assertEquals(secondaryAnnotationMap._annotations.size(), mergedAnnotationMap._annotations.size());
        assertTrue(mergedAnnotationMap._annotations.containsValue(testAnnotation2));
    }

    @Test
    public void testSize_WhenAnnotationsMapIsNull_ShouldReturnZero() {
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testSize_WhenAnnotationsMapIsEmpty_ShouldReturnZero() {
        annotationMap._annotations = Collections.emptyMap();
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testSize_WhenAnnotationsMapIsNotEmpty_ShouldReturnNumberOfAnnotations() {
        TestAnnotation testAnnotation1 = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        TestAnnotation testAnnotation2 = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        annotationMap._annotations.put(TestAnnotation.class, testAnnotation1);
        annotationMap._annotations.put(TestAnnotation.class, testAnnotation2);

        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent_WhenAnnotationsMapIsNull_ShouldAddAnnotationAndReturnTrue() {
        TestAnnotation testAnnotation = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        assertTrue(annotationMap.addIfNotPresent(testAnnotation));
        assertEquals(testAnnotation, annotationMap._annotations.get(TestAnnotation.class));
    }

    @Test
    public void testAddIfNotPresent_WhenAnnotationsMapIsEmpty_ShouldAddAnnotationAndReturnTrue() {
        annotationMap._annotations = Collections.emptyMap();
        TestAnnotation testAnnotation = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        assertTrue(annotationMap.addIfNotPresent(testAnnotation));
        assertEquals(testAnnotation, annotationMap._annotations.get(TestAnnotation.class));
    }

    @Test
    public void testAddIfNotPresent_WhenAnnotationsMapContainsAnnotation_ShouldNotAddAnnotationAndReturnFalse() {
        TestAnnotation testAnnotation = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        annotationMap._annotations.put(TestAnnotation.class, testAnnotation);
        assertFalse(annotationMap.addIfNotPresent(testAnnotation));
        assertEquals(testAnnotation, annotationMap._annotations.get(TestAnnotation.class));
    }

    @Test
    public void testAddIfNotPresent_WhenAnnotationsMapDoesNotContainAnnotation_ShouldAddAnnotationAndReturnTrue() {
        TestAnnotation testAnnotation1 = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        annotationMap._annotations.put(TestAnnotation.class, testAnnotation1);

        TestAnnotation testAnnotation2 = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        assertTrue(annotationMap.addIfNotPresent(testAnnotation2));
        assertEquals(testAnnotation2, annotationMap._annotations.get(TestAnnotation.class));
    }

    @Test
    public void testAdd_WhenAnnotationsMapIsNull_ShouldAddAnnotationAndReturnTrue() {
        TestAnnotation testAnnotation = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        assertTrue(annotationMap.add(testAnnotation));
        assertEquals(testAnnotation, annotationMap._annotations.get(TestAnnotation.class));
    }

    @Test
    public void testAdd_WhenAnnotationsMapIsEmpty_ShouldAddAnnotationAndReturnTrue() {
        annotationMap._annotations = Collections.emptyMap();
        TestAnnotation testAnnotation = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        assertTrue(annotationMap.add(testAnnotation));
        assertEquals(testAnnotation, annotationMap._annotations.get(TestAnnotation.class));
    }

    @Test
    public void testAdd_WhenAnnotationsMapContainsAnnotation_ShouldAddAnnotationAndReturnTrue() {
        TestAnnotation testAnnotation = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        annotationMap._annotations.put(TestAnnotation.class, testAnnotation);
        assertTrue(annotationMap.add(testAnnotation));
        assertEquals(testAnnotation, annotationMap._annotations.get(TestAnnotation.class));
    }

    @Test
    public void testAdd_WhenAnnotationsMapDoesNotContainAnnotation_ShouldAddAnnotationAndReturnTrue() {
        TestAnnotation testAnnotation1 = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        annotationMap._annotations.put(TestAnnotation.class, testAnnotation1);

        TestAnnotation testAnnotation2 = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        assertTrue(annotationMap.add(testAnnotation2));
        assertEquals(testAnnotation2, annotationMap._annotations.get(TestAnnotation.class));
    }

    @Test
    public void testToString_WhenAnnotationsMapIsNull_ShouldReturnNullString() {
        annotationMap._annotations = null;
        assertEquals("[null]", annotationMap.toString());
    }

    @Test
    public void testToString_WhenAnnotationsMapIsEmpty_ShouldReturnEmptyStringRepresentation() {
        annotationMap._annotations = Collections.emptyMap();
        assertEquals("{}", annotationMap.toString());
    }

    @Test
    public void testToString_WhenAnnotationsMapIsNotEmpty_ShouldReturnStringRepresentationOfAnnotations() {
        TestAnnotation testAnnotation1 = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        TestAnnotation testAnnotation2 = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return TestAnnotation.class;
            }
        };
        annotationMap._annotations.put(TestAnnotation.class, testAnnotation1);
        annotationMap._annotations.put(TestAnnotation.class, testAnnotation2);

        assertEquals("{class AnnotationMapTest$TestAnnotation=interface AnnotationMapTest$TestAnnotation}", annotationMap.toString());
    }
}