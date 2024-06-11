import org.junit.Before;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.Collections;

import static org.junit.Assert.*;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
        annotationMap.addIfNotPresent(new MyAnnotation());
    }

    @Test
    public void testGet_Null() {
        assertNull(annotationMap.get(AnotherAnnotation.class));
    }

    @Test
    public void testGet_NonNull() {
        assertNotNull(annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAnnotations_Empty() {
        assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testAnnotations_NonEmpty() {
        annotationMap.add(new AnotherAnnotation());
        assertTrue(annotationMap.annotations().iterator().hasNext());
    }

    @Test
    public void testMerge_BothNull() {
        AnnotationMap mergedMap = AnnotationMap.merge(null, null);
        assertNull(mergedMap);
    }

    @Test
    public void testMerge_PrimaryNull() {
        AnnotationMap secondaryMap = new AnnotationMap();
        secondaryMap.add(new MyAnnotation());
        AnnotationMap mergedMap = AnnotationMap.merge(null, secondaryMap);
        assertNotNull(mergedMap);
        assertEquals(1, mergedMap.size());
    }

    @Test
    public void testMerge_SecondaryNull() {
        AnnotationMap primaryMap = new AnnotationMap();
        primaryMap.add(new MyAnnotation());
        AnnotationMap mergedMap = AnnotationMap.merge(primaryMap, null);
        assertNotNull(mergedMap);
        assertEquals(1, mergedMap.size());
    }

    @Test
    public void testMerge_BothNonNull() {
        AnnotationMap primaryMap = new AnnotationMap();
        primaryMap.add(new MyAnnotation());

        AnnotationMap secondaryMap = new AnnotationMap();
        secondaryMap.add(new AnotherAnnotation());

        AnnotationMap mergedMap = AnnotationMap.merge(primaryMap, secondaryMap);
        assertNotNull(mergedMap);
        assertEquals(2, mergedMap.size());
        assertNotNull(mergedMap.get(MyAnnotation.class));
        assertNotNull(mergedMap.get(AnotherAnnotation.class));
    }

    @Test
    public void testSize_Empty() {
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testSize_NonEmpty() {
        annotationMap.add(new AnotherAnnotation());
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent_Added() {
        assertTrue(annotationMap.addIfNotPresent(new AnotherAnnotation()));
    }

    @Test
    public void testAddIfNotPresent_NotAdded() {
        assertFalse(annotationMap.addIfNotPresent(new MyAnnotation()));
    }

    @Test
    public void testAdd_Added() {
        assertTrue(annotationMap.add(new AnotherAnnotation()));
    }

    @Test
    public void testAdd_NotAdded() {
        assertFalse(annotationMap.add(new MyAnnotation()));
    }

    @Test
    public void testToString_NullAnnotations() {
        annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());
    }

    @Test
    public void testToString_NonNullAnnotations() {
        annotationMap.add(new MyAnnotation());
        assertEquals("{@MyAnnotation=MyAnnotation}", annotationMap.toString());
    }

    private static class MyAnnotation implements Annotation { }

    private static class AnotherAnnotation implements Annotation { }
}