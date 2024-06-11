import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.annotation.Annotation;
import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {

    @Test
    public void testGet_NullAnnotations_ReturnNull() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(Annotation.class));
    }

    @Test
    public void testGet_NonNullAnnotations_ReturnCorrectAnnotation() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        map.add(testAnnotation);
        assertEquals(testAnnotation, map.get(TestAnnotation.class));
    }

    @Test
    public void testAnnotations_EmptyAnnotations_ReturnEmptyList() {
        AnnotationMap map = new AnnotationMap();
        assertTrue(map.annotations().isEmpty());
    }

    @Test
    public void testAnnotations_NonEmptyAnnotations_ReturnCorrectList() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        map.add(testAnnotation);
        
        Iterable<Annotation> annotations = map.annotations();
        assertFalse(annotations.isEmpty());
        assertEquals(testAnnotation, annotations.iterator().next());
    }

    @Test
    public void testMerge_NullPrimaryAnnotations_ReturnSecondaryAnnotations() {
        AnnotationMap primary = null;
        AnnotationMap secondary = new AnnotationMap();

        TestAnnotation secondaryAnnotation = new TestAnnotation();
        secondary.add(secondaryAnnotation);

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);

        assertEquals(secondaryAnnotation, merged.get(TestAnnotation.class));
    }

    @Test
    public void testMerge_NullSecondaryAnnotations_ReturnPrimaryAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        TestAnnotation primaryAnnotation = new TestAnnotation();
        primary.add(primaryAnnotation);
        AnnotationMap secondary = null;

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);

        assertEquals(primaryAnnotation, merged.get(TestAnnotation.class));
    }

    @Test
    public void testMerge_BothAnnotations_ReturnMergedAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        TestAnnotation primaryAnnotation = new TestAnnotation();
        primary.add(primaryAnnotation);

        AnnotationMap secondary = new AnnotationMap();
        TestAnnotation secondaryAnnotation = new TestAnnotation();
        secondary.add(secondaryAnnotation);

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);

        assertEquals(primaryAnnotation, merged.get(TestAnnotation.class));
        assertEquals(secondaryAnnotation, merged.get(TestAnnotation.class));
    }

    @Test
    public void testSize_NullAnnotations_ReturnZero() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.size());
    }

    @Test
    public void testSize_NonNullAnnotations_ReturnSize() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        map.add(testAnnotation);
        assertEquals(1, map.size());
    }

    @Test
    public void testAddIfNotPresent_AnnotationNotPresent_ReturnTrueAndAddAnnotation() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        assertTrue(map.addIfNotPresent(testAnnotation));
        assertEquals(testAnnotation, map.get(TestAnnotation.class));
    }
    
    @Test
    public void testAddIfNotPresent_AnnotationPresent_ReturnFalseAndDoNotAddAnnotation() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        map.add(testAnnotation);
        assertFalse(map.addIfNotPresent(testAnnotation));
    }

    @Test
    public void testAdd_AnnotationNotPresent_ReturnTrueAndAddAnnotation() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        assertTrue(map.add(testAnnotation));
        assertEquals(testAnnotation, map.get(TestAnnotation.class));
    }

    @Test
    public void testAdd_AnnotationPresent_ReturnFalseAndDoNotAddAnnotation() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        map.add(testAnnotation);
        assertFalse(map.add(testAnnotation));
    }

    @Test
    public void testToString_NullAnnotations_ReturnNullString() {
        AnnotationMap map = new AnnotationMap();
        assertEquals("[null]", map.toString());
    }

    @Test
    public void testToString_NonNullAnnotations_ReturnAnnotationsString() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        map.add(testAnnotation);
        assertEquals("{class com.fasterxml.jackson.databind.introspect.AnnotationMapTest.TestAnnotation=@" 
                     + Integer.toHexString(testAnnotation.hashCode()) + "}", map.toString());
    }

    @Test
    public void testAdd_PrivateMethod_CannotBeAccessed() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        assertFalse(map.add(testAnnotation));
    }

    @Test
    public void testAdd_NullAnnotationType_ReturnFalse() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        assertFalse(map.add(testAnnotation));
    }

    @Test
    public void testAdd_SameAnnotationType_ReturnTrue() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation testAnnotation1 = new TestAnnotation();
        map.add(testAnnotation1);
        TestAnnotation testAnnotation2 = new TestAnnotation();
        assertTrue(map.add(testAnnotation2));
    }

    @Test
    public void testAdd_AddThenRemove_AddAgain_ReturnFalse() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        assertTrue(map.add(testAnnotation));
        assertTrue(map._add(testAnnotation));
        assertFalse(map.add(testAnnotation));
    }

    @Test
    public void testAdd_AddDifferentAnnotationTypes_ReturnTrue() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        assertTrue(map.add(testAnnotation));
        OtherAnnotation otherAnnotation = new OtherAnnotation();
        assertTrue(map.add(otherAnnotation));
    }

    @Test
    public void testAdd_EmptyAnnotations_AddSingleAnnotation_ReturnTrue() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        assertTrue(map.add(testAnnotation));
        assertEquals(testAnnotation, map.get(TestAnnotation.class));
    }

    @Test
    public void testAdd_EmptyAnnotations_AddMultipleAnnotations_ReturnTrue() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation testAnnotation1 = new TestAnnotation();
        TestAnnotation testAnnotation2 = new TestAnnotation();
        assertTrue(map.add(testAnnotation1));
        assertTrue(map.add(testAnnotation2));
        assertEquals(testAnnotation1, map.get(TestAnnotation.class));
        assertEquals(testAnnotation2, map.get(TestAnnotation.class));
    }

    @Test
    public void testAdd_NonEmptyAnnotations_AddSingleAnnotation_ReturnTrue() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation testAnnotation1 = new TestAnnotation();
        TestAnnotation testAnnotation2 = new TestAnnotation();
        map.add(testAnnotation1);
        assertTrue(map.add(testAnnotation2));
        assertEquals(testAnnotation1, map.get(TestAnnotation.class));
        assertEquals(testAnnotation2, map.get(TestAnnotation.class));
    }

    @Test
    public void testAdd_NonEmptyAnnotations_AddDuplicateAnnotation_ReturnFalse() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        map.add(testAnnotation);
        assertFalse(map.add(testAnnotation));
        assertEquals(testAnnotation, map.get(TestAnnotation.class));
    }

    @Test
    public void testAdd_OverrideExistingAnnotation_ReturnTrue() {
        AnnotationMap map = new AnnotationMap();
        TestAnnotation testAnnotation1 = new TestAnnotation();
        TestAnnotation testAnnotation2 = new TestAnnotation();
        map.add(testAnnotation1);
        assertTrue(map.add(testAnnotation2));
        assertEquals(testAnnotation2, map.get(TestAnnotation.class));
    }

    @Test
    public void testAdd_NullAnnotation_ReturnFalse() {
        AnnotationMap map = new AnnotationMap();
        assertFalse(map.add(null));
    }

    private static class TestAnnotation implements Annotation {}

    private static class OtherAnnotation implements Annotation {}

}