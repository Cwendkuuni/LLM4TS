package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testBaseSettings() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNull(settings.getClassIntrospector());
        assertNull(settings.getAnnotationIntrospector());
        assertNull(settings.getVisibilityChecker());
        assertNull(settings.getPropertyNamingStrategy());
        assertNull(settings.getTypeFactory());
        assertNull(settings.getTypeResolverBuilder());
        assertNull(settings.getDateFormat());
        assertNull(settings.getHandlerInstantiator());
        assertNull(settings.getLocale());
        assertNull(settings.getTimeZone());
        assertNull(settings.getBase64Variant());

        // Test with valid arguments
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.US;
        TimeZone tz = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.STANDARD;
        BaseSettings settings1 = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
        assertSame(ci, settings1.getClassIntrospector());
        assertSame(ai, settings1.getAnnotationIntrospector());
        assertSame(vc, settings1.getVisibilityChecker());
        assertSame(pns, settings1.getPropertyNamingStrategy());
        assertSame(tf, settings1.getTypeFactory());
        assertSame(typer, settings1.getTypeResolverBuilder());
        assertSame(dateFormat, settings1.getDateFormat());
        assertSame(hi, settings1.getHandlerInstantiator());
        assertSame(locale, settings1.getLocale());
        assertSame(tz, settings1.getTimeZone());
        assertSame(defaultBase64, settings1.getBase64Variant());

        // Test getters
        ClassIntrospector ci1 = settings1.getClassIntrospector();
        assertSame(ci, ci1);

        AnnotationIntrospector ai1 = settings1.getAnnotationIntrospector();
        assertSame(ai, ai1);

        VisibilityChecker<?> vc1 = settings1.getVisibilityChecker();
        assertSame(vc, vc1);

        PropertyNamingStrategy pns1 = settings1.getPropertyNamingStrategy();
        assertSame(pns, pns1);

        TypeFactory tf1 = settings1.getTypeFactory();
        assertSame(tf, tf1);

        TypeResolverBuilder<?> typer1 = settings1.getTypeResolverBuilder();
        assertSame(typer, typer1);

        DateFormat dateFormat1 = settings1.getDateFormat();
        assertSame(dateFormat, dateFormat1);

        HandlerInstantiator hi1 = settings1.getHandlerInstantiator();
        assertSame(hi, hi1);

        Locale locale1 = settings1.getLocale();
        assertSame(locale, locale1);

        TimeZone tz1 = settings1.getTimeZone();
        assertSame(tz, tz1);

        Base64Variant defaultBase641 = settings1.getBase64Variant();
        assertSame(defaultBase64, defaultBase641);
    }

    @Test
    public void testFactoryMethods() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.US;
        TimeZone tz = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.STANDARD;
        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);

        BaseSettings settings1 = settings.withClassIntrospector(new ClassIntrospector());
        assertNotSame(ci, settings1.getClassIntrospector());
        assertSame(ai, settings1.getAnnotationIntrospector());
        assertSame(vc, settings1.getVisibilityChecker());
        assertSame(pns, settings1.getPropertyNamingStrategy());
        assertSame(tf, settings1.getTypeFactory());
        assertSame(typer, settings1.getTypeResolverBuilder());
        assertSame(dateFormat, settings1.getDateFormat());
        assertSame(hi, settings1.getHandlerInstantiator());
        assertSame(locale, settings1.getLocale());
        assertSame(tz, settings1.getTimeZone());
        assertSame(defaultBase64, settings1.getBase64Variant());

        BaseSettings settings2 = settings.withAnnotationIntrospector(new AnnotationIntrospector());
        assertSame(ci, settings2.getClassIntrospector());
        assertNotSame(ai, settings2.getAnnotationIntrospector());
        assertSame(vc, settings2.getVisibilityChecker());
        assertSame(pns, settings2.getPropertyNamingStrategy());
        assertSame(tf, settings2.getTypeFactory());
        assertSame(typer, settings2.getTypeResolverBuilder());
        assertSame(dateFormat, settings2.getDateFormat());
        assertSame(hi, settings2.getHandlerInstantiator());
        assertSame(locale, settings2.getLocale());
        assertSame(tz, settings2.getTimeZone());
        assertSame(defaultBase64, settings2.getBase64Variant());

        BaseSettings settings3 = settings.withVisibilityChecker(new VisibilityChecker<>());
        assertSame(ci, settings3.getClassIntrospector());
        assertSame(ai, settings3.getAnnotationIntrospector());
        assertNotSame(vc, settings3.getVisibilityChecker());
        assertSame(pns, settings3.getPropertyNamingStrategy());
        assertSame(tf, settings3.getTypeFactory());
        assertSame(typer, settings3.getTypeResolverBuilder());
        assertSame(dateFormat, settings3.getDateFormat());
        assertSame(hi, settings3.getHandlerInstantiator());
        assertSame(locale, settings3.getLocale());
        assertSame(tz, settings3.getTimeZone());
        assertSame(defaultBase64, settings3.getBase64Variant());

        BaseSettings settings4 = settings.withPropertyNamingStrategy(new PropertyNamingStrategy());
        assertSame(ci, settings4.getClassIntrospector());
        assertSame(ai, settings4.getAnnotationIntrospector());
        assertSame(vc, settings4.getVisibilityChecker());
        assertNotSame(pns, settings4.getPropertyNamingStrategy());
        assertSame(tf, settings4.getTypeFactory());
        assertSame(typer, settings4.getTypeResolverBuilder());
        assertSame(dateFormat, settings4.getDateFormat());
        assertSame(hi, settings4.getHandlerInstantiator());
        assertSame(locale, settings4.getLocale());
        assertSame(tz, settings4.getTimeZone());
        assertSame(defaultBase64, settings4.getBase64Variant());

        BaseSettings settings5 = settings.withTypeFactory(new TypeFactory());
        assertSame(ci, settings5.getClassIntrospector());
        assertSame(ai, settings5.getAnnotationIntrospector());
        assertSame(vc, settings5.getVisibilityChecker());
        assertSame(pns, settings5.getPropertyNamingStrategy());
        assertNotSame(tf, settings5.getTypeFactory());
        assertSame(typer, settings5.getTypeResolverBuilder());
        assertSame(dateFormat, settings5.getDateFormat());
        assertSame(hi, settings5.getHandlerInstantiator());
        assertSame(locale, settings5.getLocale());
        assertSame(tz, settings5.getTimeZone());
        assertSame(defaultBase64, settings5.getBase64Variant());

        BaseSettings settings6 = settings.withTypeResolverBuilder(new TypeResolverBuilder<>());
        assertSame(ci, settings6.getClassIntrospector());
        assertSame(ai, settings6.getAnnotationIntrospector());
        assertSame(vc, settings6.getVisibilityChecker());
        assertSame(pns, settings6.getPropertyNamingStrategy());
        assertSame(tf, settings6.getTypeFactory());
        assertNotSame(typer, settings6.getTypeResolverBuilder());
        assertSame(dateFormat, settings6.getDateFormat());
        assertSame(hi, settings6.getHandlerInstantiator());
        assertSame(locale, settings6.getLocale());
        assertSame(tz, settings6.getTimeZone());
        assertSame(defaultBase64, settings6.getBase64Variant());

        BaseSettings settings7 = settings.withDateFormat(new StdDateFormat());
        assertSame(ci, settings7.getClassIntrospector());
        assertSame(ai, settings7.getAnnotationIntrospector());
        assertSame(vc, settings7.getVisibilityChecker());
        assertSame(pns, settings7.getPropertyNamingStrategy());
        assertSame(tf, settings7.getTypeFactory());
        assertSame(typer, settings7.getTypeResolverBuilder());
        assertNotSame(dateFormat, settings7.getDateFormat());
        assertSame(hi, settings7.getHandlerInstantiator());
        assertSame(locale, settings7.getLocale());
        assertSame(tz, settings7.getTimeZone());
        assertSame(defaultBase64, settings7.getBase64Variant());

        BaseSettings settings8 = settings.withHandlerInstantiator(new HandlerInstantiator());
        assertSame(ci, settings8.getClassIntrospector());
        assertSame(ai, settings8.getAnnotationIntrospector());
        assertSame(vc, settings8.getVisibilityChecker());
        assertSame(pns, settings8.getPropertyNamingStrategy());
        assertSame(tf, settings8.getTypeFactory());
        assertSame(typer, settings8.getTypeResolverBuilder());
        assertSame(dateFormat, settings8.getDateFormat());
        assertNotSame(hi, settings8.getHandlerInstantiator());
        assertSame(locale, settings8.getLocale());
        assertSame(tz, settings8.getTimeZone());
        assertSame(defaultBase64, settings8.getBase64Variant());

        BaseSettings settings9 = settings.with(Locale.US);
        assertSame(ci, settings9.getClassIntrospector());
        assertSame(ai, settings9.getAnnotationIntrospector());
        assertSame(vc, settings9.getVisibilityChecker());
        assertSame(pns, settings9.getPropertyNamingStrategy());
        assertSame(tf, settings9.getTypeFactory());
        assertSame(typer, settings9.getTypeResolverBuilder());
        assertSame(dateFormat, settings9.getDateFormat());
        assertSame(hi, settings9.getHandlerInstantiator());
        assertNotSame(locale, settings9.getLocale());
        assertSame(tz, settings9.getTimeZone());
        assertSame(defaultBase64, settings9.getBase64Variant());

        BaseSettings settings10 = settings.with(TimeZone.getDefault());
        assertSame(ci, settings10.getClassIntrospector());
        assertSame(ai, settings10.getAnnotationIntrospector());
        assertSame(vc, settings10.getVisibilityChecker());
        assertSame(pns, settings10.getPropertyNamingStrategy());
        assertSame(tf, settings10.getTypeFactory());
        assertSame(typer, settings10.getTypeResolverBuilder());
        assertSame(dateFormat, settings10.getDateFormat());
        assertSame(hi, settings10.getHandlerInstantiator());
        assertSame(locale, settings10.getLocale());
        assertNotSame(tz, settings10.getTimeZone());
        assertSame(defaultBase64, settings10.getBase64Variant());

        BaseSettings settings11 = settings.with(Base64Variant.BASE64_URL_SAFE);
        assertSame(ci, settings11.getClassIntrospector());
        assertSame(ai, settings11.getAnnotationIntrospector());
        assertSame(vc, settings11.getVisibilityChecker());
        assertSame(pns, settings11.getPropertyNamingStrategy());
        assertSame(tf, settings11.getTypeFactory());
        assertSame(typer, settings11.getTypeResolverBuilder());
        assertSame(dateFormat, settings11.getDateFormat());
        assertSame(hi, settings11.getHandlerInstantiator());
        assertSame(locale, settings11.getLocale());
        assertSame(tz, settings11.getTimeZone());
        assertNotSame(defaultBase64, settings11.getBase64Variant());
    }
}