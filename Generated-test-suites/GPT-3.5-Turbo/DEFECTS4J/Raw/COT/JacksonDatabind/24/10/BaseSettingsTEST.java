
import org.junit.Test;

import com.fasterxml.jackson.databind.cfg.BaseSettings;

public class BaseSettingsTest {
    
    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withClassIntrospector(ci);
        assertSame(ci, newSettings.getClassIntrospector());
    }
    
    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withAnnotationIntrospector(ai);
        assertSame(ai, newSettings.getAnnotationIntrospector());
    }
    
    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withInsertedAnnotationIntrospector(ai);
        assertSame(ai, newSettings.getAnnotationIntrospector());
    }
    
    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withAppendedAnnotationIntrospector(ai);
        assertSame(ai, newSettings.getAnnotationIntrospector());
    }
    
    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withVisibilityChecker(vc);
        assertSame(vc, newSettings.getVisibilityChecker());
    }
    
    @Test
    public void testWithVisibility() {
        PropertyAccessor forMethod = PropertyAccessor.METHOD;
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.PUBLIC_ONLY;
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withVisibility(forMethod, visibility);
        assertSame(visibility, newSettings.getVisibilityChecker().findVisibility(forMethod));
    }
    
    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withPropertyNamingStrategy(pns);
        assertSame(pns, newSettings.getPropertyNamingStrategy());
    }
    
    @Test
    public void testWithTypeFactory() {
        TypeFactory tf = new TypeFactory();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withTypeFactory(tf);
        assertSame(tf, newSettings.getTypeFactory());
    }
    
    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withTypeResolverBuilder(typer);
        assertSame(typer, newSettings.getTypeResolverBuilder());
    }
    
    @Test
    public void testWithDateFormat() {
        DateFormat df = new DateFormat();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withDateFormat(df);
        assertSame(df, newSettings.getDateFormat());
    }
    
    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator hi = new HandlerInstantiator();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withHandlerInstantiator(hi);
        assertSame(hi, newSettings.getHandlerInstantiator());
    }
    
    @Test
    public void testWithLocale() {
        Locale locale = new Locale("en-US");
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.with(locale);
        assertSame(locale, newSettings.getLocale());
    }
    
    @Test
    public void testWithTimeZone() {
        TimeZone tz = TimeZone.getTimeZone("GMT");
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.with(tz);
        assertSame(tz, newSettings.getTimeZone());
    }
    
    @Test
    public void testWithBase64Variant() {
        Base64Variant base64 = new Base64Variant();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.with(base64);
        assertSame(base64, newSettings.getBase64Variant());
    }
}
