package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Test.class));
        
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        
        TestAnnotation testAnnotation = annotationMap.get(Test.class);
        assertTrue(testAnnotation instanceof TestAnnotation);
    }
    
    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        
        assertEquals(1, annotationMap.size());
    }
    
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        assertTrue(annotationMap.addIfNotPresent(testAnnotation));

        assertTrue(!annotationMap.addIfNotPresent(testAnnotation));
    }
    
    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation();
        assertTrue(annotationMap.add(testAnnotation));

        assertTrue(!annotationMap.add(testAnnotation));
    }
    
    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        
        assertEquals("{@org.junit.Test=com.fasterxml.jackson.databind.introspect.AnnotationMapTest$TestAnnotation}", annotationMap.toString());
    }
    
    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(Collections.emptyList(), annotationMap.annotations());

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        
        List<Annotation> expectedAnnotations = new ArrayList<>();
        expectedAnnotations.add(new TestAnnotation());
        
        assertEquals(expectedAnnotations, annotationMap.annotations());
    }
    
    private static class TestAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return Test.class;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return 0;
        }
    }
}