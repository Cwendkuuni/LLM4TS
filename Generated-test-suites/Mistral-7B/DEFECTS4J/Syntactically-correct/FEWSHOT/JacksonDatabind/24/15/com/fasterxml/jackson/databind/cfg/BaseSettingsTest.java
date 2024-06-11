package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Test case for {@link BaseSettings} class.
 */
public class BaseSettingsTest {

    @Test
    public void testBaseSettings() {
        // Test with default values
        BaseSettings defaultSettings = new BaseSettings(null, null, null, null, null, null, null, null, Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.getDefault());
        assertNull(defaultSettings.getClassIntrospector());
        assertNull(defaultSettings.getAnnotationIntrospector());
        assertNull(defaultSettings.getVisibilityChecker());
        assertNull(defaultSettings.getPropertyNamingStrategy());
        assertNull(defaultSettings.getTypeFactory());
        assertNull(defaultSettings.getTypeResolverBuilder());
        assertNull(defaultSettings.getDateFormat());
        assertNull(defaultSettings.getHandlerInstantiator());
        assertSame(Locale.getDefault(), defaultSettings.getLocale());
        assertSame(TimeZone.getDefault(), defaultSettings.getTimeZone());
        assertSame(Base64Variant.getDefault(), defaultSettings.getBase64Variant());

        // Test with custom values
        AnnotationIntrospector annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
        ClassIntrospector classIntrospector = Mockito.mock(ClassIntrospector.class);
        VisibilityChecker<Object> visibilityChecker = Mockito.mock(VisibilityChecker.class);
        PropertyNamingStrategy propertyNamingStrategy = Mockito.mock(PropertyNamingStrategy.class);
        TypeFactory typeFactory = Mockito.mock(TypeFactory.class);
        TypeResolverBuilder<?> typeResolverBuilder = Mockito.mock(TypeResolverBuilder.class);
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = Mockito.mock(HandlerInstantiator.class);
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Base64Variant base64Variant = Base64Variant.STANDARD;

        BaseSettings customSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy,
                typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, base64Variant);

        assertSame(classIntrospector, customSettings.getClassIntrospector());
        assertSame(annotationIntrospector, customSettings.getAnnotationIntrospector());
        assertSame(visibilityChecker, customSettings.getVisibilityChecker());
        assertSame(propertyNamingStrategy, customSettings.getPropertyNamingStrategy());
        assertSame(typeFactory, customSettings.getTypeFactory());
        assertSame(typeResolverBuilder, customSettings.getTypeResolverBuilder());
        assertSame(dateFormat, customSettings.getDateFormat());
        assertSame(handlerInstantiator, customSettings.getHandlerInstantiator());
        assertSame(locale, customSettings.getLocale());
        assertSame(timeZone, customSettings.getTimeZone());
        assertSame(base64Variant, customSettings.getBase64Variant());
    }

    @Test
    public void testGetters() {
        AnnotationIntrospector annotationIntrospector = Mockito.mock(AnnotationIntrospector.class);
        ClassIntrospector classIntrospector = Mockito.mock(ClassIntrospector.class);
        VisibilityChecker<Object> visibilityChecker = Mockito.mock(VisibilityChecker.class);
        PropertyNamingStrategy propertyNamingStrategy = Mockito.mock(PropertyNamingStrategy.class);
        TypeFactory typeFactory = Mockito.mock(TypeFactory.class);
        TypeResolverBuilder<?> typeResolverBuilder = Mockito.mock(TypeResolverBuilder.class);
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = Mockito.mock(HandlerInstantiator.class);
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Base64Variant base64Variant = Base64Variant.STANDARD;

        BaseSettings settings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy,
                typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, base64Variant);

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
    }
}