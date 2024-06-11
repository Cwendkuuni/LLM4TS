package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.util.HandlerInstantiator;
import com.fasterxml.jackson.databind.util.LocaleName;
import com.fasterxml.jackson.databind.util.TimeZoneOffset;

import org.junit.Test;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class BaseSettingsTest {

    @Test
    public void testGetClassIntrospector() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        ClassIntrospector actual = settings.getClassIntrospector();
        assertNotNull(actual);
    }

    @Test
    public void testGetAnnotationIntrospector() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        AnnotationIntrospector actual = settings.getAnnotationIntrospector();
        assertNotNull(actual);
    }

    @Test
    public void testGetVisibilityChecker() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        VisibilityChecker<?> actual = settings.getVisibilityChecker();
        assertNotNull(actual);
    }

    @Test
    public void testGetPropertyNamingStrategy() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        PropertyNamingStrategy actual = settings.getPropertyNamingStrategy();
        assertNotNull(actual);
    }

    @Test
    public void testGetTypeFactory() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        TypeFactory actual = settings.getTypeFactory();
        assertNotNull(actual);
    }

    @Test
    public void testGetTypeResolverBuilder() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        TypeResolverBuilder<?> actual = settings.getTypeResolverBuilder();
        assertNotNull(actual);
    }

    @Test
    public void testGetDateFormat() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        DateFormat actual = settings.getDateFormat();
        assertNull(actual);
    }

    @Test
    public void testGetHandlerInstantiator() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        HandlerInstantiator actual = settings.getHandlerInstantiator();
        assertNotNull(actual);
    }

    @Test
    public void testGetLocale() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        Locale actual = settings.getLocale();
        assertEquals(Locale.getDefault(), actual);
    }

    @Test
    public void testGetTimeZone() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        TimeZone actual = settings.getTimeZone();
        assertEquals(TimeZone.getDefault(), actual);
    }

    @Test
    public void testGetBase64Variant() {
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        Base64Variant actual = settings.getBase64Variant();
        assertEquals(Base64Variant.STANDARD, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullClassIntrospector() {
        new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullAnnotationIntrospector() {
        new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullVisibilityChecker() {
        new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullPropertyNamingStrategy() {
        new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullTypeFactory() {
        new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullTypeResolverBuilder() {
        new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullDateFormat() {
        new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullHandlerInstantiator() {
        new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullLocale() {
        new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullTimeZone() {
        new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector classIntrospector = mock(ClassIntrospector.class);
        BaseSettings settings = new BaseSettings(classIntrospector, null, null, null, null, null, null, null, null, null, null);
        assertEquals(classIntrospector, settings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
        BaseSettings settings = new BaseSettings(null, annotationIntrospector, null, null, null, null, null, null, null, null, null);
        assertEquals(annotationIntrospector, settings.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings settingsWithAnnotationIntrospector = settings.withInsertedAnnotationIntrospector(annotationIntrospector);
        assertEquals(annotationIntrospector, settingsWithAnnotationIntrospector.getAnnotationIntrospector());
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector annotationIntrospector = mock(AnnotationIntrospector.class);
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings settingsWithAnnotationIntrospector = settings.withAppendedAnnotationIntrospector(annotationIntrospector);
        AnnotationIntrospector previousAnnotationIntrospector = settings.getAnnotationIntrospector();
        assertEquals(previousAnnotationIntrospector, settingsWithAnnotationIntrospector.getAnnotationIntrospector());
        assertEquals(annotationIntrospector, settingsWithAnnotationIntrospector.getAnnotationIntrospector().getAnnotationIntrospector());
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker<Object> visibilityChecker = mock(VisibilityChecker.class);
        BaseSettings settings = new BaseSettings(null, null, visibilityChecker, null, null, null, null, null, null, null, null);
        assertEquals(visibilityChecker, settings.getVisibilityChecker());
    }

    @Test
    public void testWithVisibility() {
        PropertyAccessor propertyAccessor = mock(PropertyAccessor.class);
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.ANY;
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings settingsWithVisibility = settings.withVisibility(propertyAccessor, visibility);
        assertEquals(visibility, settingsWithVisibility.getVisibilityChecker().forProperty(propertyAccessor).withGeneralVisibility(visibility));
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy propertyNamingStrategy = mock(PropertyNamingStrategy.class);
        BaseSettings settings = new BaseSettings(null, null, null, propertyNamingStrategy, null, null, null, null, null, null, null);
        assertEquals(propertyNamingStrategy, settings.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory typeFactory = mock(TypeFactory.class);
        BaseSettings settings = new BaseSettings(null, null, null, null, typeFactory, null, null, null, null, null, null);
        assertEquals(typeFactory, settings.getTypeFactory());
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typeResolverBuilder = mock(TypeResolverBuilder.class);
        BaseSettings settings = new BaseSettings(null, null, null, null, null, typeResolverBuilder, null, null, null, null, null);
        assertEquals(typeResolverBuilder, settings.getTypeResolverBuilder());
    }

    @Test
    public void testWithDateFormat() {
        DateFormat dateFormat = new StdDateFormat();
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, dateFormat, null, null, null, null);
        assertEquals(dateFormat, settings.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator handlerInstantiator = mock(HandlerInstantiator.class);
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, handlerInstantiator, null, null, null);
        assertEquals(handlerInstantiator, settings.getHandlerInstantiator());
    }

    @Test
    public void testWithLocale() {
        Locale locale = LocaleName.FRENCH;
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings settingsWithLocale = settings.with(locale);
        assertEquals(locale, settingsWithLocale.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings settingsWithTimeZone = settings.with(timeZone);
        assertEquals(timeZone, settingsWithTimeZone.getTimeZone());
        DateFormat dateFormat = settingsWithTimeZone.getDateFormat();
        assertNotNull(dateFormat);
        assertEquals(timeZone, dateFormat.getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant base64Variant = Base64Variant.BASE64_URL_SAFE;
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings settingsWithBase64Variant = settings.with(base64Variant);
        assertEquals(base64Variant, settingsWithBase64Variant.getBase64Variant());
    }
}