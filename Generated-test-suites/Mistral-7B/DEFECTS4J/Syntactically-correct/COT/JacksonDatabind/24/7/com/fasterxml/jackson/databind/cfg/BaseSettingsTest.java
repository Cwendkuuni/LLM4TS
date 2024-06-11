package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.cfg.BaseSettings;
import com.fasterxml.jackson.databind.cfg.BaseSettings.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.ClassIntrospector;
import com.fasterxml.jackson.databind.cfg.AnnotationIntrospector;
import com.fasterxml.jackson.databind.cfg.VisibilityChecker;
import com.fasterxml.jackson.databind.cfg.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.TypeFactory;
import com.fasterxml.jackson.databind.cfg.TypeResolverBuilder;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class BaseSettingsTest {

    @org.junit.Test
    public void testConstructor() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new StdNameTransformer(null, new AnnotatedClassWrapper(null), null, null);
        TypeFactory tf = new StandardSerializerFactory(null);
        TypeResolverBuilder<?> typer = new TypeResolverBuilderDefault();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = new MapperFactory();
        Locale locale = Locale.getDefault();
        TimeZone tz = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variants.STANDARD;
        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
        assertNotNull(settings);
    }

    @org.junit.Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withClassIntrospector(ci);
        assertEquals(ci, newSettings._classIntrospector);
    }

    @org.junit.Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withAnnotationIntrospector(ai);
        assertEquals(ai, newSettings._annotationIntrospector);
    }

    @org.junit.Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withInsertedAnnotationIntrospector(ai);
        assertEquals(ai, newSettings._annotationIntrospector);
        assertEquals(settings._annotationIntrospector, newSettings._annotationIntrospector);
    }

    @org.junit.Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withAppendedAnnotationIntrospector(ai);
        assertEquals(ai, newSettings._annotationIntrospector);
        assertEquals(settings._annotationIntrospector, newSettings._annotationIntrospector);
    }

    @org.junit.Test
    public void testWithVisibilityChecker() {
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withVisibilityChecker(vc);
        assertEquals(vc, newSettings._visibilityChecker);
    }

    @org.junit.Test
    public void testWithVisibility() {
        PropertyAccessor forMethod = new SimplePropertyAccessor("fieldName");
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.NON_FINAL;
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withVisibility(forMethod, visibility);
        assertEquals(visibility, newSettings._visibilityChecker.getMemberVisibility(forMethod));
    }

    @org.junit.Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy pns = new StdNameTransformer(null, new AnnotatedClassWrapper(null), null, null);
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withPropertyNamingStrategy(pns);
        assertEquals(pns, newSettings._propertyNamingStrategy);
    }

    @org.junit.Test
    public void testWithTypeFactory() {
        TypeFactory tf = new StandardSerializerFactory(null);
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withTypeFactory(tf);
        assertEquals(tf, newSettings._typeFactory);
    }

    @org.junit.Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typer = new TypeResolverBuilderDefault();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withTypeResolverBuilder(typer);
        assertEquals(typer, newSettings._typeResolverBuilder);
    }

    @org.junit.Test
    public void testWithDateFormat() {
        DateFormat dateFormat = new StdDateFormat();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withDateFormat(dateFormat);
        assertEquals(dateFormat, newSettings._dateFormat);
    }

    @org.junit.Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator hi = new MapperFactory();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.withHandlerInstantiator(hi);
        assertEquals(hi, newSettings._handlerInstantiator);
    }

    @org.junit.Test
    public void testWith() {
        Locale locale = Locale.getDefault();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.with(locale);
        assertEquals(locale, newSettings._locale);
    }

    @org.junit.Test
    public void testWithTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.with(tz);
        assertEquals(tz, newSettings._timeZone);
        assertEquals(tz, newSettings._dateFormat.getTimeZone());
    }

    @org.junit.Test
    public void testWithBase64Variant() {
        Base64Variant base64 = Base64Variants.STANDARD;
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newSettings = settings.with(base64);
        assertEquals(base64, newSettings._defaultBase64);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testWithNullLocale() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        settings.with(null);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testWithNullTimeZone() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        settings.with(null);
    }
}