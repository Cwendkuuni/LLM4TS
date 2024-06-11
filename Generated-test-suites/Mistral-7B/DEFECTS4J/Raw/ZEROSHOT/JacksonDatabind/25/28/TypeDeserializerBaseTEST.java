package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Test;

public class TypeDeserializerBaseTest {

 private static final JavaType baseType = Object.class;
 private static final TypeIdResolver idResolver = new StubTypeIdResolver();
 private static final String typePropertyName = "typeProperty";
 private static final boolean typeIdVisible = true;
 private static final Class<?> defaultImpl = String.class;

 @Test
 public void testTypeDeserializerBaseSerialization() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new StubTypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 String json = "{\"typeProperty\":\"StringType\"}";
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(DeserializationFeature.FAIL_ON_EMPTY_BEANS, false);
 ObjectReader reader = mapper.readerForClass(StubTypeDeserializerBase.class);
 StubTypeDeserializerBase deserializedObject = reader.readValue(new StringReader(json));
 assertEquals(typeDeserializerBase, deserializedObject);
 }

 @Test
 public void testForPropertyMethod() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new StubTypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 BeanProperty property = new BeanProperty("propertyName", String.class);
 TypeDeserializerBase forProperty = typeDeserializerBase.forProperty(property);
 assertEquals(StubTypeDeserializerBase.class, forProperty.getClass());
 assertEquals(property, forProperty._property);
 }

 @Test
 public void testGetTypeInclusion() {
 TypeDeserializerBase typeDeserializerBase = new StubTypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 assertEquals(JsonTypeInfo.As.EXISTING_PROPERTY, typeDeserializerBase.getTypeInclusion());
 }

 @Test
 public void testBaseTypeName() {
 TypeDeserializerBase typeDeserializerBase = new StubTypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 assertEquals(baseType.getRawClass().getName(), typeDeserializerBase.baseTypeName());
 }

 @Test
 public void testGetPropertyName() {
 TypeDeserializerBase typeDeserializerBase = new StubTypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 assertEquals(typePropertyName, typeDeserializerBase.getPropertyName());
 }

 @Test
 public void testGetTypeIdResolver() {
 TypeDeserializerBase typeDeserializerBase = new StubTypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
 }

 @Test
 public void testGetDefaultImpl() {
 TypeDeserializerBase typeDeserializerBase = new StubTypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 assertEquals(defaultImpl == null ? null : defaultImpl.getName(), typeDeserializerBase.getDefaultImpl() == null ? null : typeDeserializerBase.getDefaultImpl().getName());
 }

 @Test
 public void testToString() {
 TypeDeserializerBase typeDeserializerBase = new StubTypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 String expected = "[com.fasterxml.jackson.databind.jsontype.impl.StubTypeDeserializerBase; base-type:class java.lang.Object; id-resolver:com.fasterxml.jackson.databind.jsontype.StubTypeIdResolver@1b9d65b";
 assertEquals(expected, typeDeserializerBase.toString());
 }

 @Test
 public void testFindDeserializer() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new StubTypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 DeserializationContext ctxt = new TestDeserializationContext();
 String typeId = "StringType";
 JsonDeserializer<Object> deserializedObject = typeDeserializerBase._findDeserializer(ctxt, typeId);
 assertEquals(StringDeserializer.class, deserializedObject.getClass());
 }

 @Test(expected = IOException.class)
 public void testFindDeserializerUnknownTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new StubTypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 DeserializationContext ctxt = new TestDeserializationContext();
 String typeId = "UnknownType";
 JsonDeserializer<Object> deserializedObject = typeDeserializerBase._findDeserializer(ctxt, typeId);
 fail("Should throw IOException");
 }

 @Test
 public void testFindDefaultImplDeserializer() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new StubTypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 DeserializationContext ctxt = new TestDeserializationContext();
 JsonDeserializer<Object> deserializedObject = typeDeserializerBase._findDefaultImplDeserializer(ctxt);
 assertEquals(NullifyingDeserializer.instance.getClass(), deserializedObject.getClass());
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new StubTypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 JsonParser jp = new TestJsonParser();
 jp.setTypeId("StringType");
 Object deserializedObject = typeDeserializerBase._deserializeWithNativeTypeId(jp, new TestDeserializationContext());
 assertEquals(String.class, deserializedObject.getClass());
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeIdUnknownTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new StubTypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 JsonParser jp = new TestJsonParser();
 jp.setTypeId("UnknownType");
 typeDeserializerBase._deserializeWithNativeTypeId(jp, new TestDeserializationContext());
 fail("Should throw IOException");
 }

 @Test
 public void testHandleUnknownTypeId() throws Exception {
 TypeDeserializerBase typeDeserializerBase = new StubTypeDeserializerBase(baseType, idResolver, typePropertyName, typeIdVisible, defaultImpl);
 DeserializationContext ctxt = new TestDeserializationContext();
 String typeId = "UnknownType";
 Object deserializedObject = typeDeserializerBase._handleUnknownTypeId(ctxt, typeId, idResolver, baseType);
 fail("Should throw IOException");
 }

 private static class StubTypeDeserializerBase extends TypeDeserializerBase {
 @Override
 protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
 return super._findDeserializer(ctxt, typeId);
 }

 @Override
 protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
 return super._findDefaultImplDeserializer(ctxt);
 }

 @Override
 protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
 return super._deserializeWithNativeTypeId(jp, ctxt, typeId);
 }

 @Override
 protected Object _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
 throw new IOException("Should not be reached");
 }
 }

 private static class StubTypeIdResolver implements TypeIdResolver {
 @Override
 public JavaType typeFromId(DeserializationContext ctxt, String typeId) throws IOException {
 return ctxt.constructType((Class<?>) defaultImpl.getClass());
 }
 }

 private static class TestDeserializationContext extends DeserializationContext {
 @Override
 public JavaType findTypeById(String typeId) throws IOException, JsonMappingException {
 return null;
 }

 @Override
 public void findAndBindValueDeserializer(DeserializerProvider provider, JavaType type, BeanProperty property) throws IOException {
 }
 }

 private static class TestJsonParser extends JsonParser {
 private String typeId;

 @Override
 public void nextToken() throws IOException {
 }

 public void setTypeId(String typeId) {
 this.typeId = typeId;
 }
 }
}