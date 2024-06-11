import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGet_NullAnnotations_ReturnNull() {
        assertNull(annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testGet_NonExistingAnnotation_ReturnNull() {
        annotationMap.addIfNotPresent(new TestAnnotation("Test"));

        assertNull(annotationMap.get(AnotherAnnotation.class));
    }

    @Test
    public void testGet_ExistingAnnotation_ReturnAnnotation() {
        TestAnnotation expected = new TestAnnotation("Test");
        annotationMap.addIfNotPresent(expected);

        TestAnnotation actual = annotationMap.get(TestAnnotation.class);

        assertEquals(expected, actual);
    }

    @Test
    public void testAnnotations_EmptyAnnotations_ReturnEmptyList() {
        List<Annotation> expected = Collections.emptyList();

        Iterable<Annotation> actual = annotationMap.annotations();

        assertEquals(expected, actual);
    }

    @Test
    public void testAnnotations_NonEmptyAnnotations_ReturnAnnotationList() {
        TestAnnotation testAnnotation = new TestAnnotation("Test");
        annotationMap.addIfNotPresent(testAnnotation);

        List<Annotation> expected = Collections.singletonList(testAnnotation);

        Iterable<Annotation> actual = annotationMap.annotations();

        assertEquals(expected, actual);
    }

    @Test
    public void testSize_NullAnnotations_ReturnZero() {
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testSize_NonEmptyAnnotations_ReturnSize() {
        annotationMap.addIfNotPresent(new TestAnnotation("Test"));

        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testMerge_NullPrimary_ReturnSecondary() {
        AnnotationMap secondary = new AnnotationMap();
        secondary.addIfNotPresent(new TestAnnotation("Secondary"));

        AnnotationMap merged = AnnotationMap.merge(null, secondary);

        assertEquals(secondary, merged);
    }

    @Test
    public void testMerge_NullSecondary_ReturnPrimary() {
        AnnotationMap primary = new AnnotationMap();
        primary.addIfNotPresent(new TestAnnotation("Primary"));

        AnnotationMap merged = AnnotationMap.merge(primary, null);

        assertEquals(primary, merged);
    }

    @Test
    public void testMerge_EmptyAnnotations_ReturnMergedAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        primary.addIfNotPresent(new TestAnnotation("Primary"));

        AnnotationMap secondary = new AnnotationMap();
        secondary.addIfNotPresent(new TestAnnotation("Secondary"));

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);

        assertEquals(2, merged.size());
        assertNotNull(merged.get(TestAnnotation.class));
        assertNotNull(merged.get(AnotherAnnotation.class));
    }

    @Test
    public void testAddIfNotPresent_AnnotationNotPresent_AddAnnotationAndReturnTrue() {
        Annotation ann = new TestAnnotation("Test");

        boolean result = annotationMap.addIfNotPresent(ann);

        assertTrue(result);
        assertNotNull(annotationMap.get(TestAnnotation.class));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent_AnnotationPresent_DontAddAnnotationAndReturnFalse() {
        Annotation ann = new TestAnnotation("Test");

        boolean firstResult = annotationMap.addIfNotPresent(ann);
        boolean secondResult = annotationMap.addIfNotPresent(ann);

        assertTrue(firstResult);
        assertFalse(secondResult);
        assertNotNull(annotationMap.get(TestAnnotation.class));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAdd_AnnotationNotPresent_AddAnnotationAndReturnTrue() {
        Annotation ann = new TestAnnotation("Test");

        boolean result = annotationMap.add(ann);

        assertTrue(result);
        assertNotNull(annotationMap.get(TestAnnotation.class));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAdd_AnnotationPresent_AddAnnotationAndReturnTrue() {
        Annotation ann1 = new TestAnnotation("Test");
        Annotation ann2 = new TestAnnotation("Test");

        boolean firstResult = annotationMap.add(ann1);
        boolean secondResult = annotationMap.add(ann2);

        assertTrue(firstResult);
        assertTrue(secondResult);
        assertNotNull(annotationMap.get(TestAnnotation.class));
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testToString_NullAnnotations_ReturnNull() {
        assertEquals("[null]", annotationMap.toString());
    }

    @Test
    public void testToString_NonNullAnnotations_ReturnAnnotationsString() {
        annotationMap.addIfNotPresent(new TestAnnotation("Test"));

        String expected = "{class TestAnnotation=Test}";

        assertEquals(expected, annotationMap.toString());
    }

    @Test
    public void testAdd_NullAnnotationType_ThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> annotationMap.add(null));
    }

    @Test
    public void testGet_NullAnnotationType_ReturnNull() {
        assertNull(annotationMap.get(null));
    }
}