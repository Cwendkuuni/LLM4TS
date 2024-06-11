package com.fasterxml.jackson.databind.cfg;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setUp() {
        ClassIntrospector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<?> visibilityChecker = new VisibilityChecker();
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy();
        TypeFactory typeFactory = new TypeFactory();
        TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilder();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        Base64Variant base64Variant = new Base64Variant();

        baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker,
                propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat,
                handlerInstantiator, locale, timeZone, base64Variant);
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector newClassIntrospector = new ClassIntrospector();
        BaseSettings newSettings = baseSettings.withClassIntrospector(newClassIntrospector);
        Assert.assertEquals(newClassIntrospector, newSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withAnnotationIntrospector(newAnnotationIntrospector);
        Assert.assertEquals(newAnnotationIntrospector, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withInsertedAnnotationIntrospector(newAnnotationIntrospector);
        Assert.assertEquals(newAnnotationIntrospector, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withAppendedAnnotationIntrospector(newAnnotationIntrospector);
        Assert.assertEquals(newAnnotationIntrospector, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker<?> newVisibilityChecker = new VisibilityChecker();
        BaseSettings newSettings = baseSettings.withVisibilityChecker(newVisibilityChecker);
        Assert.assertEquals(newVisibilityChecker, newSettings.getVisibilityChecker());
    }

    @Test
    public void testWithVisibility() {
        PropertyAccessor propertyAccessor = PropertyAccessor.METHOD;
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.ANY;
        BaseSettings newSettings = baseSettings.withVisibility(propertyAccessor, visibility);
        VisibilityChecker<?> visibilityChecker = newSettings.getVisibilityChecker();
        Assert.assertEquals(visibility, visibilityChecker.findFieldVisibility());
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy newPropertyNamingStrategy = new PropertyNamingStrategy();
        BaseSettings newSettings = baseSettings.withPropertyNamingStrategy(newPropertyNamingStrategy);
        Assert.assertEquals(newPropertyNamingStrategy, newSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory newTypeFactory = new TypeFactory();
        BaseSettings newSettings = baseSettings.withTypeFactory(newTypeFactory);
        Assert.assertEquals(newTypeFactory, newSettings.getTypeFactory());
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> newTypeResolverBuilder = new TypeResolverBuilder();
        BaseSettings newSettings = baseSettings.withTypeResolverBuilder(newTypeResolverBuilder);
        Assert.assertEquals(newTypeResolverBuilder, newSettings.getTypeResolverBuilder());
    }

    @Test
    public void testWithDateFormat() {
        DateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        BaseSettings newSettings = baseSettings.withDateFormat(newDateFormat);
        Assert.assertEquals(newDateFormat, newSettings.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator newHandlerInstantiator = new HandlerInstantiator();
        BaseSettings newSettings = baseSettings.withHandlerInstantiator(newHandlerInstantiator);
        Assert.assertEquals(newHandlerInstantiator, newSettings.getHandlerInstantiator());
    }

    @Test
    public void testWithLocale() {
        Locale newLocale = Locale.US;
        BaseSettings newSettings = baseSettings.with(newLocale);
        Assert.assertEquals(newLocale, newSettings.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        TimeZone newTimeZone = TimeZone.getTimeZone("GMT");
        BaseSettings newSettings = baseSettings.with(newTimeZone);
        Assert.assertEquals(newTimeZone, newSettings.getTimeZone());
        Assert.assertEquals(newTimeZone, newSettings.getDateFormat().getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant newBase64Variant = new Base64Variant();
        BaseSettings newSettings = baseSettings.with(newBase64Variant);
        Assert.assertEquals(newBase64Variant, newSettings.getBase64Variant());
    }
}