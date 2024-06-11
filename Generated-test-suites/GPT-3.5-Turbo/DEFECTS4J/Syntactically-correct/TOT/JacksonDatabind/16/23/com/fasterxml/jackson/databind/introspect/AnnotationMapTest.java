import static org.junit.Assert.*;
import org.junit.Test;

public class AnnotationMapTest {

    @Test
    public void testGet_ExistingAnnotation_ReturnsAnnotation() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation1 annotation1 = new MyAnnotation1();
        am.add(annotation1);
        
        MyAnnotation1 result = am.get(MyAnnotation1.class);
        
        assertEquals(annotation1, result);
    }
    
    @Test
    public void testGet_NonExistingAnnotation_ReturnsNull() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation1 annotation1 = new MyAnnotation1();
        am.add(annotation1);
        
        MyAnnotation2 result = am.get(MyAnnotation2.class);
        
        assertNull(result);
    }
    
    @Test
    public void testAnnotations_EmptyAnnotationMap_ReturnsEmptyList() {
        AnnotationMap am = new AnnotationMap();
        
        Iterable<Annotation> result = am.annotations();
        
        assertFalse(result.iterator().hasNext());
    }
    
    @Test
    public void testAnnotations_NonEmptyAnnotationMap_ReturnsCorrectList() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation1 annotation1 = new MyAnnotation1();
        MyAnnotation2 annotation2 = new MyAnnotation2();
        am.add(annotation1);
        am.add(annotation2);

        Iterable<Annotation> result = am.annotations();

        ArrayList<Annotation> expected = new ArrayList<Annotation>();
        expected.add(annotation1);
        expected.add(annotation2);
        
        for (Annotation a : result) {
            assertTrue(expected.contains(a));
        }
    }
    
    @Test
    public void testMerge_BothEmptyAnnotationMaps_ReturnsNull() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        assertNull(result);
    }
    
    @Test
    public void testMerge_PrimaryEmptySecondaryNotEmpty_ReturnsSecondary() {
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        MyAnnotation1 annotation1 = new MyAnnotation1();
        secondary.add(annotation1);
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        assertSame(secondary, result);
    }
    
    @Test
    public void testMerge_PrimaryNotEmptySecondaryEmpty_ReturnsPrimary() {
        AnnotationMap primary = new AnnotationMap();
        MyAnnotation1 annotation1 = new MyAnnotation1();
        primary.add(annotation1);
        AnnotationMap secondary = new AnnotationMap();
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        assertSame(primary, result);
    }

    @Test
    public void testMerge_BothNotEmpty_ReturnsMergedAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        MyAnnotation1 primaryAnnotation1 = new MyAnnotation1();
        primary.add(primaryAnnotation1);
        MyAnnotation2 primaryAnnotation2 = new MyAnnotation2();
        primary.add(primaryAnnotation2);
        
        AnnotationMap secondary = new AnnotationMap();
        MyAnnotation2 secondaryAnnotation2 = new MyAnnotation2();
        secondary.add(secondaryAnnotation2);
        MyAnnotation3 secondaryAnnotation3 = new MyAnnotation3();
        secondary.add(secondaryAnnotation3);
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        assertNotSame(primary, result);
        assertNotSame(secondary, result);
        
        assertEquals(primaryAnnotation1, result.get(MyAnnotation1.class));
        assertEquals(primaryAnnotation2, result.get(MyAnnotation2.class));
        assertEquals(secondaryAnnotation3, result.get(MyAnnotation3.class));
    }
    
    @Test
    public void testSize_EmptyAnnotationMap_ReturnsZero() {
        AnnotationMap am = new AnnotationMap();
        
        int result = am.size();
        
        assertEquals(0, result);
    }
    
    @Test
    public void testSize_NonEmptyAnnotationMap_ReturnsCorrectSize() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation1 annotation1 = new MyAnnotation1();
        MyAnnotation2 annotation2 = new MyAnnotation2();
        am.add(annotation1);
        am.add(annotation2);
        
        int result = am.size();
        
        assertEquals(2, result);
    }
    
    @Test
    public void testAddIfNotPresent_AnnotationNotPresent_ReturnsTrue() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation1 annotation1 = new MyAnnotation1();
        
        boolean result = am.addIfNotPresent(annotation1);
        
        assertTrue(result);
    }
    
    @Test
    public void testAddIfNotPresent_AnnotationAlreadyPresent_ReturnsFalse() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation1 annotation1 = new MyAnnotation1();
        am.add(annotation1);
        
        boolean result = am.addIfNotPresent(annotation1);
        
        assertFalse(result);
    }
    
    @Test
    public void testAdd_AddAnnotation_ReturnsTrue() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation1 annotation1 = new MyAnnotation1();
        
        boolean result = am.add(annotation1);
        
        assertTrue(result);
    }
    
    @Test
    public void testAdd_AddExistingAnnotation_ReturnsFalse() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation1 annotation1 = new MyAnnotation1();
        am.add(annotation1);
        
        boolean result = am.add(annotation1);
        
        assertFalse(result);
    }
    
    @Test
    public void testToString_EmptyAnnotationMap_ReturnsNullString() {
        AnnotationMap am = new AnnotationMap();
        
        String result = am.toString();
        
        assertEquals("[null]", result);
    }
    
    @Test
    public void testToString_NonEmptyAnnotationMap_ReturnsCorrectString() {
        AnnotationMap am = new AnnotationMap();
        MyAnnotation1 annotation1 = new MyAnnotation1();
        MyAnnotation2 annotation2 = new MyAnnotation2();
        am.add(annotation1);
        am.add(annotation2);
        
        String result = am.toString();
        
        assertTrue(result.contains(annotation1.toString()));
        assertTrue(result.contains(annotation2.toString()));
    }
}