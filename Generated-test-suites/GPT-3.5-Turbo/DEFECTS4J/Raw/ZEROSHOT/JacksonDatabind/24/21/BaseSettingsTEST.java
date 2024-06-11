package com.fasterxml.jackson.databind.cfg;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {
    
    private BaseSettings baseSettings;
    
    @Before
    public void setup() {
        baseSettings = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
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
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        BaseSettings newSettings = baseSettings.withVisibilityChecker(vc);
        assertNotSame(baseSettings, newSettings);
        assertEquals(vc, newSettings.getVisibilityChecker());
    }
    
    @Test
    public void testWithVisibility() {
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        BaseSettings newSettings = baseSettings.withVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        assertNotSame(baseSettings, newSettings);
        assertEquals(vc, newSettings.getVisibilityChecker());
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
        Locale locale = new Locale();
        BaseSettings newSettings = baseSettings.with(locale);
        assertNotSame(baseSettings, newSettings);
        assertEquals(locale, newSettings.getLocale());
    }
    
    @Test
    public void testWithTimeZone() {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        BaseSettings newSettings = baseSettings.with(tz);
        assertNotSame(baseSettings, newSettings);
        assertEquals(tz, newSettings.getTimeZone());
    }
    
    @Test
    public void testWithBase64Variant() {
        Base64Variant base64 = new Base64Variant();
        BaseSettings newSettings = baseSettings.with(base64);
        assertNotSame(baseSettings, newSettings);
        assertEquals(base64, newSettings.getBase64Variant());
    }
    
    @Test
    public void testGetClassIntrospector() {
        assertNull(baseSettings.getClassIntrospector());
    }
    
    @Test
    public void testGetAnnotationIntrospector() {
        assertNull(baseSettings.getAnnotationIntrospector());
    }

    @Test
    public void testGetVisibilityChecker() {
        assertNull(baseSettings.getVisibilityChecker());
    }

    @Test
    public void testGetPropertyNamingStrategy() {
        assertNull(baseSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testGetTypeFactory() {
        assertNull(baseSettings.getTypeFactory());
    }

    @Test
    public void testGetTypeResolverBuilder() {
        assertNull(baseSettings.getTypeResolverBuilder());
    }
    
    @Test
    public void testGetDateFormat() {
        assertNull(baseSettings.getDateFormat());
    }

    @Test
    public void testGetHandlerInstantiator() {
        assertNull(baseSettings.getHandlerInstantiator());
    }

    @Test
    public void testGetLocale() {
        assertNull(baseSettings.getLocale());
    }

    @Test
    public void testGetTimeZone() {
        assertNull(baseSettings.getTimeZone());
    }

    @Test
    public void testGetBase64Variant() {
        assertNull(baseSettings.getBase64Variant());
    }
}