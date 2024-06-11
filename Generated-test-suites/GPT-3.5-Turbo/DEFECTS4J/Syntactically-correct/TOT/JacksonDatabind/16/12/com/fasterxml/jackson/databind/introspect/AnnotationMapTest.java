import org.junit.Before;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import static org.junit.Assert.*;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGet_NoAnnotations_ReturnsNull() {
        // Arrange
        Class<SomeAnnotation> annotationClass = SomeAnnotation.class;

        // Act
        Annotation result = annotationMap.get(annotationClass);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGet_AnnotationExists_ReturnsAnnotation() {
        // Arrange
        Class<SomeAnnotation> annotationClass = SomeAnnotation.class;
        SomeAnnotation expected = new SomeAnnotation();
        annotationMap.add(expected);

        // Act
        Annotation result = annotationMap.get(annotationClass);

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testGet_AnnotationDoesNotExist_ReturnsNull() {
        // Arrange
        Class<SomeAnnotation> annotationClass = SomeAnnotation.class;
        annotationMap.add(new OtherAnnotation());

        // Act
        Annotation result = annotationMap.get(annotationClass);

        // Assert
        assertNull(result);
    }

    @Test
    public void testAnnotations_NoAnnotations_ReturnsEmptyIterable() {
        // Act
        Iterable<Annotation> result = annotationMap.annotations();

        // Assert
        assertFalse(result.iterator().hasNext());
    }

    @Test
    public void testAnnotations_AnnotationsExist_ReturnsIterableWithAnnotations() {
        // Arrange
        annotationMap.add(new SomeAnnotation());
        annotationMap.add(new OtherAnnotation());

        // Act
        Iterable<Annotation> result = annotationMap.annotations();

        // Assert
        assertTrue(result.iterator().hasNext());
    }

    @Test
    public void testAnnotations_AnnotationsExist_ReturnsIterableWithAllAnnotations() {
        // Arrange
        Annotation[] expected = {new SomeAnnotation(), new OtherAnnotation()};
        for (Annotation annotation : expected) {
            annotationMap.add(annotation);
        }

        // Act
        Iterable<Annotation> result = annotationMap.annotations();

        // Assert
        int count = 0;
        for (Annotation annotation : result) {
            assertEquals(expected[count], annotation);
            count++;
        }
        assertEquals(expected.length, count);
    }

    @Test
    public void testMerge_NoAnnotations_ReturnsNull() {
        // Arrange
        AnnotationMap primary = new AnnotationMap();

        // Act
        AnnotationMap result = AnnotationMap.merge(primary, null);

        // Assert
        assertNull(result);
    }

    @Test
    public void testMerge_PrimaryAnnotationsOnly_ReturnsPrimary() {
        // Arrange
        AnnotationMap primary = new AnnotationMap();
        primary.add(new SomeAnnotation());

        // Act
        AnnotationMap result = AnnotationMap.merge(primary, null);

        // Assert
        assertSame(primary, result);
    }

    @Test
    public void testMerge_SecondaryAnnotationsOnly_ReturnsSecondary() {
        // Arrange
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new OtherAnnotation());

        // Act
        AnnotationMap result = AnnotationMap.merge(null, secondary);

        // Assert
        assertSame(secondary, result);
    }

    @Test
    public void testMerge_PrimaryAndSecondaryAnnotations_ReturnsMergedAnnotations() {
        // Arrange
        AnnotationMap primary = new AnnotationMap();
        primary.add(new SomeAnnotation());
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new OtherAnnotation());

        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        // Assert
        assertNotNull(result);
        assertNotNull(result.get(SomeAnnotation.class));
        assertNotNull(result.get(OtherAnnotation.class));
    }

    @Test
    public void testSize_NoAnnotations_ReturnsZero() {
        // Act
        int result = annotationMap.size();

        // Assert
        assertEquals(0, result);
    }

    @Test
    public void testSize_AnnotationsExist_ReturnsCorrectSize() {
        // Arrange
        annotationMap.add(new SomeAnnotation());
        annotationMap.add(new OtherAnnotation());

        // Act
        int result = annotationMap.size();

        // Assert
        assertEquals(2, result);
    }

    @Test
    public void testAddIfNotPresent_AnnotationNotPresent_AddsAnnotation() {
        // Arrange
        SomeAnnotation annotation = new SomeAnnotation();

        // Act
        boolean result = annotationMap.addIfNotPresent(annotation);

        // Assert
        assertTrue(result);
        assertNotNull(annotationMap.get(SomeAnnotation.class));
    }

    @Test
    public void testAddIfNotPresent_AnnotationPresent_DoesNotAddAnnotation() {
        // Arrange
        SomeAnnotation annotation = new SomeAnnotation();
        annotationMap.add(annotation);

        // Act
        boolean result = annotationMap.addIfNotPresent(annotation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAdd_AnnotationDoesNotExist_AddsAnnotation() {
        // Arrange
        SomeAnnotation annotation = new SomeAnnotation();

        // Act
        boolean result = annotationMap.add(annotation);

        // Assert
        assertTrue(result);
        assertNotNull(annotationMap.get(SomeAnnotation.class));
    }

    @Test
    public void testAdd_AnnotationExists_DoesNotAddAnnotation() {
        // Arrange
        SomeAnnotation annotation = new SomeAnnotation();
        annotationMap.add(annotation);

        // Act
        boolean result = annotationMap.add(annotation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testToString_NoAnnotations_ReturnsNull() {
        // Act
        String result = annotationMap.toString();

        // Assert
        assertEquals("[null]", result);
    }

    @Test
    public void testToString_AnnotationsExist_ReturnsAnnotationsAsString() {
        // Arrange
        SomeAnnotation someAnnotation = new SomeAnnotation();
        OtherAnnotation otherAnnotation = new OtherAnnotation();
        annotationMap.add(someAnnotation);
        annotationMap.add(otherAnnotation);

        // Act
        String result = annotationMap.toString();

        // Assert
        String expected = "{"
                + SomeAnnotation.class.getName() + "=" + someAnnotation.toString() + ","
                + OtherAnnotation.class.getName() + "=" + otherAnnotation.toString()
                + "}";
        assertEquals(expected, result);
    }
}