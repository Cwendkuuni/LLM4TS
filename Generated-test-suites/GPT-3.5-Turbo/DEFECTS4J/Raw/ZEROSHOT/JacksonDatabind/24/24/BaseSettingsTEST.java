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
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {

    private BaseSettings settings;
    private ClassIntrospector ci;
    private AnnotationIntrospector ai;
    private VisibilityChecker<?> vc;
    private PropertyNamingStrategy pns;
    private TypeFactory tf;
    private TypeResolverBuilder<?> typer;
    private DateFormat dateFormat;
    private HandlerInstantiator hi;
    private Locale locale;
    private TimeZone tz;
    private Base64Variant defaultBase64;

    @Before
    public void setUp() {
        ci = new ClassIntrospector();
        ai = new AnnotationIntrospector();
        vc = new VisibilityChecker();
        pns = new PropertyNamingStrategy();
        tf = new TypeFactory();
        typer = new TypeResolverBuilder();
        dateFormat = new DateFormat();
        hi = new HandlerInstantiator();
        locale = new Locale();
        tz = new TimeZone();
        defaultBase64 = new Base64Variant();
        settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector newCi = new ClassIntrospector();
        BaseSettings newSettings = settings.withClassIntrospector(newCi);
        assertEquals(newCi, newSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector newAi = new AnnotationIntrospector();
        BaseSettings newSettings = settings.withAnnotationIntrospector(newAi);
        assertEquals(newAi, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector newAi = new AnnotationIntrospector();
        BaseSettings newSettings = settings.withInsertedAnnotationIntrospector(newAi);
        assertEquals(newAi, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector newAi = new AnnotationIntrospector();
        BaseSettings newSettings = settings.withAppendedAnnotationIntrospector(newAi);
        assertEquals(newAi, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker<?> newVc = new VisibilityChecker();
        BaseSettings newSettings = settings.withVisibilityChecker(newVc);
        assertEquals(newVc, newSettings.getVisibilityChecker());
    }

    @Test
    public void testWithVisibility() {
        PropertyAccessor forMethod = PropertyAccessor.FIELD;
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.ANY;
        BaseSettings newSettings = settings.withVisibility(forMethod, visibility);
        assertEquals(visibility, newSettings.getVisibilityChecker().findFieldVisibility(forMethod));
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy newPns = new PropertyNamingStrategy();
        BaseSettings newSettings = settings.withPropertyNamingStrategy(newPns);
        assertEquals(newPns, newSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory newTf = new TypeFactory();
        BaseSettings newSettings = settings.withTypeFactory(newTf);
        assertEquals(newTf, newSettings.getTypeFactory());
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> newTyper = new TypeResolverBuilder();
        BaseSettings newSettings = settings.withTypeResolverBuilder(newTyper);
        assertEquals(newTyper, newSettings.getTypeResolverBuilder());
    }

    @Test
    public void testWithDateFormat() {
        DateFormat newDf = new DateFormat();
        BaseSettings newSettings = settings.withDateFormat(newDf);
        assertEquals(newDf, newSettings.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator newHi = new HandlerInstantiator();
        BaseSettings newSettings = settings.withHandlerInstantiator(newHi);
        assertEquals(newHi, newSettings.getHandlerInstantiator());
    }

    @Test
    public void testWithLocale() {
        Locale newLocale = new Locale();
        BaseSettings newSettings = settings.with(newLocale);
        assertEquals(newLocale, newSettings.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        TimeZone newTz = new TimeZone();
        BaseSettings newSettings = settings.with(newTz);
        assertEquals(newTz, newSettings.getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant newBase64 = new Base64Variant();
        BaseSettings newSettings = settings.with(newBase64);
        assertEquals(newBase64, newSettings.getBase64Variant());
    }

    // Other tests for getter methods
    // ...
}