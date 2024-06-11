package com.fasterxml.jackson.dataformat.xml.ser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlSerializerProviderTest {

 private ObjectMapper objectMapper;

 @Override
 public void setUp() {
 objectMapper = new ObjectMapper();
 objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 objectMapper.setSerializerProvider(new XmlSerializerProvider(new XmlRootNameLookup() {}));
 }

 @Test
 public void testSerializeNull() throws JsonProcessingException {
 ObjectNode expected = objectMapper.createObjectNode();
 expected.put("null", null);
 JsonNode actual = objectMapper.writeValueAsTree(null);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testSerializePrimitiveTypes() throws JsonProcessingException {
 ObjectNode expected = objectMapper.createObjectNode();
 expected.put("int", 1);
 expected.put("long", 1L);
 expected.put("boolean", true);
 expected.put("float", 1.2F);
 expected.put("double", 1.2);
 JsonNode actual = objectMapper.writeValueAsTree(new Object() {
 @Override
 public int intValue() {
 return 1;
 }

 @Override
 public long longValue() {
 return 1L;
 }

 @Override
 public boolean booleanValue() {
 return true;
 }

 @Override
 public float floatValue() {
 return 1.2F;
 }

 @Override
 public double doubleValue() {
 return 1.2;
 }
 });
 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testSerializeCustomClass() throws JsonProcessingException {
 ObjectNode expected = objectMapper.createObjectNode();
 expected.set("name", "value");
 JsonNode actual = objectMapper.writeValueAsTree(new CustomClass("value"));
 assertEquals(expected.toString(), actual.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNullRootName() throws JsonProcessingException {
 objectMapper.setSerializerProvider(new XmlSerializerProvider(null));
 ObjectNode expected = objectMapper.createObjectNode();
 expected.put("null", null);
 JsonNode actual = objectMapper.writeValueAsTree(null);
 assertEquals(expected.toString(), actual.toString());
 }

 @Test(expected = JsonMappingException.class)
 public void testSerializeNonRootElement() throws JsonProcessingException {
 ObjectMapper mapper = new ObjectMapper();
 mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
 mapper.setSerializerProvider(new XmlSerializerProvider(null));
 ObjectNode expected = mapper.createObjectNode();
 expected.put("name", "value");
 JsonNode actual = mapper.writeValueAsTree(new ObjectNode());
 assertEquals(expected.toString(), actual.toString());
 }
}

class CustomClass {
 private final String value;

 CustomClass(String value) {
 this.value = value;
 }

 @Override
 public String toString() {
 return "CustomClass{" +
 "value='" + value + '\'' +
 '}';
 }
}