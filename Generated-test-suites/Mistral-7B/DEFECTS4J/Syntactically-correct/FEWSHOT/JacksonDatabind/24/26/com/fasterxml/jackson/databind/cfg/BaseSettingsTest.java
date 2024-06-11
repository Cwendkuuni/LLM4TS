package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

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
import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testBaseSettings() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<Object> vc = new VisibilityChecker.Std();
        PropertyNamingStrategy pns = PropertyNamingStrategy.DEFAULT;
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilderDefault();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variants.DEFAULT;

        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);

        assertEquals(ci, baseSettings.getClassIntrospector());
        assertEquals(ai, baseSettings.getAnnotationIntrospector());
        assertEquals(vc, baseSettings.getVisibilityChecker());
        assertEquals(pns, baseSettings.getPropertyNamingStrategy());
        assertEquals(tf, baseSettings.getTypeFactory());
        assertEquals(typer, baseSettings.getTypeResolverBuilder());
        assertEquals(dateFormat, baseSettings.getDateFormat());
        assertEquals(hi, baseSettings.getHandlerInstantiator());
        assertEquals(locale, baseSettings.getLocale());
        assertEquals(timeZone, baseSettings.getTimeZone());
        assertEquals(defaultBase64, baseSettings.getBase64Variant());

        BaseSettings settingsWithClassIntrospector = baseSettings.withClassIntrospector(new ClassIntrospector());
        assertNotSame(ci, settingsWithClassIntrospector.getClassIntrospector());
        assertEquals(ai, settingsWithClassIntrospector.getAnnotationIntrospector());
        assertEquals(vc, settingsWithClassIntrospector.getVisibilityChecker());
        assertEquals(pns, settingsWithClassIntrospector.getPropertyNamingStrategy());
        assertEquals(tf, settingsWithClassIntrospector.getTypeFactory());
        assertEquals(typer, settingsWithClassIntrospector.getTypeResolverBuilder());
        assertEquals(dateFormat, settingsWithClassIntrospector.getDateFormat());
        assertEquals(hi, settingsWithClassIntrospector.getHandlerInstantiator());
        assertEquals(locale, settingsWithClassIntrospector.getLocale());
        assertEquals(timeZone, settingsWithClassIntrospector.getTimeZone());
        assertEquals(defaultBase64, settingsWithClassIntrospector.getBase64Variant());
    }

    @Test
    public void testBaseSettingsWithAnnotationIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<Object> vc = new VisibilityChecker.Std();
        PropertyNamingStrategy pns = PropertyNamingStrategy.DEFAULT;
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilderDefault();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variants.DEFAULT;

        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);

        BaseSettings settingsWithAnnotationIntrospector = baseSettings.withAnnotationIntrospector(new AnnotationIntrospector());
        assertNotSame(ai, settingsWithAnnotationIntrospector.getAnnotationIntrospector());
        assertEquals(ci, settingsWithAnnotationIntrospector.getClassIntrospector());
        assertEquals(vc, settingsWithAnnotationIntrospector.getVisibilityChecker());
        assertEquals(pns, settingsWithAnnotationIntrospector.getPropertyNamingStrategy());
        assertEquals(tf, settingsWithAnnotationIntrospector.getTypeFactory());
        assertEquals(typer, settingsWithAnnotationIntrospector.getTypeResolverBuilder());
        assertEquals(dateFormat, settingsWithAnnotationIntrospector.getDateFormat());
        assertEquals(hi, settingsWithAnnotationIntrospector.getHandlerInstantiator());
        assertEquals(locale, settingsWithAnnotationIntrospector.getLocale());
        assertEquals(timeZone, settingsWithAnnotationIntrospector.getTimeZone());
        assertEquals(defaultBase64, settingsWithAnnotationIntrospector.getBase64Variant());
    }

    @Test
    public void testBaseSettingsWithDateFormat() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<Object> vc = new VisibilityChecker.Std();
        PropertyNamingStrategy pns = PropertyNamingStrategy.DEFAULT;
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilderDefault();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variants.DEFAULT;

        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);

        BaseSettings settingsWithDateFormat = baseSettings.withDateFormat(new StdDateFormat());
        assertNotSame(dateFormat, settingsWithDateFormat.getDateFormat());
        assertEquals(ci, settingsWithDateFormat.getClassIntrospector());
        assertEquals(ai, settingsWithAnnotationIntrospector.getAnnotationIntrospector());
        assertEquals(vc, settingsWithDateFormat.getVisibilityChecker());
        assertEquals(pns, settingsWithDateFormat.getPropertyNamingStrategy());
        assertEquals(tf, settingsWithDateFormat.getTypeFactory());
        assertEquals(typer, settingsWithDateFormat.getTypeResolverBuilder());
        assertNotSame(dateFormat, settingsWithDateFormat.getDateFormat());
        assertEquals(hi, settingsWithDateFormat.getHandlerInstantiator());
        assertEquals(locale, settingsWithDateFormat.getLocale());
        assertEquals(timeZone, settingsWithDateFormat.getTimeZone());
        assertEquals(defaultBase64, settingsWithDateFormat.getBase64Variant());
    }

    @Test
    public void testBaseSettingsWithHandlerInstantiator() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<Object> vc = new VisibilityChecker.Std();
        PropertyNamingStrategy pns = PropertyNamingStrategy.DEFAULT;
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilderDefault();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variants.DEFAULT;

        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);

        BaseSettings settingsWithHandlerInstantiator = baseSettings.withHandlerInstantiator(new HandlerInstantiator());
        assertNotSame(hi, settingsWithHandlerInstantiator.getHandlerInstantiator());
        assertEquals(ci, settingsWithHandlerInstantiator.getClassIntrospector());
        assertEquals(ai, settingsWithHandlerInstantiator.getAnnotationIntrospector());
        assertEquals(vc, settingsWithHandlerInstantiator.getVisibilityChecker());
        assertEquals(pns, settingsWithHandlerInstantiator.getPropertyNamingStrategy());
        assertEquals(tf, settingsWithHandlerInstantiator.getTypeFactory());
        assertEquals(typer, settingsWithHandlerInstantiator.getTypeResolverBuilder());
        assertEquals(dateFormat, settingsWithHandlerInstantiator.getDateFormat());
        assertNotSame(hi, settingsWithHandlerInstantiator.getHandlerInstantiator());
        assertEquals(locale, settingsWithHandlerInstantiator.getLocale());
        assertEquals(timeZone, settingsWithHandlerInstantiator.getTimeZone());
        assertEquals(defaultBase64, settingsWithHandlerInstantiator.getBase64Variant());
    }

    @Test
    public void testBaseSettingsWithLocale() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<Object> vc = new VisibilityChecker.Std();
        PropertyNamingStrategy pns = PropertyNamingStrategy.DEFAULT;
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilderDefault();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = new Locale("en", "US");
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variants.DEFAULT;

        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);

        BaseSettings settingsWithLocale = baseSettings.with(new Locale("fr", "FR"));
        assertNotSame(locale, settingsWithLocale.getLocale());
        assertEquals(ci, settingsWithLocale.getClassIntrospector());
        assertEquals(ai, settingsWithLocale.getAnnotationIntrospector());
        assertEquals(vc, settingsWithLocale.getVisibilityChecker());
        assertEquals(pns, settingsWithLocale.getPropertyNamingStrategy());
        assertEquals(tf, settingsWithLocale.getTypeFactory());
        assertEquals(typer, settingsWithLocale.getTypeResolverBuilder());
        assertEquals(dateFormat, settingsWithLocale.getDateFormat());
        assertEquals(hi, settingsWithLocale.getHandlerInstantiator());
        assertNotSame(locale, settingsWithLocale.getLocale());
        assertEquals(timeZone, settingsWithLocale.getTimeZone());
        assertEquals(defaultBase64, settingsWithLocale.getBase64Variant());
    }

    @Test
    public void testBaseSettingsWithTimeZone() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<Object> vc = new VisibilityChecker.Std();
        PropertyNamingStrategy pns = PropertyNamingStrategy.DEFAULT;
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilderDefault();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = new TimeZone("Europe/Paris");
        Base64Variant defaultBase64 = Base64Variants.DEFAULT;

        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);

        BaseSettings settingsWithTimeZone = baseSettings.with(new TimeZone("America/Los_Angeles"));
        assertNotSame(timeZone, settingsWithTimeZone.getTimeZone());
        assertEquals(ci, settingsWithTimeZone.getClassIntrospector());
        assertEquals(ai, settingsWithTimeZone.getAnnotationIntrospector());
        assertEquals(vc, settingsWithTimeZone.getVisibilityChecker());
        assertEquals(pns, settingsWithTimeZone.getPropertyNamingStrategy());
        assertEquals(tf, settingsWithTimeZone.getTypeFactory());
        assertEquals(typer, settingsWithTimeZone.getTypeResolverBuilder());
        assertEquals(dateFormat, settingsWithTimeZone.getDateFormat());
        assertEquals(hi, settingsWithTimeZone.getHandlerInstantiator());
        assertEquals(locale, settingsWithTimeZone.getLocale());
        assertNotSame(timeZone, settingsWithTimeZone.getTimeZone());
        assertEquals(defaultBase64, settingsWithTimeZone.getBase64Variant());
    }

    @Test
    public void testBaseSettingsWithBase64Variant() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<Object> vc = new VisibilityChecker.Std();
        PropertyNamingStrategy pns = PropertyNamingStrategy.DEFAULT;
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilderDefault();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.BASE64_STANDARD;

        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);

        BaseSettings settingsWithBase64Variant = baseSettings.with(Base64Variant.BASE64_URL_SAFE);
        assertNotSame(defaultBase64, settingsWithBase64Variant.getBase64Variant());
        assertEquals(ci, settingsWithBase64Variant.getClassIntrospector());
        assertEquals(ai, settingsWithBase64Variant.getAnnotationIntrospector());
        assertEquals(vc, settingsWithBase64Variant.getVisibilityChecker());
        assertEquals(pns, settingsWithBase64Variant.getPropertyNamingStrategy());
        assertEquals(tf, settingsWithBase64Variant.getTypeFactory());
        assertEquals(typer, settingsWithBase64Variant.getTypeResolverBuilder());
        assertEquals(dateFormat, settingsWithBase64Variant.getDateFormat());
        assertEquals(hi, settingsWithBase64Variant.getHandlerInstantiator());
        assertEquals(locale, settingsWithBase64Variant.getLocale());
        assertEquals(timeZone, settingsWithBase64Variant.getTimeZone());
        assertNotSame(defaultBase64, settingsWithBase64Variant.getBase64Variant());
    }
}