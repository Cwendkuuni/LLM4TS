import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        // Given
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        
        // When
        BaseSettings result = baseSettings.withClassIntrospector(ci);
        
        // Then
        assertEquals(ci, result.getClassIntrospector());
    }
    
    @Test
    public void testWithAnnotationIntrospector() {
        // Given
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        
        // When
        BaseSettings result = baseSettings.withAnnotationIntrospector(ai);
        
        // Then
        assertEquals(ai, result.getAnnotationIntrospector());
    }
    
    @Test
    public void testWithInsertedAnnotationIntrospector() {
        // Given
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        
        // When
        BaseSettings result = baseSettings.withInsertedAnnotationIntrospector(ai);
        
        // Then
        assertNotNull(result.getAnnotationIntrospector());
    }

    // Other test cases for the rest of the methods

}