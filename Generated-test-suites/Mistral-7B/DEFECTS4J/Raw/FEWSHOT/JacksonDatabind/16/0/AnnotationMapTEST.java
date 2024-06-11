package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.*;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testSize() {
        AnnotationMap am = new AnnotationMap();
        assertEquals(0, am.size());

        Annotation annotation1 = mock(Annotation.class);
        AnnotationMap am1 = new AnnotationMap();
        am1.add(annotation1);
        assertEquals(1, am1.size());

        AnnotationMap am2 = new AnnotationMap(am1._annotations);
        assertEquals(1, am2.size());

        Annotation annotation2 = mock(Annotation.class);
        am1.add(annotation2);
        assertEquals(2, am1.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap am = new AnnotationMap();

        Annotation annotation1 = mock(Annotation.class);
        assertTrue(am.addIfNotPresent(annotation1));
        assertFalse(am.addIfNotPresent(annotation1));

        Annotation annotation2 = mock(Annotation.class);
        assertTrue(am.addIfNotPresent(annotation2));
    }

    @Test
    public void testAdd() {
        AnnotationMap am = new AnnotationMap();

        Annotation annotation1 = mock(Annotation.class);
        am.add(annotation1);
        assertSame(annotation1, am.get(annotation1.annotationType()));

        Annotation annotation2 = mock(Annotation.class);
        am.add(annotation2);
        assertTrue(am.annotations().contains(annotation1));
        assertTrue(am.annotations().contains(annotation2));
    }

    @Test
    public void testMerge() {
        Annotation annotation1 = mock(Annotation.class);
        AnnotationMap am1 = new AnnotationMap();
        am1.add(annotation1);

        Annotation annotation2 = mock(Annotation.class);
        AnnotationMap am2 = new AnnotationMap();
        am2.add(annotation2);

        AnnotationMap amMerged = AnnotationMap.merge(am1, am2);
        assertTrue(amMerged.annotations().contains(annotation1));
        assertTrue(amMerged.annotations().contains(annotation2));
    }

    @Test
    public void testGet() {
        AnnotationMap am = new AnnotationMap();

        Annotation annotation1 = mock(Annotation.class);
        am.add(annotation1);

        Annotation annotation2 = mock(Annotation.class);
        am.add(annotation2);

        Annotation annotationExpected = annotation1;
        Annotation annotationReturned = am.get(annotationExpected.annotationType());
        assertSame(annotationExpected, annotationReturned);

        Annotation annotationTypeDifferent = mock(Annotation.class);
        assertNull(am.get(annotationTypeDifferent.annotationType()));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap am = new AnnotationMap();

        Annotation annotation1 = mock(Annotation.class);
        Annotation annotation2 = mock(Annotation.class);
        am.add(annotation1);
        am.add(annotation2);

        Iterator<Annotation> iterator = am.annotations().iterator();
        assertTrue(iterator.hasNext());
        Annotation annotation1Iterator = iterator.next();
        assertEquals(annotation1, annotation1Iterator);
        assertTrue(iterator.hasNext());
        Annotation annotation2Iterator = iterator.next();
        assertEquals(annotation2, annotation2Iterator);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testToString() {
        AnnotationMap am = new AnnotationMap();
        assertEquals("[]", am.toString());

        Annotation annotation1 = mock(Annotation.class);
        AnnotationMap am1 = new AnnotationMap();
        am1.add(annotation1);
        assertEquals("[AnnotationClass1]", am1.toString());

        AnnotationMap am2 = new AnnotationMap(am1._annotations);
        assertEquals("[AnnotationClass1]", am2.toString());

        Annotation annotation2 = mock(Annotation.class);
        am1.add(annotation2);
        assertEquals("[AnnotationClass1, AnnotationClass2]", am1.toString());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        AnnotationMap am = new AnnotationMap();

        Annotation annotation1 = mock(Annotation.class);
        am.add(annotation1);

        Iterator<Annotation> iterator = am.annotations().iterator();
        iterator.remove();
    }
}