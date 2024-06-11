package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.util.HandlerInstantiator;

public class BaseSettingsTest {

    @Test
    public void testBaseSettingsConstructor() {
        ClassIntrospector classIntrospector = null;
        AnnotationIntrospector annotationIntrospector = null;
        VisibilityChecker<?> visibilityChecker = null;
        PropertyNamingStrategy propertyNamingStrategy = null;
        TypeFactory typeFactory = null;
        TypeResolverBuilder<?> typeResolverBuilder = null;
        DateFormat dateFormat = null;
        HandlerInstantiator handlerInstantiator = null;
        Locale locale = null;
        TimeZone timeZone = null;
        Base64Variant defaultBase64 = null;

        BaseSettings baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);

        assertNotNull(baseSettings);
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector classIntrospector = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withClassIntrospector(classIntrospector);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(classIntrospector, newBaseSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withAnnotationIntrospector(annotationIntrospector);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(annotationIntrospector, newBaseSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withInsertedAnnotationIntrospector(annotationIntrospector);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(annotationIntrospector, newBaseSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withAppendedAnnotationIntrospector(annotationIntrospector);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(annotationIntrospector, newBaseSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker<?> visibilityChecker = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withVisibilityChecker(visibilityChecker);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(visibilityChecker, newBaseSettings.getVisibilityChecker());
    }

    @Test
    public void testWithVisibility() {
        PropertyAccessor propertyAccessor = null;
        JsonAutoDetect.Visibility visibility = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withVisibility(propertyAccessor, visibility);

        assertNotSame(baseSettings, newBaseSettings);
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy propertyNamingStrategy = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withPropertyNamingStrategy(propertyNamingStrategy);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(propertyNamingStrategy, newBaseSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory typeFactory = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withTypeFactory(typeFactory);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(typeFactory, newBaseSettings.getTypeFactory());
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typeResolverBuilder = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withTypeResolverBuilder(typeResolverBuilder);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(typeResolverBuilder, newBaseSettings.getTypeResolverBuilder());
    }

    @Test
    public void testWithDateFormat() {
        DateFormat dateFormat = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withDateFormat(dateFormat);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(dateFormat, newBaseSettings.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator handlerInstantiator = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withHandlerInstantiator(handlerInstantiator);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(handlerInstantiator, newBaseSettings.getHandlerInstantiator());
    }

    @Test
    public void testWith() {
        Locale locale = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.with(locale);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(locale, newBaseSettings.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        TimeZone timeZone = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.with(timeZone);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(timeZone, newBaseSettings.getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant base64Variant = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.with(base64Variant);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(base64Variant, newBaseSettings.getBase64Variant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullLocale() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings.with(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullTimeZone() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings.with(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullTimeZoneInDateFormat() {
        DateFormat dateFormat = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, dateFormat, null, null, null, null);
        baseSettings.with(null);
    }
}