
import org.junit.Assert;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.HashMap;

public class AnnotationMapTest {
    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<Class<? extends Annotation>,Annotation>();
        annotations.put(TestAnnotation.class, new TestAnnotationImpl());
        annotationMap._annotations = annotations;

        TestAnnotation testAnnotation = annotationMap.get(TestAnnotation.class);
        Assert.assertNotNull("TestAnnotation should not be null", testAnnotation);
        Assert.assertEquals("Unexpected TestAnnotation implementation", TestAnnotationImpl.class, testAnnotation.getClass());
    }

    @Test
    public void testGetNonExistingAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();

        TestAnnotation testAnnotation = annotationMap.get(TestAnnotation.class);
        Assert.assertNull("TestAnnotation should be null", testAnnotation);
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<Class<? extends Annotation>,Annotation>();
        annotations.put(TestAnnotation.class, new TestAnnotationImpl());
        annotationMap._annotations = annotations;

        Iterable<Annotation> annotationIterable = annotationMap.annotations();
        Assert.assertFalse("Annotation Iterable should not be empty", annotationIterable.iterator().hasNext());
    }

    @Test
    public void testAnnotationsEmptyAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();

        Iterable<Annotation> annotationIterable = annotationMap.annotations();
        Assert.assertTrue("Annotation Iterable should be empty", annotationIterable.iterator().hasNext());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        HashMap<Class<? extends Annotation>,Annotation> primaryAnnotations = new HashMap<Class<? extends Annotation>,Annotation>();
        primaryAnnotations.put(TestAnnotation.class, new TestAnnotationImpl());
        primary._annotations = primaryAnnotations;

        AnnotationMap secondary = new AnnotationMap();
        HashMap<Class<? extends Annotation>,Annotation> secondaryAnnotations = new HashMap<Class<? extends Annotation>,Annotation>();
        secondaryAnnotations.put(AnotherAnnotation.class, new AnotherAnnotationImpl());
        secondary._annotations = secondaryAnnotations;

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);

        TestAnnotation testAnnotation = merged.get(TestAnnotation.class);
        Assert.assertNotNull("TestAnnotation should not be null", testAnnotation);
        Assert.assertEquals("Unexpected TestAnnotation implementation", TestAnnotationImpl.class, testAnnotation.getClass());

        AnotherAnnotation anotherAnnotation = merged.get(AnotherAnnotation.class);
        Assert.assertNotNull("AnotherAnnotation should not be null", anotherAnnotation);
        Assert.assertEquals("Unexpected AnotherAnnotation implementation", AnotherAnnotationImpl.class, anotherAnnotation.getClass());
    }

    @Test
    public void testMergeNullPrimary() {
        AnnotationMap secondary = new AnnotationMap();
        HashMap<Class<? extends Annotation>,Annotation> secondaryAnnotations = new HashMap<Class<? extends Annotation>,Annotation>();
        secondaryAnnotations.put(AnotherAnnotation.class, new AnotherAnnotationImpl());
        secondary._annotations = secondaryAnnotations;

        AnnotationMap merged = AnnotationMap.merge(null, secondary);

        AnotherAnnotation anotherAnnotation = merged.get(AnotherAnnotation.class);
        Assert.assertNotNull("AnotherAnnotation should not be null", anotherAnnotation);
        Assert.assertEquals("Unexpected AnotherAnnotation implementation", AnotherAnnotationImpl.class, anotherAnnotation.getClass());
    }

    @Test
    public void testMergeNullSecondary() {
        AnnotationMap primary = new AnnotationMap();
        HashMap<Class<? extends Annotation>,Annotation> primaryAnnotations = new HashMap<Class<? extends Annotation>,Annotation>();
        primaryAnnotations.put(TestAnnotation.class, new TestAnnotationImpl());
        primary._annotations = primaryAnnotations;

        AnnotationMap merged = AnnotationMap.merge(primary, null);

        TestAnnotation testAnnotation = merged.get(TestAnnotation.class);
        Assert.assertNotNull("TestAnnotation should not be null", testAnnotation);
        Assert.assertEquals("Unexpected TestAnnotation implementation", TestAnnotationImpl.class, testAnnotation.getClass());
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals("Unexpected AnnotationMap size", 0, annotationMap.size());
    }

    @Test
    public void testSizeWithAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<Class<? extends Annotation>,Annotation>();
        annotations.put(TestAnnotation.class, new TestAnnotationImpl());
        annotationMap._annotations = annotations;

        Assert.assertEquals("Unexpected AnnotationMap size", 1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        boolean added = annotationMap.addIfNotPresent(new TestAnnotationImpl());

        Assert.assertTrue("Annotation should have been added", added);
        Assert.assertEquals("Unexpected AnnotationMap size", 1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresentExistingAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<Class<? extends Annotation>,Annotation>();
        annotations.put(TestAnnotation.class, new TestAnnotationImpl());
        annotationMap._annotations = annotations;

        boolean added = annotationMap.addIfNotPresent(new TestAnnotationImpl());

        Assert.assertFalse("Annotation should not have been added", added);
        Assert.assertEquals("Unexpected AnnotationMap size", 1, annotationMap.size());
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        boolean added = annotationMap.add(new TestAnnotationImpl());

        Assert.assertTrue("Annotation should have been added", added);
        Assert.assertEquals("Unexpected AnnotationMap size", 1, annotationMap.size());
    }

    @Test
    public void testAddExistingAnnotation() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<Class<? extends Annotation>,Annotation>();
        annotations.put(TestAnnotation.class, new TestAnnotationImpl());
        annotationMap._annotations = annotations;

        boolean added = annotationMap.add(new TestAnnotationImpl());

        Assert.assertTrue("Annotation should have been added", added);
        Assert.assertEquals("Unexpected AnnotationMap size", 1, annotationMap.size());
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals("Unexpected AnnotationMap string representation", "[null]", annotationMap.toString());
    }

    @Test
    public void testToStringWithAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<Class<? extends Annotation>,Annotation>();
        annotations.put(TestAnnotation.class, new TestAnnotationImpl());
        annotationMap._annotations = annotations;

        Assert.assertNotEquals("Unexpected AnnotationMap string representation", "[null]", annotationMap.toString());
    }
}
