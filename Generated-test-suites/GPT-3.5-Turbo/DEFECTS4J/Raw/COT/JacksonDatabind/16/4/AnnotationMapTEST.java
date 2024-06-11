
import org.junit.Assert;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertNull(annotationMap.get(Test.class));

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        TestAnnotation annotation = annotationMap.get(TestAnnotation.class);
        Assert.assertNotNull(annotation);
        Assert.assertEquals("test", annotation.value());
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        Iterable<Annotation> iterable = annotationMap.annotations();
        Assert.assertEquals(Collections.emptyList(), iterable);

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        iterable = annotationMap.annotations();
        Assert.assertEquals(1, iterable.spliterator().getExactSizeIfKnown());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> primaryAnnotations = new HashMap<>();
        primaryAnnotations.put(Test.class, new TestAnnotation());
        primary = new AnnotationMap(primaryAnnotations);

        AnnotationMap secondary = new AnnotationMap();
        HashMap<Class<? extends Annotation>, Annotation> secondaryAnnotations = new HashMap<>();
        secondaryAnnotations.put(Deprecated.class, new DeprecatedAnnotation());
        secondary = new AnnotationMap(secondaryAnnotations);

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        Assert.assertNotNull(merged);
        Assert.assertEquals(2, merged.size());
        Assert.assertEquals(TestAnnotation.class, merged.get(TestAnnotation.class).annotationType());
        Assert.assertEquals(DeprecatedAnnotation.class, merged.get(DeprecatedAnnotation.class).annotationType());
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals(0, annotationMap.size());

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        Assert.assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertTrue(annotationMap.addIfNotPresent(new TestAnnotation()));
        Assert.assertFalse(annotationMap.addIfNotPresent(new TestAnnotation()));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertTrue(annotationMap.add(new TestAnnotation()));
        Assert.assertFalse(annotationMap.add(new TestAnnotation()));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals("[null]", annotationMap.toString());

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Test.class, new TestAnnotation());
        annotationMap = new AnnotationMap(annotations);
        Assert.assertEquals("{interface org.junit.Test=@com.fasterxml.jackson.databind.introspect.TestAnnotation(value=test)}", annotationMap.toString());
    }
}
