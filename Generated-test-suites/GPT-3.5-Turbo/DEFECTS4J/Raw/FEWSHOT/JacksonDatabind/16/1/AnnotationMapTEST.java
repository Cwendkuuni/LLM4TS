import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationMapTest {

    @Test
    public void testGet(){
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Override.class, new Override(){});
        annotations.put(Deprecated.class, new Deprecated(){});
        annotations.put(SuppressWarnings.class, new SuppressWarnings("unchecked"){}); 
        AnnotationMap am = new AnnotaionMap(annotations);

        assertEquals(Override.class, am.get(Override.class).annotationType());
        assertEquals(Deprecated.class, am.get(Deprecated.class).annotationType());
        assertEquals(SuppressWarnings.class, am.get(SuppressWarnings.class).annotationType());
        assertNull(am.get(Test.class));
        assertNull(am.get(Override.class));
        assertNull(am.get(Deprecated.class));
        assertNull(am.get(SuppressWarnings.class));
    }

    @Test
    public void testAnnotations(){
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<>();
        annotations.put(Override.class, new Override(){});
        annotations.put(Deprecated.class, new Deprecated(){});
        annotations.put(SuppressWarnings.class, new SuppressWarnings("unchecked"){}); 
        AnnotationMap am = new AnnotaionMap(annotations);

        List<Annotation> expectedAnnotations = Arrays.asList(
            new Override(){}, new Deprecated(){}, new SuppressWarnings("unchecked"){}
        );

        assertEquals(expectedAnnotations, am.annotations());
    }

    @Test
    public void testMerge(){
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        assertNull(AnnotationMap.merge(primary, secondary));

        primary.add(new Override(){});
        primary.add(new Deprecated(){});

        assertEquals(primary, AnnotationMap.merge(primary, secondary));

        secondary.add(new SuppressWarnings("unchecked"){}); 
        secondary.add(new SuppressWarnings("rawtypes"){}); 

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertNotNull(merged.get(Override.class));
        assertNotNull(merged.get(Deprecated.class));
        assertNotNull(merged.get(SuppressWarnings.class));
        assertEquals(3, merged.size());
    }

    @Test
    public void testSize(){
        AnnotationMap am1 = new AnnotationMap();
        AnnotationMap am2 = new AnnotationMap();
        AnnotationMap am3 = new AnnotationMap();
        AnnotationMap am4 = new AnnotationMap();

        am2.add(new Override(){});
        am3.add(new Deprecated(){});
        am4.add(new SuppressWarnings("unchecked"){}); 

        assertEquals(0, am1.size());
        assertEquals(1, am2.size());
        assertEquals(1, am3.size());
        assertEquals(1, am4.size());
    }

    @Test
    public void testAddIfNotPresent(){
        AnnotationMap am = new AnnotationMap();
        assertTrue(am.addIfNotPresent(new Override(){}));
        assertFalse(am.addIfNotPresent(new Override(){}));
    }

    @Test
    public void testAdd(){
        AnnotationMap am = new AnnotationMap();
        assertTrue(am.add(new Override(){}));

        AnnotationMap am2 = new AnnotationMap();
        assertTrue(am2.add(new Deprecated(){}));
    }

    @Test
    public void testToString(){
        AnnotationMap am1 = new AnnotationMap();
        AnnotationMap am2 = new AnnotationMap();

        am1.add(new Override(){});
        am2.add(new Deprecated(){}); 

        assertEquals("[null]", am1.toString());
        assertEquals("{@java.lang.Deprecated()=null}", am2.toString());
    }

    @Test
    public void test_add(){
        AnnotationMap am = new AnnotationMap();

        assertTrue(am.add(new Override(){}));
        assertTrue(am.add(new Deprecated(){}));
        assertFalse(am.add(new Deprecated(){}));
    }
}