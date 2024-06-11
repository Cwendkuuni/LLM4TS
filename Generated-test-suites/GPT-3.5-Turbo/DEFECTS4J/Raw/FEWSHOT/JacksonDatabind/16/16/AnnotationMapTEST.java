
package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annMap = new AnnotationMap();
        
        // Test when the map is empty
        assertNull(annMap.get(Deprecated.class));
        
        // Test when the map contains an annotation
        annMap.addIfNotPresent(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        assertEquals(Deprecated.class, annMap.get(Deprecated.class).annotationType());
        
        // Test when the map contains another annotation
        annMap.addIfNotPresent(new SuppressWarnings() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return SuppressWarnings.class;
            }
            @Override
            public String[] value() {
                return new String[] {"unchecked"};
            }
        });
        assertEquals(SuppressWarnings.class, annMap.get(SuppressWarnings.class).annotationType());
    }

    @Test
    public void testAnnotations() {
        AnnotationMap annMap = new AnnotationMap();
        
        // Test when the map is empty
        assertEquals(Collections.emptyList(), annMap.annotations());
        
        // Test when the map contains an annotation
        annMap.addIfNotPresent(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        List<Annotation> expectedAnnotations1 = new ArrayList<>();
        expectedAnnotations1.add(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        assertEquals(expectedAnnotations1, annMap.annotations());
        
        // Test when the map contains another annotation
        annMap.addIfNotPresent(new SuppressWarnings() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return SuppressWarnings.class;
            }
            @Override
            public String[] value() {
                return new String[] {"unchecked"};
            }
        });
        List<Annotation> expectedAnnotations2 = new ArrayList<>();
        expectedAnnotations2.add(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        expectedAnnotations2.add(new SuppressWarnings() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return SuppressWarnings.class;
            }
            @Override
            public String[] value() {
                return new String[] {"unchecked"};
            }
        });
        assertEquals(expectedAnnotations2, annMap.annotations());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap annMap1 = new AnnotationMap();
        AnnotationMap annMap2 = new AnnotationMap();
        
        // Test when both maps are empty
        AnnotationMap mergedMap1 = AnnotationMap.merge(annMap1, annMap2);
        assertEquals(0, mergedMap1.size());
        
        // Test when one map is empty
        annMap1.addIfNotPresent(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        AnnotationMap mergedMap2 = AnnotationMap.merge(annMap1, annMap2);
        assertEquals(1, mergedMap2.size());
        assertEquals(Deprecated.class, mergedMap2.get(Deprecated.class).annotationType());
        
        // Test when both maps contain annotations
        annMap2.addIfNotPresent(new SuppressWarnings() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return SuppressWarnings.class;
            }
            @Override
            public String[] value() {
                return new String[] {"unchecked"};
            }
        });
        AnnotationMap mergedMap3 = AnnotationMap.merge(annMap1, annMap2);
        assertEquals(2, mergedMap3.size());
        assertEquals(Deprecated.class, mergedMap3.get(Deprecated.class).annotationType());
        assertEquals(SuppressWarnings.class, mergedMap3.get(SuppressWarnings.class).annotationType());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annMap = new AnnotationMap();
        
        // Test when the map is empty
        bool result1 = annMap.addIfNotPresent(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        assertEquals(true, result1);
        assertEquals(Deprecated.class, annMap.get(Deprecated.class).annotationType());
        
        // Test when the map already contains the annotation
        bool result2 = annMap.addIfNotPresent(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        assertEquals(false, result2);
        assertEquals(Deprecated.class, annMap.get(Deprecated.class).annotationType());
    }

    @Test
    public void testAdd() {
        AnnotationMap annMap = new AnnotationMap();
        
        // Test when the map is empty
        assertEquals(true, annMap.add(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        }));
        assertEquals(Deprecated.class, annMap.get(Deprecated.class).annotationType());
        
        // Test when the map already contains the annotation
        assertEquals(false, annMap.add(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        }));
        assertEquals(Deprecated.class, annMap.get(Deprecated.class).annotationType());
    }
    
    @Test
    public void testSize() {
        AnnotationMap annMap = new AnnotationMap();
        
        // Test when the map is empty
        assertEquals(0, annMap.size());
        
        // Test when the map contains an annotation
        annMap.addIfNotPresent(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        assertEquals(1, annMap.size());
        
        // Test when the map contains another annotation
        annMap.addIfNotPresent(new SuppressWarnings() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return SuppressWarnings.class;
            }
            @Override
            public String[] value() {
                return new String[] {"unchecked"};
            }
        });
        assertEquals(2, annMap.size());
    }

    @Test
    public void testToString() {
        AnnotationMap annMap = new AnnotationMap();
        
        // Test when the map is empty
        assertEquals("[null]", annMap.toString());
        
        // Test when the map contains an annotation
        annMap.addIfNotPresent(new Deprecated() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Deprecated.class;
            }
        });
        assertEquals("{interface java.lang.Deprecated=@com.fasterxml.jackson.databind.introspect.AnnotationMapTest$1()}", annMap.toString());
        
        // Test when the map contains another annotation
        annMap.addIfNotPresent(new SuppressWarnings() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return SuppressWarnings.class;
            }
            @Override
            public String[] value() {
                return new String[] {"unchecked"};
            }
        });
        assertEquals("{interface java.lang.Deprecated=@com.fasterxml.jackson.databind.introspect.AnnotationMapTest$1(), interface java.lang.SuppressWarnings=@com.fasterxml.jackson.databind.introspect.AnnotationMapTest$2()}", annMap.toString());
    }
}
