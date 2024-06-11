import org.junit.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Collections;

public class AnnotationMapTest {

    @Test
    public void testGet(){
        AnnotationMap annotationMap = new AnnotationMap();
        // Test with empty annotations map
        Annotation result1 = annotationMap.get(TestAnnotation.class);
        assertNull(result1);

        // Test with populated annotations map
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(TestAnnotation.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);

        Annotation result2 = annotationMap.get(TestAnnotation.class);
        assertNotNull(result2);
    }

    @Test
    public void testAnnotations(){
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test with empty annotations map
        Iterable<Annotation> result1 = annotationMap.annotations();
        assertEquals(Collections.emptyList(), result1);

        // Test with populated annotations map
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(TestAnnotation.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);

        Iterable<Annotation> result2 = annotationMap.annotations();
        assertNotNull(result2);
        assertNotEquals(Collections.emptyList(), result2);
    }

    @Test
    public void testMerge(){
        AnnotationMap primaryMap = new AnnotationMap();
        AnnotationMap secondaryMap = new AnnotationMap();
        
        // Test with empty primary and secondary maps
        AnnotationMap result1 = AnnotationMap.merge(primaryMap, secondaryMap);
        assertEquals(secondaryMap, result1);

        // Test with empty primary map
        HashMap<Class<? extends Annotation>, Annotation> secondaryAnnotations = new HashMap<>();
        secondaryAnnotations.put(TestAnnotation.class, new TestAnnotation());
        secondaryMap = new AnnotationMap(secondaryAnnotations);

        AnnotationMap result2 = AnnotationMap.merge(primaryMap, secondaryMap);
        assertEquals(secondaryMap, result2);

        // Test with empty secondary map
        HashMap<Class<? extends Annotation>, Annotation> primaryAnnotations = new HashMap<>();
        primaryAnnotations.put(TestAnnotation.class, new TestAnnotation());
        primaryMap = new AnnotationMap(primaryAnnotations);

        AnnotationMap result3 = AnnotationMap.merge(primaryMap, secondaryMap);
        assertEquals(primaryMap, result3);

        // Test with both primary and secondary maps populated
        HashMap<Class<? extends Annotation>, Annotation> annotations1 = new HashMap<>();
        annotations1.put(TestAnnotation.class, new TestAnnotation());
        primaryMap = new AnnotationMap(annotations1);

        HashMap<Class<? extends Annotation>, Annotation> annotations2 = new HashMap<>();
        annotations2.put(AnotherAnnotation.class, new AnotherAnnotation());
        secondaryMap = new AnnotationMap(annotations2);

        AnnotationMap result4 = AnnotationMap.merge(primaryMap, secondaryMap);
        HashMap<Class<? extends Annotation>, Annotation> expectedAnnotations = new HashMap<>();
        expectedAnnotations.put(TestAnnotation.class, new TestAnnotation());
        expectedAnnotations.put(AnotherAnnotation.class, new AnotherAnnotation());

        assertEquals(new AnnotationMap(expectedAnnotations), result4);
    }

    @Test
    public void testSize(){
        AnnotationMap annotationMap = new AnnotationMap();
        // Test with empty annotations map
        int size1 = annotationMap.size();
        assertEquals(0, size1);

        // Test with populated annotations map
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(TestAnnotation.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);

        int size2 = annotationMap.size();
        assertEquals(1, size2);
    }

    @Test
    public void testAddIfNotPresent(){
        AnnotationMap annotationMap = new AnnotationMap();
        // Test adding annotation when map is empty
        TestAnnotation annotation = new TestAnnotation();
        boolean result1 = annotationMap.addIfNotPresent(annotation);
        assertTrue(result1);

        // Test adding annotation when map already contains the same annotation
        boolean result2 = annotationMap.addIfNotPresent(annotation);
        assertFalse(result2);

        // Test adding annotation when map already contains a different annotation of the same type
        AnotherAnnotation anotherAnnotation = new AnotherAnnotation();
        boolean result3 = annotationMap.addIfNotPresent(anotherAnnotation);
        assertTrue(result3);
    }

    @Test
    public void testAdd(){
        AnnotationMap annotationMap = new AnnotationMap();
        // Test adding annotation
        TestAnnotation annotation = new TestAnnotation();
        boolean result1 = annotationMap.add(annotation);
        assertTrue(result1);

        // Test adding same annotation again
        boolean result2 = annotationMap.add(annotation);
        assertFalse(result2);

        // Test adding different annotation of the same type
        AnotherAnnotation anotherAnnotation = new AnotherAnnotation();
        boolean result3 = annotationMap.add(anotherAnnotation);
        assertTrue(result3);
    }

    @Test
    public void testToString(){
        AnnotationMap annotationMap = new AnnotationMap();
        // Test with empty annotations map
        String result1 = annotationMap.toString();
        assertEquals("[null]", result1);

        // Test with populated annotations map
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(TestAnnotation.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);

        String result2 = annotationMap.toString();
        assertNotEquals("[null]", result2);
    }

    // Additional TestAnnotation class for testing purposes
    private static class TestAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation.class;
        }
    }

    // Additional AnotherAnnotation class for testing purposes
    private static class AnotherAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return AnotherAnnotation.class;
        }
    }

}