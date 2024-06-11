package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testGetters() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);

        ClassIntrospector ci = settings.getClassIntrospector();
        AnnotationIntrospector ai = settings.getAnnotationIntrospector();
        VisibilityChecker<?> vc = settings.getVisibilityChecker();
        PropertyNamingStrategy pns = settings.getPropertyNamingStrategy();
        TypeFactory tf = settings.getTypeFactory();
        TypeResolverBuilder<?> trb = settings.getTypeResolverBuilder();
        DateFormat df = settings.getDateFormat();
        HandlerInstantiator hi = settings.getHandlerInstantiator();
        Locale locale = settings.getLocale();
        TimeZone timeZone = settings.getTimeZone();
        Base64Variant base64 = settings.getBase64Variant();

        assertNotNull(ci);
        assertNotNull(ai);
        assertNotNull(vc);
        assertNotNull(pns);
        assertNotNull(tf);
        assertNotNull(trb);
        assertNotNull(df);
        assertNotNull(hi);
        assertEquals(Locale.getDefault(), locale);
        assertEquals(TimeZone.getDefault(), timeZone);
        assertEquals(Base64Variant.STANDARD, base64);
    }

    @Test
    public void testWithClassIntrospector() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings newSettings = settings.withClassIntrospector(ci);

        assertNotSame(settings, newSettings);
        assertEquals(ci, newSettings.getClassIntrospector());
        assertEquals(settings.getAnnotationIntrospector(), newSettings.getAnnotationIntrospector());
        assertEquals(settings.getVisibilityChecker(), newSettings.getVisibilityChecker());
        assertEquals(settings.getPropertyNamingStrategy(), newSettings.getPropertyNamingStrategy());
        assertEquals(settings.getTypeFactory(), newSettings.getTypeFactory());
        assertEquals(settings.getTypeResolverBuilder(), newSettings.getTypeResolverBuilder());
        assertEquals(settings.getDateFormat(), newSettings.getDateFormat());
        assertEquals(settings.getHandlerInstantiator(), newSettings.getHandlerInstantiator());
        assertEquals(settings.getLocale(), newSettings.getLocale());
        assertEquals(settings.getTimeZone(), newSettings.getTimeZone());
        assertEquals(settings.getBase64Variant(), newSettings.getBase64Variant());
    }

    // Repeat the testWithXXX methods for all the getters and factories in the BaseSettings class

    // ...

    @Test
    public void testToString() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        String toString = settings.toString();

        assertNotNull(toString);
    }
}