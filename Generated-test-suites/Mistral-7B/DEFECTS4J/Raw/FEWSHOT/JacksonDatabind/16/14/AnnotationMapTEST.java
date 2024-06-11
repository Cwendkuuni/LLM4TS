package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.lang.annotation.*;
import java.util.*;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();

        // test size
        assertEquals(0, annotationMap.size());

        // test get
        assertNull(annotationMap.get(NullAnnotation.class));

        // test addIfNotPresent and add
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.addIfNotPresent(myAnnotation);
        assertFalse(annotationMap.size() > 1);
        assertEquals(myAnnotation, annotationMap.get(MyAnnotation.class));

        annotationMap.add(myAnnotation);
        assertEquals(myAnnotation, annotationMap.get(MyAnnotation.class));

        // test annotations
        annotationMap.add(new NullAnnotation());
        annotationMap.add(new OtherAnnotation());
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        assertEquals(2, annotations.size());
        assertNotNull(annotations.get(0));
        assertEquals(annotations.get(0).annotationType(), NullAnnotation.class);
        assertNotNull(annotations.get(1));
        assertEquals(annotations.get(1).annotationType(), OtherAnnotation.class);

        // test merge
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap, new AnnotationMap());
        assertEquals(annotationMap.size(), mergedAnnotationMap.size());

        mergedAnnotationMap = AnnotationMap.merge(new AnnotationMap(), annotationMap);
        assertEquals(annotationMap.size(), mergedAnnotationMap.size());

        mergedAnnotationMap = AnnotationMap.merge(annotationMap, new AnnotationMap(Arrays.asList(new NullAnnotation(), new OtherAnnotation())));
        assertEquals(annotationMap.size() + 2, mergedAnnotationMap.size());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface NullAnnotation {}

    @Retention(RetentionPolicy.RUNTIME)
    @interface MyAnnotation {}

    @Retention(RetentionPolicy.RUNTIME)
    @interface OtherAnnotation {}
}