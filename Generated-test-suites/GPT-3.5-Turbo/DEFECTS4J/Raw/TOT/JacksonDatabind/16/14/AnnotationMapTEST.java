import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Collections;
import com.fasterxml.jackson.databind.introspect.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        // Test case where annotations map is null
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(Override.class));

        // Test case where annotation is present
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        annotations.put(Override.class, new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        map = new AnnotationMap(annotations);
        assertEquals(Override.class, map.get(Override.class).annotationType());
    }

    @Test
    public void testAnnotations() {
        // Test case where annotations map is null
        AnnotationMap map = new AnnotationMap();
        assertEquals(Collections.emptyList(), map.annotations());

        // Test case where annotations map is empty
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        map = new AnnotationMap(annotations);
        assertEquals(0, map.annotations().iterator().hasNext());

        // Test case where annotations map has values
        annotations.put(Override.class, new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        map = new AnnotationMap(annotations);
        assertTrue(map.annotations().iterator().hasNext());
        assertEquals(Override.class, map.annotations().iterator().next().annotationType());
    }

    @Test
    public void testMerge() {
        // Test case where primary and secondary are null
        AnnotationMap primary = null;
        AnnotationMap secondary = null;
        assertNull(AnnotationMap.merge(primary, secondary));

        // Test case where primary is null and secondary is empty
        secondary = new AnnotationMap();
        assertEquals(secondary, AnnotationMap.merge(primary, secondary));

        // Test case where secondary is null and primary is empty
        primary = new AnnotationMap();
        assertEquals(primary, AnnotationMap.merge(primary, secondary));
        
        // Test case where primary and secondary have overlapping annotations
        primary.addIfNotPresent(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        secondary.addIfNotPresent(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
        assertEquals(2, mergedMap.size());
        assertTrue(mergedMap.get(Override.class) instanceof Override);
        assertTrue(mergedMap.get(Deprecated.class) instanceof Deprecated);
    }

    @Test
    public void testSize() {
        // Test case where annotations map is null
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.size());

        // Test case where annotations map is empty
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        map = new AnnotationMap(annotations);
        assertEquals(0, map.size());

        // Test case where annotations map has values
        annotations.put(Override.class, new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        map = new AnnotationMap(annotations);
        assertEquals(1, map.size());
    }

    @Test
    public void testAddIfNotPresent() {
        // Test case where annotations map is null and annotation doesn't exist
        AnnotationMap map = new AnnotationMap();
        assertTrue(map.addIfNotPresent(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        }));
        assertEquals(Override.class, map.get(Override.class).annotationType());

        // Test case where annotations map is null and annotation exists
        assertFalse(map.addIfNotPresent(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        }));

        // Test case where annotations map is not null and annotation doesn't exist
        map = new AnnotationMap();
        map.addIfNotPresent(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        assertTrue(map.addIfNotPresent(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        }));
        assertEquals(Override.class, map.get(Override.class).annotationType());

        // Test case where annotations map is not null and annotation exists
        assertFalse(map.addIfNotPresent(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        }));
    }

    @Test
    public void testAdd() {
        // Test case where annotations map is null and annotation is added
        AnnotationMap map = new AnnotationMap();
        assertTrue(map.add(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        }));
        assertEquals(Override.class, map.get(Override.class).annotationType());

        // Test case where annotations map is null and annotation already exists
        assertFalse(map.add(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        }));

        // Test case where annotations map is not null and annotation is added
        map = new AnnotationMap();
        map.add(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        assertTrue(map.add(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        }));
        assertEquals(Override.class, map.get(Override.class).annotationType());

        // Test case where annotations map is not null and annotation already exists
        assertFalse(map.add(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        }));
    }

    @Test
    public void testToString() {
        // Test case where annotations map is null
        AnnotationMap map = new AnnotationMap();
        assertEquals("[null]", map.toString());

        // Test case where annotations map is not null
        HashMap<Class<? extends Annotation>,Annotation> annotations = new HashMap<>();
        annotations.put(Override.class, new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        map = new AnnotationMap(annotations);
        assertEquals("{interface java.lang.Override=@interface java.lang.Override}", map.toString());
    }
}