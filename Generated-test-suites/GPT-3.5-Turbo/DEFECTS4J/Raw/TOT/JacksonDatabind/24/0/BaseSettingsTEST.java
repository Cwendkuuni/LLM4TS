import com.fasterxml.jackson.databind.*;
import org.junit.Test;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        baseSettings = new BaseSettings(ci, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = baseSettings.withClassIntrospector(ci);

        assertSame(ci, newSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        baseSettings = new BaseSettings(null, ai, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = baseSettings.withAnnotationIntrospector(ai);

        assertSame(ai, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker checker = new VisibilityChecker();
        baseSettings = new BaseSettings(null, null, checker, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = baseSettings.withVisibilityChecker(checker);

        assertSame(checker, newSettings.getVisibilityChecker());
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy strategy = new PropertyNamingStrategy();
        baseSettings = new BaseSettings(null, null, null, strategy, null, null, null, null, null, null, null);
        BaseSettings newSettings = baseSettings.withPropertyNamingStrategy(strategy);

        assertSame(strategy, newSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory factory = new TypeFactory();
        baseSettings = new BaseSettings(null, null, null, null, factory, null, null, null, null, null, null);
        BaseSettings newSettings = baseSettings.withTypeFactory(factory);

        assertSame(factory, newSettings.getTypeFactory());
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder builder = new TypeResolverBuilder();
        baseSettings = new BaseSettings(null, null, null, null, null, builder, null, null, null, null, null);
        BaseSettings newSettings = baseSettings.withTypeResolverBuilder(builder);

        assertSame(builder, newSettings.getTypeResolverBuilder());
    }

    @Test
    public void testWithDateFormat() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        baseSettings = new BaseSettings(null, null, null, null, null, null, dateFormat, null, null, null, null);
        BaseSettings newSettings = baseSettings.withDateFormat(dateFormat);

        assertSame(dateFormat, newSettings.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator hi = new HandlerInstantiator();
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, hi, null, null, null);
        BaseSettings newSettings = baseSettings.withHandlerInstantiator(hi);

        assertSame(hi, newSettings.getHandlerInstantiator());
    }

    @Test
    public void testWithLocale() {
        Locale locale = Locale.US;
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, locale, null, null);
        BaseSettings newSettings = baseSettings.with(locale);

        assertSame(locale, newSettings.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, timeZone, null);
        BaseSettings newSettings = baseSettings.with(timeZone);

        assertSame(timeZone, newSettings.getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant base64Variant = Base64Variant.MIME_NO_LINEFEEDS;
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, base64Variant);
        BaseSettings newSettings = baseSettings.with(base64Variant);

        assertSame(base64Variant, newSettings.getBase64Variant());
    }

}