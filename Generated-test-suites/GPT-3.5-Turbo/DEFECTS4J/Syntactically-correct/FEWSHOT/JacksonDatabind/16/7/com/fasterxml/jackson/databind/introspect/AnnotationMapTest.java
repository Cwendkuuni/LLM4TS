package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class AnnotationMapTest {
    private AnnotationMap annotationMap;

    @Before
    public void setup() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGet() {
        assertNull(annotationMap.get(Override.class));

        annotationMap.add(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        assertNotNull(annotationMap.get(Override.class));
    }

    @Test
    public void testAnnotations() {
        assertEquals(0, annotationMap.annotations().size());

        annotationMap.add(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        assertEquals(1, annotationMap.annotations().size());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(0, merged.size());

        primary.add(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        merged = AnnotationMap.merge(primary, secondary);
        assertEquals(1, merged.size());

        secondary.add(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        merged = AnnotationMap.merge(primary, secondary);
        assertEquals(2, merged.size());
    }

    @Test
    public void testSize() {
        assertEquals(0, annotationMap.size());

        annotationMap.add(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        assertFalse(annotationMap.addIfNotPresent(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        }));
        assertEquals(1, annotationMap.size());

        assertFalse(annotationMap.addIfNotPresent(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        }));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAdd() {
        assertTrue(annotationMap.add(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        }));
        assertEquals(1, annotationMap.size());

        assertFalse(annotationMap.add(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        }));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testToString() {
        assertEquals("[null]", annotationMap.toString());

        annotationMap.add(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        assertEquals("{interface java.lang.Deprecated=@interface java.lang.Deprecated()}", annotationMap.toString());
    }

    @Test
    public void testAddWithDifferentAnnotations() {
        annotationMap.add(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        annotationMap.add(new Override() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Override.class;
            }
        });
        assertEquals(2, annotationMap.size());
    }
}