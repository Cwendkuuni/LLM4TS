package com.fasterxml.jackson.databind.cfg;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import static org.junit.Assert.*;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setUp() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker vc = new VisibilityChecker();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder();
        DateFormat dateFormat = new DateFormat();
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = new Locale();
        TimeZone tz = TimeZone.getDefault();
        Base64Variant defaultBase64 = new Base64Variant();

        baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
    }


    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();

        BaseSettings result = baseSettings.withClassIntrospector(ci);

        assertNotNull(result);
        assertEquals(ci, result.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();

        BaseSettings result = baseSettings.withAnnotationIntrospector(ai);

        assertNotNull(result);
        assertEquals(ai, result.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();

        BaseSettings result = baseSettings.withInsertedAnnotationIntrospector(ai);

        assertNotNull(result);
        // Check if the inserted annotation introspector is inserted in the chain
        assertEquals(AnnotationIntrospectorPair.class, result.getAnnotationIntrospector().getClass());
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();

        BaseSettings result = baseSettings.withAppendedAnnotationIntrospector(ai);

        assertNotNull(result);
        // Check if the appended annotation introspector is appended in the chain
        assertEquals(AnnotationIntrospectorPair.class, result.getAnnotationIntrospector().getClass());
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker vc = new VisibilityChecker();

        BaseSettings result = baseSettings.withVisibilityChecker(vc);

        assertNotNull(result);
        assertEquals(vc, result.getVisibilityChecker());
    }

    @Test
    public void testWithVisibility() {
        PropertyAccessor forMethod = PropertyAccessor.ALL;
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.PUBLIC_ONLY;

        BaseSettings result = baseSettings.withVisibility(forMethod, visibility);

        assertNotNull(result);
        assertEquals(visibility, result.getVisibilityChecker().findFieldVisibility(forMethod));
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy pns = new PropertyNamingStrategy();

        BaseSettings result = baseSettings.withPropertyNamingStrategy(pns);

        assertNotNull(result);
        assertEquals(pns, result.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory tf = new TypeFactory();

        BaseSettings result = baseSettings.withTypeFactory(tf);

        assertNotNull(result);
        assertEquals(tf, result.getTypeFactory());
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typer = new TypeResolverBuilder();

        BaseSettings result = baseSettings.withTypeResolverBuilder(typer);

        assertNotNull(result);
        assertEquals(typer, result.getTypeResolverBuilder());
    }

    @Test
    public void testWithDateFormat() {
        DateFormat df = new DateFormat();

        BaseSettings result = baseSettings.withDateFormat(df);

        assertNotNull(result);
        assertEquals(df, result.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator hi = new HandlerInstantiator();

        BaseSettings result = baseSettings.withHandlerInstantiator(hi);

        assertNotNull(result);
        assertEquals(hi, result.getHandlerInstantiator());
    }

    @Test
    public void testWithLocale() {
        Locale locale = new Locale();

        BaseSettings result = baseSettings.with(locale);

        assertNotNull(result);
        assertEquals(locale, result.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        TimeZone tz = TimeZone.getDefault();

        BaseSettings result = baseSettings.with(tz);

        assertNotNull(result);
        assertEquals(tz, result.getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant defaultBase64 = new Base64Variant();

        BaseSettings result = baseSettings.with(defaultBase64);

        assertNotNull(result);
        assertEquals(defaultBase64, result.getBase64Variant());
    }

    @Test
    public void testGetClassIntrospector() {
        assertNotNull(baseSettings.getClassIntrospector());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        assertNotNull(baseSettings.getAnnotationIntrospector());
    }

    @Test
    public void testGetVisibilityChecker() {
        assertNotNull(baseSettings.getVisibilityChecker());
    }

    @Test
    public void testGetPropertyNamingStrategy() {
        assertNotNull(baseSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testGetTypeFactory() {
        assertNotNull(baseSettings.getTypeFactory());
    }

    @Test
    public void testGetTypeResolverBuilder() {
        assertNotNull(baseSettings.getTypeResolverBuilder());
    }

    @Test
    public void testGetDateFormat() {
        assertNotNull(baseSettings.getDateFormat());
    }

    @Test
    public void testGetHandlerInstantiator() {
        assertNotNull(baseSettings.getHandlerInstantiator());
    }

    @Test
    public void testGetLocale() {
        assertNotNull(baseSettings.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        assertNotNull(baseSettings.getTimeZone());
    }

    @Test
    public void testGetBase64Variant() {
        assertNotNull(baseSettings.getBase64Variant());
    }
    
}