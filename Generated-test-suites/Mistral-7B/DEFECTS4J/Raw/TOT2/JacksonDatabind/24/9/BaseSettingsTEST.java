package com.example.junit4test;

import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {
    @Test
    public void testBaseSettingsConstructor() {
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

        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
        assertNotNull(baseSettings);
    }

    @Test
    public void testBaseSettingsConstructorWithNullArguments() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNull(baseSettings);
    }

    @Test
    public void testBaseSettingsConstructorWithInvalidArguments() {
        ClassIntrospector ci = new Object() {}; // Invalid instance
        BaseSettings baseSettings = new BaseSettings(ci, null, null, null, null, null, null, null, null, null, null);
        assertNull(baseSettings);
    }

    @Test
    public void testBaseSettingsDefaultConstructor() {
        BaseSettings baseSettings = new BaseSettings();
        assertNotNull(baseSettings);
    }

    @Test
    public void testBaseSettingsGetClassIntrospectorWithNullArgument() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertNull(baseSettings.getClassIntrospector());
    }

    @Test
    public void testBaseSettingsGetClassIntrospectorWithIllegalArgumentException() {
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        assertThrows(IllegalArgumentException.class, () -> baseSettings.getClassIntrospector());
    }
}