package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testNewInstance() {
        ClassIntrospector ci = null;
        AnnotationIntrospector ai = null;
        VisibilityChecker<?> vc = null;
        PropertyNamingStrategy pns = null;
        TypeFactory tf = null;
        TypeResolverBuilder<?> typer = null;
        DateFormat dateFormat = null;
        HandlerInstantiator hi = null;
        Locale locale = null;
        TimeZone tz = null;
        Base64Variant defaultBase64 = null;

        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
        assertNotNull(settings);

        // test getters
        assertSame(ci, settings.getClassIntrospector());
        assertSame(ai, settings.getAnnotationIntrospector());
        assertSame(vc, settings.getVisibilityChecker());
        assertSame(pns, settings.getPropertyNamingStrategy());
        assertSame(tf, settings.getTypeFactory());
        assertSame(typer, settings.getTypeResolverBuilder());
        assertSame(dateFormat, settings.getDateFormat());
        assertSame(hi, settings.getHandlerInstantiator());
        assertSame(locale, settings.getLocale());
        assertSame(tz, settings.getTimeZone());
        assertSame(defaultBase64, settings.getBase64Variant());
    }

    @Test
    public void testNewInstanceWithArguments() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new PropertyNamingStrategy() {};
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new SimpleTypeResolverBuilder<>();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = new HandlerInstantantiator();
        Locale locale = Locale.US;
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;

        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
        assertNotNull(settings);

        // test getters
        assertSame(ci, settings.getClassIntrospector());
        assertSame(ai, settings.getAnnotationIntrospector());
        assertSame(vc, settings.getVisibilityChecker());
        assertSame(pns, settings.getPropertyNamingStrategy());
        assertSame(tf, settings.getTypeFactory());
        assertSame(typer, settings.getTypeResolverBuilder());
        assertSame(dateFormat, settings.getDateFormat());
        assertSame(hi, settings.getHandlerInstantiator());
        assertSame(locale, settings.getLocale());
        assertSame(tz, settings.getTimeZone());
        assertSame(defaultBase64, settings.getBase64Variant());
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector oldCi = new ClassIntrospector();
        ClassIntrospector newCi = new ClassIntrospector();

        BaseSettings settings = new BaseSettings(oldCi, new AnnotationIntrospector(), new VisibilityChecker<>(),
                new PropertyNamingStrategy(), new TypeFactory(), new TypeResolverBuilder<>(), new DateFormat(),
                new HandlerInstantiator(), Locale.US, TimeZone.getDefault(), Base64Variant.DEFAULT);

        BaseSettings newSettings = settings.withClassIntrospector(newCi);
        assertNotNull(newSettings);
        assertSame(newCi, newSettings.getClassIntrospector());
        assertSame(settings, newSettings);
    }

    // Test other factory methods similarly

    @Test
    public void testClone() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new PropertyNamingStrategy() {};
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new SimpleTypeResolverBuilder<>();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = new HandlerInstantantiator();
        Locale locale = Locale.US;
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;

        BaseSettings original = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);

        BaseSettings cloned = original.clone();
        assertNotNull(cloned);
        assertNotSame(original, cloned);
        assertSame(original.getClassIntrospector(), cloned.getClassIntrospector());
        // other fields should be equal, but not identical instances
    }
}