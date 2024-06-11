package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ClassIntrospector;
import com.fasterxml.jackson.databind.HandlerInstantiator;
import com.fasterxml.jackson.databind.IntrospectionSafeBuilder;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {

    @Test
    public void testBaseSettingsWithDefaults() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<Object> vc = new VisibilityChecker.Std();
        PropertyNamingStrategy pns = PropertyNamingStrategy.STANDARD_PROPERTY_NAMING;
        TypeFactory tf = TypeFactory.defaultInstance();
        TypeResolverBuilder<?> typer = null;
        DateFormat df = StdDateFormat.DEFAULT;
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone tz = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.URL_SAFE_STANDARD;

        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, df, hi, locale, tz, defaultBase64);

        // check defaults
        assertEquals(ci, settings.getClassIntrospector());
        assertEquals(ai, settings.getAnnotationIntrospector());
        assertEquals(vc, settings.getVisibilityChecker());
        assertEquals(pns, settings.getPropertyNamingStrategy());
        assertEquals(tf, settings.getTypeFactory());
        assertEquals(typer, settings.getTypeResolverBuilder());
        assertEquals(df, settings.getDateFormat());
        assertEquals(hi, settings.getHandlerInstantiator());
        assertEquals(locale, settings.getLocale());
        assertEquals(tz, settings.getTimeZone());
        assertEquals(defaultBase64, settings.getBase64Variant());
    }

    @Test
    public void testBaseSettingsWithCustomIntrospectors() throws Exception {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<Object> vc = new VisibilityChecker.Std();
        PropertyNamingStrategy pns = PropertyNamingStrategy.CAMEL_CASE_TO_LOW_DOTS;
        TypeFactory tf = TypeFactory.defaultInstance();
        TypeResolverBuilder<?> typer = new SimpleTypeResolverBuilder();
        DateFormat df = new StdDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        HandlerInstantiator hi = new MyHandlerInstantiator();
        Locale locale = new Locale("en", "US");
        TimeZone tz = TimeZone.getTimeZone("UTC");
        Base64Variant defaultBase64 = Base64Variant.BASE64;

        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, df, hi, locale, tz, defaultBase64);

        // check custom values
        assertEquals(ci, settings.getClassIntrospector());
        assertEquals(ai, settings.getAnnotationIntrospector());
        assertEquals(vc, settings.getVisibilityChecker());
        assertEquals(pns, settings.getPropertyNamingStrategy());
        assertEquals(tf, settings.getTypeFactory());
        assertNotSame(typer, settings.getTypeResolverBuilder());
        assertEquals(df, settings.getDateFormat());
        assertEquals(hi, settings.getHandlerInstantiator());
        assertEquals(locale, settings.getLocale());
        assertEquals(tz, settings.getTimeZone());
        assertEquals(defaultBase64, settings.getBase64Variant());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBaseSettingsWithNullTimeZone() {
        ClassIntrospector ci = new ClassIntrospector();
        AnnotationIntrospector ai = new AnnotationIntrospector();
        VisibilityChecker<Object> vc = new VisibilityChecker.Std();
        PropertyNamingStrategy pns = PropertyNamingStrategy.STANDARD_PROPERTY_NAMING;
        TypeFactory tf = TypeFactory.defaultInstance();
        TypeResolverBuilder<?> typer = null;
        DateFormat df = new StdDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        HandlerInstantiator hi = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone tz = null;
        Base64Variant defaultBase64 = Base64Variant.URL_SAFE_STANDARD;

        new BaseSettings(ci, ai, vc, pns, tf, typer, df, hi, locale, tz, defaultBase64);
    }

    @Test
    public void testBaseSettingsSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValueAsString(new BaseSettings(null, null, null, null, null, null, null, null, null, null, null));
    }

    static class MyHandlerInstantiator extends HandlerInstantiator {
        @Override
        public Object instantiateValueHandler(SerializerProvider prov, Class<?> valueType) throws InstantiationException, IllegalAccessException {
            return new IntrospectionSafeCounter();
        }
    }

    static class SimpleTypeResolverBuilder implements TypeResolverBuilder {
        // empty implementation for testing
    }
}