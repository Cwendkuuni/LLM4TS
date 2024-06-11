
import static org.junit.Assert.*;
import org.junit.*;

import java.lang.annotation.*;
import java.util.*;

public class AnnotationMapTest {
  
  @Test
  public void testGet_WhenNoAnnotations_ShouldReturnNull() {
    AnnotationMap annotationMap = new AnnotationMap();
    
    Annotation result = annotationMap.get(SomeAnnotation.class);
    
    assertNull(result);
  }
  
  @Test
  public void testGet_WhenAnnotationExists_ShouldReturnAnnotation() {
    AnnotationMap annotationMap = new AnnotationMap();
    SomeAnnotation expected = new SomeAnnotationImpl();
    annotationMap._annotations = new HashMap<Class<? extends Annotation>, Annotation>();
    annotationMap._annotations.put(SomeAnnotation.class, expected);
    
    Annotation result = annotationMap.get(SomeAnnotation.class);
    
    assertEquals(expected, result);
  }
  
  @Test
  public void testAnnotations_WhenNoAnnotations_ShouldReturnEmptyList() {
    AnnotationMap annotationMap = new AnnotationMap();
    
    Iterable<Annotation> result = annotationMap.annotations();
    List<Annotation> resultList = new ArrayList<>();
    result.forEach(resultList::add);
    
    assertEquals(0, resultList.size());
  }
  
  @Test
  public void testAnnotations_WhenAnnotationsExist_ShouldReturnAnnotations() {
    AnnotationMap annotationMap = new AnnotationMap();
    SomeAnnotation annotation1 = new SomeAnnotationImpl();
    SomeAnnotation annotation2 = new SomeAnnotationImpl();
    annotationMap._annotations = new HashMap<Class<? extends Annotation>, Annotation>();
    annotationMap._annotations.put(SomeAnnotation.class, annotation1);
    annotationMap._annotations.put(SomeOtherAnnotation.class, annotation2);
    
    Iterable<Annotation> result = annotationMap.annotations();
    List<Annotation> resultList = new ArrayList<>();
    result.forEach(resultList::add);
    
    assertEquals(2, resultList.size());
    assertTrue(resultList.contains(annotation1));
    assertTrue(resultList.contains(annotation2));
  }
  
  @Test
  public void testMerge_WhenPrimaryIsNull_ShouldReturnSecondary() {
    AnnotationMap primary = null;
    AnnotationMap secondary = new AnnotationMap();
    
    AnnotationMap result = AnnotationMap.merge(primary, secondary);
    
    assertEquals(secondary, result);
  }
  
  @Test
  public void testMerge_WhenPrimaryIsEmpty_ShouldReturnSecondary() {
    AnnotationMap primary = new AnnotationMap();
    AnnotationMap secondary = new AnnotationMap();
    
    AnnotationMap result = AnnotationMap.merge(primary, secondary);
    
    assertEquals(secondary, result);
  }
  
  @Test
  public void testMerge_WhenSecondaryIsNull_ShouldReturnPrimary() {
    AnnotationMap primary = new AnnotationMap();
    AnnotationMap secondary = null;
    
    AnnotationMap result = AnnotationMap.merge(primary, secondary);
    
    assertEquals(primary, result);
  }
  
  @Test
  public void testMerge_WhenSecondaryIsEmpty_ShouldReturnPrimary() {
    AnnotationMap primary = new AnnotationMap();
    AnnotationMap secondary = new AnnotationMap();
    
    AnnotationMap result = AnnotationMap.merge(primary, secondary);
    
    assertEquals(primary, result);
  }
  
  @Test
  public void testMerge_WhenBothMapsAreNonEmpty_ShouldReturnMergedMap() {
    AnnotationMap primary = new AnnotationMap();
    primary._annotations = new HashMap<Class<? extends Annotation>, Annotation>();
    primary._annotations.put(SomeAnnotation.class, new SomeAnnotationImpl());
    
    AnnotationMap secondary = new AnnotationMap();
    secondary._annotations = new HashMap<Class<? extends Annotation>, Annotation>();
    secondary._annotations.put(SomeOtherAnnotation.class, new SomeOtherAnnotationImpl());
    
    AnnotationMap result = AnnotationMap.merge(primary, secondary);
    
    assertEquals(2, result.size());
    assertTrue(result._annotations.containsKey(SomeAnnotation.class));
    assertTrue(result._annotations.containsKey(SomeOtherAnnotation.class));
  }
  
  @Test
  public void testSize_WhenAnnotationsIsNull_ShouldReturn0() {
    AnnotationMap annotationMap = new AnnotationMap();
    
    int result = annotationMap.size();
    
    assertEquals(0, result);
  }
  
  @Test
  public void testSize_WhenAnnotationsIsNotEmpty_ShouldReturnSize() {
    AnnotationMap annotationMap = new AnnotationMap();
    annotationMap._annotations = new HashMap<Class<? extends Annotation>, Annotation>();
    annotationMap._annotations.put(SomeAnnotation.class, new SomeAnnotationImpl());
    annotationMap._annotations.put(SomeOtherAnnotation.class, new SomeOtherAnnotationImpl());
    
    int result = annotationMap.size();
    
    assertEquals(2, result);
  }
  
