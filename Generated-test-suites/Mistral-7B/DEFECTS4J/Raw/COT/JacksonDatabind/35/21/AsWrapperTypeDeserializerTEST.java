package com.fasterxml.jackson.databind.jsontype.impl.test;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Test;
import static org.junit.Assert.*;

public class AsWrapperTypeDeserializerTest {

 private static final JavaType STRING_TYPE = TypeFactory.defaultInstance().constructType(String.class);
 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserializeTypedFromObject() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(STRING_TYPE, new TypeIdResolver() {
 @Override
 public IdResolver getIdResolver(DeserializationConfig config) {
 return null;
 }
 }, "type", true, String.class);
 Object result = deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser("{\"type\":\"String\",\"value\":\"Hello World\"}"), new TestContext());
 assertEquals("Hello World", result);
 }

 @Test
 public void testDeserializeTypedFromArray() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(STRING_TYPE, new TypeIdResolver() {
 @Override
 public IdResolver getIdResolver(DeserializationConfig config) {
 return null;
 }
 }, "type", true, String.class);
 Object result = deserializer.deserializeTypedFromArray(MAPPER.getFactory().createParser("[\"String\",{\"value\":\"Hello World\"}]"), new TestContext());
 assertEquals("Hello World", result);
 }

 @Test
 public void testDeserializeTypedFromScalar() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(STRING_TYPE, new TypeIdResolver() {
 @Override
 public IdResolver getIdResolver(DeserializationConfig config) {
 return null;
 }
 }, "type", true, String.class);
 Object result = deserializer.deserializeTypedFromScalar(MAPPER.getFactory().createParser("\"String\""), new TestContext());
 assertEquals("", result);
 }

 @Test
 public void testDeserializeTypedFromAny() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(STRING_TYPE, new TypeIdResolver() {
 @Override
 public IdResolver getIdResolver(DeserializationConfig config) {
 return null;
 }
 }, "type", true, String.class);
 Object result = deserializer.deserializeTypedFromAny(MAPPER.getFactory().createParser("{\"type\":\"String\",\"value\":\"Hello World\"}"), new TestContext());
 assertEquals("Hello World", result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidJsonStructure() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(STRING_TYPE, new TypeIdResolver() {
 @Override
 public IdResolver getIdResolver(DeserializationConfig config) {
 return null;
 }
 }, "type", true, String.class);
 Object result = deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser("{\"value\":\"Hello World\"}"), new TestContext());
 fail("Expected IOException but received null");
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithInvalidTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(STRING_TYPE, new TypeIdResolver() {
 @Override
 public IdResolver getIdResolver(DeserializationConfig config) {
 return null;
 }
 }, "type", true, String.class);
 Object result = deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser("{\"type\":\"InvalidType\",\"value\":\"Hello World\"}"), new TestContext());
 fail("Expected IOException but received null");
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithNullTypeId() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(STRING_TYPE, new TypeIdResolver() {
 @Override
 public IdResolver getIdResolver(DeserializationConfig config) {
 return null;
 }
 }, "type", true, String.class);
 Object result = deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser("{\"type\":null,\"value\":\"Hello World\"}"), new TestContext());
 fail("Expected IOException but received null");
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithMissingEndObject() throws Exception {
 AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(STRING_TYPE, new TypeIdResolver() {
 @Override
 public IdResolver getIdResolver(DeserializationConfig config) {
 return null;
 }
 }, "type", true, String.class);
 Object result = deserializer.deserializeTypedFromObject(MAPPER.getFactory().createParser("{\"type\":\"String\",\"value\":\"Hello World\"}"), new TestContext());
 fail("Expected IOException but received a value");
 }
}

 class TestContext extends DeserializationContext {
 @Override
 public ObjectReader getRootValueFactory() {
 return MAPPER.getFactory();
 }

 @Override
 public DeserializationConfig getConfig() {
 return MAPPER.getConfiguration();
 }
 }