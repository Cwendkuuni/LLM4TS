import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withClassIntrospector(ci);
        Assert.assertEquals(ci, newSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withAnnotationIntrospector(ai);
        Assert.assertEquals(ai, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withInsertedAnnotationIntrospector(ai);
        Assert.assertEquals(ai, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withAppendedAnnotationIntrospector(ai);
        Assert.assertEquals(ai, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withVisibilityChecker(vc);
        Assert.assertEquals(vc, newSettings.getVisibilityChecker());
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withPropertyNamingStrategy(pns);
        Assert.assertEquals(pns, newSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory tf = new TypeFactory();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withTypeFactory(tf);
        Assert.assertEquals(tf, newSettings.getTypeFactory());
    }

    @Test
    public void testWithDateFormat() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withDateFormat(df);
        Assert.assertEquals(df, newSettings.getDateFormat());
    }

    @Test
    public void testWithTimeZone() {
        TimeZone tz = TimeZone.getTimeZone("GMT");
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withTimeZone(tz);
        Assert.assertEquals(tz, newSettings.getTimeZone());
    }

    @Test
    public void testWithDefaultBase64() {
        Base64Variant base64 = new Base64Variant();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.with(base64);
        Assert.assertEquals(base64, newSettings.getBase64Variant());
    }

}