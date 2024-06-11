import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        // Arrange
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder();
        DateFormat dateFormat = new DateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = new Locale();
        TimeZone tz = new TimeZone();
        Base64Variant defaultBase64 = new Base64Variant();
        
        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
        
        // Act
        BaseSettings result = baseSettings.withClassIntrospector(new ClassIntrospector());
        
        // Assert
        assertNotSame(baseSettings, result);
        assertNotNull(result);
        assertEquals(result.getClassIntrospector(), new ClassIntrospector());
    }
    
    @Test
    public void testWithAnnotationIntrospector() {
        // Arrange
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder();
        DateFormat dateFormat = new DateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = new Locale();
        TimeZone tz = new TimeZone();
        Base64Variant defaultBase64 = new Base64Variant();
        
        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
        
        // Act
        BaseSettings result = baseSettings.withAnnotationIntrospector(new AnnotationIntrospector());
        
        // Assert
        assertNotSame(baseSettings, result);
        assertNotNull(result);
        assertEquals(result.getAnnotationIntrospector(), new AnnotationIntrospector());
    }

    // ... continue with other test methods for each method in BaseSettings class ...

}