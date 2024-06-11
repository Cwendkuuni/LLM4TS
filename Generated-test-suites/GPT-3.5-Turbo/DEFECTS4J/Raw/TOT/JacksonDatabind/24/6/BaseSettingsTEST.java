import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setUp() {
        // Initialize the BaseSettings instance with some dummy values
        baseSettings = new BaseSettings(null, null, null, null, null,
                null, null, null, null,
                null, null);
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings result = baseSettings.withClassIntrospector(ci);

        assertEquals(ci, result.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings result = baseSettings.withAnnotationIntrospector(ai);

        assertEquals(ai, result.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings result = baseSettings.withInsertedAnnotationIntrospector(ai);

        assertEquals(ai, result.getAnnotationIntrospector());
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings result = baseSettings.withAppendedAnnotationIntrospector(ai);

        assertEquals(ai, result.getAnnotationIntrospector());
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker vc = new VisibilityChecker();
        BaseSettings result = baseSettings.withVisibilityChecker(vc);

        assertEquals(vc, result.getVisibilityChecker());
    }

    @Test
    public void testWithVisibility() {
        PropertyAccessor forMethod = PropertyAccessor.METHOD;
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.PUBLIC_ONLY;
        BaseSettings result = baseSettings.withVisibility(forMethod, visibility);

        assertEquals(visibility, result.getVisibilityChecker().findVisibility(forMethod));
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        BaseSettings result = baseSettings.withPropertyNamingStrategy(pns);

        assertEquals(pns, result.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory tf = new TypeFactory();
        BaseSettings result = baseSettings.withTypeFactory(tf);

        assertEquals(tf, result.getTypeFactory());
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder trb = new TypeResolverBuilder();
        BaseSettings result = baseSettings.withTypeResolverBuilder(trb);

        assertEquals(trb, result.getTypeResolverBuilder());
    }

    @Test
    public void testWithDateFormat() {
        DateFormat df = new DateFormat();
        BaseSettings result = baseSettings.withDateFormat(df);

        assertEquals(df, result.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator hi = new HandlerInstantiator();
        BaseSettings result = baseSettings.withHandlerInstantiator(hi);

        assertEquals(hi, result.getHandlerInstantiator());
    }

    @Test
    public void testWithLocale() {
        Locale locale = new Locale("en");
        BaseSettings result = baseSettings.with(locale);

        assertEquals(locale, result.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        TimeZone tz = TimeZone.getTimeZone("GMT");
        BaseSettings result = baseSettings.with(tz);

        assertEquals(tz, result.getTimeZone());
        assertEquals(tz, result.getDateFormat().getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant base64 = new Base64Variant();
        BaseSettings result = baseSettings.with(base64);

        assertEquals(base64, result.getBase64Variant());
    }
}