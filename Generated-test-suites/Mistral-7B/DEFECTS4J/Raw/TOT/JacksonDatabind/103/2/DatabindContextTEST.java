package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DatabindContextTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 }

 @Test
 public void testGenericConfigAccess() throws Exception {
 DatabindContext context = objectMapper.getDeserializationContext();
 MapperConfig<?> config = context.getConfig();
 assertEquals(MapperFeature.AUTO_DETECT_DERIVED, config.isEnabled(MapperFeature.AUTO_DETECT_DERIVED));
 config.disable(MapperFeature.AUTO_DETECT_DERIVED);
 context = objectMapper.getDeserializationContext();
 assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_DERIVED));
 }

 @Test
 public void testSerializationFeature() throws Exception {
 DatabindContext context = objectMapper.getSerializationContext();
 assertTrue(context.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION));
 context = objectMapper.getSerializationContext(ObjectMapper.DefaultView.FULL_SERIALIZATION_VIEW);
 assertFalse(context.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION));
 }

 @Test
 public void testActiveView() throws Exception {
 DatabindContext context = objectMapper.getDeserializationContext();
 assertNull(context.getActiveView());
 context = objectMapper.getDeserializationContext(ObjectMapper.DefaultView.FULL_SERIALIZATION_VIEW);
 assertEquals(ObjectMapper.DefaultView.FULL_SERIALIZATION_VIEW.getViewClass(), context.getActiveView());
 }

 @Test
 public void testLocale() throws Exception {
 DatabindContext context = objectMapper.getDeserializationContext();
 assertEquals(Locale.getDefault(), context.getLocale());
 context = objectMapper.getDeserializationContext(new Locale("en", "US"));
 assertEquals(new Locale("en", "US"), context.getLocale());
 }

 @Test
 public void testTimeZone() throws Exception {
 DatabindContext context = objectMapper.getDeserializationContext();
 assertEquals(TimeZone.getDefault(), context.getTimeZone());
 context = objectMapper.getDeserializationContext(TimeZone.getTimeZone("UTC"));
 assertEquals(TimeZone.getTimeZone("UTC"), context.getTimeZone());
 }

 @Test
 public void testDefaultPropertyFormat() throws Exception {
 DatabindContext context = objectMapper.getDeserializationContext();
 assertEquals(JsonFormat.Value.DATE_TIME, context.getDefaultPropertyFormat(String.class));
 }

 @Test
 public void testAttributeAccess() throws Exception {
 DatabindContext context = objectMapper.getDeserializationContext();
 context.setAttribute("testKey", "testValue");
 assertEquals("testValue", context.getAttribute("testKey"));
 }

 @Test
 public void testTypeInstantiation() throws Exception {
 DatabindContext context = objectMapper.getDeserializationContext();
 TypeFactory typeFactory = context.getTypeFactory();
 JavaType javaType = typeFactory.constructType(int[].class);
 assertEquals(javaType.getRawClass(), int[].class);
 }

 @Test
 public void testResolveSubType() throws Exception {
 DatabindContext context = objectMapper.getDeserializationContext();
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 JavaType subType = context.resolveSubType(baseType, "java.lang.StringBuffer");
 assertEquals("java.lang.StringBuffer", subType.toString());
 }

 @Test
 public void testInvalidTypeIdException() throws Exception {
 DatabindContext context = objectMapper.getDeserializationContext();
 JavaType baseType = context.getTypeFactory().constructType(String.class);
 try {
 context.invalidTypeIdException(baseType, "java.util.ArrayList");
 fail("Expected JsonMappingException");
 } catch (JsonMappingException e) {
 // Expected
 }
 }

 @Test
 public void testObjectIdGenerator() throws Exception {
 Class<MyClass> myClass = MyClass.class;
 MyClass myObject = new MyClass();
 ObjectIdGenerator<?> generator = objectMapper.getDeserializationContext().objectIdGeneratorInstance(
 MyClass.class.getAnnotations(), new ObjectIdInfo(myClass, null));
 assertEquals(myObject.getId(), generator.generateId(myObject));
 }

 @Test
 public void testObjectIdResolver() throws Exception {
 Class<MyClass> myClass = MyClass.class;
 MyClass myObject = new MyClass();
 ObjectIdResolver resolver = objectMapper.getDeserializationContext().objectIdResolverInstance(
 MyClass.class.getAnnotations(), new ObjectIdInfo(myObject, null));
 assertEquals(myObject.getId(), resolver.getId(myObject));
 }

 @Test
 public void testConverter() throws Exception {
 class MyConverter implements Converter<String, MyClass> {
 @Override
 public MyClass convert(String text, Type idFrom, Converter<?> converter) {
 return new MyClass(text);
 }
 }
 class MyClass {
 @JsonProperty("name")
 private String name;
 public MyClass() {}
 public MyClass(String name) {
 this.name = name;
 }
 public String getId() {
 return name;
 }
 }
 Annotated annotated = MyClass.class.getAnnotations()[0];
 Converter<String,MyClass> converter = objectMapper.getDeserializationContext().converterInstance(annotated, MyConverter.class);
 String text = "test";
 MyClass myObject = converter.convert(text, null, null);
 assertEquals("test", myObject.getName());
 }
}