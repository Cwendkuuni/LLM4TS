package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

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
        DateFormat dateFormat = null;
        HandlerInstantiator hi = null;
        Locale locale = null;
        TimeZone tz = null;
        Base64Variant defaultBase64 = null;
        
        baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings withClassIntrospector = baseSettings.withClassIntrospector(ci);
        assertEquals(ci, withClassIntrospector.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings withAnnotationIntrospector = baseSettings.withAnnotationIntrospector(ai);
        assertEquals(ai, withAnnotationIntrospector.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings withInsertedAnnotationIntrospector = baseSettings.withInsertedAnnotationIntrospector(ai);
        assertEquals(ai, withInsertedAnnotationIntrospector.getAnnotationIntrospector());
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings withAppendedAnnotationIntrospector = baseSettings.withAppendedAnnotationIntrospector(ai);
        assertEquals(ai, withAppendedAnnotationIntrospector.getAnnotationIntrospector());
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        BaseSettings withVisibilityChecker = baseSettings.withVisibilityChecker(vc);
        assertEquals(vc, withVisibilityChecker.getVisibilityChecker());
    }

    @Test
    public void testWithVisibility() {
        PropertyAccessor forMethod = PropertyAccessor.GETTER;
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.PUBLIC_ONLY;
        BaseSettings withVisibility = baseSettings.withVisibility(forMethod, visibility);
        VisibilityChecker<?> vc = withVisibility.getVisibilityChecker();
        assertEquals(visibility, vc.findGetterVisibility(Object.class, "property"));
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        BaseSettings withPropertyNamingStrategy = baseSettings.withPropertyNamingStrategy(pns);
        assertEquals(pns, withPropertyNamingStrategy.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory tf = new TypeFactory();
        BaseSettings withTypeFactory = baseSettings.withTypeFactory(tf);
        assertEquals(tf, withTypeFactory.getTypeFactory());
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        BaseSettings withTypeResolverBuilder = baseSettings.withTypeResolverBuilder(typer);
        assertEquals(typer, withTypeResolverBuilder.getTypeResolverBuilder());
    }

    @Test
    public void testWithDateFormat() {
        DateFormat df = new StdDateFormat();
        BaseSettings withDateFormat = baseSettings.withDateFormat(df);
        assertEquals(df, withDateFormat.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator hi = new HandlerInstantiator();
        BaseSettings withHandlerInstantiator = baseSettings.withHandlerInstantiator(hi);
        assertEquals(hi, withHandlerInstantiator.getHandlerInstantiator());
    }

    @Test
    public void testWith() {
        Locale l = new Locale("en");
        BaseSettings with = baseSettings.with(l);
        assertEquals(l, with.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        BaseSettings withTimeZone = baseSettings.with(tz);
        assertEquals(tz, withTimeZone.getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant base64 = new Base64Variant();
        BaseSettings withBase64 = baseSettings.with(base64);
        assertEquals(base64, withBase64.getBase64Variant());
    }

    @Test
    public void testGetClassIntrospector() {
        assertNull(baseSettings.getClassIntrospector());
    }
    
    @Test
    public void testGetAnnotationIntrospector() {
        assertNull(baseSettings.getAnnotationIntrospector());
    }

    @Test
    public void testGetVisibilityChecker() {
        assertNull(baseSettings.getVisibilityChecker());
    }

    @Test
    public void testGetPropertyNamingStrategy() {
        assertNull(baseSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testGetTypeFactory() {
        assertNull(baseSettings.getTypeFactory());
    }

    @Test
    public void testGetTypeResolverBuilder() {
        assertNull(baseSettings.getTypeResolverBuilder());
    }
    
    @Test
    public void testGetDateFormat() {
        assertNull(baseSettings.getDateFormat());
    }

    @Test
    public void testGetHandlerInstantiator() {
        assertNull(baseSettings.getHandlerInstantiator());
    }

    @Test
    public void testGetLocale() {
        assertNull(baseSettings.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        assertNull(baseSettings.getTimeZone());
    }

    @Test
    public void testGetBase64Variant() {
        assertNull(baseSettings.getBase64Variant());
    }
}