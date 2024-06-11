import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.*;

public class AnnotationMapTest {

    @Test
    public void testGet() {
        AnnotationMap map = new AnnotationMap();
        assertNull(map.get(Test.class));

        map.add(Test.class.getAnnotation(Test.class));
        assertNotNull(map.get(Test.class));
    }

    @Test
    public void testAnnotations() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(Collections.emptyList(), map.annotations());

        map.add(Test.class.getAnnotation(Test.class));
        assertEquals(Collections.singletonList(Test.class.getAnnotation(Test.class)), map.annotations());
    }

    @Test
    public void testMerge() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();

        primary.add(NonNull.class.getAnnotation(NonNull.class));
        secondary.add(Nullable.class.getAnnotation(Nullable.class));

        AnnotationMap mergedMap = AnnotationMap.merge(primary, secondary);
        assertNotNull(mergedMap.get(NonNull.class));
        assertNotNull(mergedMap.get(Nullable.class));
    }

    @Test
    public void testSize() {
        AnnotationMap map = new AnnotationMap();
        assertEquals(0, map.size());

        map.add(Test.class.getAnnotation(Test.class));
        assertEquals(1, map.size());
    }

    @Test
    public void testAddIfNotPresent() {
        AnnotationMap map = new AnnotationMap();
        assertTrue(map.addIfNotPresent(Test.class.getAnnotation(Test.class)));
        assertFalse(map.addIfNotPresent(Test.class.getAnnotation(Test.class)));
    }

    @Test
    public void testAdd() {
        AnnotationMap map = new AnnotationMap();
        assertTrue(map.add(Test.class.getAnnotation(Test.class)));
        assertFalse(map.add(Test.class.getAnnotation(Test.class)));
    }

    @Test
    public void testToString() {
        AnnotationMap map = new AnnotationMap();
        assertEquals("[null]", map.toString());

        map.add(Test.class.getAnnotation(Test.class));
        assertEquals("{interface org.junit.Test=@org.junit.Test(timeout=0, expected=class org.junit.Test$None)}", map.toString());
    }
}