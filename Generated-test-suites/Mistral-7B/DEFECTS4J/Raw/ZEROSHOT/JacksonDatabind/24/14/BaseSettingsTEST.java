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

/**
 * Test Class for BaseSettings.java
 *
 * @author John Doe
 */
public class BaseSettingsTest {

    @Test
    public void testBaseSettingsConstruction() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker.Default();
        PropertyNamingStrategy pns = StdPropertyNamingStrategy.INSTANCE;
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new MapTypeResolverBuilder();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HandlerInstantiator hi = new AnnotatedClassHandlerInstantiator();
        Locale locale = Locale.US;
        TimeZone tz = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variants.STANDARD;
        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
        assertNotNull(settings);
    }

    @Test
    public void testBaseSettingsClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings settings = new BaseSettings(ci, new AnnotationIntrospector(), new VisibilityChecker.Default(), StdPropertyNamingStrategy.INSTANCE,
                new TypeFactory(), new MapTypeResolverBuilder(), null, new AnnotatedClassHandlerInstantiator(), Locale.US,
                TimeZone.getDefault(), Base64Variants.STANDARD);
        assertSame(ci, settings.getClassIntrospector());
    }

    @Test
    public void testBaseSettingsAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), ai, new VisibilityChecker.Default(), StdPropertyNamingStrategy.INSTANCE,
                new TypeFactory(), new MapTypeResolverBuilder(), null, new AnnotatedClassHandlerInstantiator(), Locale.US,
                TimeZone.getDefault(), Base64Variants.STANDARD);
        assertSame(ai, settings.getAnnotationIntrospector());
    }

    @Test
    public void testBaseSettingsVisibilityChecker() {
        VisibilityChecker<?> vc = new VisibilityChecker.Default();
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(), vc, StdPropertyNamingStrategy.INSTANCE,
                new TypeFactory(), new MapTypeResolverBuilder(), null, new AnnotatedClassHandlerInstantiator(), Locale.US,
                TimeZone.getDefault(), Base64Variants.STANDARD);
        assertSame(vc, settings.getVisibilityChecker());
    }

    @Test
    public void testBaseSettingsPropertyNamingStrategy() {
        PropertyNamingStrategy pns = StdPropertyNamingStrategy.INSTANCE;
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker.Default(), pns, new TypeFactory(), new MapTypeResolverBuilder(), null,
                new AnnotatedClassHandlerInstantiator(), Locale.US, TimeZone.getDefault(), Base64Variants.STANDARD);
        assertSame(pns, settings.getPropertyNamingStrategy());
    }

    @Test
    public void testBaseSettingsTypeFactory() {
        TypeFactory tf = new TypeFactory();
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker.Default(), StdPropertyNamingStrategy.INSTANCE, tf, new MapTypeResolverBuilder(), null,
                new AnnotatedClassHandlerInstantiator(), Locale.US, TimeZone.getDefault(), Base64Variants.STANDARD);
        assertSame(tf, settings.getTypeFactory());
    }

    @Test
    public void testBaseSettingsTypeResolverBuilder() {
        TypeResolverBuilder<?> typer = new MapTypeResolverBuilder();
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker.Default(), StdPropertyNamingStrategy.INSTANCE, new TypeFactory(), typer, null,
                new AnnotatedClassHandlerInstantiator(), Locale.US, TimeZone.getDefault(), Base64Variants.STANDARD);
        assertSame(typer, settings.getTypeResolverBuilder());
    }

    @Test
    public void testBaseSettingsDateFormat() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker.Default(), StdPropertyNamingStrategy.INSTANCE, new TypeFactory(),
                new MapTypeResolverBuilder(), dateFormat, new AnnotatedClassHandlerInstantiator(), Locale.US,
                TimeZone.getDefault(), Base64Variants.STANDARD);
        assertEquals(dateFormat, settings.getDateFormat());
    }

    @Test
    public void testBaseSettingsHandlerInstantiator() {
        HandlerInstantiator hi = new AnnotatedClassHandlerInstantiator();
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker.Default(), StdPropertyNamingStrategy.INSTANCE, new TypeFactory(),
                new MapTypeResolverBuilder(), null, hi, Locale.US, TimeZone.getDefault(), Base64Variants.STANDARD);
        assertSame(hi, settings.getHandlerInstantiator());
    }

    @Test
    public void testBaseSettingsLocale() {
        Locale locale = Locale.US;
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker.Default(), StdPropertyNamingStrategy.INSTANCE, new TypeFactory(),
                new MapTypeResolverBuilder(), null, new AnnotatedClassHandlerInstantiator(), locale,
                TimeZone.getDefault(), Base64Variants.STANDARD);
        assertSame(locale, settings.getLocale());
    }

    @Test
    public void testBaseSettingsTimeZone() {
        TimeZone tz = TimeZone.getDefault();
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker.Default(), StdPropertyNamingStrategy.INSTANCE, new TypeFactory(),
                new MapTypeResolverBuilder(), null, new AnnotatedClassHandlerInstantiator(), Locale.US, tz,
                Base64Variants.STANDARD);
        assertSame(tz, settings.getTimeZone());
    }

    @Test
    public void testBaseSettingsBase64Variant() {
        Base64Variant defaultBase64 = Base64Variants.STANDARD;
        BaseSettings settings = new BaseSettings(new ClassIntrospector(), new AnnotationIntrospector(),
                new VisibilityChecker.Default(), StdPropertyNamingStrategy.INSTANCE, new TypeFactory(),
                new MapTypeResolverBuilder(), null, new AnnotatedClassHandlerInstantiator(), Locale.US,
                TimeZone.getDefault(), defaultBase64);
        assertSame(defaultBase64, settings.getBase64Variant());
    }
}