
package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.HashMap;

public class AnnotationMapTest {

    // Test case for default constructor
    @Test
    public void testDefaultConstructor() {
        AnnotationMap map = new AnnotationMap();
        assertNotNull(map);
    }
    
    // Test case for get method with a null annotations map
    @Test
    public void testGetWithNullAnnotationsMap() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(SuppressWarnings.class));
    }

    // Test case for annotations method with empty annotations map
    @Test
    public void testAnnotationsWithEmptyMap() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(Collections.emptyList(), map.annotations());
    }

    // Test case for merge method with null primary map
    @Test
    public void testMergeWithNullPrimaryMap() {
        AnnotationMap primary = null;
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        assertEquals(0, result.size());
    }

    // Test case for addIfNotPresent method with existing annotation
    @Test
    public void testAddIfNotPresentWithExistingAnnotation() {
        AnnotationMap map = new AnnotationMap();
        SuppressWarnings annotation = new SuppressWarnings() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return SuppressWarnings.class;
            }
    
            @Override
            public String[] value() {
                return new String[]{"unused"};
            }
        };
        map._add(annotation);
        assertFalse(map.addIfNotPresent(annotation));
    }

    // Test case for add method with new annotation
    @Test
    public void testAddWithNewAnnotation() {
        AnnotationMap map = new AnnotationMap();
        SuppressWarnings annotation = new SuppressWarnings() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return SuppressWarnings.class;
            }
    
            @Override
            public String[] value() {
                return new String[]{"unused"};
            }
        };
        assertTrue(map.add(annotation));
    }

    // Test case for toString method with null annotations map
    @Test
    public void testToStringWithNullAnnotationsMap() {
        AnnotationMap map = new AnnotationMap();
        assertEquals("[null]", map.toString());
    }
}
