
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setUp() {
        baseSettings = new BaseSettings(
                new ClassIntrospector(),
                new AnnotationIntrospector(),
                new VisibilityChecker(),
                new PropertyNamingStrategy(),
                new TypeFactory(),
                new TypeResolverBuilder(),
                new DateFormat(),
                new HandlerInstantiator(),
                new Locale(),
                TimeZone.getDefault(),
                new Base64Variant()
        );
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector newIntrospector = new ClassIntrospector();
        BaseSettings newSettings = baseSettings.withClassIntrospector(newIntrospector);

        assertNotSame(baseSettings, newSettings);
        assertSame(newIntrospector, newSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector newIntrospector = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withAnnotationIntrospector(newIntrospector);

        assertNotSame(baseSettings, newSettings);
        assertSame(newIntrospector, newSettings.getAnnotationIntrospector());
    }

    // ... continued for other methods

    // Add test cases for all other methods

}

