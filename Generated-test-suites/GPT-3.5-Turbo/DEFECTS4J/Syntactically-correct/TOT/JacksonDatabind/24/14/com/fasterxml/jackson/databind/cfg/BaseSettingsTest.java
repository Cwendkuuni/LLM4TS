import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setUp() {
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings newSettings = baseSettings.withClassIntrospector(ci);
        assertNotSame(baseSettings, newSettings);
        assertEquals(ci, newSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withAnnotationIntrospector(ai);
        assertNotSame(baseSettings, newSettings);
        assertEquals(ai, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withInsertedAnnotationIntrospector(ai);
        assertNotSame(baseSettings, newSettings);
        assertEquals(ai, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withAppendedAnnotationIntrospector(ai);
        assertNotSame(baseSettings, newSettings);
        assertEquals(ai, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker vc = new VisibilityChecker();
        BaseSettings newSettings = baseSettings.withVisibilityChecker(vc);
        assertNotSame(baseSettings, newSettings);
        assertEquals(vc, newSettings.getVisibilityChecker());
    }

    @Test
    public void testWithVisibility() {
        visibilityMethodTest(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.PROTECTED);
        visibilityMethodTest(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.DEFAULT);
        visibilityMethodTest(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.PUBLIC_ONLY);
        visibilityMethodTest(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NONE);
    }

    private void visibilityMethodTest(PropertyAccessor method, JsonAutoDetect.Visibility visibility) {
        BaseSettings newSettings = baseSettings.withVisibility(method, visibility);
        assertNotSame(baseSettings, newSettings);
        assertEquals(visibility, newSettings.getVisibilityChecker().findVisibility(method));
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        BaseSettings newSettings = baseSettings.withPropertyNamingStrategy(pns);
        assertNotSame(baseSettings, newSettings);
        assertEquals(pns, newSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory tf = new TypeFactory();
        BaseSettings newSettings = baseSettings.withTypeFactory(tf);
        assertNotSame(baseSettings, newSettings);
        assertEquals(tf, newSettings.getTypeFactory());
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder trb = new TypeResolverBuilder();
        BaseSettings newSettings = baseSettings.withTypeResolverBuilder(trb);
        assertNotSame(baseSettings, newSettings);
        assertEquals(trb, newSettings.getTypeResolverBuilder());
    }

    @Test
    public void testWithDateFormat() {
        DateFormat df = new DateFormat();
        BaseSettings newSettings = baseSettings.withDateFormat(df);
        assertNotSame(baseSettings, newSettings);
        assertEquals(df, newSettings.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator hi = new HandlerInstantiator();
        BaseSettings newSettings = baseSettings.withHandlerInstantiator(hi);
        assertNotSame(baseSettings, newSettings);
        assertEquals(hi, newSettings.getHandlerInstantiator());
    }

    @Test
    public void testWithLocale() {
        Locale locale = new Locale("en", "US");
        BaseSettings newSettings = baseSettings.with(locale);
        assertNotSame(baseSettings, newSettings);
        assertEquals(locale, newSettings.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("PST");
        BaseSettings newSettings = baseSettings.with(timeZone);
        assertNotSame(baseSettings, newSettings);
        assertEquals(timeZone, newSettings.getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant base64 = new Base64Variant();
        BaseSettings newSettings = baseSettings.with(base64);
        assertNotSame(baseSettings, newSettings);
        assertEquals(base64, newSettings.getBase64Variant());
    }
}