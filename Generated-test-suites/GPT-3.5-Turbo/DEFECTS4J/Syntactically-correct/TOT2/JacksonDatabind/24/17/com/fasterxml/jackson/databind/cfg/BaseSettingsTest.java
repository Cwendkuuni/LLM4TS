import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BaseSettingsTest {
    
    @Test
    public void testWithClassIntrospector() {
        // Test for setting new ClassIntrospector
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = baseSettings.withClassIntrospector(ci);
        assertEquals(ci, newSettings.getClassIntrospector());
    }
    
    @Test
    public void testWithAnnotationIntrospector() {
        // Test for setting new AnnotationIntrospector
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = baseSettings.withAnnotationIntrospector(ai);
        assertEquals(ai, newSettings.getAnnotationIntrospector());
    }
    
    // More test cases for other methods can be added here
    
}