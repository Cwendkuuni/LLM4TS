package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.cfg.BaseSettings;
import com.fasterxml.jackson.databind.cfg.AnnotationIntrospector;
import com.fasterxml.jackson.databind.cfg.ClassIntrospector;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.TypeFactory;
import com.fasterxml.jackson.databind.cfg.TypeResolverBuilder;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Test;

/**
 * Test class for BaseSettings class
 */
public class BaseSettingsTest {

    /**
     * Test method for constructor BaseSettings(ClassIntrospector, AnnotationIntrospector, VisibilityChecker, PropertyNamingStrategy, TypeFactory, TypeResolverBuilder, DateFormat, HandlerInstantiator, Locale, TimeZone, Base64Variant)
     */
    @Test
    public void testBaseSettings() {
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;
        ClassIntrospector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<?> visibilityChecker = new VisibilityChecker<>();
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy();
        TypeFactory typeFactory = new TypeFactory();
        TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilder<>();
        StdDateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();

        BaseSettings baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);

        assertNotNull(baseSettings);

        assertEquals(classIntrospector, baseSettings.getClassIntrospector());
        assertEquals(annotationIntrospector, baseSettings.getAnnotationIntrospector());
        assertEquals(visibilityChecker, baseSettings.getVisibilityChecker());
        assertEquals(propertyNamingStrategy, baseSettings.getPropertyNamingStrategy());
        assertEquals(typeFactory, baseSettings.getTypeFactory());
        assertEquals(typeResolverBuilder, baseSettings.getTypeResolverBuilder());
        assertEquals(dateFormat, baseSettings.getDateFormat());
        assertEquals(handlerInstantiator, baseSettings.getHandlerInstantiator());
        assertEquals(locale, baseSettings.getLocale());
        assertEquals(timeZone, baseSettings.getTimeZone());
        assertEquals(defaultBase64, baseSettings.getBase64Variant());
    }

    /**
     * Test method for method withClassIntrospector(ClassIntrospector)
     */
    @Test
    public void testWithClassIntrospector() {
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, defaultBase64);

        BaseSettings newBaseSettings = baseSettings.withClassIntrospector(ci);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(ci, newBaseSettings.getClassIntrospector());
    }

    /**
     * Test method for method withAnnotationIntrospector(AnnotationIntrospector)
     */
    @Test
    public void testWithAnnotationIntrospector() {
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, defaultBase64);

        BaseSettings newBaseSettings = baseSettings.withAnnotationIntrospector(ai);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(ai, newBaseSettings.getAnnotationIntrospector());
    }

    /**
     * Test method for method withInsertedAnnotationIntrospector(AnnotationIntrospector)
     */
    @Test
    public void testWithInsertedAnnotationIntrospector() {
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, defaultBase64);

        BaseSettings newBaseSettings = baseSettings.withInsertedAnnotationIntrospector(ai);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(AnnotationIntrospectorPair.create(ai, baseSettings.getAnnotationIntrospector()), newBaseSettings.getAnnotationIntrospector());
    }

    /**
     * Test method for method withAppendedAnnotationIntrospector(AnnotationIntrospector)
     */
    @Test
    public void testWithAppendedAnnotationIntrospector() {
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, defaultBase64);

        BaseSettings newBaseSettings = baseSettings.withAppendedAnnotationIntrospector(ai);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(BaseSettings.AnnotationIntrospectorPair.create(baseSettings.getAnnotationIntrospector(), ai), newBaseSettings.getAnnotationIntrospector());
    }

    /**
     * Test method for method withVisibilityChecker(VisibilityChecker)
     */
    @Test
    public void testWithVisibilityChecker() {
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, defaultBase64);

        BaseSettings newBaseSettings = baseSettings.withVisibilityChecker(vc);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(vc, newBaseSettings.getVisibilityChecker());
    }

    /**
     * Test method for method withVisibility(PropertyAccessor, JsonAutoDetect.Visibility)
     */
    @Test
    public void testWithVisibility() {
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;
        PropertyAccessor forMethod = null;
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.ANY;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, defaultBase64);

        BaseSettings newBaseSettings = baseSettings.withVisibility(forMethod, visibility);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(baseSettings.getVisibilityChecker().withVisibility(forMethod, visibility), newBaseSettings.getVisibilityChecker());
    }

    /**
     * Test method for method withPropertyNamingStrategy(PropertyNamingStrategy)
     */
    @Test
    public void testWithPropertyNamingStrategy() {
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, defaultBase64);

        BaseSettings newBaseSettings = baseSettings.withPropertyNamingStrategy(pns);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(pns, newBaseSettings.getPropertyNamingStrategy());
    }

    /**
     * Test method for method withTypeFactory(TypeFactory)
     */
    @Test
    public void testWithTypeFactory() {
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;
        TypeFactory tf = new TypeFactory();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, defaultBase64);

        BaseSettings newBaseSettings = baseSettings.withTypeFactory(tf);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(tf, newBaseSettings.getTypeFactory());
    }

    /**
     * Test method for method withTypeResolverBuilder(TypeResolverBuilder)
     */
    @Test
    public void testWithTypeResolverBuilder() {
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, defaultBase64);

        BaseSettings newBaseSettings = baseSettings.withTypeResolverBuilder(typer);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(typer, newBaseSettings.getTypeResolverBuilder());
    }

    /**
     * Test method for method withDateFormat(DateFormat)
     */
    @Test
    public void testWithDateFormat() {
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;
        DateFormat df = new StdDateFormat();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, defaultBase64);

        BaseSettings newBaseSettings = baseSettings.withDateFormat(df);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(df, newBaseSettings.getDateFormat());
    }

    /**
     * Test method for method withHandlerInstantiator(HandlerInstantiator)
     */
    @Test
    public void testWithHandlerInstantiator() {
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;
        HandlerInstantiator hi = new HandlerInstantiator();
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, defaultBase64);

        BaseSettings newBaseSettings = baseSettings.withHandlerInstantiator(hi);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(hi, newBaseSettings.getHandlerInstantiator());
    }

    /**
     * Test method for method with(Locale)
     */
    @Test
    public void testWith() {
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;
        Locale locale = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, defaultBase64);

        BaseSettings newBaseSettings = baseSettings.with(locale);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(locale, newBaseSettings.getLocale());
    }

    /**
     * Test method for method with(TimeZone)
     */
    @Test
    public void testWithTimeZone() {
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;
        TimeZone tz = null;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, defaultBase64);

        BaseSettings newBaseSettings = baseSettings.with(tz);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(tz, newBaseSettings.getTimeZone());
    }

    /**
     * Test method for method with(Base64Variant)
     */
    @Test
    public void testWithBase64Variant() {
        Base64Variant defaultBase64 = Base64Variant.DEFAULT;
        Base64Variant base64 = Base64Variant.US_ASCII;
        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, defaultBase64);

        BaseSettings newBaseSettings = baseSettings.with(base64);

        assertNotSame(baseSettings, newBaseSettings);
        assertEquals(base64, newBaseSettings.getBase64Variant());
    }
}