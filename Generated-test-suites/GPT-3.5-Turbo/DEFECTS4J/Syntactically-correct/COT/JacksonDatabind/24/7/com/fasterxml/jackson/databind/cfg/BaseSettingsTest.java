
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {
    
    private BaseSettings baseSettings;
    private ClassIntrospector ci;
    private AnnotationIntrospector ai;
    private VisibilityChecker<?> vc;
    private PropertyNamingStrategy pns;
    private TypeFactory tf;
    private TypeResolverBuilder<?> typer;
    private DateFormat dateFormat;
    private HandlerInstantiator hi;
    private Locale locale;
    private TimeZone tz;
    private Base64Variant defaultBase64;
    
    @Before
    public void setup() {
        ci = new ClassIntrospector();
        ai = new AnnotationIntrospector();
        vc = new VisibilityChecker<?>();
        pns = new PropertyNamingStrategy();
        tf = new TypeFactory();
        typer = new TypeResolverBuilder<?>();
        dateFormat = new DateFormat();
        hi = new HandlerInstantiator();
        locale = new Locale();
        tz = new TimeZone();
        defaultBase64 = new Base64Variant();
        
        baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat,
                                        hi, locale, tz, defaultBase64);
    }
    
    @Test
    public void testGetClassIntrospector() {
        assertEquals(ci, baseSettings.getClassIntrospector());
    }
    
    @Test
    public void testGetAnnotationIntrospector() {
        assertEquals(ai, baseSettings.getAnnotationIntrospector());
    }
    
    @Test
    public void testGetVisibilityChecker() {
        assertEquals(vc, baseSettings.getVisibilityChecker());
    }
    
    @Test
    public void testGetPropertyNamingStrategy() {
        assertEquals(pns, baseSettings.getPropertyNamingStrategy());
    }
    
    @Test
    public void testGetTypeFactory() {
        assertEquals(tf, baseSettings.getTypeFactory());
    }
    
    @Test
    public void testGetTypeResolverBuilder() {
        assertEquals(typer, baseSettings.getTypeResolverBuilder());
    }
    
    @Test
    public void testGetDateFormat() {
        assertEquals(dateFormat, baseSettings.getDateFormat());
    }
    
    @Test
    public void testGetHandlerInstantiator() {
        assertEquals(hi, baseSettings.getHandlerInstantiator());
    }
    
    @Test
    public void testGetLocale() {
        assertEquals(locale, baseSettings.getLocale());
    }
    
    @Test
    public void testGetTimeZone() {
        assertEquals(tz, baseSettings.getTimeZone());
    }
    
    @Test
    public void testGetBase64Variant() {
        assertEquals(defaultBase64, baseSettings.getBase64Variant());
    }
    
    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector newCi = new ClassIntrospector();
        BaseSettings newSettings = baseSettings.withClassIntrospector(newCi);
        assertEquals(newCi, newSettings.getClassIntrospector());
    }
    
    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector newAi = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withAnnotationIntrospector(newAi);
        assertEquals(newAi, newSettings.getAnnotationIntrospector());
    }
    
    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker<?> newVc = new VisibilityChecker<?>();
        BaseSettings newSettings = baseSettings.withVisibilityChecker(newVc);
        assertEquals(newVc, newSettings.getVisibilityChecker());
    }
    
    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy newPns = new PropertyNamingStrategy();
        BaseSettings newSettings = baseSettings.withPropertyNamingStrategy(newPns);
        assertEquals(newPns, newSettings.getPropertyNamingStrategy());
    }
    
    @Test
    public void testWithTypeFactory() {
        TypeFactory newTf = new TypeFactory();
        BaseSettings newSettings = baseSettings.withTypeFactory(newTf);
        assertEquals(newTf, newSettings.getTypeFactory());
    }
    
    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> newTyper = new TypeResolverBuilder<?>();
        BaseSettings newSettings = baseSettings.withTypeResolverBuilder(newTyper);
        assertEquals(newTyper, newSettings.getTypeResolverBuilder());
    }
    
    @Test
    public void testWithDateFormat() {
        DateFormat newDf = new DateFormat();
        BaseSettings newSettings = baseSettings.withDateFormat(newDf);
        assertEquals(newDf, newSettings.getDateFormat());
    }
    
    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator newHi = new HandlerInstantiator();
        BaseSettings newSettings = baseSettings.withHandlerInstantiator(newHi);
        assertEquals(newHi, newSettings.getHandlerInstantiator());
    }
    
    @Test
    public void testWith() {
        Locale newLocale = new Locale();
        BaseSettings newSettings = baseSettings.with(newLocale);
        assertEquals(newLocale, newSettings.getLocale());
    }
    
    @Test
    public void testWithSetTimeZone() {
        TimeZone newTz = new TimeZone();
        BaseSettings newSettings = baseSettings.with(newTz);
        assertEquals(newTz, newSettings.getTimeZone());
    }
    
    @Test
    public void testWithBase64Variant() {
        Base64Variant newBase64 = new Base64Variant();
        BaseSettings newSettings = baseSettings.with(newBase64);
        assertEquals(newBase64, newSettings.getBase64Variant());
    }
}
