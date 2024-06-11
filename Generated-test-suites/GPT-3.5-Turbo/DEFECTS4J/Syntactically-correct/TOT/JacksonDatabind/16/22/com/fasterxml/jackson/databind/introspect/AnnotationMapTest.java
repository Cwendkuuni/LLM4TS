import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.Iterator;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGet_NullAnnotations_ReturnsNull() {
        assertNull(annotationMap.get(Deprecated.class));
    }

    @Test
    public void testGet_AnnotationExists_ReturnsAnnotation() {
        Deprecated deprecated = new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        annotationMap.add(deprecated);
        assertEquals(deprecated, annotationMap.get(Deprecated.class));
    }

    @Test
    public void testGet_AnnotationDoesNotExist_ReturnsNull() {
        assertNull(annotationMap.get(Deprecated.class));
    }

    @Test
    public void testAnnotations_EmptyAnnotations_ReturnsEmptyIterator() {
        Iterator<Annotation> iterator = annotationMap.annotations().iterator();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAnnotations_NonEmptyAnnotations_ReturnsCorrectSize() {
        Deprecated deprecated = new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        annotationMap.add(deprecated);
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testMerge_NullPrimary_ReturnsSecondary() {
        AnnotationMap secondary = new AnnotationMap();
        Deprecated deprecated = new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        secondary.add(deprecated);

        AnnotationMap result = AnnotationMap.merge(null, secondary);
        assertEquals(deprecated, result.get(Deprecated.class));
    }

    @Test
    public void testMerge_NullSecondary_ReturnsPrimary() {
        AnnotationMap primary = new AnnotationMap();
        Deprecated deprecated = new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        primary.add(deprecated);

        AnnotationMap result = AnnotationMap.merge(primary, null);
        assertEquals(deprecated, result.get(Deprecated.class));
    }

    @Test
    public void testMerge_BothNull_ReturnsNull() {
        AnnotationMap result = AnnotationMap.merge(null, null);
        assertNull(result);
    }

    @Test
    public void testAddIfNotPresent_AnnotationNotPresent_ReturnsTrue() {
        Deprecated deprecated = new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };

        assertTrue(annotationMap.addIfNotPresent(deprecated));
        assertEquals(deprecated, annotationMap.get(Deprecated.class));
    }

    @Test
    public void testAddIfNotPresent_AnnotationAlreadyPresent_ReturnsFalse() {
        Deprecated deprecated = new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        annotationMap.add(deprecated);

        assertFalse(annotationMap.addIfNotPresent(deprecated));
    }

    @Test
    public void testToString_EmptyAnnotations_ReturnsNull() {
        assertEquals("[null]", annotationMap.toString());
    }

    @Test
    public void testToString_NonEmptyAnnotations_ReturnsAnnotationsString() {
        Deprecated deprecated = new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        annotationMap.add(deprecated);

        assertEquals("{@java.lang.Deprecated=interface java.lang.Deprecated}", annotationMap.toString());
    }
}