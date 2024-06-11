package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testConstructor() {
        AnnotationMap am = new AnnotationMap();
        assertEquals(0, am.size());
    }

    @Test
    public void testGet() {
        AnnotationMap am = new AnnotationMap();
        am.add(new MyAnnotation());

        MyAnnotation expected = new MyAnnotation();
        MyAnnotation actual = am.get(MyAnnotation.class);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetNull() {
        AnnotationMap am = new AnnotationMap();
        assertNull(am.get(Object.class));
    }

    @Test
    public void testAnnotationsEmptyList() {
        AnnotationMap am = new AnnotationMap();
        assertTrue(am.annotations().isEmpty());
    }

    @Test
    public void testAnnotationsIterator() {
        AnnotationMap am = new AnnotationMap();
        am.add(new MyAnnotation());
        am.add(new OtherAnnotation());

        List<Annotation> expected = new ArrayList<Annotation>(Arrays.asList(new MyAnnotation(), new OtherAnnotation()));
        List<Annotation> actual = new ArrayList<Annotation>(am.annotations());

        assertEquals(expected, actual);
    }

    @Test
    public void testMergeEmptyPrimary() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new MyAnnotation());

        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        assertTrue(result.size() > 0);
        assertEquals(1, result.size());
        assertEquals(secondary.get(MyAnnotation.class), result.get(MyAnnotation.class));
    }

    @Test
    public void testMergeEmptySecondary() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new MyAnnotation());

        AnnotationMap secondary = new AnnotationMap();

        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        assertTrue(result.size() > 0);
        assertEquals(1, result.size());
        assertEquals(primary.get(MyAnnotation.class), result.get(MyAnnotation.class));
    }

    @Test
    public void testMergePrimaryContainsSecondary() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new MyAnnotation());
        primary.add(new OtherAnnotation());

        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new MyAnnotation());

        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        assertTrue(result.size() > 0);
        assertEquals(2, result.size());
        assertEquals(secondary.get(MyAnnotation.class), result.get(MyAnnotation.class));
        assertEquals(primary.get(OtherAnnotation.class), result.get(OtherAnnotation.class));
    }

    @Test
    public void testMergeSecondaryContainsPrimary() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new MyAnnotation());

        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new MyAnnotation());
        secondary.add(new OtherAnnotation());

        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        assertTrue(result.size() > 0);
        assertEquals(2, result.size());
        assertEquals(secondary.get(MyAnnotation.class), result.get(MyAnnotation.class));
        assertEquals(primary.get(OtherAnnotation.class), result.get(OtherAnnotation.class));
    }

    @Test
    public void testMergePrimaryAndSecondaryBothPresent() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new MyAnnotation());
        primary.add(new OtherAnnotation());

        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new MyAnnotation());
        secondary.add(new DifferentAnnotation());

        AnnotationMap result = AnnotationMap.merge(primary, secondary);

        assertTrue(result.size() > 0);
        assertEquals(3, result.size());
        assertEquals(secondary.get(MyAnnotation.class), result.get(MyAnnotation.class));
        assertEquals(primary.get(OtherAnnotation.class), result.get(OtherAnnotation.class));
        assertEquals(secondary.get(DifferentAnnotation.class), result.get(DifferentAnnotation.class));
    }

    @Test
    public void testSize() {
        AnnotationMap am = new AnnotationMap();
        am.add(new MyAnnotation());
        am.add(new OtherAnnotation());

        assertEquals(2, am.size());
    }

    @Test
    public void testAddIfNotPresentTrue() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();

        assertTrue(am.addIfNotPresent(annotation));
        assertTrue(am.size() > 0);
        assertEquals(annotation, am.get(MyAnnotation.class));
    }

    @Test
    public void testAddIfNotPresentFalse() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();

        am.add(annotation);

        assertFalse(am.addIfNotPresent(annotation));
        assertEquals(1, am.size());
        assertEquals(annotation, am.get(MyAnnotation.class));
    }

    @Test
    public void testToString() {
        AnnotationMap am = new AnnotationMap();
        am.add(new MyAnnotation());
        am.add(new OtherAnnotation());

        String expected = "{" +
                "MyAnnotation(class java.lang.annotation.Annotation) = MyAnnotation(), " +
                "OtherAnnotation(class java.lang.annotation.Annotation) = OtherAnnotation()" +
                "}";
        String actual = am.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testMergeTwoEmptyMaps() {
        AnnotationMap map1 = new AnnotationMap();
        AnnotationMap map2 = new AnnotationMap();

        assertEquals(map2, AnnotationMap.merge(map1, map2));
    }

    @Test
    public void testMergeEmptyAndNonEmptyMaps() {
        AnnotationMap map1 = new AnnotationMap();

        MyAnnotation myAnnotation = new MyAnnotation();
        OtherAnnotation otherAnnotation = new OtherAnnotation();

        AnnotationMap map2 = new AnnotationMap();
        map2.add(myAnnotation);
        map2.add(otherAnnotation);

        AnnotationMap result = AnnotationMap.merge(map1, map2);

        assertEquals(map2, result);
    }

    @Test
    public void testMergeTwoNonEmptyMaps() {
        AnnotationMap map1 = new AnnotationMap();
        map1.add(new MyAnnotation());
        map1.add(new OtherAnnotation());

        AnnotationMap map2 = new AnnotationMap();
        map2.add(new DifferentAnnotation());

        AnnotationMap result = AnnotationMap.merge(map1, map2);

        assertEquals(3, result.size());
        assertEquals(map1.get(MyAnnotation.class), result.get(MyAnnotation.class));
        assertEquals(map1.get(OtherAnnotation.class), result.get(OtherAnnotation.class));
        assertEquals(map2.get(DifferentAnnotation.class), result.get(DifferentAnnotation.class));
    }

    private static class MyAnnotation {
    }

    private static class OtherAnnotation {
    }

    private static class DifferentAnnotation {
    }
}