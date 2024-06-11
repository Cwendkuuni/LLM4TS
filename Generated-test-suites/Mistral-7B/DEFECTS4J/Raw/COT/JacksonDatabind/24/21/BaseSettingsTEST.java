package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

public class BaseSettingsTest {

    @Test
    public void testConstructor() {
        BaseSettings baseSettings = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        assertNotNull(baseSettings);
    }

    @Test
    public void testWithClassIntrospector() {
        BaseSettings baseSettings1 = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings baseSettings2 = baseSettings1.withClassIntrospector(new ClassIntrospector());
        assertNotSame(baseSettings1, baseSettings2);
        assertEquals(baseSettings2._classIntrospector, new ClassIntrospector());
    }

    @Test
    public void testGetClassIntrospector() {
        BaseSettings baseSettings = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        ClassIntrospector classIntrospector = baseSettings.getClassIntrospector();
        assertEquals(classIntrospector, baseSettings._classIntrospector);
    }

    @Test
    public void testWithAnnotationIntrospector() {
        BaseSettings baseSettings1 = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings baseSettings2 = baseSettings1.withAnnotationIntrospector(new AnnotationIntrospector());
        assertNotSame(baseSettings1, baseSettings2);
        assertEquals(baseSettings2._annotationIntrospector, new AnnotationIntrospector());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        BaseSettings baseSettings = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        AnnotationIntrospector annotationIntrospector = baseSettings.getAnnotationIntrospector();
        assertEquals(annotationIntrospector, baseSettings._annotationIntrospector);
    }

    @Test
    public void testWithVisibilityChecker() {
        BaseSettings baseSettings1 = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings baseSettings2 = baseSettings1.withVisibilityChecker(new VisibilityChecker<Object>());
        assertNotSame(baseSettings1, baseSettings2);
        assertEquals(baseSettings2._visibilityChecker, new VisibilityChecker<Object>());
    }

    @Test
    public void testGetVisibilityChecker() {
        BaseSettings baseSettings = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        VisibilityChecker<?> visibilityChecker = baseSettings.getVisibilityChecker();
        assertEquals(visibilityChecker, baseSettings._visibilityChecker);
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        BaseSettings baseSettings1 = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings baseSettings2 = baseSettings1.withPropertyNamingStrategy(new PropertyNamingStrategy());
        assertNotSame(baseSettings1, baseSettings2);
        assertEquals(baseSettings2._propertyNamingStrategy, new PropertyNamingStrategy());
    }

    @Test
    public void testGetPropertyNamingStrategy() {
        BaseSettings baseSettings = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        PropertyNamingStrategy propertyNamingStrategy = baseSettings.getPropertyNamingStrategy();
        assertEquals(propertyNamingStrategy, baseSettings._propertyNamingStrategy);
    }

    @Test
    public void testWithTypeFactory() {
        BaseSettings baseSettings1 = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings baseSettings2 = baseSettings1.withTypeFactory(new TypeFactory());
        assertNotSame(baseSettings1, baseSettings2);
        assertEquals(baseSettings2._typeFactory, new TypeFactory());
    }

    @Test
    public void testGetTypeFactory() {
        BaseSettings baseSettings = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        TypeFactory typeFactory = baseSettings.getTypeFactory();
        assertEquals(typeFactory, baseSettings._typeFactory);
    }

    @Test
    public void testWithTypeResolverBuilder() {
        BaseSettings baseSettings1 = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings baseSettings2 = baseSettings1.withTypeResolverBuilder(new TypeResolverBuilder<Object>());
        assertNotSame(baseSettings1, baseSettings2);
        assertEquals(baseSettings2._typeResolverBuilder, new TypeResolverBuilder<Object>());
    }

    @Test
    public void testGetTypeResolverBuilder() {
        BaseSettings baseSettings = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        TypeResolverBuilder<?> typeResolverBuilder = baseSettings.getTypeResolverBuilder();
        assertEquals(typeResolverBuilder, baseSettings._typeResolverBuilder);
    }

    @Test
    public void testWithDateFormat() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BaseSettings baseSettings1 = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), dateFormat,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings baseSettings2 = baseSettings1.withDateFormat(null);
        assertNotSame(baseSettings1, baseSettings2);
        assertNull(baseSettings2._dateFormat);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithDateFormatNull() {
        BaseSettings baseSettings = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        baseSettings.withDateFormat(null);
    }

    @Test
    public void testGetDateFormat() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BaseSettings baseSettings = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), dateFormat,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        DateFormat dateFormat1 = baseSettings.getDateFormat();
        assertEquals(dateFormat1, dateFormat);
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {
            @Override
            public <T> T createHandler(Class<T> handlerType) {
                return null;
            }
        };
        BaseSettings baseSettings1 = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings baseSettings2 = baseSettings1.withHandlerInstantiator(handlerInstantiator);
        assertNotSame(baseSettings1, baseSettings2);
        assertEquals(baseSettings2._handlerInstantiator, handlerInstantiator);
    }

    @Test
    public void testGetHandlerInstantiator() {
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {
            @Override
            public <T> T createHandler(Class<T> handlerType) {
                return null;
            }
        };
        BaseSettings baseSettings = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                handlerInstantiator, Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        HandlerInstantiator handlerInstantiator1 = baseSettings.getHandlerInstantiator();
        assertEquals(handlerInstantiator1, handlerInstantiator);
    }

    @Test
    public void testWithLocale() {
        Locale locale = new Locale("fr", "FR");
        BaseSettings baseSettings1 = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings baseSettings2 = baseSettings1.with(locale);
        assertNotSame(baseSettings1, baseSettings2);
        assertEquals(baseSettings2._locale, locale);
    }

    @Test
    public void testWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("Europe/Paris");
        BaseSettings baseSettings1 = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings baseSettings2 = baseSettings1.with(timeZone);
        assertNotSame(baseSettings1, baseSettings2);
        assertEquals(baseSettings2._timeZone, timeZone);
        assertEquals(baseSettings2._dateFormat.getTimeZone(), timeZone);
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant base64Variant = Base64Variant.BASE64_EXTENDED;
        BaseSettings baseSettings1 = new BaseSettings(
                new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<Object>(), new PropertyNamingStrategy(),
                new TypeFactory(), new TypeResolverBuilder<Object>(), null,
                new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings baseSettings2 = baseSettings1.with(base64Variant);
        assertNotSame(baseSettings1, baseSettings2);
        assertEquals(baseSettings2._defaultBase64, base64Variant);
    }
}