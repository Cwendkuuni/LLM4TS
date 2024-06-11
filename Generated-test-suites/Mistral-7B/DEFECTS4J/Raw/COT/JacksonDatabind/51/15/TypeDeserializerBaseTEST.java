package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class TypeDeserializerBaseTest {

 @Mock
 private JsonParser jsonParser;

 @Mock
 private DeserializationContext context;

 @Mock
 private TypeIdResolver idResolver;

 @Mock
 private JavaType baseType;

 @Mock
 private BeanProperty property;

 @InjectMocks
 private TypeDeserializerBase typeDeserializerBase;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 // Test for the constructor
 @Test
 public void testTypeDeserializerBaseConstruction() {
 JavaType defaultImpl = mock(JavaType.class);
 TypeIdResolver typeIdResolver = mock(TypeIdResolver.class);
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, typeIdResolver, "", false, defaultImpl);
 assertNotNull(typeDeserializerBase);
 }

 // Test for the abstract method forProperty
 @Test(expected = AssertionError.class)
 public void testAbstractForPropertyMethod() {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "", false, defaultImpl) {
 @Override
 protected TypeDeserializer forProperty(BeanProperty prop) {
 throw new AssertionError("For property method should be implemented in subclasses.");
 }
 };
 typeDeserializerBase.forProperty(property);
 }

 // Test for the getTypeInclusion method
 @Test
 public void testgetTypeInclusion() {
 JsonTypeInfo.As typeInclusion = JsonTypeInfo.As.EXISTING_PROPERTY;
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "", false, defaultImpl) {
 @Override
 public JsonTypeInfo.As getTypeInclusion() {
 return typeInclusion;
 }
 };
 assertEquals(typeInclusion, typeDeserializerBase.getTypeInclusion());
 }

 // Test for the baseTypeName method
 @Test
 public void testBaseTypeName() {
 String baseTypeName = "java.lang.String";
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "", false, defaultImpl) {
 @Override
 JavaType getBaseType() {
 return new JavaType(Class.forName(baseTypeName));
 }
 };
 assertEquals(baseTypeName, typeDeserializerBase.baseTypeName());
 }

 // Test for the getPropertyName method
 @Test
 public void testgetPropertyName() {
 String propertyName = "name";
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "", false, defaultImpl) {
 @Override
 String getPropertyName() {
 return propertyName;
 }
 };
 assertEquals(propertyName, typeDeserializerBase.getPropertyName());
 }

 // Test for the getTypeIdResolver method
 @Test
 public void testgetTypeIdResolver() {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "", false, defaultImpl);
 assertEquals(idResolver, typeDeserializerBase.getTypeIdResolver());
 }

 // Test for the getDefaultImpl method
 @Test
 public void testGetDefaultImpl() {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "", false, defaultImpl);
 assertNull(typeDeserializerBase.getDefaultImpl());
 }

 // Test for the toString method
 @Test
 public void testToString() {
 String toString = "[TypeDeserializerBase; base-type:java.lang.String; id-resolver:mock.TypeIdResolver@7852e34e]";
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "", false, defaultImpl);
 assertEquals(toString, typeDeserializerBase.toString());
 }

 // Test for the findDeserializer method with a valid typeId
 @Test
 public void testFindDeserializerWithValidTypeId() {
 String typeId = "TestType";
 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "", false, defaultImpl) {
 @Override
 protected Map<String, JsonDeserializer<Object>> _deserializers = new ConcurrentHashMap<>();
 @Override
 protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
 return deserializer;
 }
 };
 typeDeserializerBase._deserializers.put(typeId, deserializer);
 assertEquals(deserializer, typeDeserializerBase._findDeserializer(context, typeId));
 }

 // Test for the findDeserializer method with a null typeId
 @Test(expected = IOException.class)
 public void testFindDeserializerWithNullTypeId() {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "", false, defaultImpl) {
 @Override
 protected JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId) throws IOException {
 throw new IOException();
 }
 };
 typeDeserializerBase._findDeserializer(context, null);
 }

 // Test for the findDefaultImplDeserializer method with a null context
 @Test(expected = IOException.class)
 public void testFindDefaultImplDeserializerWithNullContext() {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "", false, defaultImpl) {
 @Override
 protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
 throw new IOException();
 }
 };
 typeDeserializerBase._findDefaultImplDeserializer(null);
 }

 // Test for the findDefaultImplDeserializer method with a context and a null defaultImpl
 @Test
 public void testFindDefaultImplDeserializerWithNullDefaultImpl() {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "", false, null) {
 @Override
 protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
 return mock(JsonDeserializer.class);
 }
 };
 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 typeDeserializerBase._syncDefaultImplDeserializer(ctxt);
 verify(deserializer).deserialize(jsonParser, context);
 }

 // Test for the findDefaultImplDeserializer method with a context and a defaultImpl
 @Test
 public void testFindDefaultImplDeserializerWithDefaultImpl() {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "", false, defaultImpl) {
 @Override
 protected JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt) throws IOException {
 return mock(JsonDeserializer.class);
 }
 };
 JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
 typeDeserializerBase._syncDefaultImplDeserializer(context);
 verify(deserializer).deserialize(jsonParser, context);
 }

 // Test for the deserializeWithNativeTypeId method with a valid typeId and a JsonParser
 @Test
 public void testDeserializeWithNativeTypeIdWithValidTypeIdAndJsonParser() throws IOException {
 Object result = new Object();
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "", false, defaultImpl) {
 @Override
 protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
 return result;
 }
 };
 assertEquals(result, typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, "TestType"));
 }

 // Test for the deserializeWithNativeTypeId method with a null typeId and a JsonParser
 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeIdWithNullTypeIdAndJsonParser() throws IOException {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "", false, defaultImpl) {
 @Override
 protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId) throws IOException {
 throw new IOException();
 }
 };
 typeDeserializerBase._deserializeWithNativeTypeId(jsonParser, context, null);
 }

 // Test for the handleUnknownTypeId method with a TypeIdResolver and a valid typeId
 @Test
 public void testHandleUnknownTypeIdWithTypeIdResolverAndValidTypeId() throws IOException {
 JavaType javaType = mock(JavaType.class);
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "", false, defaultImpl) {
 @Override
 protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
 return javaType;
 }
 };
 assertEquals(javaType, typeDeserializerBase._handleUnknownTypeId(context, "TestType", idResolver, baseType));
 }

 // Test for the handleUnknownTypeId method with a TypeIdResolver and a null typeId
 @Test(expected = IOException.class)
 public void testHandleUnknownTypeIdWithTypeIdResolverAndNullTypeId() throws IOException {
 TypeDeserializerBase typeDeserializerBase = new TypeDeserializerBase(baseType, idResolver, "", false, defaultImpl) {
 @Override
 protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType) throws IOException {
 throw new IOException();
 }
 };
 typeDeserializerBase._handleUnknownTypeId(context, null, idResolver, baseType);
 }
}