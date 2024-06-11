package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testAnnotationMapCreation() {
        AnnotationMap am = new AnnotationMap();
        assertNull(am._annotations);
    }

    @Test
    public void testAnnotationMapCreationWithAnnotations() {
        AnnotationMap am = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation.class, new MyAnnotation());
        }});
        Map<Class<? extends Annotation>, Annotation> annotations = am._annotations;
        assertNotNull(annotations);
        assertEquals(1, annotations.size());
        assertEquals(MyAnnotation.class, annotations.keySet().iterator().next());
        assertEquals(new MyAnnotation(), annotations.get(MyAnnotation.class));
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap am = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation.class, new MyAnnotation());
        }});
        assertEquals(MyAnnotation.class, am.get(MyAnnotation.class.getClass()));
        assertNull(am.get(NotANnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap am = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation.class, new MyAnnotation());
        }});
        Iterable<Annotation> annotations = am.annotations();
        assertNotNull(annotations);
        assertEquals(1, Iterator.class.cast(annotations.iterator()).size());
        assertEquals(MyAnnotation.class, Iterator.class.cast(annotations.iterator()).next().annotationType());
    }

    @Test
    public void testMerge() {
        AnnotationMap am1 = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation1.class, new MyAnnotation1());
        }});
        AnnotationMap am2 = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation2.class, new MyAnnotation2());
        }});
        AnnotationMap merged = AnnotationMap.merge(am1, am2);
        Map<Class<? extends Annotation>, Annotation> mergedAnnotations = merged._annotations;
        assertEquals(2, mergedAnnotations.size());
        assertEquals(MyAnnotation1.class, mergedAnnotations.keySet().iterator().next());
        assertEquals(MyAnnotation1.class, mergedAnnotations.get(MyAnnotation1.class));
        assertEquals(MyAnnotation2.class, mergedAnnotations.keySet().iterator().next());
        assertEquals(MyAnnotation2.class, mergedAnnotations.get(MyAnnotation2.class));
    }

    @Test
    public void testSize() {
        AnnotationMap am = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation.class, new MyAnnotation());
            put(MyAnnotation2.class, new MyAnnotation2());
        }});
        assertEquals(2, am.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        assertTrue(am.addIfNotPresent(myAnnotation));
        assertEquals(myAnnotation, am.get(MyAnnotation.class));
    }

    @Test
    public void testAddIfNotPresentWithPresentAnnotation() {
        AnnotationMap am = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation.class, new MyAnnotation());
        }});
        MyAnnotation myAnnotation = new MyAnnotation();
        assertFalse(am.addIfNotPresent(myAnnotation));
    }

    @Test
    public void testAdd() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        assertTrue(am.add(myAnnotation));
        assertEquals(myAnnotation, am.get(MyAnnotation.class));
    }

    @Test
    public void testToString() {
        AnnotationMap am = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation.class, new MyAnnotation());
        }});
        assertEquals("[AnnotationMap(annotations=[Annotation@1])]", am.toString());
    }

    @Test
    public void testEmptyAnnotationMapToString() {
        AnnotationMap am = new AnnotationMap();
        assertEquals("[AnnotationMap(annotations=[])]", am.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testNullAnnotationMap() {
        AnnotationMap am = null;
        am.annotations();
    }

    @Test(expected = NullPointerException.class)
    public void testNullAnnotationTypeWhenGettingAnnotation() {
        AnnotationMap am = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation.class, new MyAnnotation());
        }});
        am.get(null);
    }

    @Test(expected = ClassCastException.class)
    public void testGetAnnotationWithNonAnnotationType() {
        AnnotationMap am = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation.class, new MyAnnotation());
        }});
        am.get(String.class);
    }

    @Test
    public void testMergeWithNullPrimaryAnnotationMap() {
        AnnotationMap secondary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation.class, new MyAnnotation());
        }});
        AnnotationMap merged = AnnotationMap.merge(null, secondary);
        assertEquals(secondary, merged);
    }

    @Test
    public void testMergeWithNullSecondaryAnnotationMap() {
        AnnotationMap primary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation.class, new MyAnnotation());
        }});
        AnnotationMap merged = AnnotationMap.merge(primary, null);
        assertEquals(primary, merged);
    }

    @Test
    public void testMergeWithEmptySecondaryAnnotationMap() {
        AnnotationMap primary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation.class, new MyAnnotation());
        }});
        AnnotationMap merged = AnnotationMap.merge(primary, new AnnotationMap());
        assertEquals(primary, merged);
    }

    @Test
    public void testMergeWithEmptyPrimaryAnnotationMap() {
        AnnotationMap secondary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation.class, new MyAnnotation());
        }});
        AnnotationMap merged = AnnotationMap.merge(new AnnotationMap(), secondary);
        assertEquals(secondary, merged);
    }

    @Test
    public void testMergeWithIdenticalAnnotationMap() {
        AnnotationMap primary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation.class, new MyAnnotation());
        }});
        AnnotationMap secondary = new AnnotationMap(primary);
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(primary, merged);
    }

    @Test
    public void testMergeWithOverlappingAnnotationMap() {
        AnnotationMap primary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation1.class, new MyAnnotation1());
            put(MyAnnotation2.class, new MyAnnotation2());
        }});
        AnnotationMap secondary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation1.class, new MyAnnotation1());
        }});
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        Map<Class<? extends Annotation>, Annotation> mergedAnnotations = merged._annotations;
        assertEquals(2, mergedAnnotations.size());
        assertEquals(MyAnnotation1.class, mergedAnnotations.keySet().iterator().next());
        assertEquals(new MyAnnotation1(), mergedAnnotations.get(MyAnnotation1.class));
        assertEquals(MyAnnotation2.class, mergedAnnotations.keySet().iterator().next());
        assertEquals(new MyAnnotation2(), mergedAnnotations.get(MyAnnotation2.class));
    }

    @Test
    public void testMergeWithDifferentAnnotationTypes() {
        AnnotationMap primary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation1.class, new MyAnnotation1());
        }});
        AnnotationMap secondary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>() {{
            put(MyAnnotation2.class, new MyAnnotation2());
        }});
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        Map<Class<? extends Annotation>, Annotation> mergedAnnotations = merged._annotations;
        assertEquals(2, mergedAnnotations.size());
        assertEquals(MyAnnotation1.class, mergedAnnotations.keySet().iterator().next());
        assertEquals(new MyAnnotation1(), mergedAnnotations.get(MyAnnotation1.class));
        assertEquals(MyAnnotation2.class, mergedAnnotations.keySet().iterator().next());
        assertEquals(new MyAnnotation2(), mergedAnnotations.get(MyAnnotation2.class));
    }

    @Test
    public void testIteratorReturnsSameOrderAsPut() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation1 ann1 = new MyAnnotation1();
        MyAnnotation2 ann2 = new MyAnnotation2();
        am.add(ann1);
        am.add(ann2);
        Iterator<Annotation> it = am.annotations().iterator();
        assertEquals(ann1, it.next());
        assertEquals(ann2, it.next());
        assertFalse(it.hasNext());
    }

    static class MyAnnotation {
    }

    static class MyAnnotation1 {
    }

    static class MyAnnotation2 {
    }

    static class NotANnotation {
    }
}