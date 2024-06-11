package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testBaseSettings() {
        // Test default constructor
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

        // Test constructor with arguments
        ClassIntrospector classIntrospector = null;
        AnnotationIntrospector annotationIntrospector = null;
        VisibilityChecker<?> visibilityChecker = null;
        PropertyNamingStrategy propertyNamingStrategy = null;
        TypeFactory typeFactory = null;
        TypeResolverBuilder<?> typeResolverBuilder = null;
        DateFormat dateFormat = null;
        HandlerInstantiator handlerInstantiator = null;
        Locale locale = null;
        TimeZone timeZone = null;
        Base64Variant base64Variant = null;
        BaseSettings baseSettings1 = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, base64Variant);
        assertEquals(classIntrospector, baseSettings1.getClassIntrospector());
        assertEquals(annotationIntrospector, baseSettings1.getAnnotationIntrospector());
        assertEquals(visibilityChecker, baseSettings1.getVisibilityChecker());
        assertEquals(propertyNamingStrategy, baseSettings1.getPropertyNamingStrategy());
        assertEquals(typeFactory, baseSettings1.getTypeFactory());
        assertEquals(typeResolverBuilder, baseSettings1.getTypeResolverBuilder());
        assertEquals(dateFormat, baseSettings1.getDateFormat());
        assertEquals(handlerInstantiator, baseSettings1.getHandlerInstantiator());
        assertEquals(locale, baseSettings1.getLocale());
        assertEquals(timeZone, baseSettings1.getTimeZone());
        assertEquals(base64Variant, baseSettings1.getBase64Variant());
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector classIntrospector = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings1 = baseSettings.withClassIntrospector(classIntrospector);
        assertEquals(classIntrospector, baseSettings1.getClassIntrospector());
        assertNotSame(baseSettings, baseSettings1);
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings1 = baseSettings.withAnnotationIntrospector(annotationIntrospector);
        assertEquals(annotationIntrospector, baseSettings1.getAnnotationIntrospector());
        assertNotSame(baseSettings, baseSettings1);
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings1 = baseSettings.withInsertedAnnotationIntrospector(annotationIntrospector);
        assertEquals(annotationIntrospector, baseSettings1.getAnnotationIntrospector());
        assertNotSame(baseSettings, baseSettings1);
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings1 = baseSettings.withAppendedAnnotationIntrospector(annotationIntrospector);
        assertEquals(annotationIntrospector, baseSettings1.getAnnotationIntrospector());
        assertNotSame(baseSettings, baseSettings1);
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker<?> visibilityChecker = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings1 = baseSettings.withVisibilityChecker(visibilityChecker);
        assertEquals(visibilityChecker, baseSettings1.getVisibilityChecker());
        assertNotSame(baseSettings, baseSettings1);
    }

    @Test
    public void testWithVisibility() {
        PropertyAccessor forMethod = null;
        JsonAutoDetect.Visibility visibility = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings1 = baseSettings.withVisibility(forMethod, visibility);
        assertEquals(visibility, baseSettings1.getVisibilityChecker().forProperty(forMethod));
        assertNotSame(baseSettings, baseSettings1);
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy propertyNamingStrategy = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings1 = baseSettings.withPropertyNamingStrategy(propertyNamingStrategy);
        assertEquals(propertyNamingStrategy, baseSettings1.getPropertyNamingStrategy());
        assertNotSame(baseSettings, baseSettings1);
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory typeFactory = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings1 = baseSettings.withTypeFactory(typeFactory);
        assertEquals(typeFactory, baseSettings1.getTypeFactory());
        assertNotSame(baseSettings, baseSettings1);
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typeResolverBuilder = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings1 = baseSettings.withTypeResolverBuilder(typeResolverBuilder);
        assertEquals(typeResolverBuilder, baseSettings1.getTypeResolverBuilder());
        assertNotSame(baseSettings, baseSettings1);
    }

    @Test
    public void testWithDateFormat() {
        DateFormat dateFormat = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings1 = baseSettings.withDateFormat(dateFormat);
        assertEquals(dateFormat, baseSettings1.getDateFormat());
        assertNotSame(baseSettings, baseSettings1);
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator handlerInstantiator = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings1 = baseSettings.withHandlerInstantiator(handlerInstantiator);
        assertEquals(handlerInstantiator, baseSettings1.getHandlerInstantiator());
        assertNotSame(baseSettings, baseSettings1);
    }

    @Test
    public void testWith() {
        Locale locale = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings1 = baseSettings.with(locale);
        assertEquals(locale, baseSettings1.getLocale());
        assertNotSame(baseSettings, baseSettings1);
    }

    @Test
    public void testWithTimeZone() {
        TimeZone timeZone = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings1 = baseSettings.with(timeZone);
        assertEquals(timeZone, baseSettings1.getTimeZone());
        assertNotSame(baseSettings, baseSettings1);
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant base64Variant = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings baseSettings1 = baseSettings.with(base64Variant);
        assertEquals(base64Variant, baseSettings1.getBase64Variant());
        assertNotSame(baseSettings, baseSettings1);
    }
}