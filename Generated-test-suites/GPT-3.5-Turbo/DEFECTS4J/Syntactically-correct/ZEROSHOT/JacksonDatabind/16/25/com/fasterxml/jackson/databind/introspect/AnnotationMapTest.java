import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class AnnotationMapTest {

  private AnnotationMap annotationMap;

  @Before
  public void setUp() {
    annotationMap = new AnnotationMap();
  }

  @Test
  public void testGetWithNullAnnotations() {
    assertNull(annotationMap.get(TestAnnotation.class));
  }

  @Test
  public void testGetWithNonNullAnnotations() {
    TestAnnotation testAnnotation = new TestAnnotation();
    annotationMap.add(testAnnotation);
    assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));
  }

  @Test
  public void testAnnotationsWithEmptyAnnotations() {
    assertEquals(Collections.emptyList(), annotationMap.annotations());
  }

  @Test
  public void testAnnotationsWithNonEmptyAnnotations() {
    TestAnnotation testAnnotation = new TestAnnotation();
    annotationMap.add(testAnnotation);

    Iterable<Annotation> annotations = annotationMap.annotations();
    assertTrue(annotations.iterator().hasNext());
    assertEquals(testAnnotation, annotations.iterator().next());
  }

  @Test
  public void testMergeWithNullPrimaryAndSecondary() {
    AnnotationMap result = AnnotationMap.merge(null, null);
    assertNull(result);
  }

  @Test
  public void testMergeWithNullPrimary() {
    AnnotationMap secondary = new AnnotationMap();
    AnnotationMap result = AnnotationMap.merge(null, secondary);
    assertEquals(secondary, result);
  }

  @Test
  public void testMergeWithNullSecondary() {
    AnnotationMap primary = new AnnotationMap();
    AnnotationMap result = AnnotationMap.merge(primary, null);
    assertEquals(primary, result);
  }

  @Test
  public void testMergeWithPrimaryAndSecondary() {
    AnnotationMap primary = new AnnotationMap();
    AnnotationMap secondary = new AnnotationMap();
    TestAnnotation primaryAnnotation = new TestAnnotation();
    primary.add(primaryAnnotation);
    TestAnnotation secondaryAnnotation = new TestAnnotation();
    secondary.add(secondaryAnnotation);

    AnnotationMap result = AnnotationMap.merge(primary, secondary);
    assertEquals(2, result.size());
    assertTrue(result.get(TestAnnotation.class) instanceof TestAnnotation);
  }

  @Test
  public void testSizeWithNullAnnotations() {
    assertEquals(0, annotationMap.size());
  }

  @Test
  public void testSizeWithNonNullAnnotations() {
    TestAnnotation testAnnotation = new TestAnnotation();
    annotationMap.add(testAnnotation);
    assertEquals(1, annotationMap.size());
  }

  @Test
  public void testAddIfNotPresentWithNullAnnotations() {
    TestAnnotation testAnnotation = new TestAnnotation();
    assertTrue(annotationMap.addIfNotPresent(testAnnotation));
    assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));
  }

  @Test
  public void testAddIfNotPresentWithExistingAnnotation() {
    TestAnnotation testAnnotation = new TestAnnotation();
    annotationMap.add(testAnnotation);
    assertFalse(annotationMap.addIfNotPresent(testAnnotation));
  }

  @Test
  public void testAddWithNullAnnotations() {
    TestAnnotation testAnnotation = new TestAnnotation();
    assertTrue(annotationMap.add(testAnnotation));
    assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));
  }

  @Test
  public void testAddWithExistingAnnotation() {
    TestAnnotation testAnnotation = new TestAnnotation();
    annotationMap.add(testAnnotation);
    assertFalse(annotationMap.add(testAnnotation));
  }

  @Test
  public void testToStringWithNullAnnotations() {
    assertEquals("[null]", annotationMap.toString());
  }

  @Test
  public void testToStringWithNonNullAnnotations() {
    TestAnnotation testAnnotation = new TestAnnotation();
    annotationMap.add(testAnnotation);
    assertEquals("{class TestAnnotation=@" + testAnnotation.hashCode() + "}", annotationMap.toString());
  }

  private static class TestAnnotation implements Annotation {
    @Override
    public Class<? extends Annotation> annotationType() {
      return this.getClass();
    }
  }
}