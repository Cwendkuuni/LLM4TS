import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testConstructorAndGetters() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = new Locale("en_US");
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Base64Variant base64 = new Base64Variant();

        BaseSettings bs = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, base64);

        assertEquals(ci, bs.getClassIntrospector());
        assertEquals(ai, bs.getAnnotationIntrospector());
        assertEquals(vc, bs.getVisibilityChecker());
        assertEquals(pns, bs.getPropertyNamingStrategy());
        assertEquals(tf, bs.getTypeFactory());
        assertEquals(typer, bs.getTypeResolverBuilder());
        assertEquals(dateFormat, bs.getDateFormat());
        assertEquals(hi, bs.getHandlerInstantiator());
        assertEquals(locale, bs.getLocale());
        assertEquals(timeZone, bs.getTimeZone());
        assertEquals(base64, bs.getBase64Variant());
    }

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
        Locale locale = new Locale("en_US");
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Base64Variant base64 = new Base64Variant();

        BaseSettings bs = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, base64);
        ClassIntrospector newCi = new ClassIntrospector();

        BaseSettings newBs = bs.withClassIntrospector(newCi);

        assertEquals(newCi, newBs.getClassIntrospector());
    }

    // More tests for each method in the BaseSettings class...

}