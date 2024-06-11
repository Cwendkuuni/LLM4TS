import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.Collections;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGet() {
        assertNull(annotationMap.get(Override.class));
        Annotation ann = new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        };
        annotationMap.add(ann);
        assertEquals(ann, annotationMap.get(Override.class));
    }

    @Test
    public void testAnnotations() {
        assertEquals(Collections.emptyList(), annotationMap.annotations());
        Annotation ann = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        annotationMap.add(ann);
        assertEquals(Collections.singletonList(ann), annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        Annotation ann1 = new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        };
        Annotation ann2 = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        primary.add(ann1);
        secondary.add(ann2);
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(2, merged.size());
        assertTrue(merged.get(Override.class) != null);
        assertTrue(merged.get(Deprecated.class) != null);
    }

    @Test
    public void testAddIfNotPresent() {
        Annotation ann = new SuppressWarnings() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return SuppressWarnings.class;
            }
        };
        assertTrue(annotationMap.addIfNotPresent(ann));
        assertFalse(annotationMap.addIfNotPresent(ann));
    }

    @Test
    public void testToString() {
        assertEquals("[null]", annotationMap.toString());
        Annotation ann = new SuppressWarnings() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return SuppressWarnings.class;
            }
        };
        annotationMap.add(ann);
        assertEquals("{@SuppressWarnings=java.lang.annotation.SuppressWarnings(value=unused)}", annotationMap.toString());
    }
}