import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        // Create instance of BaseSettings
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker vc = new VisibilityChecker();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder typer = new TypeResolverBuilder();
        DateFormat dateFormat = new DateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = new Locale();
        TimeZone tz = new TimeZone();
        Base64Variant defaultBase64 = new Base64Variant();
        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
        
        // Test the withClassIntrospector method
        ClassIntrospector newCi = new ClassIntrospector();
        BaseSettings newSettings = settings.withClassIntrospector(newCi);
        assertEquals(newCi, newSettings.getClassIntrospector());
        assertNotEquals(settings.getClassIntrospector(), newSettings.getClassIntrospector());
    }
    
    @Test
    public void testWithAnnotationIntrospector() {
        // Create instance of BaseSettings
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker vc = new VisibilityChecker();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder typer = new TypeResolverBuilder();
        DateFormat dateFormat = new DateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = new Locale();
        TimeZone tz = new TimeZone();
        Base64Variant defaultBase64 = new Base64Variant();
        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
        
        // Test the withAnnotationIntrospector method
        AnnotationIntrospector newAi = new AnnotationIntrospector();
        BaseSettings newSettings = settings.withAnnotationIntrospector(newAi);
        assertEquals(newAi, newSettings.getAnnotationIntrospector());
        assertNotEquals(settings.getAnnotationIntrospector(), newSettings.getAnnotationIntrospector());
    }
    
    // ... include tests for all other methods ...
    
}