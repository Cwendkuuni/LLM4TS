package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.util.HandlerInstantiator;

public class BaseSettingsTest {

 private BaseSettings original;
 private BaseSettings settingsWithCustomClassIntrospector;
 private BaseSettings settingsWithCustomAnnotationIntrospector;
 private BaseSettings settingsWithCustomVisibilityChecker;
 private BaseSettings settingsWithCustomPropertyNamingStrategy;
 private BaseSettings settingsWithCustomTypeFactory;
 private BaseSettings settingsWithCustomTypeResolverBuilder;
 private BaseSettings settingsWithCustomDateFormat;
 private BaseSettings settingsWithCustomHandlerInstantiator;
 private BaseSettings settingsWithCustomLocale;
 private BaseSettings settingsWithCustomTimeZone;
 private BaseSettings settingsWithCustomBase64;

 @Before
 public void setUp() throws Exception {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.Default();
 PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.DEFAULT();
 TypeFactory typeFactory = new TypeFactory();
 TypeResolverBuilder<?> typeResolverBuilder = new SimpleTypeResolverBuilder();
 DateFormat dateFormat = new StdDateFormat();
 HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
 Locale locale = Locale.getDefault();
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variant.STANDARD;
 original = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
 typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);
 }

 @Test
 public void test_getters_return_correctly_initialized_values() {
 assertSame("getClassIntrospector should match initialization", classIntrospector, original.getClassIntrospector());
 assertSame("getAnnotationIntrospector should match initialization", annotationIntrospector, original.getAnnotationIntrospector());
 assertSame("getVisibilityChecker should match initialization", visibilityChecker, original.getVisibilityChecker());
 assertSame("getPropertyNamingStrategy should match initialization", propertyNamingStrategy, original.getPropertyNamingStrategy());
 assertSame("getTypeFactory should match initialization", typeFactory, original.getTypeFactory());
 assertSame("getTypeResolverBuilder should match initialization", typeResolverBuilder, original.getTypeResolverBuilder());
 assertSame("getDateFormat should match initialization", dateFormat, original.getDateFormat());
 assertSame("getHandlerInstantiator should match initialization", handlerInstantiator, original.getHandlerInstantiator());
 assertSame("getLocale should match initialization", locale, original.getLocale());
 assertSame("getTimeZone should match initialization", timeZone, original.getTimeZone());
 assertSame("getDefaultBase64 should match initialization", defaultBase64, original.getBase64Variant());
 }

 @Test
 public void test_constructors() {
 settingsWithCustomClassIntrospector = original.withClassIntrospector(new ClassIntrospector());
 settingsWithCustomAnnotationIntrospector = original.withAnnotationIntrospector(new AnnotationIntrospector());
 settingsWithCustomVisibilityChecker = original.withVisibilityChecker(new VisibilityChecker.WithSerializedNameAnnotation());
 settingsWithCustomPropertyNamingStrategy = original.withPropertyNamingStrategy(new PropertyNamingStrategy.LowerCaseAnnotation());
 settingsWithCustomTypeFactory = original.withTypeFactory(new TypeFactory());
 settingsWithCustomTypeResolverBuilder = original.withTypeResolverBuilder(new SimpleTypeResolverBuilder());
 settingsWithCustomDateFormat = original.withDateFormat(new StdDateFormat());
 settingsWithCustomHandlerInstantiator = original.withHandlerInstantiator(new HandlerInstantiator());
 settingsWithCustomLocale = original.with(Locale.US);
 settingsWithCustomTimeZone = original.with(TimeZone.getTimeZone("UTC"));
 settingsWithCustomBase64 = original.with(Base64Variant.BASE64_EXTENDED);
 }

 @Test
 public void test_getters_return_correct_values_after_construction() {
 assertNotSame("getClassIntrospector should not match initialization", classIntrospector, settingsWithCustomClassIntrospector.getClassIntrospector());
 assertNotSame("getAnnotationIntrospector should not match initialization", annotationIntrospector, settingsWithCustomAnnotationIntrospector.getAnnotationIntrospector());
 assertNotSame("getVisibilityChecker should not match initialization", visibilityChecker, settingsWithCustomVisibilityChecker.getVisibilityChecker());
 assertNotSame("getPropertyNamingStrategy should not match initialization", propertyNamingStrategy, settingsWithCustomPropertyNamingStrategy.getPropertyNamingStrategy());
 assertNotSame("getTypeFactory should not match initialization", typeFactory, settingsWithCustomTypeFactory.getTypeFactory());
 assertNotSame("getTypeResolverBuilder should not match initialization", typeResolverBuilder, settingsWithCustomTypeResolverBuilder.getTypeResolverBuilder());
 assertNotSame("getDateFormat should not match initialization", dateFormat, settingsWithCustomDateFormat.getDateFormat());
 assertNotSame("getHandlerInstantiator should not match initialization", handlerInstantiator, settingsWithCustomHandlerInstantiator.getHandlerInstantiator());
 assertNotSame("getLocale should not match initialization", locale, settingsWithCustomLocale.getLocale());
 assertNotSame("getTimeZone should not match initialization", timeZone, settingsWithCustomTimeZone.getTimeZone());
 assertNotSame("getDefaultBase64 should not match initialization", defaultBase64, settingsWithCustomBase64.getBase64Variant());
 }

}