import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null,
                null, null, null, null, null, null);
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings newSettings = settings.withClassIntrospector(ci);
        assertSame(ci, newSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null,
                null, null, null, null, null, null);
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings newSettings = settings.withAnnotationIntrospector(ai);
        assertSame(ai, newSettings.getAnnotationIntrospector());
    }

    // Add additional test methods for the remaining public methods in the BaseSettings class
}

