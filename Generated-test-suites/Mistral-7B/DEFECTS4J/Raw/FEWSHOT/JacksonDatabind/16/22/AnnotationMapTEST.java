package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testSize() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.size());

        Annotation annotation = new MyAnnotation();
        map.add(annotation);
        assertEquals(1, map.size());
    }

    @Test
    public void testAdd() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new MyAnnotation();
        map.add(annotation);
        assertTrue(map.containsKey(annotation.annotationType()));
        assertTrue(map.containsValue(annotation));
        assertEquals(annotation, map.get(annotation.annotationType()));
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new MyAnnotation();
        assertFalse(map.addIfNotPresent(annotation));
        map.add(annotation);
        assertTrue(map.addIfNotPresent(annotation));
        assertEquals(annotation, map.get(annotation.annotationType()));
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        Annotation annotation1 = new MyAnnotation1();
        primary.add(annotation1);

        AnnotationMap secondary = new AnnotationMap();
        Annotation annotation2 = new MyAnnotation2();
        secondary.add(annotation2);

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);

        assertEquals(annotation1, merged.get(annotation1.annotationType()));
        assertEquals(annotation2, merged.get(annotation2.annotationType()));
    }

    @Test
    public void testMergeEmpty() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(primary, null);
        assertEquals(primary, merged);
    }

    @Test
    public void testMergeNull() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(null, primary);
        assertEquals(primary, merged);
    }

    @Test
    public void testGet() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new MyAnnotation();
        map.add(annotation);

        Annotation retrieved = map.get(annotation.annotationType());
        assertEquals(annotation, retrieved);
    }

    @Test
    public void testGetNull() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(null, map.get(MyOtherAnnotation.class));
    }

    @Test
    public void testAnnotationsEmpty() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(Collections.emptyList(), map.annotations());
    }

    @Test
    public void testAnnotationsWithAnnotations() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation1 = new MyAnnotation1();
        Annotation annotation2 = new MyAnnotation2();
        map.add(annotation1);
        map.add(annotation2);

        Set<Annotation> annotations = new HashSet<>();
        annotations.add(annotation1);
        annotations.add(annotation2);

        assertTrue(annotations.containsAll(map.annotations()));
        assertTrue(map.annotations().containsAll(annotations));
    }

    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new MyAnnotation();
        map.add(annotation);

        String expected = String.format("{%s=Annotation@%s}", annotation.annotationType().getName(), Long.toHexString(System.identityHashCode(annotation)));
        assertEquals(expected, map.toString());
    }

    @Test
    public void testAddDuplicateAnnotation() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new MyAnnotation();
        map.add(annotation);
        assertFalse(map.add(annotation));
    }

    @SuppressWarnings("unchecked")
    private static class MyAnnotation extends Object implements Annotation {
        private static final long serialVersionUID = 1L;

        @Override
        public Class<? extends Annotation> annotationType() {
            return MyAnnotation.class;
        }
    }

    @SuppressWarnings("unchecked")
    private static class MyAnnotation1 extends Object implements Annotation {
        private static final long serialVersionUID = 1L;

        @Override
        public Class<? extends Annotation> annotationType() {
            return MyAnnotation1.class;
        }
    }

    @SuppressWarnings("unchecked")
    private static class MyAnnotation2 extends Object implements Annotation {
        private static final long serialVersionUID = 1L;

        @Override
        public Class<? extends Annotation> annotationType() {
            return MyAnnotation2.class;
        }
    }

    @SuppressWarnings("unchecked")
    private static class MyOtherAnnotation extends Object implements Annotation {
        private static final long serialVersionUID = 1L;

        @Override
        public Class<? extends Annotation> annotationType() {
            return MyOtherAnnotation.class;
        }
    }
}