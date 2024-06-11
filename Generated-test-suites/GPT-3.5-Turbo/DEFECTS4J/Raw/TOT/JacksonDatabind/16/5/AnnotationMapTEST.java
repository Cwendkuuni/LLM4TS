import org.junit.Before;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.List;
import static org.junit.Assert.*;

public class AnnotationMapTest {
    
    private AnnotationMap annotationMap;
    private AnnotationMap emptyAnnotationMap;
    private AnnotationMap mergedAnnotationMap;
    
    @Before
    public void setup() {
        annotationMap = new AnnotationMap();
        emptyAnnotationMap = new AnnotationMap();
        
        emptyAnnotationMap.addIfNotPresent(new MyAnnotation("Value1"));
        emptyAnnotationMap.addIfNotPresent(new MyAnnotation("Value2"));
        
        annotationMap.addIfNotPresent(new MyAnnotation("Value3"));
        annotationMap.addIfNotPresent(new MyAnnotation("Value4"));
        
        mergedAnnotationMap = AnnotationMap.merge(annotationMap, emptyAnnotationMap);
    }
    
    @Test
    public void testGet_ReturnsValueIfExists() {
        MyAnnotation annotation = annotationMap.get(MyAnnotation.class);
        assertNotNull(annotation);
        assertEquals("Value3", annotation.getValue());
    }
    
    @Test
    public void testGet_ReturnsNullIfNotExists() {
        MyNonExistentAnnotation annotation = annotationMap.get(MyNonExistentAnnotation.class);
        assertNull(annotation);
    }
    
    @Test
    public void testAnnotations_ReturnsEmptyListIfEmpty() {
        List<Annotation> annotations = emptyAnnotationMap.annotations();
        assertTrue(annotations.isEmpty());
    }
    
    @Test
    public void testAnnotations_ReturnsListOfAnnotations() {
        List<Annotation> annotations = mergedAnnotationMap.annotations();
        assertFalse(annotations.isEmpty());
        assertEquals(4, annotations.size());
    }
    
    @Test
    public void testMerge_ReturnsPrimaryIfSecondaryIsNull() {
        AnnotationMap resultMap = AnnotationMap.merge(annotationMap, null);
        assertSame(annotationMap, resultMap);
    }
    
    @Test
    public void testMerge_ReturnsSecondaryIfPrimaryIsNull() {
        AnnotationMap resultMap = AnnotationMap.merge(null, emptyAnnotationMap);
        assertSame(emptyAnnotationMap, resultMap);
    }
    
    @Test
    public void testMerge_ReturnsMergedAnnotations() {
        AnnotationMap resultMap = AnnotationMap.merge(annotationMap, emptyAnnotationMap);
        assertEquals(4, resultMap.size());
    }
    
    @Test
    public void testSize_ReturnsZeroIfEmpty() {
        int size = emptyAnnotationMap.size();
        assertEquals(0, size);
    }
    
    @Test
    public void testSize_ReturnsNonZeroIfNotEmpty() {
        int size = annotationMap.size();
        assertTrue(size > 0);
    }
    
    @Test
    public void testAddIfNotPresent_AddsAnnotationIfNotPresent() {
        boolean added = annotationMap.addIfNotPresent(new MyAnnotation("Value5"));
        assertTrue(added);
        assertEquals(3, annotationMap.size());
    }
    
    @Test
    public void testAddIfNotPresent_DoesNotAddAnnotationIfAlreadyPresent() {
        boolean added = annotationMap.addIfNotPresent(new MyAnnotation("Value3"));
        assertFalse(added);
        assertEquals(2, annotationMap.size());
    }
    
    @Test
    public void testAdd_AddsAnnotation() {
        boolean added = annotationMap.add(new MyAnnotation("Value6"));
        assertTrue(added);
        assertEquals(3, annotationMap.size());
    }
    
    @Test
    public void testAdd_ReturnsFalseIfAlreadyPresent() {
        boolean added = annotationMap.add(new MyAnnotation("Value4"));
        assertFalse(added);
        assertEquals(2, annotationMap.size());
    }
    
    @Test
    public void testToString_ReturnsNonNullValue() {
        String stringValue = annotationMap.toString();
        assertNotNull(stringValue);
    }
    
    class MyAnnotation implements Annotation {
        private String value;
        public MyAnnotation(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
        @Override
        public Class<? extends Annotation> annotationType() {
            return MyAnnotation.class;
        }
    }
    
    class MyNonExistentAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return MyNonExistentAnnotation.class;
        }
    }
}