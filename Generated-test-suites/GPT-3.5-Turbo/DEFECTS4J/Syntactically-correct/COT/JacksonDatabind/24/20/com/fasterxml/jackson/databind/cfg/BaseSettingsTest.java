import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = baseSettings.withClassIntrospector(ci);
        assertNotSame(baseSettings, newSettings);
        assertEquals(ci, newSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = baseSettings.withAnnotationIntrospector(ai);
        assertNotSame(baseSettings, newSettings);
        assertEquals(ai, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = baseSettings.withInsertedAnnotationIntrospector(ai);
        assertNotSame(baseSettings, newSettings);
        assertEquals(ai, newSettings.getAnnotationIntrospector());
    }
    
    // ...  Rest of the test cases needed for all the methods
    
}