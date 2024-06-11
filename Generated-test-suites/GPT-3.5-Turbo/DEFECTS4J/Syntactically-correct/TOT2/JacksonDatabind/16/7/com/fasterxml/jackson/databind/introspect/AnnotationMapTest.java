import org.junit.*;
import static org.junit.Assert.*;

public class AnnotationMapTest {

    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGet() {
        assertNull(annotationMap.get(Override.class));
    }

    @Test
    public void testAnnotations() {
        assertEquals(Collections.emptyList(), annotationMap.annotations());
    }

    @Test
    public void testSize() {
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testAddIfNotPresent() {
        Annotation ann = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        assertTrue(annotationMap.addIfNotPresent(ann));
        assertFalse(annotationMap.addIfNotPresent(ann));
    }

    @Test
    public void testAdd() {
        Annotation ann = new Annotation() {
            @Override
            public Class<? extends Annotation> annotationType() {
                return this.getClass();
            }
        };
        assertTrue(annotationMap.add(ann));
    }

    @Test
    public void testToString() {
        assertEquals("[null]", annotationMap.toString());
    }
}