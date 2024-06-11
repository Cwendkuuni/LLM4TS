package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setup() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGetAnnotation() {
        Annotation annotation = annotationMap.get(TestAnnotation.class);
        assertNull(annotation);

        TestAnnotation testAnnotation = new TestAnnotationImpl(10);
        annotationMap.add(testAnnotation);
        Annotation retrievedAnnotation = annotationMap.get(TestAnnotation.class);
        assertEquals(testAnnotation, retrievedAnnotation);
    }

    @Test
    public void testAnnotations() {
        Iterable<Annotation> emptyAnnotations = annotationMap.annotations();
        List<Annotation> emptyAnnotationsList = new ArrayList<>();
        emptyAnnotations.forEach(emptyAnnotationsList::add);
        assertEquals(0, emptyAnnotationsList.size());

        TestAnnotation testAnnotation = new TestAnnotationImpl(10);
        annotationMap.add(testAnnotation);
        Iterable<Annotation> annotations = annotationMap.annotations();
        List<Annotation> annotationsList = new ArrayList<>();
        annotations.forEach(annotationsList::add);
        assertEquals(1, annotationsList.size());
        assertEquals(testAnnotation, annotationsList.get(0));

        AnotherTestAnnotation anotherTestAnnotation = new AnotherTestAnnotationImpl("test");
        annotationMap.add(anotherTestAnnotation);
        Iterable<Annotation> updatedAnnotations = annotationMap.annotations();
        List<Annotation> updatedAnnotationsList = new ArrayList<>();
        updatedAnnotations.forEach(updatedAnnotationsList::add);
        assertEquals(2, updatedAnnotationsList.size());
        assertEquals(testAnnotation, updatedAnnotationsList.get(0));
        assertEquals(anotherTestAnnotation, updatedAnnotationsList.get(1));
    }

    @Test
    public void testMerge() {
        AnnotationMap primaryMap = new AnnotationMap();
        TestAnnotation testAnnotation1 = new TestAnnotationImpl(10);
        primaryMap.add(testAnnotation1);

        AnnotationMap secondaryMap = new AnnotationMap();
        AnotherTestAnnotation anotherTestAnnotation = new AnotherTestAnnotationImpl("test");
        secondaryMap.add(anotherTestAnnotation);

        AnnotationMap mergedMap = AnnotationMap.merge(primaryMap, secondaryMap);
        assertEquals(2, mergedMap.size());
        Annotation retrievedTestAnnotation = mergedMap.get(TestAnnotation.class);
        assertEquals(testAnnotation1, retrievedTestAnnotation);
        Annotation retrievedAnotherTestAnnotation = mergedMap.get(AnotherTestAnnotation.class);
        assertEquals(anotherTestAnnotation, retrievedAnotherTestAnnotation);
    }

    @Test
    public void testSize() {
        assertEquals(0, annotationMap.size());

        TestAnnotation testAnnotation = new TestAnnotationImpl(10);
        annotationMap.add(testAnnotation);
        assertEquals(1, annotationMap.size());

        AnotherTestAnnotation anotherTestAnnotation = new AnotherTestAnnotationImpl("test");
        annotationMap.add(anotherTestAnnotation);
        assertEquals(2, annotationMap.size());

        annotationMap.add(testAnnotation);
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        TestAnnotation testAnnotation = new TestAnnotationImpl(10);
        annotationMap.add(testAnnotation);
        boolean added = annotationMap.addIfNotPresent(testAnnotation);
        assertEquals(false, added);

        AnotherTestAnnotation anotherTestAnnotation = new AnotherTestAnnotationImpl("test");
        boolean addedNew = annotationMap.addIfNotPresent(anotherTestAnnotation);
        assertEquals(true, addedNew);
        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testAdd() {
        TestAnnotation testAnnotation = new TestAnnotationImpl(10);
        boolean added = annotationMap.add(testAnnotation);
        assertEquals(true, added);
        assertEquals(1, annotationMap.size());

        AnotherTestAnnotation anotherTestAnnotation = new AnotherTestAnnotationImpl("test");
        boolean addedNew = annotationMap.add(anotherTestAnnotation);
        assertEquals(true, addedNew);
        assertEquals(2, annotationMap.size());

        boolean addedDuplicate = annotationMap.add(testAnnotation);
        assertEquals(false, addedDuplicate);
        assertEquals(2, annotationMap.size());
    }
}

interface TestAnnotation extends Annotation {
    int value();
}

class TestAnnotationImpl implements TestAnnotation {

    private final int value;

    public TestAnnotationImpl(int value) {
        this.value = value;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return TestAnnotation.class;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + value;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TestAnnotationImpl other = (TestAnnotationImpl) obj;
        if (value != other.value)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TestAnnotationImpl [value=" + value + "]";
    }
}

interface AnotherTestAnnotation extends Annotation {
    String text();
}

class AnotherTestAnnotationImpl implements AnotherTestAnnotation {

    private final String text;

    public AnotherTestAnnotationImpl(String text) {
        this.text = text;
    }

    @Override
    public String text() {
        return text;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return AnotherTestAnnotation.class;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AnotherTestAnnotationImpl other = (AnotherTestAnnotationImpl) obj;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AnotherTestAnnotationImpl [text=" + text + "]";
    }

}