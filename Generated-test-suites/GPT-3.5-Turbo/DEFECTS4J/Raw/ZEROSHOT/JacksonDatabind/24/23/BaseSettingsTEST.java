package com.fasterxml.jackson.databind.cfg;

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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setUp() {
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings newSettings = baseSettings.withClassIntrospector(ci);
        Assert.assertEquals(ci, newSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withAnnotationIntrospector(ai);
        Assert.assertEquals(ai, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withInsertedAnnotationIntrospector(ai);
        Assert.assertEquals(ai, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withAppendedAnnotationIntrospector(ai);
        Assert.assertEquals(ai, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        BaseSettings newSettings = baseSettings.withVisibilityChecker(vc);
        Assert.assertEquals(vc, newSettings.getVisibilityChecker());
    }

    @Test
    public void testWithVisibility() {
        PropertyAccessor forMethod = PropertyAccessor.GETTER;
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.DEFAULT;
        BaseSettings newSettings = baseSettings.withVisibility(forMethod, visibility);
        Assert.assertEquals(JsonAutoDetect.Visibility.DEFAULT, newSettings.getVisibilityChecker().findGetterVisibility());
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        BaseSettings newSettings = baseSettings.withPropertyNamingStrategy(pns);
        Assert.assertEquals(pns, newSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory tf = new TypeFactory();
        BaseSettings newSettings = baseSettings.withTypeFactory(tf);
        Assert.assertEquals(tf, newSettings.getTypeFactory());
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        BaseSettings newSettings = baseSettings.withTypeResolverBuilder(typer);
        Assert.assertEquals(typer, newSettings.getTypeResolverBuilder());
    }

    @Test
    public void testWithDateFormat() {
        DateFormat df = new StdDateFormat();
        BaseSettings newSettings = baseSettings.withDateFormat(df);
        Assert.assertEquals(df, newSettings.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator hi = new HandlerInstantiator();
        BaseSettings newSettings = baseSettings.withHandlerInstantiator(hi);
        Assert.assertEquals(hi, newSettings.getHandlerInstantiator());
    }

    @Test
    public void testWith() {
        Locale l = new Locale("en");
        BaseSettings newSettings = baseSettings.with(l);
        Assert.assertEquals(l, newSettings.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        BaseSettings newSettings = baseSettings.with(tz);
        Assert.assertEquals(tz, newSettings.getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant base64 = new Base64Variant();
        BaseSettings newSettings = baseSettings.with(base64);
        Assert.assertEquals(base64, newSettings.getBase64Variant());
    }
}