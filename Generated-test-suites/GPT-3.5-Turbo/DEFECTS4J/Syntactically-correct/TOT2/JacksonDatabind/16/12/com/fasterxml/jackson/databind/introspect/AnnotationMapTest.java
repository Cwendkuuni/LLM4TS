import static org.junit.Assert.*;
import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(Override.class));
    
        map.add(new Override() {
            @Override public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        assertNotNull(map.get(Override.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.annotations().size());
    
        map.add(new Override() {
            @Override public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        assertNotEquals(0, map.annotations().size());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new Override() {
            @Override public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
    
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new Deprecated() {
            @Override public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
    
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertNotNull(merged.get(Override.class));
        assertNotNull(merged.get(Deprecated.class));
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();
        Annotation ann = new Override() {
            @Override public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        };
        assertTrue(map.addIfNotPresent(ann));
        assertFalse(map.addIfNotPresent(ann));
    }

    @Test
    public void testAdd() {
        AnnotationMap map = new AnnotationMap();
        Annotation ann = new Override() {
            @Override public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        };
        assertTrue(map.add(ann));
        assertFalse(map.add(ann));
    }

    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap();
        assertEquals("[null]", map.toString());
    
        map.add(new Override() {
            @Override public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        assertNotEquals("[null]", map.toString());
    }

}