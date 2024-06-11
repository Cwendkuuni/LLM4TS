package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

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
import com.fasterxml.jackson.databind.util.HandlerInstantiator;

public class BaseSettingsTest {

    @Test
    public void testBaseSettingsConstruction() {
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

        assertNotNull(baseSettings);
        assertSame(classIntrospector, baseSettings.getClassIntrospector());
        assertSame(annotationIntrospector, baseSettings.getAnnotationIntrospector());
        assertSame(visibilityChecker, baseSettings.getVisibilityChecker());
        assertSame(propertyNamingStrategy, baseSettings.getPropertyNamingStrategy());
        assertSame(typeFactory, baseSettings.getTypeFactory());
        assertSame(typeResolverBuilder, baseSettings.getTypeResolverBuilder());
        assertSame(dateFormat, baseSettings.getDateFormat());
        assertSame(handlerInstantiator, baseSettings.getHandlerInstantiator());
        assertSame(locale, baseSettings.getLocale());
        assertSame(timeZone, baseSettings.getTimeZone());
        assertSame(defaultBase64, baseSettings.getBase64Variant());
    }

    @Test
    public void testBaseSettingsWithClassIntrospector() {
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

        BaseSettings newBaseSettings = baseSettings.withClassIntrospector(mock(ClassIntrospector.class));

        assertSame(baseSettings.getClassIntrospector(), baseSettings.getClassIntrospector());
        assertSame(newBaseSettings.getClassIntrospector(), newBaseSettings.getClassIntrospector());
        assertNotSame(baseSettings.getClassIntrospector(), newBaseSettings.getClassIntrospector());
    }

    // Add more test methods here for other factory methods

    @Test
    public void testBaseSettingsGetters() {
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

        BaseSettings baseSettings = new BaseSettings(classIntrospector, annotationIntrospector,
                visibilityChecker, propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale,
                timeZone, defaultBase64);

        assertSame(classIntrospector, baseSettings.getClassIntrospector());
        assertSame(annotationIntrospector, baseSettings.getAnnotationIntrospector());
        assertSame(visibilityChecker, baseSettings.getVisibilityChecker());
        assertSame(propertyNamingStrategy, baseSettings.getPropertyNamingStrategy());
        assertSame(typeFactory, baseSettings.getTypeFactory());
        assertSame(typeResolverBuilder, baseSettings.getTypeResolverBuilder());
        assertSame(dateFormat, baseSettings.getDateFormat());
        assertSame(handlerInstantiator, baseSettings.getHandlerInstantiator());
        assertSame(locale, baseSettings.getLocale());
        assertSame(timeZone, baseSettings.getTimeZone());
        assertSame(defaultBase64, baseSettings.getBase64Variant());
    }
}