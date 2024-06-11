package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

/**
 * Test class for BaseSettings.java
 */
public class BaseSettingsTest {

    @Test
    public void testBaseSettings() {
        // Test constructor with default values
        ClassIntrospector defaultClassIntrospector = new ClassIntrospector();
        AnnotationIntrospector defaultAnnotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<?> defaultVisibilityChecker = new VisibilityChecker.Std();
        PropertyNamingStrategy defaultPropertyNamingStrategy = new PropertyNamingStrategy.DEFAULT();
        TypeFactory defaultTypeFactory = TypeFactory.defaultInstance();
        TypeResolverBuilder<?> defaultTypeResolverBuilder = TypeResolverBuilder.std();
        DateFormat defaultDateFormat = new StdDateFormat();
        HandlerInstantiator defaultHandlerInstantiator = new HandlerInstantiator();
        Locale defaultLocale = Locale.getDefault();
        TimeZone defaultTimeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.STANDARD;

        BaseSettings baseSettings = new BaseSettings(defaultClassIntrospector, defaultAnnotationIntrospector,
                defaultVisibilityChecker, defaultPropertyNamingStrategy, defaultTypeFactory, defaultTypeResolverBuilder,
                defaultDateFormat, defaultHandlerInstantiator, defaultLocale, defaultTimeZone, defaultBase64);

        // Test getters
        ClassIntrospector actualClassIntrospector = baseSettings.getClassIntrospector();
        assertEquals(defaultClassIntrospector, actualClassIntrospector);

        AnnotationIntrospector actualAnnotationIntrospector = baseSettings.getAnnotationIntrospector();
        assertEquals(defaultAnnotationIntrospector, actualAnnotationIntrospector);

        VisibilityChecker<?> actualVisibilityChecker = baseSettings.getVisibilityChecker();
        assertEquals(defaultVisibilityChecker, actualVisibilityChecker);

        PropertyNamingStrategy actualPropertyNamingStrategy = baseSettings.getPropertyNamingStrategy();
        assertEquals(defaultPropertyNamingStrategy, actualPropertyNamingStrategy);

        TypeFactory actualTypeFactory = baseSettings.getTypeFactory();
        assertEquals(defaultTypeFactory, actualTypeFactory);

        TypeResolverBuilder<?> actualTypeResolverBuilder = baseSettings.getTypeResolverBuilder();
        assertEquals(defaultTypeResolverBuilder, actualTypeResolverBuilder);

        DateFormat actualDateFormat = baseSettings.getDateFormat();
        assertEquals(defaultDateFormat, actualDateFormat);

        HandlerInstantiator actualHandlerInstantiator = baseSettings.getHandlerInstantiator();
        assertEquals(defaultHandlerInstantiator, actualHandlerInstantiator);

        Locale actualLocale = baseSettings.getLocale();
        assertEquals(defaultLocale, actualLocale);

        TimeZone actualTimeZone = baseSettings.getTimeZone();
        assertEquals(defaultTimeZone, actualTimeZone);

        Base64Variant actualBase64 = baseSettings.getBase64Variant();
        assertEquals(defaultBase64, actualBase64);

        // Test factory methods
        ClassIntrospector newClassIntrospector = new ClassIntrospector();
        BaseSettings newBaseSettings = baseSettings.withClassIntrospector(newClassIntrospector);
        assertEquals(newClassIntrospector, newBaseSettings.getClassIntrospector());
        assertNotSame(baseSettings, newBaseSettings);

        AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
        BaseSettings newBaseSettings2 = newBaseSettings.withAnnotationIntrospector(newAnnotationIntrospector);
        assertEquals(newAnnotationIntrospector, newBaseSettings2.getAnnotationIntrospector());
        assertNotSame(newBaseSettings, newBaseSettings2);

        // Test other methods
        Locale newLocale = Locale.JAPAN;
        BaseSettings newBaseSettings3 = newBaseSettings2.with(newLocale);
        assertEquals(newLocale, newBaseSettings3.getLocale());
        assertNotSame(newBaseSettings2, newBaseSettings3);

        TimeZone newTimeZone = TimeZone.getTimeZone("UTC");
        BaseSettings newBaseSettings4 = newBaseSettings3.with(newTimeZone);
        assertEquals(newTimeZone, newBaseSettings4.getTimeZone());
        assertNotSame(newBaseSettings3, newBaseSettings4);

        Base64Variant newBase64 = Base64Variant.BASE64_EXTENDED;
        BaseSettings newBaseSettings5 = newBaseSettings4.with(newBase64);
        assertEquals(newBase64, newBaseSettings5.getBase64Variant());
        assertNotSame(newBaseSettings4, newBaseSettings5);
    }
}