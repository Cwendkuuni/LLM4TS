package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

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
import com.fasterxml.jackson.databind.util.StdHandlerInstantiator;

public class BaseSettingsTest {

 private BaseSettings settings;
 private ClassIntrospector classIntrospectorMock;
 private AnnotationIntrospector annotationIntrospectorMock;
 private VisibilityChecker<?> visibilityCheckerMock;
 private PropertyNamingStrategy propertyNamingStrategyMock;
 private TypeFactory typeFactoryMock;
 private TypeResolverBuilder<?> typeResolverBuilderMock;
 private DateFormat dateFormatMock;
 private HandlerInstantiator handlerInstantiatorMock;
 private Locale localeMock;
 private TimeZone timeZoneMock;
 private Base64Variant base64VariantMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 classIntrospectorMock = mock(ClassIntrospector.class);
 annotationIntrospectorMock = mock(AnnotationIntrospector.class);
 visibilityCheckerMock = mock(VisibilityChecker.class);
 propertyNamingStrategyMock = mock(PropertyNamingStrategy.class);
 typeFactoryMock = mock(TypeFactory.class);
 typeResolverBuilderMock = mock(TypeResolverBuilder.class);
 dateFormatMock = mock(DateFormat.class);
 handlerInstantiatorMock = new StdHandlerInstantiator();
 localeMock = Locale.getDefault();
 timeZoneMock = TimeZone.getDefault();
 base64VariantMock = Base64Variant.STANDARD;

