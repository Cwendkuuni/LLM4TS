import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationMapTest {
    
    @Test
    public void testGet() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(Override.class));
        
        // Adding an annotation
        map.addIfNotPresent(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        
        // Getting the annotation
        Override override = map.get(Override.class);
        assertNotNull(override);
    }
    
    @Test
    public void testAnnotations() {
        AnnotationMap map = new AnnotationMap();
        Iterable<Annotation> annotations = map.annotations();
        assertNotNull(annotations);
        
        // Adding an annotation
        map.addIfNotPresent(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        
        // Retrieving the annotations
        annotations = map.annotations();
        assertNotNull(annotations);
        assertTrue(annotations.iterator().hasNext());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        // Adding annotations to primary map
        primary.addIfNotPresent(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        primary.addIfNotPresent(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        
        // Adding annotations to secondary map
        secondary.addIfNotPresent(new SuppressWarnings() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return SuppressWarnings.class;
            }
            
            @Override
            public String[] value() {
                return new String[] {"unchecked"};
            }
        });
        
        // Merging the maps
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertNotNull(merged);
    }
    
    @Test
    public void testSize() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.size());
        
        // Adding annotations
        map.addIfNotPresent(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        map.addIfNotPresent(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        
        // Checking the size
        assertEquals(2, map.size());
    }
    
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();
        
        // Adding the same annotation twice
        assertTrue(map.addIfNotPresent(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        }));
        
        assertFalse(map.addIfNotPresent(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        }));
    }
    
    @Test
    public void testAdd() {
        AnnotationMap map = new AnnotationMap();
        
        // Adding an annotation
        assertTrue(map.add(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        }));
        
        // Adding the same annotation again
        assertFalse(map.add(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        }));
    }
    
    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap();
        assertEquals("[null]", map.toString());
        
        // Adding annotations
        map.addIfNotPresent(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        map.addIfNotPresent(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });

        assertEquals("{@java.lang.Override=interface java.lang.Override, @java.lang.Deprecated=interface java.lang.Deprecated}", map.toString());
    }
}