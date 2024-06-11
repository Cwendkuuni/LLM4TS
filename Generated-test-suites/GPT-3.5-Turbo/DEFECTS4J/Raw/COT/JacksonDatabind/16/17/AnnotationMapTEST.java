
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Collections;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Test.class));

        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        Annotation annotation = new TestAnnotation();
        annotations.put(TestAnnotation.class, annotation);
        annotationMap = new AnnotationMap(annotations);

        assertEquals(annotation, annotationMap.get(TestAnnotation.class));
        assertNull(annotationMap.get(Test.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(Collections.emptyList(), annotationMap.annotations());

        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        Annotation annotation = new TestAnnotation();
        annotations.put(TestAnnotation.class, annotation);
        annotationMap = new AnnotationMap(annotations);

        assertEquals(Collections.singletonList(annotation), annotationMap.annotations());

        annotations.put(Test.class, null);
        annotationMap = new AnnotationMap(annotations);
        assertEquals(Collections.singletonList(annotation), annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        assertNull(AnnotationMap.merge(primary, null));
        assertNull(AnnotationMap.merge(null, primary));

        AnnotationMap secondary = new AnnotationMap();
        assertEquals(primary, AnnotationMap.merge(primary, secondary));

        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        Annotation annotation1 = new TestAnnotation();
        annotations.put(TestAnnotation.class, annotation1);
        primary = new AnnotationMap(annotations);

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, merged);

        AnnotationMap mergedReverse = AnnotationMap.merge(secondary, primary);
        assertEquals(primary, mergedReverse);

        annotations = new HashMap<>();
        Annotation annotation2 = new DeprecatedAnnotation();
        annotations.put(DeprecatedAnnotation.class, annotation2);
        secondary = new AnnotationMap(annotations);

        merged = AnnotationMap.merge(primary, secondary);
        assertEquals(2, merged.size());
        assertEquals(annotation1, merged.get(TestAnnotation.class));
        assertEquals(annotation2, merged.get(DeprecatedAnnotation.class));

        mergedReverse = AnnotationMap.merge(secondary, primary);
        assertEquals(2, mergedReverse.size());
        assertEquals(annotation1, mergedReverse.get(TestAnnotation.class));
        assertEquals(annotation2, mergedReverse.get(DeprecatedAnnotation.class));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        Annotation annotation1 = new TestAnnotation();
        Annotation annotation2 = new DeprecatedAnnotation();
        annotations.put(TestAnnotation.class, annotation1);
        annotations.put(DeprecatedAnnotation.class, annotation2);
        annotationMap = new AnnotationMap(annotations);

        assertEquals(2, annotationMap.size());
        annotations.remove(TestAnnotation.class);
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new TestAnnotation();
        assertTrue(annotationMap.addIfNotPresent(annotation));
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new TestAnnotation();
        assertTrue(annotationMap.add(annotation));
        assertFalse(annotationMap.add(annotation));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());

        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        Annotation annotation1 = new TestAnnotation();
        Annotation annotation2 = new DeprecatedAnnotation();
        annotations.put(TestAnnotation.class, annotation1);
        annotations.put(DeprecatedAnnotation.class, annotation2);
        annotationMap = new AnnotationMap(annotations);

        assertEquals(annotations.toString(), annotationMap.toString());
    }

    private static class TestAnnotation implements Annotation { }

    private static class DeprecatedAnnotation implements Annotation { }
}
