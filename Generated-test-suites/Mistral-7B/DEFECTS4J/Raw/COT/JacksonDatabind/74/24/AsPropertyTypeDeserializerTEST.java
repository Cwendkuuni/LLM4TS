package com.example.junit;

import static org.junit.Assert.*;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.util.TypeFactory;

public class AsPropertyTypeDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @SuppressWarnings("unchecked")
 private static JavaType createTestType(Class<?> clazz, As inclusion) {
 return TypeFactory.defaultInstance().constructType(clazz, new TypeReference<JavaType>() {});
 }

 @SuppressWarnings("unchecked")
 private static JavaType createTestDefaultImpl(Class<?> clazz) {
 return (JavaType) MAPPER.getTypeFactory().constructType(clazz);
 }

 @Test
 public void testAsPropertyTypeDeserializerConstructorWithObjectType() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 createTestType(String.class, As.PROPERTY),
 MAPPER.getTypeIdResolver(),
 "_typePropertyName",
 true,
 createTestDefaultImpl(String.class.getClassLoader().getType("com.example.test.TestClass")));
 assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
 }

 @Test
 public void testAsPropertyTypeDeserializerConstructorWithBeanProperty() throws Exception {
 BeanProperty property = new BeanProperty("_property", String.class);
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 createTestType(String.class, As.PROPERTY),
 MAPPER.getTypeIdResolver(),
 "_typePropertyName",
 true,
 createTestDefaultImpl(String.class.getClassLoader().getType("com.example.test.TestClass")),
 property);
 assertEquals(As.PROPERTY, deserializer.getTypeInclusion());
 assertEquals(property, deserializer._property);
 }

 @Test
 public void testForProperty() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 createTestType(String.class, As.PROPERTY),
 MAPPER.getTypeIdResolver(),
 "_typePropertyName",
 true,
 createTestDefaultImpl(String.class.getClassLoader().getType("com.example.test.TestClass")));
 AsPropertyTypeDeserializer deserializer2 = deserializer.forProperty(null);
 assertNotSame(deserializer, deserializer2);
 deserializer2 = deserializer.forProperty(new BeanProperty("_property", String.class));
 assertEquals(deserializer, deserializer2);
 }

 @Test
 public void testDeserializeTypedFromObjectWithNull() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 createTestType(String.class, As.PROPERTY),
 MAPPER.getTypeIdResolver(),
 "_typePropertyName",
 true,
 createTestDefaultImpl(String.class.getClassLoader().getType("com.example.test.TestClass")));
 JsonParser parser = MAPPER.getFactory().createParser("{}");
