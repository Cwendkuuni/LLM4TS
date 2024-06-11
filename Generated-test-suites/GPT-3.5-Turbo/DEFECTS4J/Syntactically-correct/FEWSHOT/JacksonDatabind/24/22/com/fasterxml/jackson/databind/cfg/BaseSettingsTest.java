package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testConstructorAndGetters() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder();
        DateFormat df = new DateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = new Locale();
        TimeZone timeZone = TimeZone.getTimeZone("EST");
        Base64Variant base64 = new Base64Variant();

        BaseSettings bs = new BaseSettings(ci, ai, vc, pns, tf, typer, df, hi, locale, timeZone, base64);

        assertNotNull(bs.getClassIntrospector());
        assertNotNull(bs.getAnnotationIntrospector());
        assertNotNull(bs.getVisibilityChecker());
        assertNotNull(bs.getPropertyNamingStrategy());
        assertNotNull(bs.getTypeFactory());
        assertNotNull(bs.getTypeResolverBuilder());
        assertNotNull(bs.getDateFormat());
        assertNotNull(bs.getHandlerInstantiator());
        assertNotNull(bs.getLocale());
        assertNotNull(bs.getTimeZone());
        assertNotNull(bs.getBase64Variant());

        assertEquals(ci, bs.getClassIntrospector());
        assertEquals(ai, bs.getAnnotationIntrospector());
        assertEquals(vc, bs.getVisibilityChecker());
        assertEquals(pns, bs.getPropertyNamingStrategy());
        assertEquals(tf, bs.getTypeFactory());
        assertEquals(typer, bs.getTypeResolverBuilder());
        assertEquals(df, bs.getDateFormat());
        assertEquals(hi, bs.getHandlerInstantiator());
        assertEquals(locale, bs.getLocale());
        assertEquals(timeZone, bs.getTimeZone());
        assertEquals(base64, bs.getBase64Variant());
    }

    @Test
    public void testWithMethods() {
        BaseSettings originalSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);

        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings ciSettings = originalSettings.withClassIntrospector(ci);
        assertNotNull(ciSettings);
        assertEquals(ci, ciSettings.getClassIntrospector());

        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings aiSettings = originalSettings.withAnnotationIntrospector(ai);
        assertNotNull(aiSettings);
        assertEquals(ai, aiSettings.getAnnotationIntrospector());

        VisibilityChecker<?> vc = new VisibilityChecker();
        BaseSettings vcSettings = originalSettings.withVisibilityChecker(vc);
        assertNotNull(vcSettings);
        assertEquals(vc, vcSettings.getVisibilityChecker());

        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        BaseSettings pnsSettings = originalSettings.withPropertyNamingStrategy(pns);
        assertNotNull(pnsSettings);
        assertEquals(pns, pnsSettings.getPropertyNamingStrategy());

        TypeFactory tf = new TypeFactory();
        BaseSettings tfSettings = originalSettings.withTypeFactory(tf);
        assertNotNull(tfSettings);
        assertEquals(tf, tfSettings.getTypeFactory());

        TypeResolverBuilder<?> typer = new TypeResolverBuilder();
        BaseSettings typerSettings = originalSettings.withTypeResolverBuilder(typer);
        assertNotNull(typerSettings);
        assertEquals(typer, typerSettings.getTypeResolverBuilder());

        DateFormat df = new DateFormat();
        BaseSettings dfSettings = originalSettings.withDateFormat(df);
        assertNotNull(dfSettings);
        assertEquals(df, dfSettings.getDateFormat());

        HandlerInstantiator hi = new HandlerInstantiator();
        BaseSettings hiSettings = originalSettings.withHandlerInstantiator(hi);
        assertNotNull(hiSettings);
        assertEquals(hi, hiSettings.getHandlerInstantiator());

        Locale locale = new Locale();
        BaseSettings localeSettings = originalSettings.with(locale);
        assertNotNull(localeSettings);
        assertEquals(locale, localeSettings.getLocale());

        TimeZone timeZone = TimeZone.getTimeZone("EST");
        BaseSettings tzSettings = originalSettings.with(timeZone);
        assertNotNull(tzSettings);
        assertEquals(timeZone, tzSettings.getTimeZone());

        Base64Variant base64 = new Base64Variant();
        BaseSettings base64Settings = originalSettings.with(base64);
        assertNotNull(base64Settings);
        assertEquals(base64, base64Settings.getBase64Variant());
    }
}