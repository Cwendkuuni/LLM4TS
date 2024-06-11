package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationMapTest {
    @Test
    public void testGet() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(Override.class));

        map.add(new Override() {
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        assertNotNull(map.get(Override.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(Collections.emptyList(), map.annotations());

        map.add(new Override() {
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        assertEquals(1, map.annotations().size());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap result;

        result = AnnotationMap.merge(null, null);
        assertNull(result);

        result = AnnotationMap.merge(primary, null);
        assertSame(primary, result);

        result = AnnotationMap.merge(null, secondary);
        assertSame(secondary, result);

        primary.add(new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        secondary.add(new Override() {
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });

        result = AnnotationMap.merge(primary, secondary);
        assertNotNull(result.get(Deprecated.class));
        assertNotNull(result.get(Override.class));
        assertEquals(2, result.size());
    }

    @Test
    public void testSize() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.size());
        
        map.add(new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        assertEquals(1, map.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();
        assertTrue(map.addIfNotPresent(new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        }));

        assertFalse(map.addIfNotPresent(new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        }));
    }

    @Test
    public void testAdd() {
        AnnotationMap map = new AnnotationMap();
        assertTrue(map.add(new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        }));

        assertFalse(map.add(new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        }));
    }

    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap();
        assertEquals("[null]", map.toString());

        map.add(new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        assertFalse(map.toString().equals("[null]"));
    }
}