import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import java.util.Collections;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Test.class));
        
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        AnnotationMap annotationMap2 = new AnnotationMap(annotations);
        
        assertEquals(TestAnnotation.class, annotationMap2.get(Test.class).getClass());
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(Collections.emptyList(), annotationMap.annotations());
        
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        AnnotationMap annotationMap2 = new AnnotationMap(annotations);
        
        assertEquals(Collections.singletonList(new TestAnnotation()), annotationMap2.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        
        assertEquals(secondary, merged);
        
        HashMap<Class<? extends Annotation>,Annotation> primaryAnnotations = new HashMap<>();
        primaryAnnotations.put(Test.class, new TestAnnotation());
        primary = new AnnotationMap(primaryAnnotations);
        
        merged = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, merged);
        
        HashMap<Class<? extends Annotation>,Annotation> secondaryAnnotations = new HashMap<>();
        secondaryAnnotations.put(Deprecated.class, new DeprecatedAnnotation());
        secondary = new AnnotationMap(secondaryAnnotations);
        
        merged = AnnotationMap.merge(primary, secondary);
        AnnotationMap expectedMerged = new AnnotationMap();
        expectedMerged.add(new TestAnnotation());
        expectedMerged.add(new DeprecatedAnnotation());
        assertEquals(expectedMerged, merged);
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
        
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        AnnotationMap annotationMap2 = new AnnotationMap(annotations);
        
        assertEquals(1, annotationMap2.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation testAnnotation = new TestAnnotation();
        assertTrue(annotationMap.addIfNotPresent(testAnnotation));
        assertFalse(annotationMap.addIfNotPresent(testAnnotation));
        assertEquals(testAnnotation, annotationMap.get(Test.class));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation testAnnotation = new TestAnnotation();
        assertTrue(annotationMap.add(testAnnotation));
        assertFalse(annotationMap.add(testAnnotation));
        assertEquals(testAnnotation, annotationMap.get(Test.class));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());
        
        Annotation testAnnotation = new TestAnnotation();
        annotationMap.add(testAnnotation);
        assertEquals("{@org.junit.Test=TestAnnotation}", annotationMap.toString());
    }
    
    public static class TestAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return Test.class;
        }
    }
    
    public static class DeprecatedAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return Deprecated.class;
        }
    }
}