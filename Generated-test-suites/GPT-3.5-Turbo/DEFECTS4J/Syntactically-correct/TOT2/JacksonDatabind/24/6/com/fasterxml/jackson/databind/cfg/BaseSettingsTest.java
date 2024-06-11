import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        // Test typical use case
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = baseSettings.withClassIntrospector(ci);
        
        assertEquals(ci, newSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        // Test typical use case
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = baseSettings.withAnnotationIntrospector(ai);
        
        assertEquals(ai, newSettings.getAnnotationIntrospector());
    }

    // Additional test cases for each method can be added here

}