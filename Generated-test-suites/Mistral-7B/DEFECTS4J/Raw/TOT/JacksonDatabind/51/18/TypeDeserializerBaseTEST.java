package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase typeDeserializerBase;
 private DeserializationContext context;
 private ObjectMapper objectMapper;

 @Before
 public void setUp() throws Exception {
 objectMapper = new ObjectMapper();
 objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 context = objectMapper.getDeserializationContext();
 typeDeserializerBase = new BaseTypeDeserializer(Object.class, context, null, false, null);
 }

 @Test
 public void testNullTypeId() throws Exception {
 Object result = typeDeserializerBase.deserialize(null, context, null);
 assertNull(result);
 }

 @Test
 public void testEmptyTypeId() throws Exception {
 Object result = typeDeserializerBase.deserialize(new StringReader("{}"), context, null);
 assertNull(result);
 }

 @Test
 public void testValidTypeId() throws Exception {
 String typeId = "com.example.MyClass";
 Object result = typeDeserializerBase.deserialize(new StringReader("{\"@type\":\"" + typeId + "\"}"), context, null);
 assertTrue(result instanceof MyClass);
 }

 @Test
 public void testMissingTypeId() throws Exception {
 String json = "{\"foo\":\"bar\"}";
 try {
 Object result = typeDeserializerBase.deserialize(new StringReader(json), context, null);
 fail("Expected exception");
 } catch (JsonMappingException e) {
 // expected
 assertEquals("Can not instantiate value of type [simple type, class com.example.MyClass] from JSON String; no String deserializer found for class com.example.MyClass. Deserialization failed: com.fasterxml.jackson.databind.JsonMappingException: Can not instantiate value of type [simple type, class com.example.MyClass] from JSON String", e.getMessage());
 }
 }

 @Test
 public void testTypeIdVisible() throws Exception {
 typeDeserializerBase = new BaseTypeDeserializer(Object.class, context, null, true, null);
 Object result = typeDeserializerBase.deserialize(new StringReader("{\"@type\":\"com.example.MyClass\"}"), context, null);
 assertTrue(result instanceof MyClass);
 }

 @Test
 public void testNotFoundTypeId() throws Exception {
 typeDeserializerBase = new BaseTypeDeserializer(Object.class, context, null, false, null);
 String typeId = "com.example.NonExistentClass";
 try {
 Object result = typeDeserializerBase.deserialize(new StringReader("{\"@type\":\"" + typeId + "\"}"), context, null);
 fail("Expected exception");
 } catch (JsonMappingException e) {
 // expected
 assertEquals("Could not find deserializer for type [simple type, class com.example.NonExistentClass]", e.getMessage());
 }
 }

 @Test
 public void testDeserializerNotFound() throws Exception {
 typeDeserializerBase = new BaseTypeDeserializer(Object.class, context, null, false, null);
 TypeIdResolver idResolver = new BaseTypeIdResolver();
 typeDeserializerBase = new BaseTypeDeserializer(Object.class, context, idResolver, false, null);
 String typeId = "com.example.MyClass";
 JsonDeserializer<MyClass> deserializer = new MyClassDeserializer();
 idResolver.putIdMapping(typeId, deserializer);
 try {
 Object result = typeDeserializerBase.deserialize(new StringReader("{\"@type\":\"" + typeId + "\"}"), context, null);
 fail("Expected exception");
 } catch (JsonMappingException e) {
 // expected
 assertEquals("Deserializer for type [simple type, class com.example.MyClass] not found", e.getMessage());
 }
 }

 @Test
 public void testDefaultImpl() throws Exception {
 typeDeserializerBase = new BaseTypeDeserializer(Object.class, context, null, false, null);
 Object result = typeDeserializerBase.deserialize(new StringReader("null"), context, null);
 assertNull(result);
 }

 @Test
 public void testDefaultImplDeserializer() throws Exception {
 typeDeserializerBase = new BaseTypeDeserializer(Object.class, context, null, false, new MyClass());
 Object result = typeDeserializerBase.deserialize(new StringReader("null"), context, null);
 assertTrue(result instanceof MyClass);
 }

 @Test
 public void testTypeIdVisibleWithDefaultImpl() throws Exception {
 typeDeserializerBase = new BaseTypeDeserializer(Object.class, context, null, true, new MyClass());
 String json = "{\"@type\":\"myClass\", \"value\":1}" ;
 ObjectReader reader = objectMapper.getObjectReaderForValueType(MyClass.class);
 Object result = reader.readValue(new StringReader(json), typeDeserializerBase);
 assertTrue(result instanceof MyClass);
 }
}

class MyClassDeserializer extends StdDeserializer<MyClass> {
 MyClassDeserializer() { this(null); }
 MyClassDeserializer(Class<MyClass> c) { super(c); }
 @Override
 public MyClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new MyClass();
 }
}

class BaseTypeDeserializer extends TypeDeserializerBase<Object> {
 BaseTypeDeserializer(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
 super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 }

 @Override
 public TypeDeserializer forProperty(BeanProperty prop) {
 throw new UnsupportedOperationException();
 }

 @Override
 public JsonTypeInfo.As getTypeInclusion() {
 return JsonTypeInfo.As.EXISTING_PROPERTY;
 }
}

class BaseTypeIdResolver extends TypeIdResolverBase {
 @Override
 public JsonDeserializer<Object> findDeserializerForId(String id, DeserializationContext ctxt) throws IOException {
 return null;
 }
}

class MyClass {
}