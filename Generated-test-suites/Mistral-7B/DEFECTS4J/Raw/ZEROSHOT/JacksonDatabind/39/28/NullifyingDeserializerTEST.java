package com.fasterxml.jackson.databind.deser.std;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerTestBase;

public class NullifyingDeserializerTest extends TypeDeserializerTestBase {

 private static final NullifyingDeserializer NULLIFIER = NullifyingDeserializer.instance;

 @Override
 protected TypeDeserializer getDeserializerUnderTest() {
 return NULLIFIER;
 }

 public void testDeserializeNull() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonFactory factory = mapper.getFactory();
 JsonParser parser = factory.createParser("[]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(null, deserializeTyped(parser, ctxt, String.class));
 assertEquals(null, deserializeTyped(parser, ctxt, Integer.class));
 }

 public void testDeserializeNullWithTypeDeserializer() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonFactory factory = mapper.getFactory();
 ObjectMapper mapperWithTypeDeserializer = new ObjectMapper(new TypeFactory());
 JsonParser parser = factory.createParser("[]");
 DeserializationContext ctxt = mapperWithTypeDeserializer.getDeserializationContext();
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(String.class);
 assertEquals(null, deserializeTypedWithTypeDeserializer(parser, ctxt, typeDeserializer));
 assertEquals(null, deserializeTypedWithTypeDeserializer(parser, ctxt, new ArrayType(Integer.class)));
 }

 public void testDeserializeWithArrayToken() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonFactory factory = mapper.getFactory();
 JsonParser parser = factory.createParser("[1,2,3]");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(null, deserializeTyped(parser, ctxt, String.class));
 }

 public void testDeserializeWithStartObjectToken() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonFactory factory = mapper.getFactory();
 JsonParser parser = factory.createParser("{\"field\":1}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(null, deserializeTyped(parser, ctxt, String.class));
 }

 public void testDeserializeWithFieldNameToken() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonFactory factory = mapper.getFactory();
 JsonParser parser = factory.createParser("{\"field\":1}");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueTypeDeserializer(String.class);
 assertEquals(null, deserializeTypedWithTypeDeserializer(parser, ctxt, typeDeserializer));
 }

 public void testDeserializeWithOtherToken() throws Exception {
 ObjectMapper mapper = new ObjectMapper();
 JsonFactory factory = mapper.getFactory();
 JsonParser parser = factory.createParser("123");
 DeserializationContext ctxt = mapper.getDeserializationContext();
 assertEquals(null, deserializeTyped(parser, ctxt, String.class));
 }

}