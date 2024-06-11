import static org.junit.Assert.*;
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
        DateFormat dateFormat = new DateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = new Locale();
        TimeZone tz = TimeZone.getDefault();
        Base64Variant bv = new Base64Variant();
        BaseSettings bs = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, bv);
        
        ClassIntrospector newCi = new ClassIntrospector();
        BaseSettings updatedBs = bs.withClassIntrospector(newCi);
        
        assertEquals(newCi, updatedBs.getClassIntrospector());
    }
    
    @Test
    public void testWithAnnotationIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        DateFormat dateFormat = new DateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = new Locale();
        TimeZone tz = TimeZone.getDefault();
        Base64Variant bv = new Base64Variant();
        BaseSettings bs = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, bv);
        
        AnnotationIntrospector newAi = new AnnotationIntrospector();
        BaseSettings updatedBs = bs.withAnnotationIntrospector(newAi);
        
        assertEquals(newAi, updatedBs.getAnnotationIntrospector());
    }
    
    // Additional test cases for other methods...
}