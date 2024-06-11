package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testGetClassIntrospector() {
        // Assuming you have an instance of BaseSettings
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        ClassIntrospector classIntrospector = settings.getClassIntrospector();
        assertNotNull(classIntrospector);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        AnnotationIntrospector annotationIntrospector = settings.getAnnotationIntrospector();
        assertNotNull(annotationIntrospector);
    }

    @Test
    public void testGetVisibilityChecker() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        VisibilityChecker<?> visibilityChecker = settings.getVisibilityChecker();
        assertNotNull(visibilityChecker);
    }

    @Test
    public void testGetPropertyNamingStrategy() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        PropertyNamingStrategy propertyNamingStrategy = settings.getPropertyNamingStrategy();
        assertNotNull(propertyNamingStrategy);
    }

    @Test
    public void testGetTypeFactory() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        TypeFactory typeFactory = settings.getTypeFactory();
        assertNotNull(typeFactory);
    }

    @Test
    public void testGetTypeResolverBuilder() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        TypeResolverBuilder<?> typeResolverBuilder = settings.getTypeResolverBuilder();
        assertNotNull(typeResolverBuilder);
    }

    @Test
    public void testGetDateFormat() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        DateFormat dateFormat = settings.getDateFormat();
        assertNull(dateFormat);
    }

    @Test
    public void testGetHandlerInstantiator() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        HandlerInstantiator handlerInstantiator = settings.getHandlerInstantiator();
        assertNotNull(handlerInstantiator);
    }

    @Test
    public void testGetLocale() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        Locale locale = settings.getLocale();
        assertNull(locale);
    }

    @Test
    public void testGetTimeZone() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        TimeZone timeZone = settings.getTimeZone();
        assertNull(timeZone);
    }

    @Test
    public void testGetBase64Variant() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        Base64Variant base64Variant = settings.getBase64Variant();
        assertNull(base64Variant);
    }

    @Test
    public void testWithClassIntrospector() {
        // Assuming you have an instance of BaseSettings
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        ClassIntrospector classIntrospector = new MockClassIntrospector();
        BaseSettings newSettings = settings.withClassIntrospector(classIntrospector);
        assertEquals(classIntrospector, newSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        // Assuming you have an instance of BaseSettings
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        AnnotationIntrospector annotationIntrospector = new MockAnnotationIntrospector();
        BaseSettings newSettings = settings.withAnnotationIntrospector(annotationIntrospector);
        assertEquals(annotationIntrospector, newSettings.getAnnotationIntrospector());
    }

    // Add more test methods for other methods as needed

    private static class MockClassIntrospector extends ClassIntrospector {}
    private static class MockAnnotationIntrospector extends AnnotationIntrospector {}
}