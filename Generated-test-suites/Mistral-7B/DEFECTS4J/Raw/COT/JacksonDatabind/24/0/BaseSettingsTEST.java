package com.fasterxml.jackson.databind.cfg.test;

import static org.junit.Assert.*;

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
    public void testGetClassIntrospector() {
        BaseSettings settings = new BaseSettings(
                new ClassIntrospector() {}, new AnnotationIntrospector() {},
                new VisibilityChecker.Std() {}, new PropertyNamingStrategyDefault() {},
                new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator() {}, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        ClassIntrospector actual = settings.getClassIntrospector();
        ClassIntrospector expected = new ClassIntrospector();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        BaseSettings settings = new BaseSettings(
                new ClassIntrospector() {}, new AnnotationIntrospector() {},
                new VisibilityChecker.Std() {}, new PropertyNamingStrategyDefault() {},
                new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator() {}, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        AnnotationIntrospector actual = settings.getAnnotationIntrospector();
        AnnotationIntrospector expected = new AnnotationIntrospector();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullLocale() {
        BaseSettings settings = new BaseSettings(
                new ClassIntrospector() {}, new AnnotationIntrospector() {},
                new VisibilityChecker.Std() {}, new PropertyNamingStrategyDefault() {},
                new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator() {}, null, TimeZone.getDefault(), Base64Variant.STANDARD);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullTimeZone() {
        BaseSettings settings = new BaseSettings(
                new ClassIntrospector() {}, new AnnotationIntrospector() {},
                new VisibilityChecker.Std() {}, new PropertyNamingStrategyDefault() {},
                new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator() {}, Locale.getDefault(), null, Base64Variant.STANDARD);
    }

    @Test
    public void testWith() {
        Locale locale = new Locale("fr", "FR");
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        BaseSettings settings = new BaseSettings(
                new ClassIntrospector() {}, new AnnotationIntrospector() {},
                new VisibilityChecker.Std() {}, new PropertyNamingStrategyDefault() {},
                new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator() {}, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings settingsWithLocale = settings.with(locale);
        BaseSettings settingsWithTimeZone = settingsWithLocale.with(timeZone);
        assertEquals(locale, settingsWithTimeZone.getLocale());
        assertEquals(timeZone, settingsWithTimeZone.getTimeZone());
    }

    @Test
    public void testWithDateFormat() {
        DateFormat dateFormat = new StdDateFormat();
        BaseSettings settings = new BaseSettings(
                new ClassIntrospector() {}, new AnnotationIntrospector() {},
                new VisibilityChecker.Std() {}, new PropertyNamingStrategyDefault() {},
                new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator() {}, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings settingsWithDateFormat = settings.withDateFormat(dateFormat);
        DateFormat actual = settingsWithDateFormat.getDateFormat();
        assertEquals(dateFormat, actual);
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {};
        BaseSettings settings = new BaseSettings(
                new ClassIntrospector() {}, new AnnotationIntrospector() {},
                new VisibilityChecker.Std() {}, new PropertyNamingStrategyDefault() {},
                new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator() {}, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings settingsWithHandlerInstantiator = settings.withHandlerInstantiator(handlerInstantiator);
        HandlerInstantiator actual = settingsWithHandlerInstantiator.getHandlerInstantiator();
        assertEquals(handlerInstantiator, actual);
    }

    @Test
    public void testWithLocale() {
        Locale locale = new Locale("fr", "FR");
        BaseSettings settings = new BaseSettings(
                new ClassIntrospector() {}, new AnnotationIntrospector() {},
                new VisibilityChecker.Std() {}, new PropertyNamingStrategyDefault() {},
                new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator() {}, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings settingsWithLocale = settings.with(locale);
        assertEquals(locale, settingsWithLocale.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        BaseSettings settings = new BaseSettings(
                new ClassIntrospector() {}, new AnnotationIntrospector() {},
                new VisibilityChecker.Std() {}, new PropertyNamingStrategyDefault() {},
                new TypeFactory(), new TypeResolverBuilderDefault(), null,
                new HandlerInstantiator() {}, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings settingsWithTimeZone = settings.with(timeZone);
        assertEquals(timeZone, settingsWithTimeZone.getTimeZone());
    }
}