package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testBaseSettings() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null);
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

        settings = new BaseSettings(null, null, null, null, null, null, null, null, Locale.US, TimeZone.getDefault(), null);
        assertNull(settings.getClassIntrospector());
        assertNull(settings.getAnnotationIntrospector());
        assertNull(settings.getVisibilityChecker());
        assertNull(settings.getPropertyNamingStrategy());
        assertNull(settings.getTypeFactory());
        assertNull(settings.getTypeResolverBuilder());
        assertNotNull(settings.getDateFormat());
        assertNull(settings.getHandlerInstantiator());
        assertEquals(Locale.US, settings.getLocale());
        assertNotNull(settings.getTimeZone());
        assertNull(settings.getBase64Variant());

        settings = new BaseSettings(null, null, null, null, null, null, null, null, Locale.US, null, null, null);
        assertNull(settings.getClassIntrospector());
        assertNull(settings.getAnnotationIntrospector());
        assertNull(settings.getVisibilityChecker());
        assertNull(settings.getPropertyNamingStrategy());
        assertNull(settings.getTypeFactory());
        assertNull(settings.getTypeResolverBuilder());
        assertNotNull(settings.getDateFormat());
        assertNull(settings.getHandlerInstantiator());
        assertEquals(Locale.US, settings.getLocale());
        assertNull(settings.getTimeZone());
        assertNull(settings.getBase64Variant());
    }

    @Test
    public void testSetters() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null);

        ClassIntrospector ci = new ClassIntrospector();
        settings = settings.withClassIntrospector(ci);
        assertEquals(ci, settings.getClassIntrospector());

        AnnotationIntrospector ai = new AnnotationIntrospector();
        settings = settings.withAnnotationIntrospector(ai);
        assertEquals(ai, settings.getAnnotationIntrospector());

        VisibilityChecker<?> vc = new VisibilityChecker<>();
        settings = settings.withVisibilityChecker(vc);
        assertEquals(vc, settings.getVisibilityChecker());

        PropertyNamingStrategy pns = new StdPropertyNamingStrategy();
        settings = settings.withPropertyNamingStrategy(pns);
        assertEquals(pns, settings.getPropertyNamingStrategy());

        TypeFactory tf = new TypeFactory();
        settings = settings.withTypeFactory(tf);
        assertEquals(tf, settings.getTypeFactory());

        TypeResolverBuilder<?> typer = new SimpleTypeResolverBuilder();
        settings = settings.withTypeResolverBuilder(typer);
        assertEquals(typer, settings.getTypeResolverBuilder());

        DateFormat df = new SimpleDateFormat();
        settings = settings.withDateFormat(df);
        assertEquals(df, settings.getDateFormat());

        HandlerInstantiator hi = new SimpleHandlerInstantiator();
        settings = settings.withHandlerInstantiator(hi);
        assertEquals(hi, settings.getHandlerInstantiator());

        Locale locale = Locale.US;
        settings = settings.with(locale);
        assertEquals(locale, settings.getLocale());

        TimeZone timeZone = TimeZone.getDefault();
        settings = settings.with(timeZone);
        assertEquals(timeZone, settings.getTimeZone());

        Base64Variant base64 = Base64Variants.STANDARD;
        settings = settings.with(base64);
        assertEquals(base64, settings.getBase64Variant());
    }
}