package com.fasterxml.jackson.databind.cfg;

import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<?>();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<?>();
        DateFormat dateFormat = new DateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = new Locale();
        TimeZone tz = new TimeZone();
        Base64Variant defaultBase64 = new Base64Variant();
        
        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
        
        BaseSettings newSettings = baseSettings.withClassIntrospector(new ClassIntrospector());
        
        assertNotSame(baseSettings, newSettings);
        assertSame(new ClassIntrospector(), newSettings.getClassIntrospector());
        assertSame(ai, newSettings.getAnnotationIntrospector());
        assertSame(vc, newSettings.getVisibilityChecker());
        assertSame(pns, newSettings.getPropertyNamingStrategy());
        assertSame(tf, newSettings.getTypeFactory());
        assertSame(typer, newSettings.getTypeResolverBuilder());
        assertSame(dateFormat, newSettings.getDateFormat());
        assertSame(hi, newSettings.getHandlerInstantiator());
        assertSame(locale, newSettings.getLocale());
        assertSame(tz, newSettings.getTimeZone());
        assertSame(defaultBase64, newSettings.getBase64Variant());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<?>();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<?>();
        DateFormat dateFormat = new DateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = new Locale();
        TimeZone tz = new TimeZone();
        Base64Variant defaultBase64 = new Base64Variant();
        
        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
        
        BaseSettings newSettings = baseSettings.withAnnotationIntrospector(new AnnotationIntrospector());
        
        assertNotSame(baseSettings, newSettings);
        assertSame(ci, newSettings.getClassIntrospector());
        assertSame(new AnnotationIntrospector(), newSettings.getAnnotationIntrospector());
        assertSame(vc, newSettings.getVisibilityChecker());
        assertSame(pns, newSettings.getPropertyNamingStrategy());
        assertSame(tf, newSettings.getTypeFactory());
        assertSame(typer, newSettings.getTypeResolverBuilder());
        assertSame(dateFormat, newSettings.getDateFormat());
        assertSame(hi, newSettings.getHandlerInstantiator());
        assertSame(locale, newSettings.getLocale());
        assertSame(tz, newSettings.getTimeZone());
        assertSame(defaultBase64, newSettings.getBase64Variant());
    }

    // TODO: Add test methods for the remaining methods in the BaseSettings class

}