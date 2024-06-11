package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.test.BaseMapTest;

public class AsPropertyTypeDeserializerTest extends BaseMapTest {

 private static class DummyTypeIdResolver extends TypeIdResolverBase {
 @Override
 public TypeId idFromValueAndType(Object value, JavaType javaType) {
 return null;
 }
 }

 @SuppressWarnings("unchecked")
 @ org.junit.Test
 public void testAsPropertyTypeDeserializer() throws IOException {
 // Test with valid JSON
 String json = "{\"type\":\"MyType\",\"data\":123}";
 JavaType javaType = mapper.getTypeFactory().constructType(MyType.class);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectReader objectReader = objectMapper.readerForType(javaType);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, new DummyTypeIdResolver(), "type", true, MyDefaultImpl.class, As.PROPERTY);
 TypeDeserializer deserializerForProperty = deserializer.forProperty(new BeanPropertyImpl(javaType, "type"));
 Object result = deserializerForProperty.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), new TestContext());
 assertEquals(MyType.class, result.getClass());
 assertEquals(123, ((MyType) result).getData());

 // Test with JSON Object
 json = "{\"type\":\"MyType\",\"data\":123,\"other\":\"otherValue\"}";
 result = deserializerForProperty.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), new TestContext());
 assertEquals(MyType.class, result.getClass());
 assertEquals(123, ((MyType) result).getData());
 assertEquals("otherValue", ((BaseMap) result).get("other"));

 // Test with missing "type" property
 json = "{\"data\":123}";
 result = deserializerForProperty.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), new TestContext());
 assertNull(result);

 // Test with native type id
 json = "{\"type\":\"MyType\",\"typeId\":\"MyTypeId\",\"data\":123}";
 result = deserializerForProperty.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), new TestContext());
 assertEquals(MyType.class, result.getClass());
 assertEquals(123, ((MyType) result).getData());

 // Test with JSON Array
 json = "[\"MyType\",123]";
 result = deserializerForProperty.deserializeTypedFromAny(objectMapper.getFactory().createParser(json), new TestContext());
 assertEquals(MyType[].class, result.getClass());
 assertArrayEquals(new MyType[]{new MyType()}, (MyType[]) result);

 // Test with JSON Scalar
 json = "123";
 result = deserializerForProperty.deserializeTypedFromScalar(objectMapper.getFactory().createParser(json), new TestContext());
 assertEquals(Integer.class, result.getClass());
 assertEquals(123, result);
 }

 @SuppressWarnings("unchecked")
 @ org.junit.Test
 public void testAsPropertyTypeDeserializerWithDefaultImpl() throws IOException {
 // Test with valid JSON
 JavaType javaType = mapper.getTypeFactory().constructType(MyTypeWithDefaultImpl.class);
 ObjectMapper objectMapper = new ObjectMapper();
 ObjectReader objectReader = objectMapper.readerForType(javaType);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(javaType, new DummyTypeIdResolver(), "type", true, MyDefaultImpl.class, As.PROPERTY);
 Object result = deserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(VALID_JSON), new TestContext());
 assertEquals(MyTypeWithDefaultImpl.class, result.getClass());
 assertEquals(45, ((MyTypeWithDefaultImpl) result).getData());

 // Test with JSON Object
 String json = "{\"type\":\"MyTypeWithDefaultImpl\",\"data\":123,\"other\":\"otherValue\"}";
 result = deserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), new TestContext());
 assertEquals(MyTypeWithDefaultImpl.class, result.getClass());
 assertEquals(123, ((MyTypeWithDefaultImpl) result).getData());
 assertEquals("otherValue", ((BaseMap) result).get("other"));

 // Test with missing "type" property
 json = "{\"data\":123}";
 result = deserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), new TestContext());
 assertEquals(MyTypeWithDefaultImpl.class, result.getClass());
 assertEquals(45, ((MyTypeWithDefaultImpl) result).getData());

 // Test with native type id
 json = "{\"type\":\"MyTypeWithDefaultImpl\",\"typeId\":\"MyTypeId\",\"data\":123}";
 result = deserializer.deserializeTypedFromObject(objectMapper.getFactory().createParser(json), new TestContext());
 assertEquals(MyTypeWithDefaultImpl.class, result.getClass());
 assertEquals(123, ((MyTypeWithDefaultImpl) result).getData());

 // Test with JSON Array
 json = "[\"MyTypeWithDefaultImpl\",123]";
 result = deserializer.deserializeTypedFromAny(objectMapper.getFactory().createParser(json), new TestContext());
 assertEquals(MyTypeWithDefaultImpl[].class, result.getClass());
 assertArrayEquals(new MyTypeWithDefaultImpl[]{new MyTypeWithDefaultImpl()}, (MyTypeWithDefaultImpl[]) result);

 // Test with JSON Scalar
 json = "123";
 result = deserializer.deserializeTypedFromScalar(objectMapper.getFactory().createParser(json), new TestContext());
 assertEquals(Integer.class, result.getClass());
 assertEquals(123, result);
 }

 private static class TestContext extends DeserializationContext {
 @Override
 public Object findValueInASG(String fieldName, JsonParser p) throws IOException {
 return null;
 }

 @Override
 public void handleUnexpectedValue(DeserializationContext ctxt, JsonParser p, Object value) throws IOException {
 // Test with unknown type
 if (value != null && (value.getClass() != MyType.class && value.getClass() != MyTypeWithDefaultImpl.class)) {
 throw new AssertionError("Unexpected value: " + value.getClass());
 }
 }
 }

 private static ObjectMapper mapper = new ObjectMapper();
 private static String VALID_JSON = "{\"type\":\"MyType\",\"data\":123}";
}

class MyType {
 private int data;

 public int getData() {
 return data;
 }

 public void setData(int data) {
 this.data = data;
 }
}

class MyDefaultImpl {
 public int data = 45;
}

class MyTypeWithDefaultImpl extends MyType {
}