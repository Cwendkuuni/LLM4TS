import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.Collections;
import static org.junit.Assert.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertNull(annotationMap.get(Annotation.class));
    }
    
    @Test
    public void testAnnotations() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertTrue(annotationMap.annotations().isEmpty());
    }
    
    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertSame(primary, merged);
    }
    
    @Test
    public void testSize() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals(0, annotationMap.size());
    }
    
    @Test
    public void testAddIfNotPresent() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        assertTrue(annotationMap.addIfNotPresent(annotation));
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }
    
    @Test
    public void testAdd() {
        AnnotationMap annotationMap = new AnnotationMap();
        Annotation annotation = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return Annotation.class;
            }
        };
        assertTrue(annotationMap.add(annotation));
        assertFalse(annotationMap.add(annotation));
    }
    
    @Test
    public void testToString() {
        AnnotationMap annotationMap = new AnnotationMap();
        assertEquals("[null]", annotationMap.toString());
    }
}