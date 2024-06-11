package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testGetters() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, Locale.US, TimeZone.getDefault(), Base64Variant.STANDARD);

        ClassIntrospector ci = settings.getClassIntrospector();
        AnnotationIntrospector ai = settings.getAnnotationIntrospector();
        VisibilityChecker<?> vc = settings.getVisibilityChecker();
        PropertyNamingStrategy pns = settings.getPropertyNamingStrategy();
        TypeFactory tf = settings.getTypeFactory();
        TypeResolverBuilder<?> typer = settings.getTypeResolverBuilder();
        DateFormat dateFormat = settings.getDateFormat();
        HandlerInstantiator hi = settings.getHandlerInstantiator();
        Locale locale = settings.getLocale();
        TimeZone timeZone = settings.getTimeZone();
        Base64Variant base64 = settings.getBase64Variant();

        assertNotNull(ci);
        assertNotNull(ai);
        assertNotNull(vc);
        assertNotNull(pns);
        assertNotNull(tf);
        assertNotNull(typer);
        assertNotNull(dateFormat);
        assertNotNull(hi);
        assertEquals(Locale.US, locale);
        assertEquals(TimeZone.getDefault(), timeZone);
        assertEquals(Base64Variant.STANDARD, base64);
    }

    @Test
    public void testSetters() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, Locale.US, TimeZone.getDefault(), Base64Variant.STANDARD);

        BaseSettings newSettings = settings.withClassIntrospector(new ClassIntrospector() {});
        assertEquals(newSettings.getClassIntrospector(), new ClassIntrospector());

        newSettings = settings.withAnnotationIntrospector(new AnnotationIntrospector() {});
        assertEquals(newSettings.getAnnotationIntrospector(), new AnnotationIntrospector());

        newSettings = settings.withVisibilityChecker(new VisibilityChecker<Object>() {});
        assertEquals(newSettings.getVisibilityChecker(), new VisibilityChecker<Object>());

        newSettings = settings.withPropertyNamingStrategy(new PropertyNamingStrategy() {});
        assertEquals(newSettings.getPropertyNamingStrategy(), new PropertyNamingStrategy());

        newSettings = settings.withTypeFactory(new TypeFactory() {});
        assertEquals(newSettings.getTypeFactory(), new TypeFactory());

        newSettings = settings.withTypeResolverBuilder(new TypeResolverBuilder<Object>() {});
        assertEquals(newSettings.getTypeResolverBuilder(), new TypeResolverBuilder<Object>());

        newSettings = settings.withDateFormat(new DateFormat() {});
        assertEquals(newSettings.getDateFormat(), new DateFormat());

        newSettings = settings.withHandlerInstantiator(new HandlerInstantiator() {});
        assertEquals(newSettings.getHandlerInstantiator(), new HandlerInstantiator());

        newSettings = settings.with(Locale.ENGLISH);
        assertEquals(newSettings.getLocale(), Locale.ENGLISH);

        newSettings = settings.with(TimeZone.getTimeZone("UTC"));
        assertEquals(newSettings.getTimeZone(), TimeZone.getTimeZone("UTC"));

        newSettings = settings.with(Base64Variant.URL_SAFE);
        assertEquals(newSettings.getBase64Variant(), Base64Variant.URL_SAFE);
    }
}