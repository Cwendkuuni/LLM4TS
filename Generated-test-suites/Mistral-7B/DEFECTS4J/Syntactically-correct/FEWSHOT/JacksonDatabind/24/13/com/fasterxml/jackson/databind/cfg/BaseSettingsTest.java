package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testGetters() {
        ClassIntrospector classIntrospector = new ClassIntrospector() {};
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector() {};
        VisibilityChecker<?> visibilityChecker = new VisibilityChecker<Object>() {};
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy() {};
        TypeFactory typeFactory = new TypeFactory() {};
        TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilder<Object>() {};
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {};
        Locale locale = Locale.ENGLISH;
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant base64Variant = Base64Variant.STANDARD;

        BaseSettings settings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy,
                typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, base64Variant);

        assertEquals(classIntrospector, settings.getClassIntrospector());
        assertEquals(annotationIntrospector, settings.getAnnotationIntrospector());
        assertNotNull(visibilityChecker);
        assertEquals(propertyNamingStrategy, settings.getPropertyNamingStrategy());
        assertEquals(typeFactory, settings.getTypeFactory());
        assertEquals(typeResolverBuilder, settings.getTypeResolverBuilder());
        assertEquals(dateFormat, settings.getDateFormat());
        assertEquals(handlerInstantiator, settings.getHandlerInstantiator());
        assertEquals(locale, settings.getLocale());
        assertEquals(timeZone, settings.getTimeZone());
        assertEquals(base64Variant, settings.getBase64Variant());
    }
}