 settings = new BaseSettings(classIntrospectorMock, annotationIntrospectorMock, visibilityCheckerMock, propertyNamingStrategyMock, typeFactoryMock,
         typeResolverBuilderMock, dateFormatMock, handlerInstantiatorMock, localeMock, timeZoneMock, base64VariantMock);
 }

 @Test
 public void testGetClassIntrospector() {
 assertSame(classIntrospectorMock, settings.getClassIntrospector());
 }

 @Test
 public void testGetAnnotationIntrospector() {
 assertSame(annotationIntrospectorMock, settings.getAnnotationIntrospector());
 }

 @Test
 public void testGetVisibilityChecker() {
 assertSame(visibilityCheckerMock, settings.getVisibilityChecker());
 }

 @Test
 public void testGetPropertyNamingStrategy() {
 assertSame(propertyNamingStrategyMock, settings.getPropertyNamingStrategy());
 }

 @Test
 public void testGetTypeFactory() {
 assertSame(typeFactoryMock, settings.getTypeFactory());
 }

 @Test
 public void testGetTypeResolverBuilder() {
 assertSame(typeResolverBuilderMock, settings.getTypeResolverBuilder());
 }

 @Test
 public void testGetDateFormat() {
 assertSame(dateFormatMock, settings.getDateFormat());
 }

 @Test
 public void testGetHandlerInstantiator() {
 assertSame(handlerInstantiatorMock, settings.getHandlerInstantiator());
 }

 @Test
 public void testGetLocale() {
 assertSame(localeMock, settings.getLocale());
 }

 @Test
 public void testGetTimeZone() {
 assertSame(timeZoneMock, settings.getTimeZone());
 }

 @Test
 public void testGetBase64Variant() {
 assertSame(base64VariantMock, settings.getBase64Variant());
 }

 @Test
 public void testWithClassIntrospector() {
 BaseSettings newSettings = settings.withClassIntrospector(classIntrospectorMock);
 assertSame(classIntrospectorMock, newSettings.getClassIntrospector());
 assertNotSame(settings, newSettings);
 }

 @Test
 public void testWithAnnotationIntrospector() {
 BaseSettings newSettings = settings.withAnnotationIntrospector(annotationIntrospectorMock);
 assertSame(annotationIntrospectorMock, newSettings.getAnnotationIntrospector());
 assertNotSame(settings, newSettings);
 }

 @Test
 public void testWithInsertedAnnotationIntrospector() {
 AnnotationIntrospectorPair pair = AnnotationIntrospectorPair.create(annotationIntrospectorMock, classIntrospectorMock);
 BaseSettings newSettings = settings.withInsertedAnnotationIntrospector(annotationIntrospectorMock);
 assertSame(pair.getSecond(), newSettings.getAnnotationIntrospector());
 assertNotSame(settings, newSettings);
 }

 @Test
 public void testWithAppendedAnnotationIntrospector() {
 AnnotationIntrospectorPair pair = AnnotationIntrospectorPair.create(classIntrospectorMock, annotationIntrospectorMock);
 BaseSettings newSettings = settings.withAppendedAnnotationIntrospector(annotationIntrospectorMock);
 assertSame(pair.getFirst(), newSettings.getAnnotationIntrospector());
 assertNotSame(settings, newSettings);
 }

 @Test
 public void testWithVisibilityChecker() {
 VisibilityChecker<?> newVisibilityChecker = mock(VisibilityChecker.class);
 BaseSettings newSettings = settings.withVisibilityChecker(newVisibilityChecker);
 assertNotSame(visibilityCheckerMock, newVisibilityChecker);
 assertNotSame(settings, newSettings);
 }

 @Test
 public void testWithVisibility() {
 PropertyAccessor propAccessor = PropertyAccessor.FIELD;
 JsonAutoDetect.Visibility newVisibility = JsonAutoDetect.Visibility.NONE;
 BaseSettings newSettings = settings.withVisibility(propAccessor, newVisibility);
 assertNotSame(settings, newSettings);
 }

 @Test
 public void testWithPropertyNamingStrategy() {
 PropertyNamingStrategy newStrategy = mock(PropertyNamingStrategy.class);
 BaseSettings newSettings = settings.withPropertyNamingStrategy(newStrategy);
 assertNotSame(propertiesNamingStrategyMock, newStrategy);
 assertNotSame(settings, newSettings);
 }

 @Test
 public void testWithTypeFactory() {
 TypeFactory newFactory = mock(TypeFactory.class);
 BaseSettings newSettings = settings.withTypeFactory(newFactory);
 assertNotSame(typeFactoryMock, newFactory);
 assertNotSame(settings, newSettings);
 }

 @Test
 public void testWithTypeResolverBuilder() {
 TypeResolverBuilder<?> newBuilder = mock(TypeResolverBuilder.class);
 BaseSettings newSettings = settings.withTypeResolverBuilder(newBuilder);
 assertNotSame(typeResolverBuilderMock, newBuilder);
 assertNotSame(settings, newSettings);
 }

 @Test
 public void testWithDateFormat() {
 DateFormat newFormat = mock(DateFormat.class);
 BaseSettings newSettings = settings.withDateFormat(newFormat);
 assertNotSame(dateFormatMock, newFormat);
 assertNotSame(settings, newSettings);
 }

 @Test
 public void testWithHandlerInstantiator() {
 HandlerInstantiator newHandlerInstantiator = mock(HandlerInstantiator.class);
 BaseSettings newSettings = settings.withHandlerInstantiator(newHandlerInstantiator);
 assertNotSame(handlerInstantiatorMock, newHandlerInstantiator);
 assertNotSame(settings, newSettings);
 }

 @Test
 public void testWith() {
 Locale newLocale = Locale.US;
 BaseSettings newSettings = settings.with(newLocale);
 assertNotSame(localeMock, newLocale);
 assertNotSame(settings, newSettings);
 }

 @Test
 public void testWithTimeZone() {
 TimeZone newTimeZone = TimeZone.getTimeZone("UTC");
 BaseSettings newSettings = settings.with(newTimeZone);
 assertSame(newTimeZone, newSettings.getTimeZone());
 }

 @Test
 public void testWithBase64Variant() {
 Base64Variant newBase64Variant = Base64Variant.PASSTHROUGH;
 BaseSettings newSettings = settings.with(newBase64Variant);
 assertSame(newBase64Variant, newSettings.getBase64Variant());
 }
}