package com.example.annotationmap;

import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonSerialize annotation = new JsonSerialize();
        annotationMap.add(annotation);
        JsonSerialize expectedAnnotation = annotationMap.get(JsonSerialize.class);
        assertEquals(annotation, expectedAnnotation);
    }

    @Test
    public void testGetNullAnnotationMap() {
        assertNull(new AnnotationMap().get(JsonSerialize.class));
    }

    @Test
    public void testGetEmptyAnnotationMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEmpty(annotationMap.annotations());
        assertFalse(annotationMap.size() > 0);
    }

    @Test
    public void testGetDuplicateAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonSerialize ann1 = new JsonSerialize();
        JsonSerialize ann2 = new JsonSerialize();
        annotationMap.add(ann1);
        annotationMap.add(ann2);
        assertSame(ann1, annotationMap.get(JsonSerialize.class));
    }

    @Test
    public void testGetDifferentAnnotationTypes() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonSerialize jsonSerialize = new JsonSerialize();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(jsonSerialize);
        annotationMap.add(myAnnotation);
        assertNotSame(jsonSerialize, annotationMap.get(MyAnnotation.class));
        assertNotSame(myAnnotation, annotationMap.get(JsonSerialize.class));
    }

    @Test
    public void testGetAnnotationHashMapCapacity() {
        int capacity = 1000;
        AnnotationMap annotationMap = new AnnotationMap();
        for (int i = 0; i < capacity; i++) {
            JsonSerialize jsonSerialize = new JsonSerialize();
            annotationMap.add(jsonSerialize);
        }
        assertEquals(capacity, annotationMap.size());
    }
}