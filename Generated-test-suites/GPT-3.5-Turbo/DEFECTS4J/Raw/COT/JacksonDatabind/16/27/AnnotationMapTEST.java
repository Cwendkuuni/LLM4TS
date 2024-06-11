
import com.fasterxml.jackson.databind.introspect.AnnotationMap;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.HashMap;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        AnnotationMap testAnnotationMap = new AnnotationMap(new HashMap<>());
        Assert.assertNull(annotationMap.get(Test.class));
        Assert.assertNull(testAnnotationMap.get(Test.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        AnnotationMap testAnnotationMap = new AnnotationMap(new HashMap<>());
        Assert.assertEquals(0, annotationMap.annotations().spliterator().getExactSizeIfKnown());
        Assert.assertEquals(0, testAnnotationMap.annotations().spliterator().getExactSizeIfKnown());
    }

    @Test
    public void testMerge() {
        AnnotationMap annotationMap = new AnnotationMap();
        AnnotationMap testAnnotationMap = new AnnotationMap(new HashMap<>());
        AnnotationMap mergedAnnotationMap1 = AnnotationMap.merge(annotationMap, testAnnotationMap);
        AnnotationMap mergedAnnotationMap2 = AnnotationMap.merge(testAnnotationMap, annotationMap);
        Assert.assertSame(testAnnotationMap, mergedAnnotationMap1);
        Assert.assertSame(testAnnotationMap, mergedAnnotationMap2);
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        AnnotationMap testAnnotationMap = new AnnotationMap(new HashMap<>());
        Assert.assertEquals(0, annotationMap.size());
        Assert.assertEquals(0, testAnnotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        AnnotationMap testAnnotationMap = new AnnotationMap(new HashMap<>());
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        Assert.assertTrue(annotationMap.addIfNotPresent(annotation));
        Assert.assertTrue(testAnnotationMap.addIfNotPresent(annotation));
        Assert.assertFalse(annotationMap.addIfNotPresent(annotation));
        Assert.assertFalse(testAnnotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        AnnotationMap testAnnotationMap = new AnnotationMap(new HashMap<>());
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        Assert.assertTrue(annotationMap.add(annotation));
        Assert.assertTrue(testAnnotationMap.add(annotation));
        Assert.assertFalse(annotationMap.add(annotation));
        Assert.assertFalse(testAnnotationMap.add(annotation));
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        AnnotationMap testAnnotationMap = new AnnotationMap(new HashMap<>());
        Assert.assertEquals("[null]", annotationMap.toString());
        Assert.assertEquals("[null]", testAnnotationMap.toString());
    }
}
