import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(Override.class));
        
        // Test adding and retrieving annotation
        Annotation ann = new Override() {
            public int value() { return 1; }
        };
        map.add(ann);
        assertEquals(ann, map.get(Override.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap map = new AnnotationMap();
        Iterable<Annotation> emptyAnnotations = map.annotations();
        assertEquals(emptyAnnotations, Collections.emptyList());
        
        // Test adding annotations and retrieving them
        Annotation ann1 = new Deprecated() {
            public String since() { return "1.0"; }
        };
        Annotation ann2 = new SuppressWarnings() {
            public String[] value() { return new String[] {"unchecked", "deprecation"}; }
        };
        map.add(ann1);
        map.add(ann2);
        Iterable<Annotation> annotations = map.annotations();
        assertEquals(List.of(ann1, ann2), annotations);
    }

    @Test
    public void testMerge() {
        AnnotationMap primaryMap = new AnnotationMap();
        AnnotationMap secondaryMap = new AnnotationMap();
        
        // Test merging two empty maps
        AnnotationMap mergedMap = AnnotationMap.merge(primaryMap, secondaryMap);
        assertEquals(0, mergedMap.size());
        
        // Test merging primary with annotations with secondary with no annotations
        Annotation ann1 = new Deprecated();
        primaryMap.add(ann1);
        mergedMap = AnnotationMap.merge(primaryMap, secondaryMap);
        assertEquals(1, mergedMap.size());
        assertTrue(mergedMap.get(Deprecated.class) != null);
        
        // Test merging primary and secondary with annotations
        Annotation ann2 = new SuppressWarnings();
        secondaryMap.add(ann2);
        mergedMap = AnnotationMap.merge(primaryMap, secondaryMap);
        assertEquals(2, mergedMap.size());
        assertTrue(mergedMap.get(Deprecated.class) != null);
        assertTrue(mergedMap.get(SuppressWarnings.class) != null);
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();

        // Test adding new annotation
        Annotation ann1 = new Deprecated();
        assertTrue(map.addIfNotPresent(ann1));
        assertTrue(map.get(Deprecated.class) != null);

        // Test adding existing annotation
        assertFalse(map.addIfNotPresent(ann1));
    }

    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap();
        assertEquals("[null]", map.toString());
        
        // Test adding annotation and toString method
        Annotation ann = new SuppressWarnings() {
            public String[] value() { return new String[] {"unchecked", "deprecation"}; }
        };
        map.add(ann);
        assertEquals("{@java.lang.SuppressWarnings(value=[unchecked, deprecation])}", map.toString());
    }
}