
package com.fasterxml.jackson.databind.cfg;

import org.junit.Assert;
import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testConstructorAndGetters() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker vc = new VisibilityChecker();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder typer = new TypeResolverBuilder();
        DateFormat dateFormat = new DateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone tz = TimeZone.getDefault();
        Base64Variant defaultBase64 = new Base64Variant();

        BaseSettings bs = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);

        Assert.assertEquals(ci, bs.getClassIntrospector());
        Assert.assertEquals(ai, bs.getAnnotationIntrospector());
        Assert.assertEquals(vc, bs.getVisibilityChecker());
        Assert.assertEquals(pns, bs.getPropertyNamingStrategy());
        Assert.assertEquals(tf, bs.getTypeFactory());
        Assert.assertEquals(typer, bs.getTypeResolverBuilder());
        Assert.assertEquals(dateFormat, bs.getDateFormat());
        Assert.assertEquals(hi, bs.getHandlerInstantiator());
        Assert.assertEquals(locale, bs.getLocale());
        Assert.assertEquals(tz, bs.getTimeZone());
        Assert.assertEquals(defaultBase64, bs.getBase64Variant());
    }

    @Test
    public void testWithMethods() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker vc = new VisibilityChecker();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder typer = new TypeResolverBuilder();
        DateFormat dateFormat = new DateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone tz = TimeZone.getDefault();
        Base64Variant defaultBase64 = new Base64Variant();

        BaseSettings bs1 = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);

        ClassIntrospector ci2 = new ClassIntrospector();
        AnnotationIntrospector ai2 = new AnnotationIntrospector();
        VisibilityChecker vc2 = new VisibilityChecker();
        PropertyNamingStrategy pns2 = new PropertyNamingStrategy();
        TypeFactory tf2 = new TypeFactory();
        TypeResolverBuilder typer2 = new TypeResolverBuilder();
        DateFormat dateFormat2 = new DateFormat();
        HandlerInstantiator hi2 = new HandlerInstantiator();
        Locale locale2 = Locale.getDefault();
        TimeZone tz2 = TimeZone.getDefault();
        Base64Variant defaultBase642 = new Base64Variant();

        BaseSettings bs2 = bs1.withClassIntrospector(ci2)
                .withAnnotationIntrospector(ai2)
                .withVisibilityChecker(vc2)
                .withPropertyNamingStrategy(pns2)
                .withTypeFactory(tf2)
                .withTypeResolverBuilder(typer2)
                .withDateFormat(dateFormat2)
                .withHandlerInstantiator(hi2)
                .with(locale2)
                .with(tz2)
                .with(defaultBase642);

        Assert.assertEquals(ci2, bs2.getClassIntrospector());
        Assert.assertEquals(ai2, bs2.getAnnotationIntrospector());
        Assert.assertEquals(vc2, bs2.getVisibilityChecker());
        Assert.assertEquals(pns2, bs2.getPropertyNamingStrategy());
        Assert.assertEquals(tf2, bs2.getTypeFactory());
        Assert.assertEquals(typer2, bs2.getTypeResolverBuilder());
        Assert.assertEquals(dateFormat2, bs2.getDateFormat());
        Assert.assertEquals(hi2, bs2.getHandlerInstantiator());
        Assert.assertEquals(locale2, bs2.getLocale());
        Assert.assertEquals(tz2, bs2.getTimeZone());
        Assert.assertEquals(defaultBase642, bs2.getBase64Variant());
    }
}
