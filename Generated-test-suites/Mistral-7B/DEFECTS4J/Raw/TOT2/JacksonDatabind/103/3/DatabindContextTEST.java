package com.fasterxml.jackson.databind.test;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.DatabindContext;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.cfg.SerializerConfig;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TypeFactory;
import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DatabindContextTest {

 private DatabindContext context;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws Exception {
 context = new BaseDatabindContext();
 objectMapper = new ObjectMapper();
 objectMapper.setHandlerInstantiator(context);
 }

 @Test
 public void testGetConfig() {
 MapperConfig<?> config = context.getConfig();
 Assert.assertNotNull(config);
 }

 @Test
 public void testGetAnnotationIntrospector() {
 AnnotationIntrospector annotationIntrospector = context.getAnnotationIntrospector();
 Assert.assertNotNull(annotationIntrospector);
 }

 @Test
 public void testIsEnabled_BasicUseCase() {
 Assert.assertTrue(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
 }

 @Test
 public void testIsEnabled_DisabledUseCase() {
 context.getConfig().disable(MapperFeature.AUTO_DETECT_CREATORS);
 Assert.assertFalse(context.isEnabled(MapperFeature.AUTO_DETECT_CREATORS));
 }

 @Test
 public void testGetActiveView() {
 Assert.assertNull(context.getActiveView());
 }

 @Test
 public void testGetLocale() {
 Assert.assertEquals(Locale.getDefault(), context.getLocale());
 }

 @Test
 public void testGetTimeZone() {
 Assert.assertEquals(TimeZone.getDefault(), context.getTimeZone());
 }

 @Test
 public void testgetDefaultPropertyFormat() {
 Class<?> baseType = String.class;
 JsonFormat.Value defaultPropertyFormat = context.getDefaultPropertyFormat(baseType);
 Assert.assertNotNull(defaultPropertyFormat);
 }

 @Test
 public void testGetAttribute() {
 Object attributeKey = "testKey";
 Object attributeValue = "testValue";
 context.setAttribute(attributeKey, attributeValue);
 Object attribute = context.getAttribute(attributeKey);
 Assert.assertEquals(attributeValue, attribute);
 }

 @Test
 public void testConstructType() {
 Class<?> type = Integer.class;
 JavaType javaType = context.getTypeFactory().constructType(type);
 Assert.assertNotNull(javaType);
 }

 @Test
 public void testConstructSpecializedType() {
 Class<?> baseType = Integer.class;
 Class<?> subclass = Integer[].class;
 JavaType specializedType = context.constructSpecializedType(baseType, subclass);
 Assert.assertNotNull(specializedType);
 }

 @Test
 public void testResolveSubType() throws JsonMappingException {
 Class<?> baseType = String.class;
 String subClass = "java.util.ArrayList<java.lang.String>";
 JavaType resolvedType = context.resolveSubType(baseType, subClass);
 Assert.assertNotNull(resolvedType);
 }

 @Test(expected = JsonMappingException.class)
 public void testInvalidTypeIdException() throws JsonMappingException {
 JavaType baseType = JavaType.construct();
 String typeId = "InvalidType";
 context.invalidTypeIdException(baseType, typeId, "InvalidType");
 }

 @Test
 public void testGetTypeFactory() {
 TypeFactory typeFactory = context.getTypeFactory();
 Assert.assertNotNull(typeFactory);
 }

 @Test
 public void testObjectIdGeneratorInstance() throws JsonMappingException {
 Annotated annotated = new AnnotatedClass(Integer.class);
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(annotated, null, null, true, null);
 ObjectIdGenerator<?> objectIdGenerator = context.objectIdGeneratorInstance(annotated, objectIdInfo);
 Assert.assertNotNull(objectIdGenerator);
 }

 @Test
 public void testObjectIdResolverInstance() {
 Annotated annotated = new AnnotatedClass(Integer.class);
 ObjectIdInfo objectIdInfo = new ObjectIdInfo(annotated, null, null, true, null);
 ObjectIdResolver objectIdResolver = context.objectIdResolverInstance(annotated, objectIdInfo);
 Assert.assertNotNull(objectIdResolver);
 }

 @Test
 public void testConverterInstance() throws JsonMappingException {
 Annotated annotated = new AnnotatedClass(String.class);
 Object converterDef = StringToIntegerConverter.class;
 Converter<Object, Object> converter = context.converterInstance(annotated, converterDef);
 Assert.assertNotNull(converter);
 }

 @Test
 public void testReportBadDefinition() throws IOException {
 JavaType type = Object.class;
 String msg = "Invalid definition for class " + type.getRawClass().getName();
 Object badDefinition = context.reportBadDefinition(type, msg);
 Assert.fail("Expected exception, but got: " + badDefinition);
 }

 @Test
 public void testReportBadDefinition_Class() throws IOException {
 Class<?> type = Object.class;
 String msg = "Invalid definition for class " + type.getName();
 Object badDefinition = context.reportBadDefinition(type, msg);
 Assert.fail("Expected exception, but got: " + badDefinition);
 }
}

class BaseDatabindContext extends DatabindContext {
 // Empty implementation for testing purposes
}

class StringToIntegerConverter extends StdConverter<String, Integer> {
}

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class TestBean {
 @JsonProperty("id")
 private int id;

 @JsonDeserialize(using = IdDeserializer.class)
 @JsonSerialize(using = IdSerializer.class)
 private String name;

 public int getId() {
 return id;
 }

 public void setId(int id) {
 this.id = id;
 }

 public String getName() {
 return name;
 }

 public void setName(String name) {
 this.name = name;
 }
}

class IdDeserializer extends StdDeserializer<Integer> {
 @Override
 public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return ctxt.readValueAs(Integer.class);
 }
}

class IdSerializer extends StdSerializer<Integer> {
 @Override
 public void serialize(Integer value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
 gen.writeNumber(value);
 }
}