import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonPropertyOrder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class AnnotationMapTest {

 @Test
 public void testGetAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap();
 MyAnnotation myAnnotation = new MyAnnotation();
 annotationMap.add(myAnnotation);

 Class<MyAnnotation> myAnnotationClass = MyAnnotation.class;
 MyAnnotation annotation = annotationMap.get(myAnnotationClass);
 assertEquals(myAnnotation, annotation);
 }

 @Test
 public void testGetNullAnnotation() {
 AnnotationMap annotationMap = new AnnotationMap();
 assertNull(annotationMap.get(String.class));
 }

 @Test
 public void testAnnotations() {
 AnnotationMap annotationMap = new AnnotationMap();
 MyAnnotation myAnnotation1 = new MyAnnotation();
 MyAnnotation myAnnotation2 = new MyAnnotation();
 annotationMap.add(myAnnotation1);
 annotationMap.add(myAnnotation2);

 List<Annotation> annotations = new ArrayList<>(annotationMap.annotations());
 assertEquals(2, annotations.size());
 assertSame(myAnnotation1, annotations.get(0));
 assertSame(myAnnotation2, annotations.get(1));
 }

 @Test
 public void testEmptyAnnotations() {
 AnnotationMap emptyAnnotationMap = new AnnotationMap();
 assertTrue(emptyAnnotationMap.annotations().isEmpty());
 }

 @Test
 public void testMergePrimaryAndNullSecondary() {
 AnnotationMap primaryAnnotationMap = new AnnotationMap();
 MyAnnotation primaryAnnotation1 = new MyAnnotation();
 MyAnnotation primaryAnnotation2 = new MyAnnotation();
 primaryAnnotationMap.add(primaryAnnotation1);
 primaryAnnotationMap.add(primaryAnnotation2);

 AnnotationMap secondaryAnnotationMap = null;
 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertEquals(primaryAnnotationMap, mergedAnnotationMap);
 }

 @Test
 public void testMergePrimaryAndNonEmptySecondary() {
 AnnotationMap primaryAnnotationMap = new AnnotationMap();
 MyAnnotation primaryAnnotation1 = new MyAnnotation();
 MyAnnotation primaryAnnotation2 = new MyAnnotation();
 primaryAnnotationMap.add(primaryAnnotation1);
 primaryAnnotationMap.add(primaryAnnotation2);

 AnnotationMap secondaryAnnotationMap = new AnnotationMap();
 MyAnnotation secondaryAnnotation1 = new MyAnnotation();
 MyAnnotation secondaryAnnotation2 = new MyAnnotation();
 secondaryAnnotationMap.add(secondaryAnnotation1);
 secondaryAnnotationMap.add(secondaryAnnotation2);

 AnnotationMap mergedAnnotationMap = AnnotationMap.merge(primaryAnnotationMap, secondaryAnnotationMap);
 assertEquals(5, mergedAnnotationMap.size());

 Map<Class<? extends Annotation>, Annotation> mergedMap = mergedAnnotationMap._annotations;
 Set<Class<? extends Annotation>> primaryKeys = new HashSet<>(primaryAnnotationMap._annotations.keySet());
 Set<Class<? extends Annotation>> secondaryKeys = new HashSet<>(secondaryAnnotationMap._annotations.keySet());
 primaryKeys.removeAll(secondaryKeys);

 for (Class<? extends Annotation> primaryKey : primaryKeys) {
 Annotation primaryAnnotation = primaryAnnotationMap.get(primaryKey);
 Annotation mergedAnnotation = mergedAnnotationMap.get(primaryKey);
 assertEquals(primaryAnnotation, mergedAnnotation);
 }

 for (Class<? extends Annotation> secondaryKey : secondaryKeys) {
 Annotation secondaryAnnotation = secondaryAnnotationMap.get(secondaryKey);
 Annotation mergedAnnotation = mergedAnnotationMap.get(secondaryKey);
 assertNotSame(secondaryAnnotation, mergedAnnotation);
 assertEquals(secondaryAnnotation.annotationType(), mergedAnnotation.annotationType());
 }
 }
}

class MyAnnotation {
 @JsonPropertyOrder(value = {"name", "age"})
 @JsonDeserialize
 static class Builder {
 @JsonPOJOBuilder(withPrefix = "")
 String name;
 int age;
 }

 String name;
 int age;

 public MyAnnotation() {
 }

 public MyAnnotation(Builder builder) {
 this.name = builder.name;
 this.age = builder.age;
 }
}