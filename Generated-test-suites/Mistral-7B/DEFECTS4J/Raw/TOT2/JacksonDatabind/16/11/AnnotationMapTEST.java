import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class AnnotationMapTest {

    @Test
    public void testAnnotationMapCreation() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNotNull(annotationMap);
    }

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonSerialize jsonSerializeAnnotation = new JsonSerialize();
        annotationMap.add(jsonSerializeAnnotation);
        JsonSerialize annotation = annotationMap.get(JsonSerialize.class);
        assertNotNull(annotation);
        assertEquals(jsonSerializeAnnotation, annotation);
    }

    @Test
    public void testEmptyAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        List<Annotation> annotations = annotationMap.annotations();
        assertNotNull(annotations);
        assertTrue(annotations.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testGetAnnotationNullMap() {
        AnnotationMap annotationMap = null;
        assertNull(annotationMap.get(JsonSerialize.class));
    }

    @Test(expected = NullPointerException.class)
    public void testGetAnnotationNullAnnotationType() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(null));
    }

    @Test(expected = NullPointerException.class)
    public void testAddIfNotPresentNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertFalse(annotationMap.addIfNotPresent(null));
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullMap() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertFalse(annotationMap.addIfNotPresent(new JsonSerialize()));
        AnnotationMap nullAnnotationMap = null;
        assertFalse(annotationMap.add(nullAnnotationMap));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddAnnotationAlreadyPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonSerialize jsonSerializeAnnotation = new JsonSerialize();
        annotationMap.add(jsonSerializeAnnotation);
        assertFalse(annotationMap.add(jsonSerializeAnnotation));
    }

    @Test(expected = NullPointerException.class)
    public void testMergeNullPrimaryMap() {
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        assertNotNull(secondaryAnnotationMap);
        assertFalse(AnnotationMap.merge(null, secondaryAnnotationMap) instanceof AnnotationMap);
    }

    @Test(expected = NullPointerException.class)
    public void testMergeNullSecondaryMap() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        assertNotNull(primaryAnnotationMap);
        assertFalse(AnnotationMap.merge(primaryAnnotationMap, null) instanceof AnnotationMap);
    }

    @Test
    public void testMergeEmptyMaps() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        assertEquals(mergedAnnotationMap, primaryAnnotationMap);
    }

    @Test
    public void testMergeMapsWithSingleAnnotation() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        JsonSerialize jsonSerializeAnnotation1 = new JsonSerialize();
        primaryAnnotationMap.add(jsonSerializeAnnotation1);

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        JsonSerialize jsonSerializeAnnotation2 = new JsonSerialize();
        secondaryAnnotationMap.add(jsonSerializeAnnotation2);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        List<Annotation> annotations = mergedAnnotationMap.annotations();
        assertEquals(annotations.size(), 2);
        for (Annotation annotation : annotations) {
            if (annotation instanceof JsonSerialize) {
                JsonSerialize jsonSerializeAnnotation = (JsonSerialize) annotation;
                assertEquals(jsonSerializeAnnotation1, jsonSerializeAnnotation);
            }
        }
    }

    @Test
    public void testMergeMapsWithMultipleAnnotations() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        JsonSerialize jsonSerializeAnnotation1 = new JsonSerialize();
        primaryAnnotationMap.add(jsonSerializeAnnotation1);

        JsonSerialize jsonSerializeAnnotation2 = new JsonSerialize();
        primaryAnnotationMap.add(jsonSerializeAnnotation2);

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        JsonSerialize jsonSerializeAnnotation3 = new JsonSerialize();
        secondaryAnnotationMap.add(jsonSerializeAnnotation3);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        List<Annotation> annotations = mergedAnnotationMap.annotations();
        assertEquals(annotations.size(), 3);
        for (Annotation annotation : annotations) {
            if (annotation instanceof JsonSerialize) {
                JsonSerialize jsonSerializeAnnotation = (JsonSerialize) annotation;
                if (jsonSerializeAnnotation == jsonSerializeAnnotation1) {
                    continue;
                }
                if (jsonSerializeAnnotation == jsonSerializeAnnotation2) {
                    continue;
                }
                assertEquals(jsonSerializeAnnotation3, annotation);
            }
        }
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(annotationMap.size(), 0);

        JsonSerialize jsonSerializeAnnotation = new JsonSerialize();
        annotationMap.add(jsonSerializeAnnotation);
        assertEquals(annotationMap.size(), 1);
    }

    // Add test cases for remaining methods

}