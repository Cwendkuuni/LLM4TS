import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();

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

        annotationMap.add(annotation1);
        annotationMap.add(annotation2);

        assertEquals(annotation1, annotationMap.get(Annotation.class));
        assertEquals(annotation2, annotationMap.get(Annotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();

        Iterable<Annotation> emptyList = Collections.emptyList();
        assertEquals(emptyList, annotationMap.annotations());

        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };

        annotationMap.add(annotation);
        ArrayList<Annotation> annotations = new ArrayList<Annotation>();
        annotations.add(annotation);
        assertEquals(annotations, annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

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

        primary.add(annotation1);
        secondary.add(annotation2);

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);

        assertTrue(merged.get(Annotation.class) == annotation2);

        primary = new AnnotationMap();
        secondary = new AnnotationMap();

        merged = AnnotationMap.merge(primary, secondary);

        assertEquals(emptyList, merged.annotations());
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };

        annotationMap.add(annotation);
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();

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

        assertTrue(annotationMap.addIfNotPresent(annotation1));
        assertFalse(annotationMap.addIfNotPresent(annotation1));
        assertTrue(annotationMap.addIfNotPresent(annotation2));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();

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

        assertTrue(annotationMap.add(annotation1));
        assertFalse(annotationMap.add(annotation1));
        assertTrue(annotationMap.add(annotation2));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());

        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };

        annotationMap.add(annotation);
        assertEquals("{interface java.lang.annotation.Annotation=com.fasterxml.jackson.databind.introspect.AnnotationMapTest$1@}", annotationMap.toString());
    }

}