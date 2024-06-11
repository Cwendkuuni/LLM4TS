package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker.SetterFeature;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

/**
 * JUnit 4 test cases for BaseSettings class
 */
public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setup() {
        ClassIntrospector ci = null;
        AnnotationIntrospector ai = null;
        VisibilityChecker<?> vc = null;
        PropertyNamingStrategy pns = null;
        TypeFactory tf = null;
        TypeResolverBuilder<?> typer = null;
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = null;
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.STANDARD;
        baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);
    }

    @Test
    public void testBaseSettings() {
        // Test default values
        assertNull(baseSettings.getClassIntrospector());
        assertNull(baseSettings.getAnnotationIntrospector());
        assertSame(VisibilityChecker.Std.DEFAULT, baseSettings.getVisibilityChecker());
        assertNull(baseSettings.getPropertyNamingStrategy());
        assertNull(baseSettings.getTypeFactory());
        assertNull(baseSettings.getTypeResolverBuilder());
        assertSame(dateFormat, baseSettings.getDateFormat());
        assertNull(baseSettings.getHandlerInstantiator());
        assertEquals(Locale.getDefault(), baseSettings.getLocale());
        assertEquals(TimeZone.getDefault(), baseSettings.getTimeZone());
        assertEquals(Base64Variant.STANDARD, baseSettings.getBase64Variant());

        // Test getters
        ClassIntrospector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<?> newVisibilityChecker = new VisibilityChecker.Std(SetterFeature.DEFAULT);
        PropertyNamingStrategy newPropertyNamingStrategy = new PropertyNamingStrategy.Default();
        TypeFactory newTypeFactory = new TypeFactory();
        TypeResolverBuilder<?> newTypeResolverBuilder = new TypeResolverBuilder();
        DateFormat newDateFormat = new StdDateFormat();
        HandlerInstantiator newHandlerInstantiator = new HandlerInstantiator();
        Locale newLocale = new Locale("en", "US");
        TimeZone newTimeZone = new TimeZone("UTC");
        Base64Variant newBase64Variant = Base64Variant.BASE64_STANDARD;

        BaseSettings newBaseSettings = baseSettings
                .withClassIntrospector(classIntrospector)
                .withAnnotationIntrospector(annotationIntrospector)
                .withVisibilityChecker(newVisibilityChecker)
                .withPropertyNamingStrategy(newPropertyNamingStrategy)
                .withTypeFactory(newTypeFactory)
                .withTypeResolverBuilder(newTypeResolverBuilder)
                .withDateFormat(newDateFormat)
                .withHandlerInstantiator(newHandlerInstantiator)
                .withLocale(newLocale)
                .withTimeZone(newTimeZone)
                .withBase64Variant(newBase64Variant);

        assertSame(classIntrospector, newBaseSettings.getClassIntrospector());
        assertSame(annotationIntrospector, newBaseSettings.getAnnotationIntrospector());
        assertSame(newVisibilityChecker, newBaseSettings.getVisibilityChecker());
        assertSame(newPropertyNamingStrategy, newBaseSettings.getPropertyNamingStrategy());
        assertSame(newTypeFactory, newBaseSettings.getTypeFactory());
        assertSame(newTypeResolverBuilder, newBaseSettings.getTypeResolverBuilder());
        assertSame(newDateFormat, newBaseSettings.getDateFormat());
        assertSame(newHandlerInstantiator, newBaseSettings.getHandlerInstantiator());
        assertSame(newLocale, newBaseSettings.getLocale());
        assertSame(newTimeZone, newBaseSettings.getTimeZone());
        assertSame(newBase64Variant, newBaseSettings.getBase64Variant());
    }
}