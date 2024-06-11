import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.lang.annotation.Annotation;

public class AnnotationMapTest {
    
    private AnnotationMap annotationMap;

    @Before
    public void setUp() {
        annotationMap = new AnnotationMap();
    }

    @Test
    public void testGet_WhenAnnotationExists_ShouldReturnAnnotation() {
        // Arrange
        TestAnnotation testAnn = new TestAnnotation();
        annotationMap.add(testAnn);
        
        // Act
        TestAnnotation result = annotationMap.get(TestAnnotation.class);
        
        // Assert
        assertEquals(testAnn, result);
    }
    
    @Test
    public void testGet_WhenAnnotationDoesNotExist_ShouldReturnNull() {
        // Act
        TestAnnotation result = annotationMap.get(TestAnnotation.class);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testAnnotations_WhenEmptyAnnotationMap_ShouldReturnEmptyList() {
        // Act
        Iterable<Annotation> result = annotationMap.annotations();

        // Assert
        assertFalse(result.iterator().hasNext());
    }
    
    @Test
    public void testAnnotations_WhenNonEmptyAnnotationMap_ShouldReturnListOfAnnotations() {
        // Arrange
        TestAnnotation testAnn1 = new TestAnnotation();
        TestAnnotation testAnn2 = new TestAnnotation();
        annotationMap.add(testAnn1);
        annotationMap.add(testAnn2);
        
        // Act
        Iterable<Annotation> result = annotationMap.annotations();
        
        // Assert
        assertTrue(result.iterator().hasNext());
        assertEquals(testAnn1, result.iterator().next());
    }
    
    @Test
    public void testMerge_WhenBothAnnotationMapsAreNull_ShouldReturnNull() {
        // Act
        AnnotationMap result = AnnotationMap.merge(null, null);
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testMerge_WhenPrimaryAnnotationMapIsNull_ShouldReturnSecondaryAnnotationMap() {
        // Arrange
        AnnotationMap secondary = new AnnotationMap();
        
        // Act
        AnnotationMap result = AnnotationMap.merge(null, secondary);
        
        // Assert
        assertEquals(secondary, result);
    }
    
    @Test
    public void testMerge_WhenSecondaryAnnotationMapIsNull_ShouldReturnPrimaryAnnotationMap() {
        // Arrange
        AnnotationMap primary = new AnnotationMap();
        
        // Act
        AnnotationMap result = AnnotationMap.merge(primary, null);
        
        // Assert
        assertEquals(primary, result);
    }
    
    @Test
    public void testMerge_WhenBothAnnotationMapsAreEmpty_ShouldReturnEmptyAnnotationMap() {
        // Arrange
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        
        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        // Assert
        assertTrue(result.size() == 0);
    }
    
    @Test
    public void testMerge_WhenBothAnnotationMapsHaveDifferentAnnotations_ShouldReturnMergedAnnotationMap() {
        // Arrange
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        TestAnnotation testAnn1 = new TestAnnotation();
        TestAnnotation testAnn2 = new TestAnnotation();
        primary.add(testAnn1);
        secondary.add(testAnn2);
        
        // Act
        AnnotationMap result = AnnotationMap.merge(primary, secondary);
        
        // Assert
        assertTrue(result.size() == 2);
        assertTrue(result.get(TestAnnotation.class) == testAnn2);
    }
    
    @Test
    public void testMerge_WhenPrimaryAnnotationMapOverridesSecondaryAnnotationMap_ShouldReturnMergedAnnotationMapWithPrimaryOverrides() {
        // Arrange
        AnnotationMap primary = new AnnotationMap();
        AnnotationMap secondary = new AnnotationMap();
        TestAnnotation testAnn1 = new TestAnnotation();
        TestAnnotation testAnn2 = new TestAnnotation();
        primary.add(testAnn1);
        secondary.add(testAnn2);
        
        // Act
        AnnotationMap result = AnnotationMap.merge(secondary, primary);
        
        // Assert
        assertTrue(result.size() == 1);
        assertTrue(result.get(TestAnnotation.class) == testAnn1);
    }
    
    @Test
    public void testSize_WhenAnnotationMapIsEmpty_ShouldReturnZero() {
        // Act
        int result = annotationMap.size();
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testSize_WhenAnnotationMapIsNotEmpty_ShouldReturnNumberOfAnnotations() {
        // Arrange
        TestAnnotation testAnn = new TestAnnotation();
        annotationMap.add(testAnn);
        
        // Act
        int result = annotationMap.size();
        
        // Assert
        assertEquals(1, result);
    }
    
    @Test
    public void testAddIfNotPresent_WhenAnnotationDoesNotExist_ShouldAddAnnotationAndReturnTrue() {
        // Arrange
        TestAnnotation testAnn = new TestAnnotation();
        
        // Act
        boolean result = annotationMap.addIfNotPresent(testAnn);
        
        // Assert
        assertTrue(result);
        assertEquals(testAnn, annotationMap.get(TestAnnotation.class));
    }
    
    @Test
    public void testAddIfNotPresent_WhenAnnotationExists_ShouldNotAddAnnotationAndReturnFalse() {
        // Arrange
        TestAnnotation testAnn = new TestAnnotation();
        annotationMap.add(testAnn);
        
        // Act
        boolean result = annotationMap.addIfNotPresent(testAnn);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testAdd_WhenAnnotationDoesNotExist_ShouldAddAnnotationAndReturnTrue() {
        // Arrange
        TestAnnotation testAnn = new TestAnnotation();
        
        // Act
        boolean result = annotationMap.add(testAnn);
        
        // Assert
        assertTrue(result);
        assertEquals(testAnn, annotationMap.get(TestAnnotation.class));
    }
    
    @Test
    public void testAdd_WhenAnnotationExists_ShouldAddAnnotationAndReturnTrue() {
        // Arrange
        TestAnnotation testAnn1 = new TestAnnotation();
        TestAnnotation testAnn2 = new TestAnnotation();
        annotationMap.add(testAnn1);
        
        // Act
        boolean result = annotationMap.add(testAnn2);
        
        // Assert
        assertTrue(result);
        assertEquals(testAnn2, annotationMap.get(TestAnnotation.class));
    }
    
    @Test
    public void testToString_WhenAnnotationMapIsEmpty_ShouldReturnStringRepresentationOfEmptyMap() {
        // Act
        String result = annotationMap.toString();
        
        // Assert
        assertEquals("[null]", result);
    }
    
    @Test
    public void testToString_WhenAnnotationMapIsNotEmpty_ShouldReturnStringRepresentationOfMap() {
        // Arrange
        TestAnnotation testAnn = new TestAnnotation();
        annotationMap.add(testAnn);
        
        // Act
        String result = annotationMap.toString();
        
        // Assert
        assertEquals("{class TestAnnotation=" + testAnn.toString() + "}", result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void test_add_NullAnnotationProvided_ShouldThrowIllegalArgumentException() {
        // Act
        annotationMap.add(null);
    }
    
    private static class TestAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() {
            return TestAnnotation.class;
        }
    }

}