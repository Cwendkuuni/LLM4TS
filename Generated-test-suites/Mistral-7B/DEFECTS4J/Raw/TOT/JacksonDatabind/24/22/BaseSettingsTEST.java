package com.fasterxml.jackson.databind.cfg;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BaseSettingsTest {

 private BaseSettings settings;

 @Before
 public void setUp() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.Std();
 PropertyNamingStrategy propertyNamingStrategy = new PropertyNamingStrategy.Default();
 TypeFactory typeFactory = new TypeFactory();
 TypeResolverBuilder<?> typeResolverBuilder = new SimpleTypeResolverBuilder();
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 HandlerInstantiator handlerInstantiator = new HandlerInstantiator();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 Base64Variant base64Variant = Base64Variants.getDefaultVariant();
 settings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
 typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, base64Variant);
 }

 @Test
 public void testGetters() {
 ClassIntrospector expectedClassIntrospector = new ClassIntrospector();
 AnnotationIntrospector expectedAnnotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> expectedVisibilityChecker = new VisibilityChecker.Std();
 PropertyNamingStrategy expectedPropertyNamingStrategy = new PropertyNamingStrategy.Default();
 TypeFactory expectedTypeFactory = new TypeFactory();
 TypeResolverBuilder<?> expectedTypeResolverBuilder = new SimpleTypeResolverBuilder();
 DateFormat expectedDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 HandlerInstantiator expectedHandlerInstantiator = new HandlerInstantiator();
 Locale expectedLocale = Locale.US;
 TimeZone expectedTimeZone = TimeZone.getTimeZone("UTC");
 Base64Variant expectedBase64Variant = Base64Variants.getDefaultVariant();

 ClassIntrospector actualClassIntrospector = settings.getClassIntrospector();
 AnnotationIntrospector actualAnnotationIntrospector = settings.getAnnotationIntrospector();
 VisibilityChecker<?> actualVisibilityChecker = settings.getVisibilityChecker();
 PropertyNamingStrategy actualPropertyNamingStrategy = settings.getPropertyNamingStrategy();
 TypeFactory actualTypeFactory = settings.getTypeFactory();
 TypeResolverBuilder<?> actualTypeResolverBuilder = settings.getTypeResolverBuilder();
 DateFormat actualDateFormat = settings.getDateFormat();
 HandlerInstantiator actualHandlerInstantiator = settings.getHandlerInstantiator();
 Locale actualLocale = settings.getLocale();
 TimeZone actualTimeZone = settings.getTimeZone();
 Base64Variant actualBase64Variant = settings.getBase64Variant();

 assertEquals(expectedClassIntrospector, actualClassIntrospector);
 assertEquals(expectedAnnotationIntrospector, actualAnnotationIntrospector);
 assertEquals(expectedVisibilityChecker, actualVisibilityChecker);
 assertEquals(expectedPropertyNamingStrategy, actualPropertyNamingStrategy);
 assertEquals(expectedTypeFactory, actualTypeFactory);
 assertEquals(expectedTypeResolverBuilder, actualTypeResolverBuilder);
 assertEquals(expectedDateFormat, actualDateFormat);
 assertEquals(expectedHandlerInstantiator, actualHandlerInstantiator);
 assertEquals(expectedLocale, actualLocale);
 assertEquals(expectedTimeZone, actualTimeZone);
 assertEquals(expectedBase64Variant, actualBase64Variant);
 }

 @Test
 public void testSetters() {
 ClassIntrospector newClassIntrospector = new ClassIntrospector();
 AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> newVisibilityChecker = new VisibilityChecker.Std();
 PropertyNamingStrategy newPropertyNamingStrategy = new PropertyNamingStrategy.Default();
 TypeFactory newTypeFactory = new TypeFactory();
 TypeResolverBuilder<?> newTypeResolverBuilder = new SimpleTypeResolverBuilder();
 DateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 HandlerInstantiator newHandlerInstantiator = new HandlerInstantiator();
 Locale newLocale = Locale.FRANCE;
 TimeZone newTimeZone = TimeZone.getTimeZone("Europe/Paris");
 Base64Variant newBase64Variant = Base64Variants.US_BASE64;

 BaseSettings settingsCopy = settings.copy();
 settingsCopy = settingsCopy.withClassIntrospector(newClassIntrospector)
 .withAnnotationIntrospector(newAnnotationIntrospector)
 .withVisibilityChecker(newVisibilityChecker)
 .withPropertyNamingStrategy(newPropertyNamingStrategy)
 .withTypeFactory(newTypeFactory)
 .withTypeResolverBuilder(newTypeResolverBuilder)
 .withDateFormat(newDateFormat)
 .withHandlerInstantiator(newHandlerInstantiator)
 .withLocale(newLocale)
 .withTimeZone(newTimeZone)
 .withBase64Variant(newBase64Variant);

 ClassIntrospector expectedNewClassIntrospector = newClassIntrospector;
 AnnotationIntrospector expectedNewAnnotationIntrospector = newAnnotationIntrospector;
 VisibilityChecker<?> expectedNewVisibilityChecker = newVisibilityChecker;
 PropertyNamingStrategy expectedNewPropertyNamingStrategy = newPropertyNamingStrategy;
 TypeFactory expectedNewTypeFactory = newTypeFactory;
 TypeResolverBuilder<?> expectedNewTypeResolverBuilder = newTypeResolverBuilder;
 DateFormat expectedNewDateFormat = newDateFormat;
 HandlerInstantiator expectedNewHandlerInstantiator = newHandlerInstantiator;
 Locale expectedNewLocale = newLocale;
 TimeZone expectedNewTimeZone = newTimeZone;
 Base64Variant expectedNewBase64Variant = newBase64Variant;

 ClassIntrospector actualNewClassIntrospector = settingsCopy.getClassIntrospector();
 AnnotationIntrospector actualNewAnnotationIntrospector = settingsCopy.getAnnotationIntrospector();
 VisibilityChecker<?> actualNewVisibilityChecker = settingsCopy.getVisibilityChecker();
 PropertyNamingStrategy actualNewPropertyNamingStrategy = settingsCopy.getPropertyNamingStrategy();
 TypeFactory actualNewTypeFactory = settingsCopy.getTypeFactory();
 TypeResolverBuilder<?> actualNewTypeResolverBuilder = settingsCopy.getTypeResolverBuilder();
 DateFormat actualNewDateFormat = settingsCopy.getDateFormat();
 HandlerInstantantiator actualNewHandlerInstantiator = settingsCopy.getHandlerInstantiator();
 Locale actualNewLocale = settingsCopy.getLocale();
 TimeZone actualNewTimeZone = settingsCopy.getTimeZone();
 Base64Variant actualNewBase64Variant = settingsCopy.getBase64Variant();

 assertEquals(expectedNewClassIntrospector, actualNewClassIntrospector);
 assertEquals(expectedNewAnnotationIntrospector, actualNewAnnotationIntrospector);
 assertEquals(expectedNewVisibilityChecker, actualNewVisibilityChecker);
 assertEquals(expectedNewPropertyNamingStrategy, actualNewPropertyNamingStrategy);
 assertEquals(expectedNewTypeFactory, actualNewTypeFactory);
 assertEquals(expectedNewTypeResolverBuilder, actualNewTypeResolverBuilder);
 assertEquals(expectedNewDateFormat, actualNewDateFormat);
 assertEquals(expectedNewHandlerInstantiator, actualNewHandlerInstantiator);
 assertEquals(expectedNewLocale, actualNewLocale);
 assertEquals(expectedNewTimeZone, actualNewTimeZone);
 assertEquals(expectedNewBase64Variant, actualNewBase64Variant);
 }

 @Test
 public void testClone() {
 BaseSettings original = settings;
 BaseSettings cloned = original.clone();

 ClassIntrospector actualClassIntrospector = cloned.getClassIntrospector();
 ClassIntrospector expectedClassIntrospector = original.getClassIntrospector();
 assertEquals(expectedClassIntrospector, actualClassIntrospector);
 }
}