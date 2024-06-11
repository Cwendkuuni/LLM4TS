import org.junit.*;
import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.Collections;

public class AnnotationMapTest {

    // Define the AnnotationMap instance to be used in the test cases
    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGet_ReturnsNullWhenNoAnnotationsPresent() {
        assertNull(annotationMap.get(SomeAnnotation.class));
    }

    @Test
    public void testGet_ReturnsAnnotationWhenPresent() {
        SomeAnnotation someAnnotation = new SomeAnnotationImpl();
        annotationMap.add(someAnnotation);

        assertEquals(someAnnotation, annotationMap.get(SomeAnnotation.class));
    }

    @Test
    public void testGet_ReturnsNullForDifferentAnnotationType() {
        SomeOtherAnnotation someOtherAnnotation = new SomeOtherAnnotationImpl();
        annotationMap.add(someOtherAnnotation);

        assertNull(annotationMap.get(SomeAnnotation.class));
    }

    @Test
    public void testAddIfNotPresent_AddsAnnotationWhenNotAlreadyPresent() {
        SomeAnnotation someAnnotation = new SomeAnnotationImpl();
        assertTrue(annotationMap.addIfNotPresent(someAnnotation));

        assertEquals(someAnnotation, annotationMap.get(SomeAnnotation.class));
    }

    @Test
    public void testAddIfNotPresent_DoesNotAddAnnotationWhenAlreadyPresent() {
        SomeAnnotation someAnnotation = new SomeAnnotationImpl();
        annotationMap.add(someAnnotation);

        assertFalse(annotationMap.addIfNotPresent(someAnnotation));
    }

    @Test
    public void testAdd_AddsAnnotation() {
        SomeAnnotation someAnnotation = new SomeAnnotationImpl();
        assertTrue(annotationMap.add(someAnnotation));

        assertEquals(someAnnotation, annotationMap.get(SomeAnnotation.class));
    }

    @Test
    public void testAdd_ReturnsFalseWhenAnnotationAlreadyPresent() {
        SomeAnnotation someAnnotation = new SomeAnnotationImpl();
        annotationMap.add(someAnnotation);

        assertFalse(annotationMap.add(someAnnotation));
    }

    @Test
    public void testAnnotations_ReturnsEmptyListWhenNoAnnotationsPresent() {
        assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testAnnotations_ReturnsListWithAnnotationsWhenPresent() {
        SomeAnnotation someAnnotation = new SomeAnnotationImpl();
        annotationMap.add(someAnnotation);

        assertEquals(Collections.singletonList(someAnnotation), annotationMap.annotations());
    }

    @Test
    public void testMerge_ReturnsPrimaryWhenSecondaryIsNull() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new SomeAnnotationImpl());

        assertEquals(primary, AnnotationMap.merge(primary, null));
    }

    @Test
    public void testMerge_ReturnsSecondaryWhenPrimaryIsNull() {
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new SomeAnnotationImpl());

        assertEquals(secondary, AnnotationMap.merge(null, secondary));
    }

    @Test
    public void testMerge_MergesAnnotationsFromBothMaps() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new SomeAnnotationImpl());

        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new SomeOtherAnnotationImpl());

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);

        assertTrue(merged.get(SomeAnnotation.class) instanceof SomeAnnotation);
        assertTrue(merged.get(SomeOtherAnnotation.class) instanceof SomeOtherAnnotation);
    }

    @Test
    public void testSize_ReturnsZeroWhenNoAnnotationsPresent() {
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testSize_ReturnsNumberOfAnnotationsPresent() {
        annotationMap.add(new SomeAnnotationImpl());
        annotationMap.add(new SomeOtherAnnotationImpl());

        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testToString_ReturnsStringRepresentationOfAnnotations() {
        SomeAnnotation someAnnotation = new SomeAnnotationImpl();
        annotationMap.add(someAnnotation);

        assertEquals("{class com.example.SomeAnnotationImpl=SomeAnnotation}", annotationMap.toString());
    }

    // Additional test cases for potential edge cases and exception handling scenarios can be added here

}