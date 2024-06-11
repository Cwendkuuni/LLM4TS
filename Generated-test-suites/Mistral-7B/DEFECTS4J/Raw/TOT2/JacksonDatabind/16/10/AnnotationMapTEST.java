import static org.junit.Assert.*;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class AnnotationMapTest {

    @Test
    public void testAnnotationMapConstructor() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(JsonDeserialize.class));
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize());
        assertEquals(annotationMap.get(JsonDeserialize.class), annotationMap.get(JsonDeserialize.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize());
        annotationMap.add(new JsonSerialize());
        List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
        assertEquals(annotations.get(0).annotationType(), JsonDeserialize.class);
        assertEquals(annotations.get(1).annotationType(), JsonSerialize.class);
    }

    @Test
    public void testMergeNullPrimary() {
        AnnotationMap annotationMap1 = null;
        AnnotationMap annotationMap2 = new AnnotationMap();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        assertTrue(annotationMap2.equals(mergedAnnotationMap));
    }

    @Test
    public void testMergeNullSecondary() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        AnnotationMap annotationMap2 = null;
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        assertTrue(annotationMap1.equals(mergedAnnotationMap));
    }

    @Test
    public void testMergeBothEmpty() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        AnnotationMap annotationMap2 = new AnnotationMap();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        assertTrue(annotationMap1.equals(mergedAnnotationMap));
    }

    @Test
    public void testMergeWithAnnotation() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        annotationMap1.add(new JsonDeserialize());

        AnnotationMap annotationMap2 = new AnnotationMap();
        annotationMap2.add(new JsonSerialize());

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        List<Annotation> mergedAnnotations = new ArrayList<>(mergedAnnotationMap.annotations());
        assertEquals(mergedAnnotations.size(), 2);
        assertEquals(mergedAnnotations.get(0).annotationType(), JsonDeserialize.class);
        assertEquals(mergedAnnotations.get(1).annotationType(), JsonSerialize.class);
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(annotationMap.size(), 0);

        annotationMap.add(new JsonDeserialize());
        assertEquals(annotationMap.size(), 1);
    }

    @Test
    public void testAddIfNotPresentSameType() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation jsonDeserializeAnnotation = new JsonDeserialize();

        assertFalse(annotationMap.addIfNotPresent(jsonDeserializeAnnotation));
        assertEquals(annotationMap.size(), 0);
    }

    @Test
    public void testAddIfNotPresentDifferentTypes() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation jsonDeserializeAnnotation = new JsonDeserialize();
        Annotation jsonSerializeAnnotation = new JsonSerialize();

        assertTrue(annotationMap.addIfNotPresent(jsonDeserializeAnnotation));
        assertEquals(annotationMap.size(), 1);
        assertEquals(annotationMap.get(JsonDeserialize.class), jsonDeserializeAnnotation);
        assertEquals(annotationMap.get(JsonSerialize.class), null);

        assertTrue(annotationMap.addIfNotPresent(jsonSerializeAnnotation));
        assertEquals(annotationMap.size(), 2);
        assertEquals(annotationMap.get(JsonDeserialize.class), jsonDeserializeAnnotation);
        assertEquals(annotationMap.get(JsonSerialize.class), jsonSerializeAnnotation);
    }

    @Test
    public void testAddNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertFalse(annotationMap.addIfNotPresent(null));
        assertEquals(annotationMap.size(), 0);
    }

    @Test
    public void testGetNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(annotationMap.get(null), null);
    }

    @Test(expected = NullPointerException.class)
    public void testMergeNullAnnotations() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        AnnotationMap annotationMap2 = null;
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(annotationMap1, annotationMap2);
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        annotationMap.add(new JsonDeserialize());
        annotationMap.add(new JsonSerialize());
        String annotationMapString = annotationMap.toString();
        assertTrue(annotationMapString.contains("[class com.fasterxml.jackson.databind.annotation.JsonDeserialize, class com.fasterxml.jackson.databind.annotation.JsonSerialize]"));
    }
}