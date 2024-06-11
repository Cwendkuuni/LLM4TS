package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.Introspector;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {

    @Test
    public void testBaseSettings() {
        Introspector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<Object> visibilityChecker = new VisibilityChecker.Default<>();
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.Default();
        TypeFactory typeFactory = new TypeFactory();
        TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
        StdDateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variants.STANDARD;

        BaseSettings originalSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
                typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);

        BaseSettings copySettingsWithClassIntrospector = originalSettings.withClassIntrospector(new ClassIntrospector());
        assertEquals(originalSettings.getClassIntrospector(), copySettingsWithClassIntrospector.getClassIntrospector());
        assertNotSame(originalSettings.getClassIntrospector(), copySettingsWithClassIntrospector.getClassIntrospector());

        BaseSettings copySettingsWithAnnotationIntrospector = originalSettings.withAnnotationIntrospector(new AnnotationIntrospector());
        assertEquals(originalSettings.getAnnotationIntrospector(), copySettingsWithAnnotationIntrospector.getAnnotationIntrospector());
        assertNotSame(originalSettings.getAnnotationIntrospector(), copySettingsWithAnnotationIntrospector.getAnnotationIntrospector());

        BaseSettings copySettingsWithVisibilityChecker = originalSettings.withVisibilityChecker(new VisibilityChecker.Default<Object>());
        assertEquals(originalSettings.getVisibilityChecker(), copySettingsWithVisibilityChecker.getVisibilityChecker());
        assertNotSame(originalSettings.getVisibilityChecker(), copySettingsWithVisibilityChecker.getVisibilityChecker());

        BaseSettings copySettingsWithPropertyNamingStrategy = originalSettings.withPropertyNamingStrategy(new PropertyNamingStrategy.Default());
        assertEquals(originalSettings.getPropertyNamingStrategy(), copySettingsWithPropertyNamingStrategy.getPropertyNamingStrategy());
        assertNotSame(originalSettings.getPropertyNamingStrategy(), copySettingsWithPropertyNamingStrategy.getPropertyNamingStrategy());

        BaseSettings copySettingsWithTypeFactory = originalSettings.withTypeFactory(new TypeFactory());
        assertEquals(originalSettings.getTypeFactory(), copySettingsWithTypeFactory.getTypeFactory());
        assertNotSame(originalSettings.getTypeFactory(), copySettingsWithTypeFactory.getTypeFactory());

        BaseSettings copySettingsWithTypeResolverBuilder = originalSettings.withTypeResolverBuilder(new TypeResolverBuilderDefault());
        assertEquals(originalSettings.getTypeResolverBuilder(), copySettingsWithTypeResolverBuilder.getTypeResolverBuilder());
        assertNotSame(originalSettings.getTypeResolverBuilder(), copySettingsWithTypeResolverBuilder.getTypeResolverBuilder());

        BaseSettings copySettingsWithDateFormat = originalSettings.withDateFormat(new StdDateFormat());
        assertEquals(originalSettings.getDateFormat(), copySettingsWithDateFormat.getDateFormat());
        assertNotSame(originalSettings.getDateFormat(), copySettingsWithDateFormat.getDateFormat());

        BaseSettings copySettingsWithHandlerInstantiator = originalSettings.withHandlerInstantiator(new HandlerInstantiator());
        assertEquals(originalSettings.getHandlerInstantiator(), copySettingsWithHandlerInstantiator.getHandlerInstantiator());
        assertNotSame(originalSettings.getHandlerInstantiator(), copySettingsWithHandlerInstantiator.getHandlerInstantiator());

        BaseSettings copySettingsWithLocale = originalSettings.with(locale);
        assertEquals(originalSettings.getLocale(), copySettingsWithLocale.getLocale());
        assertNotSame(originalSettings.getLocale(), copySettingsWithLocale.getLocale());

        BaseSettings copySettingsWithTimeZone = originalSettings.with(timeZone);
        assertEquals(originalSettings.getTimeZone(), copySettingsWithTimeZone.getTimeZone());
        assertNotSame(originalSettings.getTimeZone(), copySettingsWithTimeZone.getTimeZone());

        BaseSettings copySettingsWithBase64Variant = originalSettings.with(Base64Variants.URL_SAFE);
        assertEquals(originalSettings.getBase64Variant(), copySettingsWithBase64Variant.getBase64Variant());
        assertNotSame(originalSettings.getBase64Variant(), copySettingsWithBase64Variant.getBase64Variant());
    }
}