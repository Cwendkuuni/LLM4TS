package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testBaseSettingsWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings settings1 = new BaseSettings(ci, null, null, null, null, null, null, null, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings settings2 = settings1.withClassIntrospector(ci);
        assertEquals(ci, settings2._classIntrospector);
    }

    @Test
    public void testBaseSettingsWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings settings1 = new BaseSettings(null, ai, null, null, null, null, null, null, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings settings2 = settings1.withAnnotationIntrospector(ai);
        assertEquals(ai, settings2._annotationIntrospector);
    }

    @Test
    public void testBaseSettingsWithVisibilityChecker() {
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        BaseSettings settings1 = new BaseSettings(null, null, vc, null, null, null, null, null, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings settings2 = settings1.withVisibilityChecker(vc);
        assertEquals(vc, settings2._visibilityChecker);
    }

    @Test
    public void testBaseSettingsWithPropertyNamingStrategy() {
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        BaseSettings settings1 = new BaseSettings(null, null, null, pns, null, null, null, null, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings settings2 = settings1.withPropertyNamingStrategy(pns);
        assertEquals(pns, settings2._propertyNamingStrategy);
    }

    @Test
    public void testBaseSettingsWithTypeFactory() {
        TypeFactory tf = new TypeFactory();
        BaseSettings settings1 = new BaseSettings(null, null, null, null, tf, null, null, null, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings settings2 = settings1.withTypeFactory(tf);
        assertEquals(tf, settings2._typeFactory);
    }

    @Test
    public void testBaseSettingsWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        BaseSettings settings1 = new BaseSettings(null, null, null, null, null, typer, null, null, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings settings2 = settings1.withTypeResolverBuilder(typer);
        assertEquals(typer, settings2._typeResolverBuilder);
    }

    @Test
    public void testBaseSettingsWithDateFormat() {
        DateFormat df = new StdDateFormat();
        BaseSettings settings1 = new BaseSettings(null, null, null, null, null, null, df, null, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings settings2 = settings1.withDateFormat(df);
        assertEquals(df, settings2._dateFormat);
    }

    @Test
    public void testBaseSettingsWithHandlerInstantiator() {
        HandlerInstantiator hi = new HandlerInstantiator();
        BaseSettings settings1 = new BaseSettings(null, null, null, null, null, null, null, hi, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings settings2 = settings1.withHandlerInstantiator(hi);
        assertEquals(hi, settings2._handlerInstantiator);
    }

    @Test
    public void testBaseSettingsWithLocale() {
        Locale locale = new Locale("en", "US");
        BaseSettings settings1 = new BaseSettings(null, null, null, null, null, null, null, null, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings settings2 = settings1.with(locale);
        assertEquals(locale, settings2.getLocale());
    }

    @Test
    public void testBaseSettingsWithTimeZone() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        BaseSettings settings1 = new BaseSettings(null, null, null, null, null, null, null, null, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings settings2 = settings1.with(tz);
        assertEquals(tz, settings2.getTimeZone());
    }

    @Test
    public void testBaseSettingsWithBase64Variant() {
        Base64Variant base64 = Base64Variant.BASE64_URL;
        BaseSettings settings1 = new BaseSettings(null, null, null, null, null, null, null, null, Locale.getDefault(), TimeZone.getDefault(), Base64Variant.STANDARD);
        BaseSettings settings2 = settings1.with(base64);
        assertEquals(base64, settings2.getBase64Variant());
    }

    @Test
    public void testNullArguments() {
        assertNull(new BaseSettings(null, null, null, null, null, null, null, null, null, null, null));
    }
}