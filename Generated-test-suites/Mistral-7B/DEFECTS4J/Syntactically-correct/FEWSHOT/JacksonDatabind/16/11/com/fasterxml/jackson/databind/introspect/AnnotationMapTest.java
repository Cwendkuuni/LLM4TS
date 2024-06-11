package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testAnnotationMap() {
        AnnotationMap am = new AnnotationMap();
        assertNull(am.get(Object.class));
        assertTrue(am.size() == 0);
        assertFalse(am.addIfNotPresent(null));
        assertFalse(am.addIfNotPresent(new Object() {}));
        assertTrue(am.addIfNotPresent(Annotation1.class));
        assertEquals(Annotation1.class, am.get(Annotation1.class));
        assertEquals(1, am.size());
        assertFalse(am.addIfNotPresent(Annotation1.class));
        assertTrue(am.add(Annotation2.class));
        assertEquals(Annotation2.class, am.get(Annotation2.class));
        assertEquals(2, am.size());
        assertFalse(am.addIfNotPresent(Annotation1.class));
        assertSame(Annotation1.class, am.get(Annotation1.class));
        assertSame(Annotation2.class, am.get(Annotation2.class));
        List<Annotation> annotations = Arrays.asList(am.annotations()).stream().toList();
        assertEquals(2, annotations.size());
        assertEquals(Annotation1.class, annotations.get(0));
        assertEquals(Annotation2.class, annotations.get(1));
        Set<Class<? extends Annotation>> expectedSet = new HashSet<>(Arrays.asList(Annotation1.class, Annotation2.class));
        assertTrue(am.containsAll(expectedSet));
    }

    @Test
    public void testMerge() {
        AnnotationMap am1 = new AnnotationMap();
        am1.add(Annotation1.class);
        AnnotationMap am2 = new AnnotationMap();
        am2.add(Annotation2.class);
        AnnotationMap am3 = AnnotationMap.merge(am1, am2);
        assertEquals(Annotation1.class, am3.get(Annotation1.class));
        assertEquals(Annotation2.class, am3.get(Annotation2.class));
        assertEquals(2, am3.size());
        Set<Class<? extends Annotation>> expectedSet = new HashSet<>(Arrays.asList(Annotation1.class, Annotation2.class));
        assertTrue(am3.containsAll(expectedSet));

        AnnotationMap am4 = new AnnotationMap();
        am4.add(Annotation3.class);
        am3 = AnnotationMap.merge(am3, am4);
        assertEquals(Annotation1.class, am3.get(Annotation1.class));
        assertEquals(Annotation2.class, am3.get(Annotation2.class));
        assertEquals(Annotation3.class, am3.get(Annotation3.class));
        assertEquals(3, am3.size());
        expectedSet = new HashSet<>(Arrays.asList(Annotation1.class, Annotation2.class, Annotation3.class));
        assertTrue(am3.containsAll(expectedSet));

        AnnotationMap am5 = new AnnotationMap();
        am5.add(Annotation1.class);
        am5.add(Annotation2.class);
        am3 = AnnotationMap.merge(am3, am5);
        assertEquals(Annotation1.class, am3.get(Annotation1.class));
        assertEquals(Annotation2.class, am3.get(Annotation2.class));
        assertEquals(Annotation3.class, am3.get(Annotation3.class));
        assertEquals(Annotation1.class, am3.get(Annotation1.class));
        assertEquals(Annotation2.class, am3.get(Annotation2.class));
        assertEquals(3, am3.size());
        expectedSet = new HashSet<>(Arrays.asList(Annotation1.class, Annotation2.class, Annotation3.class));
        assertTrue(am3.containsAll(expectedSet));
    }

    @Test
    public void testSize() {
        AnnotationMap am = new AnnotationMap();
        assertEquals(0, am.size());
        am.add(Annotation1.class);
        assertEquals(1, am.size());
        am.add(Annotation2.class);
        assertEquals(2, am.size());
    }

    @Test
    public void testToString() {
        AnnotationMap am = new AnnotationMap();
        assertEquals("[null]", am.toString());
        am.add(Annotation1.class);
        assertEquals("[@com.fasterxml.jackson.annotation.JsonAutoDetect(creator = com.fasterxml.jackson.annotation.JsonAutoDetect.None.class, fieldVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY, getterVisibility = com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE)]", am.toString());
    }

    static class Annotation1 {}

    static class Annotation2 {}

    static class Annotation3 {}
}