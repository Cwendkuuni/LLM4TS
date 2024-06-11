package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.PropertyNamingStrategy;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {

    @Test
    public void testBaseSettings() {
        // Create an instance of BaseSettings with default values
        BaseSettings defaultSettings = new BaseSettings(
                new StandardClassIntrospector(),
                new StandardAnnotationIntrospector(),
                new Jdk8VisibilityChecker(),
                PropertyNamingStrategy.STANDARD_EXCLUSIVE,
                new StandardTypeFactory(),
                new StandardTypeResolverBuilder(),
                new StdDateFormat(),
                new JacksonAnnotationIntrospector(),
                Locale.getDefault(),
                TimeZone.getDefault(),
                Base64Variant.DEFAULT);

        // Test getter methods for default values
        ClassIntrospector classIntrospector = defaultSettings.getClassIntrospector();
        AnnotationIntrospector annotationIntrospector = defaultSettings.getAnnotationIntrospector();
        VisibilityChecker<?> visibilityChecker = defaultSettings.getVisibilityChecker();
        PropertyNamingStrategy propertyNamingStrategy = defaultSettings.getPropertyNamingStrategy();
        TypeFactory typeFactory = defaultSettings.getTypeFactory();
        TypeResolverBuilder<?> typeResolverBuilder = defaultSettings.getTypeResolverBuilder();
        DateFormat dateFormat = defaultSettings.getDateFormat();
        HandlerInstantiator handlerInstantiator = defaultSettings.getHandlerInstantiator();
        Locale locale = defaultSettings.getLocale();
        TimeZone timeZone = defaultSettings.getTimeZone();
        Base64Variant base64Variant = defaultSettings.getBase64Variant();

        // Test getter methods for customized values
        BaseSettings customSettings = new BaseSettings(
                new StandardClassIntrospector(),
                new StandardAnnotationIntrospector(),
                new Jdk8VisibilityChecker(),
                PropertyNamingStrategy.CAMEL_CASE_TO_UNDERSCORES,
                new StandardTypeFactory(),
                new StandardTypeResolverBuilder(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                new JacksonAnnotationIntrospector(),
                Locale.US,
                TimeZone.getTimeZone("UTC"),
                Base64Variant.URL_SAFE);

        ClassIntrospector customClassIntrospector = customSettings.getClassIntrospector();
        AnnotationIntrospector customAnnotationIntrospector = customSettings.getAnnotationIntrospector();
        VisibilityChecker<?> customVisibilityChecker = customSettings.getVisibilityChecker();
        PropertyNamingStrategy customPropertyNamingStrategy = customSettings.getPropertyNamingStrategy();
        TypeFactory customTypeFactory = customSettings.getTypeFactory();
        TypeResolverBuilder<?> customTypeResolverBuilder = customSettings.getTypeResolverBuilder();
        DateFormat customDateFormat = customSettings.getDateFormat();
        HandlerInstantiator customHandlerInstantiator = customSettings.getHandlerInstantiator();
        Locale customLocale = customSettings.getLocale();
        TimeZone customTimeZone = customSettings.getTimeZone();
        Base64Variant customBase64Variant = customSettings.getBase64Variant();

        // Test getClassIntrospector method
        assertEquals(classIntrospector, defaultSettings.getClassIntrospector());
        assertEquals(customClassIntrospector, customSettings.getClassIntrospector());

        // Test getAnnotationIntrospector method
        assertEquals(annotationIntrospector, defaultSettings.getAnnotationIntrospector());
        assertEquals(customAnnotationIntrospector, customSettings.getAnnotationIntrospector());

        // Test getVisibilityChecker method
        assertEquals(visibilityChecker, defaultSettings.getVisibilityChecker());
        assertNotSame(visibilityChecker, customSettings.getVisibilityChecker());

        // Test getPropertyNamingStrategy method
        assertEquals(propertyNamingStrategy, defaultSettings.getPropertyNamingStrategy());
        assertNotSame(propertyNamingStrategy, customSettings.getPropertyNamingStrategy());

        // Test getTypeFactory method
        assertEquals(typeFactory, defaultSettings.getTypeFactory());
        assertEquals(customTypeFactory, customSettings.getTypeFactory());

        // Test getTypeResolverBuilder method
        assertEquals(typeResolverBuilder, defaultSettings.getTypeResolverBuilder());
        assertNotSame(typeResolverBuilder, customSettings.getTypeResolverBuilder());

        // Test getDateFormat method
        assertEquals(dateFormat, defaultSettings.getDateFormat());
        assertNotSame(dateFormat, customDateFormat);

        // Test getHandlerInstantiator method
        assertEquals(handlerInstantiator, defaultSettings.getHandlerInstantiator());
        assertNotSame(handlerInstantiator, customSettings.getHandlerInstantiator());

        // Test getLocale method
        assertEquals(locale, defaultSettings.getLocale());
        assertNotSame(locale, customLocale);

        // Test getTimeZone method
        assertEquals(timeZone, defaultSettings.getTimeZone());
        assertNotSame(timeZone, customTimeZone);

        // Test getBase64Variant method
        assertEquals(base64Variant, defaultSettings.getBase64Variant());
        assertNotSame(base64Variant, customBase64Variant);

        // Test factory methods
        BaseSettings defaultSettingsWithClassIntrospector = defaultSettings.withClassIntrospector(new CustomClassIntrospector());
        assertNotSame(defaultSettings, defaultSettingsWithClassIntrospector);
        assertEquals(defaultSettingsWithClassIntrospector.getClassIntrospector(), new CustomClassIntrospector());

        BaseSettings customSettingsWithAnnotationIntrospector = customSettings.withAnnotationIntrospector(new CustomAnnotationIntrospector());
        assertNotSame(customSettings, customSettingsWithAnnotationIntrospector);
        assertEquals(customSettingsWithAnnotationIntrospector.getAnnotationIntrospector(), new CustomAnnotationIntrospector());
    }
}

class CustomClassIntrospector extends StandardClassIntrospector {}
class CustomAnnotationIntrospector extends StandardAnnotationIntrospector {}