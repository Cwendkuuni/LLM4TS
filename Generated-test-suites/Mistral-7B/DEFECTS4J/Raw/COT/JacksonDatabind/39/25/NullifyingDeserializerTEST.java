package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;

/**
 * Test class for NullifyingDeserializer.
 */
public class NullifyingDeserializerTest {

 private static final ObjectMapper MAPPER = new ObjectMapper();

 @Test
 public void testDeserialize() throws Exception {
 Object result = new NullifyingDeserializer().deserialize(MAPPER.getFactory().createParser("{}"), MAPper.getDeserializationContext());
 assertNull(result);
 }

 @Test
 public void testDeserializeWithType() throws Exception {
 JsonParser parser = MAPper.getFactory().createParser("[]");
 TypeDeserializer typeDeserializer = new StdTypeDeserializer();
 Object result = new NullifyingDeserializer().deserializeWithType(parser, MAPper.getDeserializationContext(), typeDeserializer);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithIdStartArray() throws Exception {
 JsonParser parser = MAPper.getFactory().createParser("[]");
 Object result = new NullifyingDeserializer().deserializeWithType(parser, MAPper.getDeserializationContext(), null);
 assertNull(result);
 }

 @Test
 public void testDeserializeWithIdStartObject() throws Exception {
 JsonParser parser = MAPper.getFactory().createParser("{}");
 Object result = new NullifyingDeserializer().deserializeWithType(parser, MAPper.getDeserializationContext(), null);
 assertNull(result);
 }

 @Test(expected = IOException.class)
 public void testDeserializeWithIdFieldName() throws Exception {
 JsonParser parser = MAPper.getFactory().createParser("{\"name\":1}");
 Object result = new NullifyingDeserializer().deserializeWithType(parser, MAPper.getDeserializationContext(), null);
 fail("Expected IOException but got null.");
 }

}