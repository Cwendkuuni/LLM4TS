import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        ClassIntrospector classIntrospector = new ClassIntrospector();
        BaseSettings updatedSettings = baseSettings.withClassIntrospector(classIntrospector);
        assertSame(classIntrospector, updatedSettings.getClassIntrospector());
        assertNotSame(baseSettings, updatedSettings);
    }

    @Test
    public void testWithAnnotationIntrospector() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        BaseSettings updatedSettings = baseSettings.withAnnotationIntrospector(annotationIntrospector);
        assertSame(annotationIntrospector, updatedSettings.getAnnotationIntrospector());
        assertNotSame(baseSettings, updatedSettings);
    }
    
    // Add more test cases for the other methods in the BaseSettings class
    
    // Test edge cases and exception handling scenarios
    @Test
    public void testWithNullTimeZone() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        try {
            baseSettings.with(null);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithNullBase64Variant() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        try {
            baseSettings.with(null);
            fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}

