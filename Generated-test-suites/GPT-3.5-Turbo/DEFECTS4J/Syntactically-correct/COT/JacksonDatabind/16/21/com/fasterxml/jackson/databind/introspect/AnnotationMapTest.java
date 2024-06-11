import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationMapTest {
  
  @Test
  public void testGet() {
    AnnotationMap annotationMap = new AnnotationMap();
    assertNull(annotationMap.get(Override.class));
  }
  
  @Test
  public void testAnnotations() {
    AnnotationMap annotationMap = new AnnotationMap();
    Iterable<Annotation> annotations = annotationMap.annotations();
    assertNotNull(annotations);
    assertFalse(annotations.iterator().hasNext());
  }
  
  @Test
  public void testMerge() {
    AnnotationMap primary = new AnnotationMap();
    AnnotationMap secondary = new AnnotationMap();
    AnnotationMap merged = AnnotationMap.merge(primary, secondary);
    assertNotNull(merged);
    assertEquals(0, merged.size());
  }
  
  @Test
  public void testSize() {
    AnnotationMap annotationMap = new AnnotationMap();
    assertEquals(0, annotationMap.size());
  }
  
  @Test
  public void testAddIfNotPresent() {
    AnnotationMap annotationMap = new AnnotationMap();
    Annotation ann = new Annotation() {
      @Override
      public Class<? extends Annotation> annotationType() {
        return null;
      }
    };
    assertTrue(annotationMap.addIfNotPresent(ann));
    assertFalse(annotationMap.addIfNotPresent(ann));
  }
  
  @Test
  public void testAdd() {
    AnnotationMap annotationMap = new AnnotationMap();
    Annotation ann = new Annotation() {
      @Override
      public Class<? extends Annotation> annotationType() {
        return null;
      }
    };
    assertTrue(annotationMap.add(ann));
    assertFalse(annotationMap.add(ann));
  }
  
  @Test
  public void testToString() {
    AnnotationMap annotationMap = new AnnotationMap();
    assertEquals("[null]", annotationMap.toString());
  }
}