package com.fasterxml.jackson.databind.cfg;

import org.junit.Test;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.*;

public class BaseSettingsTest {

    // Typical use case for BaseSettings constructor
    @Test
    public void testBaseSettings() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = new Locale("en", "US");
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Base64Variant defaultBase64 = new Base64Variant();

        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);

        assertNotNull(baseSettings);
        assertEquals(ci, baseSettings.getClassIntrospector());
        assertEquals(ai, baseSettings.getAnnotationIntrospector());
        assertEquals(vc, baseSettings.getVisibilityChecker());
        assertEquals(pns, baseSettings.getPropertyNamingStrategy());
        assertEquals(tf, baseSettings.getTypeFactory());
        assertEquals(typer, baseSettings.getTypeResolverBuilder());
        assertEquals(dateFormat, baseSettings.getDateFormat());
        assertEquals(hi, baseSettings.getHandlerInstantiator());
        assertEquals(locale, baseSettings.getLocale());
        assertEquals(timeZone, baseSettings.getTimeZone());
        assertEquals(defaultBase64, baseSettings.getBase64Variant());
    }

    // Edge case for BaseSettings constructor where dateFormat is null
    @Test
    public void testBaseSettingsWithNullDateFormat() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        DateFormat dateFormat = null;
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = new Locale("en", "US");
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Base64Variant defaultBase64 = new Base64Variant();
        
        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);

        assertNotNull(baseSettings);
        assertEquals(ci, baseSettings.getClassIntrospector());
        assertEquals(ai, baseSettings.getAnnotationIntrospector());
        assertEquals(vc, baseSettings.getVisibilityChecker());
        assertEquals(pns, baseSettings.getPropertyNamingStrategy());
        assertEquals(tf, baseSettings.getTypeFactory());
        assertEquals(typer, baseSettings.getTypeResolverBuilder());
        assertNull(baseSettings.getDateFormat()); // Expecting null
        assertEquals(hi, baseSettings.getHandlerInstantiator());
        assertEquals(locale, baseSettings.getLocale());
        assertEquals(timeZone, baseSettings.getTimeZone());
        assertEquals(defaultBase64, baseSettings.getBase64Variant());
    }

    // Error scenario for BaseSettings constructor where timeZone is null
    @Test(expected = IllegalArgumentException.class)
    public void testBaseSettingsWithNullTimeZone() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = new Locale("en", "US");
        TimeZone timeZone = null;
        Base64Variant defaultBase64 = new Base64Variant();

        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);
    }
}