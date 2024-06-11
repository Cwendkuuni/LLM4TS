import org.junit.*;
import com.fasterxml.jackson.databind.util.Annotations;
import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationMapTest {
    
    private AnnotationMap annotationMap;
    
    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }
    
    @After
    public void tearDown() {
        annotationMap = null;
    }
    
    @Test
    public void testGet() {
        Annotation actual = annotationMap.get(Deprecated.class);
        Assert.assertNull(actual);
        // Add annotation
        Deprecated annotation = new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        annotationMap.add(annotation);
        actual = annotationMap.get(Deprecated.class);
        Assert.assertEquals(annotation, actual);
    }
    
    @Test
    public void testAnnotations() {
        Iterable<Annotation> expected = Collections.emptyList();
        Iterable<Annotation> actual = annotationMap.annotations(); 
        Assert.assertEquals(expected, actual);
        // Add annotation
        Deprecated annotation = new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        annotationMap.add(annotation);
        expected = Collections.singleton(annotation);
        actual = annotationMap.annotations();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap actual = AnnotationMap.merge(primary, secondary);
        Assert.assertEquals(primary, actual);
        // Add annotations
        Deprecated annotation1 = new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        primary.add(annotation1);
        Deprecated annotation2 = new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        secondary.add(annotation2);
        actual = AnnotationMap.merge(primary, secondary);
        AnnotationMap expected = new AnnotationMap();
        expected.add(annotation2);
        expected.add(annotation1);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testSize() {
        int expected = 0;
        int actual = annotationMap.size();
        Assert.assertEquals(expected, actual);
        // Add annotation
        Deprecated annotation = new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        annotationMap.add(annotation);
        expected = 1;
        actual = annotationMap.size();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testAddIfNotPresent() {
        boolean actual = annotationMap.addIfNotPresent(Deprecated.class);
        Assert.assertTrue(actual);
        // Add same annotation again
        actual = annotationMap.addIfNotPresent(Deprecated.class);
        Assert.assertFalse(actual);
    }
    
    @Test
    public void testAdd() {
        boolean actual = annotationMap.add(Deprecated.class);
        Assert.assertTrue(actual);
        // Add same annotation again
        actual = annotationMap.add(Deprecated.class);
        Assert.assertFalse(actual);
    }
    
    @Test
    public void testToString() {
        String expected = "[null]";
        String actual = annotationMap.toString();
        Assert.assertEquals(expected, actual);
        // Add annotation
        Deprecated annotation = new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        };
        annotationMap.add(annotation);
        expected = "{" + annotation.annotationType().getName() + "=" + annotation.toString() + "}";
        actual = annotationMap.toString();
        Assert.assertEquals(expected, actual);
    }
}