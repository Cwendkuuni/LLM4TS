import org.junit.*;
import static org.junit.Assert.*;
import java.lang.annotation.Annotation;

public class AnnotationMapTest {

    private AnnotationMap emptyMap;
    private AnnotationMap mapWithAnnotations;
    
    @Before
    public void setUp() {
        emptyMap = new AnnotationMap();
        
        mapWithAnnotations = new AnnotationMap();
        mapWithAnnotations.add(new TestAnnotation());
    }
    
    @Test
    public void testGet_ReturnsNull_WhenEmptyMap() {
        assertNull(emptyMap.get(TestAnnotation.class));
    }
    
    @Test
    public void testGet_ReturnsAnnotation_WhenAnnotationExists() {
        assertNotNull(mapWithAnnotations.get(TestAnnotation.class));
    }
    
    @Test
    public void testGet_ReturnsNull_WhenAnnotationDoesNotExist() {
        assertNull(mapWithAnnotations.get(DummyAnnotation.class));
    }
    
    @Test
    public void testAnnotations_ReturnsEmptyList_WhenEmptyMap() {
        assertTrue(emptyMap.annotations().isEmpty());
    }
    
    @Test
    public void testAnnotations_ReturnsNonEmptyList_WhenMapWithAnnotations() {
        assertFalse(mapWithAnnotations.annotations().isEmpty());
    }
    
    @Test
    public void testAnnotations_ReturnsAllAnnotations() {
        assertEquals(1, mapWithAnnotations.annotations().size());
    }
    
    @Test
    public void testSize_ReturnsZero_WhenEmptyMap() {
        assertEquals(0, emptyMap.size());
    }
    
    @Test
    public void testSize_ReturnsNonZero_WhenMapWithAnnotations() {
        assertTrue(mapWithAnnotations.size() > 0);
    }
    
    @Test
    public void testMerge_ReturnsSecondaryMap_WhenPrimaryMapNull() {
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new DummyAnnotation());
        
        AnnotationMap merged = AnnotationMap.merge(null, secondary);
        assertEquals(secondary, merged);
    }
    
    @Test
    public void testMerge_ReturnsPrimaryMap_WhenSecondaryMapNull() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new DummyAnnotation());
        
        AnnotationMap merged = AnnotationMap.merge(primary, null);
        assertEquals(primary, merged);
    }
    
    @Test
    public void testMerge_ReturnsMergedMap_WhenBothMapsNotEmpty() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new DummyAnnotation());
        
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new TestAnnotation());
        
        AnnotationMap merged = AnnotationMap.merge(primary, secondary);
        assertEquals(2, merged.size());
    }
    
    @Test
    public void testAddIfNotPresent_ReturnsTrue_WhenAnnotationNotPresent() {
        assertTrue(emptyMap.addIfNotPresent(new TestAnnotation()));
    }
    
    @Test
    public void testAddIfNotPresent_ReturnsFalse_WhenAnnotationAlreadyPresent() {
        assertFalse(mapWithAnnotations.addIfNotPresent(new TestAnnotation()));
    }
    
    @Test
    public void testAdd_ReturnsTrue_WhenAnnotationAdded() {
        assertTrue(emptyMap.add(new TestAnnotation()));
    }
    
    @Test
    public void testAdd_ReturnsFalse_WhenAnnotationAlreadyPresent() {
        assertFalse(mapWithAnnotations.add(new TestAnnotation()));
    }
    
    @Test
    public void testToString_ReturnsNonNullString() {
        assertNotNull(emptyMap.toString());
    }
    
    @Test
    public void testToString_ReturnsCorrectString() {
        assertEquals(mapWithAnnotations.toString(), mapWithAnnotations.annotations().toString());
    }
    
    private static class TestAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation.class;
        }
    }
    
    private static class DummyAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return DummyAnnotation.class;
        }
    }
}