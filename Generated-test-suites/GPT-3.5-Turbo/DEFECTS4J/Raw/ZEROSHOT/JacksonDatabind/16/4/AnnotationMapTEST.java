import org.junit.Before;
import org.junit.Test;
import java.lang.annotation.Annotation;
import java.util.*;
import static org.junit.Assert.*;

public class AnnotationMapTest {
    
    private AnnotationMap annotationMap;
    
    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }
    
    @Test
    public void testGet_ReturnsNullWhenAnnotationsIsNull() {
        assertNull(annotationMap.get(SomeAnnotation.class));
    }
    
    @Test
    public void testGet_ReturnsNullWhenAnnotationDoesNotExist() {
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(OtherAnnotation.class, new OtherAnnotationImpl());
        annotationMap = new AnnotationMap(annotations);
        
        assertNull(annotationMap.get(SomeAnnotation.class));
    }
    
    @Test
    public void testGet_ReturnsAnnotationWhenAnnotationExists() {
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(SomeAnnotation.class, new SomeAnnotationImpl());
        annotationMap = new AnnotationMap(annotations);
        
        SomeAnnotation result = annotationMap.get(SomeAnnotation.class);
        
        assertNotNull(result);
        assertEquals(SomeAnnotationImpl.class, result.annotationType());
    }
    
    @Test
    public void testAnnotations_ReturnsEmptyListWhenAnnotationsIsNull() {
        Iterable<Annotation> result = annotationMap.annotations();
        
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
    }
    
    @Test
    public void testAnnotations_ReturnsEmptyListWhenAnnotationsIsEmpty() {
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotationMap = new AnnotationMap(annotations);
        
        Iterable<Annotation> result = annotationMap.annotations();
        
        assertNotNull(result);
        assertFalse(result.iterator().hasNext());
    }
    
    @Test
    public void testAnnotations_ReturnsAnnotationsWhenAnnotationsExist() {
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(SomeAnnotation.class, new SomeAnnotationImpl());
        annotationMap = new AnnotationMap(annotations);
        
        Iterable<Annotation> result = annotationMap.annotations();
        
        assertNotNull(result);
        Iterator<Annotation> iterator = result.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(SomeAnnotationImpl.class, iterator.next().annotationType());
    }
    
    @Test
    public void testMerge_ReturnsSecondaryWhenPrimaryIsNull() {
        AnnotationMap primary = null;
        
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new SomeAnnotationImpl());
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        assertNotNull(result);
        SomeAnnotation someAnnotation = result.get(SomeAnnotation.class);
        assertNotNull(someAnnotation);
        assertEquals(SomeAnnotationImpl.class, someAnnotation.annotationType());
    }
    
    @Test
    public void testMerge_ReturnsPrimaryWhenSecondaryIsNull() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new SomeAnnotationImpl());
        
        AnnotationMap secondary = null;
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        assertNotNull(result);
        SomeAnnotation someAnnotation = result.get(SomeAnnotation.class);
        assertNotNull(someAnnotation);
        assertEquals(SomeAnnotationImpl.class, someAnnotation.annotationType());
    }
    
    @Test
    public void testMerge_ReturnsPrimaryWhenSecondaryIsEmpty() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new SomeAnnotationImpl());
        
        AnnotationMap secondary = new AnnotationMap();
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        assertNotNull(result);
        SomeAnnotation someAnnotation = result.get(SomeAnnotation.class);
        assertNotNull(someAnnotation);
        assertEquals(SomeAnnotationImpl.class, someAnnotation.annotationType());
    }
    
    @Test
    public void testMerge_ReturnsSecondaryWhenPrimaryIsEmpty() {
        AnnotationMap primary = new AnnotationMap();
        
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new SomeAnnotationImpl());
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        assertNotNull(result);
        SomeAnnotation someAnnotation = result.get(SomeAnnotation.class);
        assertNotNull(someAnnotation);
        assertEquals(SomeAnnotationImpl.class, someAnnotation.annotationType());
    }
    
    @Test
    public void testMerge_ReturnsMergedAnnotations() {
        AnnotationMap primary = new AnnotationMap();
        primary.add(new SomeAnnotationImpl());
        
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(new OtherAnnotationImpl());
        
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        assertNotNull(result);
        SomeAnnotation someAnnotation = result.get(SomeAnnotation.class);
        assertNotNull(someAnnotation);
        assertEquals(SomeAnnotationImpl.class, someAnnotation.annotationType());
        
        OtherAnnotation otherAnnotation = result.get(OtherAnnotation.class);
        assertNotNull(otherAnnotation);
        assertEquals(OtherAnnotationImpl.class, otherAnnotation.annotationType());
    }
    
    @Test
    public void testSize_ReturnsZeroWhenAnnotationsIsNull() {
        assertEquals(0, annotationMap.size());
    }
    
    @Test
    public void testSize_ReturnsNumberOfAnnotationsWhenAnnotationsExist() {
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(SomeAnnotation.class, new SomeAnnotationImpl());
        annotationMap = new AnnotationMap(annotations);
        
        assertEquals(1, annotationMap.size());
    }
    
    @Test
    public void testAddIfNotPresent_ReturnsFalseWhenAnnotationsContainsAnnotation() {
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(SomeAnnotation.class, new SomeAnnotationImpl());
        annotationMap = new AnnotationMap(annotations);
        
        assertFalse(annotationMap.addIfNotPresent(new SomeAnnotationImpl()));
    }
    
    @Test
    public void testAddIfNotPresent_ReturnsTrueWhenAnnotationsDoesNotContainAnnotation() {
        assertTrue(annotationMap.addIfNotPresent(new SomeAnnotationImpl()));
    }
    
    @Test
    public void testAdd_ReturnsFalseWhenAnnotationsContainsAnnotation() {
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(SomeAnnotation.class, new SomeAnnotationImpl());
        annotationMap = new AnnotationMap(annotations);
        
        assertFalse(annotationMap.add(new SomeAnnotationImpl()));
    }
    
    @Test
    public void testAdd_ReturnsTrueWhenAnnotationsDoesNotContainAnnotation() {
        assertTrue(annotationMap.add(new SomeAnnotationImpl()));
    }
    
    @Test
    public void testToString_ReturnsNullWhenAnnotationsIsNull() {
        assertEquals("[null]", annotationMap.toString());
    }
    
    @Test
    public void testToString_ReturnsAnnotationsToStringWhenAnnotationsExist() {
        HashMap<Class<? extends Annotation>, Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();
        annotations.put(SomeAnnotation.class, new SomeAnnotationImpl());
        annotationMap = new AnnotationMap(annotations);
        
        assertEquals("{interface com.fasterxml.jackson.databind.introspect.SomeAnnotation=SomeAnnotationImpl}", annotationMap.toString());
    }
    
    private static interface SomeAnnotation extends Annotation {
    }
    
    private static class SomeAnnotationImpl implements SomeAnnotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return SomeAnnotation.class;
        }
        
        @Override
        public int hashCode() {
            return 0;
        }
        
        @Override
        public boolean equals(Object obj) {
            return obj instanceof SomeAnnotation;
        }
    }
    
    private static interface OtherAnnotation extends Annotation {
    }
    
    private static class OtherAnnotationImpl implements OtherAnnotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return OtherAnnotation.class;
        }
        
        @Override
        public int hashCode() {
            return 0;
        }
        
        @Override
        public boolean equals(Object obj) {
            return obj instanceof OtherAnnotation;
        }
    }
}