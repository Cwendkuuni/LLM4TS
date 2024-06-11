package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;
import java.lang.annotation.*;
import java.util.*;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        annotationMap.add(annotation);

        MyAnnotation retrievedAnnotation = annotationMap.get(MyAnnotation.class);
        assertEquals(annotation, retrievedAnnotation);
    }

    @Test(expected = NullPointerException.class)
    public void testGetAnnotationNullClass() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.get(null);
    }

    @Test
    public void testGetAnnotationAbsent() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testSizeZero() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testSizeOne() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        annotationMap.add(annotation);
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testSizeMultiple() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation annotation1 = new MyAnnotation();
        MyAnnotation annotation2 = new MyAnnotation();
        annotationMap.add(annotation1);
        annotationMap.add(annotation2);
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAnnotationMapMergeEmptyPrimary() {
        AnnotationMap secondary = new AnnotationMap();
        MyAnnotation secondaryAnnotation1 = new MyAnnotation();
        MyAnnotation secondaryAnnotation2 = new MyAnnotation();
        secondary.add(secondaryAnnotation1);
        secondary.add(secondaryAnnotation2);

        AnnotationMap primary = new AnnotationMap();

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
        assertEquals(secondaryAnnotation1.getClass(), mergedAnnotationMap.get(MyAnnotation.class).getClass());
        assertEquals(secondaryAnnotation1, mergedAnnotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAnnotationMapMergeEmptySecondary() {
        AnnotationMap primary = new AnnotationMap();
        MyAnnotation primaryAnnotation1 = new MyAnnotation();
        primary.add(primaryAnnotation1);

        AnnotationMap secondary = new AnnotationMap();

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
        assertEquals(primaryAnnotation1.getClass(), mergedAnnotationMap.get(MyAnnotation.class).getClass());
        assertEquals(primaryAnnotation1, mergedAnnotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAnnotationMapMergeDifferentAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        MyAnnotation primaryAnnotation1 = new MyAnnotation();
        primary.add(primaryAnnotation1);

        AnnotationMap secondary = new AnnotationMap();
        MyAnnotationSecondary secondaryAnnotation1 = new MyAnnotationSecondary();
        secondary.add(secondaryAnnotation1);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
        assertNotEquals(primaryAnnotation1, mergedAnnotationMap.get(MyAnnotation.class));
        assertNotEquals(secondaryAnnotation1, mergedAnnotationMap.get(MyAnnotation.class));
        assertEquals(primaryAnnotation1.getClass(), mergedAnnotationMap.get(MyAnnotation.class).getClass());
        assertEquals(primaryAnnotation1, mergedAnnotationMap.get(MyAnnotation.class));
        assertEquals(secondaryAnnotation1.getClass(), mergedAnnotationMap.get(MyAnnotationSecondary.class).getClass());
        assertEquals(secondaryAnnotation1, mergedAnnotationMap.get(MyAnnotationSecondary.class));
    }

    @Test
    public void testAnnotationMapMergeSameAnnotation() {
        AnnotationMap primary = new AnnotationMap();
        MyAnnotation primaryAnnotation = new MyAnnotation();
        primary.add(primaryAnnotation);

        AnnotationMap secondary = new AnnotationMap();
        MyAnnotation secondaryAnnotation = new MyAnnotation();
        secondary.add(secondaryAnnotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
        assertEquals(primaryAnnotation.getClass(), mergedAnnotationMap.get(MyAnnotation.class).getClass());
        assertEquals(primaryAnnotation, mergedAnnotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testMergeWithNullParameter() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);

        NullPointerException exception = assertThrows(NullPointerException.class, () -> AnnotationMap.merge(annotationMap, null));
        assertTrue(exception.getMessage().contains("null"));
    }

    @Test
    public void testMergeWithEmptyMap() {
        AnnotationMap primary = new AnnotationMap();
        MyAnnotation primaryAnnotation = new MyAnnotation();
        primary.add(primaryAnnotation);

        AnnotationMap secondary = new AnnotationMap();

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
        assertEquals(primaryAnnotation.getClass(), mergedAnnotationMap.get(MyAnnotation.class).getClass());
        assertEquals(primaryAnnotation, mergedAnnotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testMergeWithMapContainingSameAnnotation() {
        AnnotationMap primary = new AnnotationMap();
        MyAnnotation primaryAnnotation = new MyAnnotation();
        primary.add(primaryAnnotation);

        AnnotationMap secondary = new AnnotationMap();
        MyAnnotation secondaryAnnotation = new MyAnnotation();
        secondary.add(secondaryAnnotation);
        secondary.add(primaryAnnotation); // add the same annotation as in the primary map

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
        assertEquals(primaryAnnotation.getClass(), mergedAnnotationMap.get(MyAnnotation.class).getClass());
        assertEquals(primaryAnnotation, mergedAnnotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testMergeWithMapContainingDifferentAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        MyAnnotation primaryAnnotation = new MyAnnotation();
        primary.add(primaryAnnotation);

        AnnotationMap secondary = new AnnotationMap();
        MyAnnotationSecondary secondaryAnnotation = new MyAnnotationSecondary();
        secondary.add(secondaryAnnotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primary, secondary);
        assertNotEquals(primaryAnnotation, mergedAnnotationMap.get(MyAnnotation.class));
        assertNotEquals(secondaryAnnotation, mergedAnnotationMap.get(MyAnnotation.class));
        assertEquals(primaryAnnotation.getClass(), mergedAnnotationMap.get(MyAnnotation.class).getClass());
        assertEquals(primaryAnnotation, mergedAnnotationMap.get(MyAnnotation.class));
        assertEquals(secondaryAnnotation.getClass(), mergedAnnotationMap.get(MyAnnotationSecondary.class).getClass());
        assertEquals(secondaryAnnotation, mergedAnnotationMap.get(MyAnnotationSecondary.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);

        List<Annotation> annotationsList = new ArrayList<>(annotationMap.annotations());
        assertEquals(1, annotationsList.size());
        assertEquals(myAnnotation.getClass(), annotationsList.get(0).getClass());
        assertEquals(myAnnotation, annotationsList.get(0));
    }

    @Test
    public void testEmptyListWhenNoAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        List<Annotation> annotationsList = new ArrayList<>(annotationMap.annotations());
        assertEquals(0, annotationsList.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();

        assertTrue(annotationMap.addIfNotPresent(myAnnotation));
        assertTrue(annotationMap.containsKey(MyAnnotation.class));
        assertEquals(myAnnotation, annotationMap.get(MyAnnotation.class));

        assertFalse(annotationMap.addIfNotPresent(myAnnotation));
        assertTrue(annotationMap.containsKey(MyAnnotation.class));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();

        assertTrue(annotationMap.add(myAnnotation));
        assertTrue(annotationMap.containsKey(MyAnnotation.class));
        assertEquals(myAnnotation, annotationMap.get(MyAnnotation.class));

        AnnotationMap newAnnotationMap = new AnnotationMap();
        newAnnotationMap.add(myAnnotation);
        assertNotEquals(annotationMap, newAnnotationMap);
        assertEquals(annotationMap.get(MyAnnotation.class), newAnnotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        MyAnnotation myAnnotation2 = new MyAnnotation();
        annotationMap.add(myAnnotation1);
        annotationMap.add(myAnnotation2);

        String expectedOutput = "AnnotationMap{_annotations=HashMap{MyAnnotation=MyAnnotation@78a47692, MyAnnotation=MyAnnotation@16d7e1f8}}";
        assertEquals(expectedOutput, annotationMap.toString());
    }

    @Test
    public void testMergeWithNullAnnotationArray() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);

        NullPointerException exception = assertThrows(NullPointerException.class, () -> AnnotationMap.merge(annotationMap, null));
        assertTrue(exception.getMessage().contains("null"));
    }

    @Test
    public void testMergeWithEmptyAnnotationArray() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, new AnnotationMap[]{});
        assertEquals(annotationMap.get(MyAnnotation.class), mergedAnnotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testMergeWithSingleAnnotationArray() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);

        MyAnnotation singleAnnotation = new MyAnnotation();
        AnnotationMap singleAnnotationMap = new AnnotationMap();
        singleAnnotationMap.add(singleAnnotation);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, new AnnotationMap[]{singleAnnotationMap});
        assertEquals(annotationMap.get(MyAnnotation.class), mergedAnnotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testMergeWithMultipleAnnotationArrays() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        annotationMap.add(myAnnotation1);

        MyAnnotation myAnnotation2 = new MyAnnotation();
        AnnotationMap myAnnotationMap2 = new AnnotationMap();
        myAnnotationMap2.add(myAnnotation2);

        MyAnnotation myAnnotation3 = new MyAnnotation();
        AnnotationMap myAnnotationMap3 = new AnnotationMap();
        myAnnotationMap3.add(myAnnotation3);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, new AnnotationMap[]{myAnnotationMap2, myAnnotationMap3});
        assertEquals(annotationMap.get(MyAnnotation.class), mergedAnnotationMap.get(MyAnnotation.class));
        assertEquals(myAnnotationMap2.get(MyAnnotation.class), mergedAnnotationMap.get(MyAnnotation.class));
        assertEquals(myAnnotationMap3.get(MyAnnotation.class), mergedAnnotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testMergeWithMultipleAnnotationArraysContainingSameAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        annotationMap.add(myAnnotation1);

        MyAnnotation myAnnotation2 = new MyAnnotation();
        AnnotationMap myAnnotationMap2 = new AnnotationMap();
        myAnnotationMap2.add(myAnnotation2);
        myAnnotationMap2.add(myAnnotation1); // add the same annotation as in the primary map

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, new AnnotationMap[]{myAnnotationMap2});
        assertEquals(annotationMap.get(MyAnnotation.class), mergedAnnotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testMergeWithMultipleAnnotationArraysContainingDifferentAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation1 = new MyAnnotation();
        annotationMap.add(myAnnotation1);

        MyAnnotationSecondary myAnnotation2 = new MyAnnotationSecondary();
        AnnotationMap myAnnotationMap2 = new AnnotationMap();
        myAnnotationMap2.add(myAnnotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, new AnnotationMap[]{myAnnotationMap2});
        assertNotEquals(annotationMap.get(MyAnnotation.class), mergedAnnotationMap.get(MyAnnotation.class));
        assertNotEquals(annotationMap.get(MyAnnotationSecondary.class), mergedAnnotationMap.get(MyAnnotationSecondary.class));
        assertEquals(annotationMap.get(MyAnnotation.class), mergedAnnotationMap.get(MyAnnotation.class));
        assertEquals(myAnnotation2, mergedAnnotationMap.get(MyAnnotationSecondary.class));
    }
}

class MyAnnotation {
    @Retention(RetentionPolicy.RUNTIME)
    @interface Native{}
}

class MyAnnotationSecondary {
    @Retention(RetentionPolicy.RUNTIME)
    @interface Native{}
}