import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collections;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Test.class));

        Test testAnnotation = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };

        annotationMap.add(testAnnotation);
        assertEquals(testAnnotation, annotationMap.get(Test.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(Collections.emptyList(), annotationMap.annotations());

        Test testAnnotation = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };

        annotationMap.add(testAnnotation);
        assertEquals(Collections.singletonList(testAnnotation), annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        AnnotationMap annotationMap2 = new AnnotationMap();

        assertNull(AnnotationMap.merge(null, annotationMap2));
        assertEquals(annotationMap1, AnnotationMap.merge(annotationMap1, null));

        Test testAnnotation1 = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };

        annotationMap1.add(testAnnotation1);
        assertEquals(annotationMap1, AnnotationMap.merge(annotationMap1, null));

        Test testAnnotation2 = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };

        annotationMap2.add(testAnnotation2);
        assertEquals(annotationMap2, AnnotationMap.merge(null, annotationMap2));

        AnnotationMap mergedMap = AnnotationMap.merge(annotationMap1, annotationMap2);
        assertTrue(mergedMap.get(Test.class) == testAnnotation1 || mergedMap.get(Test.class) == testAnnotation2);
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertFalse(annotationMap.addIfNotPresent(Test.class));

        assertTrue(annotationMap.addIfNotPresent(new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        }));

        assertFalse(annotationMap.addIfNotPresent(new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        }));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.add(Test.class));

        assertFalse(annotationMap.add(Test.class));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        annotationMap.add(Test.class);
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());

        annotationMap.add(Test.class);
        assertEquals("{interface org.junit.Test=@org.junit.Test(timeout=0, expected=class org.junit.Test$None)}", annotationMap.toString());
    }
}

