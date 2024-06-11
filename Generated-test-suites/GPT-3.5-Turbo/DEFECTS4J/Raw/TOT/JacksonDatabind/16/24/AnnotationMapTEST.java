import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.*;

public class AnnotationMapTest {
  
  // Test case for the get() method
  @Test
  public void testGetMethod() {
    AnnotationMap annotationMap = new AnnotationMap();
    
    // Test scenario 1: When the annotation map is empty
    assertNull(annotationMap.get(SomeAnnotation.class));
    
    // Test scenario 2: When the annotation map contains the specified annotation
    SomeAnnotation someAnnotation = new SomeAnnotationImpl();
    annotationMap.add(someAnnotation);
    
    assertEquals(someAnnotation, annotationMap.get(SomeAnnotation.class));
    
    // Test scenario 3: When the annotation map does not contain the specified annotation but has other annotations
    assertNull(annotationMap.get(SomeOtherAnnotation.class));
    
    // Test scenario 4: When the annotation map has multiple annotations of the same type
    SomeAnnotation otherSomeAnnotation = new SomeAnnotationImpl();
    annotationMap.add(otherSomeAnnotation);
    
    assertEquals(someAnnotation, annotationMap.get(SomeAnnotation.class));
  }
  
  // Test case for the annotations() method
  @Test
  public void testAnnotationsMethod() {
    AnnotationMap annotationMap = new AnnotationMap();
    
    // Test scenario 1: When the annotation map is empty
    assertEquals(Collections.emptyList(), annotationMap.annotations());
    
    // Test scenario 2: When the annotation map contains annotations
    SomeAnnotation someAnnotation = new SomeAnnotationImpl();
    annotationMap.add(someAnnotation);
    
    assertEquals(Collections.singletonList(someAnnotation), annotationMap.annotations());
    
    // Test scenario 3: When the annotation map contains multiple annotations
    SomeAnnotation otherSomeAnnotation = new SomeAnnotationImpl();
    annotationMap.add(otherSomeAnnotation);
    
    assertEquals(Arrays.asList(someAnnotation, otherSomeAnnotation), annotationMap.annotations());
  }
  
  // Test case for the merge() method
  @Test
  public void testMergeMethod() {
    AnnotationMap primary = new AnnotationMap();
    AnnotationMap secondary = new AnnotationMap();
    
    // Test scenario 1: When primary and secondary annotation maps are both empty
    assertEquals(secondary, AnnotationMap.merge(primary, secondary));
    
    // Test scenario 2: When primary annotation map is empty and secondary has annotations
    SomeAnnotation someAnnotation = new SomeAnnotationImpl();
    secondary.add(someAnnotation);
    
    assertEquals(secondary, AnnotationMap.merge(primary, secondary));
    
    // Test scenario 3: When secondary annotation map is empty and primary has annotations
    SomeAnnotation otherSomeAnnotation = new SomeAnnotationImpl();
    primary.add(otherSomeAnnotation);
    
    assertEquals(primary, AnnotationMap.merge(primary, secondary));
    
    // Test scenario 4: When both primary and secondary annotation maps have annotations
    assertEquals(2, AnnotationMap.merge(primary, secondary).size());
  }
  
  // Test case for the size() method
  @Test
  public void testSizeMethod() {
    AnnotationMap annotationMap = new AnnotationMap();
    
    // Test scenario 1: When the annotation map is empty
    assertEquals(0, annotationMap.size());
    
    // Test scenario 2: When the annotation map has annotations
    SomeAnnotation someAnnotation = new SomeAnnotationImpl();
    annotationMap.add(someAnnotation);
    
    assertEquals(1, annotationMap.size());
    
    // Test scenario 3: When the annotation map has multiple annotations
    SomeAnnotation otherSomeAnnotation = new SomeAnnotationImpl();
    annotationMap.add(otherSomeAnnotation);
    
    assertEquals(2, annotationMap.size());
  }
  
  // Test case for the addIfNotPresent() method
  @Test
  public void testAddIfNotPresentMethod() {
    AnnotationMap annotationMap = new AnnotationMap();
    
    // Test scenario 1: When the annotation map is empty
    SomeAnnotation someAnnotation = new SomeAnnotationImpl();
    
    assertTrue(annotationMap.addIfNotPresent(someAnnotation));
    assertFalse(annotationMap.addIfNotPresent(someAnnotation));
    
    // Test scenario 2: When the annotation map already contains the specified annotation
    SomeAnnotation otherSomeAnnotation = new SomeAnnotationImpl();
    
    assertFalse(annotationMap.addIfNotPresent(otherSomeAnnotation));
  }
  
  // Test case for the add() method
  @Test
  public void testAddMethod() {
    AnnotationMap annotationMap = new AnnotationMap();
    
    // Test scenario 1: When the annotation map is empty
    SomeAnnotation someAnnotation = new SomeAnnotationImpl();
    
    assertTrue(annotationMap.add(someAnnotation));
    
    // Test scenario 2: When the annotation map already contains the specified annotation
    SomeAnnotation otherSomeAnnotation = new SomeAnnotationImpl();
    
    assertFalse(annotationMap.add(otherSomeAnnotation));
  }
  
  // Test case for the toString() method
  @Test
  public void testToStringMethod() {
    AnnotationMap annotationMap = new AnnotationMap();
    
    // Test scenario 1: When the annotation map is empty
    assertEquals("[null]", annotationMap.toString());
    
    // Test scenario 2: When the annotation map has annotations
    SomeAnnotation someAnnotation = new SomeAnnotationImpl();
    annotationMap.add(someAnnotation);
    
    assertEquals("{interface SomeAnnotation=SomeAnnotationImpl}", annotationMap.toString());
    
    // Test scenario 3: When the annotation map has multiple annotations
    SomeAnnotation otherSomeAnnotation = new SomeAnnotationImpl();
    annotationMap.add(otherSomeAnnotation);
    
    assertEquals("{interface SomeAnnotation=SomeAnnotationImpl, interface SomeAnnotation=SomeAnnotationImpl}", annotationMap.toString());
  }
  
  @Retention(RetentionPolicy.RUNTIME)
  public @interface SomeAnnotation {
    
  }
  
  public class SomeAnnotationImpl implements SomeAnnotation {
    
  }
  
  @Retention(RetentionPolicy.RUNTIME)
  public @interface SomeOtherAnnotation {
    
  }

}