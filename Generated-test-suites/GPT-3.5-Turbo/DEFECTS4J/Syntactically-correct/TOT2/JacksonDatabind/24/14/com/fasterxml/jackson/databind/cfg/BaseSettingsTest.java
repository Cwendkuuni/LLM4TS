package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testConstruction() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker vc = new VisibilityChecker();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.US;
        TimeZone tz = TimeZone.getDefault();
        Base64Variant defaultBase64 = new Base64Variant();

        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);

        assertNotNull(settings);
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(), new VisibilityChecker(), new PropertyNamingStrategy(), new TypeFactory(), new TypeResolverBuilder(), new SimpleDateFormat("yyyy-MM-dd"), new HandlerInstantiator(), Locale.US, TimeZone.getDefault(), new Base64Variant());

        BaseSettings newSettings = settings.withClassIntrospector(ci);

        assertNotEquals(settings, newSettings);
        assertEquals(newSettings.getClassIntrospector(), ci);
    }

    // Add more test cases for other public methods here

}