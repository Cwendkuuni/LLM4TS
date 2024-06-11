import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class AnnotationMapTest {

    private AnnotationMap testMap;

    @Before
    public void setUp() {
        testMap = new AnnotationMap();
    }

    @Test
    public void testGet() {
        assertNull(testMap.get(Override.class));
    }

    @Test
    public void testAnnotations() {
        List<Annotation> annotations = new ArrayList<>();
        assertEquals(annotations, testMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        primary.addIfNotPresent(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });

        AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
        assertNotNull(mergedMap);
    }

    @Test
    public void testSize() {
        assertEquals(0, testMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        Annotation customAnnotation = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        };

        assertTrue(testMap.addIfNotPresent(customAnnotation));
        assertFalse(testMap.addIfNotPresent(customAnnotation));
    }

    @Test
    public void testAdd() {
        Annotation customAnnotation = new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        };

        assertTrue(testMap.add(customAnnotation));
    }

    @Test
    public void testToString() {
        assertEquals("[null]", testMap.toString());
    }
}