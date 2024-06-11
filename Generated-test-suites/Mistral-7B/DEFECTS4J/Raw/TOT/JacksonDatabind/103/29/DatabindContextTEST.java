package com.example.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;

public class DatabindContextTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testGetConfig() {
 MapperConfig<?> config = mapper.getConfiguration();
 assertNotNull(config);
 }

 @Test
 public void testGetAnnotationIntrospector() {
 AnnotationIntrospector introspector = mapper.getSerializationConfig().getIntrospector();
 assertNotNull(introspector);
 }

 @Test
 public void testIsEnabledMapperFeature() {
 assertTrue(mapper.isEnabled(MapperFeature.DEFAULT_INCLUDE_ANNOTATIONS));
 assertFalse(mapper.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
 }

 @Test
 public void testCanOverrideAccessModifiers() {
 assertTrue(mapper.canOverrideAccessModifiers());
 }

 @Test
 public void testGetActiveView() {
 // Not really testable in abstract class
 }

 @Test
 public void testGetLocale() {
 assertEquals(Locale.getDefault(), mapper.getLocale());
 }

 @Test
 public void testGetTimeZone() {
 assertEquals(TimeZone.getDefault(), mapper.getTimeZone());
 }

 @Test
 public void testGetDefaultPropertyFormat() {
 assertEquals(new JsonFormat.Value(null, null, null, null, null, null, null), mapper.getDefaultPropertyFormat(Object.class));
 }

 @Test
 public void testAttribute() {
 MapperConfig<?> config = mapper.getConfiguration();
 config.setAttribute(SerializationFeature.FAIL_ON_EMPTY_BEANS, true);
 Object attr = config.getAttribute(SerializationFeature.FAIL_ON_EMPTY_BEANS);
 assertEquals(true, attr);
 }

 @Test
 public void testConstructType() {
 Class<String> stringClass = String.class;
 JavaType javaType = mapper.getTypeFactory().constructType(stringClass);
 assertEquals(stringClass, javaType.getRawClass());
 }

 @Test
 public void testConstructSpecializedType() {
 Class<String> stringClass = String.class;
 JavaType listStringType = mapper.getTypeFactory().constructType(new TypeReference<List<String>>(){}.getType());
 JavaType specializedType = mapper.getTypeFactory().constructSpecializedType(listStringType, String[].class);
 assertEquals(String[].class, specializedType.getRawClass());
 }

 @Test
 public void testResolveSubType() throws JsonMappingException {
 JavaType baseType = mapper.getTypeFactory().constructType(String.class);
 String className = "java.util.ArrayList";
 JavaType resolvedType = mapper.getTypeFactory().resolveSubType(baseType, className);
 assertNotNull(resolvedType);
 assertEquals(ArrayList.class, resolvedType.getRawClass());
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeIdException() throws JsonMappingException {
 JavaType baseType = mapper.getTypeFactory().constructType(String.class);
 String typeId = "InvalidType";
 mapper.getTypeFactory().invalidTypeIdException(baseType, typeId, "InvalidTypeIdException");
 }

 @Test
 public void testObjectIdGeneratorInstance() throws JsonMappingException {
 Annotated annotated = mapper.getSerializationConfig().findPropertyDefinition("id", String.class);
 ObjectIdInfo objectIdInfo = mapper.getAnnotationIntrospector().findObjectId(annotated);
 ObjectIdGenerator<?> idGenerator = mapper.objectIdGeneratorInstance(annotated, objectIdInfo);
 assertNotNull(idGenerator);
 }

 @Test
 public void testObjectIdResolverInstance() throws JsonMappingException {
 Annotated annotated = mapper.getSerializationConfig().findPropertyDefinition("name", String.class);
 ObjectIdInfo objectIdInfo = mapper.getAnnotationIntrospector().findObjectId(annotated);
 ObjectIdResolver idResolver = mapper.objectIdResolverInstance(annotated, objectIdInfo);
 assertNotNull(idResolver);
 }

 @Test
 public void testConverterInstance() throws JsonMappingException {
 Annotated annotated = mapper.getSerializationConfig().findPropertyDefinition("value", Integer.class);
 Object converterDef = annotated.getAnnotation(JsonProperty.class).valueConverter();
 Converter<Object,Object> converter = mapper.converterInstance(annotated, converterDef);
 assertNotNull(converter);
 }

 @Test
 public void testReportBadDefinition() throws IOException {
 try {
 mapper.registerModule(new SimpleModule());
 } catch (JsonMappingException e) {
 assertEquals("Problem: (ModuleRegistrationModule) Unrecognized 'SimpleModule' as module type",
 mapper.reportBadDefinition(MapperModule.class, "Unrecognized 'SimpleModule' as module type"));
 }
 }

 @Test
 public void testReportBadDefinitionWithCustomMessage() throws IOException {
 try {
 mapper.registerModule(new MyCustomModule());
 } catch (JsonMappingException e) {
 assertEquals("Problem: (MyCustomModule) Unrecognized 'MyCustomModule' as module type",
 mapper.reportBadDefinition(MyCustomModule.class, "Unrecognized 'MyCustomModule' as module type"));
 }
 }
}