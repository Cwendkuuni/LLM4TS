import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGetShouldReturnNullWhenNoAnnotationsPresent() {
        assertNull(annotationMap.get(Annotation.class));
    }

    @Test
    public void testGetShouldReturnAnnotationWhenPresent() {
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        annotationMap.add(annotation);
        assertEquals(annotation, annotationMap.get(annotation.getClass()));
    }

    @Test
    public void testAnnotationsShouldReturnEmptyListWhenNoAnnotationsPresent() {
        Iterable<Annotation> annotations = annotationMap.annotations();
        assertFalse(annotations.iterator().hasNext());
    }

    @Test
    public void testAnnotationsShouldReturnAllAnnotationsWhenPresent() {
        Annotation annotation1 = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        Annotation annotation2 = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);
        Iterable<Annotation> annotations = annotationMap.annotations();
        List<Annotation> expectedAnnotations = Arrays.asList(annotation1, annotation2);
        assertEquals(expectedAnnotations, annotations);
    }

    @Test
    public void testMergeShouldReturnNullWhenBothPrimaryAndSecondaryAreNull() {
        AnnotationMap primary = null;
        AnnotationMap secondary = null;
        assertNull(AnnotationMap.merge(primary, secondary));
    }

    @Test
    public void testMergeShouldReturnSecondaryWhenPrimaryIsNull() {
        AnnotationMap primary = null;
        AnnotationMap secondary = new AnnotationMap();
        assertEquals(secondary, AnnotationMap.merge(primary, secondary));
    }

    @Test
    public void testMergeShouldReturnPrimaryWhenSecondaryIsNull() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = null;
        assertEquals(primary, AnnotationMap.merge(primary, secondary));
    }

    @Test
    public void testMergeShouldReturnPrimaryWhenBothPrimaryAndSecondaryArePresent() {
        Annotation annotation1 = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        Annotation annotation2 = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        AnnotationMap primary = new AnnotationMap();
        primary.add(annotation1);
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(annotation2);
        AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
        assertTrue(mergedMap.get(annotation1.getClass()) != null);
        assertTrue(mergedMap.get(annotation2.getClass()) != null);
    }

    @Test
    public void testSizeShouldReturnZeroWhenNoAnnotationsPresent() {
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testSizeShouldReturnNumberOfAnnotationsWhenPresent() {
        Annotation annotation1 = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        Annotation annotation2 = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresentShouldReturnTrueWhenAnnotationNotPresent() {
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        assertTrue(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAddIfNotPresentShouldReturnFalseWhenAnnotationPresent() {
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        annotationMap.add(annotation);
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAddShouldReturnTrueWhenAnnotationAdded() {
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        assertTrue(annotationMap.add(annotation));
    }

    @Test
    public void testAddShouldReturnFalseWhenAnnotationNotAdded() {
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        annotationMap.add(annotation);
        assertFalse(annotationMap.add(annotation));
    }

    @Test
    public void testToStringShouldReturnNullWhenNoAnnotationsPresent() {
        assertEquals("[null]", annotationMap.toString());
    }

    @Test
    public void testToStringShouldReturnAnnotationsStringRepresentationWhenPresent() {
        Annotation annotation1 = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        Annotation annotation2 = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);
        assertEquals(annotationMap._annotations.toString(), annotationMap.toString());
    }

}