
package com.fasterxml.jackson.databind.cfg;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class BaseSettingsTest {

  @Test
  public void testWithClassIntrospector() {
    // Arrange
    ClassIntrospector ci = new ClassIntrospector();
    AnnotationIntrospector ai = new AnnotationIntrospector();
    VisibilityChecker<?> vc = new VisibilityChecker<>();
    PropertyNamingStrategy pns = new PropertyNamingStrategy();
    TypeFactory tf = new TypeFactory();
    TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
    DateFormat df = new StdDateFormat();
    HandlerInstantiator hi = new HandlerInstantiator();
    Locale locale = Locale.US;
    TimeZone tz = TimeZone.getDefault();
    Base64Variant defaultBase64 = new Base64Variant();

    BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, df, hi, locale, tz, defaultBase64);

    // Act
    BaseSettings result = baseSettings.withClassIntrospector(new ClassIntrospector());

    // Assert
    Assert.assertNotEquals(baseSettings, result);
    Assert.assertEquals(new ClassIntrospector(), result.getClassIntrospector());
  }

  @Test
  public void testWithAnnotationIntrospector() {
    // Arrange
    ClassIntrospector ci = new ClassIntrospector();
    AnnotationIntrospector ai = new AnnotationIntrospector();
    VisibilityChecker<?> vc = new VisibilityChecker<>();
    PropertyNamingStrategy pns = new PropertyNamingStrategy();
    TypeFactory tf = new TypeFactory();
    TypeResolverBuilder<?> typer = new TypeResolverBuilder<>();
    DateFormat df = new StdDateFormat();
    HandlerInstantiator hi = new HandlerInstantiator();
    Locale locale = Locale.US;
    TimeZone tz = TimeZone.getDefault();
    Base64Variant defaultBase64 = new Base64Variant();

    BaseSettings baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, df, hi, locale, tz, defaultBase64);

    // Act
    BaseSettings result = baseSettings.withAnnotationIntrospector(new AnnotationIntrospector());

    // Assert
    Assert.assertNotEquals(baseSettings, result);
    Assert.assertEquals(new AnnotationIntrospector(), result.getAnnotationIntrospector());
  }

  // Rest of the tests for other methods go here...

}

