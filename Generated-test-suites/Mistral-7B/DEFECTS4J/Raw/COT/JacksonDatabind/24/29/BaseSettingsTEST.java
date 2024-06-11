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
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {

    @Test
    public void testConstructor() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNotNull(settings);
    }

    @Test
    public void testWithClassIntrospector() {
        BaseSettings settings1 = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings settings2 = settings1.withClassIntrospector(new ClassIntrospector());
        assertNotSame(settings1, settings2);
    }

    @Test
    public void testGetClassIntrospector() {
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), null, null, null, null, null, null, null, null, null, null);
        ClassIntrospector ci = settings.getClassIntrospector();
        assertEquals(ci, settings._classIntrospector);
    }

    @Test
    public void testSetAndGetAnnotationIntrospector() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        AnnotationIntrospector ai = new AnnotationIntrospector();
        settings = settings.withAnnotationIntrospector(ai);
        AnnotationIntrospector annotationIntrospector = settings.getAnnotationIntrospector();
        assertEquals(ai, annotationIntrospector);
    }

    @Test
    public void testSetAndGetVisibilityChecker() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        settings = settings.withVisibilityChecker(vc);
        VisibilityChecker<?> visibilityChecker = settings.getVisibilityChecker();
        assertEquals(vc, visibilityChecker);
    }

    @Test
    public void testSetAndGetPropertyNamingStrategy() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        settings = settings.withPropertyNamingStrategy(pns);
        PropertyNamingStrategy propertyNamingStrategy = settings.getPropertyNamingStrategy();
        assertEquals(pns, propertyNamingStrategy);
    }

    @Test
    public void testSetAndGetTypeFactory() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        TypeFactory tf = new TypeFactory();
        settings = settings.withTypeFactory(tf);
        TypeFactory typeFactory = settings.getTypeFactory();
        assertEquals(tf, typeFactory);
    }

    @Test
    public void testSetAndGetTypeResolverBuilder() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        settings = settings.withTypeResolverBuilder(typer);
        TypeResolverBuilder<?> typeResolverBuilder = settings.getTypeResolverBuilder();
        assertEquals(typer, typeResolverBuilder);
    }

    @Test
    public void testSetAndGetDateFormat() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        DateFormat df = new StdDateFormat();
        settings = settings.withDateFormat(df);
        DateFormat dateFormat = settings.getDateFormat();
        assertEquals(df, dateFormat);
    }

    @Test
    public void testSetAndGetHandlerInstantiator() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        HandlerInstantiator hi = new HandlerInstantiator();
        settings = settings.withHandlerInstantiator(hi);
        HandlerInstantiator handlerInstantiator = settings.getHandlerInstantiator();
        assertEquals(hi, handlerInstantiator);
    }

    @Test
    public void testSetAndGetLocale() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        Locale locale = Locale.ENGLISH;
        settings = settings.with(locale);
        Locale locale1 = settings.getLocale();
        assertEquals(locale, locale1);
    }

    @Test
    public void testSetAndGetTimeZone() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new StdDateFormat();
        df.setTimeZone(tz);
        settings = settings.withDateFormat(df).with(tz);
        TimeZone timeZone = settings.getTimeZone();
        assertEquals(tz, timeZone);
    }

    @Test
    public void testSetAndGetBase64Variant() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        Base64Variant base64 = Base64Variant.DEFAULT;
        settings = settings.with(base64);
        Base64Variant base641 = settings.getBase64Variant();
        assertEquals(base64, base641);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLocale() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        settings.with(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullTimeZone() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        DateFormat df = new StdDateFormat();
        settings = settings.withDateFormat(df).with(null);
    }
}