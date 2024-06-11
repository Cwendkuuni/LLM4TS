package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testGetters() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, Locale.US, TimeZone.getDefault(), Base64Variant.DEFAULT);
        assertSame(settings._classIntrospector, settings.getClassIntrospector());
        assertSame(settings._annotationIntrospector, settings.getAnnotationIntrospector());
        assertSame(settings._visibilityChecker, settings.getVisibilityChecker());
        assertSame(settings._propertyNamingStrategy, settings.getPropertyNamingStrategy());
        assertSame(settings._typeFactory, settings.getTypeFactory());
        assertSame(settings._typeResolverBuilder, settings.getTypeResolverBuilder());
        assertSame(settings._dateFormat, settings.getDateFormat());
        assertSame(settings._handlerInstantiator, settings.getHandlerInstantiator());
        assertEquals(Locale.US, settings.getLocale());
        assertEquals(TimeZone.getDefault(), settings.getTimeZone());
        assertEquals(Base64Variant.DEFAULT, settings.getBase64Variant());
    }

    @Test
    public void testWithClassIntrospector() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, Locale.US, TimeZone.getDefault(), Base64Variant.DEFAULT);
        BaseSettings newSettings = settings.withClassIntrospector(new ClassIntrospector());
        assertNotSame(settings, newSettings);
        assertNull(newSettings._classIntrospector);
        assertSame(newSettings._annotationIntrospector, settings._annotationIntrospector);
        assertSame(newSettings._visibilityChecker, settings._visibilityChecker);
        assertSame(newSettings._propertyNamingStrategy, settings._propertyNamingStrategy);
        assertSame(newSettings._typeFactory, settings._typeFactory);
        assertSame(newSettings._typeResolverBuilder, settings._typeResolverBuilder);
        assertSame(newSettings._dateFormat, settings._dateFormat);
        assertSame(newSettings._handlerInstantiator, settings._handlerInstantiator);
        assertEquals(Locale.US, newSettings.getLocale());
        assertEquals(TimeZone.getDefault(), newSettings.getTimeZone());
        assertEquals(Base64Variant.DEFAULT, newSettings.getBase64Variant());
        assertSame(newSettings._classIntrospector, newSettings.getClassIntrospector());
    }

    // Add more test cases for all getters and factory methods
}