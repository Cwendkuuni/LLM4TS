package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

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

public class BaseSettingsTest {

    @Test
    public void testConstructor() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNotNull(baseSettings);
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector classIntrospector = mock(ClassIntrospector.class);
        BaseSettings baseSettings = new BaseSettings(classIntrospector, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withClassIntrospector(null);
        assertNotSame(baseSettings, newBaseSettings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithInvalidAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = null;
        BaseSettings baseSettings = new BaseSettings(null, annotationIntrospector, null, null, null, null, null, null, null, null, null);
        baseSettings.withAnnotationIntrospector(annotationIntrospector);
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withInsertedAnnotationIntrospector(annotationIntrospector);
        assertNotSame(baseSettings, newBaseSettings);
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withAppendedAnnotationIntrospector(annotationIntrospector);
        assertNotSame(baseSettings, newBaseSettings);
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker<?> visibilityChecker = mock(VisibilityChecker.class);
        BaseSettings baseSettings = new BaseSettings(null, null, visibilityChecker, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withVisibilityChecker(null);
        assertNotSame(baseSettings, newBaseSettings);
    }

    @Test
    public void testWithVisibility() {
        PropertyAccessor propertyAccessor = mock(PropertyAccessor.class);
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.NONE;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withVisibility(propertyAccessor, visibility);
        assertNotSame(baseSettings, newBaseSettings);
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy propertyNamingStrategy = mock(PropertyNamingStrategy.class);
        BaseSettings baseSettings = new BaseSettings(null, null, null, propertyNamingStrategy, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withPropertyNamingStrategy(null);
        assertNotSame(baseSettings, newBaseSettings);
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory typeFactory = mock(TypeFactory.class);
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, typeFactory, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withTypeFactory(null);
        assertNotSame(baseSettings, newBaseSettings);
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typeResolverBuilder = mock(TypeResolverBuilder.class);
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, typeResolverBuilder, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withTypeResolverBuilder(null);
        assertNotSame(baseSettings, newBaseSettings);
    }

    @Test
    public void testWithDateFormat() {
        DateFormat dateFormat = new StdDateFormat();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, dateFormat, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withDateFormat(null);
        assertNotSame(baseSettings, newBaseSettings);
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator handlerInstantiator = mock(HandlerInstantiator.class);
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, handlerInstantiator, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withHandlerInstantiator(null);
        assertNotSame(baseSettings, newBaseSettings);
    }

    @Test
    public void testWith() {
        Locale locale = Locale.US;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.with(locale);
        assertNotSame(baseSettings, newBaseSettings);
    }

    @Test
    public void testWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.with(timeZone);
        assertNotSame(baseSettings, newBaseSettings);
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant base64Variant = Base64Variant.STANDARD;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.with(base64Variant);
        assertNotSame(baseSettings, newBaseSettings);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullClassIntrospector() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings.withClassIntrospector(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullAnnotationIntrospector() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings.withAnnotationIntrospector(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullVisibilityChecker() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings.withVisibilityChecker(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullPropertyNamingStrategy() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings.withPropertyNamingStrategy(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullTypeFactory() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings.withTypeFactory(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullTypeResolverBuilder() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings.withTypeResolverBuilder(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullDateFormat() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings.withDateFormat(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullHandlerInstantiator() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings.withHandlerInstantiator(null);
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
    public void testWithNullBase64Variant() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        baseSettings.with(null);
    }
}