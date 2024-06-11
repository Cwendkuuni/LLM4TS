import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGet() {
        assertNull(annotationMap.get(MyAnnotation.class));
    }

    @Test
    public void testAnnotations() {
        assertTrue(annotationMap.annotations().isEmpty());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new MyAnnotation());

        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new MyAnnotation2());

        AnnotationMap merged = AnnotationMap.merge(primary, secondary);

        assertEquals(2, merged.size());
    }

    @Test
    public void testSize() {
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        assertTrue(annotationMap.addIfNotPresent(new MyAnnotation()));
        assertFalse(annotationMap.addIfNotPresent(new MyAnnotation()));
    }

    @Test
    public void testAdd() {
        assertTrue(annotationMap.add(new MyAnnotation()));
    }

    @Test
    public void testToString() {
        assertEquals("[null]", annotationMap.toString());
    }

    @Test
    public void testAddAnnotations() {
        AnnotationMap map = new AnnotationMap();
        MyAnnotation annotation = new MyAnnotation();
        map.add(annotation);

        assertTrue(map.get(MyAnnotation.class).equals(annotation));
    }

    private static class MyAnnotation implements Annotation { }

    private static class MyAnnotation2 implements Annotation { }

}