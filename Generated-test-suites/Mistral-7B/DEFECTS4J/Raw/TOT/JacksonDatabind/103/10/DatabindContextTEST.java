package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
import com.fasterxml.jackson.databind.jsonschema.JsonSchemaGenerator;
import com.fasterxml.jackson.databind.jsonschema.SchemaPrinter;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter.StdConverter;

import org.junit.Before;
import org.junit.Test;

public class DatabindContextTest {

 private ObjectMapper mapper;

 @Before
 public void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testGetConfig() {
 MapperConfig<?> config = mapper.getConfig();
 assertNotNull(config);
 }

 @Test
 public void testGetAnnotationIntrospector() {
 AnnotationIntrospector ai = mapper.getAnnotationIntrospector();
 assertNotNull(ai);
 }

 @Test
 public void testIsEnabled() {
 MapperFeature feature = MapperFeature.DEFAULT_INTENTS;
 assertTrue(mapper.isEnabled(feature));
 mapper.disable(feature);
 assertFalse(mapper.isEnabled(feature));
 mapper.enable(feature);
 assertTrue(mapper.isEnabled(feature));
 }

 @Test
 public void testCanOverrideAccessModifiers() {
 assertFalse(mapper.canOverrideAccessModifiers());
 }

 @Test
 public void testGetActiveView() {
 assertNull(mapper.getActiveView());
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
 Class<?> baseType = String.class;
 JsonFormat.Value format = mapper.getDefaultPropertyFormat(baseType);
 assertNotNull(format);
 }

 @Test
 public void testGetAttribute() {
 Object key = JsonProperty.VALUE;
 Object value = mapper.getActiveView();
 assertEquals(value, mapper.getAttribute(key));
 }

 @Test
 public void testSetAttribute() {
 Object key = JsonProperty.VALUE;
 Object value = new Object();
 mapper.setAttribute(key, value);
 assertEquals(value, mapper.getAttribute(key));
 }

 @Test
 public void testConstructType() {
 Type type = String[].class;
 JavaType javaType = mapper.getTypeFactory().constructType(type);
 assertEquals(javaType.getRawClass(), (Class<?>)type.getComponentType());
 assertEquals(javaType.getRawClass().getComponentType(), String.class);
 }

 @Test
 public void testConstructSpecializedType() {
 JavaType baseType = mapper.getTypeFactory().constructType(String.class);
 Class<?> subclass = StringBuffer.class;
 JavaType specializedType = mapper.getTypeFactory().constructSpecializedType(baseType, subclass);
 assertEquals(specializedType.getRawClass(), subclass);
 }

 @Test
 public void testResolveSubType() throws JsonMappingException {
 JavaType baseType = mapper.getTypeFactory().constructType(String.class);
 String subClass = "java.util.ArrayList";
 JavaType resolvedType = mapper.getTypeFactory().resolveSubType(baseType, subClass);
 assertNotNull(resolvedType);
 assertEquals(JavaType.constructFromCanonical(subClass), resolvedType);
 }

 @Test
 public void testInvalidTypeIdException() throws JsonMappingException {
 JavaType baseType = mapper.getTypeFactory().constructType(String.class);
 String typeId = "com.example.MyClass";
 JsonMappingException exception = mapper.getTypeFactory().invalidTypeIdException(baseType, typeId, null);
 assertNotNull(exception);
 }

 @Test
 public void testTypeFactory() {
 TypeFactory typeFactory = mapper.getTypeFactory();
 assertNotNull(typeFactory);
 }

 @Test
 public void testObjectIdGeneratorInstance() throws JsonMappingException {
 Annotated annotated = mapper.getSerializationConfig().getClassAnnotations().findAnnotation(ObjectIdGenerator.class);
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(annotated, null);
 ObjectIdGenerator<?> gen = mapper.objectIdGeneratorInstance(annotated, objectIdInfo);
 assertNotNull(gen);
 }

