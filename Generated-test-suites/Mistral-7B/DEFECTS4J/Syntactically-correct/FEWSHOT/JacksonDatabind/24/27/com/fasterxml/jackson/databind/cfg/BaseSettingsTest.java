package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testBaseSettingsWithClassIntrospector() {
        ClassIntrospector classIntrospector = null; // initialize with actual value
        BaseSettings baseSettings = new BaseSettings(classIntrospector, null, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withClassIntrospector(new ClassIntrospector());
        assertEquals(newClassIntrospector, newBaseSettings.getClassIntrospector());
        assertEquals(baseSettings, newBaseSettings.clone()); // check if cloned
    }

    @Test
    public void testBaseSettingsWithAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = null; // initialize with actual value
        BaseSettings baseSettings = new BaseSettings(null, annotationIntrospector, null, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withAnnotationIntrospector(new AnnotationIntrospector());
        assertEquals(newAnnotationIntrospector, newBaseSettings.getAnnotationIntrospector());
        assertEquals(baseSettings, newBaseSettings.clone()); // check if cloned
    }

    @Test
    public void testBaseSettingsWithVisibilityChecker() {
        VisibilityChecker<?> visibilityChecker = null; // initialize with actual value
        BaseSettings baseSettings = new BaseSettings(null, null, visibilityChecker, null, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withVisibilityChecker(new VisibilityChecker<>());
        assertEquals(newVisibilityChecker, newBaseSettings.getVisibilityChecker());
        assertEquals(baseSettings, newBaseSettings.clone()); // check if cloned
    }

    @Test
    public void testBaseSettingsWithPropertyNamingStrategy() {
        PropertyNamingStrategy propertyNamingStrategy = null; // initialize with actual value
        BaseSettings baseSettings = new BaseSettings(null, null, null, propertyNamingStrategy, null, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withPropertyNamingStrategy(new PropertyNamingStrategy());
        assertEquals(newPropertyNamingStrategy, newBaseSettings.getPropertyNamingStrategy());
        assertEquals(baseSettings, newBaseSettings.clone()); // check if cloned
    }

    @Test
    public void testBaseSettingsWithTypeFactory() {
        TypeFactory typeFactory = null; // initialize with actual value
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, typeFactory, null, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withTypeFactory(new TypeFactory());
        assertEquals(newTypeFactory, newBaseSettings.getTypeFactory());
        assertEquals(baseSettings, newBaseSettings.clone()); // check if cloned
    }

    @Test
    public void testBaseSettingsWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typeResolverBuilder = null; // initialize with actual value
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, typeResolverBuilder, null, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withTypeResolverBuilder(new TypeResolverBuilder<>());
        assertEquals(newTypeResolverBuilder, newBaseSettings.getTypeResolverBuilder());
        assertEquals(baseSettings, newBaseSettings.clone()); // check if cloned
    }

    @Test
    public void testBaseSettingsWithDateFormat() {
        DateFormat dateFormat = null; // initialize with actual value
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, dateFormat, null, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withDateFormat(new DateFormat());
        assertEquals(newDateFormat, newBaseSettings.getDateFormat());
        assertEquals(baseSettings, newBaseSettings.clone()); // check if cloned
    }

    @Test
    public void testBaseSettingsWithHandlerInstantiator() {
        HandlerInstantiator handlerInstantiator = null; // initialize with actual value
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, handlerInstantiator, null, null, null);
        BaseSettings newBaseSettings = baseSettings.withHandlerInstantiator(new HandlerInstantiator());
        assertEquals(newHandlerInstantiator, newBaseSettings.getHandlerInstantiator());
        assertEquals(baseSettings, newBaseSettings.clone()); // check if cloned
    }

    @Test
    public void testBaseSettingsWithLocale() {
        Locale locale = null; // initialize with actual value
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, locale, null, null);
        BaseSettings newBaseSettings = baseSettings.with(locale);
        assertEquals(newLocale, newBaseSettings.getLocale());
        assertEquals(baseSettings, newBaseSettings.clone()); // check if cloned
    }

    @Test
    public void testBaseSettingsWithTimeZone() {
        TimeZone timeZone = null; // initialize with actual value
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, timeZone, null);
        BaseSettings newBaseSettings = baseSettings.with(timeZone);
        assertEquals(newTimeZone, newBaseSettings.getTimeZone());
        assertEquals(baseSettings, newBaseSettings.clone()); // check if cloned
    }

    @Test
    public void testBaseSettingsWithBase64Variant() {
        Base64Variant base64Variant = null; // initialize with actual value
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, base64Variant);
        BaseSettings newBaseSettings = baseSettings.with(base64Variant);
        assertEquals(newBase64Variant, newBaseSettings.getBase64Variant());
        assertEquals(baseSettings, newBaseSettings.clone()); // check if cloned
    }
}