package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import org.junit.Test;

public class BaseSettingsTest {

    @Test
    public void testGetters() {
        ClassIntrospector ci = Mockito.mock(ClassIntrospector.class);
        AnnotationIntrospector ai = Mockito.mock(AnnotationIntrospector.class);
        VisibilityChecker<?> vc = Mockito.mock(VisibilityChecker.class);
        PropertyNamingStrategy pns = Mockito.mock(PropertyNamingStrategy.class);
        TypeFactory tf = Mockito.mock(TypeFactory.class);
        TypeResolverBuilder<?> typer = Mockito.mock(TypeResolverBuilder.class);
        DateFormat dateFormat = Mockito.mock(DateFormat.class);
        HandlerInstantiator hi = Mockito.mock(HandlerInstantiator.class);
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.STANDARD;

        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);

        assertEquals(ci, baseSettings.getClassIntrospector());
        assertEquals(ai, baseSettings.getAnnotationIntrospector());
        assertEquals(vc, baseSettings.getVisibilityChecker());
        assertEquals(pns, baseSettings.getPropertyNamingStrategy());
        assertEquals(tf, baseSettings.getTypeFactory());
        assertEquals(typer, baseSettings.getTypeResolverBuilder());
        assertEquals(dateFormat, baseSettings.getDateFormat());
        assertEquals(hi, baseSettings.getHandlerInstantiator());
        assertEquals(locale, baseSettings.getLocale());
        assertEquals(timeZone, baseSettings.getTimeZone());
        assertEquals(defaultBase64, baseSettings.getBase64Variant());
    }

    @Test
    public void testFactoryMethods() {
        ClassIntrospector ci = Mockito.mock(ClassIntrospector.class);
        AnnotationIntrospector ai = Mockito.mock(AnnotationIntrospector.class);
        VisibilityChecker<?> vc = Mockito.mock(VisibilityChecker.class);
        PropertyNamingStrategy pns = Mockito.mock(PropertyNamingStrategy.class);
        TypeFactory tf = Mockito.mock(TypeFactory.class);
        TypeResolverBuilder<?> typer = Mockito.mock(TypeResolverBuilder.class);
        DateFormat dateFormat = Mockito.mock(DateFormat.class);
        HandlerInstantiator hi = Mockito.mock(HandlerInstantiator.class);
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = Base64Variant.STANDARD;
        BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);

        BaseSettings withClassIntrospector = baseSettings.withClassIntrospector(ci);
        assertNotSame(baseSettings, withClassIntrospector);
        assertEquals(ci, withClassIntrospector.getClassIntrospector());

        BaseSettings withAnnotationIntrospector = baseSettings.withAnnotationIntrospector(ai);
        assertNotSame(baseSettings, withAnnotationIntrospector);
        assertEquals(ai, withAnnotationIntrospector.getAnnotationIntrospector());

        BaseSettings withVisibilityChecker = baseSettings.withVisibilityChecker(vc);
        assertNotSame(baseSettings, withVisibilityChecker);
        assertEquals(vc, withVisibilityChecker.getVisibilityChecker());

        BaseSettings withPropertyNamingStrategy = baseSettings.withPropertyNamingStrategy(pns);
        assertNotSame(baseSettings, withPropertyNamingStrategy);
        assertEquals(pns, withPropertyNamingStrategy.getPropertyNamingStrategy());

        BaseSettings withTypeFactory = baseSettings.withTypeFactory(tf);
        assertNotSame(baseSettings, withTypeFactory);
        assertEquals(tf, withTypeFactory.getTypeFactory());

        BaseSettings withTypeResolverBuilder = baseSettings.withTypeResolverBuilder(typer);
        assertNotSame(baseSettings, withTypeResolverBuilder);
        assertEquals(typer, withTypeResolverBuilder.getTypeResolverBuilder());

        BaseSettings withDateFormat = baseSettings.withDateFormat(dateFormat);
        assertNotSame(baseSettings, withDateFormat);
        assertEquals(dateFormat, withDateFormat.getDateFormat());

        BaseSettings withHandlerInstantiator = baseSettings.withHandlerInstantiator(hi);
        assertNotSame(baseSettings, withHandlerInstantiator);
        assertEquals(hi, withHandlerInstantiator.getHandlerInstantiator());

        BaseSettings withLocale = baseSettings.with(locale);
        assertNotSame(baseSettings, withLocale);
        assertEquals(locale, withLocale.getLocale());

        BaseSettings withTimeZone = baseSettings.with(timeZone);
        assertNotSame(baseSettings, withTimeZone);
        assertEquals(timeZone, withTimeZone.getTimeZone());

        BaseSettings withDefaultBase64 = baseSettings.with(Base64Variant.BASE64_EXTENDED);
        assertNotSame(baseSettings, withDefaultBase64);
        assertEquals(Base64Variant.BASE64_EXTENDED, withDefaultBase64.getBase64Variant());
    }
}