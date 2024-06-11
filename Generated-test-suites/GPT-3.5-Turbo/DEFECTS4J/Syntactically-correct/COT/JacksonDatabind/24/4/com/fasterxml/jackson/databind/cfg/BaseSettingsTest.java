import static org.junit.Assert.*;
import org.junit.Test;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings = baseSettings.withClassIntrospector(ci);
        assertEquals(ci, baseSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings = baseSettings.withAnnotationIntrospector(ai);
        assertEquals(ai, baseSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings = baseSettings.withInsertedAnnotationIntrospector(ai);
        assertEquals(ai, baseSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings = baseSettings.withAppendedAnnotationIntrospector(ai);
        assertEquals(ai, baseSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings = baseSettings.withVisibilityChecker(vc);
        assertEquals(vc, baseSettings.getVisibilityChecker());
    }

    @Test
    public void testWithVisibility() {
        PropertyAccessor propAccessor = PropertyAccessor.FIELD;
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.DEFAULT;
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings = baseSettings.withVisibility(propAccessor, visibility);
        assertEquals(visibility, baseSettings.getVisibilityChecker().findFieldVisibility(visibility));
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings = baseSettings.withPropertyNamingStrategy(pns);
        assertEquals(pns, baseSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory tf = new TypeFactory();
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings = baseSettings.withTypeFactory(tf);
        assertEquals(tf, baseSettings.getTypeFactory());
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<Object>();
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings = baseSettings.withTypeResolverBuilder(typer);
        assertEquals(typer, baseSettings.getTypeResolverBuilder());
    }

    @Test
    public void testWithDateFormat() {
        DateFormat df = DateFormat.getInstance();
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings = baseSettings.withDateFormat(df);
        assertEquals(df, baseSettings.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator hi = new HandlerInstantiator();
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings = baseSettings.withHandlerInstantiator(hi);
        assertEquals(hi, baseSettings.getHandlerInstantiator());
    }

    @Test
    public void testWithLocale() {
        Locale l = Locale.getDefault();
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings = baseSettings.with(l);
        assertEquals(l, baseSettings.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings = baseSettings.with(tz);
        assertEquals(tz, baseSettings.getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant b64 = Base64Variant.getDefaultVariant();
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings = baseSettings.with(b64);
        assertEquals(b64, baseSettings.getBase64Variant());
    }
}