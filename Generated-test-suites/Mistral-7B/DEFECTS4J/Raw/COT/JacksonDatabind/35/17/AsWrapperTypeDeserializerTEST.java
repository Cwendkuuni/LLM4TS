package com.example.aswrappertypedeserializertest;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.JsonParserSequence;

import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserializeTypedFromObject() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("TestClass"), new StandardTypeIdResolver(), "typeProperty", true, Object.class);
 Object actualResult = deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser("{\"type\":\"TestClass\"}"), new TestContext());
 assertEquals(TestClass.class, actualResult.getClass());
 }

 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("TestClass"), new StandardTypeIdResolver(), "typeProperty", true, Object.class);
 Object[] actualResult = deserializer.deserializeTypedFromArray(MAPper.getFactory().createParser("[\".TestClass\"]"), new TestContext());
 assertEquals(TestClass.class, actualResult[0].getClass());
 }

 @Test
 public void testDeserializeTypedFromScalar() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("TestClass"), new StandardTypeIdResolver(), "typeProperty", true, Object.class);
 Object actualResult = deserializer.deserializeTypedFromScalar(MAPper.getFactory().createParser("{\"type\":\"TestClass\"}"), new TestContext());
 assertEquals(TestClass.class, actualResult.getClass());
 }

 @Test
 public void testDeserializeTypedFromAny() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("TestClass"), new StandardTypeIdResolver(), "typeProperty", true, Object.class);
 Object actualResult = deserializer.deserializeTypedFromAny(MAPper.getFactory().createParser("{\"type\":\"TestClass\"}"), new TestContext());
 assertEquals(TestClass.class, actualResult.getClass());
 }

 @Test(expected = IOException.class)
 public void testDeserializeWrongJsonStructure() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("TestClass"), new StandardTypeIdResolver(), "typeProperty", true, Object.class);
 JsonParser jp = MAPper.getFactory().createParser("{\"type\":\"TestClass\",\"data\":\"test\"}");
 deserializer.deserializeTypedFromObject(jp, new TestContext());
 fail("Expected IOException but no exception was thrown");
 }

 @Test(expected = IOException.class)
 public void testDeserializeMissingTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("TestClass"), new StandardTypeIdResolver(), "typeProperty", true, Object.class);
 JsonParser jp = MAPper.getFactory().createParser("{\"data\":\"test\"}");
 deserializer.deserializeTypedFromObject(jp, new TestContext());
 fail("Expected IOException but no exception was thrown");
 }

 @Test(expected = IOException.class)
 public void testDeserializeInvalidTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("TestClass"), new StandardTypeIdResolver(), "typeProperty", true, Object.class);
 JsonParser jp = MAPper.getFactory().createParser("{\"type\":\"InvalidType\",\"data\":\"test\"}");
 deserializer.deserializeTypedFromObject(jp, new TestContext());
 fail("Expected IOException but no exception was thrown");
 }

 @Test(expected = JsonMappingException.class)
 public void testDeserializeMissingTypeProperty() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("TestClass"), new StandardTypeIdResolver(), null, true, Object.class);
 JsonParser jp = MAPper.getFactory().createParser("{\"type\":\"TestClass\",\"data\":\"test\"}");
 deserializer.deserializeTypedFromObject(jp, new TestContext());
 fail("Expected JsonMappingException but no exception was thrown");
 }

 @Test
 public void testDeserializeWithNativeTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
 new SimpleType("TestClass"), new StandardTypeIdResolver(), "typeProperty", true, Object.class);
 Object actualResult = deserializer.deserializeWithNativeTypeId(MAPper.getFactory().createParser("{\"@type\":\"TestClass\"}"), new TestContext());
 assertEquals(TestClass.class, actualResult.getClass());
 }

}

 class TestContext extends DeserializationContext {
 @Override
 public Object findValueInstantiator() {
 return new SimpleObjectInstancator();
 }
 }

 class TestClass {

 }