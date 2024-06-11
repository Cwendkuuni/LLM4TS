package com.fasterxml.jackson.databind.cfg;

import com.fasterxml.jackson.core.base.TestUtils;
import com.fasterxml.jackson.databind.cfg.BaseSettings;
import org.junit.Assert;
import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testConstructor() {
        ClassIntrospector ci = TestUtils.getSimpleClassIntrospector();
        AnnotationIntrospector ai = TestUtils.getSimpleAnnotationIntrospector();
        VisibilityChecker<?> vc = TestUtils.getSimpleVisibilityChecker();
        PropertyNamingStrategy pns = TestUtils.getSimplePropertyNamingStrategy();
        TypeFactory tf = TestUtils.getSimpleTypeFactory();
        TypeResolverBuilder<?> typer = TestUtils.getSimpleTypeResolverBuilder();
        DateFormat dateFormat = TestUtils.getSimpleDateFormat();
        HandlerInstantiator hi = TestUtils.getSimpleHandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = null;

        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);

        Assert.assertNotNull(settings.getClassIntrospector());
        Assert.assertNotNull(settings.getAnnotationIntrospector());
        Assert.assertNotNull(settings.getVisibilityChecker());
        Assert.assertNotNull(settings.getPropertyNamingStrategy());
        Assert.assertNotNull(settings.getTypeFactory());
        Assert.assertNotNull(settings.getTypeResolverBuilder());
        Assert.assertNotNull(settings.getDateFormat());
        Assert.assertNotNull(settings.getHandlerInstantiator());
        Assert.assertNotNull(settings.getLocale());
        Assert.assertNotNull(settings.getTimeZone());
        Assert.assertNull(settings.getBase64Variant());
    }

    @Test
    public void testWithClassIntrospector() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        ClassIntrospector ci = TestUtils.getSimpleClassIntrospector();

        BaseSettings newSettings = settings.withClassIntrospector(ci);

        Assert.assertNotSame(settings, newSettings);
        Assert.assertSame(ci, newSettings.getClassIntrospector());
        Assert.assertSame(settings.getAnnotationIntrospector(), newSettings.getAnnotationIntrospector());
        Assert.assertSame(settings.getVisibilityChecker(), newSettings.getVisibilityChecker());
        Assert.assertSame(settings.getPropertyNamingStrategy(), newSettings.getPropertyNamingStrategy());
        Assert.assertSame(settings.getTypeFactory(), newSettings.getTypeFactory());
        Assert.assertSame(settings.getTypeResolverBuilder(), newSettings.getTypeResolverBuilder());
        Assert.assertSame(settings.getDateFormat(), newSettings.getDateFormat());
        Assert.assertSame(settings.getHandlerInstantiator(), newSettings.getHandlerInstantiator());
        Assert.assertSame(settings.getLocale(), newSettings.getLocale());
        Assert.assertSame(settings.getTimeZone(), newSettings.getTimeZone());
        Assert.assertSame(settings.getBase64Variant(), newSettings.getBase64Variant());
    }

    // Add test cases for each setter method with appropriate input and expected output

    @Test
    public void testGetters() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);

        ClassIntrospector ci = TestUtils.getSimpleClassIntrospector();
        AnnotationIntrospector ai = TestUtils.getSimpleAnnotationIntrospector();
        VisibilityChecker<?> vc = TestUtils.getSimpleVisibilityChecker();
        PropertyNamingStrategy pns = TestUtils.getSimplePropertyNamingStrategy();
        TypeFactory tf = TestUtils.getSimpleTypeFactory();
        TypeResolverBuilder<?> typer = TestUtils.getSimpleTypeResolverBuilder();
        DateFormat dateFormat = TestUtils.getSimpleDateFormat();
        HandlerInstantiator hi = TestUtils.getSimpleHandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = null;

        settings = settings.withClassIntrospector(ci)
                .withAnnotationIntrospector(ai)
                .withVisibilityChecker(vc)
                .withPropertyNamingStrategy(pns)
                .withTypeFactory(tf)
                .withTypeResolverBuilder(typer)
                .withDateFormat(dateFormat)
                .withHandlerInstantiator(hi)
                .with(locale)
                .with(timeZone)
                .with(defaultBase64);

        Assert.assertSame(ci, settings.getClassIntrospector());
        Assert.assertSame(ai, settings.getAnnotationIntrospector());
        Assert.assertSame(vc, settings.getVisibilityChecker());
        Assert.assertSame(pns, settings.getPropertyNamingStrategy());
        Assert.assertSame(tf, settings.getTypeFactory());
        Assert.assertSame(typer, settings.getTypeResolverBuilder());
        Assert.assertSame(dateFormat, settings.getDateFormat());
        Assert.assertSame(hi, settings.getHandlerInstantiator());
        Assert.assertSame(locale, settings.getLocale());
        Assert.assertSame(timeZone, settings.getTimeZone());
        Assert.assertSame(defaultBase64, settings.getBase64Variant());
    }

    // Add test cases for edge cases and exceptions

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullLocale() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        settings.with(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullTimeZone() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        settings.with(null);
    }

    // Add more test cases for other edge cases and exception handling scenarios

}