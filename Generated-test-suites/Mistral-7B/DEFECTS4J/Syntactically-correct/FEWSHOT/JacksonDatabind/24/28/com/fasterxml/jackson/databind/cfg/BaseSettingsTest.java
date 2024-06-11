package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testGetters() {
        ClassIntrospector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<Object> visibilityChecker = new VisibilityChecker.Std();
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.Default();
        TypeFactory typeFactory = new TypeFactory();
        TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
        Locale locale = Locale.ENGLISH;
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant base64Variant = Base64Variant.DEFAULT;

        BaseSettings settings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker,
                propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone,
                base64Variant);

        assertSame(classIntrospector, settings.getClassIntrospector());
        assertSame(annotationIntrospector, settings.getAnnotationIntrospector());
        assertSame(visibilityChecker, settings.getVisibilityChecker());
        assertSame(propertyNamingStrategy, settings.getPropertyNamingStrategy());
        assertSame(typeFactory, settings.getTypeFactory());
        assertSame(typeResolverBuilder, settings.getTypeResolverBuilder());
        assertSame(dateFormat, settings.getDateFormat());
        assertSame(handlerInstantiator, settings.getHandlerInstantiator());
        assertSame(locale, settings.getLocale());
        assertSame(timeZone, settings.getTimeZone());
        assertSame(base64Variant, settings.getBase64Variant());

        // Test null values
        DateFormat dateFormatNull = null;
        BaseSettings settingsNullDateFormat = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker,
                propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormatNull, handlerInstantiator, locale, timeZone,
                base64Variant);
        assertNotNull(settingsNullDateFormat.getDateFormat());
        assertSame(dateFormatNull, settingsNullDateFormat.getDateFormat());
    }
}