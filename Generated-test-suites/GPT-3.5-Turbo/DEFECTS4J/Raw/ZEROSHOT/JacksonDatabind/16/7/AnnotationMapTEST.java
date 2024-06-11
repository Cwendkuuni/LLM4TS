package com.fasterxml.jackson.databind.introspect;

import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;

public class AnnotationMapTest {
    
    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertNull(annotationMap.get(Test.class));
        
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        });
        AnnotationMap annotationMapWithAnnotation = new AnnotationMap(annotations);
        Assert.assertNotNull(annotationMapWithAnnotation.get(Test.class));
    }
    
    @Test
    public void testAnnotations() {
        AnnotationMap emptyAnnotationMap = new AnnotationMap();
        Assert.assertEquals(Collections.emptyList(), emptyAnnotationMap.annotations());
        
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        });
        AnnotationMap annotationMapWithAnnotation = new AnnotationMap(annotations);
        Assert.assertEquals(1, annotationMapWithAnnotation.annotations().spliterator().estimateSize());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        Assert.assertSame(primary, merged);
        
        AnnotationMap primaryAnnotations = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> primaryAnnotationsMap = new HashMap<>();
        primaryAnnotationsMap.put(Test.class, new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        });
        primaryAnnotations._annotations = primaryAnnotationsMap;
        
        AnnotationMap secondaryAnnotations = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> secondaryAnnotationsMap = new HashMap<>();
        secondaryAnnotationsMap.put(Test.class, new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        });
        secondaryAnnotations._annotations = secondaryAnnotationsMap;
        
        merged = AnnotationMap.merge(primaryAnnotations, secondaryAnnotations);
        Assert.assertSame(secondaryAnnotations, merged);
    }
    
    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals(0, annotationMap.size());
        
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        });
        AnnotationMap annotationMapWithAnnotation = new AnnotationMap(annotations);
        Assert.assertEquals(1, annotationMapWithAnnotation.size());
    }
    
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };
        Assert.assertTrue(annotationMap.addIfNotPresent(testAnnotation));
        Assert.assertFalse(annotationMap.addIfNotPresent(testAnnotation));
    }
    
    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        TestAnnotation testAnnotation = new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };
        Assert.assertTrue(annotationMap.add(testAnnotation));
    }
    
    @Override
    public String toString() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals("[null]", annotationMap.toString());
        
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        });
        AnnotationMap annotationMapWithAnnotation = new AnnotationMap(annotations);
        Assert.assertNotNull(annotationMapWithAnnotation.toString());
    }
}