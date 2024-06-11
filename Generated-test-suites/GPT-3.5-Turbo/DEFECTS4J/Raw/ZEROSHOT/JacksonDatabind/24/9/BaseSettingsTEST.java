package com.fasterxml.jackson.databind.cfg;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setUp() {
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings result = baseSettings.withClassIntrospector(ci);
        assertEquals(ci, result.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings result = baseSettings.withAnnotationIntrospector(ai);
        assertEquals(ai, result.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings result = baseSettings.withInsertedAnnotationIntrospector(ai);
        assertEquals(ai, result.getAnnotationIntrospector());
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings result = baseSettings.withAppendedAnnotationIntrospector(ai);
        assertEquals(ai, result.getAnnotationIntrospector());
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        BaseSettings result = baseSettings.withVisibilityChecker(vc);
        assertEquals(vc, result.getVisibilityChecker());
    }

    @Test
    public void testWithVisibility() {
        BaseSettings result = baseSettings.withVisibility(null, null);
        assertNotNull(result);
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        BaseSettings result = baseSettings.withPropertyNamingStrategy(pns);
        assertEquals(pns, result.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory tf = new TypeFactory();
        BaseSettings result = baseSettings.withTypeFactory(tf);
        assertEquals(tf, result.getTypeFactory());
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        BaseSettings result = baseSettings.withTypeResolverBuilder(typer);
        assertEquals(typer, result.getTypeResolverBuilder());
    }

    @Test
    public void testWithDateFormat() {
        DateFormat df = new DateFormat();
        BaseSettings result = baseSettings.withDateFormat(df);
        assertEquals(df, result.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator hi = new HandlerInstantiator();
        BaseSettings result = baseSettings.withHandlerInstantiator(hi);
        assertEquals(hi, result.getHandlerInstantiator());
    }

    @Test
    public void testWith() {
        Locale l = new Locale();
        BaseSettings result = baseSettings.with(l);
        assertEquals(l, result.getLocale());
    }

    @Test
    public void testWithOneToManyTimezone() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        BaseSettings result = baseSettings.with(tz);
        assertEquals(tz, result.getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant base64 = new Base64Variant();
        BaseSettings result = baseSettings.with(base64);
        assertEquals(base64, result.getBase64Variant());
    }

}