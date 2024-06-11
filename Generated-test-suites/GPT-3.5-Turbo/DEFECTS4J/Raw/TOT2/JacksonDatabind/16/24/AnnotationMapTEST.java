import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.annotation.Annotation;
import java.util.Collections;

public class AnnotationMapTest {

    @Test
    public void testGetReturnsNullWhenAnnotationsMapIsEmpty() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Annotation.class));
    }

    @Test
    public void testAnnotationsReturnsEmptyIterableWhenAnnotationsMapIsEmpty() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testMergeReturnsSecondaryWhenPrimaryIsNull() {
        AnnotationMap primary = null;
        AnnotationMap secondary = new AnnotationMap();
        assertEquals(secondary, AnnotationMap.merge(primary, secondary));
    }

    @Test
    public void testMergeReturnsPrimaryWhenSecondaryIsNull() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = null;
        assertEquals(primary, AnnotationMap.merge(primary, secondary));
    }

    @Test
    public void testSizeReturnsZeroWhenAnnotationsMapIsNull() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresentAddsNewAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new TestAnnotation();
        assertTrue(annotationMap.addIfNotPresent(annotation));
        assertTrue(annotationMap.get(TestAnnotation.class) == annotation);
    }

    @Test
    public void testAddIfNotPresentDoesNotAddExistingAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new TestAnnotation();
        annotationMap.addIfNotPresent(annotation);
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAddReturnsTrueWhenAddingNewAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new TestAnnotation();
        assertTrue(annotationMap.add(annotation));
    }

    @Test
    public void testAddReturnsFalseWhenAddingExistingAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new TestAnnotation();
        annotationMap.add(annotation);
        assertFalse(annotationMap.add(annotation));
    }

    @Test
    public void testToStringReturnsCorrectString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());
    }

    private static class TestAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation.class;
        }
    }
}