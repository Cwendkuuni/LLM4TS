package com.fasterxml.jackson.databind.cfg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.time.ZoneId;
import java.util.Locale;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ClassIntrospector;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector.BaseAnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker.StdGetter;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker.StdIsGetter;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.databind.util.StdSerializer;

public class BaseSettingsTest {

 private BaseSettings baseSettings;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 ClassIntrospector ci = new ClassIntrospector(new AnnotationIntrospector());
 AnnotationIntrospector ai = new AnnotationIntrospector();
 VisibilityChecker<Object> vc = new VisibilityChecker.Std() {
 @Override
 public boolean isGetter(FieldDescriptor fd) {
 return StdGetter.class.isAssignableFrom(fd.getMember().getDeclaringClass());
 }

 @Override
 public boolean isSetter(FieldDescriptor fd) {
 return StdIsGetter.class.isAssignableFrom(fd.getMember().getDeclaringClass());
 }
 };
 PropertyNamingStrategy pns = new PropertyNamingStrategy.Default();
 TypeFactory tf = new TypeFactory();
 TypeResolverBuilder<?> typer = new TypeResolverBuilder() {
 @SuppressWarnings("unchecked")
 @Override
 public <T> JavaType constructType(Class<?> rawType, Class<?> containerClass, AnnotationMetadata annotations)
 throws JsonMappingException {
 return super.constructType(rawType, containerClass, annotations);
 }
 };
 DateFormat df = new StdDateFormat();
 HandlerInstantiator hi = new HandlerInstantiator() {
 @Override
 public Object createHandler(DeserializationConfig config, BeanDescription beanDesc)
 throws JsonMappingException, InstantiationException {
 return new StdSerializer<Object>(Object.class) {};
 }

 @Override
 public Object createHandler(DeserializationConfig config, JavaType type)
 throws JsonMappingException, InstantiationException {
 return new StdSerializer<Object>(type) {};
 }

 @Override
 public Object createHandler(DeserializationConfig config, TypeDescriptor descriptor)
 throws JsonMappingException, InstantiationException {
 return new StdSerializer<Object>(descriptor.getType()) {};
 }
 };
 Locale locale = Locale.US;
 TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of("Europe/Paris"));
 Base64Variant base64 = Base64Variant.STANDARD;
 baseSettings = new BaseSettings(ci, ai, vc, pns, tf, typer, df, hi, locale, timeZone, base64);
 }

 @Test
 public void testGettersAndSetters() {
 assertEquals(ci, baseSettings.getClassIntrospector());
 assertEquals(ai, baseSettings.getAnnotationIntrospector());
 assertEquals(vc, baseSettings.getVisibilityChecker());
 assertEquals(pns, baseSettings.getPropertyNamingStrategy());
 assertEquals(tf, baseSettings.getTypeFactory());
 assertEquals(typer, baseSettings.getTypeResolverBuilder());
 assertEquals(df, baseSettings.getDateFormat());
 assertEquals(hi, baseSettings.getHandlerInstantiator());
 assertEquals(locale, baseSettings.getLocale());
 assertEquals(timeZone, baseSettings.getTimeZone());
 assertEquals(base64, baseSettings.getBase64Variant());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullLocale() {
 baseSettings = new BaseSettings(baseSettings.getClassIntrospector(), baseSettings.getAnnotationIntrospector(),
 baseSettings.getVisibilityChecker(), baseSettings.getPropertyNamingStrategy(), baseSettings.getTypeFactory(),
 baseSettings.getTypeResolverBuilder(), null, baseSettings.getHandlerInstantiator(), null, null, null);
 }

 @Test
 public void testCloneDateFormat() {
 DateFormat dfClone = baseSettings.withDateFormat(null);
 assertNotNull(dfClone);
 }

 @Test
 public void testChangeDateFormatTimeZone() {
 DateFormat df = new StdDateFormat();
 df.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
 baseSettings = baseSettings.withDateFormat(df);
 assertEquals(df, baseSettings.getDateFormat());
 }

 @Test
 public void testChangeTimeZone() {
 TimeZone newTimeZone = TimeZone.getTimeZone(ZoneId.of("Europe/Moscow"));
 baseSettings = baseSettings.with(newTimeZone);
 assertEquals(newTimeZone, baseSettings.getTimeZone());
 }

 @Test
 public void testChangeBase64Variant() {
 Base64Variant newBase64Variant = Base64Variant.BASE64_EXTENDED;
 baseSettings = baseSettings.with(newBase64Variant);
 assertEquals(newBase64Variant, baseSettings.getBase64Variant());
 }

 @Test
 public void testChangeAnnotatationIntrospector() {
 AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
 baseSettings = baseSettings.withAnnotationIntrospector(newAnnotationIntrospector);
 assertEquals(newAnnotationIntrospector, baseSettings.getAnnotationIntrospector());
 }

 @Test
 public void testChangePropertyNamingStrategy() {
 PropertyNamingStrategy newPropertyNamingStrategy = new PropertyNamingStrategy.LowerSnakeCase();
 baseSettings = baseSettings.withPropertyNamingStrategy(newPropertyNamingStrategy);
 assertEquals(newPropertyNamingStrategy, baseSettings.getPropertyNamingStrategy());
 }

 @Test
 public void testChangeTypeFactory() {
 TypeFactory newTypeFactory = new TypeFactory();
 baseSettings = baseSettings.withTypeFactory(newTypeFactory);
 assertEquals(newTypeFactory, baseSettings.getTypeFactory());
 }

 @Test
 public void testChangeTypeResolverBuilder() {
 TypeResolverBuilder<?> newTypeResolverBuilder = new TypeResolverBuilder() {
 @SuppressWarnings("unchecked")
 @Override
 public <T> JavaType constructType(Class<?> rawType, Class<?> containerClass, AnnotationMetadata annotations)
 throws JsonMappingException {
 return super.constructType(rawType, containerClass, annotations);
 }
 };
 baseSettings = baseSettings.withTypeResolverBuilder(newTypeResolverBuilder);
 assertEquals(newTypeResolverBuilder, baseSettings.getTypeResolverBuilder());
 }

 @Test
 public void testChangeClassIntrospector() {
 ClassIntrospector newClassIntrospector = new ClassIntrospector(new AnnotationIntrospector());
 baseSettings = baseSettings.withClassIntrospector(newClassIntrospector);
 assertEquals(newClassIntrospector, baseSettings.getClassIntrospector());
 }

 @Test
 public void testChangeVisibilityChecker() {
 VisibilityChecker<Object> newVisibilityChecker = new VisibilityChecker.Std() {
 @Override
 public boolean isGetter(FieldDescriptor fd) {
 return StdGetter.class.isAssignableFrom(fd.getMember().getDeclaringClass());
 }

 @Override
 public boolean isSetter(FieldDescriptor fd) {
 return StdIsGetter.class.isAssignableFrom(fd.getMember().getDeclaringClass());
 }
 };
 baseSettings = baseSettings.withVisibilityChecker(newVisibilityChecker);
 assertEquals(newVisibilityChecker, baseSettings.getVisibilityChecker());
 }

 @Test
 public void testChangeAnnotationIntrospectorPair() {
 AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
 baseSettings = baseSettings.withInsertedAnnotationIntrospector(newAnnotationIntrospector);
 assertEquals(newAnnotationIntrospector, baseSettings.getAnnotationIntrospector());
 }

 @Test
 public void testChangeAnnotationIntrospectorPairAppended() {
 AnnotationIntrospector newAnnotationIntrospector = new AnnotationIntrospector();
 baseSettings = baseSettings.withAppendedAnnotationIntrospector(newAnnotationIntrospector);
 assertEquals(newAnnotationIntrospector, baseSettings.getAnnotationIntrospector());
 }

 @Test
 public void testChangeLocale() {
 Locale newLocale = Locale.GERMAN;
 baseSettings = baseSettings.with(newLocale);
 assertEquals(newLocale, baseSettings.getLocale());
 }

 @Test
 public void testChangeHandlerInstantiator() {
 HandlerInstantiator newHandlerInstantiator = new HandlerInstantiator() {
 @Override
 public Object createHandler(DeserializationConfig config, BeanDescription beanDesc)
 throws JsonMappingException, InstantiationException {
 return new Object() {};
 }
 };
 baseSettings = baseSettings.withHandlerInstantiator(newHandlerInstantiator);
 assertEquals(newHandlerInstantiator, baseSettings.getHandlerInstantiator());
 }

 @Test
 public void testDeserializeObjectWithDefaultSettings() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 String json = "{\"name\":\"John\",\"age\":35}";
 Object result = mapper.readValue(json, Object.class);
 assertEquals(json, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
 }

 @Test
 public void testDeserializeObjectWithCustomSettings() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CAMEL_CASE);
 mapper.setDateFormat(new StdDateFormat());
 mapper.setLocale(Locale.FRENCH);
 Object result = mapper.readValue("[{\"name\":\"John\",\"age\":35}]", new TypeReference<List<Object>>() {});
 assertEquals(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 assertEquals(JsonInclude.Include.NON_NULL, mapper.getSerializationInclusion());
 assertEquals(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CAMEL_CASE, mapper.getPropertyNamingStrategy());
 assertEquals(new StdDateFormat(), mapper.getDateFormat());
 assertEquals(Locale.FRENCH, mapper.getLocale());
 }
}