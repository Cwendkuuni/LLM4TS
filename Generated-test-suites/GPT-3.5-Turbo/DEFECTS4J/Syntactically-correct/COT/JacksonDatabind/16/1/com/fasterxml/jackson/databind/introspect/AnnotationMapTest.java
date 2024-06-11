
import org.junit.Assert;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.Collections;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertNull(annotationMap.get(Test.class));

        TestAnnotation testAnnotation = new TestAnnotation();
        annotationMap.add(testAnnotation);
        Assert.assertEquals(testAnnotation, annotationMap.get(TestAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals(Collections.emptyList(), annotationMap.annotations());

        TestAnnotation testAnnotation = new TestAnnotation();
        annotationMap.add(testAnnotation);
        Assert.assertEquals(Collections.singletonList(testAnnotation), annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        TestAnnotation testAnnotation1 = new TestAnnotation();
        TestAnnotation testAnnotation2 = new TestAnnotation();

        primary.add(testAnnotation1);
        secondary.add(testAnnotation2);

        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        Assert.assertEquals(testAnnotation2, result.get(TestAnnotation.class));
        Assert.assertEquals(1, result.size());
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals(0, annotationMap.size());

        TestAnnotation testAnnotation = new TestAnnotation();
        annotationMap.add(testAnnotation);
        Assert.assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();

        TestAnnotation testAnnotation1 = new TestAnnotation();
        TestAnnotation testAnnotation2 = new TestAnnotation();

        Assert.assertTrue(annotationMap.addIfNotPresent(testAnnotation1));
        Assert.assertFalse(annotationMap.addIfNotPresent(testAnnotation2));
        Assert.assertEquals(testAnnotation1, annotationMap.get(TestAnnotation.class));
        Assert.assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();

        TestAnnotation testAnnotation1 = new TestAnnotation();
        TestAnnotation testAnnotation2 = new TestAnnotation();

        Assert.assertTrue(annotationMap.add(testAnnotation1));
        Assert.assertTrue(annotationMap.add(testAnnotation2));
        Assert.assertEquals(testAnnotation2, annotationMap.get(TestAnnotation.class));
        Assert.assertEquals(2, annotationMap.size());
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        Assert.assertEquals("[null]", annotationMap.toString());

        TestAnnotation testAnnotation = new TestAnnotation();
        annotationMap.add(testAnnotation);
        Assert.assertEquals("{class TestAnnotation=com.fasterxml.jackson.databind.introspect.TestAnnotation@<hashCode>}", annotationMap.toString());
    }

    @TestAnnotation
    private static class TestAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation.class;
        }
    }
}
