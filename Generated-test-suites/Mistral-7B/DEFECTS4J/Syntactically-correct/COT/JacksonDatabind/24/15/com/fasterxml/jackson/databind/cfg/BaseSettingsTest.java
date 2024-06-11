package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.util.HandlerInstantiator;

public class BaseSettingsTest {

 private BaseSettings settings;

 @Before
 public void setUp() {
 ClassIntrospector ci = new ClassIntrospector();
 AnnotationIntrospector ai = new AnnotationIntrospector();
 VisibilityChecker<?> vc = new VisibilityChecker<>();
 PropertyNamingStrategy pns = new StdPropertyNamingStrategy();
 TypeFactory tf = new TypeFactory();
 TypeResolverBuilder<?> typer = new SimpleTypeResolverBuilder();
 DateFormat dateFormat = new StdDateFormat();
 HandlerInstantiator hi = new DefaultHandlerInstantiator();
 Locale locale = Locale.getDefault();
 TimeZone tz = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variant.STANDARD;
 settings = new BaseSettings(ci, ai, vc, pns, tf, typer, dateFormat, hi, locale, tz, defaultBase64);
 }

 @Test
 public void testGetClassIntrospector() {
 ClassIntrospector actual = settings.getClassIntrospector();
 assertEquals("Unexpected ClassIntrospector", ci, actual);
 }

 @Test
 public void testGetAnnotationIntrospector() {
 AnnotationIntrospector actual = settings.getAnnotationIntrospector();
 assertEquals("Unexpected AnnotationIntrospector", ai, actual);
 }

 @Test
 public void testGetVisibilityChecker() {
 VisibilityChecker<?> actual = settings.getVisibilityChecker();
 assertEquals("Unexpected VisibilityChecker", vc, actual);
 }

 @Test
 public void testGetPropertyNamingStrategy() {
 PropertyNamingStrategy actual = settings.getPropertyNamingStrategy();
 assertEquals("Unexpected PropertyNamingStrategy", pns, actual);
 }

 @Test
 public void testGetTypeFactory() {
 TypeFactory actual = settings.getTypeFactory();
 assertEquals("Unexpected TypeFactory", tf, actual);
 }

 @Test
 public void testGetTypeResolverBuilder() {
 TypeResolverBuilder<?> actual = settings.getTypeResolverBuilder();
 assertEquals("Unexpected TypeResolverBuilder", typer, actual);
 }

 @Test
 public void testGetDateFormat() {
 DateFormat actual = settings.getDateFormat();
 assertTrue("Unexpected DateFormat instance", actual instanceof StdDateFormat);
 }

 @Test
 public void testGetHandlerInstantiator() {
 HandlerInstantiator actual = settings.getHandlerInstantiator();
 assertEquals("Unexpected HandlerInstantiator", hi, actual);
 }

 @Test
 public void testGetLocale() {
 Locale actual = settings.getLocale();
 assertEquals("Unexpected Locale", locale, actual);
 }

 @Test
 public void testGetTimeZone() {
 TimeZone actual = settings.getTimeZone();
 assertEquals("Unexpected TimeZone", tz, actual);
 }

 @Test
 public void testGetBase64Variant() {
 Base64Variant actual = settings.getBase64Variant();
 assertEquals("Unexpected Base64Variant", Base64Variant.STANDARD, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullTimeZone() {
 settings.with(null);
 }

 @Test
 public void testWithDateFormat() {
 DateFormat dateFormat = new SimpleDateFormat();
 settings = settings.withDateFormat(dateFormat);
 assertNotSame("DateFormat instances should be different", settings._dateFormat, this.dateFormat);
 }
}