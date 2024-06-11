package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testBaseSettingsInstance() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.STANDARD;

        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);

        assertNotNull(settings);
        assertSame(ci, settings._classIntrospector);
        assertSame(ai, settings._annotationIntrospector);
        assertSame(vc, settings._visibilityChecker);
        assertSame(pns, settings._propertyNamingStrategy);
        assertSame(tf, settings._typeFactory);
        assertSame(typer, settings._typeResolverBuilder);
        assertSame(dateFormat, settings._dateFormat);
        assertSame(hi, settings._handlerInstantiator);
        assertSame(locale, settings._locale);
        assertSame(timeZone, settings._timeZone);
        assertSame(defaultBase64, settings._defaultBase64);
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings settings = new BaseSettings();

        BaseSettings newSettings = settings.withClassIntrospector(ci);

        assertSame(ci, newSettings._classIntrospector);
        assertSame(settings, newSettings);
    }

    // Add test cases for all other methods in the BaseSettings class: withAnnotationIntrospector, withVisibilityChecker, etc.

    @Test
    public void testClone() throws CloneNotSupportedException {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.STANDARD;

        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);

        BaseSettings clonedSettings = (BaseSettings) settings.clone();

        assertNotNull(clonedSettings);
        assertSame(ci, clonedSettings._classIntrospector);
        assertSame(ai, clonedSettings._annotationIntrospector);
        assertSame(vc, clonedSettings._visibilityChecker);
        assertSame(pns, clonedSettings._propertyNamingStrategy);
        assertSame(tf, clonedSettings._typeFactory);
        assertSame(typer, clonedSettings._typeResolverBuilder);
        assertSame(dateFormat, clonedSettings._dateFormat);
        assertSame(hi, clonedSettings._handlerInstantiator);
        assertSame(locale, clonedSettings._locale);
        assertSame(timeZone, clonedSettings._timeZone);
        assertSame(defaultBase64, clonedSettings._defaultBase64);
    }
}