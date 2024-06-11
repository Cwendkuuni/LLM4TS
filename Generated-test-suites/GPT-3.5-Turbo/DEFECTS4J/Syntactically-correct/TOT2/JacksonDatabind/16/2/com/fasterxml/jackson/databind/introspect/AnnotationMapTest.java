import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGetAnnotation() {
        assertNull(annotationMap.get(Deprecated.class));
        
        Deprecated deprecated = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        
        annotationMap.add(deprecated);
        assertEquals(deprecated, annotationMap.get(Deprecated.class));
    }

    @Test
    public void testAnnotationsMethod() {
        assertTrue(annotationMap.annotations().iterator().hasNext());
        
        Annotation customAnnotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };

        annotationMap.add(customAnnotation);
        assertTrue(annotationMap.annotations().iterator().hasNext());
    }

    @Test
    public void testMergeAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        Deprecated deprecated = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        primary.add(deprecated);
        
        Annotation mergedAnnotation = AnnotationMap.merge(primary, secondary).get(Deprecated.class);
        assertEquals(deprecated, mergedAnnotation);
    }

    @Test
    public void testAddIfNotPresent() {
        Annotation customAnnotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };

        assertTrue(annotationMap.addIfNotPresent(customAnnotation));
        assertFalse(annotationMap.addIfNotPresent(customAnnotation));
    }

    @Test
    public void testSize() {
        assertEquals(0, annotationMap.size());
        
        Deprecated deprecated = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        
        annotationMap.add(deprecated);
        
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testToString() {
        assertEquals("[null]", annotationMap.toString());
        
        Deprecated deprecated = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        
        annotationMap.add(deprecated);
        assertEquals("{class java.lang.Deprecated=interface java.lang.Deprecated}", annotationMap.toString());
    }

}