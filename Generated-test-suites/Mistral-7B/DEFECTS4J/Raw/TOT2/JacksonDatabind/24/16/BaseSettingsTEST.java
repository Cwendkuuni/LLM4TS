import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testBaseSettingsConstruction() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new PropertyNamingStrategy() {
            @Override
            public String nameForProperty(PropertyDescription property) {
                return property.getName();
            }
        };
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new SimpleTypeResolverBuilder();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HandlerInstantiator hi = new HandlerInstanciator();
        Locale locale = Locale.getDefault();
        TimeZone tz = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variants.STANDARD;
        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
        assertNotNull(baseSettings);
    }
}