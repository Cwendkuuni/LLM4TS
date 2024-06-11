package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class BaseSettingsTest {

    private BaseSettings baseSettings;
    private ClassIntrospector classIntrospector;
    private AnnotationIntrospector annotationIntrospector;
    private VisibilityChecker<?> visibilityChecker;
    private PropertyNamingStrategy propertyNamingStrategy;
    private TypeFactory typeFactory;
    private TypeResolverBuilder<?> typeResolverBuilder;
    private DateFormat dateFormat;
    private HandlerInstantiator handlerInstantiator;
    private Locale locale;
    private TimeZone timeZone;
    private Base64Variant base64Variant;

    @Before
    public void setup() {
        classIntrospector = new ClassIntrospector();
        annotationIntrospector = new AnnotationIntrospector();
        visibilityChecker = new VisibilityChecker();
        propertyNamingStrategy = new PropertyNamingStrategy();
        typeFactory = new TypeFactory();
        typeResolverBuilder = new TypeResolverBuilder();
        dateFormat = DateFormat.getInstance();
        handlerInstantiator = new HandlerInstantiator();
        locale = Locale.US;
        timeZone = TimeZone.getDefault();
        base64Variant = Base64Variant.getDefaultVariant();
        baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker,
                propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat, handlerInstantiator,
                locale, timeZone, base64Variant);
    }

    @Test
    public void testGetClassIntrospector() {
        assertTrue(baseSettings.getClassIntrospector() instanceof ClassIntrospector);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        assertTrue(baseSettings.getAnnotationIntrospector() instanceof AnnotationIntrospector);
    }

    @Test
    public void testGetVisibilityChecker() {
        assertTrue(baseSettings.getVisibilityChecker() instanceof VisibilityChecker);
    }

    @Test
    public void testGetPropertyNamingStrategy() {
        assertTrue(baseSettings.getPropertyNamingStrategy() instanceof PropertyNamingStrategy);
    }

    @Test
    public void testGetTypeFactory() {
        assertTrue(baseSettings.getTypeFactory() instanceof TypeFactory);
    }

    @Test
    public void testGetTypeResolverBuilder() {
        assertTrue(baseSettings.getTypeResolverBuilder() instanceof TypeResolverBuilder);
    }

    @Test
    public void testGetDateFormat() {
        assertTrue(baseSettings.getDateFormat() instanceof DateFormat);
    }

    @Test
    public void testGetHandlerInstantiator() {
        assertTrue(baseSettings.getHandlerInstantiator() instanceof HandlerInstantiator);
    }

    @Test
    public void testGetLocale() {
        assertEquals(Locale.US, baseSettings.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        assertEquals(TimeZone.getDefault(), baseSettings.getTimeZone());
    }

    @Test
    public void testGetBase64Variant() {
        assertEquals(Base64Variant.getDefaultVariant(), baseSettings.getBase64Variant());
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector newClassIntrospector = new ClassIntrospector();
        BaseSettings newSettings = baseSettings.withClassIntrospector(newClassIntrospector);
        assertEquals(newClassIntrospector, newSettings.getClassIntrospector());
    }
    
    // Add more test cases to test all the methods in the BaseSettings class
    
}