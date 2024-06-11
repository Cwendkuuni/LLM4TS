import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Collections;

public class AnnotationMapTest {
  
  @Test
  public void testGet_NullAnnotations_ReturnNull() {
    AnnotationMap annotationMap = new AnnotationMap();
    assertNull(annotationMap.get(Annotation.class));
  }
  
  @Test
  public void testGet_AvailableAnnotation_ReturnAnnotation() {
    AnnotationMap annotationMap = new AnnotationMap();
    Annotation ann = new Annotation() {};
    annotationMap.add(ann);
    assertEquals(ann, annotationMap.get(Annotation.class));
  }
  
  @Test
  public void testAnnotations_NullAnnotations_ReturnEmptyList() {
    AnnotationMap annotationMap = new AnnotationMap();
    assertEquals(Collections.emptyList(), annotationMap.annotations());
  }
  
  @Test
  public void testAnnotations_AvailableAnnotations_ReturnListWithAnnotations() {
    AnnotationMap annotationMap = new AnnotationMap();
    Annotation ann1 = new Annotation() {};
    Annotation ann2 = new Annotation() {};
    annotationMap.add(ann1);
    annotationMap.add(ann2);
    assertEquals(Arrays.asList(ann1, ann2), annotationMap.annotations());
  }
  
  @Test
  public void testMerge_NullPrimaryAndSecondary_ReturnNull() {
    AnnotationMap primary = null;
    AnnotationMap secondary = null;
    assertNull(AnnotationMap.merge(primary, secondary));
  }
  
  @Test
  public void testMerge_NullPrimary_ReturnSecondary() {
    AnnotationMap primary = null;
    AnnotationMap secondary = new AnnotationMap();
    Annotation ann = new Annotation() {};
    secondary.add(ann);
    assertEquals(secondary, AnnotationMap.merge(primary, secondary));
  }
  
  @Test
  public void testMerge_NullSecondary_ReturnPrimary() {
    AnnotationMap primary = new AnnotationMap();
    AnnotationMap secondary = null;
    Annotation ann = new Annotation() {};
    primary.add(ann);
    assertEquals(primary, AnnotationMap.merge(primary, secondary));
  }
  
  @Test
  public void testMerge_AvailablePrimaryAndSecondary_MergeAndReturnNewAnnotationMap() {
    AnnotationMap primary = new AnnotationMap();
    AnnotationMap secondary = new AnnotationMap();
    Annotation ann1 = new Annotation() {};
    Annotation ann2 = new Annotation() {};
    primary.add(ann1);
    secondary.add(ann2);
    AnnotationMap result = AnnotationMap.merge(primary, secondary);
    assertEquals(Arrays.asList(ann2, ann1), result.annotations());
  }
  
  @Test
  public void testSize_NullAnnotations_ReturnZero() {
    AnnotationMap annotationMap = new AnnotationMap();
    assertEquals(0, annotationMap.size());
  }
  
  @Test
  public void testSize_AvailableAnnotations_ReturnSize() {
    AnnotationMap annotationMap = new AnnotationMap();
    Annotation ann1 = new Annotation() {};
    Annotation ann2 = new Annotation() {};
    annotationMap.add(ann1);
    annotationMap.add(ann2);
    assertEquals(2, annotationMap.size());
  }
  
  @Test
  public void testAddIfNotPresent_NullAnnotations_ReturnTrueAndAddAnnotation() {
    AnnotationMap annotationMap = new AnnotationMap();
    Annotation ann = new Annotation() {};
    assertTrue(annotationMap.addIfNotPresent(ann));
    assertEquals(ann, annotationMap.get(Annotation.class));
  }
  
  @Test
  public void testAddIfNotPresent_AnnotationAlreadyPresent_ReturnFalseAndDoNotAddAnnotation() {
    AnnotationMap annotationMap = new AnnotationMap();
    Annotation ann1 = new Annotation() {};
    Annotation ann2 = new Annotation() {};
    annotationMap.add(ann1);
    assertFalse(annotationMap.addIfNotPresent(ann2));
    assertEquals(ann1, annotationMap.get(Annotation.class));
  }
  
  @Test
  public void testAdd_True_ReturnTrueAndAddAnnotation() {
    AnnotationMap annotationMap = new AnnotationMap();
    Annotation ann = new Annotation() {};
    assertTrue(annotationMap.add(ann));
    assertEquals(ann, annotationMap.get(Annotation.class));
  }
  
  @Test
  public void testAdd_False_ReturnFalseAndDoNotAddAnnotation() {
    AnnotationMap annotationMap = new AnnotationMap();
    Annotation ann1 = new Annotation() {};
    Annotation ann2 = new Annotation() {};
    annotationMap.add(ann1);
    assertFalse(annotationMap.add(ann2));
    assertEquals(ann1, annotationMap.get(Annotation.class));
  }
  
  @Test
  public void testToString_NullAnnotations_ReturnStringWithNull() {
    AnnotationMap annotationMap = new AnnotationMap();
    assertEquals("[null]", annotationMap.toString());
  }
  
  @Test
  public void testToString_AvailableAnnotations_ReturnStringWithAnnotations() {
    AnnotationMap annotationMap = new AnnotationMap();
    Annotation ann1 = new Annotation() {};
    Annotation ann2 = new Annotation() {};
    annotationMap.add(ann1);
    annotationMap.add(ann2);
    assertEquals("[class AnnotationMapTest$1, class AnnotationMapTest$1]", annotationMap.toString());
  }
}