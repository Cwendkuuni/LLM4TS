package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.base.ParserArgs;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class ValueInstantiatorTest {

 private static ObjectMapper mapper;

 @BeforeClass
 public static void setup() {
 mapper = new ObjectMapper();
 }

 @Test
 public void testDefaultCreator() throws Exception {
 ValueInstantiator instantiator = mapper.getFactory().getValueInstantiatorInstance();
 assertNull(instantiator.getDefaultCreator());
 }

 @Test
 public void testStringFromString() throws Exception {
 ValueInstantiator instantiator = new StdValueInstantiator(String.class);
 String json = "\"hello\"";
 Object result = instantiator.createFromString(mapper.getDeserializationContext(), json);
 assertEquals("hello", result);
 }

 @Test
 public void testIntFromInt() throws Exception {
 ValueInstantiator instantiator = new StdValueInstantiator(Integer.class);
 int json = 42;
 Object result = instantiator.createFromInt(mapper.getDeserializationContext(), json);
 assertEquals(Integer.valueOf(42), result);
 }

 @Test
 public void testLongFromLong() throws Exception {
 ValueInstantiator instantiator = new StdValueInstantiator(Long.class);
 long json = 42L;
 Object result = instantiator.createFromLong(mapper.getDeserializationContext(), json);
 assertEquals(Long.valueOf(42L), result);
 }

 @Test
 public void testDoubleFromDouble() throws Exception {
 ValueInstantiator instantiator = new StdValueInstantiator(Double.class);
 double json = 42.0;
 Object result = instantiator.createFromDouble(mapper.getDeserializationContext(), json);
 assertEquals(Double.valueOf(42.0), result);
 }

 @Test
 public void testBooleanFromBoolean() throws Exception {
 ValueInstantiator instantiator = new StdValueInstantiator(Boolean.class);
 boolean json = true;
 Object result = instantiator.createFromBoolean(mapper.getDeserializationContext(), json);
 assertEquals(Boolean.valueOf(true), result);
 }

 @Test
 public void testNullFromString() throws Exception {
 ValueInstantiator instantiator = new StdValueInstantiator(String.class);
 String json = "";
 Object result = instantiator.createFromString(mapper.getDeserializationContext(), json);
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testMissingCreator() throws Exception {
 ValueInstantiator instantiator = new ValueInstantiatorBase() {
 @Override
 public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
 return new SettableBeanProperty[0];
 }
 };
 String json = "{\"foo\":\"bar\"}";
 instantiator.createFromObjectWith(mapper.getDeserializationContext(), null);
 }

 @Test
 public void testCreateFromObjectWithNull() throws Exception {
 ValueInstantiator instantiator = new ValueInstantiatorBase() {
 @Override
 public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
 return null;
 }
 };
 Object result = instantiator.createFromObjectWith(mapper.getDeserializationContext(), null);
 assertNull(result);
 }

 @Test
 public void testCreateFromObjectWith() throws Exception {
 ValueInstantiator instantiator = new ValueInstantiatorBase() {
 @Override
 public SettableBeanProperty[] getFromObjectArguments(DeserializationConfig config) {
 return new SettableBeanProperty[] { config.findBeanProperty("foo") };
 }
 };
 String json = "{\"foo\":\"bar\"}";
 Object result = instantiator.createFromObjectWith(mapper.getDeserializationContext(), null);
 assertNotNull(result);
 }

 @Test
 public void testCreateUsingDefault() throws Exception {
 ValueInstantiator instantiator = new ValueInstantiatorBase() {
 @Override
 public boolean canCreateUsingDefault() {
 return true;
 }
 };
 DeserializationContext ctxt = mapper.getDeserializationContext();
 Object result = instantiator.createUsingDefault(ctxt);
 assertNotNull(result);
 }

 @Test
 public void testCreateUsingDelegate() throws Exception {
 ValueInstantiator instantiator = new ValueInstantiatorBase() {
 @Override
 public AnnotatedWithParams getDelegateCreator() {
 return mapper.getFactory().getAnnotatedClass(MyDelegate.class);
 }
 };
 Object json = "{\"delegate\":{\"foo\":\"bar\"}}";
 Object result = instantiator.createUsingDelegate(mapper.getDeserializationContext(), mapper.readValue(json, Map.class));
 assertEquals("bar", result);
 }
}

class MyDelegate {
 String foo;
}