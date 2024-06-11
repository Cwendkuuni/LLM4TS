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
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

/**
 * Tests for {@link BaseSettings} class
 */
public class BaseSettingsTest {

 private BaseSettings baseSettings;

 @Before
 public void setup() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker<>();
 PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy() {
 // empty implementation for testing
 };
 TypeFactory typeFactory = new TypeFactory();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilder();
 DateFormat dateFormat = new StdDateFormat();
 HandlerInstantiator handlerInstantiator = new HandlerInstantiator() {
 // empty implementation for testing
 };
 Locale locale = Locale.getDefault();
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant base64 = Base64Variant.STANDARD;
 baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
 typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, base64);
 }

 @Test
 public void testSettersAndGetters() {
 // test getters and setters of BaseSettings fields
 ClassIntrospector ci = baseSettings.getClassIntrospector();
 assertNotNull(ci);
 AnnotationIntrospector ai = baseSettings.getAnnotationIntrospector();
 assertNotNull(ai);
 VisibilityChecker<?> vc = baseSettings.getVisibilityChecker();
 assertNotNull(vc);
 PropertyNamingStrategy pns = baseSettings.getPropertyNamingStrategy();
 assertNotNull(pns);
 TypeFactory tf = baseSettings.getTypeFactory();
 assertNotNull(tf);
 TypeResolverBuilder<?> typer = baseSettings.getTypeResolverBuilder();
 assertNotNull(typer);
 DateFormat df = baseSettings.getDateFormat();
 assertNotNull(df);
 HandlerInstantiator hi = baseSettings.getHandlerInstantiator();
 assertNotNull(hi);
 Locale locale = baseSettings.getLocale();
 assertNotNull(locale);
 TimeZone timeZone = baseSettings.getTimeZone();
 assertNotNull(timeZone);
 Base64Variant base64 = baseSettings.getBase64Variant();
 assertNotNull(base64);
 // test setters
 baseSettings = baseSettings.withClassIntrospector(new ClassIntrospector());
 assertNotSame(classIntrospector, baseSettings.getClassIntrospector());
 assertSame(annotationIntrospector, baseSettings.getAnnotationIntrospector());
 baseSettings = baseSettings.withAnnotationIntrospector(new AnnotationIntrospector());
 assertSame(classIntrospector, baseSettings.getClassIntrospector());
 assertNotSame(annotationIntrospector, baseSettings.getAnnotationIntrospector());
 baseSettings = baseSettings.withInsertedAnnotationIntrospector(new AnnotationIntrospector());
 baseSettings = baseSettings.withAppendedAnnotationIntrospector(new AnnotationIntrospector());
 baseSettings = baseSettings.withVisibilityChecker(new VisibilityChecker<Object>() {
 // empty implementation for testing
 });
 assertNotSame(visibilityChecker, baseSettings.getVisibilityChecker());
 baseSettings = baseSettings.withVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NONE);
 assertSame(visibilityChecker, baseSettings.getVisibilityChecker());
 baseSettings = baseSettings.withPropertyNamingStrategy(new PropertyNamingStrategy() {
 // empty implementation for testing
 });
 assertNotSame(pns, baseSettings.getPropertyNamingStrategy());
 baseSettings = baseSettings.withTypeFactory(new TypeFactory());
 assertNotSame(tf, baseSettings.getTypeFactory());
 baseSettings = baseSettings.withTypeResolverBuilder(new TypeResolverBuilder());
 assertNotSame(typer, baseSettings.getTypeResolverBuilder());
 baseSettings = baseSettings.withDateFormat(null);
 assertNotSame(df, baseSettings.getDateFormat());
 baseSettings = baseSettings.withHandlerInstantiator(new HandlerInstantiator() {
 // empty implementation for testing
 });
 assertNotSame(hi, baseSettings.getHandlerInstantiator());
 baseSettings = baseSettings.with(Locale.US);
 assertNotSame(locale, baseSettings.getLocale());
 baseSettings = baseSettings.with(TimeZone.getTimeZone("UTC"));
 assertNotSame(timeZone, baseSettings.getTimeZone());
 baseSettings = baseSettings.with(Base64Variant.BASE64_URL_SAFE);
 assertNotSame(base64, baseSettings.getBase64Variant());
 }

 @Test
 public void testToString() {
 String toString = baseSettings.toString();
 assertNotNull(toString);
 }
}