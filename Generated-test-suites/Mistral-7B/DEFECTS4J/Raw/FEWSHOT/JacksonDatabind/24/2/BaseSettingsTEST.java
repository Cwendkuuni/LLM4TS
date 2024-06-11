package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testGetters() {
        ClassIntrospector ci = null;
        AnnotationIntrospector ai = null;
        VisibilityChecker<?> vc = null;
        PropertyNamingStrategy pns = null;
        TypeFactory tf = null;
        TypeResolverBuilder<?> typer = null;
        DateFormat dateFormat = new StdDateFormat();
        HandlerInstantiator hi = null;
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = null;

        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);

        assertEquals(ci, settings.getClassIntrospector());
        assertEquals(ai, settings.getAnnotationIntrospector());
        assertEquals(vc, settings.getVisibilityChecker());
        assertEquals(pns, settings.getPropertyNamingStrategy());
        assertEquals(tf, settings.getTypeFactory());
        assertEquals(typer, settings.getTypeResolverBuilder());
        assertEquals(dateFormat, settings.getDateFormat());
        assertEquals(hi, settings.getHandlerInstantiator());
        assertEquals(locale, settings.getLocale());
        assertEquals(timeZone, settings.getTimeZone());
        assertEquals(defaultBase64, settings.getBase64Variant());

        // Test null values
        ci = null;
        settings = new BaseSettings(ci, ai, vc, pns, tf, typer, null, hi, locale, timeZone, defaultBase64);
        assertNull(settings.getClassIntrospector());
        assertNull(settings.getAnnotationIntrospector());
        assertNull(settings.getVisibilityChecker());
        assertNull(settings.getPropertyNamingStrategy());
        assertNull(settings.getTypeFactory());
        assertNull(settings.getTypeResolverBuilder());
        assertNull(settings.getDateFormat());
        assertNull(settings.getHandlerInstantiator());
        assertNull(settings.getLocale());
        assertNotNull(settings.getTimeZone()); // getTimeZone() returns defaultTimeZone even if null is passed
        assertNull(settings.getBase64Variant());
    }

    @Test
    public void testFactoryMethods() {
        ClassIntrospector ci = null;
        AnnotationIntrospector ai = null;
        VisibilityChecker<?> vc = null;
        PropertyNamingStrategy pns = null;
        TypeFactory tf = null;
        TypeResolverBuilder<?> typer = null;
        DateFormat dateFormat = null;
        HandlerInstantiator hi = null;
        Locale locale = Locale.getDefault();
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = null;

        BaseSettings settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);

        settings = settings.withClassIntrospector(new ClassIntrospector() {});
        assertEquals(new ClassIntrospector(), settings.getClassIntrospector());

        settings = settings.withAnnotationIntrospector(new AnnotationIntrospector() {});
        assertEquals(new AnnotationIntrospector(), settings.getAnnotationIntrospector());

        settings = settings.withInsertedAnnotationIntrospector(new AnnotationIntrospector() {});
        assertEquals(new AnnotationIntrospectorPair(new AnnotationIntrospector(), settings.getAnnotationIntrospector()),
                settings.getAnnotationIntrospector());

        settings = settings.withAppendedAnnotationIntrospector(new AnnotationIntrospector() {});
        assertEquals(settings.getAnnotationIntrospector(), new AnnotationIntrospectorPair(settings.getAnnotationIntrospector(), new AnnotationIntrospector()));

        settings = settings.withVisibilityChecker(new VisibilityChecker<Object>() {});
        assertEquals(new VisibilityChecker<Object>(), settings.getVisibilityChecker());

        settings = settings.withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);
        assertEquals(new VisibilityChecker<Object>(settings.getVisibilityChecker().withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE)),
                settings.getVisibilityChecker());

        settings = settings.withPropertyNamingStrategy(new PropertyNamingStrategy() {});
        assertEquals(new PropertyNamingStrategy(), settings.getPropertyNamingStrategy());

        settings = settings.withTypeFactory(new TypeFactory() {});
        assertEquals(new TypeFactory(), settings.getTypeFactory());

        settings = settings.withTypeResolverBuilder(new TypeResolverBuilder<Object>() {});
        assertEquals(new TypeResolverBuilder<Object>(), settings.getTypeResolverBuilder());

        settings = settings.withDateFormat(new StdDateFormat());
        assertEquals(new StdDateFormat(), settings.getDateFormat());

        settings = settings.withHandlerInstantiator(new HandlerInstantiator() {});
        assertEquals(new HandlerInstantiator(), settings.getHandlerInstantiator());

        settings = settings.with(Locale.US);
        assertEquals(Locale.US, settings.getLocale());

        settings = settings.with(TimeZone.getTimeZone("Asia/Kolkata"));
        assertEquals(TimeZone.getTimeZone("Asia/Kolkata"), settings.getTimeZone());

        settings = settings.with(new Base64Variant() {});
        assertEquals(new Base64Variant(), settings.getBase64Variant());
    }
}