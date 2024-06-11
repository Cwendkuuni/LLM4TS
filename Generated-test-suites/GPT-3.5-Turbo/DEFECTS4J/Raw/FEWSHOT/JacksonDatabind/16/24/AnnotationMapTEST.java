package com.fasterxml.jackson.databind.introspect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;

import org.junit.Test;

@Retention(RetentionPolicy.RUNTIME)
@interface SampleAnnotation {
    String value();
}

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        SampleAnnotation sampleAnnotation = new SampleAnnotation() {
            @Override
            public String value() {
                return "sample";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return SampleAnnotation.class;
            }
        };
        annotationMap.add(sampleAnnotation);
        assertEquals(sampleAnnotation, annotationMap.get(SampleAnnotation.class));
        assertNull(annotationMap.get(Test.class));
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        SampleAnnotation sampleAnnotation1 = new SampleAnnotation() {
            @Override
            public String value() {
                return "sample1";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return SampleAnnotation.class;
            }
        };
        SampleAnnotation sampleAnnotation2 = new SampleAnnotation() {
            @Override
            public String value() {
                return "sample2";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return SampleAnnotation.class;
            }
        };
        primary.add(sampleAnnotation1);
        secondary.add(sampleAnnotation2);
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(sampleAnnotation2, merged.get(SampleAnnotation.class));
        assertEquals(1, merged.size());
    }

    @Test
    public void testSize() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        assertEquals(0, annotationMap1.size());
        AnnotationMap annotationMap2 = new AnnotationMap();
        SampleAnnotation sampleAnnotation = new SampleAnnotation() {
            @Override
            public String value() {
                return "sample";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return SampleAnnotation.class;
            }
        };
        annotationMap2.add(sampleAnnotation);
        assertEquals(1, annotationMap2.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        SampleAnnotation sampleAnnotation = new SampleAnnotation() {
            @Override
            public String value() {
                return "sample";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return SampleAnnotation.class;
            }
        };
        annotationMap.addIfNotPresent(sampleAnnotation);
        assertEquals(sampleAnnotation, annotationMap.get(SampleAnnotation.class));
        annotationMap.addIfNotPresent(sampleAnnotation);
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        SampleAnnotation sampleAnnotation = new SampleAnnotation() {
            @Override
            public String value() {
                return "sample";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return SampleAnnotation.class;
            }
        };
        annotationMap.add(sampleAnnotation);
        assertEquals(sampleAnnotation, annotationMap.get(SampleAnnotation.class));
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testToString() {
        AnnotationMap annotationMap1 = new AnnotationMap();
        assertEquals("[null]", annotationMap1.toString());
        AnnotationMap annotationMap2 = new AnnotationMap();
        SampleAnnotation sampleAnnotation = new SampleAnnotation() {
            @Override
            public String value() {
                return "sample";
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return SampleAnnotation.class;
            }
        };
        annotationMap2.add(sampleAnnotation);
        assertEquals("{interface com.fasterxml.jackson.databind.introspect.SampleAnnotation=com.fasterxml.jackson.databind.introspect.AnnotationMapTest$1}", annotationMap2.toString());
    }
}