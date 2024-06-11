import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationMapTest {

    @Test
    public void testGet_NullAnnotations() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(Annotation.class));
    }

    @Test
    public void testGet_NonNullAnnotation() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation1 annotation1 = new TestAnnotation1();
        map.add(annotation1);
        assertSame(annotation1, map.get(TestAnnotation1.class));
    }

    @Test
    public void testAnnotations_EmptyMap() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.annotations().spliterator().getExactSizeIfKnown());
    }

    @Test
    public void testAnnotations_NonEmptyMap() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation1 annotation1 = new TestAnnotation1();
        TestAnnotation2 annotation2 = new TestAnnotation2();
        map.add(annotation1);
        map.add(annotation2);
        Iterable<Annotation> annotations = map.annotations();
        List<Class<? extends Annotation>> annotationTypes = new ArrayList<>();
        for (Annotation annotation : annotations) {
            annotationTypes.add(annotation.annotationType());
        }
        assertTrue(annotationTypes.contains(TestAnnotation1.class));
        assertTrue(annotationTypes.contains(TestAnnotation2.class));
        assertFalse(annotationTypes.contains(TestAnnotation3.class));
    }

    @Test
    public void testMerge_NullPrimary() {
        AnnotationMap secondary = new AnnotationMap();
        assertNotNull(AnnotationMap.merge(null, secondary));
    }

    @Test
    public void testMerge_NullSecondary() {
        AnnotationMap primary = new AnnotationMap();
        assertNotNull(AnnotationMap.merge(primary, null));
    }

    @Test
    public void testMerge_EmptyMaps() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
        assertTrue(mergedMap._annotations.isEmpty());
    }

    @Test
    public void testMerge_WithPrimaryAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        TestAnnotation1 annotation1 = new TestAnnotation1();
        primary.add(annotation1);

        AnnotationMap secondary = new AnnotationMap();
        TestAnnotation2 annotation2 = new TestAnnotation2();
        secondary.add(annotation2);

        AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
        List<Class<? extends Annotation>> annotationTypes = new ArrayList<>();
        for (Annotation annotation : mergedMap.annotations()) {
            annotationTypes.add(annotation.annotationType());
        }
        assertTrue(annotationTypes.contains(TestAnnotation1.class));
        assertTrue(annotationTypes.contains(TestAnnotation2.class));
    }

    @Test
    public void testMerge_WithoutPrimaryAnnotations() {
        AnnotationMap primary = new AnnotationMap();

        AnnotationMap secondary = new AnnotationMap();
        TestAnnotation2 annotation2 = new TestAnnotation2();
        secondary.add(annotation2);

        AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
        assertEquals(1, mergedMap.annotations().spliterator().getExactSizeIfKnown());
        assertTrue(mergedMap.annotations().spliterator().tryAdvance(annotation -> {
            assertEquals(TestAnnotation2.class, annotation.annotationType());
        }));
    }

    @Test
    public void testSize_NullAnnotations() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.size());
    }

    @Test
    public void testSize_NonEmptyAnnotations() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation1 annotation1 = new TestAnnotation1();
        map.add(annotation1);
        assertEquals(1, map.size());
    }

    @Test
    public void testAddIfNotPresent_AnnotationNotPresent() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation1 annotation1 = new TestAnnotation1();
        assertTrue(map.addIfNotPresent(annotation1));
        assertEquals(annotation1, map._annotations.get(TestAnnotation1.class));
    }

    @Test
    public void testAddIfNotPresent_AnnotationAlreadyPresent() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation1 annotation1 = new TestAnnotation1();
        map.add(annotation1);
        assertFalse(map.addIfNotPresent(annotation1));
    }

    @Test
    public void testAdd_True() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation1 annotation1 = new TestAnnotation1();
        assertTrue(map.add(annotation1));
    }

    @Test
    public void testAdd_False() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation1 annotation1 = new TestAnnotation1();
        map.add(annotation1);
        assertFalse(map.add(annotation1));
    }

    private static class TestAnnotation1 implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation1.class;
        }
    }

    private static class TestAnnotation2 implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation2.class;
        }
    }

    private static class TestAnnotation3 implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation3.class;
        }
    }
}

