import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    @Test
    public void testConstruction() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker vc = new VisibilityChecker();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.US;
        TimeZone tz = TimeZone.getTimeZone("GMT");
        Base64Variant defaultBase64 = new Base64Variant();

        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);

        assertNotNull(baseSettings);
        assertEquals(ci, baseSettings.getClassIntrospector());
        assertEquals(ai, baseSettings.getAnnotationIntrospector());
        assertEquals(vc, baseSettings.getVisibilityChecker());
        assertEquals(pns, baseSettings.getPropertyNamingStrategy());
        assertEquals(tf, baseSettings.getTypeFactory());
        assertEquals(typer, baseSettings.getTypeResolverBuilder());
        assertEquals(dateFormat, baseSettings.getDateFormat());
        assertEquals(hi, baseSettings.getHandlerInstantiator());
        assertEquals(locale, baseSettings.getLocale());
        assertEquals(tz, baseSettings.getTimeZone());
        assertEquals(defaultBase64, baseSettings.getBase64Variant());
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci1 = new ClassIntrospector();
        ClassIntrospector ci2 = new ClassIntrospector();

        BaseSettings baseSettings = new BaseSettings(ci1, new AnnotationIntrospector(), new VisibilityChecker(),
                                                     new PropertyNamingStrategy(), new TypeFactory(), new TypeResolverBuilder<>(),
                                                     new SimpleDateFormat("yyyy-MM-dd"), new HandlerInstantiator(), Locale.US,
                                                     TimeZone.getTimeZone("GMT"), new Base64Variant());

        BaseSettings newSettings = baseSettings.withClassIntrospector(ci2);
        
        assertNotEquals(baseSettings, newSettings);
        assertEquals(ci2, newSettings.getClassIntrospector());
    }

    // Add more test cases for other methods as needed

}