try {
 deserializer.deserializeTypedFromObject(parser, MAPPER.getDeserializationContext());
 fail("Expected IOException");
} catch (IOException e) {
 // Expected
}
}

 @Test
 public void testDeserializeTypedFromObjectWithTypeId() throws Exception {
 String typeId = "com.example.test.TestClass";
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 createTestType(String.class, As.PROPERTY),
 MAPPER.getTypeIdResolver(),
 "_typePropertyName",
 true,
 createTestDefaultImpl(String.class.getClassLoader().getType("com.example.test.TestClass")),
 As.PROPERTY);
 JsonParser parser = MAPPER.getFactory().createParser("{\"_typePropertyName\":{\"@class\":\"" + typeId + "\"}}");
 assertEquals(typeId, deserializer.deserializeTypedFromObject(parser, MAPPER.getDeserializationContext()).getClass().getName());
 }

 @Test
 public void testDeserializeTypedFromObjectWithTypePropertyName() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 createTestType(String.class, As.PROPERTY),
 MAPPER.getTypeIdResolver(),
 "_typePropertyName",
 true,
 createTestDefaultImpl(String.class.getClassLoader().getType("com.example.test.TestClass")),
 As.PROPERTY);
 JsonParser parser = MAPPER.getFactory().createParser("{\"typePropertyName\":{\"@class\":\"" + String.class.getName() + "\"}}");
 assertEquals(String.class, deserializer.deserializeTypedFromObject(parser, MAPPER.getDeserializationContext()).getClass());
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedFromObjectWithMissingTypePropertyName() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 createTestType(String.class, As.PROPERTY),
 MAPPER.getTypeIdResolver(),
 "_typePropertyName",
 true,
 createTestDefaultImpl(String.class.getClassLoader().getType("com.example.test.TestClass")),
 As.PROPERTY);
 JsonParser parser = MAPPER.getFactory().createParser("{ \"fieldName\": \"value\" }");
 deserializer.deserializeTypedFromObject(parser, MAPPER.getDeserializationContext());
 fail("Expected IOException");
 }

 @Test
 public void testDeserializeTypedFromObjectWithMissingTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 createTestType(String.class, As.PROPERTY),
 MAPPER.getTypeIdResolver(),
 "_typePropertyName",
 true,
 createTestDefaultImpl(String.class.getClassLoader().getType("com.example.test.TestClass")),
 As.PROPERTY);
 JsonParser parser = MAPPER.getFactory().createParser("{\"_typePropertyName\":{}}");
 assertEquals(String.class, deserializer.deserializeTypedFromObject(parser, MAPPER.getDeserializationContext()).getClass());
 }

 @Test
 public void testDeserializeTypedFromObjectWithNativeTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 createTestType(String.class, As.PROPERTY),
 MAPPER.getTypeIdResolver(),
 "_typePropertyName",
 true,
 createTestDefaultImpl(String.class.getClassLoader().getType("com.example.test.TestClass")),
 As.PROPERTY);
 JsonParser parser = MAPPER.getFactory().createParser("{\"_typePropertyName\":{\"@class\":\"com.example.test.TestClass\"}}");
 assertEquals(String.class, deserializer.deserializeTypedFromObject(parser, MAPPER.getDeserializationContext()).getClass());
 }

 @Test
 public void testDeserializeTypedUsingDefaultImplWithNull() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 createTestType(String.class, As.PROPERTY),
 MAPPER.getTypeIdResolver(),
 "_typePropertyName",
 true,
 createTestDefaultImpl(String.class.getClassLoader().getType("com.example.test.TestClass")),
 As.PROPERTY);
 JsonParser parser = MAPPER.getFactory().createParser("{}");
 assertEquals(String.class, deserializer._defaultImpl.deserialize(parser, MAPPER.getDeserializationContext()).getClass());
 }

 @Test
 public void testDeserializeTypedUsingDefaultImplWithTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 createTestType(String.class, As.PROPERTY),
 MAPPER.getTypeIdResolver(),
 "_typePropertyName",
 true,
 createTestDefaultImpl(String.class.getClassLoader().getType("com.example.test.TestClass")),
 As.PROPERTY);
 JsonParser parser = MAPPER.getFactory().createParser("{\"_typePropertyName\":{\"@class\":\"com.example.test.TestClass\"}}");
 assertEquals("TestClass", parser.getText()); // to verify that default impl deserializes correctly
 assertEquals(String.class, deserializer._defaultImpl.getType().getRawClass());
 }

 @Test
 public void testDeserializeTypedUsingDefaultImplWithTypePropertyName() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 createTestType(String.class, As.PROPERTY),
 MAPPER.getTypeIdResolver(),
 "_typePropertyName",
 true,
 createTestDefaultImpl(String.class.getClassLoader().getType("com.example.test.TestClass")),
 As.PROPERTY);
 JsonParser parser = MAPPER.getFactory().createParser("{\"typePropertyName\":{\"@class\":\"com.example.test.TestClass\"}}");
 assertEquals("TestClass", parser.getText()); // to verify that default impl deserializes correctly
 assertEquals(String.class, deserializer._defaultImpl.getType().getRawClass());
 }

 @Test(expected = IOException.class)
 public void testDeserializeTypedUsingDefaultImplWithMissingTypePropertyName() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 createTestType(String.class, As.PROPERTY),
 MAPPER.getTypeIdResolver(),
 "_typePropertyName",
 true,
 createTestDefaultImpl(String.class.getClassLoader().getType("com.example.test.TestClass")),
 As.PROPERTY);
 JsonParser parser = MAPPER.getFactory().createParser("{ \"fieldName\": \"value\" }");
 deserializer._defaultImpl.deserialize(parser, MAPPER.getDeserializationContext());
 fail("Expected IOException");
 }

 @Test
 public void testDeserializeTypedUsingDefaultImplWithMissingTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 createTestType(String.class, As.PROPERTY),
 MAPPER.getTypeIdResolver(),
 "_typePropertyName",
 true,
 createTestDefaultImpl(String.class.getClassLoader().getType("com.example.test.TestClass")),
 As.PROPERTY);
 JsonParser parser = MAPPER.getFactory().createParser("{\"_typePropertyName\":{}}");
 assertEquals("TestClass", deserializer._defaultImpl.getType().getRawClass().getSimpleName());
 }

 @Test
 public void testDeserializeTypedUsingDefaultImplWithNativeTypeId() throws Exception {
 AsPropertyTypeDeserializer deserializer = new AsPropertyTypeDeserializer(
 createTestType(String.class, As.PROPERTY),
 MAPPER.getTypeIdResolver(),
 "_typePropertyName",
 true,
 createTestDefaultImpl(String.class.getClassLoader().getType("com.example.test.TestClass")),
 As.PROPERTY);
 JsonParser parser = MAPPER.getFactory().createParser("{\"_typePropertyName\":{\"@class\":\"com.example.test.TestClass\"}}");
 assertEquals("TestClass", parser.getText()); // to verify that default impl deserializes correctly
 assertEquals(String.class, deserializer._defaultImpl.getType().getRawClass());
 }
}