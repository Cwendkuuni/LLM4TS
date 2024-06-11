package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TypeDeserializerBaseTest {

 private ObjectMapper objectMapper;

 @Before
 public void setup() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 }

 @Test
 public void testForProperty() throws Exception {
 // Given
 JavaType baseType = objectMapper.getTypeFactory().constructType(TypeDeserializerBase.class);
 TypeIdResolver idResolver = new StdTypeIdResolver();
 String typePropertyName = "type";
 boolean typeIdVisible = true;
 Class<?> defaultImpl = Object.class;
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 BeanProperty property = new BeanProperty("testProperty", String.class);
 TypeDeserializer expectedTypeDeserializer = new StringDeserializer(idResolver, null, null, null, null, null, null, null, null, null, null, null);
 TypeDeserializer actualTypeDeserializer = typeDeserializerBase.forProperty(property);

 // When

 // Then
 assertEquals(expectedTypeDeserializer, actualTypeDeserializer);
 }

 @Test
 public void testFindDeserializerWithValidTypeId() throws Exception {
 // Given
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseBase(null, null, null, false, null);
 String typeId = "com.example.TestClass";
 JsonDeserializer<Object> expectedDeserializer = new TestClassDeserializer();

 // When
 JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), typeId);

 // Then
 assertEquals(expectedDeserializer, actualDeserializer);
 }

 @Test(expected = JsonMappingException.class)
 public void testFindDeserializerWithUnknownTypeId() throws Exception {
 // Given
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseBase(null, null, null, false, null);
 String typeId = "com.unknown.UnknownClass";

 // When
 typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), typeId);

 // Then
 // Test passes if the expected exception is thrown
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 // Given
 ObjectMapper objectMapper = new ObjectMapper();
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseBase(null, null, null, false, Object.class);

 // When
 JsonDeserializer<Object> actualDeserializer = typeDeserializerBase._findDefaultImplDeserializer(objectMapper.getDeserializationContext());

 // Then
 assertNull(actualDeserializer);
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 // Given
 String json = "{\"type\":\"com.example.TestClass\"}";
 ObjectNode node = new ObjectMapper().readValue(json, ObjectNode.class);
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBaseBase(null, null, null, false, null);

 // When
 Object result = typeDeserializerBase._deserializeWithNativeTypeId(new JsonFactory(), typeDeserializerBase.getDeserializationContext(), "com.example.TestClass");

 // Then
 assertTrue(result instanceof TestClass);
 }
}

class TestClassDeserializer extends StdDeserializer<TestClass> {
 public TestClassDeserializer() { this(null); }

 protected TestClassDeserializer(Class<?> vc) { super(vc); }

 @Override
 public TestClass deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
 return new TestClass();
 }
}

class TypeDeserializerBaseBase extends TypeDeserializerBase {
 public TypeDeserializerBaseBase(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, Class<?> defaultImpl) {
 super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 }
}