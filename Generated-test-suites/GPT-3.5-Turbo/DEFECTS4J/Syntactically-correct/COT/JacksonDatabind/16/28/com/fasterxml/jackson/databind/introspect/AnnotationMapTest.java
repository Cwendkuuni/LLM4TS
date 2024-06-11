
package com.fasterxml.jackson.databind.introspect;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGet() {
        // Test with no annotations added
        assertNull(annotationMap.get(Test.class));

        // Test with added annotation
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
        // Test with no annotations added
        assertEquals(0, annotationMap.annotations().spliterator().getExactSizeIfKnown());

        // Test with added annotation
        Test testAnnotation = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };
        annotationMap.add(testAnnotation);

        assertEquals(1, annotationMap.annotations().spliterator().getExactSizeIfKnown());
    }

    @Test
    public void testMerge() {
        // Test with null maps
        AnnotationMap mergedMap = AnnotationMap.merge(null, null);
        assertNull(mergedMap);

        // Test with one null map
        AnnotationMap map1 = new AnnotationMap();
        AnnotationMap mergedMap1 = AnnotationMap.merge(map1, null);
        assertEquals(map1, mergedMap1);

        AnnotationMap map2 = new AnnotationMap();
        AnnotationMap mergedMap2 = AnnotationMap.merge(null, map2);
        assertEquals(map2, mergedMap2);

        // Test with empty maps
        AnnotationMap mergedMap3 = AnnotationMap.merge(map1, map2);
        assertEquals(map1, mergedMap3);

        // Test with non-empty maps
        Test testAnnotation1 = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };
        map1.add(testAnnotation1);

        Test testAnnotation2 = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };
        map2.add(testAnnotation2);

        AnnotationMap mergedMap4 = AnnotationMap.merge(map1, map2);
        assertEquals(2, mergedMap4.size());
        assertEquals(testAnnotation1, mergedMap4.get(Test.class));
        assertEquals(testAnnotation2, mergedMap4.get(Test.class));
    }

    @Test
    public void testSize() {
        // Test with no annotations added
        assertEquals(0, annotationMap.size());

        // Test with added annotation
        Test testAnnotation = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };
        annotationMap.add(testAnnotation);

        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        // Test adding annotation when not present
        Test testAnnotation1 = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };
        boolean added1 = annotationMap.addIfNotPresent(testAnnotation1);
        assertEquals(true, added1);
        assertEquals(testAnnotation1, annotationMap.get(Test.class));

        // Test adding annotation when already present
        Test testAnnotation2 = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };
        boolean added2 = annotationMap.addIfNotPresent(testAnnotation2);
        assertEquals(false, added2);
    }

    @Test
    public void testAdd() {
        // Test adding annotation
        Test testAnnotation = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };
        boolean added = annotationMap.add(testAnnotation);
        assertEquals(true, added);
        assertEquals(testAnnotation, annotationMap.get(Test.class));
    }

    @Test
    public void testToString() {
        // Test with no annotations added
        assertEquals("[null]", annotationMap.toString());

        // Test with added annotation
        Test testAnnotation = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };
        annotationMap.add(testAnnotation);

        assertEquals("{interface org.junit.Test="+testAnnotation.toString()+"}", annotationMap.toString());
    }

}
