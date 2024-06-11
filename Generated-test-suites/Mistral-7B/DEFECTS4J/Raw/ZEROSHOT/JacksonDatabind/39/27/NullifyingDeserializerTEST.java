package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.junit.Test;

/**
 * Unit tests for {@link NullifyingDeserializer} class.
 */
public class NullifyingDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserializeNullifyingDeserializer() throws Exception {
 JsonParser parser = MAPPER.getFactory().createParser("{}");
 StdDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserialize(parser, new DeserializationContext(MAPPER.getFactory()));
 assertNull(result);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithType() throws Exception {
 JsonParser parser = MAPPER.getFactory().createParser("[]");
 StdDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(parser, new DeserializationContext(MAPPER.getFactory()), null);
 assertNull(result);

 TypeFactory typeFactory = MAPPER.getTypeFactory();
 Object resultWithType = deserializer.deserializeWithType(parser, new DeserializationContext(MAPper.getFactory()), typeFactory.constructCollectionType(List.class, Integer.class));
 assertNull(resultWithType);
 }

 @Test
 public void testDeserializeNullifyingDeserializerWithComplexObject() throws Exception {
 String json = "{\"name\": \"John Doe\", \"age\": 30}";
 JsonParser parser = MAPPER.getFactory().createParser(json);
 StdDeserializer deserializer = NullifyingDeserializer.instance;
 Object result = deserializer.deserializeWithType(parser, new DeserializationContext(MAPPER.getFactory()), null);
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeNullifyingDeserializerWithInvalidToken() throws Exception {
 JsonParser parser = MAPPER.getFactory().createParser("123");
 StdDeserializer deserializer = NullifyingDeserializer.instance;
 deserializer.deserializeWithType(parser, new DeserializationContext(MAPPER.getFactory()), null);
 fail("Expected IOException but no exception thrown");
 }
}