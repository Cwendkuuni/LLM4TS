package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Override.class));

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(Deprecated.class, new DeprecatedImpl());
        annotationMap = new AnnotationMap(annotations);
        assertEquals(Deprecated.class, annotationMap.get(Deprecated.class).annotationType());
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(Collections.emptyList(), annotationMap.annotations());

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(Deprecated.class, new DeprecatedImpl());
        annotationMap = new AnnotationMap(annotations);
        assertEquals(annotations.values(), annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        assertNull(AnnotationMap.merge(primary, null));

        AnnotationMap secondary = new AnnotationMap();
        assertNull(AnnotationMap.merge(null, secondary));

        primary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>());
        annotations.put(Deprecated.class, new DeprecatedImpl());

        secondary = new AnnotationMap(new HashMap<Class<? extends Annotation>, Annotation>());
        annotations.put(SuppressWarnings.class, new SuppressWarningsImpl());

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(Deprecated.class, merged.get(Deprecated.class).annotationType());
        assertEquals(SuppressWarnings.class, merged.get(SuppressWarnings.class).annotationType());
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(Deprecated.class, new DeprecatedImpl());
        annotationMap = new AnnotationMap(annotations);
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        DeprecatedImpl deprecated = new DeprecatedImpl();
        assertEquals(true, annotationMap.addIfNotPresent(deprecated));
        assertEquals(false, annotationMap.addIfNotPresent(deprecated));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        DeprecatedImpl deprecated = new DeprecatedImpl();
        assertEquals(true, annotationMap.add(deprecated));
        assertEquals(false, annotationMap.add(deprecated));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(Deprecated.class, new DeprecatedImpl());
        annotationMap = new AnnotationMap(annotations);
        assertEquals(annotations.toString(), annotationMap.toString());
    }

    class DeprecatedImpl implements Deprecated {

    }

    class SuppressWarningsImpl implements SuppressWarnings {

        @Override
        public Class<? extends Annotation> annotationType() {
            return SuppressWarnings.class;
        }

        @Override
        public String[] value() {
            return new String[0];
        }
    }
}