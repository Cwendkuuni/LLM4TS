package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.property.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setUp() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<?> vc = new VisibilityChecker.Std();
        PropertyNamingStrategy pns = new PropertyNamingStrategy.Default();
        TypeFactory tf = new TypeFactory();
        TypeResolverBuilder<?> typer = new TypeResolverBuilderDefault();
        DateFormat dateFormat = StdDateFormat.DEFAULT;
        HandlerInstantiator hi = new HandlerInstantiator();
        baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, Locale.US, TimeZone.getDefault(), Base64Variant.STANDARD);
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
        assertSame(Locale.US, baseSettings.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        assertNotNull(baseSettings.getTimeZone());
    }

    @Test
    public void testGetBase64Variant() {
        assertSame(Base64Variant.STANDARD, baseSettings.getBase64Variant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullTimeZone() {
        baseSettings.with(null);
    }

    @Test
    public void testWithCustomDateFormat() {
        DateFormat customDateFormat = new StdDateFormat();
        BaseSettings withCustomDateFormat = baseSettings.withDateFormat(customDateFormat);
        assertSame(customDateFormat, withCustomDateFormat.getDateFormat());
    }

    @Test
    public void testWithCustomHandlerInstantiator() {
        HandlerInstantiator customHandlerInstantiator = new HandlerInstantiator() {
            @Override
            public <T> T instantiateValueHandler(Class<T> handlerType) throws InstantiationException, IllegalAccessException {
                return handlerType.newInstance();
            }
        };
        BaseSettings withCustomHandlerInstantiator = baseSettings.withHandlerInstantiator(customHandlerInstantiator);
        assertSame(customHandlerInstantiator, withCustomHandlerInstantiator.getHandlerInstantiator());
    }

    @Test
    public void testWithCustomLocale() {
        Locale customLocale = new Locale("en", "GB");
        BaseSettings withCustomLocale = baseSettings.with(customLocale);
        assertSame(customLocale, withCustomLocale.getLocale());
    }

    @Test
    public void testWithCustomTimeZone() {
        TimeZone customTimeZone = TimeZone.getTimeZone("UTC");
        BaseSettings withCustomTimeZone = baseSettings.with(customTimeZone);
        assertSame(customTimeZone, withCustomTimeZone.getTimeZone());
    }

    @Test
    public void testWithCustomBase64Variant() {
        Base64Variant customBase64 = Base64Variant.BASE64_EXTENDED;
        BaseSettings withCustomBase64 = baseSettings.with(customBase64);
        assertSame(customBase64, withCustomBase64.getBase64Variant());
    }
}