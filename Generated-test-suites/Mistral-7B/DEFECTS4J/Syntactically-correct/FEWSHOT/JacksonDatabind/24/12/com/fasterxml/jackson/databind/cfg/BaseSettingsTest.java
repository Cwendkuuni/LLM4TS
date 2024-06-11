package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testBaseSettings() {
        // Test default constructor
        BaseSettings settings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null, null);
        assertNull(settings.getClassIntrospector());
        assertNull(settings.getAnnotationIntrospector());
        assertNull(settings.getVisibilityChecker());
        assertNull(settings.getPropertyNamingStrategy());
        assertNull(settings.getTypeFactory());
        assertNull(settings.getTypeResolverBuilder());
        assertNull(settings.getDateFormat());
        assertNull(settings.getHandlerInstantiator());
        assertNull(settings.getLocale());
        assertNull(settings.getTimeZone());
        assertNull(settings.getBase64Variant());

        // Test constructor with all arguments
        ClassIntrospector ci = null;
        AnnotationIntrospector ai = null;
        VisibilityChecker<?> vc = null;
        PropertyNamingStrategy pns = null;
        TypeFactory tf = null;
        TypeResolverBuilder<?> typer = null;
        DateFormat dateFormat = null;
        HandlerInstantiator hi = null;
        Locale locale = null;
        TimeZone tz = null;
        Base64Variant defaultBase64 = null;
        BaseSettings settings1 = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
        assertEquals(ci, settings1.getClassIntrospector());
        assertEquals(ai, settings1.getAnnotationIntrospector());
        assertEquals(vc, settings1.getVisibilityChecker());
        assertEquals(pns, settings1.getPropertyNamingStrategy());
        assertEquals(tf, settings1.getTypeFactory());
        assertEquals(typer, settings1.getTypeResolverBuilder());
        assertEquals(dateFormat, settings1.getDateFormat());
        assertEquals(hi, settings1.getHandlerInstantiator());
        assertEquals(locale, settings1.getLocale());
        assertEquals(tz, settings1.getTimeZone());
        assertEquals(defaultBase64, settings1.getBase64Variant());

        // Test factory methods
        BaseSettings settings2 = settings1.withClassIntrospector(new ClassIntrospector());
        assertNotSame(settings1, settings2);
        assertEquals(new ClassIntrospector(), settings2.getClassIntrospector());

        settings2 = settings1.withAnnotationIntrospector(new AnnotationIntrospector());
        assertNotSame(settings1, settings2);
        assertEquals(new AnnotationIntrospector(), settings2.getAnnotationIntrospector());

        settings2 = settings1.withInsertedAnnotationIntrospector(new AnnotationIntrospector());
        assertNotSame(settings1, settings2);
        assertEquals(new AnnotationIntrospectorPair(new AnnotationIntrospector(), settings1.getAnnotationIntrospector()), settings2.getAnnotationIntrospector());

        settings2 = settings1.withAppendedAnnotationIntrospector(new AnnotationIntrospector());
        assertNotSame(settings1, settings2);
        assertEquals(new AnnotationIntrospectorPair(settings1.getAnnotationIntrospector(), new AnnotationIntrospector()), settings2.getAnnotationIntrospector());

        settings2 = settings1.withVisibilityChecker(new VisibilityChecker<Object>() {
            // Empty implementation for test purposes
        });
        assertNotSame(settings1, settings2);
        assertEquals(new VisibilityChecker<Object>() {
            // Empty implementation for test purposes
        }, settings2.getVisibilityChecker());

        settings2 = settings1.withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);
        assertNotSame(settings1, settings2);
        assertEquals(JsonAutoDetect.Visibility.NONE, settings2.getVisibilityChecker().getVisibleMembers(PropertyAccessor.FIELD, null, null));

        settings2 = settings1.withPropertyNamingStrategy(new PropertyNamingStrategy() {
            // Empty implementation for test purposes
        });
        assertNotSame(settings1, settings2);
        assertEquals(new PropertyNamingStrategy() {
            // Empty implementation for test purposes
        }, settings2.getPropertyNamingStrategy());

        settings2 = settings1.withTypeFactory(new TypeFactory());
        assertNotSame(settings1, settings2);
        assertEquals(new TypeFactory(), settings2.getTypeFactory());

        settings2 = settings1.withTypeResolverBuilder(new TypeResolverBuilder<Object>() {
            // Empty implementation for test purposes
        });
        assertNotSame(settings1, settings2);
        assertEquals(new TypeResolverBuilder<Object>() {
            // Empty implementation for test purposes
        }, settings2.getTypeResolverBuilder());

        settings2 = settings1.withDateFormat(new StdDateFormat());
        assertNotSame(settings1, settings2);
        assertEquals(new StdDateFormat(), settings2.getDateFormat());

        settings2 = settings1.withHandlerInstantiator(new HandlerInstantiator() {
            // Empty implementation for test purposes
        });
        assertNotSame(settings1, settings2);
        assertEquals(new HandlerInstantiator() {
            // Empty implementation for test purposes
        }, settings2.getHandlerInstantiator());

        settings2 = settings1.with(Locale.US);
        assertNotSame(settings1, settings2);
        assertEquals(Locale.US, settings2.getLocale());

        settings2 = settings1.with(TimeZone.getTimeZone("UTC"));
        assertNotSame(settings1, settings2);
        assertEquals(TimeZone.getTimeZone("UTC"), settings2.getTimeZone());

        settings2 = settings1.with(Base64Variant.BASE64_STANDARD);
        assertNotSame(settings1, settings2);
        assertEquals(Base64Variant.BASE64_STANDARD, settings2.getBase64Variant());
    }
}