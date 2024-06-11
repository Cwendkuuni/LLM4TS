import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.*;
import com.fasterxml.jackson.databind.util.Annotations;

public class AnnotationMapTest {
    
    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test when _annotations is null
        assertNull(annotationMap.get(Test.class));
        
        // Test when _annotations is not null
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        Test testAnnotation = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };
        annotations.put(Test.class, testAnnotation);
        annotationMap = new AnnotationMap(annotations);
        assertEquals(testAnnotation, annotationMap.get(Test.class));
    }
    
    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test when _annotations is null
        assertEquals(Collections.emptyList(), annotationMap.annotations());
        
        // Test when _annotations is not null
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        Test testAnnotation = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };
        annotations.put(Test.class, testAnnotation);
        annotationMap = new AnnotationMap(annotations);
        assertEquals(Collections.singletonList(testAnnotation), annotationMap.annotations());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        // Test when primary and secondary are null
        assertNull(AnnotationMap.merge(null, null));
        
        // Test when primary is null
        HashMap<Class<? extends Annotation>, Annotation> secondaryAnnotations = new HashMap<>();
        Test testAnnotation1 = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };
        secondaryAnnotations.put(Test.class, testAnnotation1);
        secondary = new AnnotationMap(secondaryAnnotations);
        assertEquals(secondary, AnnotationMap.merge(null, secondary));
        
        // Test when secondary is null
        HashMap<Class<? extends Annotation>, Annotation> primaryAnnotations = new HashMap<>();
        Test testAnnotation2 = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };
        primaryAnnotations.put(Test.class, testAnnotation2);
        primary = new AnnotationMap(primaryAnnotations);
        assertEquals(primary, AnnotationMap.merge(primary, null));
        
        // Test when both primary and secondary are not null
        primaryAnnotations.put(com.fasterxml.jackson.annotation.JsonIgnore.class, new com.fasterxml.jackson.annotation.JsonIgnore() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return com.fasterxml.jackson.annotation.JsonIgnore.class;
            }

            @Override
            public boolean value() {
                return false;
            }
        });
        primary = new AnnotationMap(primaryAnnotations);
        
        secondaryAnnotations.put(com.fasterxml.jackson.databind.annotation.JsonSerialize.class, new com.fasterxml.jackson.databind.annotation.JsonSerialize() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return com.fasterxml.jackson.databind.annotation.JsonSerialize.class;
            }

            @Override
            public Class<?> using() {
                return Void.class;
            }

            @Override
            public Class<?>[] as() {
                return new Class<?>[]{};
            }
        });
        secondary = new AnnotationMap(secondaryAnnotations);
        
        AnnotationMap expectedResult = new AnnotationMap();
        expectedResult.add(com.fasterxml.jackson.annotation.JsonIgnore.class);
        expectedResult.add(com.fasterxml.jackson.databind.annotation.JsonSerialize.class);
        assertEquals(expectedResult, AnnotationMap.merge(primary, secondary));
    }
    
    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test when _annotations is null
        assertEquals(0, annotationMap.size());
        
        // Test when _annotations is not null
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        Test testAnnotation = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };
        annotations.put(Test.class, testAnnotation);
        annotationMap = new AnnotationMap(annotations);
        assertEquals(1, annotationMap.size());
    }
    
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Test testAnnotation = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };
        
        // Test when _annotations is null
        assertTrue(annotationMap.addIfNotPresent(testAnnotation));
        assertEquals(Collections.singletonList(testAnnotation), annotationMap.annotations());
        
        // Test when _annotations is not null and does not contain the annotation
        annotationMap.addIfNotPresent(testAnnotation);
        assertEquals(Collections.singletonList(testAnnotation), annotationMap.annotations());
        
        // Test when _annotations is not null and contains the annotation
        assertFalse(annotationMap.addIfNotPresent(testAnnotation));
        assertEquals(Collections.singletonList(testAnnotation), annotationMap.annotations());
    }
    
    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        Test testAnnotation = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };
        
        // Test when _annotations is null
        assertTrue(annotationMap.add(testAnnotation));
        assertEquals(Collections.singletonList(testAnnotation), annotationMap.annotations());
        
        // Test when _annotations is not null and does not contain the annotation
        annotationMap.add(testAnnotation);
        assertEquals(Collections.singletonList(testAnnotation), annotationMap.annotations());
        
        // Test when _annotations is not null and contains the annotation
        assertFalse(annotationMap.add(testAnnotation));
        assertEquals(Collections.singletonList(testAnnotation), annotationMap.annotations());
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        
        // Test when _annotations is null
        assertEquals("[null]", annotationMap.toString());
        
        // Test when _annotations is not null
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        Test testAnnotation = new Test() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Test.class;
            }
        };
        annotations.put(Test.class, testAnnotation);
        annotationMap = new AnnotationMap(annotations);
        assertEquals(annotations.toString(), annotationMap.toString());
    }    
}