package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationMapTest {
    @Test
    public void testGet() {
        AnnotationMap am = new AnnotationMap();
        assertNull(am.get(Override.class));

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Override.class, new Override() {
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });
        AnnotationMap am2 = new AnnotationMap(annotations);
        assertNotNull(am2.get(Override.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap am = new AnnotationMap();
        Iterable<Annotation> empty = Collections.emptyList();
        assertEquals(empty, am.annotations());

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Override.class, new Override() {
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });
        AnnotationMap am2 = new AnnotationMap(annotations);
        assertNotNull(am2.annotations());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        assertNull(AnnotationMap.merge(primary, secondary));

        HashMap<Class<? extends Annotation>, Annotation> primaryAnnotations = new HashMap<>();
        primaryAnnotations.put(Override.class, new Override() {
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });
        primaryAnnotations.put(Deprecated.class, new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });
        HashMap<Class<? extends Annotation>, Annotation> secondaryAnnotations = new HashMap<>();
        secondaryAnnotations.put(Deprecated.class, new Deprecated() {
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });
        secondaryAnnotations.put(SuppressWarnings.class, new SuppressWarnings() {
            public String[] value() {
                return null;
            }

                public String[] suppress() {
                return null;
            }

            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });
        AnnotationMap primaryMap = new AnnotationMap(primaryAnnotations);
        AnnotationMap secondaryMap = new AnnotationMap(secondaryAnnotations);
        AnnotationMap merged = AnnotationMap.merge(primaryMap, secondaryMap);

        assertEquals(3, merged.size());
        assertNotNull(merged.get(Override.class));
        assertNotNull(merged.get(Deprecated.class));
        assertNotNull(merged.get(SuppressWarnings.class));
    }

    @Test
    public void testSize() {
        AnnotationMap am = new AnnotationMap();
        assertEquals(0, am.size());

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Override.class, new Override() {
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });
        AnnotationMap am2 = new AnnotationMap(annotations);
        assertEquals(1, am2.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap am = new AnnotationMap();
        assertTrue(am.addIfNotPresent(new Override() {
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        }));
        assertFalse(am.addIfNotPresent(new Override() {
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        }));
    }

    @Test
    public void testAdd() {
        AnnotationMap am = new AnnotationMap();
        assertTrue(am.add(new Override() {
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        }));
        assertFalse(am.add(new Override() {
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        }));
    }

    @Test
    public void testToString() {
        AnnotationMap am = new AnnotationMap();
        assertEquals("[null]", am.toString());

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Override.class, new Override() {
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });
        AnnotationMap am2 = new AnnotationMap(annotations);
        assertNotNull(am2.toString());
    }

    @Test
    public void test_add() {
        AnnotationMap am = new AnnotationMap();
        assertTrue(am._add(new Override() {
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        }));
        assertFalse(am._add(new Override() {
            public Class<? extends Annotation> annotationType() {
                return null;
            }
        }));
    }
}