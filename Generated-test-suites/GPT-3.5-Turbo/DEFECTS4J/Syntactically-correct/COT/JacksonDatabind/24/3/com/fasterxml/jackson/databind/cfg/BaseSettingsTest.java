
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class BaseSettingsTest {

    private DateFormat dateFormat;

    @Before
    public void setUp() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings baseSettings = new BaseSettings(ci, null, null, null, null, null, null, null, null, null, null);

        BaseSettings updatedSettings = baseSettings.withClassIntrospector(ci);

        Assert.assertEquals(ci, updatedSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, ai, null, null, null, null, null, null, null, null, null);

        BaseSettings updatedSettings = baseSettings.withAnnotationIntrospector(ai);

        Assert.assertEquals(ai, updatedSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);

        BaseSettings updatedSettings = baseSettings.withInsertedAnnotationIntrospector(ai);

        Assert.assertNotEquals(baseSettings.getAnnotationIntrospector(), updatedSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);

        BaseSettings updatedSettings = baseSettings.withAppendedAnnotationIntrospector(ai);

        Assert.assertNotEquals(baseSettings.getAnnotationIntrospector(), updatedSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker vc = new VisibilityChecker();
        BaseSettings baseSettings = new BaseSettings(null, null, vc, null, null, null, null, null, null, null, null);

        BaseSettings updatedSettings = baseSettings.withVisibilityChecker(vc);

        Assert.assertEquals(vc, updatedSettings.getVisibilityChecker());
    }

    @Test
    public void testWithVisibility() {
        VisibilityChecker vc = new VisibilityChecker();
        PropertyAccessor pa = PropertyAccessor.ALL;
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.PUBLIC_ONLY;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);

        BaseSettings updatedSettings = baseSettings.withVisibility(pa, visibility);

        Assert.assertNotEquals(baseSettings.getVisibilityChecker(), updatedSettings.getVisibilityChecker());
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        BaseSettings baseSettings = new BaseSettings(null, null, null, pns, null, null, null, null, null, null, null);

        BaseSettings updatedSettings = baseSettings.withPropertyNamingStrategy(pns);

        Assert.assertEquals(pns, updatedSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory tf = new TypeFactory();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, tf, null, null, null, null, null, null);

        BaseSettings updatedSettings = baseSettings.withTypeFactory(tf);

        Assert.assertEquals(tf, updatedSettings.getTypeFactory());
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder trb = new TypeResolverBuilder();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, trb, null, null, null, null, null);

        BaseSettings updatedSettings = baseSettings.withTypeResolverBuilder(trb);

        Assert.assertEquals(trb, updatedSettings.getTypeResolverBuilder());
    }

    @Test
    public void testWithDateFormat() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, dateFormat, null, null, null, null);

        BaseSettings updatedSettings = baseSettings.withDateFormat(dateFormat);

        Assert.assertEquals(dateFormat, updatedSettings.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator hi = new HandlerInstantiator();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, hi, null, null, null);

        BaseSettings updatedSettings = baseSettings.withHandlerInstantiator(hi);

        Assert.assertEquals(hi, updatedSettings.getHandlerInstantiator());
    }

    @Test
    public void testWithLocale() {
        Locale l = Locale.US;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, l, null, null);

        BaseSettings updatedSettings = baseSettings.with(l);

        Assert.assertEquals(l, updatedSettings.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        TimeZone tz = TimeZone.getTimeZone("GMT");
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, tz, null);

        BaseSettings updatedSettings = baseSettings.with(tz);

        Assert.assertEquals(tz, updatedSettings.getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant bv = new Base64Variant();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, bv);

        BaseSettings updatedSettings = baseSettings.with(bv);

        Assert.assertEquals(bv, updatedSettings.getBase64Variant());
    }

}