 @Test
 public void testObjectIdResolverInstance() throws JsonMappingException {
 Annotated annotated = mapper.getSerializationConfig().getClassAnnotations().findAnnotation(ObjectIdResolver.class);
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(annotated, null);
 ObjectIdResolver resolver = mapper.objectIdResolverInstance(annotated, objectIdInfo);
 assertNotNull(resolver);
 }

 @Test
 public void testConverterInstance() throws JsonMappingException {
 Annotated annotated = mapper.getSerializationConfig().getClassAnnotations().findAnnotation(Converter.class);
 Object converterDef = annotated.getRawMemberValue("value");
 Converter<Object,Object> converter = mapper.converterInstance(annotated, converterDef);
 assertNotNull(converter);
 }

 @Test
 public void testReportBadDefinition() throws JsonMappingException {
 Class<?> type = Integer.class;
 assertEquals("Expected a constructable class, but got " + type.getName(),
 mapper.reportBadDefinition(type, "Expected a constructable class"));
 }

 @Test
 public void testReportBadDefinitionWithString() throws JsonMappingException {
 String msg = "Expected a constructable class, but got " + String.class.getName();
 assertEquals(msg, mapper.reportBadDefinition(String.class, msg));
 }

 @Test
 public void testFormat() {
 String msg = "Expected a constructable class, but got %s";
 String expected = "Expected a constructable class, but got java.lang.String";
 String result = mapper._format(msg, String.class.getName());
 assertEquals(expected, result);
 }

 @Test
 public void testTruncate() {
 String desc = "This is a very long description for testing the _truncate method.";
 String expected = "This is a very long description for testing the _truncate method...";
 String result = mapper._truncate(desc);
 assertEquals(expected, result);
 }

 @Test
 public void testQuotedString() {
 String desc = "This is a \"test\" string for testing the _quotedString method.";
 String expected = "\"This is a \"test\" string for testing the _quotedString method.\"".replaceAll("\\\\", "\\\\\\\\");
 String result = mapper._quotedString(desc);
 assertEquals(expected, result);
 }

 @Test
 public void testColonConcat() {
 String msgBase = "Expected a constructable class, but got ";
 String extra = "java.lang.String";
 String expected = "Expected a constructable class, but got java.lang.String";
 String result = mapper._colonConcat(msgBase, extra);
 assertEquals(expected, result);
 }

 @Test
 public void testDesc() {
 String desc = "This is a description for testing the _desc method.";
 String expected = "This is a description for testing the _desc method.";
 String result = mapper._desc(desc);
 assertEquals(expected, result);
 }

 // Test for custom converter
 @JsonProperty("customField")
 static class CustomClass {
 private int id;
 private String name;

 public CustomClass() {}

 public CustomClass(int id, String name) {
 this.id = id;
 this.name = name;
 }

 @JsonValue
 public int getId() {
 return id;
 }

 public String getName() {
 return name;
 }
 }

 @Test
 public void testCustomConverter() throws JsonMappingException {
 mapper.registerModule(new SimpleModule() {
 @Override
 public void setupModule(SetupContext context) throws Exception {
 addSerializer(CustomClass.class, new StdConverter.StdConverter<CustomClass, Integer>(CustomClass.class, Integer.class) {
 @Override
 public Integer convert(CustomClass customClass, JavaType javaTypeOfT) {
 return customClass.getId();
 }

 @Override
 public CustomClass reconstructFrom(Integer intValue, JavaType javaTypeOfT) {
 return new CustomClass(intValue, null);
 }
 });
 }
 });

 String json = "{\"customField\":1}";
 CustomClass obj = mapper.readValue(json, CustomClass.class);
 assertEquals(1, obj.getId());
 assertEquals(null, obj.getName());
 }

 @Test
 public void testJsonSchemaGeneration() throws JsonMappingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(MapperFeature.AUTO_DETECT_DERIVED_FOR_GETTERS, true);
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

 JsonSchemaGenerator schemaGenerator = new JsonSchemaGenerator();
 schemaGenerator.configure(mapper, new SchemaPrinter());

 JsonSchema schema = schemaGenerator.generateSchema(CustomClass.class);
 assertEquals(CustomClass.class, schema.getSchemaProvider().getSchema(schema).getBaseType());
 }
}