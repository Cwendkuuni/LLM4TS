import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Collections;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setup() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGetWhenAnnotationExists() {
        // Arrange
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotations.put(myAnnotation.annotationType(), myAnnotation);
        annotationMap = new AnnotationMap(annotations);

        // Act
        MyAnnotation result = annotationMap.get(MyAnnotation.class);

        // Assert
        Assert.assertEquals(myAnnotation, result);
    }

    @Test
    public void testGetWhenAnnotationDoesNotExist() {
        // Arrange
        annotationMap = new AnnotationMap();

        // Act
        Annotation result = annotationMap.get(MyAnnotation.class);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testAnnotationsWhenAnnotationsExist() {
        // Arrange
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotations.put(myAnnotation.annotationType(), myAnnotation);
        annotationMap = new AnnotationMap(annotations);

        // Act
        Iterable<Annotation> result = annotationMap.annotations();

        // Assert
        Assert.assertEquals(Collections.singletonList(myAnnotation), result);
    }

    @Test
    public void testAnnotationsWhenNoAnnotationsExist() {
        // Arrange
        annotationMap = new AnnotationMap();

        // Act
        Iterable<Annotation> result = annotationMap.annotations();

        // Assert
        Assert.assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testMergeWhenPrimaryIsNull() {
        // Arrange
        AnnotationMap primary = null;
        AnnotationMap secondary = new AnnotationMap();

        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        // Assert
        Assert.assertEquals(secondary, result);
    }

    @Test
    public void testMergeWhenSecondaryIsNull() {
        // Arrange
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = null;

        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        // Assert
        Assert.assertEquals(primary, result);
    }

    @Test
    public void testMergeWhenBothAreNull() {
        // Arrange
        AnnotationMap primary = null;
        AnnotationMap secondary = null;

        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        // Assert
        Assert.assertNull(result);
    }

    @Test
    public void testMergeWhenPrimaryAndSecondaryAreNotEmpty() {
        // Arrange
        HashMap<Class<? extends Annotation>, Annotation> primaryAnnotations = new HashMap<>();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        primaryAnnotations.put(myAnnotation1.annotationType(), myAnnotation1);
        AnnotationMap primary = new AnnotationMap(primaryAnnotations);

        HashMap<Class<? extends Annotation>, Annotation> secondaryAnnotations = new HashMap<>();
        MyAnnotation myAnnotation2 = new MyAnnotation();
        secondaryAnnotations.put(myAnnotation2.annotationType(), myAnnotation2);
        AnnotationMap secondary = new AnnotationMap(secondaryAnnotations);

        HashMap<Class<? extends Annotation>, Annotation> expectedAnnotations = new HashMap<>();
        expectedAnnotations.put(myAnnotation2.annotationType(), myAnnotation2);
        expectedAnnotations.put(myAnnotation1.annotationType(), myAnnotation1);
        AnnotationMap expected = new AnnotationMap(expectedAnnotations);

        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        // Assert
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testSizeWhenAnnotationsExist() {
        // Arrange
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotations.put(myAnnotation.annotationType(), myAnnotation);
        annotationMap = new AnnotationMap(annotations);

        // Act
        int result = annotationMap.size();

        // Assert
        Assert.assertEquals(1, result);
    }

    @Test
    public void testSizeWhenNoAnnotationsExist() {
        // Arrange
        annotationMap = new AnnotationMap();

        // Act
        int result = annotationMap.size();

        // Assert
        Assert.assertEquals(0, result);
    }

    @Test
    public void testAddIfNotPresentWhenAnnotationDoesNotExist() {
        // Arrange
        annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();

        // Act
        boolean result = annotationMap.addIfNotPresent(myAnnotation);

        // Assert
        Assert.assertTrue(result);
        Assert.assertEquals(myAnnotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAddIfNotPresentWhenAnnotationExists() {
        // Arrange
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotations.put(myAnnotation.annotationType(), myAnnotation);
        annotationMap = new AnnotationMap(annotations);

        // Act
        boolean result = annotationMap.addIfNotPresent(myAnnotation);

        // Assert
        Assert.assertFalse(result);
        Assert.assertEquals(myAnnotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAddWhenAnnotationDoesNotExist() {
        // Arrange
        annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();

        // Act
        boolean result = annotationMap.add(myAnnotation);

        // Assert
        Assert.assertTrue(result);
        Assert.assertEquals(myAnnotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAddWhenAnnotationExists() {
        // Arrange
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotations.put(myAnnotation.annotationType(), myAnnotation);
        annotationMap = new AnnotationMap(annotations);

        // Act
        boolean result = annotationMap.add(myAnnotation);

        // Assert
        Assert.assertTrue(result);
        Assert.assertEquals(myAnnotation, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testToStringWhenAnnotationsExist() {
        // Arrange
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotations.put(myAnnotation.annotationType(), myAnnotation);
        annotationMap = new AnnotationMap(annotations);

        // Act
        String result = annotationMap.toString();

        // Assert
        Assert.assertEquals(annotations.toString(), result);
    }

    @Test
    public void testToStringWhenNoAnnotationsExist() {
        // Arrange
        annotationMap = new AnnotationMap();

        // Act
        String result = annotationMap.toString();

        // Assert
        Assert.assertEquals("[null]", result);
    }

    private static class MyAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return MyAnnotation.class;
        }
    }
}