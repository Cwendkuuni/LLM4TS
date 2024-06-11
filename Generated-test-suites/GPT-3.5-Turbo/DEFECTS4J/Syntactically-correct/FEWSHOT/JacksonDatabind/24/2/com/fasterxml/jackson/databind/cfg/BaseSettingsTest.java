package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

public class BaseSettingsTest {
    @Test
    public void testConstructor() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder();
        DateFormat df = DateFormat.getDateInstance();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.US;
        TimeZone tz = TimeZone.getDefault();
        Base64Variant base64 = new Base64Variant();
        
        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, df, hi, locale, tz, base64);
        
        assertEquals(ci, settings.getClassIntrospector());
        assertEquals(ai, settings.getAnnotationIntrospector());
        assertEquals(vc, settings.getVisibilityChecker());
        assertEquals(pns, settings.getPropertyNamingStrategy());
        assertEquals(tf, settings.getTypeFactory());
        assertEquals(typer, settings.getTypeResolverBuilder());
        assertEquals(df, settings.getDateFormat());
        assertEquals(hi, settings.getHandlerInstantiator());
        assertEquals(locale, settings.getLocale());
        assertEquals(tz, settings.getTimeZone());
        assertEquals(base64, settings.getBase64Variant());
    }
    
    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings updatedSettings = settings.withClassIntrospector(ci);
        
        assertEquals(ci, updatedSettings.getClassIntrospector());
        assertNull(updatedSettings.getAnnotationIntrospector());
        assertNull(updatedSettings.getVisibilityChecker());
        assertNull(updatedSettings.getPropertyNamingStrategy());
        assertNull(updatedSettings.getTypeFactory());
        assertNull(updatedSettings.getTypeResolverBuilder());
        assertNull(updatedSettings.getDateFormat());
        assertNull(updatedSettings.getHandlerInstantiator());
        assertNull(updatedSettings.getLocale());
        assertNull(updatedSettings.getTimeZone());
        assertNull(updatedSettings.getBase64Variant());
    }
    
    // Additional test cases for other methods in BaseSettings class
    // ...
}