
package com.fasterxml.jackson.databind.cfg;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setup() {
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        baseSettings = baseSettings.withClassIntrospector(ci);
        Assert.assertEquals(ci, baseSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        baseSettings = baseSettings.withAnnotationIntrospector(ai);
        Assert.assertEquals(ai, baseSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        baseSettings = baseSettings.withInsertedAnnotationIntrospector(ai);
        Assert.assertEquals(ai, baseSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        baseSettings = baseSettings.withAppendedAnnotationIntrospector(ai);
        Assert.assertEquals(ai, baseSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        baseSettings = baseSettings.withVisibilityChecker(vc);
        Assert.assertEquals(vc, baseSettings.getVisibilityChecker());
    }

    @Test
    public void testWithVisibility() {
        PropertyAccessor forMethod = PropertyAccessor.GETTER;
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.DEFAULT;
        baseSettings = baseSettings.withVisibility(forMethod, visibility);
        Assert.assertTrue(baseSettings.getVisibilityChecker().isGetterVisible(forMethod));
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        baseSettings = baseSettings.withPropertyNamingStrategy(pns);
        Assert.assertEquals(pns, baseSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory tf = new TypeFactory();
        baseSettings = baseSettings.withTypeFactory(tf);
        Assert.assertEquals(tf, baseSettings.getTypeFactory());
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        baseSettings = baseSettings.withTypeResolverBuilder(typer);
        Assert.assertEquals(typer, baseSettings.getTypeResolverBuilder());
    }

    @Test
    public void testWithDateFormat() {
        DateFormat df = new StdDateFormat();
        baseSettings = baseSettings.withDateFormat(df);
        Assert.assertEquals(df, baseSettings.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator hi = new HandlerInstantiator();
        baseSettings = baseSettings.withHandlerInstantiator(hi);
        Assert.assertEquals(hi, baseSettings.getHandlerInstantiator());
    }

    @Test
    public void testWithLocale() {
        Locale l = new Locale("en", "US");
        baseSettings = baseSettings.with(l);
        Assert.assertEquals(l, baseSettings.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        TimeZone tz = TimeZone.getTimeZone("PST");
        baseSettings = baseSettings.with(tz);
        Assert.assertEquals(tz, baseSettings.getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant bv = new Base64Variant();
        baseSettings = baseSettings.with(bv);
        Assert.assertEquals(bv, baseSettings.getBase64Variant());
    }
}
