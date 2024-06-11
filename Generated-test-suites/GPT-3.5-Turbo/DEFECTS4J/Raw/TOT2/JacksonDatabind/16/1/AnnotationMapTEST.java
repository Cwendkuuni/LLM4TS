import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import java.lang.annotation.Annotation;
import java.util.HashMap;

public class AnnotationMapTest {

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(SomeAnnotation.class));
        
        // Add annotation and test retrieval
        SomeAnnotation someAnnotation = new SomeAnnotation();
        annotationMap.add(someAnnotation);
        assertEquals(someAnnotation, annotationMap.get(SomeAnnotation.class));
    }

    @Test
    public void testMergeAnnotationMaps() {
        AnnotationMap primaryMap = new AnnotationMap();
        AnnotationMap secondaryMap = new AnnotationMap();
        
        // Test merge when both maps are empty
        AnnotationMap mergedMap = AnnotationMap.merge(primaryMap, secondaryMap);
        assertTrue(mergedMap.size() == 0);
        
        // Test merge when primary map has annotations
        SomeAnnotation someAnnotation = new SomeAnnotation();
        primaryMap.add(someAnnotation);
        mergedMap = AnnotationMap.merge(primaryMap, secondaryMap);
        assertEquals(someAnnotation, mergedMap.get(SomeAnnotation.class));
        
        // Test merge when secondary map has annotations that override primary map
        OtherAnnotation otherAnnotation = new OtherAnnotation();
        secondaryMap.add(otherAnnotation);
        mergedMap = AnnotationMap.merge(primaryMap, secondaryMap);
        assertEquals(otherAnnotation, mergedMap.get(OtherAnnotation.class));
        assertNull(mergedMap.get(SomeAnnotation.class));
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Add annotation for the first time
        SomeAnnotation someAnnotation = new SomeAnnotation();
        assertTrue(annotationMap.addIfNotPresent(someAnnotation));
        assertEquals(someAnnotation, annotationMap.get(SomeAnnotation.class));
        
        // Add the same annotation again
        assertFalse(annotationMap.addIfNotPresent(someAnnotation));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());
        
        SomeAnnotation someAnnotation = new SomeAnnotation();
        annotationMap.add(someAnnotation);
        assertEquals("{class SomeAnnotation=SomeAnnotation}", annotationMap.toString());
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
        
        SomeAnnotation someAnnotation = new SomeAnnotation();
        annotationMap.add(someAnnotation);
        assertEquals(1, annotationMap.size());
    }

    // Additional test methods for edge cases and exception handling scenarios can be added here

}

class SomeAnnotation implements Annotation {
    // Mock class for testing purposes
}

class OtherAnnotation implements Annotation {
    // Mock class for testing purposes
}