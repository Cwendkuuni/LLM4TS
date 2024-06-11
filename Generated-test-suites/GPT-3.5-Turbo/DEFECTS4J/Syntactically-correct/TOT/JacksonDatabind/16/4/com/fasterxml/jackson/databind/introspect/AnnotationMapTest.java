import org.junit.*;
import static org.junit.Assert.*;
import java.lang.annotation.*;
import java.util.*;

public class AnnotationMapTest {

    @Test
    public void testGet_Null() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Annotation.class));
    }

    @Test
    public void testGet_ExistingAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(TestAnnotation.class.getAnnotation(Annotation.class));
        assertNotNull(annotationMap.get(Annotation.class));
    }

    @Test
    public void testGet_NonExistingAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(NotNull.class));
    }

    @Test
    public void testAnnotations_EmptyMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        Iterable<Annotation> annotations = annotationMap.annotations();
        assertEquals(Collections.emptyList(), annotations);
    }

    @Test
    public void testAnnotations_NonEmptyMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(TestAnnotation.class.getAnnotation(Annotation.class));
        Iterable<Annotation> annotations = annotationMap.annotations();
        assertNotNull(annotations);
        assertTrue(annotations.iterator().hasNext());
    }

    @Test
    public void testMerge_BothNull() {
        AnnotationMap primary = null;
        AnnotationMap secondary = null;
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertNull(merged);
    }

    @Test
    public void testMerge_PrimaryNull() {
        AnnotationMap primary = null;
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(TestAnnotation.class.getAnnotation(Annotation.class));
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertNotNull(merged);
        assertNotNull(merged.get(TestAnnotation.class.getAnnotation(Annotation.class).annotationType()));
    }

    @Test
    public void testMerge_SecondaryNull() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(TestAnnotation.class.getAnnotation(Annotation.class));
        AnnotationMap secondary = null;
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertNotNull(merged);
        assertNotNull(merged.get(TestAnnotation.class.getAnnotation(Annotation.class).annotationType()));
    }

    @Test
    public void testMerge_BothNonEmpty() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(TestAnnotation.class.getAnnotation(Annotation.class));
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(NotNull.class.getAnnotation(Annotation.class));
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertNotNull(merged);
        assertNotNull(merged.get(TestAnnotation.class.getAnnotation(Annotation.class).annotationType()));
        assertNotNull(merged.get(NotNull.class.getAnnotation(Annotation.class).annotationType()));
    }

    @Test
    public void testSize_EmptyMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testSize_NonEmptyMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(TestAnnotation.class.getAnnotation(Annotation.class));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent_Added() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.addIfNotPresent(TestAnnotation.class.getAnnotation(Annotation.class)));
    }

    @Test
    public void testAddIfNotPresent_NotAdded() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(TestAnnotation.class.getAnnotation(Annotation.class));
        assertFalse(annotationMap.addIfNotPresent(TestAnnotation.class.getAnnotation(Annotation.class)));
    }

    @Test
    public void testAdd_Added() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.add(TestAnnotation.class.getAnnotation(Annotation.class)));
    }

    @Test
    public void testAdd_NotAdded() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(TestAnnotation.class.getAnnotation(Annotation.class));
        assertFalse(annotationMap.add(TestAnnotation.class.getAnnotation(Annotation.class)));
    }

    @Test
    public void testToString_Null() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());
    }

    @Test
    public void testToString_NonNull() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(TestAnnotation.class.getAnnotation(Annotation.class));
        assertNotNull(annotationMap.toString());
    }

    @TestAnnotation
    @Retention(RetentionPolicy.RUNTIME)
    private @interface TestAnnotation {
    }

}