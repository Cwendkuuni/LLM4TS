import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings result = baseSettings.withClassIntrospector(ci);
        assertEquals(ci, result.getClassIntrospector());
    }
    
    @Test
    public void testWithAnnotationIntrospector() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings result = baseSettings.withAnnotationIntrospector(ai);
        assertEquals(ai, result.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings result = baseSettings.withInsertedAnnotationIntrospector(ai);
        assertEquals(ai, result.getAnnotationIntrospector());
    }
    
    @Test
    public void testWithAppendedAnnotationIntrospector() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings result = baseSettings.withAppendedAnnotationIntrospector(ai);
        assertEquals(ai, result.getAnnotationIntrospector());
    }
    
    @Test
    public void testWithVisibilityChecker() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        BaseSettings result = baseSettings.withVisibilityChecker(vc);
        assertEquals(vc, result.getVisibilityChecker());
    }

    @Test
    public void testWithVisibility() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings result = baseSettings.withVisibility(PropertyAccessor.GETTER, Visibility.PUBLIC_ONLY);
        assertEquals(Visibility.PUBLIC_ONLY, result.getVisibilityChecker().getFieldVisibility());
    }
    
    @Test
    public void testWithPropertyNamingStrategy() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        BaseSettings result = baseSettings.withPropertyNamingStrategy(pns);
        assertEquals(pns, result.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        TypeFactory tf = new TypeFactory();
        BaseSettings result = baseSettings.withTypeFactory(tf);
        assertEquals(tf, result.getTypeFactory());
    }

    @Test
    public void testWithTypeResolverBuilder() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        BaseSettings result = baseSettings.withTypeResolverBuilder(typer);
        assertEquals(typer, result.getTypeResolverBuilder());
    }
    
    @Test
    public void testWithDateFormat() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        DateFormat df = DateFormat.getDateInstance();
        BaseSettings result = baseSettings.withDateFormat(df);
        assertEquals(df, result.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        HandlerInstantiator hi = new HandlerInstantiator();
        BaseSettings result = baseSettings.withHandlerInstantiator(hi);
        assertEquals(hi, result.getHandlerInstantiator());
    }

    @Test
    public void testWithLocale() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        Locale l = Locale.US;
        BaseSettings result = baseSettings.with(l);
        assertEquals(l, result.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        TimeZone tz = TimeZone.getTimeZone("GMT");
        BaseSettings result = baseSettings.with(tz);
        assertEquals(tz, result.getTimeZone());
    }
    
    @Test
    public void testWithBase64Variant() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        Base64Variant base64 = new Base64Variant();
        BaseSettings result = baseSettings.with(base64);
        assertEquals(base64, result.getBase64Variant());
    }

}