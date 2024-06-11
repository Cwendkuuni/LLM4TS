package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setUp() {
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

        baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat,
                hi, locale, tz, defaultBase64);
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings updatedSettings = baseSettings.withClassIntrospector(ci);
        assertEquals(ci, updatedSettings.getClassIntrospector());
        assertNotSame(baseSettings, updatedSettings);
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings updatedSettings = baseSettings.withAnnotationIntrospector(ai);
        assertEquals(ai, updatedSettings.getAnnotationIntrospector());
        assertNotSame(baseSettings, updatedSettings);
    }
    
    // Add test methods for other methods in BaseSettings class
    
}