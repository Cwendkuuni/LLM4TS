package com.fasterxml.jackson.databind.cfg.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
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

    private BaseSettings baseSettings;

    @Before
    public void setUp() {
        ClassIntrospector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<?> visibilityChecker = new VisibilityChecker.Default();
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.Default();
        TypeFactory typeFactory = new TypeFactory();
        TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderImpl();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.STANDARD;

        baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
                typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);
    }

    @Test
    public void testGetClassIntrospector() {
        ClassIntrospector result = baseSettings.getClassIntrospector();
        assertNotNull(result);
        assertSame(result, baseSettings._classIntrospector);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        AnnotationIntrospector result = baseSettings.getAnnotationIntrospector();
        assertNotNull(result);
        assertSame(result, baseSettings._annotationIntrospector);
    }

    @Test
    public void testGetVisibilityChecker() {
        VisibilityChecker<?> result = baseSettings.getVisibilityChecker();
        assertNotNull(result);
        assertSame(result, baseSettings._visibilityChecker);
    }

    @Test
    public void testGetPropertyNamingStrategy() {
        PropertyNamingStrategy result = baseSettings.getPropertyNamingStrategy();
        assertNotNull(result);
        assertSame(result, baseSettings._propertyNamingStrategy);
    }

    @Test
    public void testGetTypeFactory() {
        TypeFactory result = baseSettings.getTypeFactory();
        assertNotNull(result);
        assertSame(result, baseSettings._typeFactory);
    }

    @Test
    public void testGetTypeResolverBuilder() {
        TypeResolverBuilder<?> result = baseSettings.getTypeResolverBuilder();
        assertNotNull(result);
        assertSame(result, baseSettings._typeResolverBuilder);
    }

    @Test
    public void testGetDateFormat() {
        DateFormat result = baseSettings.getDateFormat();
        assertNotNull(result);
        assertSame(result, baseSettings._dateFormat);
    }

    @Test
    public void testGetHandlerInstantiator() {
        HandlerInstantiator result = baseSettings.getHandlerInstantiator();
        assertNotNull(result);
        assertSame(result, baseSettings._handlerInstantiator);
    }

    @Test
    public void testGetLocale() {
        Locale result = baseSettings.getLocale();
        assertNotNull(result);
        assertSame(result, baseSettings._locale);
    }

    @Test
    public void testGetTimeZone() {
        TimeZone result = baseSettings.getTimeZone();
        assertNotNull(result);
        assertSame(result, baseSettings._timeZone);
    }

    @Test
    public void testGetBase64Variant() {
        Base64Variant result = baseSettings.getBase64Variant();
        assertNotNull(result);
        assertSame(result, baseSettings._defaultBase64);
    }
}

class TypeResolverBuilderImpl implements TypeResolverBuilder<Object> {

    @Override
    public JavaType buildType(Class<?> baseType, Class<?> valueType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JavaType buildType(Class<?> baseType, JavaType valueType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JavaType buildType(JavaType baseType, Class<?> valueType) {
        throw new UnsupportedOperationException();
    }

    @Override
    public JavaType buildType(JavaType baseType, JavaType valueType) {
        throw new UnsupportedOperationException();
    }
}