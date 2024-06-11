import static org.junit.Assert.*;
import org.junit.Test;

public class AnnotationMapTest {
  
  @Test
  public void testGet_NullAnnotations_ReturnsNull() {
    AnnotationMap annotationMap = new AnnotationMap();
    
    assertNull(annotationMap.get(SomeAnnotation.class));
  }
  
  @Test
  public void testGet_ValidAnnotation_ReturnsAnnotation() {
    AnnotationMap annotationMap = new AnnotationMap();
    SomeAnnotation annotation = new SomeAnnotation();
    annotationMap.add(annotation);
    
    assertEquals(annotation, annotationMap.get(SomeAnnotation.class));
  }
  
  @Test
  public void testGet_NonExistingAnnotation_ReturnsNull() {
    AnnotationMap annotationMap = new AnnotationMap();
    SomeAnnotation annotation = new SomeAnnotation();
    annotationMap.add(annotation);
    
    assertNull(annotationMap.get(OtherAnnotation.class));
  }
  
  @Test
  public void testAnnotations_EmptyMap_ReturnsEmptyList() {
    AnnotationMap annotationMap = new AnnotationMap();
    
    assertTrue(annotationMap.annotations().isEmpty());
  }
  
  @Test
  public void testAnnotations_NonEmptyMap_ReturnsListWithAnnotations() {
    AnnotationMap annotationMap = new AnnotationMap();
    SomeAnnotation annotation = new SomeAnnotation();
    annotationMap.add(annotation);
    
    List<Annotation> annotations = new ArrayList<>();
    annotations.add(annotation);
    
    assertEquals(annotations, annotationMap.annotations());
  }
  
  @Test
  public void testSize_EmptyMap_ReturnsZero() {
    AnnotationMap annotationMap = new AnnotationMap();
    
    assertEquals(0, annotationMap.size());
  }
  
  @Test
  public void testSize_NonEmptyMap_ReturnsNumberOfAnnotations() {
    AnnotationMap annotationMap = new AnnotationMap();
    SomeAnnotation annotation1 = new SomeAnnotation();
    SomeAnnotation annotation2 = new SomeAnnotation();
    annotationMap.add(annotation1);
    annotationMap.add(annotation2);
    
    assertEquals(2, annotationMap.size());
  }
  
  @Test
  public void testAddIfNotPresent_AnnotationPresent_ReturnsFalse() {
    AnnotationMap annotationMap = new AnnotationMap();
    SomeAnnotation annotation = new SomeAnnotation();
    annotationMap.add(annotation);
    
    assertFalse(annotationMap.addIfNotPresent(annotation));
  }
  
  @Test
  public void testAddIfNotPresent_AnnotationNotPresent_ReturnsTrue() {
    AnnotationMap annotationMap = new AnnotationMap();
    SomeAnnotation annotation = new SomeAnnotation();
    
    assertTrue(annotationMap.addIfNotPresent(annotation));
  }
  
  @Test
  public void testAdd_WhenAnnotationNotPresent_ReturnsTrue() {
    AnnotationMap annotationMap = new AnnotationMap();
    SomeAnnotation annotation = new SomeAnnotation();
    
    assertTrue(annotationMap.add(annotation));
  }
  
  @Test
  public void testAdd_WhenAnnotationPresent_ReturnsFalse() {
    AnnotationMap annotationMap = new AnnotationMap();
    SomeAnnotation annotation = new SomeAnnotation();
    annotationMap.add(annotation);
    
    assertFalse(annotationMap.add(annotation));
  }
  
  @Test
  public void testMerge_BothMapsEmpty_ReturnsNull() {
    AnnotationMap primary = new AnnotationMap();
    AnnotationMap secondary = new AnnotationMap();
    
    assertNull(AnnotationMap.merge(primary, secondary));
  }
  
  @Test
  public void testMerge_SecondaryMapEmpty_ReturnsPrimaryMap() {
    AnnotationMap primary = new AnnotationMap();
    SomeAnnotation annotation = new SomeAnnotation();
    primary.add(annotation);
    AnnotationMap secondary = new AnnotationMap();
    
    assertSame(primary, AnnotationMap.merge(primary, secondary));
  }
  
  @Test
  public void testMerge_PrimaryMapEmpty_ReturnsSecondaryMap() {
    AnnotationMap primary = new AnnotationMap();
    AnnotationMap secondary = new AnnotationMap();
    SomeAnnotation annotation = new SomeAnnotation();
    secondary.add(annotation);
    
    assertSame(secondary, AnnotationMap.merge(primary, secondary));
  }
  
  @Test
  public void testMerge_BothMapsNonEmpty_ReturnsCombinedMap() {
    AnnotationMap primary = new AnnotationMap();
    SomeAnnotation annotation1 = new SomeAnnotation();
    SomeAnnotation annotation2 = new SomeAnnotation();
    primary.add(annotation1);
    
    AnnotationMap secondary = new AnnotationMap();
    SomeAnnotation annotation3 = new SomeAnnotation();
    secondary.add(annotation2);
    secondary.add(annotation3);
    
    AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
    
    assertEquals(3, mergedMap.size());
    assertEquals(annotation1, mergedMap.get(SomeAnnotation.class));
    assertEquals(annotation2, mergedMap.get(SomeAnnotation.class));
    assertEquals(annotation3, mergedMap.get(SomeAnnotation.class));
  }
  
  @Test
  public void testToString_NullAnnotations_ReturnsNullString() {
    AnnotationMap annotationMap = new AnnotationMap();
    
    assertEquals("[null]", annotationMap.toString());
  }
  
  @Test
  public void testToString_NonEmptyAnnotations_ReturnsStringRepresentation() {
    AnnotationMap annotationMap = new AnnotationMap();
    SomeAnnotation annotation = new SomeAnnotation();
    annotationMap.add(annotation);
    
    assertEquals("{SomeAnnotation=" + annotation.toString() + "}", annotationMap.toString());
  }

  @Test
  public void testAdd_WhenAnnotationIsNull_ReturnsFalse() {
    AnnotationMap annotationMap = new AnnotationMap();
    
    assertFalse(annotationMap.add(null));
  }
}