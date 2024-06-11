package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testBaseSettingsWithClassIntrospector() {
        ClassIntrospector ci = new CustomClassIntrospector(); // Replace with your implementation
        BaseSettings settings1 = new BaseSettings(ci, null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings settings2 = settings1.withClassIntrospector(ci);
        assertEquals(settings1, settings2);
    }

    @Test
    public void testBaseSettingsWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new CustomAnnotationIntrospector(); // Replace with your implementation
        BaseSettings settings1 = new BaseSettings(null, ai, null, null, null, null, null, null, null, null, null, null);
        BaseSettings settings2 = settings1.withAnnotationIntrospector(ai);
        assertEquals(settings1, settings2);
    }

    @Test
    public void testBaseSettingsWithVisibilityChecker() {
        VisibilityChecker<?> vc = new CustomVisibilityChecker(); // Replace with your implementation
        BaseSettings settings1 = new BaseSettings(null, null, vc, null, null, null, null, null, null, null, null, null);
        BaseSettings settings2 = settings1.withVisibilityChecker(vc);
        assertEquals(settings1, settings2);
    }

    @Test
    public void testBaseSettingsWithPropertyNamingStrategy() {
        PropertyNamingStrategy pns = new CustomPropertyNamingStrategy(); // Replace with your implementation
        BaseSettings settings1 = new BaseSettings(null, null, null, pns, null, null, null, null, null, null, null, null);
        BaseSettings settings2 = settings1.withPropertyNamingStrategy(pns);
        assertEquals(settings1, settings2);
    }

    @Test
    public void testBaseSettingsWithTypeFactory() {
        TypeFactory tf = new CustomTypeFactory(); // Replace with your implementation
        BaseSettings settings1 = new BaseSettings(null, null, null, null, tf, null, null, null, null, null, null, null);
        BaseSettings settings2 = settings1.withTypeFactory(tf);
        assertEquals(settings1, settings2);
    }

    @Test
    public void testBaseSettingsWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typer = new CustomTypeResolverBuilder(); // Replace with your implementation
        BaseSettings settings1 = new BaseSettings(null, null, null, null, null, typer, null, null, null, null, null, null);
        BaseSettings settings2 = settings1.withTypeResolverBuilder(typer);
        assertEquals(settings1, settings2);
    }

    @Test
    public void testBaseSettingsWithDateFormat() {
        DateFormat df = new CustomDateFormat(); // Replace with your implementation
        BaseSettings settings1 = new BaseSettings(null, null, null, null, null, null, df, null, null, null, null, null);
        BaseSettings settings2 = settings1.withDateFormat(df);
        assertEquals(settings1, settings2);
    }

    @Test
    public void testBaseSettingsWithHandlerInstantiator() {
        HandlerInstantiator hi = new CustomHandlerInstantiator(); // Replace with your implementation
        BaseSettings settings1 = new BaseSettings(null, null, null, null, null, null, null, hi, null, null, null, null);
        BaseSettings settings2 = settings1.withHandlerInstantiator(hi);
        assertEquals(settings1, settings2);
    }

    @Test
    public void testBaseSettingsWithLocale() {
        Locale locale = Locale.US;
        BaseSettings settings1 = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings settings2 = settings1.with(locale);
        assertEquals(settings1, settings2);
        assertNotSame(settings1.getLocale(), settings2.getLocale());
        assertEquals(locale, settings2.getLocale());
    }

    @Test
    public void testBaseSettingsWithTimeZone() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        BaseSettings settings1 = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings settings2 = settings1.with(tz);
        assertEquals(settings1, settings2);
        assertNotSame(settings1.getTimeZone(), settings2.getTimeZone());
        assertEquals(tz, settings2.getTimeZone());
    }

    @Test
    public void testBaseSettingsWithBase64Variant() {
        Base64Variant base64 = Base64Variant.STANDARD;
        BaseSettings settings1 = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings settings2 = settings1.with(base64);
        assertEquals(settings1, settings2);
        assertNotSame(settings1.getDefaultBase64(), settings2.getDefaultBase64());
        assertEquals(base64, settings2.getDefaultBase64());
    }
}