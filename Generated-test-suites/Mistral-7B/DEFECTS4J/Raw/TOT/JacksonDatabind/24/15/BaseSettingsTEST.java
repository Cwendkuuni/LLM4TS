package com.fasterxml.jackson.databind.cfg;

import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSettingsTest {

 private BaseSettings baseSettings;

 @Test
 public void testBaseSettingsConstruction() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.With();
 PropertyNamingStrategy propertyNamingStrategy = new DefaultPropertyNamingStrategy();
 TypeFactory typeFactory = new TypeFactoryBuilder().build();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 HandlerInstantiator handlerInstantiator = new HandlerInstanciator();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variants.STANDARD;

 baseSettings = new BaseSettings(classIntrospector, annotationIntrospector, visibilityChecker, propertyNamingStrategy, typeFactory,
 typeResolverBuilder, dateFormat, handlerInstantiator, locale, timeZone, defaultBase64);

 assertNotNull(baseSettings);
 assertSame(classIntrospector, baseSettings.getClassIntrospector());
 assertSame(annotationIntrospector, baseSettings.getAnnotationIntrospector());
 assertSame(visibilityChecker, baseSettings.getVisibilityChecker());
 assertSame(propertyNamingStrategy, baseSettings.getPropertyNamingStrategy());
 assertSame(typeFactory, baseSettings.getTypeFactory());
 assertSame(typeResolverBuilder, baseSettings.getTypeResolverBuilder());
 assertSame(dateFormat, baseSettings.getDateFormat());
 assertSame(handlerInstantiator, baseSettings.getHandlerInstantiator());
 assertSame(locale, baseSettings.getLocale());
 assertSame(timeZone, baseSettings.getTimeZone());
 assertSame(defaultBase64, baseSettings.getBase64Variant());
 }

 @Test
 public void testBaseSettingsWithClassIntrospector() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.With();
 PropertyNamingStrategy propertyNamingStrategy = new DefaultPropertyNamingStrategy();
 TypeFactory typeFactory = new TypeFactoryBuilder().build();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 HandlerInstantiator handlerInstantiator = new HandlerInstanciator();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variants.STANDARD;

 BaseSettings baseSettings = new BaseSettings(baseSettings.withClassIntrospector(classIntrospector));

 assertSame(classIntrospector, baseSettings.getClassIntrospector());
 }

 @Test
 public void testBaseSettingsWithAnnotationIntrospector() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.With();
 PropertyNamingStrategy propertyNamingStrategy = new DefaultPropertyNamingStrategy();
 TypeFactory typeFactory = new TypeFactoryBuilder().build();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 HandlerInstantiator handlerInstantiator = new HandlerInstanciator();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variants.STANDARD;

 BaseSettings baseSettings = new BaseSettings(baseSettings.withAnnotationIntrospector(newAnnotationIntrospector));

 assertNotSame(annotationIntrospector, baseSettings.getAnnotationIntrospector());
 assertSame(newAnnotationIntrospector, baseSettings.getAnnotationIntrospector());
 }

 @Test
 public void testBaseSettingsWithVisibilityChecker() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.With();
 VisibilityChecker<?> newVisibilityChecker = new VisibilityChecker.With("FIELD", JsonAutoDetect.Visibility.NONE);
 PropertyNamingStrategy propertyNamingStrategy = new DefaultPropertyNamingStrategy();
 TypeFactory typeFactory = new TypeFactoryBuilder().build();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 HandlerInstantiator handlerInstantiator = new HandlerInstanciator();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variants.STANDARD;

 BaseSettings baseSettings = new BaseSettings(baseSettings.withVisibilityChecker(newVisibilityChecker));

 assertNotSame(visibilityChecker, baseSettings.getVisibilityChecker());
 assertSame(newVisibilityChecker, baseSettings.getVisibilityChecker());
 }

 @Test
 public void testBaseSettingsWithPropertyNamingStrategy() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.With();
 PropertyNamingStrategy propertyNamingStrategy = new DefaultPropertyNamingStrategy();
 PropertyNamingStrategy newPropertyNamingStrategy = new PropertyNamingStrategy() {
 @Override public String nameForProperty(BeanProperty property) {
 return property.getName().toUpperCase();
 }
 };
 TypeFactory typeFactory = new TypeFactoryBuilder().build();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 HandlerInstantiator handlerInstantiator = new HandlerInstanciator();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variants.STANDARD;

 BaseSettings baseSettings = new BaseSettings(baseSettings.withPropertyNamingStrategy(newPropertyNamingStrategy));

 assertNotSame(propertyNamingStrategy, baseSettings.getPropertyNamingStrategy());
 assertSame(newPropertyNamingStrategy, baseSettings.getPropertyNamingStrategy());
 }

 @Test
 public void testBaseSettingsWithTypeFactory() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.With();
 PropertyNamingStrategy propertyNamingStrategy = new DefaultPropertyNamingStrategy();
 TypeFactory typeFactory = new TypeFactoryBuilder().build();
 TypeFactory newTypeFactory = new TypeFactoryBuilder().build();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 HandlerInstantiator handlerInstantiator = new HandlerInstanciator();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variants.STANDARD;

 BaseSettings baseSettings = new BaseSettings(baseSettings.withTypeFactory(newTypeFactory));

 assertNotSame(typeFactory, baseSettings.getTypeFactory());
 assertSame(newTypeFactory, baseSettings.getTypeFactory());
 }

 @Test
 public void testBaseSettingsWithTypeResolverBuilder() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.With();
 PropertyNamingStrategy propertyNamingStrategy = new DefaultPropertyNamingStrategy();
 TypeFactory typeFactory = new TypeFactoryBuilder().build();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
 TypeResolverBuilder<?> newTypeResolverBuilder = new TypeResolverBuilderDefault();
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 HandlerInstantiator handlerInstantiator = new HandlerInstanciator();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variants.STANDARD;

 BaseSettings baseSettings = new BaseSettings(baseSettings.withTypeResolverBuilder(newTypeResolverBuilder));

 assertNotSame(typeResolverBuilder, baseSettings.getTypeResolverBuilder());
 assertSame(newTypeResolverBuilder, baseSettings.getTypeResolverBuilder());
 }

 @Test
 public void testBaseSettingsWithDateFormat() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.With();
 PropertyNamingStrategy propertyNamingStrategy = new DefaultPropertyNamingStrategy();
 TypeFactory typeFactory = new TypeFactoryBuilder().build();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 DateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss 'at' z");
 HandlerInstantiator handlerInstantiator = new HandlerInstanciator();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variants.STANDARD;

 BaseSettings baseSettings = new BaseSettings(baseSettings.withDateFormat(newDateFormat));

 assertNotSame(dateFormat, baseSettings.getDateFormat());
 assertSame(newDateFormat, baseSettings.getDateFormat());
 }

 @Test
 public void testBaseSettingsWithHandlerInstantiator() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.With();
 PropertyNamingStrategy propertyNamingStrategy = new DefaultPropertyNamingStrategy();
 TypeFactory typeFactory = new TypeFactoryBuilder().build();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 HandlerInstantiator newHandlerInstantiator = new HandlerInstanciator();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variants.STANDARD;

 BaseSettings baseSettings = new BaseSettings(baseSettings.withHandlerInstantiator(newHandlerInstantiator));

 assertNotSame(handlerInstantiator, baseSettings.getHandlerInstantiator());
 assertSame(newHandlerInstantiator, baseSettings.getHandlerInstantiator());
 }

 @Test
 public void testBaseSettingsLocale() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.With();
 PropertyNamingStrategy propertyNamingStrategy = new DefaultPropertyNamingStrategy();
 TypeFactory typeFactory = new TypeFactoryBuilder().build();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 HandlerInstantiator handlerInstantiator = new HandlerInstanciator();
 Locale locale = Locale.FRANCE;
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant defaultBase64 = Base64Variants.STANDARD;

 BaseSettings baseSettings = new BaseSettings(baseSettings.with(locale));

 assertNotSame(Locale.US, baseSettings.getLocale());
 assertSame(locale, baseSettings.getLocale());
 }

 @Test
 public void testBaseSettingsTimeZone() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.With();
 PropertyNamingStrategy propertyNamingStrategy = new DefaultPropertyNamingStrategy();
 TypeFactory typeFactory = new TypeFactoryBuilder().build();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 HandlerInstantiator handlerInstantiator = new HandlerInstanciator();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getTimeZone("UTC");
 Base64Variant defaultBase64 = Base64Variants.STANDARD;

 BaseSettings baseSettings = new BaseSettings(baseSettings.with(timeZone));

 assertNotSame(TimeZone.getDefault(), baseSettings.getTimeZone());
 assertSame(timeZone, baseSettings.getTimeZone());
 }

 @Test
 public void testBaseSettingsBase64Variant() {
 ClassIntrospector classIntrospector = new ClassIntrospector();
 AnnotationIntrospector annotationIntrospector = new AnnotationIntrospector();
 VisibilityChecker<?> visibilityChecker = new VisibilityChecker.With();
 PropertyNamingStrategy propertyNamingStrategy = new DefaultPropertyNamingStrategy();
 TypeFactory typeFactory = new TypeFactoryBuilder().build();
 TypeResolverBuilder<?> typeResolverBuilder = new TypeResolverBuilderDefault();
 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 HandlerInstantiator handlerInstantiator = new HandlerInstanciator();
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getDefault();
 Base64Variant newBase64Variant = Base64Variants.BASE64_URL_SAFE;

 BaseSettings baseSettings = new BaseSettings(baseSettings.with(newBase64Variant));

 assertNotSame(Base64Variants.STANDARD, baseSettings.getBase64Variant());
 assertSame(newBase64Variant, baseSettings.getBase64Variant());
 }
}