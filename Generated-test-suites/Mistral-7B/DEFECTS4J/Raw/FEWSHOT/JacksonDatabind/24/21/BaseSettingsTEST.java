package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testGetters() {
        ClassIntrospector classIntrospector = mock(ClassIntrospector.class);
        AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
        VisibilityChecker<?> visibilityChecker = mock(VisibilityChecker.class);
        PropertyNamingStrategy propertyNamingStrategy = mock(PropertyNamingStrategy.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        TypeResolverBuilder<?> typeResolverBuilder = mock(TypeResolverBuilder.class);
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = mock(HandlerInstantiator.class);
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.STANDARD;

        BaseSettings baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker,
                propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone,
                defaultBase64);

        assertEquals(classIntrospector, baseSettings.getClassIntrospector());
        assertEquals(annotationIntrospector, baseSettings.getAnnotationIntrospector());
        assertEquals(visibilityChecker, baseSettings.getVisibilityChecker());
        assertEquals(propertyNamingStrategy, baseSettings.getPropertyNamingStrategy());
        assertEquals(typeFactory, baseSettings.getTypeFactory());
        assertEquals(typeResolverBuilder, baseSettings.getTypeResolverBuilder());
        assertEquals(dateFormat, baseSettings.getDateFormat());
        assertEquals(handlerInstantiator, baseSettings.getHandlerInstantiator());
        assertEquals(locale, baseSettings.getLocale());
        assertEquals(timeZone, baseSettings.getTimeZone());
        assertEquals(defaultBase64, baseSettings.getBase64Variant());
    }

    @Test
    public void testGettersWithCustomSettings() {
        ClassIntrospector classIntrospector = mock(ClassIntrospector.class);
        AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
        VisibilityChecker<?> visibilityChecker = mock(VisibilityChecker.class);
        PropertyNamingStrategy propertyNamingStrategy = mock(PropertyNamingStrategy.class);
        TypeFactory typeFactory = mock(TypeFactory.class);
        TypeResolverBuilder<?> typeResolverBuilder = mock(TypeResolverBuilder.class);
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = mock(HandlerInstantiator.class);
        Locale locale = new Locale("en", "US");
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Base64Variant defaultBase64 = Base64Variant.BASE64_EXTENDED;

        BaseSettings baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker,
                propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone,
                defaultBase64);

        assertEquals(classIntrospector, baseSettings.getClassIntrospector());
        assertEquals(annotationIntrospector, baseSettings.getAnnotationIntrospector());
        assertEquals(visibilityChecker, baseSettings.getVisibilityChecker());
        assertEquals(propertyNamingStrategy, baseSettings.getPropertyNamingStrategy());
        assertEquals(typeFactory, baseSettings.getTypeFactory());
        assertEquals(typeResolverBuilder, baseSettings.getTypeResolverBuilder());
        assertEquals(dateFormat, baseSettings.getDateFormat());
        assertEquals(handlerInstantiator, baseSettings.getHandlerInstantiator());
        assertEquals(locale, baseSettings.getLocale());
        assertEquals(timeZone, baseSettings.getTimeZone());
        assertEquals(defaultBase64, baseSettings.getBase64Variant());
    }
}