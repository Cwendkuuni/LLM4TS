import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        BaseSettings baseSettings = new BaseSettings(new ClassIntrospectorImpl(), new AnnotationIntrospectorImpl(),
                new VisibilityCheckerImpl(), new PropertyNamingStrategyImpl(), new TypeFactoryImpl(),
                new TypeResolverBuilderImpl(), new DateFormatImpl(), new HandlerInstantiatorImpl(), Locale.US, TimeZone.getDefault(), Base64Variant.STANDARD);

        // Test with null ClassIntrospector
        try {
            BaseSettings newBaseSettings = baseSettings.withClassIntrospector(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test with the same ClassIntrospector
        BaseSettings newBaseSettings = baseSettings.withClassIntrospector(new ClassIntrospectorImpl());
        assertNotSame(baseSettings, newBaseSettings);

        // Test if the new BaseSettings object is created with the new ClassIntrospector
        ClassIntrospector newClassIntrospector = new ClassIntrospectorImpl();
        BaseSettings newNewBaseSettings = newBaseSettings.withClassIntrospector(newClassIntrospector);
        assertEquals(newClassIntrospector, newNewBaseSettings._classIntrospector);
    }

    @Test
    public void testWithAnnotationIntrospector() {
        // ... tests for the withAnnotationIntrospector method
    }

    // ... tests for other methods

    @Test
    public void testWithHandlerInstantiator() {
        // ... tests for the withHandlerInstantiator method
    }

    @Test
    public void testWithLocale() {
        // ... tests for the withLocale method
    }

    @Test
    public void testWithTimeZone() {
        // ... tests for the withTimeZone method
    }

    @Test
    public void testWithBase64Variant() {
        // ... tests for the withBase64Variant method
    }
}