package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.time.ZoneId;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker.Visibility;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

/**
 * Unit tests for {@link BaseSettings} class
 */
public class BaseSettingsTest {

    @Test
    public void testBaseSettingsConstruction() {
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

        BaseSettings baseSettings1 = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
        BaseSettings baseSettings2 = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);

        assertEquals(baseSettings1, baseSettings2);
    }

    @Test
    public void testBaseSettingsFactoryMethods() {
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

        BaseSettings baseSettingsWithClassIntrospector = baseSettings.withClassIntrospector(new ClassIntrospector() {});
        assertEquals(baseSettings, baseSettingsWithClassIntrospector);

        BaseSettings baseSettingsWithAnnotationIntrospector = baseSettings.withAnnotationIntrospector(new AnnotationIntrospector() {});
        assertEquals(baseSettings, baseSettingsWithAnnotationIntrospector);

        BaseSettings baseSettingsWithInsertedAnnotationIntrospector = baseSettings.withInsertedAnnotationIntrospector(new AnnotationIntrospector() {});
        assertEquals(baseSettings, baseSettingsWithInsertedAnnotationIntrospector);

        BaseSettings baseSettingsWithAppendedAnnotationIntrospector = baseSettings.withAppendedAnnotationIntrospector(new AnnotationIntrospector() {});
        assertEquals(baseSettings, baseSettingsWithAppendedAnnotationIntrospector);

        BaseSettings baseSettingsWithVisibilityChecker = baseSettings.withVisibilityChecker(new VisibilityChecker<PropertyAccessor>() {
            @Override
            public Visibility getMemberVisibility(PropertyAccessor pa) {
                return Visibility.NONE;
            }
        });
        assertNotEquals(baseSettings, baseSettingsWithVisibilityChecker);

        BaseSettings baseSettingsWithVisibility = baseSettings.with(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);
        assertNotEquals(baseSettings, baseSettingsWithVisibility);

        BaseSettings baseSettingsWithPropertyNamingStrategy = baseSettings.withPropertyNamingStrategy(new PropertyNamingStrategy() {});
        assertEquals(baseSettings, baseSettingsWithPropertyNamingStrategy);

        BaseSettings baseSettingsWithTypeFactory = baseSettings.withTypeFactory(new TypeFactory() {});
        assertEquals(baseSettings, baseSettingsWithTypeFactory);

        BaseSettings baseSettingsWithTypeResolverBuilder = baseSettings.withTypeResolverBuilder(new TypeResolverBuilder() {});
        assertEquals(baseSettings, baseSettingsWithTypeResolverBuilder);

        BaseSettings baseSettingsWithDateFormat = baseSettings.withDateFormat(new StdDateFormat() {});
        assertEquals(baseSettings, baseSettingsWithDateFormat);

        BaseSettings baseSettingsWithHandlerInstantiator = baseSettings.withHandlerInstantiator(new HandlerInstantiator() {});
        assertEquals(baseSettings, baseSettingsWithHandlerInstantiator);

        BaseSettings baseSettingsWithLocale = baseSettings.with(Locale.US);
        assertNotEquals(baseSettings, baseSettingsWithLocale);

        BaseSettings baseSettingsWithTimeZone = baseSettings.with(ZoneId.of("America/Los_Angeles"));
        assertNotEquals(baseSettings, baseSettingsWithTimeZone);

        BaseSettings baseSettingsWithBase64Variant = baseSettings.with(Base64Variant.RFC3548);
        assertNotEquals(baseSettings, baseSettingsWithBase64Variant);
    }
}