  @Test
  public void testAddIfNotPresent_WhenAnnotationsIsNullAndAnnotationNotPresent_ShouldAddAnnotationAndReturnTrue() {
    AnnotationMap annotationMap = new AnnotationMap();
    SomeAnnotation annotation = new SomeAnnotationImpl();
    
    boolean result = annotationMap.addIfNotPresent(annotation);
    
    assertNotNull(annotationMap._annotations);
    assertTrue(annotationMap._annotations.containsKey(SomeAnnotation.class));
    assertTrue(result);
  }
  
  @Test
  public void testAddIfNotPresent_WhenAnnotationsIsNullAndAnnotationPresent_ShouldNotAddAnnotationAndReturnFalse() {
    AnnotationMap annotationMap = new AnnotationMap();
    SomeAnnotation existingAnnotation = new SomeAnnotationImpl();
    annotationMap._annotations = new HashMap<Class<? extends Annotation>, Annotation>();
    annotationMap._annotations.put(SomeAnnotation.class, existingAnnotation);
    SomeAnnotation newAnnotation = new SomeAnnotationImpl();
    
    boolean result = annotationMap.addIfNotPresent(newAnnotation);
    
    assertEquals(existingAnnotation, annotationMap._annotations.get(SomeAnnotation.class));
    assertFalse(result);
  }
  
  @Test
  public void testAddIfNotPresent_WhenAnnotationsIsNotEmptyAndAnnotationNotPresent_ShouldAddAnnotationAndReturnTrue() {
    AnnotationMap annotationMap = new AnnotationMap();
    SomeAnnotation annotation1 = new SomeAnnotationImpl();
    annotationMap._annotations = new HashMap<Class<? extends Annotation>, Annotation>();
    annotationMap._annotations.put(SomeAnnotation.class, annotation1);
    SomeAnnotation annotation2 = new SomeAnnotationImpl();
    
    boolean result = annotationMap.addIfNotPresent(annotation2);
    
    assertEquals(2, annotationMap.size());
    assertTrue(annotationMap._annotations.containsKey(SomeAnnotation.class));
    assertTrue(annotationMap._annotations.containsKey(SomeAnnotation.class));
    assertTrue(result);
  }
  
  @Test
  public void testAdd_WhenAnnotationsIsNull_ShouldAddAnnotationAndUpdateAnnotationsAndReturnTrue() {
    AnnotationMap annotationMap = new AnnotationMap();
    SomeAnnotation annotation = new SomeAnnotationImpl();
    
    boolean result = annotationMap.add(annotation);
    
    assertNotNull(annotationMap._annotations);
    assertTrue(annotationMap._annotations.containsKey(SomeAnnotation.class));
    assertTrue(result);
  }
  
  @Test
  public void testAdd_WhenAnnotationsIsNotEmptyAndAnnotationNotPresent_ShouldAddAnnotationAndUpdateAnnotationsAndReturnTrue() {
    AnnotationMap annotationMap = new AnnotationMap();
    SomeAnnotation annotation1 = new SomeAnnotationImpl();
    annotationMap._annotations = new HashMap<Class<? extends Annotation>, Annotation>();
    annotationMap._annotations.put(SomeAnnotation.class, annotation1);
    SomeAnnotation annotation2 = new SomeAnnotationImpl();
    
    boolean result = annotationMap.add(annotation2);
    
    assertEquals(2, annotationMap.size());
    assertTrue(annotationMap._annotations.containsKey(SomeAnnotation.class));
    assertTrue(annotationMap._annotations.containsKey(SomeAnnotation.class));
    assertTrue(result);
  }
  
  @Test
  public void testAdd_WhenAnnotationsIsNotEmptyAndAnnotationPresent_ShouldNotAddAnnotationAndReturnFalse() {
    AnnotationMap annotationMap = new AnnotationMap();
    SomeAnnotation existingAnnotation = new SomeAnnotationImpl();
    annotationMap._annotations = new HashMap<Class<? extends Annotation>, Annotation>();
    annotationMap._annotations.put(SomeAnnotation.class, existingAnnotation);
    SomeAnnotation newAnnotation = new SomeAnnotationImpl();
    
    boolean result = annotationMap.add(newAnnotation);
    
    assertEquals(existingAnnotation, annotationMap._annotations.get(SomeAnnotation.class));
    assertFalse(result);
  }
  
  @Test
  public void testToString_WhenAnnotationsIsNull_ShouldReturnNullString() {
    AnnotationMap annotationMap = new AnnotationMap();
    
    String result = annotationMap.toString();
    
    assertEquals("[null]", result);
  }
  
  @Test
  public void testToString_WhenAnnotationsIsNotNull_ShouldReturnAnnotationsToString() {
    AnnotationMap annotationMap = new AnnotationMap();
    SomeAnnotation annotation1 = new SomeAnnotationImpl();
    SomeOtherAnnotation annotation2 = new SomeOtherAnnotationImpl();
    annotationMap._annotations = new HashMap<Class<? extends Annotation>, Annotation>();
    annotationMap._annotations.put(SomeAnnotation.class, annotation1);
    annotationMap._annotations.put(SomeOtherAnnotation.class, annotation2);
    
    String result = annotationMap.toString();
    
    assertEquals(annotationMap._annotations.toString(), result);
  }
}

interface SomeAnnotation extends Annotation {
}

interface SomeOtherAnnotation extends Annotation {
}

class SomeAnnotationImpl implements SomeAnnotation {
}

class SomeOtherAnnotationImpl implements SomeOtherAnnotation {
}
