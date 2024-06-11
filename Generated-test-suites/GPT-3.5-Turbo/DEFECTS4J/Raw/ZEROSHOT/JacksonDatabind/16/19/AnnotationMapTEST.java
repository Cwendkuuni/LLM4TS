package com.fasterxml.jackson.databind.introspect;

import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.*;

import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(MyAnnotation.class));
        
        MyAnnotation myAnnotation = new MyAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return MyAnnotation.class;
            }
        };
        
        annotationMap.add(myAnnotation);
        
        assertEquals(myAnnotation, annotationMap.get(MyAnnotation.class));
    }
    
    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        Iterable<Annotation> annotations = annotationMap.annotations();
        
        assertFalse(annotations.iterator().hasNext());
        
        MyAnnotation myAnnotation = new MyAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return MyAnnotation.class;
            }
        };
        
        annotationMap.add(myAnnotation);
        
        annotations = annotationMap.annotations();
        assertTrue(annotations.iterator().hasNext());
        assertEquals(myAnnotation, annotations.iterator().next());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        assertNull(AnnotationMap.merge(primary, secondary));
        
        MyAnnotation myAnnotation1 = new MyAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return MyAnnotation.class;
            }
        };
        
        MyAnnotation myAnnotation2 = new MyAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return MyAnnotation.class;
            }
        };
        
        primary.add(myAnnotation1);
        secondary.add(myAnnotation2);
        
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(myAnnotation2, merged.get(MyAnnotation.class));
    }
    
    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
        
        MyAnnotation myAnnotation = new MyAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return MyAnnotation.class;
            }
        };
        
        annotationMap.add(myAnnotation);
        
        assertEquals(1, annotationMap.size());
    }
    
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        MyAnnotation myAnnotation = new MyAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return MyAnnotation.class;
            }
        };
        
        assertTrue(annotationMap.addIfNotPresent(myAnnotation));
        assertFalse(annotationMap.addIfNotPresent(myAnnotation));
    }
    
    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        MyAnnotation myAnnotation = new MyAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return MyAnnotation.class;
            }
        };
        
        assertTrue(annotationMap.add(myAnnotation));
        assertFalse(annotationMap.add(myAnnotation));
    }
    
    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());
        
        MyAnnotation myAnnotation = new MyAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return MyAnnotation.class;
            }
        };
        
        annotationMap.add(myAnnotation);
        
        assertEquals("{interface com.fasterxml.jackson.databind.introspect.MyAnnotation=com.fasterxml.jackson.databind.introspect.AnnotationMapTest$1}", annotationMap.toString());
    }
}

interface MyAnnotation extends Annotation {
}