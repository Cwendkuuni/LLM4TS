package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testGetters() {
        ClassIntrospector classIntrospector = Mockito.mock(ClassIntrospector.class);
        AnnotationIntrospector annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
        VisibilityChecker<?> visibilityChecker = Mockito.mock(VisibilityChecker.class);
        PropertyNamingStrategy propertyNamingStrategy = Mockito.mock(PropertyNamingStrategy.class);
        TypeFactory typeFactory = Mockito.mock(TypeFactory.class);
        TypeResolverBuilder<?> typeResolverBuilder = Mockito.mock(TypeResolverBuilder.class);
        DateFormat dateFormat = Mockito.mock(DateFormat.class);
        HandlerInstantiator handlerInstantiator = Mockito.mock(HandlerInstantiator.class);
        Locale locale = Mockito.mock(Locale.class);
        TimeZone timeZone = Mockito.mock(TimeZone.class);
        Base64Variant base64Variant = Mockito.mock(Base64Variant.class);

        BaseSettings settings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, base64Variant);

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
        assertEquals(base64Variant, settings.getBase64Variant());
    }

    @Test
    public void testSetters() {
        ClassIntrospector classIntrospector = Mockito.mock(ClassIntrospector.class);
        AnnotationIntrospector annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
        VisibilityChecker<?> visibilityChecker = Mockito.mock(VisibilityChecker.class);
        PropertyNamingStrategy propertyNamingStrategy = Mockito.mock(PropertyNamingStrategy.class);
        TypeFactory typeFactory = Mockito.mock(TypeFactory.class);
        TypeResolverBuilder<?> typeResolverBuilder = Mockito.mock(TypeResolverBuilder.class);
        DateFormat dateFormat = Mockito.mock(DateFormat.class);
        HandlerInstantiator handlerInstantiator = Mockito.mock(HandlerInstantiator.class);
        Locale locale = Mockito.mock(Locale.class);
        TimeZone timeZone = Mockito.mock(TimeZone.class);
        Base64Variant base64Variant = Mockito.mock(Base64Variant.class);

        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);

        BaseSettings newSettings = settings.withClassIntrospector(classIntrospector);
        assertEquals(classIntrospector, newSettings.getClassIntrospector());
        assertNotSame(settings, newSettings);

        newSettings = newSettings.withAnnotationIntrospector(annotationIntrospector);
        assertEquals(annotationIntrospector, newSettings.getAnnotationIntrospector());
        assertNotSame(settings, newSettings);

        newSettings = newSettings.withInsertedAnnotationIntrospector(annotationIntrospector);
        assertEquals(annotationIntrospector, newSettings.getAnnotationIntrospector());
        assertNotSame(settings, newSettings);

        newSettings = newSettings.withAppendedAnnotationIntrospector(annotationIntrospector);
        assertEquals(annotationIntrospector, newSettings.getAnnotationIntrospector());
        assertNotSame(settings, newSettings);

        newSettings = newSettings.withVisibilityChecker(visibilityChecker);
        assertEquals(visibilityChecker, newSettings.getVisibilityChecker());
        assertNotSame(settings, newSettings);

        newSettings = newSettings.withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);
        assertNotSame(settings, newSettings);

        newSettings = newSettings.withPropertyNamingStrategy(propertyNamingStrategy);
        assertEquals(propertyNamingStrategy, newSettings.getPropertyNamingStrategy());
        assertNotSame(settings, newSettings);

        newSettings = newSettings.withTypeFactory(typeFactory);
        assertEquals(typeFactory, newSettings.getTypeFactory());
        assertNotSame(settings, newSettings);

        newSettings = newSettings.withTypeResolverBuilder(typeResolverBuilder);
        assertEquals(typeResolverBuilder, newSettings.getTypeResolverBuilder());
        assertNotSame(settings, newSettings);

        newSettings = newSettings.withDateFormat(dateFormat);
        assertEquals(dateFormat, newSettings.getDateFormat());
        assertNotSame(settings, newSettings);

        newSettings = newSettings.withHandlerInstantiator(handlerInstantiator);
        assertEquals(handlerInstantiator, newSettings.getHandlerInstantiator());
        assertNotSame(settings, newSettings);

        newSettings = newSettings.with(locale);
        assertEquals(locale, newSettings.getLocale());
        assertNotSame(settings, newSettings);

        newSettings = newSettings.with(timeZone);
        assertEquals(timeZone, newSettings.getTimeZone());
        assertNotSame(settings, newSettings);

        newSettings = newSettings.with(base64Variant);
        assertEquals(base64Variant, newSettings.getBase64Variant());
        assertNotSame(settings, newSettings);

        newSettings = newSettings.with(null);
        assertNull(newSettings.getBase64Variant());
        assertNotSame(settings, newSettings);
    }
}