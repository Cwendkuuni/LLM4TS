package com.fasterxml.jackson.databind.cfg;

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
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setUp() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker();
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
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
        VisibilityChecker<?> vc = new VisibilityChecker();

        BaseSettings result = baseSettings.withVisibilityChecker(vc);

        assertEquals(vc, result.getVisibilityChecker());
    }

    @Test
    public void testWithVisibility() {
        PropertyAccessor forMethod = PropertyAccessor.FIELD;
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.ANY;

        BaseSettings result = baseSettings.withVisibility(forMethod, visibility);

        assertEquals(visibility, result.getVisibilityChecker().findFieldVisibility(forMethod));
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
    public void testWithTimeZone() {
        TimeZone tz = TimeZone.getDefault();

        BaseSettings result = baseSettings.with(tz);

        assertEquals(tz, result.getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant base64 = new Base64Variant();

        BaseSettings result = baseSettings.with(base64);

        assertEquals(base64, result.getBase64Variant());
    }
    
    @Test
    public void testGetClassIntrospector() {
        ClassIntrospector result = baseSettings.getClassIntrospector();

        assertNotNull(result);
    }
    
    @Test
    public void testGetAnnotationIntrospector() {
        AnnotationIntrospector result = baseSettings.getAnnotationIntrospector();

        assertNotNull(result);
    }

    @Test
    public void testGetVisibilityChecker() {
        VisibilityChecker<?> result = baseSettings.getVisibilityChecker();

        assertNotNull(result);
    }

    @Test
    public void testGetPropertyNamingStrategy() {
        PropertyNamingStrategy result = baseSettings.getPropertyNamingStrategy();

        assertNotNull(result);
    }

    @Test
    public void testGetTypeFactory() {
        TypeFactory result = baseSettings.getTypeFactory();

        assertNotNull(result);
    }

    @Test
    public void testGetTypeResolverBuilder() {
        TypeResolverBuilder<?> result = baseSettings.getTypeResolverBuilder();

        assertNotNull(result);
    }
    
    @Test
    public void testGetDateFormat() {
        DateFormat result = baseSettings.getDateFormat();

        assertNotNull(result);
    }

    @Test
    public void testGetHandlerInstantiator() {
        HandlerInstantiator result = baseSettings.getHandlerInstantiator();

        assertNotNull(result);
    }

    @Test
    public void testGetLocale() {
        Locale result = baseSettings.getLocale();

        assertNotNull(result);
    }

    @Test
    public void testGetTimeZone() {
        TimeZone result = baseSettings.getTimeZone();

        assertNotNull(result);
    }

    @Test
    public void testGetBase64Variant() {
        Base64Variant result = baseSettings.getBase64Variant();

        assertNotNull(result);
    }
}