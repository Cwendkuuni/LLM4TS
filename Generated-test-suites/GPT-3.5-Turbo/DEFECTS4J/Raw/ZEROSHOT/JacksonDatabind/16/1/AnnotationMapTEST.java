import org.junit.Before;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.*;
import static org.junit.Assert.*;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGet_NullAnnotations_ReturnsNull() {
        Annotation result = annotationMap.get(SomeAnnotation.class);
        assertNull(result);
    }

    @Test
    public void testGet_AnnotationPresent_ReturnsAnnotation() {
        SomeAnnotation annotation = new SomeAnnotationImpl();

        annotationMap.add(annotation);

        SomeAnnotation result = annotationMap.get(SomeAnnotation.class);
        assertEquals(annotation, result);
    }

    @Test
    public void testGet_AnnotationNotPresent_ReturnsNull() {
        SomeAnnotation annotation1 = new SomeAnnotationImpl();
        SomeAnnotation annotation2 = new SomeAnnotationImpl();

        annotationMap.add(annotation1);

        Annotation result = annotationMap.get(annotation2.getClass());
        assertNull(result);
    }

    @Test
    public void testAnnotations_EmptyAnnotations_ReturnsEmptyList() {
        Iterable<Annotation> result = annotationMap.annotations();
        assertTrue(((Collection<?>) result).isEmpty());
    }

    @Test
    public void testAnnotations_NonEmptyAnnotations_ReturnsNonEmptyList() {
        SomeAnnotation annotation = new SomeAnnotationImpl();

        annotationMap.add(annotation);

        Iterable<Annotation> result = annotationMap.annotations();
        assertFalse(((Collection<?>) result).isEmpty());
        assertTrue(((Collection<?>) result).contains(annotation));
    }

    @Test
    public void testMerge_PrimaryAnnotationsEmpty_ReturnsSecondaryAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        SomeAnnotation annotation = new SomeAnnotationImpl();
        secondary.add(annotation);

        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertEquals(1, result.size());
        assertEquals(annotation, result.get(SomeAnnotation.class));
    }

    @Test
    public void testMerge_SecondaryAnnotationsEmpty_ReturnsPrimaryAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        SomeAnnotation annotation = new SomeAnnotationImpl();
        primary.add(annotation);

        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertEquals(1, result.size());
        assertEquals(annotation, result.get(SomeAnnotation.class));
    }

    @Test
    public void testMerge_BothAnnotationsNonEmpty_ReturnsMergedAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        SomeAnnotation annotation1 = new SomeAnnotationImpl();
        SomeAnnotation annotation2 = new SomeAnnotationImpl();

        primary.add(annotation1);
        secondary.add(annotation2);

        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertEquals(2, result.size());
        assertEquals(annotation1, result.get(SomeAnnotation.class));
        assertEquals(annotation2, result.get(SomeAnnotation.class));
    }

    @Test
    public void testSize_NullAnnotations_ReturnsZero() {
        int result = annotationMap.size();
        assertEquals(0, result);
    }

    @Test
    public void testSize_NonNullAnnotations_ReturnsSize() {
        SomeAnnotation annotation = new SomeAnnotationImpl();

        annotationMap.add(annotation);

        int result = annotationMap.size();
        assertEquals(1, result);
    }

    @Test
    public void testAddIfNotPresent_NullAnnotations_ReturnsTrue() {
        SomeAnnotation annotation = new SomeAnnotationImpl();

        boolean result = annotationMap.addIfNotPresent(annotation);

        assertTrue(result);
        assertEquals(1, annotationMap.size());
        assertEquals(annotation, annotationMap.get(SomeAnnotation.class));
    }

    @Test
    public void testAddIfNotPresent_AnnotationAlreadyPresent_ReturnsFalse() {
        SomeAnnotation annotation = new SomeAnnotationImpl();

        annotationMap.add(annotation);

        boolean result = annotationMap.addIfNotPresent(annotation);

        assertFalse(result);
        assertEquals(1, annotationMap.size());
        assertEquals(annotation, annotationMap.get(SomeAnnotation.class));
    }

    @Test
    public void testAdd_AnnotationNotPresent_ReturnsTrue() {
        SomeAnnotation annotation = new SomeAnnotationImpl();

        boolean result = annotationMap.add(annotation);

        assertTrue(result);
        assertEquals(1, annotationMap.size());
        assertEquals(annotation, annotationMap.get(SomeAnnotation.class));
    }

    @Test
    public void testAdd_AnnotationAlreadyPresent_ReturnsFalse() {
        SomeAnnotation annotation = new SomeAnnotationImpl();

        annotationMap.add(annotation);

        boolean result = annotationMap.add(annotation);

        assertFalse(result);
        assertEquals(1, annotationMap.size());
        assertEquals(annotation, annotationMap.get(SomeAnnotation.class));
    }

    @Test
    public void testToString_NullAnnotations_ReturnsNull() {
        String result = annotationMap.toString();
        assertEquals("[null]", result);
    }

    @Test
    public void testToString_NonNullAnnotations_ReturnsAnnotationsString() {
        SomeAnnotation annotation = new SomeAnnotationImpl();

        annotationMap.add(annotation);

        String result = annotationMap.toString();
        assertEquals(annotation.toString(), result);
    }

    private static class SomeAnnotationImpl implements SomeAnnotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return SomeAnnotation.class;
        }
    }

    private interface SomeAnnotation extends Annotation {
    }
}