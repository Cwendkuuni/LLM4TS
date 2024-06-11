
package com.fasterxml.jackson.databind.cfg;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BaseSettingsTest {
  
  private BaseSettings baseSettings;
  
  @Before
  public void setUp() {
    ClassIntrospector ci = new ClassIntrospector();
    AnnotationIntrospector ai = new AnnotationIntrospector();
    VisibilityChecker<?> vc = new VisibilityChecker<>();
    PropertyNamingStrategy pns = new PropertyNamingStrategy();
    TypeFactory tf = new TypeFactory();
    TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
    DateFormat dateFormat = new DateFormat();
    HandlerInstantiator hi = new HandlerInstantiator();
    Locale locale = new Locale();
    TimeZone tz = TimeZone.getDefault();
    Base64Variant defaultBase64 = new Base64Variant();
    
    baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
  }
  
  @Test
  public void testWithClassIntrospector() {
    ClassIntrospector ci = new ClassIntrospector();
    
    BaseSettings updatedSettings = baseSettings.withClassIntrospector(ci);
    
    assertEquals(updatedSettings.getClassIntrospector(), ci);
    assertNotEquals(updatedSettings.getClassIntrospector(), baseSettings.getClassIntrospector());
  }
  
  @Test
  public void testWithAnnotationIntrospector() {
    AnnotationIntrospector ai = new AnnotationIntrospector();
    
    BaseSettings updatedSettings = baseSettings.withAnnotationIntrospector(ai);
    
    assertEquals(updatedSettings.getAnnotationIntrospector(), ai);
    assertNotEquals(updatedSettings.getAnnotationIntrospector(), baseSettings.getAnnotationIntrospector());
  }
  
  @Test
  public void testWithInsertedAnnotationIntrospector() {
    AnnotationIntrospector ai = new AnnotationIntrospector();
    
    BaseSettings updatedSettings = baseSettings.withInsertedAnnotationIntrospector(ai);
    
    assertNotEquals(updatedSettings.getAnnotationIntrospector(), ai);
    assertNotEquals(updatedSettings.getAnnotationIntrospector(), baseSettings.getAnnotationIntrospector());
  }
  
  @Test
  public void testWithAppendedAnnotationIntrospector() {
    AnnotationIntrospector ai = new AnnotationIntrospector();
    
    BaseSettings updatedSettings = baseSettings.withAppendedAnnotationIntrospector(ai);
    
    assertNotEquals(updatedSettings.getAnnotationIntrospector(), ai);
    assertNotEquals(updatedSettings.getAnnotationIntrospector(), baseSettings.getAnnotationIntrospector());
  }
  
  @Test
  public void testWithVisibilityChecker() {
    VisibilityChecker<?> vc = new VisibilityChecker<>();
    
    BaseSettings updatedSettings = baseSettings.withVisibilityChecker(vc);
    
    assertEquals(updatedSettings.getVisibilityChecker(), vc);
    assertNotEquals(updatedSettings.getVisibilityChecker(), baseSettings.getVisibilityChecker());
  }
  
  @Test
  public void testWithVisibility() {
    PropertyAccessor forMethod = new PropertyAccessor();
    JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.DEFAULT;
    
    BaseSettings updatedSettings = baseSettings.withVisibility(forMethod, visibility);
    
    assertNotEquals(updatedSettings.getVisibilityChecker(), baseSettings.getVisibilityChecker());
  }
  
  @Test
  public void testWithPropertyNamingStrategy() {
    PropertyNamingStrategy pns = new PropertyNamingStrategy();
    
    BaseSettings updatedSettings = baseSettings.withPropertyNamingStrategy(pns);
    
    assertEquals(updatedSettings.getPropertyNamingStrategy(), pns);
    assertNotEquals(updatedSettings.getPropertyNamingStrategy(), baseSettings.getPropertyNamingStrategy());
  }
  
  @Test
  public void testWithTypeFactory() {
    TypeFactory tf = new TypeFactory();
    
    BaseSettings updatedSettings = baseSettings.withTypeFactory(tf);
    
    assertEquals(updatedSettings.getTypeFactory(), tf);
    assertNotEquals(updatedSettings.getTypeFactory(), baseSettings.getTypeFactory());
  }
  
  @Test
  public void testWithTypeResolverBuilder() {
    TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
    
    BaseSettings updatedSettings = baseSettings.withTypeResolverBuilder(typer);
    
    assertEquals(updatedSettings.getTypeResolverBuilder(), typer);
    assertNotEquals(updatedSettings.getTypeResolverBuilder(), baseSettings.getTypeResolverBuilder());
  }
  
  @Test
  public void testWithDateFormat() {
    DateFormat df = new DateFormat();
    
    BaseSettings updatedSettings = baseSettings.withDateFormat(df);
    
    assertEquals(updatedSettings.getDateFormat(), df);
    assertNotEquals(updatedSettings.getDateFormat(), baseSettings.getDateFormat());
  }
  
  @Test
  public void testWithHandlerInstantiator() {
    HandlerInstantiator hi = new HandlerInstantiator();
    
    BaseSettings updatedSettings = baseSettings.withHandlerInstantiator(hi);
    
    assertEquals(updatedSettings.getHandlerInstantiator(), hi);
    assertNotEquals(updatedSettings.getHandlerInstantiator(), baseSettings.getHandlerInstantiator());
  }
  
  @Test
  public void testWithLocale() {
    Locale locale = new Locale();
    
    BaseSettings updatedSettings = baseSettings.with(locale);
    
    assertEquals(updatedSettings.getLocale(), locale);
    assertNotEquals(updatedSettings.getLocale(), baseSettings.getLocale());
  }
  
  @Test
  public void testWithTimeZone() {
    TimeZone tz = TimeZone.getDefault();
    
    BaseSettings updatedSettings = baseSettings.with(tz);
    
    assertEquals(updatedSettings.getTimeZone(), tz);
    assertNotEquals(updatedSettings.getTimeZone(), baseSettings.getTimeZone());
  }
  
  @Test
  public void testWithBase64Variant() {
    Base64Variant base64 = new Base64Variant();
    
    BaseSettings updatedSettings = baseSettings.with(base64);
    
    assertEquals(updatedSettings.getBase64Variant(), base64);
    assertNotEquals(updatedSettings.getBase64Variant(), baseSettings.getBase64Variant());
  }
  
  // Add more test methods to test all the methods in the BaseSettings class
  
}
