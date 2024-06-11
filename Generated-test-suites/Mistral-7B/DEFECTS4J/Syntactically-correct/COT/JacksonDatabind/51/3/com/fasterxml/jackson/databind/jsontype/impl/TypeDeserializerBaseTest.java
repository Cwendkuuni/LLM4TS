package com.example.junit.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolverBase;

public class TypeDeserializerBaseTest {

 private TypeDeserializerBase typeDeserializerBase;
 private ObjectMapper objectMapper;
 private TypeIdResolverBase typeIdResolver;

 @Before
 public void setup() {
 typeIdResolver = new TypeIdResolverBase();
 objectMapper = new ObjectMapper();
 objectMapper.setDeserializationFeature(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 objectMapper.setSerializationFeature(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 typeDeserializerBase = new TestTypeDeserializerBase(null, typeIdResolver, "", false, null);
 }

 @Test
 public void testForProperty() throws IOException {
 BeanProperty property = new BeanProperty(null, "testProperty");
 TypeDeserializer forProperty = typeDeserializerBase.forProperty(property);
 assertNotNull("forProperty should not be null", forProperty);
 }

 @Test(expected = NullPointerException.class)
 public void testForPropertyNullProperty() {
 BeanProperty property = null;
 typeDeserializerBase.forProperty(property);
 }

 @Test
 public void testGetTypeInclusion() {
 assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeserializerBase.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() {
 String baseTypeName = typeDeserializerBase.baseTypeName();
 assertNotNull("baseTypeName should not be null", baseTypeName);
 }

 @Test
 public void testGetPropertyName() {
 String propertyName = typeDeserializerBase.getPropertyName();
 assertEquals("", propertyName);
 }

 @Test
 public void testGetTypeIdResolver() {
 TypeIdResolver idResolver = typeDeserializerBase.getTypeIdResolver();
 assertEquals(typeIdResolver, idResolver);
 }

 @Test
 public void testGetDefaultImpl() {
 Class<?> defaultImpl = typeDeserializerBase.getDefaultImpl();
 assertNull("defaultImpl should be null", defaultImpl);
 }

 @Test
 public void testToString() throws IOException {
 String toString = typeDeserializerBase.toString();
 assertNotNull("toString should not be null", toString);
 }

 @Test
 public void testFindDeserializerNull() throws IOException {
 String typeId = null;
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), typeId);
 assertNull("deserializer should be null", deserializer);
 }

 @Test
 public void testFindDeserializerEmptyTypeId() throws IOException {
 String typeId = "";
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), typeId);
 assertNull("deserializer should be null", deserializer);
 }

 @Test
 public void testFindDeserializerTypeId() throws IOException {
 String typeId = "TestTypeId";
 JavaType javaType = objectMapper.getTypeFactory().constructType(String.class);
 JsonDeserializer<Object> deserializer = typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), typeId);
 assertNotNull("deserializer should not be null", deserializer);
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerNullTypeId() throws IOException {
 String typeId = null;
 typeDeserializerBase._findDeserializer(objectMapper.getDeserializationContext(), typeId);
 }

 @Test
 public void testFindDefaultImplDeserializerNull() throws IOException {
 JsonDeserializer<Object> defaultImplDeserializer = typeDeserializerBase._findDefaultImplDeserializer(objectMapper.getDeserializationContext());
 assertNull("defaultImplDeserializer should be null", defaultImplDeserializer);
 }

 @Test
 public void testFindDefaultImplDeserializerNotNull() throws IOException {
 JsonDeserializer<Object> defaultImplDeserializer = typeDeserializerBase._findDefaultImplDeserializer(objectMapper.getDeserializationContext());
 assertNotNull("defaultImplDeserializer should not be null", defaultImplDeserializer);
 }

 @Test
 public void testHandleUnknownTypeIdNullTypeId() throws IOException {
 JavaType result = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), null, typeIdResolver, null);
 assertNull("result should be null", result);
 }

 @Test(expected = IOException.class)
 public void testHandleUnknownTypeIdNullTypeIdResolver() throws IOException {
 JavaType result = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), "", null, null);
 assertTrue("result should be instance of Map", result instanceof Map);
 }

 @Test
 public void testHandleUnknownTypeIdEmptyTypeId() throws IOException {
 JavaType result = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), "", typeIdResolver, null);
 assertNull("result should be null", result);
 }

 @Test
 public void testHandleUnknownTypeIdTypeId() throws IOException {
 JavaType result = typeDeserializerBase._handleUnknownTypeId(objectMapper.getDeserializationContext(), "TestTypeId", typeIdResolver, null);
 assertNotNull("result should not be null", result);
 }

}

class TestTypeDeserializerBase extends TypeDeserializerBase {
 TestTypeDeserializerBase(JavaType baseType, TypeIdResolver idResolver, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl) {
 super(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 }
}