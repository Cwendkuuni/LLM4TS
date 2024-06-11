package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
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

 private BaseSettings baseSettings;

 @Before
 public void setUp() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.Std();
 PropertyNamingStrategy propertyNamingStrategy = PropertyNamingStrategy.STANDARD_EXCLUDE_PREFIXES;
 TypeFactory typeFactory = new TypeFactory();
 TypeResolverBuilder<?> typeResolverBuilder = new MapTypeResolverBuilder();
 DateFormat dateFormat = new StdDateFormat();
 HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
 Locale locale = Locale.getDefault();
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variant.STANDARD;
 baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
 typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);
 }

 @Test
 public void testGetters() {
 ClassIntrospector actualClassIntrospector = baseSettings.getClassIntrospector();
 AnnotationIntrospector actualAnnotationIntrospector = baseSettings.getAnnotationIntrospector();
 VisibilityChecker<?> actualVisibilityChecker = baseSettings.getVisibilityChecker();
 PropertyNamingStrategy actualPropertyNamingStrategy = baseSettings.getPropertyNamingStrategy();
 TypeFactory actualTypeFactory = baseSettings.getTypeFactory();
 TypeResolverBuilder<?> actualTypeResolverBuilder = baseSettings.getTypeResolverBuilder();
 DateFormat actualDateFormat = baseSettings.getDateFormat();
 HandlerInstantiator actualHandlerInstantiator = baseSettings.getHandlerInstantiator();
 Locale actualLocale = baseSettings.getLocale();
 TimeZone actualTimeZone = baseSettings.getTimeZone();
 Base64Variant actualBase64Variant = baseSettings.getBase64Variant();
 assertEquals(actualClassIntrospector, classIntrospector);
 assertEquals(actualAnnotationIntrospector, annotationIntrospector);
 assertEquals(actualVisibilityChecker, visibilityChecker);
 assertEquals(actualPropertyNamingStrategy, propertyNamingStrategy);
 assertEquals(actualTypeFactory, typeFactory);
 assertEquals(actualTypeResolverBuilder, typeResolverBuilder);
 assertEquals(actualDateFormat, dateFormat);
 assertEquals(actualHandlerInstantiator, handlerInstantiator);
 assertEquals(actualLocale, locale);
 assertEquals(actualTimeZone, timeZone);
 assertEquals(actualBase64Variant, defaultBase64);
 }

 @Test
 public void testSettingClassIntrospector() {
 ClassIntrospector newClassIntrospector = new ClassIntrospector();
 BaseSettings settingsWithNewClassIntrospector = baseSettings.withClassIntrospector(newClassIntrospector);
 assertEquals(settingsWithNewClassIntrospector.getClassIntrospector(), newClassIntrospector);
 assertEquals(settingsWithNewClassIntrospector, baseSettings);
 }

 @Test
 public void testSettingAnnotationIntrospector() {
 AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
 BaseSettings settingsWithNewAnnotationIntrospector = baseSettings.withAnnotationIntrospector(newAnnotationIntrospector);
 assertEquals(settingsWithNewAnnotationIntrospector.getAnnotationIntrospector(), newAnnotationIntrospector);
 assertEquals(settingsWithNewAnnotationIntrospector, baseSettings);
 }

 @Test
 public void testSettingVisibilityChecker() {
 PropertyAccessor propertyAccessor = PropertyAccessor.FIELD;
 Visibility visibility = Visibility.ANY;
 BaseSettings settingsWithNewVisibilityChecker = baseSettings.withVisibility(propertyAccessor, visibility);
 assertEquals(settingsWithNewVisibilityChecker.getVisibilityChecker().isVisibleForSerialization(propertyAccessor, JsonAutoDetect.None.class),
 false);
 assertEquals(settingsWithNewVisibilityChecker, baseSettings);
 }

 @Test
 public void testSettingPropertyNamingStrategy() {
 PropertyNamingStrategy newPropertyNamingStrategy = PropertyNamingStrategy.LOWER_CAMEL_CASE;
 BaseSettings settingsWithNewPropertyNamingStrategy = baseSettings.withPropertyNamingStrategy(newPropertyNamingStrategy);
 assertEquals(settingsWithNewPropertyNamingStrategy.getPropertyNamingStrategy(), newPropertyNamingStrategy);
 assertEquals(settingsWithNewPropertyNamingStrategy, baseSettings);
 }

 @Test
 public void testSettingTypeFactory() {
 TypeFactory newTypeFactory = new TypeFactory();
 BaseSettings settingsWithNewTypeFactory = baseSettings.withTypeFactory(newTypeFactory);
 assertEquals(settingsWithNewTypeFactory.getTypeFactory(), newTypeFactory);
 assertEquals(settingsWithNewTypeFactory, baseSettings);
 }

 @Test
 public void testSettingTypeResolverBuilder() {
 TypeResolverBuilder<?> newTypeResolverBuilder = new MapTypeResolverBuilder();
 BaseSettings settingsWithNewTypeResolverBuilder = baseSettings.withTypeResolverBuilder(newTypeResolverBuilder);
 assertEquals(settingsWithNewTypeResolverBuilder.getTypeResolverBuilder(), newTypeResolverBuilder);
 assertEquals(settingsWithNewTypeResolverBuilder, baseSettings);
 }

 @Test
 public void testSettingDateFormat() {
 DateFormat newDateFormat = new StdDateFormat();
 BaseSettings settingsWithNewDateFormat = baseSettings.withDateFormat(newDateFormat);
 assertNotSame(settingsWithNewDateFormat.getDateFormat(), baseSettings.getDateFormat());
 assertEquals(settingsWithNewDateFormat, baseSettings);
 }

 @Test
 public void testSettingHandlerInstantiator() {
 HandlerInstantiator newHandlerInstantiator = new HandlerInstantiator();
 BaseSettings settingsWithNewHandlerInstantiator = baseSettings.withHandlerInstantiator(newHandlerInstantiator);
 assertEquals(settingsWithNewHandlerInstantiator.getHandlerInstantiator(), newHandlerInstantiator);
 assertEquals(settingsWithNewHandlerInstantiator, baseSettings);
 }

 @Test
 public void testSettingLocale() {
 Locale newLocale = Locale.US;
 BaseSettings settingsWithNewLocale = baseSettings.with(newLocale);
 assertNotSame(settingsWithNewLocale.getLocale(), baseSettings.getLocale());
 assertEquals(settingsWithNewLocale, baseSettings);
 }

 @Test
 public void testSettingTimeZone() {
 TimeZone newTimeZone = TimeZone.getTimeZone("UTC");
 BaseSettings settingsWithNewTimeZone = baseSettings.with(newTimeZone);
 assertNotSame(settingsWithNewTimeZone.getTimeZone(), baseSettings.getTimeZone());
 assertEquals(settingsWithNewTimeZone, baseSettings);
 }

 @Test
 public void testSettingBase64Variant() {
 Base64Variant newBase64Variant = Base64Variant.BASE64_STANDARD_URL_SAFE;
 BaseSettings settingsWithNewBase64Variant = baseSettings.with(newBase64Variant);
 assertNotSame(settingsWithNewBase64Variant.getBase64Variant(), baseSettings.getBase64Variant());
 assertEquals(settingsWithNewBase64Variant, baseSettings);
 }

}