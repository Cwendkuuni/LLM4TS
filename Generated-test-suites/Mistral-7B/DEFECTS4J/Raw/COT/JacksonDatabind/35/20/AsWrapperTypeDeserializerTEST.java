package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

 private static final ObjectMapper mapper = new ObjectMapper();

 @Test
 public void testConstructor() {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 assertNotNull(deserializer);
 }

 @Test
 public void testForProperty() throws Exception {
 JavaType baseType = mapper.getTypeFactory().constructType(String.class);
 TypeIdResolver idResolver = mapper.getTypeIdResolver();
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(baseType, idResolver, null, false, null);
 BeanProperty property = new BeanProperty("testProperty", baseType, null);
 AsWrapperTypeDeserializer deserializerForProperty = deserializer.forProperty(property);
 assertEquals(deserializer, deserializerForProperty);
 }

 @Test
 public void testGetTypeInclusion() {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 assertEquals(As.WRAPPER_OBJECT, deserializer.getTypeInclusion());
 }

 @Test
 public void testDeserializeTypedFromObject_NullInputs() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser jp = null;
 DeserializationContext ctxt = null;
 try {
 deserializer.deserializeTypedFromObject(jp, ctxt);
 fail("Expected IOException");
 } catch (IOException e) {
 // Expected
 }

 jp = mapper.getFactory().createParser("[{}]");
 ctxt = mapper.getDeserializationContext();
 try {
 deserializer.deserializeTypedFromObject(null, ctxt);
 fail("Expected IOException");
 } catch (IOException e) {
 // Expected
 }

 jp.close();
 ctxt.close();
}

 @Test
 public void testDeserializeTypedFromObject_ValidInputs() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(String.class), mapper.getTypeIdResolver(), null, false, String.class);
 JsonParser jp = mapper.getFactory().createParser("{\"typeId\":\"java.lang.String\",\"value\":\"test\"}");
 Object result = deserializer.deserializeTypedFromObject(jp, mapper.getDeserializationContext());
 assertEquals("test", result);
 jp.close();
 }

 @Test
 public void testDeserializeTypedFromArray_NullInputs() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser jp = null;
 DeserializationContext ctxt = null;
 try {
 deserializer.deserializeTypedFromArray(jp, ctxt);
 fail("Expected IOException");
 } catch (IOException e) {
 // Expected
 }

 jp = mapper.getFactory().createParser("[{}]");
 ctxt = mapper.getDeserializationContext();
 try {
 deserializer.deserializeTypedFromArray(null, ctxt);
 fail("Expected IOException");
 } catch (IOException e) {
 // Expected
 }

 jp.close();
 ctxt.close();
}

 @Test
 public void testDeserializeTypedFromArray_ValidInputs() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(List.class), mapper.getTypeIdResolver(), null, false, ArrayList.class);
 JsonParser jp = mapper.getFactory().createParser("[{\"typeId\":\"java.util.ArrayList\",\"value\":[1,2,3]}]");
 Object result = deserializer.deserializeTypedFromArray(jp, mapper.getDeserializationContext());
 List<?> list = (List<?>) result;
 assertEquals(3, list.size());
 assertEquals(1, list.get(0));
 assertEquals(2, list.get(1));
 assertEquals(3, list.get(2));
 jp.close();
 }

 @Test
 public void testDeserializeTypemFromScalar_NullInputs() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser jp = null;
 DeserializationContext ctxt = null;
 try {
 deserializer.deserializeTypedFromScalar(jp, ctxt);
 fail("Expected IOException");
 } catch (IOException e) {
 // Expected
 }

 jp = mapper.getFactory().createParser("{}");
 ctxt = mapper.getDeserializationContext();
 try {
 deserializer.deserializeTypedFromScalar(null, ctxt);
 fail("Expected IOException");
 } catch (IOException e) {
 // Expected
 }

 jp.close();
 ctxt.close();
}

 @Test
 public void testDeserializeTypemFromScalar_ValidInputs() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(Integer.class), mapper.getTypeIdResolver(), null, false, null);
 JsonParser jp = mapper.getFactory().createParser("{\"typeId\":\"java.lang.Integer\",\"value\":42}");
 Object result = deserializer.deserializeTypedFromScalar(jp, mapper.getDeserializationContext());
 assertEquals(42, result);
 jp.close();
 }

 @Test
 public void testDeserializeTypedFromAny_NullInputs() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser jp = null;
 DeserializationContext ctxt = null;
 try {
 deserializer.deserializeTypedFromAny(jp, ctxt);
 fail("Expected IOException");
 } catch (IOException e) {
 // Expected
 }

 jp = mapper.getFactory().createParser("{}");
 ctxt = mapper.getDeserializationContext();
 try {
 deserializer.deserializeTypedFromAny(null, ctxt);
 fail("Expected IOException");
 } catch (IOException e) {
 // Expected
 }

 jp.close();
 ctxt.close();
}

 @Test
 public void testDeserializeTypedFromAny_ValidInputs() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(Integer.class), mapper.getTypeIdResolver(), null, false, null);
 JsonParser jp = mapper.getFactory().createParser("{\"typeId\":\"java.lang.Integer\",\"value\":42}");
 Object result = deserializer.deserializeTypedFromAny(jp, mapper.getDeserializationContext());
 assertEquals(42, result);
 jp.close();

 jp = mapper.getFactory().createParser("{\"typeId\":\"com.test.CustomType\",\"value\":{\"name\":\"Test\"}}");
 Object result2 = deserializer.deserializeTypedFromAny(jp, mapper.getDeserializationContext());
 assertNotNull(result2);
 jp.close();
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeId_NullInputs() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(null, null, null, false, null);
 JsonParser jp = null;
 DeserializationContext ctxt = null;
 deserializer._deserializeWithNativeTypeId(jp, ctxt, null);
 fail("Expected IOException");
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeId_InvalidTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(String.class), mapper.getTypeIdResolver(), null, false, null);
 JsonParser jp = mapper.getFactory().createParser("{\"typeId\":\"invalidTypeId\",\"value\":\"test\"}");
 deserializer._deserializeWithNativeTypeId(jp, mapper.getDeserializationContext(), "invalidTypeId");
 fail("Expected IOException");
 jp.close();
 }

 @Test
 public void testDeserializeWithNativeTypeId_ValidInputs() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(String.class), mapper.getTypeIdResolver(), null, false, null);
 JsonParser jp = mapper.getFactory().createParser("{\"typeId\":\"java.lang.String\",\"value\":\"test\"}");
 Object result = deserializer._deserializeWithNativeTypeId(jp, mapper.getDeserializationContext(), "java.lang.String");
 assertEquals("test", result);
 jp.close();
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNativeTypeId_InvalidToken() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(mapper.getTypeFactory().constructType(String.class), mapper.getTypeIdResolver(), null, false, null);
 JsonParser jp = mapper.getFactory().createParser("{\"typeId\":123,\"value\":\"test\"}");
 deserializer._deserializeWithNativeTypeId(jp, mapper.getDeserializationContext(), "java.lang.String");
 fail("Expected IOException");
 jp.close();
 }
}