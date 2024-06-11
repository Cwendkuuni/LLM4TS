
package com.fasterxml.jackson.databind.cfg;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setup() {
        baseSettings = new BaseSettings(null, null, null, null, null,
                null, null, null, null,
                null, null);
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings newSettings = baseSettings.withClassIntrospector(ci);
        assertNotSame(baseSettings, newSettings);
        assertEquals(ci, newSettings.getClassIntrospector());
    }

    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withAnnotationIntrospector(ai);
        assertNotSame(baseSettings, newSettings);
        assertEquals(ai, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withInsertedAnnotationIntrospector(ai);
        assertNotSame(baseSettings, newSettings);
        assertEquals(ai, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withAppendedAnnotationIntrospector(ai);
        assertNotSame(baseSettings, newSettings);
        assertEquals(ai, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker vc = new VisibilityChecker();
        BaseSettings newSettings = baseSettings.withVisibilityChecker(vc);
        assertNotSame(baseSettings, newSettings);
        assertEquals(vc, newSettings.getVisibilityChecker());
    }

    @Test
    public void testWithVisibility() {
        PropertyAccessor forMethod = PropertyAccessor.GETTER;
        JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.DEFAULT;
        BaseSettings newSettings = baseSettings.withVisibility(forMethod, visibility);
        assertNotSame(baseSettings, newSettings);
        assertEquals(forMethod, newSettings.getVisibilityChecker().getFieldVisibility());
        assertEquals(visibility, newSettings.getVisibilityChecker().getGetterVisibility());
        assertEquals(visibility, newSettings.getVisibilityChecker().isGetterVisible(null));
        assertEquals(visibility, newSettings.getVisibilityChecker().isSetterVisible(null));
        assertEquals(visibility, newSettings.getVisibilityChecker().isCreatorVisible(null));
    }

    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        BaseSettings newSettings = baseSettings.withPropertyNamingStrategy(pns);
        assertNotSame(baseSettings, newSettings);
        assertEquals(pns, newSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory tf = new TypeFactory();
        BaseSettings newSettings = baseSettings.withTypeFactory(tf);
        assertNotSame(baseSettings, newSettings);
        assertEquals(tf, newSettings.getTypeFactory());
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        BaseSettings newSettings = baseSettings.withTypeResolverBuilder(typer);
        assertNotSame(baseSettings, newSettings);
        assertEquals(typer, newSettings.getTypeResolverBuilder());
    }

    @Test
    public void testWithDateFormat() {
        DateFormat df = new DateFormat();
        BaseSettings newSettings = baseSettings.withDateFormat(df);
        assertNotSame(baseSettings, newSettings);
        assertEquals(df, newSettings.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator hi = new HandlerInstantiator();
        BaseSettings newSettings = baseSettings.withHandlerInstantiator(hi);
        assertNotSame(baseSettings, newSettings);
        assertEquals(hi, newSettings.getHandlerInstantiator());
    }

    @Test
    public void testWithLocale() {
        Locale l = new Locale("en");
        BaseSettings newSettings = baseSettings.with(l);
        assertNotSame(baseSettings, newSettings);
        assertEquals(l, newSettings.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        TimeZone tz = TimeZone.getTimeZone("GMT");
        BaseSettings newSettings = baseSettings.with(tz);
        assertNotSame(baseSettings, newSettings);
        assertEquals(tz, newSettings.getTimeZone());
        assertEquals(tz, newSettings.getDateFormat().getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant base64 = new Base64Variant();
        BaseSettings newSettings = baseSettings.with(base64);
        assertNotSame(baseSettings, newSettings);
        assertEquals(base64, newSettings.getBase64Variant());
    }
}
