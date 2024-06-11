
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {
    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withClassIntrospector(ci);
        assertEquals(ci, newSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withAnnotationIntrospector(ai);
        assertEquals(ai, newSettings.getAnnotationIntrospector());
    }
    
    // Add more test cases for other methods
    
}
