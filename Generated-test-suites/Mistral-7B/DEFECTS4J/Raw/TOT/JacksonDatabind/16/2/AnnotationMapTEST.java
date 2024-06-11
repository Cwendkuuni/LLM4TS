import org.junit.Assert;
import org.junit.Test;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AnnotationMapTest {

    @Test
    public void testGetAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation);

        Assert.assertEquals(jsonDeserializeAnnotation, annotationMap.get(JsonDeserialize.class));
    }

    @Test
    public void testGetNullAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(JsonSerialize.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation1);
        JsonDeserialize jsonDeserializeAnnotation2 = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation2);

        Set<Annotation> expectedAnnotations = new HashSet<>();
        expectedAnnotations.add(jsonDeserializeAnnotation1);
        expectedAnnotations.add(jsonDeserializeAnnotation2);

        assertEquals(expectedAnnotations, new HashSet<>(annotationMap.annotations()));
    }

    @Test
    public void testEmptyAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(Collections.emptySet(), new HashSet<>(annotationMap.annotations()));
    }

    @Test
    public void testMergeEmptyAnnotationMaps() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        AnnotationMap secondaryAnnotationMap = new AnnotationMap();

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        assertEquals(primaryAnnotationMap, mergedAnnotationMap);
    }

    @Test
    public void testMergeAnnotationMapsWithSameAnnotations() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        primaryAnnotationMap.add(jsonDeserializeAnnotation1);

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        secondaryAnnotationMap.add(jsonDeserializeAnnotation1);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        assertEquals(primaryAnnotationMap, mergedAnnotationMap);
    }

    @Test
    public void testMergeAnnotationMapsWithDifferentAnnotations() {
        AnnotationMap primaryAnnotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        primaryAnnotationMap.add(jsonDeserializeAnnotation1);

        AnnotationMap secondaryAnnotationMap = new AnnotationMap();
        JsonSerialize jsonSerializeAnnotation1 = new JsonSerialize();
        secondaryAnnotationMap.add(jsonSerializeAnnotation1);

        AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
        Set<Annotation> expectedAnnotations = new HashSet<>();
        expectedAnnotations.add(jsonDeserializeAnnotation1);
        assertEquals(expectedAnnotations, new HashSet<>(mergedAnnotationMap.annotations()));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation1);

        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();

        assertTrue(annotationMap.addIfNotPresent(jsonDeserializeAnnotation));
        assertTrue(annotationMap.size() == 1);

        assertFalse(annotationMap.addIfNotPresent(jsonDeserializeAnnotation));
        assertEquals(annotationMap.size(), 1);
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation = new JsonDeserialize();

        assertTrue(annotationMap.add(jsonDeserializeAnnotation));
        assertTrue(annotationMap.size() == 1);

        JsonSerialize jsonSerializeAnnotation = new JsonSerialize();
        assertTrue(annotationMap.add(jsonSerializeAnnotation));
        assertTrue(annotationMap.size() == 2);
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        JsonDeserialize jsonDeserializeAnnotation1 = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation1);

        JsonDeserialize jsonDeserializeAnnotation2 = new JsonDeserialize();
        annotationMap.add(jsonDeserializeAnnotation2);

        String expectedString = "{" +
                "  @com.fasterxml.jackson.databind.annotation.JsonDeserialize(Lcom.fasterxml.jackson.databind.JsonDeserializer;): " +
                "[Ljava.lang.annotation.Annotation;@6d0235b2, @com.fasterxml.jackson.databind.annotation.JsonDeserialize(Lcom.fasterxml.jackson.databind.JsonDeserializer;): " +
                "[Ljava.lang.annotation.Annotation;@10606f0]";

        assertEquals(expectedString, annotationMap.toString());
    }

    @Test
    public void testCustomAnnotations() {
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.METHOD)
        static class MyAnnotation {}

        AnnotationMap annotationMap = new AnnotationMap();
        MyAnnotation myAnnotation = new MyAnnotation();
        annotationMap.add(myAnnotation);

        assertTrue(annotationMap.size() == 1);
        assertTrue(annotationMap.get(MyAnnotation.class) == myAnnotation);
    }
}