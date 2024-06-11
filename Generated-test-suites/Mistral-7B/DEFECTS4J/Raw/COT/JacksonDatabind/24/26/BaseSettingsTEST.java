package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.util.HandlerInstantiator;

public class BaseSettingsTest {

 private BaseSettings _baseSettings;
 private ClassIntrospector _classIntrospectorMock;
 private AnnotationIntrospector _annotationIntrospectorMock;
 private VisibilityChecker<?> _visibilityCheckerMock;
 private PropertyNamingStrategy _propertyNamingStrategyMock;
 private TypeFactory _typeFactoryMock;
 private TypeResolverBuilder<?> _typeResolverBuilderMock;
 private DateFormat _dateFormatMock;
 private HandlerInstantiator _handlerInstantiatorMock;
 private Locale _localeMock;
 private TimeZone _timeZoneMock;
 private Base64Variant _defaultBase64Mock;

 @Before
 public void setup() {
 _classIntrospectorMock = mock(ClassIntrospector.class);
 _annotationIntrospectorMock = mock(AnnotationIntrospector.class);
 _visibilityCheckerMock = mock(VisibilityChecker.class);
 _propertyNamingStrategyMock = mock(PropertyNamingStrategy.class);
 _typeFactoryMock = mock(TypeFactory.class);
 _typeResolverBuilderMock = mock(TypeResolverBuilder.class);
 _dateFormatMock = mock(DateFormat.class);
 _handlerInstantiatorMock = mock(HandlerInstantiator.class);
 _localeMock = mock(Locale.class);
 _timeZoneMock = mock(TimeZone.class);
 _defaultBase64Mock = mock(Base64Variant.class);

 _baseSettings = new BaseSettings(_classIntrospectorMock, _annotationIntrospectorMock, _visibilityCheckerMock,
 _propertyNamingStrategyMock, _typeFactoryMock, _typeResolverBuilderMock, _dateFormatMock, _handlerInstantiatorMock, _localeMock,
 _timeZoneMock, _defaultBase64Mock);
 }

 @Test
 public void testGetClassIntrospector() {
 assertSame(_classIntrospectorMock, _baseSettings.getClassIntrospector());
 }

 @Test
 public void testGetAnnotationIntrospector() {
 assertSame(_annotationIntrospectorMock, _baseSettings.getAnnotationIntrospector());
 }

 @Test
 public void testGetVisibilityChecker() {
 assertSame(_visibilityCheckerMock, _baseSettings.getVisibilityChecker());
 }

 @Test
 public void testGetPropertyNamingStrategy() {
 assertSame(_propertyNamingStrategyMock, _baseSettings.getPropertyNamingStrategy());
 }

 @Test
 public void testGetTypeFactory() {
 assertSame(_typeFactoryMock, _baseSettings.getTypeFactory());
 }

 @Test
 public void testGetTypeResolverBuilder() {
 assertSame(_typeResolverBuilderMock, _baseSettings.getTypeResolverBuilder());
 }

 @Test
 public void testGetDateFormat() {
 assertSame(_dateFormatMock, _baseSettings.getDateFormat());
 }

 @Test
 public void testGetHandlerInstantiator() {
 assertSame(_handlerInstantiatorMock, _baseSettings.getHandlerInstantiator());
 }

 @Test
 public void testGetLocale() {
 assertSame(_localeMock, _baseSettings.getLocale());
 }

 @Test
 public void testGetTimeZone() {
 assertSame(_timeZoneMock, _baseSettings.getTimeZone());
 }

 @Test
 public void testGetBase64Variant() {
 assertSame(_defaultBase64Mock, _baseSettings.getBase64Variant());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testWithNullTimeZone() {
 _baseSettings.with(null);
 }

 @Test
 public void testWithNullLocale() {
 BaseSettings settings = new BaseSettings(_classIntrospectorMock, _annotationIntrospectorMock,
 _visibilityCheckerMock, _propertyNamingStrategyMock, _typeFactoryMock, _typeResolverBuilderMock, _dateFormatMock,
 _handlerInstantiatorMock, null, _timeZoneMock, _defaultBase64Mock);
 assertNotNull(settings.getLocale());
 }

 @Test
 public void testWithDefaultLocale() {
 BaseSettings settings = new BaseSettings(_classIntrospectorMock, _annotationIntrospectorMock,
 _visibilityCheckerMock, _propertyNamingStrategyMock, _typeFactoryMock, _typeResolverBuilderMock, _dateFormatMock,
 _handlerInstantiatorMock, Locale.getDefault(), _timeZoneMock, _defaultBase64Mock);
 assertSame(Locale.getDefault(), settings.getLocale());
 }

 @Test
 public void testWithTimeZone() {
 TimeZone timeZone = mock(TimeZone.class);
 BaseSettings settings = new BaseSettings(_classIntrospectorMock, _annotationIntrospectorMock,
 _visibilityCheckerMock, _propertyNamingStrategyMock, _typeFactoryMock, _typeResolverBuilderMock, _dateFormatMock,
 _handlerInstantiatorMock, _localeMock, timeZone, _defaultBase64Mock);
 when(_dateFormatMock.getTimeZone()).thenReturn(timeZone);
 assertSame(timeZone, settings.getTimeZone());
 }

 @Test
 public void testWithDateFormat() {
 DateFormat dateFormat = mock(DateFormat.class);
 BaseSettings settings = new BaseSettings(_classIntrospectorMock, _annotationIntrospectorMock,
 _visibilityCheckerMock, _propertyNamingStrategyMock, _typeFactoryMock, _typeResolverBuilderMock, dateFormat,
 _handlerInstantiatorMock, _localeMock, _timeZoneMock, _defaultBase64Mock);
 when(dateFormat.getTimeZone()).thenReturn(_timeZoneMock);
 assertSame(dateFormat, settings.getDateFormat());
 }

 @Test
 public void testWithBase64Variant() {
 BaseSettings settings = new BaseSettings(_classIntrospectorMock, _annotationIntrospectorMock,
 _visibilityCheckerMock, _propertyNamingStrategyMock, _typeFactoryMock, _typeResolverBuilderMock, _dateFormatMock,
 _handlerInstantiatorMock, _localeMock, _timeZoneMock, _defaultBase64Mock);
 Base64Variant base64 = mock(Base64Variant.class);
 BaseSettings newSettings = settings.with(base64);
 assertNotSame(settings, newSettings);
 assertSame(base64, newSettings.getBase64Variant());
 }
}