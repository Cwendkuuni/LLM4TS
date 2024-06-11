import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = new Base64Variant();
        
        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);
        BaseSettings newSettings = baseSettings.withClassIntrospector(new ClassIntrospector());
        
        assertEquals(newSettings.getClassIntrospector(), new ClassIntrospector());
        assertEquals(newSettings.getAnnotationIntrospector(), baseSettings.getAnnotationIntrospector());
        assertEquals(newSettings.getVisibilityChecker(), baseSettings.getVisibilityChecker());
        assertEquals(newSettings.getPropertyNamingStrategy(), baseSettings.getPropertyNamingStrategy());
        assertEquals(newSettings.getTypeFactory(), baseSettings.getTypeFactory());
        assertEquals(newSettings.getTypeResolverBuilder(), baseSettings.getTypeResolverBuilder());
        assertEquals(newSettings.getDateFormat(), baseSettings.getDateFormat());
        assertEquals(newSettings.getHandlerInstantiator(), baseSettings.getHandlerInstantiator());
        assertEquals(newSettings.getLocale(), baseSettings.getLocale());
        assertEquals(newSettings.getTimeZone(), baseSettings.getTimeZone());
        assertEquals(newSettings.getBase64Variant(), baseSettings.getBase64Variant());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = new Base64Variant();
        
        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);
        BaseSettings newSettings = baseSettings.withAnnotationIntrospector(new AnnotationIntrospector());
        
        assertEquals(newSettings.getClassIntrospector(), baseSettings.getClassIntrospector());
        assertEquals(newSettings.getAnnotationIntrospector(), new AnnotationIntrospector());
        assertEquals(newSettings.getVisibilityChecker(), baseSettings.getVisibilityChecker());
        assertEquals(newSettings.getPropertyNamingStrategy(), baseSettings.getPropertyNamingStrategy());
        assertEquals(newSettings.getTypeFactory(), baseSettings.getTypeFactory());
        assertEquals(newSettings.getTypeResolverBuilder(), baseSettings.getTypeResolverBuilder());
        assertEquals(newSettings.getDateFormat(), baseSettings.getDateFormat());
        assertEquals(newSettings.getHandlerInstantiator(), baseSettings.getHandlerInstantiator());
        assertEquals(newSettings.getLocale(), baseSettings.getLocale());
        assertEquals(newSettings.getTimeZone(), baseSettings.getTimeZone());
        assertEquals(newSettings.getBase64Variant(), baseSettings.getBase64Variant());
    }

    // Add more test cases for the remaining methods
    
}