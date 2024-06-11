import org.junit.Assert;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationMapTest {
    @Test
    public void testGet() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };

        map.add(annotation);

        Assert.assertEquals(annotation, map.get(Annotation.class));
    }

    @Test 
    public void testAnnotations() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };

        map.add(annotation);

        Iterable<Annotation> annotations = map.annotations();
        List<Annotation> annotationList = new ArrayList<>();
        for (Annotation ann : annotations) {
            annotationList.add(ann);
        }

        Assert.assertEquals(1, annotationList.size());
        Assert.assertEquals(annotation, annotationList.get(0));
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        Annotation primaryAnnotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        primary.add(primaryAnnotation);

        AnnotationMap secondary = new AnnotationMap();
        Annotation secondaryAnnotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        secondary.add(secondaryAnnotation);

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);

        Assert.assertEquals(primaryAnnotation, merged.get(Annotation.class));
    }
    
    @Test
    public void testSize() {
        AnnotationMap map = new AnnotationMap();
        
        Assert.assertEquals(0, map.size());
        
        map.add(new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        });
        
        Assert.assertEquals(1, map.size());
    }
    
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        
        Assert.assertTrue(map.addIfNotPresent(annotation));
        Assert.assertFalse(map.addIfNotPresent(annotation));
    }
    
    @Test
    public void testAdd() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        
        Assert.assertTrue(map.add(annotation));
        Assert.assertFalse(map.add(annotation));
    }

    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap();
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };

        map.add(annotation);

        String expectedString = "{interface java.lang.annotation.Annotation=java.lang.annotation.Annotation@<hashcode>}";
        Assert.assertEquals(expectedString, map.toString());
    }
}