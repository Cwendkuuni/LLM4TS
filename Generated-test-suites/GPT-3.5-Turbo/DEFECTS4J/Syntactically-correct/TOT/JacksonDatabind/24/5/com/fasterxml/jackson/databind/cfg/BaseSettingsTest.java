import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSettingsTest {
    
    @Test
    public void testWithClassIntrospector() {
        // Test typical use case
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null,
                                                null, null, null, null, null);
        BaseSettings result = settings.withClassIntrospector(ci);
        assertSame(ci, result.getClassIntrospector());
        
        // Test edge case with same input
        result = settings.withClassIntrospector(settings.getClassIntrospector());
        assertSame(settings.getClassIntrospector(), result.getClassIntrospector());
    }
    
    @Test
    public void testWithAnnotationIntrospector() {
        // Test typical use case
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null,
                                                null, null, null, null, null);
        BaseSettings result = settings.withAnnotationIntrospector(ai);
        assertSame(ai, result.getAnnotationIntrospector());
        
        // Test edge case with same input
        result = settings.withAnnotationIntrospector(settings.getAnnotationIntrospector());
        assertSame(settings.getAnnotationIntrospector(), result.getAnnotationIntrospector());
    }
    
    // Repeat the same pattern for other methods
    
    // ... Following methods to test other methods of BaseSettings class
    
}