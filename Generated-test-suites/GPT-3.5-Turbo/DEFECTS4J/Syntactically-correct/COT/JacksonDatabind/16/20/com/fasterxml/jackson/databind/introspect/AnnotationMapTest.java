import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(SomeAnnotation.class));

        SomeAnnotation annotation = new SomeAnnotation();
        map.add(annotation);

        assertEquals(annotation, map.get(SomeAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap map = new AnnotationMap();

        assertEquals(Collections.emptyList(), map.annotations());

        SomeAnnotation annotation = new SomeAnnotation();
        map.add(annotation);

        assertEquals(Collections.singletonList(annotation), map.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        SomeAnnotation annotation1 = new SomeAnnotation();
        primary.add(annotation1);

        SomeAnnotation annotation2 = new SomeAnnotation();
        secondary.add(annotation2);

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);

        assertEquals(annotation1, merged.get(SomeAnnotation.class));
    }

    @Test
    public void testSize() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.size());

        SomeAnnotation annotation = new SomeAnnotation();
        map.add(annotation);

        assertEquals(1, map.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();

        SomeAnnotation annotation1 = new SomeAnnotation();
        assertTrue(map.addIfNotPresent(annotation1));
        assertFalse(map.addIfNotPresent(annotation1));

        SomeAnnotation annotation2 = new SomeAnnotation();
        assertFalse(map.addIfNotPresent(annotation2));

        assertEquals(annotation1, map.get(SomeAnnotation.class));
    }

    @Test
    public void testAdd() {
        AnnotationMap map = new AnnotationMap();

        SomeAnnotation annotation1 = new SomeAnnotation();
        assertTrue(map.add(annotation1));
        assertFalse(map.add(annotation1));

        SomeAnnotation annotation2 = new SomeAnnotation();
        assertTrue(map.add(annotation2));

        assertEquals(annotation2, map.get(SomeAnnotation.class));
    }

    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap();
        assertEquals("[null]", map.toString());

        SomeAnnotation annotation = new SomeAnnotation();
        map.add(annotation);

        assertEquals("{class SomeAnnotation=SomeAnnotation}", map.toString());
    }

}