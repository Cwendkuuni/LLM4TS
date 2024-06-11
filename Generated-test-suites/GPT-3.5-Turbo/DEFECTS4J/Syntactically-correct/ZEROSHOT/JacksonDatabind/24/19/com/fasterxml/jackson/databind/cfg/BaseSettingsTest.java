
package com.fasterxml.jackson.databind.cfg;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setUp() {
        baseSettings = new BaseSettings(null, null, null, null, null,
                null, null, null, null,
                null, null);
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings newSettings = baseSettings.withClassIntrospector(ci);
        assertEquals(ci, newSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withAnnotationIntrospector(ai);
        assertEquals(ai, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withInsertedAnnotationIntrospector(ai);
        assertEquals(ai, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withAppendedAnnotationIntrospector(ai);
        assertEquals(ai, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker vc = new VisibilityChecker();
        BaseSettings newSettings = baseSettings.withVisibilityChecker(vc);
        assertEquals(vc, newSettings.getVisibilityChecker());
    }

    @Test
    public void testWithVisibility() {
        PropertyAccessor forMethod = PropertyAccessor.METHOD;
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.ANY;
        BaseSettings newSettings = baseSettings.withVisibility(forMethod, visibility);
        assertEquals(visibility, newSettings.getVisibilityChecker().findGetterVisibility());
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        BaseSettings newSettings = baseSettings.withPropertyNamingStrategy(pns);
        assertEquals(pns, newSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory tf = new TypeFactory();
        BaseSettings newSettings = baseSettings.withTypeFactory(tf);
        assertEquals(tf, newSettings.getTypeFactory());
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        BaseSettings newSettings = baseSettings.withTypeResolverBuilder(typer);
        assertEquals(typer, newSettings.getTypeResolverBuilder());
    }

    @Test
    public void testWithDateFormat() {
        DateFormat dateFormat = new DateFormat();
        BaseSettings newSettings = baseSettings.withDateFormat(dateFormat);
        assertEquals(dateFormat, newSettings.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator hi = new HandlerInstantiator();
        BaseSettings newSettings = baseSettings.withHandlerInstantiator(hi);
        assertEquals(hi, newSettings.getHandlerInstantiator());
    }

    @Test
    public void testWithLocale() {
        Locale locale = new Locale();
        BaseSettings newSettings = baseSettings.with(locale);
        assertEquals(locale, newSettings.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        TimeZone timeZone = new TimeZone();
        BaseSettings newSettings = baseSettings.with(timeZone);
        assertEquals(timeZone, newSettings.getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant base64 = new Base64Variant();
        BaseSettings newSettings = baseSettings.with(base64);
        assertEquals(base64, newSettings.getBase64Variant());
    }

    // Add tests for other methods in BaseSettings here
}
