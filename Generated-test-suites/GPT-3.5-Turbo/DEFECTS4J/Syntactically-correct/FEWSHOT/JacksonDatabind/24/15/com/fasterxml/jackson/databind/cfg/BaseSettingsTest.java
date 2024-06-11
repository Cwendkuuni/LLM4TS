package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {
    
    private ClassIntrospector ci;
    private AnnotationIntrospector ai;
    private VisibilityChecker<?> vc;
    private PropertyNamingStrategy pns;
    private TypeFactory tf;
    private TypeResolverBuilder<?> typer;
    private DateFormat dateFormat;
    private HandlerInstantiator hi;
    private Locale locale;
    private TimeZone timeZone;
    private Base64Variant defaultBase64;
    private BaseSettings baseSettings;
    
    @Before
    public void setup() {
        ci = new ClassIntrospector();
        ai = new AnnotationIntrospector();
        vc = new VisibilityChecker<?>();
        pns = new PropertyNamingStrategy();
        tf = new TypeFactory();
        typer = new TypeResolverBuilder<?>();
        dateFormat = new StdDateFormat();
        hi = new HandlerInstantiator();
        locale = Locale.getDefault();
        timeZone = TimeZone.getDefault();
        defaultBase64 = new Base64Variant();
        
        baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, timeZone, defaultBase64);
    }
    
    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector newCi = new ClassIntrospector();
        BaseSettings newSettings = baseSettings.withClassIntrospector(newCi);
        
        assertNotNull(newSettings);
        assertEquals(newCi, newSettings.getClassIntrospector());
        
        assertEquals(ai, newSettings.getAnnotationIntrospector());
        assertEquals(vc, newSettings.getVisibilityChecker());
        assertEquals(pns, newSettings.getPropertyNamingStrategy());
        assertEquals(tf, newSettings.getTypeFactory());
        assertEquals(typer, newSettings.getTypeResolverBuilder());
        assertEquals(dateFormat, newSettings.getDateFormat());
        assertEquals(hi, newSettings.getHandlerInstantiator());
        assertEquals(locale, newSettings.getLocale());
        assertEquals(timeZone, newSettings.getTimeZone());
        assertEquals(defaultBase64, newSettings.getBase64Variant());
    }
    
    // Add more test cases for other methods in the BaseSettings class
    
        
    
}