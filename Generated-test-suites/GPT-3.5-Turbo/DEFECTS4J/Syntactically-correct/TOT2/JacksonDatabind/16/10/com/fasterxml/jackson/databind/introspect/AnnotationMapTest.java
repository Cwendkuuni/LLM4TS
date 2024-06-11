import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.HashMap;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Test when annotations map is null
        assertNull(annotationMap.get(TestAnnotation.class));

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(TestAnnotation.class, new TestAnnotation());
        AnnotationMap annotationMapWithAnnotations = new AnnotationMap(annotations);
        // Test when annotation exists in the map
        assertEquals(TestAnnotation.class, annotationMapWithAnnotations.get(TestAnnotation.class).annotationType());
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Test when annotations map is null
        assertEquals(0, annotationMap.annotations().spliterator().estimateSize());

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(TestAnnotation.class, new TestAnnotation());
        AnnotationMap annotationMapWithAnnotations = new AnnotationMap(annotations);
        // Test when annotations map is not empty
        assertEquals(1, annotationMapWithAnnotations.annotations().spliterator().estimateSize());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        // Test when both primary and secondary are empty
        assertEquals(0, AnnotationMap.merge(primary, secondary).size());

        HashMap<Class<? extends Annotation>, Annotation> primaryAnnotations = new HashMap<>();
        primaryAnnotations.put(TestAnnotation.class, new TestAnnotation());
        primary = new AnnotationMap(primaryAnnotations);

        // Test when secondary is empty
        assertEquals(1, AnnotationMap.merge(primary, secondary).size());

        HashMap<Class<? extends Annotation>, Annotation> secondaryAnnotations = new HashMap<>();
        secondaryAnnotations.put(AnotherTestAnnotation.class, new AnotherTestAnnotation());
        secondary = new AnnotationMap(secondaryAnnotations);

        // Test when both primary and secondary have annotations
        assertEquals(2, AnnotationMap.merge(primary, secondary).size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Test when annotation is added as it is not present
        assertTrue(annotationMap.addIfNotPresent(new TestAnnotation()));

        // Test when annotation is not added as it is already present
        assertFalse(annotationMap.addIfNotPresent(new TestAnnotation()));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        // Test when annotation is added
        assertTrue(annotationMap.add(new TestAnnotation()));
    }
}