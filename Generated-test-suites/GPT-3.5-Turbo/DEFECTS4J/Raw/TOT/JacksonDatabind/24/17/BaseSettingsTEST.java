import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        // Typical use case: change the class introspector
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings result = settings.withClassIntrospector(ci);
        assertNotNull(result);
        assertNotSame(settings, result);
        assertEquals(ci, result.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        // Typical use case: change the annotation introspector
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings result = settings.withAnnotationIntrospector(ai);
        assertNotNull(result);
        assertNotSame(settings, result);
        assertEquals(ai, result.getAnnotationIntrospector());
    }

    @Test
    public void testWithVisibilityChecker() {
        // Typical use case: change the visibility checker
        VisibilityChecker<?> vc = new VisibilityChecker();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings result = settings.withVisibilityChecker(vc);
        assertNotNull(result);
        assertNotSame(settings, result);
        assertEquals(vc, result.getVisibilityChecker());
    }

    // Additional test cases for each method...


}