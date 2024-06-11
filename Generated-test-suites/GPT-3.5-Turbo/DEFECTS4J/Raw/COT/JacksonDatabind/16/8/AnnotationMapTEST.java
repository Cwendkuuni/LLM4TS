
import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Arrays;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(Override.class));
        
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        map.add(annotation);
        
        assertEquals(annotation, map.get(annotation.annotationType()));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap map = new AnnotationMap();
        assertTrue(map.annotations().isEmpty());
        
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        map.add(annotation);
        
        assertEquals(Arrays.asList(annotation), map.annotations());
    }
    
    @Test
    public void testMerge() {
        Annotation annotation1 = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        
        AnnotationMap map1 = new AnnotationMap();
        map1.add(annotation1);
        
        AnnotationMap map2 = new AnnotationMap();
        Annotation annotation2 = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        map2.add(annotation2);
        
        AnnotationMap mergedMap = AnnotationMap.merge(map1, map2);
        assertEquals(Arrays.asList(annotation2, annotation1), mergedMap.annotations());
    }
    
    @Test
    public void testSize() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.size());
        
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        map.add(annotation);
        
        assertEquals(1, map.size());
    }
    
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        assertTrue(map.addIfNotPresent(annotation));
        assertFalse(map.addIfNotPresent(annotation));
    }
    
    @Test
    public void testAdd() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        assertTrue(map.add(annotation));
        assertFalse(map.add(annotation));
    }
    
    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap();
        assertEquals("[null]", map.toString());
        
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        map.add(annotation);
        
        assertEquals(new HashMap<Class<? extends Annotation>,Annotation>() {{

            put(annotation.annotationType(), annotation);

        }}.toString(), map.toString());
    }

    @Test
    public void testAddUnderscore() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        assertFalse(map._add(annotation));
        assertTrue(map.add(annotation));
    }
}

