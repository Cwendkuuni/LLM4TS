package com.fasterxml.jackson.databind.introspect;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {

    @Test
    public void testSize() {
        AnnotationMap am = new AnnotationMap();
        Assert.assertEquals(0, am.size());

        Annotation ann1 = getAnnotationMock();
        AnnotationMap am1 = new AnnotationMap();
        am1.put(ann1.getClass(), ann1);
        Assert.assertEquals(1, am1.size());

        Annotation ann2 = getAnnotationMock();
        AnnotationMap am2 = new AnnotationMap();
        am2.put(ann2.getClass(), ann2);
        am2.put(ann1.getClass(), ann1);
        Assert.assertEquals(2, am2.size());
    }

    @Test
    public void testGet() {
        AnnotationMap am = new AnnotationMap();
        Annotation ann1 = getAnnotationMock();
        Annotation ann2 = getAnnotationMock();

        am.put(ann1.getClass(), ann1);
        am.put(ann2.getClass(), ann2);

        Assert.assertNull(am.get(null));
        Assert.assertNull(am.get(Object.class.getAnnotation(null)));
        Assert.assertNull(am.get(Object.class.getAnnotation(null)));

        Assert.assertEquals(ann1, am.get(ann1.getClass()));
        Assert.assertEquals(ann2, am.get(ann2.getClass()));
    }

    @Test
    public void testIterableAnnotations() {
        AnnotationMap am = new AnnotationMap();
        Annotation ann1 = getAnnotationMock();
        Annotation ann2 = getAnnotationMock();

        am.put(ann1.getClass(), ann1);
        am.put(ann2.getClass(), ann2);

        List<Annotation> annotations = new ArrayList<>(am.annotations());
        Iterator<Annotation> i = annotations.iterator();
        Assert.assertEquals(ann1, i.next());
        Assert.assertEquals(ann2, i.next());
        Assert.assertFalse(i.hasNext());

        Assert.assertEquals(Collections.emptyList(), new AnnotationMap().annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap am1 = new AnnotationMap();
        Annotation ann1 = getAnnotationMock();
        AnnotationMap am2 = new AnnotationMap();
        Annotation ann2 = getAnnotationMock();
        AnnotationMap am3 = new AnnotationMap();
        Annotation ann3 = getAnnotationMock();

        am1.put(ann1.getClass(), ann1);
        am2.put(ann2.getClass(), ann2);
        am2.put(ann1.getClass(), ann1);

        AnnotationMap merged = AnnotationMap.merge(am1, am2);
        Assert.assertEquals(am2.size(), merged.size());
        Assert.assertEquals(ann1, merged.get(ann1.getClass()));
        Assert.assertEquals(ann2, merged.get(ann2.getClass()));

        am3.put(ann3.getClass(), ann3);
        merged = AnnotationMap.merge(merged, am3);
        Assert.assertEquals(am3.size() + merged.size(), merged.size());
        Assert.assertEquals(ann3, merged.get(ann3.getClass()));
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap am = new AnnotationMap();
        Annotation ann1 = getAnnotationMock();
        Annotation ann2 = getAnnotationMock();

        Assert.assertFalse(am.addIfNotPresent(ann1));
        Assert.assertEquals(Collections.emptyList(), am.annotations());

        Assert.assertTrue(am.addIfNotPresent(ann1));
        Assert.assertEquals(1, am.size());
        Assert.assertEquals(ann1, am.get(ann1.getClass()));

        Assert.assertFalse(am.addIfNotPresent(ann1));

        Assert.assertTrue(am.addIfNotPresent(ann2));
        Assert.assertEquals(2, am.size());
        Assert.assertEquals(ann1, am.get(ann1.getClass()));
        Assert.assertEquals(ann2, am.get(ann2.getClass()));
    }

    @Test
    public void testAdd() {
        AnnotationMap am = new AnnotationMap();
        Annotation ann1 = getAnnotationMock();
        Annotation ann2 = getAnnotationMock();

        Assert.assertTrue(am.add(ann1));
        Assert.assertEquals(1, am.size());
        Assert.assertEquals(ann1, am.get(ann1.getClass()));

        Assert.assertTrue(am.add(ann2));
        Assert.assertEquals(2, am.size());
        Assert.assertEquals(ann1, am.get(ann1.getClass()));
        Assert.assertEquals(ann2, am.get(ann2.getClass()));

        Set<Class<? extends Annotation>> classes = new HashSet<>(2);
        classes.add(ann1.getClass());
        classes.add(ann2.getClass());
        for (Annotation annotation : am.annotations()) {
            Assert.assertTrue(classes.contains(annotation.annotationType()));
        }
    }

    private Annotation getAnnotationMock() {
        return new MyAnnotation();
    }

    private static class MyAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return MyAnnotation.class;
        }

        @Override
        public String toString() {
            return "MyAnnotation";
        }
    }
}