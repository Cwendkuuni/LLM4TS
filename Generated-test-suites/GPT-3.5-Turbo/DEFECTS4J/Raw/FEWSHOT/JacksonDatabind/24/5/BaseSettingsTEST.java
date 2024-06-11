package com.fasterxml.jackson.databind.cfg;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSettingsTest {
    
    @Test
    public void testWithClassIntrospector() {
        ClassIntrospector ci = new ClassIntrospector();
        BaseSettings bs = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings result = bs.withClassIntrospector(ci);
        assertEquals(ci, result.getClassIntrospector());
    }
    
    @Test
    public void testWithAnnotationIntrospector() {
        AnnotationIntrospector ai = new AnnotationIntrospector();
        BaseSettings bs = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings result = bs.withAnnotationIntrospector(ai);
        assertEquals(ai, result.getAnnotationIntrospector());
    }
    
    @Test
    public void testWithVisibilityChecker() {
        VisibilityChecker<?> vc = new VisibilityChecker<>();
        BaseSettings bs = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings result = bs.withVisibilityChecker(vc);
        assertEquals(vc, result.getVisibilityChecker());
    }
    
    @Test
    public void testWithPropertyNamingStrategy() {
        PropertyNamingStrategy pns = new PropertyNamingStrategy();
        BaseSettings bs = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings result = bs.withPropertyNamingStrategy(pns);
        assertEquals(pns, result.getPropertyNamingStrategy());
    }
    
    @Test
    public void testWithTypeFactory() {
        TypeFactory tf = new TypeFactory();
        BaseSettings bs = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings result = bs.withTypeFactory(tf);
        assertEquals(tf, result.getTypeFactory());
    }
    
    @Test
    public void testWithTypeResolverBuilder() {
        TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
        BaseSettings bs = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings result = bs.withTypeResolverBuilder(typer);
        assertEquals(typer, result.getTypeResolverBuilder());
    }
    
    @Test
    public void testWithDateFormat() {
        DateFormat df = new DateFormat();
        BaseSettings bs = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings result = bs.withDateFormat(df);
        assertEquals(df, result.getDateFormat());
    }
    
    @Test
    public void testWithHandlerInstantiator() {
        HandlerInstantiator hi = new HandlerInstantiator();
        BaseSettings bs = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings result = bs.withHandlerInstantiator(hi);
        assertEquals(hi, result.getHandlerInstantiator());
    }
    
    @Test
    public void testWith() {
        Locale l = Locale.getDefault();
        BaseSettings bs = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings result = bs.with(l);
        assertEquals(l, result.getLocale());
    }
    
    @Test
    public void testWith2() {
        TimeZone tz = TimeZone.getDefault();
        BaseSettings bs = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings result = bs.with(tz);
        assertEquals(tz, result.getTimeZone());
    }
    
    @Test
    public void testWithBase64Variant() {
        Base64Variant bv = new Base64Variant();
        BaseSettings bs = new BaseSettings(null, null, null, null, null, null, null, null, null, null, null);
        BaseSettings result = bs.with(bv);
        assertEquals(bv, result.getBase64Variant());
    }
}