package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testGetters() {
        ClassIntrospector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<Object> visibilityChecker = new VisibilityChecker.Default();
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.Default();
        TypeFactory typeFactory = new TypeFactory();
        TypeResolverBuilder<?> typeResolverBuilder = new MapTypeResolverBuilder();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variants.BASE64_STANDARD;

        BaseSettings settings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);

        assertEquals(classIntrospector, settings.getClassIntrospector());
        assertEquals(annotationIntrospector, settings.getAnnotationIntrospector());
        assertEquals(visibilityChecker, settings.getVisibilityChecker());
        assertEquals(propertyNamingStrategy, settings.getPropertyNamingStrategy());
        assertEquals(typeFactory, settings.getTypeFactory());
        assertEquals(typeResolverBuilder, settings.getTypeResolverBuilder());
        assertEquals(dateFormat, settings.getDateFormat());
        assertEquals(handlerInstantiator, settings.getHandlerInstantiator());
        assertEquals(locale, settings.getLocale());
        assertEquals(timeZone, settings.getTimeZone());
        assertEquals(defaultBase64, settings.getBase64Variant());
    }

    @Test
    public void testConstructors() {
        ClassIntrospector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<Object> visibilityChecker = new VisibilityChecker.Default();
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.Default();
        TypeFactory typeFactory = new TypeFactory();
        TypeResolverBuilder<?> typeResolverBuilder = new MapTypeResolverBuilder();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variants.BASE64_STANDARD;

        BaseSettings settings1 = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);

        BaseSettings settings2 = new BaseSettings();
        BaseSettings settings2Builder = new BaseSettings.Builder()
                .withClassIntrospector(classIntrospector)
                .withAnnotationIntrospector(annotationIntrospector)
                .withVisibilityChecker(visibilityChecker)
                .withPropertyNamingStrategy(propertyNamingStrategy)
                .withTypeFactory(typeFactory)
                .withTypeResolverBuilder(typeResolverBuilder)
                .withDateFormat(dateFormat)
                .withHandlerInstantiator(handlerInstantiator)
                .withLocale(locale)
                .withTimeZone(timeZone)
                .withBase64Variant(defaultBase64)
                .build();

        assertEquals(settings1, settings2);
    }
}