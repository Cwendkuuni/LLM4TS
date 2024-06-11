
package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {

    private BaseSettings baseSettings;

    @Before
    public void setup() {
        ClassIntrospector classIntrospector = new ClassIntrospector();
        AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
        VisibilityChecker<?> visibilityChecker = new VisibilityChecker<?>();
        PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy();
        TypeFactory typeFactory = new TypeFactory();
        TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilder<?>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
        Locale locale = Locale.US;
        TimeZone timeZone = TimeZone.getDefault();
        Base64Variant defaultBase64 = new Base64Variant("test", "test", false, 'A', '+');
        
        baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker,
                propertyNamingStrategy, typeFactory, typeResolverBuilder, dateFormat,
                handlerInstantiator, locale, timeZone, defaultBase64);
    }

    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector newClassIntrospector = new ClassIntrospector();
        BaseSettings newSettings = baseSettings.withClassIntrospector(newClassIntrospector);
        
        assertNotSame(baseSettings, newSettings);
        assertEquals(newClassIntrospector, newSettings.getClassIntrospector());
    }
    
    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withAnnotationIntrospector(newAnnotationIntrospector);
        
        assertNotSame(baseSettings, newSettings);
        assertEquals(newAnnotationIntrospector, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithInsertedAnnotationIntrospector() {
        AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withInsertedAnnotationIntrospector(newAnnotationIntrospector);
        
        assertNotSame(baseSettings, newSettings);
        assertEquals(newAnnotationIntrospector, newSettings.getAnnotationIntrospector());
    }

    @Test
    public void testWithAppendedAnnotationIntrospector() {
        AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
        BaseSettings newSettings = baseSettings.withAppendedAnnotationIntrospector(newAnnotationIntrospector);
        
        assertNotSame(baseSettings, newSettings);
        assertEquals(newAnnotationIntrospector, newSettings.getAnnotationIntrospector());
    }
    
    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker<?> newVisibilityChecker = new VisibilityChecker<?>();
        BaseSettings newSettings = baseSettings.withVisibilityChecker(newVisibilityChecker);
        
        assertNotSame(baseSettings, newSettings);
        assertEquals(newVisibilityChecker, newSettings.getVisibilityChecker());
    }

    @Test
    public void testWithVisibility() {
        BaseSettings newSettings = baseSettings.withVisibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NONE);
        
        assertNotSame(baseSettings, newSettings);
        assertEquals(JsonAutoDetect.Visibility.NONE, newSettings.getVisibilityChecker().getGetterVisibility());
    }
    
    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy newPropertyNamingStrategy = new PropertyNamingStrategy();
        BaseSettings newSettings = baseSettings.withPropertyNamingStrategy(newPropertyNamingStrategy);
        
        assertNotSame(baseSettings, newSettings);
        assertEquals(newPropertyNamingStrategy, newSettings.getPropertyNamingStrategy());
    }

    @Test
    public void testWithTypeFactory() {
        TypeFactory newTypeFactory = new TypeFactory();
        BaseSettings newSettings = baseSettings.withTypeFactory(newTypeFactory);
        
        assertNotSame(baseSettings, newSettings);
        assertEquals(newTypeFactory, newSettings.getTypeFactory());
    }

    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> newTypeResolverBuilder = new TypeResolverBuilder<?>();
        BaseSettings newSettings = baseSettings.withTypeResolverBuilder(newTypeResolverBuilder);
        
        assertNotSame(baseSettings, newSettings);
        assertEquals(newTypeResolverBuilder, newSettings.getTypeResolverBuilder());
    }
    
    @Test
    public void testWithDateFormat() {
        DateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        BaseSettings newSettings = baseSettings.withDateFormat(newDateFormat);
        
        assertNotSame(baseSettings, newSettings);
        assertEquals(newDateFormat, newSettings.getDateFormat());
    }

    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator newHandlerInstantiator = new HandlerInstantiator();
        BaseSettings newSettings = baseSettings.withHandlerInstantiator(newHandlerInstantiator);
        
        assertNotSame(baseSettings, newSettings);
        assertEquals(newHandlerInstantiator, newSettings.getHandlerInstantiator());
    }

    @Test
    public void testWithLocale() {
        Locale newLocale = Locale.US;
        BaseSettings newSettings = baseSettings.with(newLocale);
        
        assertNotSame(baseSettings, newSettings);
        assertEquals(newLocale, newSettings.getLocale());
    }

    @Test
    public void testWithTimeZone() {
        TimeZone newTimeZone = TimeZone.getDefault();
        BaseSettings newSettings = baseSettings.with(newTimeZone);
        
        assertNotSame(baseSettings, newSettings);
        assertEquals(newTimeZone, newSettings.getTimeZone());
    }

    @Test
    public void testWithBase64Variant() {
        Base64Variant newBase64Variant = new Base64Variant("test1", "test2", true, 'B', '-');
        BaseSettings newSettings = baseSettings.with(newBase64Variant);
        
        assertNotSame(baseSettings, newSettings);
        assertEquals(newBase64Variant, newSettings.getBase64Variant());
    }
}
