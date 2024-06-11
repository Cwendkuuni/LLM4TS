package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.time.ZoneId;

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

public class BaseSettingsTest {

 private BaseSettings originalSettings;
 private BaseSettings newSettings;

 @Before
 public void setUp() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker<>();
 PropertyNamingStrategy propertyNamingStrategy = null;
 TypeFactory typeFactory = new TypeFactory();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilder();
 DateFormat dateFormat = new StdDateFormat();
 HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
 Locale locale = Locale.getDefault();
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant base64 = Base64Variant.DEFAULT;

 originalSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
         typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, base64);
 }

 @Test
 public void testWithClassIntrospector() {
 ClassIntrospector ci = new ClassIntrospector();
 newSettings = originalSettings.withClassIntrospector(ci);
 assertNotSame(originalSettings, newSettings);
 assertEquals(ci, newSettings.getClassIntrospector());
 }

 @Test
 public void testWithAnnotationIntrospector() {
 AnnotationIntrospector ai = new AnnotationIntrospector();
 newSettings = originalSettings.withAnnotationIntrospector(ai);
 assertNotSame(originalSettings, newSettings);
 assertEquals(ai, newSettings.getAnnotationIntrospector());
 }

 @Test
 public void testWithInsertedAnnotationIntrospector() {
 AnnotationIntrospector ai = new AnnotationIntrospector();
 newSettings = originalSettings.withInsertedAnnotationIntrospector(ai);
 assertNotSame(originalSettings, newSettings);
 assertEquals(ai, newSettings.getAnnotationIntrospector());
 assertEquals(originalSettings.getAnnotationIntrospector(), newSettings.getAnnotationIntrospector().getAnnotationIntrospector());
 }

 @Test
 public void testWithAppendedAnnotationIntrospector() {
 AnnotationIntrospector ai = new AnnotationIntrospector();
 newSettings = originalSettings.withAppendedAnnotationIntrospector(ai);
 assertNotSame(originalSettings, newSettings);
 assertEquals(originalSettings.getAnnotationIntrospector(), newSettings.getAnnotationIntrospector());
 }

 @Test
 public void testWithVisibilityChecker() {
 VisibilityChecker<?> vc = new VisibilityChecker<>();
 newSettings = originalSettings.withVisibilityChecker(vc);
 assertNotSame(originalSettings, newSettings);
 assertEquals(vc, newSettings.getVisibilityChecker());
 }

 @Test
 public void testWithVisibility() {
 PropertyAccessor accessor = PropertyAccessor.FIELD;
 JsonAutoDetect.Visibility visibility = JsonAutoDetect.Visibility.ANY;
 newSettings = originalSettings.withVisibility(accessor, visibility);
 assertNotSame(originalSettings, newSettings);
 }

 @Test
 public void testWithPropertyNamingStrategy() {
 PropertyNamingStrategy pns = new PropertyNamingStrategy();
 newSettings = originalSettings.withPropertyNamingStrategy(pns);
 assertNotSame(originalSettings, newSettings);
 assertEquals(pns, newSettings.getPropertyNamingStrategy());
 }

 @Test
 public void testWithTypeFactory() {
 TypeFactory tf = new TypeFactory();
 newSettings = originalSettings.withTypeFactory(tf);
 assertNotSame(originalSettings, newSettings);
 assertEquals(tf, newSettings.getTypeFactory());
 }

 @Test
 public void testWithTypeResolverBuilder() {
 TypeResolverBuilder<?> typer = new TypeResolverBuilder();
 newSettings = originalSettings.withTypeResolverBuilder(typer);
 assertNotSame(originalSettings, newSettings);
 assertEquals(typer, newSettings.getTypeResolverBuilder());
 }

 @Test
 public void testWithDateFormat() {
 DateFormat df = new StdDateFormat();
 newSettings = originalSettings.withDateFormat(df);
 assertNotSame(originalSettings, newSettings);
 assertEquals(df, newSettings.getDateFormat());
 }

 @Test
 public void testWithHandlerInstantiator() {
 HandlerInstantiator hi = new HandlerInstantiator();
 newSettings = originalSettings.withHandlerInstantiator(hi);
 assertNotSame(originalSettings, newSettings);
 assertEquals(hi, newSettings.getHandlerInstantiator());
 }

 @Test
 public void testWith() {
 Locale locale = Locale.US;
 newSettings = originalSettings.with(locale);
 assertNotSame(originalSettings, newSettings);
 assertEquals(locale, newSettings.getLocale());
 }

 @Test
 public void testWithTimeZone() {
 ZoneId zoneId = ZoneId.of("UTC");
 TimeZone timeZone = TimeZone.getTimeZone(zoneId);
 newSettings = originalSettings.with(timeZone);
 assertNotSame(originalSettings, newSettings);
 assertEquals(timeZone, newSettings.getTimeZone());
 assertEquals(newSettings.getDateFormat().getTimeZone(), timeZone);
 }

 @Test
 public void testWithBase64Variant() {
 Base64Variant base64 = Base64Variant.US_ASCII;
 newSettings = originalSettings.with(base64);
 assertNotSame(originalSettings, newSettings);
 assertEquals(base64, newSettings.getBase64Variant());
 }

}