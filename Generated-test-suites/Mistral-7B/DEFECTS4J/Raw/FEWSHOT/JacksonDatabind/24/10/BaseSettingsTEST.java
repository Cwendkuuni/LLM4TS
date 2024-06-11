package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testBaseSettings() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNull(baseSettings.getClassIntrospector());
        assertNull(baseSettings.getAnnotationIntrospector());
        assertNull(baseSettings.getVisibilityChecker());
        assertNull(baseSettings.getPropertyNamingStrategy());
        assertNull(baseSettings.getTypeFactory());
        assertNull(baseSettings.getTypeResolverBuilder());
        assertNull(baseSettings.getDateFormat());
        assertNull(baseSettings.getHandlerInstantiator());
        assertNull(baseSettings.getLocale());
        assertNull(baseSettings.getTimeZone());
        assertNull(baseSettings.getBase64Variant());

        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;

        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);
        assertEquals(ci, settings.getClassIntrospector());
        assertEquals(ai, settings.getAnnotationIntrospector());
        assertEquals(vc, settings.getVisibilityChecker());
        assertEquals(pns, settings.getPropertyNamingStrategy());
        assertEquals(tf, settings.getTypeFactory());
        assertEquals(typer, settings.getTypeResolverBuilder());
        assertEquals(dateFormat, settings.getDateFormat());
        assertEquals(hi, settings.getHandlerInstantiator());
        assertEquals(locale, settings.getLocale());
        assertEquals(timeZone, settings.getTimeZone());
        assertEquals(defaultBase64, settings.getBase64Variant());
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings settings = baseSettings.withClassIntrospector(ci);
        assertEquals(ci, settings.getClassIntrospector());
        assertNotSame(baseSettings, settings);
    }

    // Add test cases for other factory methods similarly

}