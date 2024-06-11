import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGetWithNullAnnotations() {
        assertNull(annotationMap.get(SomeAnnotation.class));
    }

    @Test
    public void testAnnotationsWithEmptyMap() {
        assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testMergeWithNullPrimary() {
        AnnotationMap secondaryMap = new AnnotationMap();
        secondaryMap.add(new SomeAnnotation());
        AnnotationMap mergedMap = AnnotationMap.merge(null, secondaryMap);
        assertTrue(mergedMap.get(SomeAnnotation.class) != null);
    }

    @Test
    public void testMergeWithNullSecondary() {
        AnnotationMap primaryMap = new AnnotationMap();
        primaryMap.add(new SomeAnnotation());
        AnnotationMap mergedMap = AnnotationMap.merge(primaryMap, null);
        assertTrue(mergedMap.get(SomeAnnotation.class) != null);
    }

    @Test
    public void testSizeWithNullAnnotations() {
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresentWithExistingAnnotation() {
        SomeAnnotation annotation = new SomeAnnotation();
        annotationMap.add(annotation);
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testToStringWithNullAnnotations() {
        assertEquals("[null]", annotationMap.toString());
    }

    @Test
    public void testAddHelperMethod() {
        SomeAnnotation annotation = new SomeAnnotation();
        assertTrue(annotationMap.add(annotation));
    }

    private static class SomeAnnotation implements Annotation {
        // Some mock implementation for testing purposes
    }

}