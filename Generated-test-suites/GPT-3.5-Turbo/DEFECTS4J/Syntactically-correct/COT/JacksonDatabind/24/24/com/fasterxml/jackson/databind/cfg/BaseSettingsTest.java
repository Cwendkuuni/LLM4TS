import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings settings = new BaseSettings();
        BaseSettings result = settings.withClassIntrospector(ci);
        assertEquals(ci, result.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings settings = new BaseSettings();
        BaseSettings result = settings.withAnnotationIntrospector(ai);
        assertEquals(ai, result.getAnnotationIntrospector());
    }

    // tests for other methods...

}