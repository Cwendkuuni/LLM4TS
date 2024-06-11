import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setup() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGet() {
        assertNull(annotationMap.get(Override.class));
    }

    @Test
    public void testAnnotationsEmpty() {
        assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);

        assertEquals(0, merged.size());
    }

    @Test
    public void testAddIfNotPresent() {
        Annotation testAnnotation = new TestAnnotation();

        assertTrue(annotationMap.addIfNotPresent(testAnnotation));
        assertFalse(annotationMap.addIfNotPresent(testAnnotation));
    }

    @Test
    public void testAdd() {
        Annotation testAnnotation = new TestAnnotation();

        assertTrue(annotationMap.add(testAnnotation));
        assertFalse(annotationMap.add(testAnnotation));
    }

    @Test
    public void testToString() {
        assertEquals("[null]", annotationMap.toString());
    }

    // Test edge cases and exceptions here

    private static class TestAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation.class;
        }
    }
}