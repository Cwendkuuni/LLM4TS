import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationMapTest {
  
  @Test
  public void testGet() {
    AnnotationMap am = new AnnotationMap();
    assertNull(am.get(TestAnnotation.class));
    
    TestAnnotation annotation = new TestAnnotation();
    am.add(annotation);
    assertEquals(annotation, am.get(TestAnnotation.class));
  }
  
  @Test
  public void testAnnotations() {
    AnnotationMap am = new AnnotationMap();
    assertEquals(Collections.emptyList(), am.annotations());
    
    TestAnnotation annotation = new TestAnnotation();
    am.add(annotation);
    Iterable<Annotation> annotations = am.annotations();
    assertTrue(annotations.iterator().hasNext());
    assertEquals(annotation, annotations.iterator().next());
  }
  
  @Test
  public void testMerge() {
    AnnotationMap primary = new AnnotationMap();
    AnnotationMap secondary = new AnnotationMap();
    
    TestAnnotation annotation1 = new TestAnnotation();
    primary.add(annotation1);
    
    TestAnnotation annotation2 = new TestAnnotation();
    secondary.add(annotation2);
    
    AnnotationMap merged = AnnotationMap.merge(primary, secondary);
    assertEquals(2, merged.size());
    assertEquals(annotation2, merged.get(TestAnnotation.class));
  }
  
  @Test
  public void testAddIfNotPresent() {
    AnnotationMap am = new AnnotationMap();
    TestAnnotation annotation = new TestAnnotation();
    assertTrue(am.addIfNotPresent(annotation));
    assertFalse(am.addIfNotPresent(annotation));
  }
  
  @Test
  public void testAdd() {
    AnnotationMap am = new AnnotationMap();
    TestAnnotation annotation = new TestAnnotation();
    assertTrue(am.add(annotation));
    assertFalse(am.add(annotation));
  }
  
  @Test
  public void testToString() {
    AnnotationMap am = new AnnotationMap();
    assertEquals("[null]", am.toString());
    
    TestAnnotation annotation = new TestAnnotation();
    am.add(annotation);
    assertEquals("{class TestAnnotation=TestAnnotation}", am.toString());
  }
}