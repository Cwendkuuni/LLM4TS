package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
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
    public void testBaseSettingsWithDefaultConstructor() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNull(settings.getClassIntrospector());
        assertNull(settings.getAnnotationIntrospector());
        assertNull(settings.getVisibilityChecker());
        assertNull(settings.getPropertyNamingStrategy());
        assertNull(settings.getTypeFactory());
        assertNull(settings.getTypeResolverBuilder());
        assertNull(settings.getDateFormat());
        assertNull(settings.getHandlerInstantiator());
        assertNull(settings.getLocale());
        assertNull(settings.getTimeZone());
        assertNull(settings.getBase64Variant());
    }

    @Test
    public void testBaseSettingsWithArguments() {
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = new HandlerInstaniator();
        Locale locale = Locale.ENGLISH;
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant base64 = Base64Variant.STANDARD;

        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, dateFormat, handlerInstantiator, locale,
                timeZone, base64);

        assertEquals(dateFormat, settings.getDateFormat());
        assertEquals(handlerInstantiator, settings.getHandlerInstantiator());
        assertEquals(locale, settings.getLocale());
        assertEquals(timeZone, settings.getTimeZone());
        assertEquals(base64, settings.getBase64Variant());
    }

    @Test
    public void testBaseSettingsWithClassIntrospector() {
        ClassIntrospector classIntrospector = new ClassIntrospector();
        BaseSettings settings = new BaseSettings(classIntrospector, null, null, null, null, null, null, null, null, null, null);
        assertEquals(classIntrospector, settings.getClassIntrospector());
    }

    @Test
    public void testBaseSettingsWithAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        BaseSettings settings = new BaseSettings(null, annotationIntrospector, null, null, null, null, null, null, null, null, null);
        assertEquals(annotationIntrospector, settings.getAnnotationIntrospector());
    }

    @Test
    public void testBaseSettingsWithVisibilityChecker() {
        VisibilityChecker<?> visibilityChecker = new VisibilityChecker<>();
        BaseSettings settings = new BaseSettings(null, null, visibilityChecker, null, null, null, null, null, null, null, null);
        assertEquals(visibilityChecker, settings.getVisibilityChecker());
    }

    @Test
    public void testBaseSettingsWithPropertyNamingStrategy() {
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy();
        BaseSettings settings = new BaseSettings(null, null, null, propertyNamingStrategy, null, null, null, null, null, null, null);
        assertEquals(propertyNamingStrategy, settings.getPropertyNamingStrategy());
    }

    @Test
    public void testBaseSettingsWithTypeFactory() {
        TypeFactory typeFactory = new TypeFactory();
        BaseSettings settings = new BaseSettings(null, null, null, null, typeFactory, null, null, null, null, null, null);
        assertEquals(typeFactory, settings.getTypeFactory());
    }

    @Test
    public void testBaseSettingsWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilder<>();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, typeResolverBuilder, null, null, null, null, null);
        assertEquals(typeResolverBuilder, settings.getTypeResolverBuilder());
    }

    @Test
    public void testBaseSettingsWithDateFormat() {
        DateFormat dateFormat = new StdDateFormat();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, dateFormat, null, null, null, null);
        assertEquals(dateFormat, settings.getDateFormat());
    }

    @Test
    public void testBaseSettingsWithHandlerInstantiator() {
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, handlerInstantiator, null, null, null);
        assertEquals(handlerInstantiator, settings.getHandlerInstantiator());
    }

    @Test
    public void testBaseSettingsWithLocale() {
        Locale locale = Locale.ENGLISH;
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, locale, null, null);
        assertEquals(locale, settings.getLocale());
    }

    @Test
    public void testBaseSettingsWithTimeZone() {
        TimeZone timeZone = TimeZone.getDefault();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, timeZone, null);
        assertEquals(timeZone, settings.getTimeZone());
    }

    @Test
    public void testBaseSettingsWithBase64Variant() {
        Base64Variant base64 = Base64Variant.STANDARD;
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, base64);
        assertEquals(base64, settings.getBase64Variant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSettingsWithNullTimeZone() {
        TimeZone timeZone = null;
        new BaseSettings(null, null, null, null, null, null, null, null, null, timeZone, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSettingsWithNullDateFormat() {
        DateFormat dateFormat = null;
        new BaseSettings(null, null, null, null, null, null, dateFormat, null, null, null, null);
    }
}