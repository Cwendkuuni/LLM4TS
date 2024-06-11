package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.annotation.Annotation;
import java.util.*;

import org.junit.Test;

public class AnnotationMapTest {
    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Override.class));
        assertNull(annotationMap.get(Deprecated.class));
        assertNull(annotationMap.get(SuppressWarnings.class));

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Override.class, new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        annotations.put(Deprecated.class, new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        annotations.put(SuppressWarnings.class, new SuppressWarnings() {
            @Override
            public String[] value() {
                return new String[] {};
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return SuppressWarnings.class;
            }
        });

        annotationMap = new AnnotationMap(annotations);
        assertEquals(Override.class, annotationMap.get(Override.class).annotationType());
        assertEquals(Deprecated.class, annotationMap.get(Deprecated.class).annotationType());
        assertEquals(SuppressWarnings.class, annotationMap.get(SuppressWarnings.class).annotationType());
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        List<Annotation> emptyList = Collections.emptyList();
        assertEquals(emptyList, annotationMap.annotations());

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Override.class, new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        annotations.put(Deprecated.class, new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        annotations.put(SuppressWarnings.class, new SuppressWarnings() {
            @Override
            public String[] value() {
                return new String[] {};
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return SuppressWarnings.class;
            }
        });

        annotationMap = new AnnotationMap(annotations);
        List<Annotation> expectedAnnotations = new ArrayList<>();
        expectedAnnotations.add(annotationMap.get(Override.class));
        expectedAnnotations.add(annotationMap.get(Deprecated.class));
        expectedAnnotations.add(annotationMap.get(SuppressWarnings.class));
        assertEquals(expectedAnnotations, annotationMap.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        assertNull(AnnotationMap.merge(primary, secondary));

        primary = new AnnotationMap();
        secondary = null;
        assertEquals(primary, AnnotationMap.merge(primary, secondary));

        primary = null;
        secondary = new AnnotationMap();
        assertEquals(secondary, AnnotationMap.merge(primary, secondary));

        primary = new AnnotationMap();
        secondary = new AnnotationMap();
        primary.add(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(primary.get(Override.class), merged.get(Override.class));

        primary = new AnnotationMap();
        secondary = new AnnotationMap();
        secondary.add(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        merged = AnnotationMap.merge(primary, secondary);
        assertEquals(secondary.get(Deprecated.class), merged.get(Deprecated.class));

        primary = new AnnotationMap();
        secondary = new AnnotationMap();
        primary.add(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        secondary.add(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        merged = AnnotationMap.merge(primary, secondary);
        assertEquals(primary.get(Override.class), merged.get(Override.class));
        assertEquals(secondary.get(Deprecated.class), merged.get(Deprecated.class));
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Override.class, new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        annotations.put(Deprecated.class, new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        annotations.put(SuppressWarnings.class, new SuppressWarnings() {
            @Override
            public String[] value() {
                return new String[] {};
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return SuppressWarnings.class;
            }
        });

        annotationMap = new AnnotationMap(annotations);
        assertEquals(3, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();

        assertTrue(annotationMap.addIfNotPresent(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        }));
        assertFalse(annotationMap.addIfNotPresent(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        }));

        assertTrue(annotationMap.addIfNotPresent(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        }));
        assertFalse(annotationMap.addIfNotPresent(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        }));
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();

        assertTrue(annotationMap.add(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        }));

        assertTrue(annotationMap.add(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        }));

        assertEquals(2, annotationMap.size());
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());

        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Override.class, new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        annotations.put(Deprecated.class, new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        annotations.put(SuppressWarnings.class, new SuppressWarnings() {
            @Override
            public String[] value() {
                return new String[] {};
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return SuppressWarnings.class;
            }
        });

        annotationMap = new AnnotationMap(annotations);
        assertEquals(annotations.toString(), annotationMap.toString());
    }

    /*
    /**********************************************************
    /* Helper methods
    /**********************************************************
     */

}