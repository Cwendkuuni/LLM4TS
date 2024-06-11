package com.fasterxml.jackson.databind.cfg;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {
    @Test
    public void testGetClassIntrospector() {
        // Setup
        ClassIntrospector ci = new ClassIntrospector();

        BaseSettings baseSettings = new BaseSettings(ci, null, null, null, null, null, null, null, null, null, null);

        // Verify
        Assert.assertEquals(ci, baseSettings.getClassIntrospector());
    }

    @Test
    public void testGetAnnotationIntrospector() {
        // Setup
        AnnotationIntrospectorPair ai = new AnnotationIntrospectorPair();

        BaseSettings baseSettings = new BaseSettings(null, ai, null, null, null, null, null, null, null, null, null);

        // Verify
        Assert.assertEquals(ai, baseSettings.getAnnotationIntrospector());
    }

    @Test
    public void testGetVisibilityChecker() {
        // Setup
        VisibilityChecker<?> vc = new VisibilityChecker<>();

        BaseSettings baseSettings = new BaseSettings(null, null, vc, null, null, null, null, null, null, null, null);

        // Verify
        Assert.assertEquals(vc, baseSettings.getVisibilityChecker());
    }

    @Test
    public void testGetPropertyNamingStrategy() {
        // Setup
        PropertyNamingStrategy pns = new PropertyNamingStrategy();

        BaseSettings baseSettings = new BaseSettings(null, null, null, pns, null, null, null, null, null, null, null);

        // Verify
        Assert.assertEquals(pns, baseSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testGetTypeFactory() {
        // Setup
        TypeFactory tf = new TypeFactory();

        BaseSettings baseSettings = new BaseSettings(null, null, null, null, tf, null, null, null, null, null, null);

        // Verify
        Assert.assertEquals(tf, baseSettings.getTypeFactory());
    }

    @Test
    public void testGetTypeResolverBuilder() {
        // Setup
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();

        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, typer, null, null, null, null, null);

        // Verify
        Assert.assertEquals(typer, baseSettings.getTypeResolverBuilder());
    }

    @Test
    public void testGetDateFormat() {
        // Setup
        DateFormat df = new StdDateFormat();

        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, df, null, null, null, null);

        // Verify
        Assert.assertEquals(df, baseSettings.getDateFormat());
    }

    @Test
    public void testGetHandlerInstantiator() {
        // Setup
        HandlerInstantiator hi = new HandlerInstantiator();

        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, hi, null, null, null);

        // Verify
        Assert.assertEquals(hi, baseSettings.getHandlerInstantiator());
    }

    @Test
    public void testGetLocale() {
        // Setup
        Locale l = Locale.US;

        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, l, null, null);

        // Verify
        Assert.assertEquals(l, baseSettings.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        // Setup
        TimeZone tz = TimeZone.getTimeZone("UTC");

        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, tz, null);

        // Verify
        Assert.assertEquals(tz, baseSettings.getTimeZone());
    }

    @Test
    public void testGetBase64Variant() {
        // Setup
        Base64Variant base64 = new Base64Variant();

        BaseSettings baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, base64);

        // Verify
        Assert.assertEquals(base64, baseSettings.getBase64Variant());
    }
}