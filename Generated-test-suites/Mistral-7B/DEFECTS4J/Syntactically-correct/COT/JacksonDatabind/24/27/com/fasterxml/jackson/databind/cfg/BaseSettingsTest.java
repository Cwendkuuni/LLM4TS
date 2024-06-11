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
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.util.HandlerInstantiator;

public class BaseSettingsTest {

    @Test
    public void testBaseSettingsConstruction() {
        // Test default constructor
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.STANDARD);
        assertNotNull(settings);
        
        // Test constructor with arguments
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
        BaseSettings settingsWithArgs = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz,
                defaultBase64);
        assertNotNull(settingsWithArgs);
    }

    @Test
    public void testSetters() {
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

        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz,
                defaultBase64);

        BaseSettings newSettings = baseSettings.withClassIntrospector(ci);
        assertEquals(ci, newSettings.getClassIntrospector());

        newSettings = baseSettings.withAnnotationIntrospector(ai);
        assertEquals(ai, newSettings.getAnnotationIntrospector());

        newSettings = baseSettings.withInsertedAnnotationIntrospector(ai);
        assertEquals(ai, newSettings.getAnnotationIntrospector());

        newSettings = baseSettings.withVisibilityChecker(vc);
        assertEquals(vc, newSettings.getVisibilityChecker());

        newSettings = baseSettings.withVisibility(JsonAutoDetect.Method.FIELDS, JsonAutoDetect.Visibility.NONE);
        assertNotEquals(vc, newSettings.getVisibilityChecker());

        newSettings = baseSettings.withPropertyNamingStrategy(pns);
        assertEquals(pns, newSettings.getPropertyNamingStrategy());

        newSettings = baseSettings.withTypeFactory(tf);
        assertEquals(tf, newSettings.getTypeFactory());

        newSettings = baseSettings.withTypeResolverBuilder(typer);
        assertEquals(typer, newSettings.getTypeResolverBuilder());

        newSettings = baseSettings.withDateFormat(dateFormat);
        assertEquals(dateFormat, newSettings.getDateFormat());

        newSettings = baseSettings.withHandlerInstantiator(hi);
        assertEquals(hi, newSettings.getHandlerInstantiator());

        newSettings = baseSettings.with(locale);
        assertEquals(locale, newSettings.getLocale());

        newSettings = baseSettings.with(tz);
        assertEquals(tz, newSettings.getTimeZone());

        newSettings = baseSettings.with(Base64Variant.UU_ENCODE_VARIANT);
        assertEquals(Base64Variant.UU_ENCODE_VARIANT, newSettings.getBase64Variant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
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

        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz,
                defaultBase64);

        baseSettings.with(null);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
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

        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz,
                defaultBase64);

        BaseSettings clonedSettings = (BaseSettings) baseSettings.clone();
        assertEquals(baseSettings.getClassIntrospector(), clonedSettings.getClassIntrospector());
        assertEquals(baseSettings.getAnnotationIntrospector(), clonedSettings.getAnnotationIntrospector());
        assertEquals(baseSettings.getVisibilityChecker(), clonedSettings.getVisibilityChecker());
        assertEquals(baseSettings.getPropertyNamingStrategy(), clonedSettings.getPropertyNamingStrategy());
        assertEquals(baseSettings.getTypeFactory(), clonedSettings.getTypeFactory());
        assertEquals(baseSettings.getTypeResolverBuilder(), clonedSettings.getTypeResolverBuilder());
        assertEquals(baseSettings.getDateFormat(), clonedSettings.getDateFormat());
        assertEquals(baseSettings.getHandlerInstantiator(), clonedSettings.getHandlerInstantiator());
        assertEquals(baseSettings.getLocale(), clonedSettings.getLocale());
        assertEquals(baseSettings.getTimeZone(), clonedSettings.getTimeZone());
        assertEquals(baseSettings.getBase64Variant(), clonedSettings.getBase64Variant());
    }
}