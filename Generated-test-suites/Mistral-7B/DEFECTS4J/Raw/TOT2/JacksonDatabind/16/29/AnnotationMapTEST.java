import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testAnnotationMapConstructor() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Annotation.class));
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation());
        assertEquals(MyAnnotation.class, annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation());
        Set<Annotation> annotations = new HashSet<Annotation>(annotationMap.annotations());
        assertTrue(annotations.contains(annotationMap.get(MyAnnotation.class)));
    }

    @Test
    public void testConstructorWithNullArgument() {
        try {
            new AnnotationMap(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testGetWithNonExistentAnnotationType() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation());
        assertNull(annotationMap.get(Object.class));
    }

    @Test
    public void testGetWithNullArgument() {
        AnnotationMap annotationMap = new AnnotationMap();
        try {
            annotationMap.get(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // Add test cases for other methods here...

    @Test
    public void testSizeWithNegativeValue() {
        AnnotationMap annotationMap = new AnnotationMap();
        try {
            annotationMap.size();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSizeWithGreaterValue() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation());
        annotationMap.add(new MyAnnotation());
        int size = annotationMap.size();
        try {
            assertEquals(3, size);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testAddIfNotPresentWithNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        try {
            annotationMap.addIfNotPresent(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testMergeWithNullPrimary() {
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap primary = null;
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertEquals(secondary, result);
    }

    @Test
    public void testMergeWithNullSecondary() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = null;
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, result);
    }

    @Test
    public void testMergeWithIdenticalAnnotationTypes() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new MyAnnotation());
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new MyAnnotation());
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertEquals(primary.size(), result.size());
        Set<Annotation> annotations = new HashSet<Annotation>(result.annotations());
        assertTrue(annotations.containsAll(primary.annotations()));
    }

    @Test
    public void testMergeWithDifferentAnnotationTypes() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new MyAnnotation());
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new DifferentAnnotation());
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertEquals(primary.size() + 1, result.size());
        Set<Annotation> annotations = new HashSet<Annotation>(result.annotations());
        assertTrue(annotations.containsAll(primary.annotations()));
        assertTrue(annotations.contains(secondary.get(DifferentAnnotation.class)));
    }

    // Add test cases for other methods here...

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new MyAnnotation());
        assertEquals("{class com.fasterxml.jackson.databind.introspect.AnnotationMap@1b6d7422}", annotationMap.toString());
    }
}

class MyAnnotation {}

class DifferentAnnotation {}

//