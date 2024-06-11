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
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ClassIntrospector;
import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.cfg.BaseSettings;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;

public class BaseSettingsTest {

    @Test
    public void testBaseSettings() {
        ClassIntrospector ci = null;
        AnnotationIntrospector ai = null;
        VisibilityChecker<?> vc = null;
        PropertyNamingStrategy pns = null;
        TypeFactory tf = null;
        TypeResolverBuilder<?> typer = null;
        DateFormat dateFormat = null;
        HandlerInstantiator hi = null;
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.getDefault();

        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);
        assertEquals(ci, settings.getClassIntrospector());
        assertEquals(ai, settings.getAnnotationIntrospector());
        assertEquals(vc, settings.getVisibilityChecker());
        assertEquals(pns, settings.getPropertyNamingStrategy());
        assertEquals(tf, settings.getTypeFactory());
        assertEquals(typer, settings.getTypeResolverBuilder());
        assertEquals(dateFormat, settings.getDateFormat());
        assertEquals(hi, settings.getHandlerInstantiator());
        assertEquals(locale, settings.getLocale());
        assertEquals(timeZone, settings.getTimeZone());
        assertEquals(defaultBase64, settings.getBase64Variant());

        ci = new ClassIntrospector();
        ai = new AnnotationIntrospector();
        vc = new VisibilityChecker<>();
        pns = new PropertyNamingStrategy();
        tf = new TypeFactory();
        typer = new TypeResolverBuilder<>();
        dateFormat = new StdDateFormat();
        hi = new HandlerInstantiator();
        locale = Locale.US;
        timeZone = TimeZone.getTimeZone("UTC");
        defaultBase64 = Base64Variant.STANDARD;

        settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);
        assertNotSame(ci, settings.getClassIntrospector());
        assertNotSame(ai, settings.getAnnotationIntrospector());
        assertNotSame(vc, settings.getVisibilityChecker());
        assertNotSame(pns, settings.getPropertyNamingStrategy());
        assertNotSame(tf, settings.getTypeFactory());
        assertNotSame(typer, settings.getTypeResolverBuilder());
        assertNotSame(dateFormat, settings.getDateFormat());
        assertNotSame(hi, settings.getHandlerInstantiator());
        assertNotSame(locale, settings.getLocale());
        assertNotSame(timeZone, settings.getTimeZone());
        assertNotSame(defaultBase64, settings.getBase64Variant());

        settings = settings.withClassIntrospector(ci);
        assertEquals(ci, settings.getClassIntrospector());

        settings = settings.withAnnotationIntrospector(ai);
        assertEquals(ai, settings.getAnnotationIntrospector());

        settings = settings.withInsertedAnnotationIntrospector(ai);
        assertEquals(AnnotationIntrospectorPair.create(ai, settings.getAnnotationIntrospector()), settings.getAnnotationIntrospector());

        settings = settings.withAppendedAnnotationIntrospector(ai);
        assertEquals(AnnotationIntrospectorPair.create(settings.getAnnotationIntrospector(), ai), settings.getAnnotationIntrospector());

        settings = settings.withVisibilityChecker(vc);
        assertEquals(vc, settings.getVisibilityChecker());

        settings = settings.withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);
        assertNotSame(vc, settings.getVisibilityChecker());

        settings = settings.withPropertyNamingStrategy(pns);
        assertEquals(pns, settings.getPropertyNamingStrategy());

        settings = settings.withTypeFactory(tf);
        assertEquals(tf, settings.getTypeFactory());

        settings = settings.withTypeResolverBuilder(typer);
        assertEquals(typer, settings.getTypeResolverBuilder());

        settings = settings.withDateFormat(dateFormat);
        assertEquals(dateFormat, settings.getDateFormat());

        settings = settings.withHandlerInstantiator(hi);
        assertEquals(hi, settings.getHandlerInstantiator());

        settings = settings.with(locale);
        assertEquals(locale, settings.getLocale());

        settings = settings.with(timeZone);
        assertNotSame(timeZone, settings.getTimeZone());
        assertEquals(dateFormat.getTimeZone(), settings.getTimeZone());

        settings = settings.with(defaultBase64);
        assertEquals(defaultBase64, settings.getBase64Variant());
    }
}