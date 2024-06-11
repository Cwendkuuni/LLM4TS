import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

public class AnnotationMapTest 
{
    private AnnotationMap annotationMap;
    private Annotation annotation;

    @Before
    public void setUp() 
    {
        annotationMap = new AnnotationMap();
        annotation = new Annotation() 
        {
            @Override
            public Class<? extends Annotation> annotationType() 
            {
                return Annotation.class;
            }
        };
    }

    @Test
    public void testGet_WithNullAnnotations_ReturnsNull() 
    {
        assertNull(annotationMap.get(Annotation.class));
    }

    @Test
    public void testGet_WithExistingAnnotation_ReturnsAnnotation() 
    {
        annotationMap.add(annotation);
        assertEquals(annotation, annotationMap.get(Annotation.class));
    }

    @Test
    public void testAnnotations_WithNullAnnotations_ReturnsEmptyList() 
    {
        Iterable<Annotation> annotations = annotationMap.annotations();
        assertFalse(annotations.iterator().hasNext());
    }

    @Test
    public void testAnnotations_WithAnnotations_ReturnsNonEmptyList() 
    {
        annotationMap.add(annotation);
        Iterable<Annotation> annotations = annotationMap.annotations();
        assertTrue(annotations.iterator().hasNext());
    }

    @Test
    public void testSize_WithNullAnnotations_ReturnsZero() 
    {
        assertEquals(0, annotationMap.size());
    }

    @Test
    public void testSize_WithAnnotations_ReturnsCorrectSize() 
    {
        annotationMap.add(annotation);
        assertEquals(1, annotationMap.size());
    }

    @Test
    public void testMerge_WithNullPrimary_ReturnsSecondary() 
    {
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(annotation);
        AnnotationMap result = AnnotationMap.merge(null, secondary);
        assertEquals(1, result.size());
        assertEquals(annotation, result.get(Annotation.class));
    }

    @Test
    public void testMerge_WithNullSecondary_ReturnsPrimary() 
    {
        annotationMap.add(annotation);
        AnnotationMap result = AnnotationMap.merge(annotationMap, null);
        assertEquals(1, result.size());
        assertEquals(annotation, result.get(Annotation.class));
    }

    @Test
    public void testMerge_WithBothPrimaryAndSecondary_ReturnsMergedAnnotations() 
    {
        annotationMap.add(annotation);
        Annotation secondaryAnnotation = new Annotation() 
        {
            @Override
            public Class<? extends Annotation> annotationType() 
            {
                return Annotation.class;
            }
        };
        AnnotationMap secondary = new AnnotationMap();
        secondary.add(secondaryAnnotation);
        AnnotationMap result = AnnotationMap.merge(annotationMap, secondary);
        assertEquals(2, result.size());
        assertEquals(annotation, result.get(Annotation.class));
        assertEquals(secondaryAnnotation, result.get(secondaryAnnotation.annotationType()));
    }

    @Test
    public void testAddIfNotPresent_WithNullAnnotations_ReturnsTrue() 
    {
        assertTrue(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAddIfNotPresent_WithExistingAnnotation_ReturnsFalse() 
    {
        annotationMap.add(annotation);
        assertFalse(annotationMap.addIfNotPresent(annotation));
    }

    @Test
    public void testAdd_WithAnnotation_ReturnsTrue() 
    {
        assertTrue(annotationMap.add(annotation));
    }

    @Test
    public void testToString_WithNullAnnotations_ReturnsNullStringRepresentation() 
    {
        assertEquals("[null]", annotationMap.toString());
    }

    @Test
    public void testToString_WithAnnotations_ReturnsCorrectStringRepresentation() 
    {
        annotationMap.add(annotation);
        assertEquals(annotationMap._annotations.toString(), annotationMap.toString());
    }
}