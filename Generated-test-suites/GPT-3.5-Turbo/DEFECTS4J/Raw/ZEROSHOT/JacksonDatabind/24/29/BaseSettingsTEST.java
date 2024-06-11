package com.fasterxml.jackson.databind.cfg;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setUp() {
        ClassIntrospector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker visibilityChecker = new VisibilityChecker();
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy();
        TypeFactory typeFactory = new TypeFactory();
        TypeResolverBuilder typeResolverBuilder = new TypeResolverBuilder();
        DateFormat dateFormat = new DateFormat();
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
        Locale locale = new Locale();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant base64Variant = new Base64Variant();
        
        baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, base64Variant);
    }

    @Test
    public void testGetClassIntrospector() {
        Assert.assertNotNull(baseSettings.getClassIntrospector());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        Assert.assertNotNull(baseSettings.getAnnotationIntrospector());
    }

    @Test
    public void testGetVisibilityChecker() {
        Assert.assertNotNull(baseSettings.getVisibilityChecker());
    }

    @Test
    public void testGetPropertyNamingStrategy() {
        Assert.assertNotNull(baseSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testGetTypeFactory() {
        Assert.assertNotNull(baseSettings.getTypeFactory());
    }

    @Test
    public void testGetTypeResolverBuilder() {
        Assert.assertNotNull(baseSettings.getTypeResolverBuilder());
    }

    @Test
    public void testGetDateFormat() {
        Assert.assertNotNull(baseSettings.getDateFormat());
    }

    @Test
    public void testGetHandlerInstantiator() {
        Assert.assertNotNull(baseSettings.getHandlerInstantiator());
    }

    @Test
    public void testGetLocale() {
        Assert.assertNotNull(baseSettings.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        Assert.assertNotNull(baseSettings.getTimeZone());
    }

    @Test
    public void testGetBase64Variant() {
        Assert.assertNotNull(baseSettings.getBase64Variant());
    }
}