package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testGetClassIntrospector() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<>(), new PropertyNamingStrategy(), new TypeFactory(),
                new TypeResolverBuilder<>(), new DateFormat(), new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        ClassIntrospector ci = settings.getClassIntrospector();
        assertNotNull(ci);
        assertEquals(ci, settings.getClassIntrospector());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<>(), new PropertyNamingStrategy(), new TypeFactory(),
                new TypeResolverBuilder<>(), new DateFormat(), new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        AnnotationIntrospector ai = settings.getAnnotationIntrospector();
        assertNotNull(ai);
        assertEquals(ai, settings.getAnnotationIntrospector());
    }

    @Test
    public void testGetVisibilityChecker() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<>(), new PropertyNamingStrategy(), new TypeFactory(),
                new TypeResolverBuilder<>(), new DateFormat(), new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        VisibilityChecker<?> vc = settings.getVisibilityChecker();
        assertNotNull(vc);
        assertEquals(vc, settings.getVisibilityChecker());
    }

    @Test
    public void testGetPropertyNamingStrategy() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<>(), new PropertyNamingStrategy(), new TypeFactory(),
                new TypeResolverBuilder<>(), new DateFormat(), new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        PropertyNamingStrategy pns = settings.getPropertyNamingStrategy();
        assertNotNull(pns);
        assertEquals(pns, settings.getPropertyNamingStrategy());
    }

    @Test
    public void testGetTypeFactory() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<>(), new PropertyNamingStrategy(), new TypeFactory(),
                new TypeResolverBuilder<>(), new DateFormat(), new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        TypeFactory tf = settings.getTypeFactory();
        assertNotNull(tf);
        assertEquals(tf, settings.getTypeFactory());
    }

    @Test
    public void testGetTypeResolverBuilder() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<>(), new PropertyNamingStrategy(), new TypeFactory(),
                new TypeResolverBuilder<>(), new DateFormat(), new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        TypeResolverBuilder<?> typer = settings.getTypeResolverBuilder();
        assertNotNull(typer);
        assertEquals(typer, settings.getTypeResolverBuilder());
    }

    @Test
    public void testGetDateFormat() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<>(), new PropertyNamingStrategy(), new TypeFactory(),
                new TypeResolverBuilder<>(), new DateFormat(), new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        DateFormat df = settings.getDateFormat();
        assertNotNull(df);
        assertEquals(df, settings.getDateFormat());
    }

    @Test
    public void testGetHandlerInstantiator() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<>(), new PropertyNamingStrategy(), new TypeFactory(),
                new TypeResolverBuilder<>(), new DateFormat(), new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        HandlerInstantiator hi = settings.getHandlerInstantiator();
        assertNotNull(hi);
        assertEquals(hi, settings.getHandlerInstantiator());
    }

    @Test
    public void testGetLocale() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<>(), new PropertyNamingStrategy(), new TypeFactory(),
                new TypeResolverBuilder<>(), new DateFormat(), new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        Locale locale = settings.getLocale();
        assertNotNull(locale);
        assertEquals(locale, settings.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<>(), new PropertyNamingStrategy(), new TypeFactory(),
                new TypeResolverBuilder<>(), new DateFormat(), new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        TimeZone tz = settings.getTimeZone();
        assertNotNull(tz);
        assertEquals(tz, settings.getTimeZone());
    }

    @Test
    public void testGetBase64Variant() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<>(), new PropertyNamingStrategy(), new TypeFactory(),
                new TypeResolverBuilder<>(), new DateFormat(), new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        Base64Variant base64 = settings.getBase64Variant();
        assertNotNull(base64);
        assertEquals(base64, settings.getBase64Variant());
    }

    @Test
    public void testWithClassIntrospector() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<>(), new PropertyNamingStrategy(), new TypeFactory(),
                new TypeResolverBuilder<>(), new DateFormat(), new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings newSettings = settings.withClassIntrospector(new ClassIntrospector());
        assertNotSame(settings, newSettings);
        assertEquals(newClassIntrospector, newSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<>(), new PropertyNamingStrategy(), new TypeFactory(),
                new TypeResolverBuilder<>(), new DateFormat(), new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings newSettings = settings.withAnnotationIntrospector(new AnnotationIntrospector());
        assertNotSame(settings, newSettings);
        assertEquals(newAnnotationIntrospector, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithDateFormat() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<>(), new PropertyNamingStrategy(), new TypeFactory(),
                new TypeResolverBuilder<>(), new DateFormat(), new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        DateFormat df = new DateFormat() {
            @Override
            public String format(Date date) {
                return "test";
            }
        };
        BaseSettings newSettings = settings.withDateFormat(df);
        assertNotSame(settings, newSettings);
        assertEquals(df, newSettings.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<>(), new PropertyNamingStrategy(), new TypeFactory(),
                new TypeResolverBuilder<>(), new DateFormat(), new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        HandlerInstantiator hi = new HandlerInstantiator() {};
        BaseSettings newSettings = settings.withHandlerInstantiator(hi);
        assertNotSame(settings, newSettings);
        assertEquals(hi, newSettings.getHandlerInstantiator());
    }

    @Test
    public void testWithLocale() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<>(), new PropertyNamingStrategy(), new TypeFactory(),
                new TypeResolverBuilder<>(), new DateFormat(), new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        Locale locale = new Locale("test", "test");
        BaseSettings newSettings = settings.with(locale);
        assertNotSame(settings, newSettings);
        assertEquals(locale, newSettings.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<>(), new PropertyNamingStrategy(), new TypeFactory(),
                new TypeResolverBuilder<>(), new DateFormat(), new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        TimeZone tz = new TimeZone("test") {};
        BaseSettings newSettings = settings.with(tz);
        assertNotSame(settings, newSettings);
        assertEquals(tz, newSettings.getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker<>(), new PropertyNamingStrategy(), new TypeFactory(),
                new TypeResolverBuilder<>(), new DateFormat(), new HandlerInstantiator(), Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        Base64Variant base64 = new Base64Variant() {};
        BaseSettings newSettings = settings.with(base64);
        assertNotSame(settings, newSettings);
        assertEquals(base64, newSettings.getBase64Variant());
    }
}