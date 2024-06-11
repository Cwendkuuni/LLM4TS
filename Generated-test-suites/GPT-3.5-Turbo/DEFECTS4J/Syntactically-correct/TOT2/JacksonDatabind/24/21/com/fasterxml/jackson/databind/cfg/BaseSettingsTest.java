import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        // Test typical use case
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withClassIntrospector(new ClassIntrospector());
        assertNotNull(newBaseSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        // Test typical use case
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withAnnotationIntrospector(new AnnotationIntrospector());
        assertNotNull(newBaseSettings.getAnnotationIntrospector());
    }

    // Add test methods for all other public methods in BaseSettings class

}