import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSettingsTest {

    //Test classIntrospector getter
    @Test
    public void testGetClassIntrospector() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNull(baseSettings.getClassIntrospector());
    }
    
    //Test annotationIntrospector getter
    @Test
    public void testGetAnnotationIntrospector() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNull(baseSettings.getAnnotationIntrospector());
    }
    
    //Test visibilityChecker getter
    @Test
    public void testGetVisibilityChecker() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNull(baseSettings.getVisibilityChecker());
    }
    
    //Test propertyNamingStrategy getter
    @Test
    public void testGetPropertyNamingStrategy() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNull(baseSettings.getPropertyNamingStrategy());
    }
    
    //Test typeFactory getter
    @Test
    public void testGetTypeFactory() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNull(baseSettings.getTypeFactory());
    }
    
    //Test typeResolverBuilder getter
    @Test
    public void testGetTypeResolverBuilder() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNull(baseSettings.getTypeResolverBuilder());
    }
    
    //Test dateFormat getter
    @Test
    public void testGetDateFormat() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNull(baseSettings.getDateFormat());
    }
    
    //Test handlerInstantiator getter
    @Test
    public void testGetHandlerInstantiator() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNull(baseSettings.getHandlerInstantiator());
    }
    
    //Test locale getter
    @Test
    public void testGetLocale() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNull(baseSettings.getLocale());
    }
    
    //Test timeZone getter
    @Test
    public void testGetTimeZone() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNull(baseSettings.getTimeZone());
    }
    
    //Test base64Variant getter
    @Test
    public void testGetBase64Variant() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNull(baseSettings.getBase64Variant());
    }
}