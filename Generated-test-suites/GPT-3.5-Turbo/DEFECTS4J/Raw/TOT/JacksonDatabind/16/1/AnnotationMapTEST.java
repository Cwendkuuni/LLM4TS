package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        // Test case for successful retrieval of annotation
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new Deprecated() {
            public String value() { return "This annotation is deprecated"; }
            public Class<? extends Annotation> annotationType() { return Deprecated.class; }
        });

        Deprecated deprecated = annotationMap.get(Deprecated.class);
        assertNotNull(deprecated);
        assertEquals("This annotation is deprecated", deprecated.value());

        // Test case for annotation not present
        assertNull(annotationMap.get(SuppressWarnings.class));
    }

    @Test
    public void testAnnotations() {
        // Test case when no annotations are present
        AnnotationMap annotationMap = new AnnotationMap();
        Iterable<Annotation> annotations = annotationMap.annotations();
        assertNotNull(annotations);
        Iterator<Annotation> iterator = annotations.iterator();
        assertFalse(iterator.hasNext());

        // Test case when annotations are present
        annotationMap.add(new Deprecated() {
            public String value() { return "This annotation is deprecated"; }
            public Class<? extends Annotation> annotationType() { return Deprecated.class; }
        });
        annotationMap.add(new SuppressWarnings() {
            public String[] value() { return new String[] { "unchecked", "deprecation" }; }
            public Class<? extends Annotation> annotationType() { return SuppressWarnings.class; }
        });
        
        annotations = annotationMap.annotations();
        assertNotNull(annotations);
        iterator = annotations.iterator();
        assertTrue(iterator.hasNext());
        List<String> annotationNames = new ArrayList<>();
        while (iterator.hasNext()) {
            Annotation annotation = iterator.next();
            annotationNames.add(annotation.annotationType().getSimpleName());
        }
        assertEquals(2, annotationNames.size());
        assertTrue(annotationNames.contains("Deprecated"));
        assertTrue(annotationNames.contains("SuppressWarnings"));
    }

    @Test
    public void testMerge() {
        // Test case when primary annotation map is null
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new Deprecated() {
            public String value() { return "This annotation is deprecated"; }
            public Class<? extends Annotation> annotationType() { return Deprecated.class; }
        });

        AnnotationMap merged = AnnotationMap.merge(null, secondary);
        assertNotNull(merged);
        assertEquals(1, merged.size());
        Deprecated deprecated = merged.get(Deprecated.class);
        assertNotNull(deprecated);
        assertEquals("This annotation is deprecated", deprecated.value());

        // Test case when secondary annotation map is null
        AnnotationMap primary = new AnnotationMap();
        primary.add(new Deprecated() {
            public String value() { return "This annotation is deprecated"; }
            public Class<? extends Annotation> annotationType() { return Deprecated.class; }
        });

        merged = AnnotationMap.merge(primary, null);
        assertNotNull(merged);
        assertEquals(1, merged.size());
        deprecated = merged.get(Deprecated.class);
        assertNotNull(deprecated);
        assertEquals("This annotation is deprecated", deprecated.value());

        // Test case when both primary and secondary annotation maps are null
        merged = AnnotationMap.merge(null, null);
        assertNull(merged);

        // Test case when primary and secondary annotation maps are not null
        primary = new AnnotationMap();
        primary.add(new Deprecated() {
            public String value() { return "This annotation is deprecated"; }
            public Class<? extends Annotation> annotationType() { return Deprecated.class; }
        });

        secondary = new AnnotationMap();
        secondary.add(new SuppressWarnings() {
            public String[] value() { return new String[] { "unchecked", "deprecation" }; }
            public Class<? extends Annotation> annotationType() { return SuppressWarnings.class; }
        });

        merged = AnnotationMap.merge(primary, secondary);
        assertNotNull(merged);
        assertEquals(2, merged.size());
        deprecated = merged.get(Deprecated.class);
        assertNotNull(deprecated);
        assertEquals("This annotation is deprecated", deprecated.value());
        SuppressWarnings suppressWarnings = merged.get(SuppressWarnings.class);
        assertNotNull(suppressWarnings);
        assertArrayEquals(new String[] { "unchecked", "deprecation" }, suppressWarnings.value());
    }

    @Test
    public void testSize() {
        // Test case when annotation map is null
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        // Test case when annotation map is not null
        annotationMap.add(new Deprecated() {
            public String value() { return "This annotation is deprecated"; }
            public Class<? extends Annotation> annotationType() { return Deprecated.class; }
        });
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        // Test case when annotation is not present in the map
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.addIfNotPresent(new Deprecated() {
            public String value() { return "This annotation is deprecated"; }
            public Class<? extends Annotation> annotationType() { return Deprecated.class; }
        }));
        assertEquals(1, annotationMap.size());
        Deprecated deprecated = annotationMap.get(Deprecated.class);
        assertNotNull(deprecated);
        assertEquals("This annotation is deprecated", deprecated.value());

        // Test case when annotation is already present in the map
        assertFalse(annotationMap.addIfNotPresent(new Deprecated() {
            public String value() { return "This annotation is deprecated"; }
            public Class<? extends Annotation> annotationType() { return Deprecated.class; }
        }));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAdd() {
        // Test case when addition changes the contents of the annotation map
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.add(new Deprecated() {
            public String value() { return "This annotation is deprecated"; }
            public Class<? extends Annotation> annotationType() { return Deprecated.class; }
        }));
        assertEquals(1, annotationMap.size());
        Deprecated deprecated = annotationMap.get(Deprecated.class);
        assertNotNull(deprecated);
        assertEquals("This annotation is deprecated", deprecated.value());

        // Test case when addition does not change the contents of the annotation map
        assertFalse(annotationMap.add(new Deprecated() {
            public String value() { return "This annotation is deprecated"; }
            public Class<? extends Annotation> annotationType() { return Deprecated.class; }
        }));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testToString() {
        // Test case when annotation map is null
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());

        // Test case when annotation map is not null
        annotationMap.add(new Deprecated() {
            public String value() { return "This annotation is deprecated"; }
            public Class<? extends Annotation> annotationType() { return Deprecated.class; }
        });
        assertEquals("{@java.lang.Deprecated(value=This annotation is deprecated)}", annotationMap.toString());
    }
}