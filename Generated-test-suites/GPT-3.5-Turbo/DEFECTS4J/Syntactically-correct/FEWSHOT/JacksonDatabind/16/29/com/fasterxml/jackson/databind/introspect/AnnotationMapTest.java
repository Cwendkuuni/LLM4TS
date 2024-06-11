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
        
        assertNull(annotationMap.get(Test.class));
        
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        
        annotationMap._annotations = annotations;
        
        assertEquals(TestAnnotation.class, annotationMap.get(Test.class).getClass());
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        assertEquals(Collections.emptyList(), annotationMap.annotations());
        
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        
        annotationMap._annotations = annotations;
        
        assertEquals(Collections.singletonList(new TestAnnotation()), annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        assertNull(primary.merge(null, null));
        
        AnnotationMap secondary = new AnnotationMap();
        
        HashMap<Class<? extends Annotation>,Annotation> annotations1 = new HashMap<>();
        annotations1.put(Test.class, new TestAnnotation());
        primary._annotations = annotations1;
        
        HashMap<Class<? extends Annotation>,Annotation> annotations2 = new HashMap<>();
        annotations2.put(Deprecated.class, new DeprecatedAnnotation());
        secondary._annotations = annotations2;
        
        AnnotationMap merged = primary.merge(primary, secondary);
        HashMap<Class<? extends Annotation>,Annotation> expectedAnnotations = new HashMap<>();
        expectedAnnotations.put(Test.class, new TestAnnotation());
        expectedAnnotations.put(Deprecated.class, new DeprecatedAnnotation());
        
        assertEquals(expectedAnnotations, merged._annotations);
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        assertEquals(0, annotationMap.size());
        
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        
        annotationMap._annotations = annotations;
        
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        TestAnnotation annotation = new TestAnnotation();
        
        assertEquals(true, annotationMap.addIfNotPresent(annotation));
        assertEquals(false, annotationMap.addIfNotPresent(annotation));
        
        HashMap<Class<? extends Annotation>,Annotation> expectedAnnotations = new HashMap<>();
        expectedAnnotations.put(Test.class, annotation);
        
        assertEquals(expectedAnnotations, annotationMap._annotations);
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        TestAnnotation annotation = new TestAnnotation();
        
        assertEquals(true, annotationMap.add(annotation));
        
        HashMap<Class<? extends Annotation>,Annotation> expectedAnnotations = new HashMap<>();
        expectedAnnotations.put(Test.class, annotation);
        
        assertEquals(expectedAnnotations, annotationMap._annotations);
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        assertEquals("[null]", annotationMap.toString());
        
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        
        annotationMap._annotations = annotations;
        
        assertEquals("{java.lang.annotation.Retention=java.lang.annotation.RetentionAnnotation, @Test=com.fasterxml.jackson.databind.introspect.TestAnnotation}", annotationMap.toString());
    }

    class TestAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return Test.class;
        }
    }
    
    class DeprecatedAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return Deprecated.class;
        }
    }
}