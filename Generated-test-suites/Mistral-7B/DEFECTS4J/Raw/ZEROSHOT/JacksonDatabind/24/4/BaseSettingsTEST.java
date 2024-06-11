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

/**
 * Test class for BaseSettings class
 */
public class BaseSettingsTest {

    @Test
    public void testBaseSettings() {
        // Test constructor
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = null;
        PropertyNamingStrategy pns = null;
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = null;
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = null;

        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);

        // Test getters
        ClassIntrospector actualClassIntrospector = settings.getClassIntrospector();
        assertEquals(ci, actualClassIntrospector);

        AnnotationIntrospector actualAnnotationIntrospector = settings.getAnnotationIntrospector();
        assertEquals(ai, actualAnnotationIntrospector);

        VisibilityChecker<?> actualVisibilityChecker = settings.getVisibilityChecker();
        assertEquals(vc, actualVisibilityChecker);

        PropertyNamingStrategy actualPropertyNamingStrategy = settings.getPropertyNamingStrategy();
        assertEquals(pns, actualPropertyNamingStrategy);

        TypeFactory actualTypeFactory = settings.getTypeFactory();
        assertEquals(tf, actualTypeFactory);

        TypeResolverBuilder<?> actualTypeResolverBuilder = settings.getTypeResolverBuilder();
        assertEquals(typer, actualTypeResolverBuilder);

        DateFormat actualDateFormat = settings.getDateFormat();
        assertEquals(dateFormat, actualDateFormat);

        HandlerInstantiator actualHandlerInstantiator = settings.getHandlerInstantiator();
        assertEquals(hi, actualHandlerInstantiator);

        Locale actualLocale = settings.getLocale();
        assertEquals(locale, actualLocale);

        TimeZone actualTimeZone = settings.getTimeZone();
        assertEquals(timeZone, actualTimeZone);

        Base64Variant actualBase64Variant = settings.getBase64Variant();
        assertEquals(defaultBase64, actualBase64Variant);

        // Test factory methods
        BaseSettings settingsWithClassIntrospector = settings.withClassIntrospector(new ClassIntrospector());
        assertNotSame(settings, settingsWithClassIntrospector);
        assertEquals(new ClassIntrospector(), settingsWithClassIntrospector.getClassIntrospector());

        BaseSettings settingsWithAnnotationIntrospector = settings.withAnnotationIntrospector(new AnnotationIntrospector());
        assertNotSame(settings, settingsWithAnnotationIntrospector);
        assertEquals(new AnnotationIntrospector(), settingsWithAnnotationIntrospector.getAnnotationIntrospector());

        BaseSettings settingsWithTypeFactory = settings.withTypeFactory(new TypeFactory());
        assertNotSame(settings, settingsWithTypeFactory);
        assertEquals(new TypeFactory(), settingsWithTypeFactory.getTypeFactory());

        BaseSettings settingsWithTypeResolverBuilder = settings.withTypeResolverBuilder(new TypeResolverBuilder<Object>() {});
        assertNotSame(settings, settingsWithTypeResolverBuilder);
        assertEquals(new TypeResolverBuilder<Object>() {}, settingsWithTypeResolverBuilder.getTypeResolverBuilder());

        BaseSettings settingsWithDateFormat = settings.withDateFormat(new StdDateFormat());
        assertNotSame(settings, settingsWithDateFormat);
        assertNotSame(dateFormat, settingsWithDateFormat.getDateFormat());
        assertEquals(new StdDateFormat(), settingsWithDateFormat.getDateFormat());

        Base64Variant defaultBase64 = Base64Variant.STANDARD;
        BaseSettings settingsWithDefaultBase64 = settings.with(defaultBase64);
        assertNotSame(settings, settingsWithDefaultBase64);
        assertEquals(defaultBase64, settingsWithDefaultBase64.getBase64Variant());
    }
}