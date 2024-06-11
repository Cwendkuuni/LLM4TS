package com.fasterxml.jackson.databind.cfg;

import java.text.DateFormat;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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

import org.junit.*;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    // Create instance of BaseSettings for testing
    BaseSettings baseSettings;

    @Before
    public void init() {
        ClassIntrospector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<?> visibilityChecker = new VisibilityChecker<>();
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy();
        TypeFactory typeFactory = new TypeFactory();
        TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilder<>();
        DateFormat dateFormat = new DateFormat();
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant base64Variant = new Base64Variant();

        baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, base64Variant);
    }

    @Test
    public void testGetClassIntrospector() {
        ClassIntrospector classIntrospector = baseSettings.getClassIntrospector();
        // Perform assertion
        assertNotNull(classIntrospector);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = baseSettings.getAnnotationIntrospector();
        // Perform assertion
        assertNotNull(annotationIntrospector);
    }

    @Test
    public void testGetVisibilityChecker() {
        VisibilityChecker<?> visibilityChecker = baseSettings.getVisibilityChecker();
        // Perform assertion
        assertNotNull(visibilityChecker);
    }

    @Test
    public void testGetPropertyNamingStrategy() {
        PropertyNamingStrategy propertyNamingStrategy = baseSettings.getPropertyNamingStrategy();
        // Perform assertion
        assertNotNull(propertyNamingStrategy);
    }

    @Test
    public void testGetTypeFactory() {
        TypeFactory typeFactory = baseSettings.getTypeFactory();
        // Perform assertion
        assertNotNull(typeFactory);
    }

    @Test
    public void testGetTypeResolverBuilder() {
        TypeResolverBuilder<?> typeResolverBuilder = baseSettings.getTypeResolverBuilder();
        // Perform assertion
        assertNotNull(typeResolverBuilder);
    }

    @Test
    public void testGetDateFormat() {
        DateFormat dateFormat = baseSettings.getDateFormat();
        // Perform assertion
        assertNotNull(dateFormat);
    }

    @Test
    public void testGetHandlerInstantiator() {
        HandlerInstantiator handlerInstantiator = baseSettings.getHandlerInstantiator();
        // Perform assertion
        assertNotNull(handlerInstantiator);
    }

    @Test
    public void testGetLocale() {
        Locale locale = baseSettings.getLocale();
        // Perform assertion
        assertNotNull(locale);
    }

    @Test
    public void testGetTimeZone() {
        TimeZone timeZone = baseSettings.getTimeZone();
        // Perform assertion
        assertNotNull(timeZone);
    }

    @Test
    public void testGetBase64Variant() {
        Base64Variant base64Variant = baseSettings.getBase64Variant();
        // Perform assertion
        assertNotNull(base64Variant);
    }
}