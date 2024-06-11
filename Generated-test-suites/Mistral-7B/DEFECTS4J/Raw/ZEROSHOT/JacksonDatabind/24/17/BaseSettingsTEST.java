package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

/**
 * Test class for {@link BaseSettings}
 */
public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Test
    public void testBaseSettings() {
        // Create a new BaseSettings instance with default values
        ClassIntrospector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<Object> visibilityChecker = new VisibilityChecker.Std();
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.Default();
        TypeFactory typeFactory = new TypeFactory();
        TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilder() {};
        DateFormat dateFormat = StdDateFormat.DEFAULT;
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {};
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.getDefault();

        baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
                typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);

        // Test getters
        ClassIntrospector getClassIntrospector = baseSettings.getClassIntrospector();
        AnnotationIntrospector getAnnotationIntrospector = baseSettings.getAnnotationIntrospector();
        VisibilityChecker<?> getVisibilityChecker = baseSettings.getVisibilityChecker();
        PropertyNamingStrategy getPropertyNamingStrategy = baseSettings.getPropertyNamingStrategy();
        TypeFactory getTypeFactory = baseSettings.getTypeFactory();
        TypeResolverBuilder<?> getTypeResolverBuilder = baseSettings.getTypeResolverBuilder();
        DateFormat getDateFormat = baseSettings.getDateFormat();
        HandlerInstantiator getHandlerInstantiator = baseSettings.getHandlerInstantiator();
        Locale getLocale = baseSettings.getLocale();
        TimeZone getTimeZone = baseSettings.getTimeZone();
        Base64Variant getBase64Variant = baseSettings.getBase64Variant();

        // Test default values
        assertSame(getClassIntrospector(), baseSettings.getClassIntrospector());
        assertSame(getAnnotationIntrospector(), baseSettings.getAnnotationIntrospector());
        assertSame(getVisibilityChecker(), baseSettings.getVisibilityChecker());
        assertSame(getPropertyNamingStrategy(), baseSettings.getPropertyNamingStrategy());
        assertSame(getTypeFactory(), baseSettings.getTypeFactory());
        assertSame(getTypeResolverBuilder(), baseSettings.getTypeResolverBuilder());
        assertSame(getDateFormat(), baseSettings.getDateFormat());
        assertSame(getHandlerInstantiator(), baseSettings.getHandlerInstantiator());
        assertSame(getLocale(), baseSettings.getLocale());
        assertSame(getTimeZone(), baseSettings.getTimeZone());
        assertSame(getBase64Variant(), baseSettings.getBase64Variant());

        // Test factory methods
        ClassIntrospector newClassIntrospector = new ClassIntrospector();
        AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<Object> newVisibilityChecker = new VisibilityChecker.Std();
        PropertyNamingStrategy newPropertyNamingStrategy = new PropertyNamingStrategy.Default();
        TypeFactory newTypeFactory = new TypeFactory();
        TypeResolverBuilder<?> newTypeResolverBuilder = new TypeResolverBuilder() {};
        DateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z", Locale.ENGLISH);
        HandlerInstantiator newHandlerInstantiator = new HandlerInstantiator() {};
        Locale newLocale = new Locale("en", "US");
        TimeZone newTimeZone = TimeZone.getTimeZone("UTC");

        BaseSettings newBaseSettings = baseSettings.withClassIntrospector(newClassIntrospector)
                .withAnnotationIntrospector(newAnnotationIntrospector)
                .withVisibilityChecker(newVisibilityChecker)
                .withPropertyNamingStrategy(newPropertyNamingStrategy)
                .withTypeFactory(newTypeFactory)
                .withTypeResolverBuilder(newTypeResolverBuilder)
                .withDateFormat(newDateFormat)
                .withHandlerInstantiator(newHandlerInstantiator)
                .withLocale(newLocale)
                .withTimeZone(newTimeZone)
                .withBase64Variant(Base64Variant.US_ASCII);

        // Test getters with new values
        ClassIntrospector newgetClassIntrospector = newBaseSettings.getClassIntrospector();
        AnnotationIntrospector newgetAnnotationIntrospector = newBaseSettings.getAnnotationIntrospector();
        VisibilityChecker<?> newgetVisibilityChecker = newBaseSettings.getVisibilityChecker();
        PropertyNamingStrategy newgetPropertyNamingStrategy = newBaseSettings.getPropertyNamingStrategy();
        TypeFactory newgetTypeFactory = newBaseSettings.getTypeFactory();
        TypeResolverBuilder<?> newgetTypeResolverBuilder = newBaseSettings.getTypeResolverBuilder();
        DateFormat newgetDateFormat = newBaseSettings.getDateFormat();
        HandlerInstantiator newgetHandlerInstantiator = newBaseSettings.getHandlerInstantiator();
        Locale newgetLocale = newBaseSettings.getLocale();
        TimeZone newgetTimeZone = newBaseSettings.getTimeZone();
        Base64Variant newgetBase64Variant = newBaseSettings.getBase64Variant();

        // Test new values
        assertNotSame(getClassIntrospector(), newgetClassIntrospector);
        assertNotSame(getAnnotationIntrospector(), newgetAnnotationIntrospector);
        assertNotSame(getVisibilityChecker(), newgetVisibilityChecker);
        assertNotSame(getPropertyNamingStrategy(), newgetPropertyNamingStrategy);
        assertNotSame(getTypeFactory(), newgetTypeFactory);
        assertNotSame(getTypeResolverBuilder(), newgetTypeResolverBuilder);
        assertNotSame(getDateFormat(), newgetDateFormat);
        assertNotSame(getHandlerInstantiator(), newgetHandlerInstantiator);
        assertNotSame(getLocale(), newgetLocale);
        assertNotSame(getTimeZone(), newgetTimeZone);
        assertNotSame(getBase64Variant(), newgetBase64Variant);

        // Test equal and hashCode methods
        BaseSettings equalSettings = newBaseSettings;
        BaseSettings notEqualSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker,
                propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone,
                defaultBase64);

        assertTrue(equalSettings.equals(newBaseSettings));
        assertTrue(newBaseSettings.equals(equalSettings));
        assertEquals(hashCode(), newBaseSettings.hashCode());
        assertEquals(newBaseSettings.hashCode(), hashCode());

        // Test not equal settings
        assertFalse(baseSettings.equals(notEqualSettings));
        assertFalse(notEqualSettings.equals(baseSettings));
        assertNotEquals(hashCode(), notEqualSettings.hashCode());
        assertNotEquals(notEqualSettings.hashCode(), hashCode());
    }
}