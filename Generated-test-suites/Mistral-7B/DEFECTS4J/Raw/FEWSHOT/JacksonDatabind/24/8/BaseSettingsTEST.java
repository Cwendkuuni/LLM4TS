package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testGetters() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.DEFAULT);

        assertEquals(null, settings.getClassIntrospector());
        assertEquals(null, settings.getAnnotationIntrospector());
        assertEquals(null, settings.getVisibilityChecker());
        assertEquals(null, settings.getPropertyNamingStrategy());
        assertEquals(null, settings.getTypeFactory());
        assertEquals(null, settings.getTypeResolverBuilder());
        assertEquals(null, settings.getDateFormat());
        assertEquals(null, settings.getHandlerInstantiator());
        assertEquals(Locale.getDefault(), settings.getLocale());
        assertEquals(TimeZone.getDefault(), settings.getTimeZone());
        assertEquals(Base64Variant.DEFAULT, settings.getBase64Variant());

        BaseSettings settingsWithValues = new BaseSettings(null, null, null, null, null, null, new StdDateFormat(), null,
                Locale.US, TimeZone.getTimeZone("UTC"), Base64Variant.UU_ENCODED);

        assertNotNull(settingsWithValues.getClassIntrospector());
        assertNotNull(settingsWithValues.getAnnotationIntrospector());
        assertNotNull(settingsWithValues.getVisibilityChecker());
        assertNotNull(settingsWithValues.getPropertyNamingStrategy());
        assertNotNull(settingsWithValues.getTypeFactory());
        assertNotNull(settingsWithValues.getTypeResolverBuilder());
        assertNotNull(settingsWithValues.getDateFormat());
        assertNotNull(settingsWithValues.getHandlerInstantiator());
        assertEquals(Locale.US, settingsWithValues.getLocale());
        assertEquals(TimeZone.getTimeZone("UTC"), settingsWithValues.getTimeZone());
        assertEquals(Base64Variant.UU_ENCODED, settingsWithValues.getBase64Variant());
    }

    @Test
    public void testSetters() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, Locale.getDefault(),
                TimeZone.getDefault(), Base64Variant.DEFAULT);

        BaseSettings settingsWithClassIntrospector = settings.withClassIntrospector(new ClassIntrospector());
        assertEquals(new ClassIntrospector(), settingsWithClassIntrospector.getClassIntrospector());
        assertEquals(settings, settingsWithClassIntrospector);

        BaseSettings settingsWithAnnotationIntrospector = settingsWithClassIntrospector
                .withAnnotationIntrospector(new AnnotationIntrospector());
        assertEquals(new AnnotationIntrospector(), settingsWithAnnotationIntrospector.getAnnotationIntrospector());
        assertEquals(settingsWithClassIntrospector, settingsWithAnnotationIntrospector);

        BaseSettings settingsWithDateFormat = settingsWithAnnotationIntrospector
                .withDateFormat(new StdDateFormat());
        assertEquals(new StdDateFormat(), settingsWithDateFormat.getDateFormat());
        assertEquals(settingsWithAnnotationIntrospector, settingsWithDateFormat);

        BaseSettings settingsWithHandlerInstantiator = settingsWithDateFormat
                .withHandlerInstantiator(new HandlerInstantiator());
        assertEquals(new HandlerInstantiator(), settingsWithHandlerInstantiator.getHandlerInstantiator());
        assertEquals(settingsWithDateFormat, settingsWithHandlerInstantiator);

        BaseSettings settingsWithLocale = settingsWithHandlerInstantiator.with(Locale.US);
        assertEquals(Locale.US, settingsWithLocale.getLocale());
        assertEquals(settingsWithHandlerInstantiator, settingsWithLocale);

        BaseSettings settingsWithTimeZone = settingsWithLocale.with(TimeZone.getTimeZone("UTC"));
        assertEquals(TimeZone.getTimeZone("UTC"), settingsWithTimeZone.getTimeZone());
        assertEquals(settingsWithLocale, settingsWithTimeZone);

        BaseSettings settingsWithBase64Variant = settingsWithTimeZone.with(Base64Variant.UU_ENCODED);
        assertEquals(Base64Variant.UU_ENCODED, settingsWithBase64Variant.getBase64Variant());
        assertEquals(settingsWithTimeZone, settingsWithBase64Variant);
    }
}